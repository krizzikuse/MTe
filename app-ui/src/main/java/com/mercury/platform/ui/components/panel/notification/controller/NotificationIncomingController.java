package com.mercury.platform.ui.components.panel.notification.controller;

import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.entity.message.CurrencyTradeNotificationDescriptor;
import com.mercury.platform.shared.entity.message.ItemTradeNotificationDescriptor;
import com.mercury.platform.shared.entity.message.MercuryError;
import com.mercury.platform.shared.entity.message.NotificationDescriptor;
import com.mercury.platform.shared.entity.message.TradeNotificationDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.components.panel.notification.CurrencyTradeIncNotificationPanel;
import com.mercury.platform.ui.components.panel.notification.ItemTradeIncNotificationPanel;
import com.mercury.platform.ui.components.panel.notification.priceverifier.PoEdotcomCurrencyPriceVerifier;
import com.mercury.platform.ui.components.panel.notification.priceverifier.PoEdotcomPriceVerifier;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import currencydata.CurrencyData;
import poedotcom.PoEdotcomQueryHandler;
import lombok.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Data;
import org.apache.logging.log4j.Level;
import poedotcom.OfferVsListingData;
import poedotcom.PoEdotcomOffers;
import poedotcom.basictypes.Enums;
import poedotcom.offer.PoEdotcomOffer;
import priceverifier.POETradeCurrencyData;
import priceverifier.TabInfos;
import tradehelpers.PoeTradeDefaults;

//todo proxy
public class NotificationIncomingController implements IncomingPanelController {
    private static final Logger log = LogManager.getLogger(NotificationIncomingController.class);
    private NotificationDescriptor notificationDescriptor;

    public NotificationIncomingController(NotificationDescriptor notificationDescriptor) {
        this.notificationDescriptor = notificationDescriptor;
    }

    @Override
    public void performInvite() {
        MercuryStoreCore.chatCommandSubject.onNext("/invite " + notificationDescriptor.getWhisperNickname());
        showITH();
    }

    @Override
    public void performKick() {
        MercuryStoreCore.chatCommandSubject.onNext("/kick " + notificationDescriptor.getWhisperNickname());
    }

    @Override
    public void performOfferTrade() {
        MercuryStoreCore.chatCommandSubject.onNext("/tradewith " + notificationDescriptor.getWhisperNickname());
    }

    @Override
    public void performOpenChat() {
        MercuryStoreCore.openChatSubject.onNext(notificationDescriptor.getWhisperNickname());
    }

    @Override
    public void performResponse(@NonNull String responseText) {
        MercuryStoreCore.chatCommandSubject.onNext("@" + notificationDescriptor.getWhisperNickname() + " " + responseText);
    }

    @Override
    public void performHide() {
        this.closeMessagePanel();
    }

    @Override
    public void showITH() {
        if (notificationDescriptor instanceof ItemTradeNotificationDescriptor) {
            this.copyItemNameToClipboard(((ItemTradeNotificationDescriptor) notificationDescriptor).getItemName());
            if (((ItemTradeNotificationDescriptor) notificationDescriptor).getTabName() != null) {
                MercuryStoreUI.showItemGridSubject.onNext((ItemTradeNotificationDescriptor) notificationDescriptor);
            }
        }
    }

    private void copyItemNameToClipboard(@NonNull String itemName) {
        Timer timer = new Timer(30, action -> {
            try {
                StringSelection selection = new StringSelection(itemName);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, null);
            } catch (IllegalStateException e) {
                MercuryStoreCore.errorHandlerSubject.onNext(new MercuryError(e));
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    public void doPriceCheck(CurrencyTradeIncNotificationPanel tradeNotification) {
        PoEdotcomCurrencyPriceVerifier pv = new PoEdotcomCurrencyPriceVerifier(
                notificationDescriptor, tradeNotification);
        Thread pvT = new Thread(pv);
        pvT.start();        
    }
    
    @Override
    public void doPriceCheck(ItemTradeIncNotificationPanel tradeNotification) {
//        PoEdotcomPriceVerifier pvT = new PoEdotcomPriceVerifier(notificationDescriptor, tradeNotification);
//        pvT.run();
        PoEdotcomPriceVerifier pv = new PoEdotcomPriceVerifier(notificationDescriptor, tradeNotification);
        Thread pvT = new Thread(pv);
        pvT.start();
//          SwingUtilities.invokeLater(new ItemTradeIncNotificationPanel.PriceVerificationResult());
    }
    
    @Override
    public OfferVsListingData doPriceCheck_old() {
        try {
            //poedotcom.PoEdotcomQueryHandler a = new poedotcom.PoEdotcomQueryHandler();
            PoEdotcomQueryHandler poecomqueryhandler = new PoEdotcomQueryHandler();
            String league = ((TradeNotificationDescriptor)notificationDescriptor).getLeague();
            //String playerNickName = Configuration.get().notificationConfiguration().get().getPlayerNickname();  //TODO need a new field for accoutnname!
            String accountName = Configuration.get().notificationConfiguration().get().getAccountName();  //TODO need a new field for accoutnname!
            String itemname = ((ItemTradeNotificationDescriptor) notificationDescriptor).getItemName();
            String stash = ((ItemTradeNotificationDescriptor) notificationDescriptor).getTabName();
            int stashX = ((ItemTradeNotificationDescriptor) notificationDescriptor).getLeft();
            int stashY = ((ItemTradeNotificationDescriptor) notificationDescriptor).getTop();
            //poecomqueryhandler.getOffers("Betrayal",Enums.QueryType.currency,"aserelite","Orb of Alchemy"); 
            PoEdotcomOffers offers;
            
//            pattern = Pattern.compile("<div id=\"resultStats\">About ([0-9,]+) results</div>");
//            m = pattern.matcher(response);
//            if( m.find() ) {          
            //Pattern pattern = Pattern.compile(".*level (\\d+) (\\d+)%(.*)");
            Pattern pattern = Pattern.compile(".*level (\\d+) (\\d+)%(.*)");
            Matcher matcher = pattern.matcher(itemname);
            
            if (matcher.find()) {
                String gemlevel = matcher.group(1);
                String gemquality = matcher.group(2);
                String gemname = matcher.group(3);
                offers = poecomqueryhandler.getGemOffers(league,Enums.QueryType.gem,accountName,gemname, gemlevel, gemquality); 
            } else if (notificationDescriptor instanceof ItemTradeNotificationDescriptor) 
                //&& (!notificationDescriptor.getSourceString().matches(".*level \\d+ \\d+%.*")))
                offers = poecomqueryhandler.getOffers(league,Enums.QueryType.item,accountName,itemname); 
            else
                offers = poecomqueryhandler.getOffers(league,Enums.QueryType.currency,accountName,itemname); 
                
            for (PoEdotcomOffer result : offers.getResult()) {
                if (result.getListing().getAccount().getName().equals(accountName)) {
                    log.log(Level.DEBUG, "accountName found!");
//                    System.out.println("accountName found!");
                    if (result.getListing().getStash().getName().equals(stash)) {
                        log.log(Level.DEBUG, "stash found!");
//                        System.out.println("stash found!");
                        if (result.getListing().getStash().getX() == (stashX-1)) {
                            log.log(Level.DEBUG, "stashX found!");
//                            System.out.println("stashX found!");
                            if (result.getListing().getStash().getY() == (stashY-1)) {
                                log.log(Level.DEBUG, "stashY found!");
//                                System.out.println("stashY found!");
                                //double offeredPrice = Double.parseDouble(((ItemTradeNotificationDescriptor) notificationDescriptor).getOffer());
                                double offeredPrice = ((ItemTradeNotificationDescriptor) notificationDescriptor).getCurCount();
                                
                                double listedPrice = result.getListing().getPrice().getAmount();
                                String listedCurrency = result.getListing().getPrice().getCurrency();
                                String listedCurrencyType = result.getListing().getPrice().getType();
                                double offerVsPrice = offeredPrice/listedPrice;
                                
                                OfferVsListingData ret = new OfferVsListingData();
                                ret.setListedCurrencyType(listedCurrency);
                                ret.setListedCurrencyAmount(listedPrice);
                                
                                ret.setOfferedCurrencyType(((ItemTradeNotificationDescriptor) notificationDescriptor).getCurrency());
                                ret.setOfferedCurrencyAmount(((ItemTradeNotificationDescriptor) notificationDescriptor).getCurCount());
                                
                                ret.setRelation(offerVsPrice);
                                return ret;
                            }
                        }
                    }
                }
            }
                    
        } catch (Exception ex) {
            log.log(Level.ERROR, "something went wrong in doPriceCheck_old which shouldnt be called at all anymore!!");
//            System.out.println("something went wrong!");
        }
        //System.out.println("item wasnt found!");
        log.log(Level.DEBUG, "item wasnt found, in doPriceCheck_old which shouldnt be called at all anymore!!");
        return null;
    }

    private void closeMessagePanel() {
        Timer timer = new Timer(30, action -> {
            MercuryStoreCore.removeNotificationSubject.onNext(notificationDescriptor);
        });
        timer.setRepeats(false);
        timer.start();
    }
    
//    //	GetTabContent(tabName) {    
//    private TabInfos getTabContent() {
//    //      global GuiTrades_Controls    
//    //      GuiControlGet, visibleInfos, Trades:,% GuiTrades_Controls["hTEXT_TradeInfos" tabName]
//    //      GuiControlGet, invisibleInfos, Trades:,% GuiTrades_Controls["hTEXT_HiddenTradeInfos" tabName]
//    //      GuiControlGet, timeReceived, Trades:,% GuiTrades_Controls["hTEXT_TradeReceivedTime" tabName]        
//        TabInfos ti = new TabInfos();
//
//        ti.setBuyer(notificationDescriptor.getWhisperNickname());
//        ti.setItem(((ItemTradeNotificationDescriptor) notificationDescriptor).getItemName());
//        ti.setPrice(((ItemTradeNotificationDescriptor) notificationDescriptor).getOffer());
//        ti.setStash(((ItemTradeNotificationDescriptor) notificationDescriptor).getTabName());
//        ti.setOther(notificationDescriptor.getSourceString());  //other was always empty in my tests so i left it blank     
//        return(ti);
//        //TradeNotificationDescriptor
//        
//        //the rest below is propbably useless -> but i leave it here in case it isnt!
//    //
//    //		visibleInfosArr := {}
//    //		Loop, Parse, visibleInfos, `n, `r
//    //		{
//    //			if RegExMatch(A_LoopField, "O)Buyer:" A_Tab "(.*)", buyerPat)
//    //				visibleInfosArr.Buyer := buyerPat.1
//    //			else if RegExMatch(A_LoopField, "O)Item:" A_Tab "(.*)", itemPat)
//    //				visibleInfosArr.Item := itemPat.1
//    //			else if RegExMatch(A_LoopField, "O)Price:" A_Tab "(.*)", pricePat)
//    //				visibleInfosArr.Price := pricePat.1
//    //			else if RegExMatch(A_LoopField, "O)Stash:" A_Tab "(.*)", stashPat)
//    //				visibleInfosArr.Stash := stashPat.1
//    //			else if RegExMatch(A_LoopField, "O)Other:" A_Tab "(.*)", otherPat) 
//    //				visibleInfosArr.Other := otherPat.1, lastWasOther := True
//    //			else if (lastWasOther)
//    //				visibleInfosArr.Other := visibleInfosArr.Other "`n" A_LoopField
//    //		}        
//
////
////		invisibleInfosArr := {}
////		Loop, Parse, invisibleInfos, `n, `r
////		{
////			if RegExMatch(A_LoopField, "O)BuyerGuild:" A_Tab "(.*)", buyerGuildPat)
////				invisibleInfosArr.BuyerGuild := buyerGuildPat.1
////			else if RegExMatch(A_LoopField, "O)TimeStamp:" A_Tab "(.*)", timeStampPat)
////				invisibleInfosArr.TimeStamp := timeStampPat.1
////			else if RegExMatch(A_LoopField, "O)PID:" A_Tab "(.*)", pidPat)
////				invisibleInfosArr.PID := pidPat.1
////			else if RegExMatch(A_LoopField, "O)IsInArea:" A_Tab "(.*)", isInAreaPat)
////				invisibleInfosArr.IsInArea := isInAreaPat.1
////			else if RegExMatch(A_LoopField, "O)HasNewMessage:" A_Tab "(.*)", hasNewMessagePat)
////				invisibleInfosArr.HasNewMessage := hasNewMessagePat.1
////			else if RegExMatch(A_LoopField, "O)WithdrawTally:" A_Tab "(.*)", withdrawTallyPat)
////				invisibleInfosArr.WithdrawTally := withdrawTallyPat.1
////			else if RegExMatch(A_LoopField, "O)ItemName:" A_Tab "(.*)", itemNamePat)
////				invisibleInfosArr.ItemName := itemNamePat.1
////			else if RegExMatch(A_LoopField, "O)ItemLevel:" A_Tab "(.*)", itemLevelPat)
////				invisibleInfosArr.ItemLevel := itemLevelPat.1
////			else if RegExMatch(A_LoopField, "O)ItemQuality:" A_Tab "(.*)", itemQualityPat)
////				invisibleInfosArr.ItemQuality := itemQualityPat.1
////			else if RegExMatch(A_LoopField, "O)StashLeague:" A_Tab "(.*)", stashLeaguePat)
////				invisibleInfosArr.StashLeague := stashLeaguePat.1
////			else if RegExMatch(A_LoopField, "O)StashTab:" A_Tab "(.*)", stashTabPat)
////				invisibleInfosArr.StashTab := stashTabPat.1
////			else if RegExMatch(A_LoopField, "O)StashPosition:" A_Tab "(.*)", stashPositionPat)
////				invisibleInfosArr.StashPosition := stashPositionPat.1
////			else if RegExMatch(A_LoopField, "O)TimeYear:" A_Tab "(.*)", timeYearPat)
////				invisibleInfosArr.TimeYear := timeYearPat.1
////			else if RegExMatch(A_LoopField, "O)TimeMonth:" A_Tab "(.*)", timeMonthPat)
////				invisibleInfosArr.TimeMonth := timeMonthPat.1
////			else if RegExMatch(A_LoopField, "O)TimeDay:" A_Tab "(.*)", timeDayPat)
////				invisibleInfosArr.TimeDay := timeDayPat.1
////			else if RegExMatch(A_LoopField, "O)TimeHour:" A_Tab "(.*)", timeHourPat)
////				invisibleInfosArr.TimeHour := timeHourPat.1
////			else if RegExMatch(A_LoopField, "O)TimeMinute:" A_Tab "(.*)", timeMinPat)
////				invisibleInfosArr.TimeMinute := timeMinPat.1
////			else if RegExMatch(A_LoopField, "O)TimeSecond:" A_Tab "(.*)", timeSecPat)
////				invisibleInfosArr.TimeSecond := timeSecPat.1
////			else if RegExMatch(A_LoopField, "O)UniqueID:" A_Tab "(.*)", uniqueIDPat)
////				invisibleInfosArr.UniqueID := uniqueIDPat.1
////			else if RegExMatch(A_LoopField, "O)TradeVerify:" A_Tab "(.*)", tradeVerifyPat)
////				invisibleInfosArr.TradeVerify := tradeVerifyPat.1
////			else if RegExMatch(A_LoopField, "O)WhisperSite:" A_Tab "(.*)", whisperSitePat)
////				invisibleInfosArr.WhisperSite := whisperSitePat.1
////			else if RegExMatch(A_LoopField, "O)TradeVerifyInfos:" A_Tab "(.*)", tradeVerifyInfosPat)
////				invisibleInfosArr.TradeVerifyInfos := tradeVerifyInfosPat.1
////			else if RegExMatch(A_LoopField, "O)IsBuyerInvited:" A_Tab "(.*)", isBuyerInvitedPat)
////				invisibleInfosArr.IsBuyerInvited := isBuyerInvitedPat.1
////			else if RegExMatch(A_LoopField, "O)WhisperLang:" A_Tab "(.*)", whisperLangPat)
////				invisibleInfosArr.WhisperLang := whisperLangPat.1
////		}
////
////		tabContent := {}
////		for key, value in visibleInfosArr
////			tabContent[key] := value
////		for key, value in invisibleInfosArr
////			tabContent[key] := value
////		tabContent["Time"] := timeReceived
////
////		return tabContent
////	}        
//    }
//    
//    @Data
//    public class CurrencyInfos {
//        String name;
//        int id;
//        byte  isCurrencyListed;
//        private currencydata.POETradeCurrencyData poeTrade;
//        public CurrencyInfos(String name, byte isCurrencyListed) {
//            this.name = name;
//            this.isCurrencyListed = isCurrencyListed;
//            poeTrade = Configuration.get().currencyData().getPoeTradeData(name);
//        }
//    }
//    //Compare the specified currency with poe.trade abridged currency names to retrieve the real currency name.
//    //  When the string is plural, check if the full list of currencies contains its non-plural counterpart.
//    //Get_CurrencyInfos(currency) {
//    public CurrencyInfos get_CurrencyInfos(String currency) {
//    //  global PROGRAM
//        //boolean isCurrencyListed = false; //like in the script but we do it with byte instead - so we can signal which site it cane from just as well
//        String currencyWithoutS="";
//        String currencyFullName;
//        boolean isPlural=false;
//        byte isCurrencyListed = CurrencyData.NOPE;
//        
//    //  isCurrencyListed := False    
//        
//        if (currency.matches("See Offer"))
//            return(new CurrencyInfos(currency,isCurrencyListed)); 
//    //  if RegExMatch(currency, "See Offer") {
//    //      Return {Name:currency, Is_Listed:isCurrencyListed}  //return currency for structure-component ".Name" and isCurrencyListed for .Is_Listed
//    //  }    
//        currency = currency.replaceAll("\\d+", "");
//        currency = currency.trim();
//        if (currency.substring(currency.length()-1).equals("s"))
//            currencyWithoutS = currency.substring(0, currency.length()-2);
//    //            currency := RegExReplace(currency, "\d")
//    //            AutoTrimStr(currency) ; Remove whitespaces
//    //            lastChar := SubStr(currency, 0) ; Get last char
//    //            if (lastChar = "s") ; poeapp adds an "s" for >1 currencies
//    //                    StringTrimRight, currencyWithoutS, currency, 1            
////        if (Configuration.get().currencyData().get(currency)==null) {   //TODO - also read poe.com currency data
////            currencyFullName = 
////        }
//        isCurrencyListed=0;
//        //if (!Configuration.get().currencyData().isInCurrencyList(currency)) { //OBSOLETE - check whether thats true!
//        isCurrencyListed = Configuration.get().currencyData().isCurrencyKnown(currency);
//        if (isCurrencyListed==CurrencyData.NOPE) {//if not found check if found without the s at the end
//            isCurrencyListed = Configuration.get().currencyData().isCurrencyKnown(currencyWithoutS);
//            isPlural=true;
//        }
//        //}                                                                      //OBSOLETE - check whether thats true!
//        switch (isCurrencyListed) {
//            case CurrencyData.POEDOTCOM:
//            case CurrencyData.POETRADE:
//            case CurrencyData.CURRENCYLIST:
//                if (isPlural) {
//                    currencyFullName = currencyWithoutS;
//                } else {
//                    currencyFullName = currency;
//                }
//                break;
//                
//            case CurrencyData.NOPE:
//            default:
//                currencyFullName = currency;
//                break;
//        }
//        return(new CurrencyInfos(currencyFullName,isCurrencyListed));
        //return(new CurrencyInfos(currencyFullName,isCurrencyListed));
        /*
        // see MessageParser for details! :
        //Matcher PoeTradeCurrencyMatcher = poeTradeCurrencyPattern.matcher(fullMessage);
        //if (poeTradeCurrencyMatcher.find()) {
        
        //POETradeCurrencyData tt = new POETradeCurrencyData();
        //tt.getId();
        */
        //Configuration.get().currencyData().get(currencyWithoutS);
        //return new CurrencyInfos(currency,isCurrencyListed);
        
    /*
            if !IsIn(currency, PROGRAM.DATA.CURRENCY_LIST) {
                    currencyFullName := PROGRAM.DATA.POETRADE_CURRENCY_DATA[currency] ? PROGRAM.DATA.POETRADE_CURRENCY_DATA[currency]
                            :	PROGRAM.DATA.POEDOTCOM_CURRENCY_DATA[currency] ? PROGRAM.DATA.POEDOTCOM_CURRENCY_DATA[currency]
                            :	""
                    if (currencyFullName)
                            isCurrencyListed := True
            }
            else { ; Currency is in list
                    currencyFullName := currency
                    isCurrencyListed := True
            }
            if (!currencyFullName && currencyWithoutS) { ; Couldn't retrieve full name, and currency is possibly plural
                    if IsIn(currencyWithoutS, PROGRAM.DATA.CURRENCY_LIST) ; Currency is in list, was most likely plural
                    {
                            currencyFullName := currencyWithoutS
                            isCurrencyListed := True
                    }
            }
            else if !(currencyFullName) { ; Unknown currency name
                    AppendToLogs(A_ThisFunc "(currency=" currency "): Unknown currency name.")
            }

            currencyFullName := (currencyFullName)?(currencyFullName):(currency)
            Return {Name:currencyFullName, Is_Listed:isCurrencyListed}
            ; return currencyFullName
    }    
    */           
//    }
 /*
PoeTrade_GetCurrencySearchUrl(itemObj) {
    itemURL := PoeTrade_CreateCurrencyPayload(itemObj)
    return itemURL
}
*/
//    public String poeTrade_GetCurrencySearchUrl(Map<String, String> itemObj) {
//        String itemURL = poeTrade_CreateCurrencyPayload(itemObj,false);
//        return(itemURL);
//    }
    /*
PoeTrade_CreateCurrencyPayload(obj, addDefaultParams=False) {
    defaultParams := {"have": "", "league": "Incursion", "online": "x", "stock": "", "want": ""}
    */
//    public String poeTrade_CreateCurrencyPayload(Map<String,String> obj, boolean addDefaultParams) {
//        //boolean addDefaultParams=false;
//        Map<String,String> defaultParams = new HashMap<String,String>();
//        defaultParams.put("have", "");
//        defaultParams.put("league", "Betrayal");   //TODO!
//        defaultParams.put("online", "x");
//        defaultParams.put("stock", "");
//        defaultParams.put("want", "");
//        Map<String,String> poeTradeObj = obj;
//        String league = poeTradeObj.get("league");
//        league = league.substring(0, 1).toUpperCase() + league.substring(1);
//        poeTradeObj.put("league", league);
//        /*
//        poeTradeObj := obj
//
//        ; Capitalize league first letters
//        league := poeTradeObj["league"]
//        StringUpper, league, league, T
//        poeTradeObj["league"] := league        
//        */
//        //Create payload
//        String payload = "";
//        
//        if (addDefaultParams) {
//            for (Map.Entry<String, String> entry : defaultParams.entrySet()) {
//                String key = entry.getKey();
//                String defValue = entry.getValue();
//                
//                String value = "";
//                if (poeTradeObj.containsKey(key))
//                    value = entry.getValue();
//                else if (addDefaultParams && defValue!= null)
//                    value = defValue;
//                //else                //not needed because inititlization with ""
//                //    value = "";     //not needed because inititlization with ""
//                
//                String payloadStr = key + "=" + value;
//                if (!payload.equals(""))
//                    payload += "&"; 
//                payload += payloadStr;
//                /*
//                ; Create payload
//                payload := ""
//                if (addDefaultParams) {
//                    for key, defValue in defaultParams {
//                        value := poeTradeObj.HasKey(key) ? poeTradeObj[key] : addDefaultParams=True && defValue?defValue : ""
//                        payloadStr := key "=" value
//                        payload .= (payload)?("&" payloadStr):(payloadStr)
//                    }
//                }                
//                */
//            }
//        } else {
//            for (Map.Entry<String, String> entry : poeTradeObj.entrySet()) {
//                String payloadStr = entry.getKey() + "=" + entry.getValue();
//                if (!payload.equals(""))
//                    payload += "&"; 
//                payload += payloadStr;
//            }
//        }
//        /*
//        else {
//            for key, value in poeTradeObj {
//                payloadStr := key "=" value
//                payload .= (payload)?("&" payloadStr):(payloadStr)
//            }
//        }        
//        */
//        if (poeTradeObj.get("have")==null) {
//            log.log(Level.ERROR, "Failed to get currency ID for \"" + obj.get("have") + "\" (have)");
////            System.out.println("Failed to get currency ID for \"" + obj.get("have") + "\" (have)");
//        }
//        if (poeTradeObj.get("want")==null) {
//            System.out.println("Failed to get currency ID for \"" + obj.get("want") + "\" (want)");
//        }        
//        //if (logsAppend)               //TODO - some kind of logging - same logs as mercury, or a separate one for my functionality
//        //    AppendToLogs(logsAppend)  //TODO - some kind of logging - same logs as mercury, or a separate one for my functionality
//        return(payload);
//    /*
//    if (poeTradeObj.have = "") {
//        logsLine := "Failed to get currency ID for """ obj.have """ (have)"
//        logsAppend := logsAppend ? "`n" logsLine : logsLine, payload := ""
//    }
//    if (poeTradeObj.want = "") {
//        logsLine := "Failed to get currency ID for """ obj.want """ (want)"
//        logsAppend := logsAppend ? "`n" logsLine : logsLine, payload := ""
//    }
//
//    if (logsAppend)
//        AppendToLogs(logsAppend)
//
//    return payload
//}        
//        */
//    }
//    public void poeTrade_GetMatchingCurrencyTradeData(Map<String,String>dataObj, String itemURL) {
//        
//    }
    /*
    PoETrade_GetMatchingCurrencyTradeData(dataObj, itemURL) {
        html := CurrencyPoeTrade_GetSource("http://currency.poe.trade/search?" itemURL, skipPayload:=True)

        regexPos := 1
        matchingDatas := {}, foundMatchIndex := 0
        Loop {
            loopindex := A_Index
            foundPos := RegExMatch(html, "iO)<div class=""displayoffer "".*?<div class=""row"">", htmlPat, regexPos)
            if (foundPos) {
                tBody := htmlPat.0, regexPos := foundPos+1

                saleInfoTags := "username,sellcurrency,sellvalue,buycurrency,buyvalue,ign", foundObj := {}
                Loop, Parse, saleInfoTags,% ","
                {
                    RegExMatch(tBody, "iO)data-" A_LoopField "=""(.*?)""", foundPat)
                    foundObj[A_LoopField] := foundPat.1
                }

                sellBuyRatio := RemoveTrailingZeroes(foundObj.sellvalue / foundObj.buyvalue)
                isSameAccount := foundObj.username = dataObj.username ? True : False
                isSameRatio := sellBuyRatio = dataObj.sellBuyRatio ? True : False

                if (isSameAccount) {
                    foundMatchIndex++
                    isMatching := isSameRatio=True?True:False
                    matchingDatas[foundMatchIndex] := foundObj
                    matchingDatas[foundMatchIndex].SellBuyRatio := sellBuyRatio
                    matchingDatas[foundMatchIndex].IsSameRatio := isSameRatio
                }
            }
            else    
                Break
        }
        return matchingDatas
    }    
    */
/*
PoeTrade_CreatePayload(obj, addDefaultParams=False) {
    defaultParams := {"altart": "", "aps_max": "", "aps_min": "", "armour_max": "", "armour_min": "", "base": "", "block_max": "", "block_min": "", "buyout_currency": ""
    , "buyout_max": "", "buyout_min": "", "capquality": "x", "corrupted": "", "crafted": "", "crit_max": "", "crit_min": "", "dmg_max": "", "dmg_min": ""
    , "dps_max": "", "dps_min": "", "edps_max": "", "edps_min": "", "elder": "", "enchanted": "", "evasion_max": "", "evasion_min": "", "exact_currency": ""
    , "group_count": 1, "group_max": "", "group_min": "", "group_type": "And", "has_buyout": "", "identified": "", "ilvl_max": "", "ilvl_min": ""
    , "league": "Incursion", "level_max": "", "level_min": "", "link_max": "", "link_min": "", "linked_b": "", "linked_g": "", "linked_r": "", "linked_w": ""
    , "map_series": "", "mod_max": "", "mod_min": "", "mod_name": "", "mod_weight": "", "name": "", "online": "x", "pdps_max": "", "pdps_min": ""
    , "progress_max": "", "progress_min": "", "q_max": "", "q_min": "", "rarity": "", "rdex_max": "", "rdex_min": "", "rint_max": "", "rint_min": ""
    , "rlevel_max": "", "rlevel_min": "", "rstr_max": "", "rstr_min": "", "seller": "", "shaper": "", "shield_max": "", "shield_min": "", "sockets_a_max": ""
    , "sockets_a_min": "", "sockets_b": "", "sockets_g": "", "sockets_max": "", "sockets_min": "", "sockets_r": "", "sockets_w": "", "thread": "" , "type": ""}
*/
//    public String PoeTrade_CreatePayload(Map<String,String> obj, boolean addDefaultParams) {
//        Map<String,String> defaultParams = PoeTradeDefaults.getPOETradeDefaults();
//        Map<String,String> poeTradeObj = obj;
//        
//        // Capitalize league first letters
//        String[] leagueSplit = poeTradeObj.get("league").split(" (");
//        String leagueName = leagueSplit[0];
//        String leagueID = leagueSplit[1];
//        leagueName = leagueName.substring(0, 1).toUpperCase() + leagueName.substring(1);
//        String leagueFull = leagueID != null ? leagueName + " (" + leagueID : leagueName;
//        poeTradeObj.put("league", leagueFull);
//        /*
//        poeTradeObj := obj
//
//        ; Capitalize league first letters
//        leagueSplit := StrSplit(poeTradeObj["league"], " (")
//        leagueName := leagueSplit.1, leagueID := leagueSplit.2
//        StringUpper, leagueName, leagueName, T
//        leagueFull := leagueID?leagueName " (" leagueID : leagueName
//        poeTradeObj["league"] := leagueFull        
//        */
//        // Create payload
//        String payload = "";
//        
//        if (addDefaultParams) {
//            for (Map.Entry<String, String> entry : defaultParams.entrySet()) {
//                String key = entry.getKey();
//                String defValue = entry.getValue();
//                
//                String value = poeTradeObj.containsKey(key) ? poeTradeObj.get(key) : addDefaultParams && defValue!=null ? defValue : "";
//                String payloadStr = key + "=" + value;
//                payload = !payload.equals("") ? "&" + payloadStr : payloadStr;
//            }
//        } else {
//             for (Map.Entry<String, String> entry : poeTradeObj.entrySet()) {
//                String key = entry.getKey();
//                String value = entry.getValue();
//                
//                String payloadStr = key + "=" + value;
//                payload = !payload.equals("") ? "&" + payloadStr : payloadStr;
//             }
//        }
//        return payload;
//        /*
//        ; Create payload
//        payload := ""
//        if (addDefaultParams) {
//            for key, defValue in defaultParams {
//                value := poeTradeObj.HasKey(key) ? poeTradeObj[key] : addDefaultParams=True && defValue?defValue : ""
//                payloadStr := key "=" value
//                payload .= (payload)?("&" payloadStr):(payloadStr)
//            }
//        }
//        else {
//            for key, value in poeTradeObj {
//                payloadStr := key "=" value
//                payload .= (payload)?("&" payloadStr):(payloadStr)
//            }
//        }
//        return payload
//}            
//        */        
//     }
    //VerifyItemPrice(tabInfos) {
//    private void verifyItemPrice() {
//    //  global PROGRAM, SKIN
//        String wantFullName;
//        int wantID=0;
//        boolean fail=false;
//        byte isWantListed = CurrencyData.NOPE;
//        TabInfos ti = getTabContent();  //see above - verify calls getTabContent to get the infos instead of getting called by gettabcontent - for now!
//        
//        //String playerNickName = Configuration.get().notificationConfiguration().get().getPlayerNickname();   
//        String accountName = Configuration.get().notificationConfiguration().get().getAccountName();   
//    //  accounts := PROGRAM.SETTINGS.SETTINGS_MAIN.PoeAccounts  //AccountName/charactername    
//        //see getDescriptorData.getDescriptorData for details!
//        switch (notificationDescriptor.getType()) {
//            //if RegExMatch(tabInfos.Item, "iO)(\d+\.\d+|\d+) (\D+)", itemPat) { ; its a currency trade   //searches Field info, with that regex, and puts the matches to itemPat => itemPat.1 => group1
//            case INC_CURRENCY_MESSAGE:
//                double wantCount = ((CurrencyTradeNotificationDescriptor)notificationDescriptor).getCurrForSaleCount();
//                String wantWhat  = ((CurrencyTradeNotificationDescriptor)notificationDescriptor).getCurrForSaleTitle();
//                CurrencyInfos wantCurInfos = get_CurrencyInfos(wantWhat);
//                switch (wantCurInfos.isCurrencyListed) {
//                    case CurrencyData.POEDOTCOM:
//                        wantFullName = wantCurInfos.name;
//                        wantID = wantCurInfos.getPoeTrade().getId();
//                        break;
//                    case CurrencyData.POETRADE:
//                        wantFullName = wantCurInfos.name;
//                        //wantID = Configuration.get().currencyData().getPoeTradeData(wantFullName).getId();
//                        wantID = wantCurInfos.getPoeTrade().getId();
//                        break;
//                    case CurrencyData.CURRENCYLIST:
//                        wantFullName = wantCurInfos.name;
//                        wantID = wantCurInfos.getPoeTrade().getId();
//                        break;
//                    case CurrencyData.NOPE:
//                    default:
//                        fail = true;
//                        break;
//                }
//                isWantListed = wantCurInfos.getIsCurrencyListed();
//                //wantFullName := wantCurInfos.Name, 
//                //wantID := PROGRAM["DATA"]["POETRADE_CURRENCY_DATA"][wantFullName].ID, 
//                //isWantListed := wantCurInfos.Is_Listed
//                Double giveCount = ((TradeNotificationDescriptor)notificationDescriptor).getCurCount();
//                String giveWhat = ((TradeNotificationDescriptor)notificationDescriptor).getCurrency();
//                CurrencyInfos giveCurInfos = get_CurrencyInfos(giveWhat);
//                String giveFullName = giveCurInfos.getName();
//                if (giveCurInfos.isCurrencyListed==CurrencyData.NOPE)
//                    fail = true;
//                int giveID = giveCurInfos.getId();
//                byte isGiveListed = giveCurInfos.getIsCurrencyListed();
//                double sellBuyRatio = wantCount/giveCount;
//                
//                //giveCount := pricePat.1, giveWhat := pricePat.2
//                //giveCurInfos := Get_CurrencyInfos(giveWhat)
//                //giveFullName := giveCurInfos.Name, 
//                //giveID := PROGRAM["DATA"]["POETRADE_CURRENCY_DATA"][giveFullName].ID, 
//                //isGiveListed := giveCurInfos.Is_Listed  
//                //sellBuyRatio := RemoveTrailingZeroes(wantCount/giveCount)
//                //
//                //Loop, Parse, accounts,% ","   //obsolete since we only hae 1 account - YET !! (TODO!!)
//                //{
//                String league = ((TradeNotificationDescriptor)notificationDescriptor).getLeague();
//                Map<String, String> poeTradeObj = new HashMap<String,String>();
//                poeTradeObj.put("league", league);
//                poeTradeObj.put("online","x");
//                poeTradeObj.put("want", "" + wantID);   //TODO - let CurrencyData ALWAYS - return an ID => search in poeTradeCurrencyData - wherever the buyer came from!
//                poeTradeObj.put("have","" + giveID);
//                
//                String itemURL = poeTrade_GetCurrencySearchUrl(poeTradeObj);
//                
//                //http://poe.trade/search?league=Harbinger&name=Kaom%27s+Heart+Glorious+Plate
//                //                poeTradeObj := {"league": tabInfos.StashLeague, "online": "x", "want": wantID, "have": giveID}
//                //				itemURL := PoeTrade_GetCurrencySearchUrl(poeTradeObj)
//                
//                poeTradeObj.put("username", accountName);
//                poeTradeObj.put("sellcurrency",""+wantID); poeTradeObj.put("sellvalue",""+ wantCount);
//                poeTradeObj.put("buycurrency", ""+giveID); poeTradeObj.put("buyvalue",""+giveCount);
//                poeTradeObj.put("sellBuyRatio", ""+sellBuyRatio);
//                if (!itemURL.equals(""))
//                    //matchingObj;
//                /*
//                poeTradeObj.username := A_LoopField
//		poeTradeObj.sellcurrency := wantID, poeTradeObj.sellvalue := wantCount
//		poeTradeObj.buycurrency := giveID, poeTradeObj.buyvalue := giveCount
//                poeTradeObj.sellBuyRatio := sellBuyRatio
//                if (itemURL)
//                    matchingObj := PoETrade_GetMatchingCurrencyTradeData(poeTradeObj, itemURL)
//                */
//                
//                // Types of relevant TradeNotificationDescriptors
//                //CurrencyTradeNotificationDescriptor
//                //ItemTradeNotificationDescriptor
//                //TradeNotificationDescriptor
//                
//                break;
//                
//            case INC_ITEM_MESSAGE:                
//                break;   
//            default:
//                System.out.println("an error occured!");
//        }
//        /*
//			RegExMatch(tabInfos.Price, "iO)(\d+\.\d+|\d+) (\D+)", pricePat)
//			wantCount := itemPat.1, wantWhat := itemPat.2
//			wantCurInfos := Get_CurrencyInfos(wantWhat)
//			wantFullName := wantCurInfos.Name, wantID := PROGRAM["DATA"]["POETRADE_CURRENCY_DATA"][wantFullName].ID, isWantListed := wantCurInfos.Is_Listed
//			giveCount := pricePat.1, giveWhat := pricePat.2
//			giveCurInfos := Get_CurrencyInfos(giveWhat)
//			giveFullName := giveCurInfos.Name, giveID := PROGRAM["DATA"]["POETRADE_CURRENCY_DATA"][giveFullName].ID, isGiveListed := giveCurInfos.Is_Listed
//			sellBuyRatio := RemoveTrailingZeroes(wantCount/giveCount)
//
//			Loop, Parse, accounts,% ","
//			{
//				poeTradeObj := {"league": tabInfos.StashLeague, "online": "x", "want": wantID, "have": giveID}
//				itemURL := PoeTrade_GetCurrencySearchUrl(poeTradeObj)
//
//				poeTradeObj.username := A_LoopField
//				poeTradeObj.sellcurrency := wantID, poeTradeObj.sellvalue := wantCount
//				poeTradeObj.buycurrency := giveID, poeTradeObj.buyvalue := giveCount
//				poeTradeObj.sellBuyRatio := sellBuyRatio
//
//				if (itemURL)
//					matchingObj := PoETrade_GetMatchingCurrencyTradeData(poeTradeObj, itemURL)
//
//				if matchingObj.MaxIndex() {
//					foundMatch := True
//					Break
//				}
//			}
//
//			tabID := GUI_Trades.GetTabNumberFromUniqueID(tabInfos.UniqueID)
//
//			_infos .= ""
//			if (foundMatch) {
//				Loop % matchingObj.MaxIndex() { ; Loop through matchs
//					legitRatio := matchingObj[A_Index].IsSameRatio
//					ratioTxt := "poe.trade: 1 " giveFullName " = " matchingObj[A_Index].sellBuyRatio " " wantFullName
//						. "\nwhisper: 1 " giveFullName " = " sellBuyRatio " " wantFullName
//					
//					if (legitRatio=True) { ; If ratio is the same
//						_infos := "Ratio is the same."
//						. "\n" ratioTxt
//						vColor := "Green"
//						Break
//					}
//					else if (matchingObj[A_Index].sellBuyRatio > sellBuyRatio) { ; Or if ratio is higher
//						_infos := "Ratio is higher."
//						. "\n" ratioTxt
//						vColor := "Green"
//						Break
//					}
//					else { ; Otherwise, currency either not listed or ratio modified
//						if (!isWantListed || !isGiveListed) {
//							wantListedInfos := isWantListed=True?"" : "\nUnknown currency type: """ wantFullName """"
//							giveListedInfos := isGiveListed=True?"" : "\nUnknown currency type: """ giveFullName """"
//							_infos := wantListedInfos . giveListedInfos "\nPlease report it."
//							vColor := "Orange"
//						}
//						else {
//							_infos := "Ratio is lower."
//							. "\n" ratioTxt
//							vColor := "Red"
//						}
//					}
//				}
//			}
//			else {
//				if (tabInfos.WhisperLang != "ENG") {
//					_infos := "Cannot verify price for"
//					. "\npathofexile.com/trade translated whispers."
//					vColor := "Orange"
//				}
//				else {
//					_infos := "Could not find any item matching the same currency trade."
//					. "\nMake sure to set your account name in the settings."
//					. "\nAccounts: " accounts
//					vColor := "Orange"
//				}
//			}
//
//			GUI_Trades.SetTabVerifyColor(tabID, vColor)
//			GUI_Trades.UpdateSlotContent(tabID, "TradeVerifyInfos", _infos)
//		}
//		else { ; its a regular trade
//			itemQualNoPercent := StrReplace(tabInfos.ItemQuality, "%", "")
//			RegExMatch(tabInfos.StashPosition, "O)(.*);(.*)", stashPosPat)
//			stashPosX := stashPosPat.1, stashPosY := stashPosPat.2
//			RegExMatch(tabInfos.Price, "O)(\d+\.\d+|\d+) (\D+)", pricePat)
//			priceNum := pricePat.1, priceCurrency := pricePat.2
//			AutoTrimStr(priceNum, pricePat)
//			
//			currencyInfos := Get_CurrencyInfos(priceCurrency)
//			poeTradeCurrencyName := PoeTrade_Get_CurrencyAbridgedName_From_FullName(currencyInfos.Name)
//			poeTradePrice := priceNum " " poeTradeCurrencyName
//
//			Loop, Parse, accounts,% ","
//			{
//				poeTradeObj := {"name": tabInfos.ItemName, "buyout": poeTradePrice
//				, "level_min": tabInfos.ItemLevel, "level_max": tabInfos.ItemLevel
//				, "q_min": itemQualNoPercent, "q_max": itemQualNoPercent
//				, "league": tabInfos.StashLeague, "seller": A_LoopField}
//				itemURL := PoeTrade_GetItemSearchUrl(poeTradeObj)
//				
//				poeTradeObj.seller := A_LoopField, poeTradeObj.level := poeTradeObj.level_max
//				poeTradeObj.quality := poeTradeObj.q_max, poeTradeObj.tab := tabInfos.StashTab
//				poeTradeObj.x := stashPosX,	poeTradeObj.y := stashPosY, poeTradeObj.online := ""
//
//				matchingObj := PoeTrade_GetMatchingItemData(poeTradeObj, itemURL)
//
//				if IsObject(matchingObj) {
//					foundMatch := True
//					Break
//				}
//			}
//
//			tabID := GUI_Trades.GetTabNumberFromUniqueID(tabInfos.UniqueID)
//
//			_infos := ""
//			if (foundMatch) {
//				if (poeTradeObj.buyout = matchingObj.buyout) {
//					_infos := "Price confirmed legit."
//					. "\npoe.trade: " matchingObj.buyout
//					. "\nwhisper: " poeTradeObj.buyout
//					vColor := "Green"
//				}
//				else {
//					if (currencyInfos.Name = "") {
//						_infos := "/!\ Cannot verify unpriced items yet. /!\"
//						vColor := "Orange"
//						
//					}
//					else if (!currencyInfos.Is_Listed) {
//						_infos := "Unknown currency name: """ currencyInfos.Name """"
//						. "\nPlease report it."
//						vColor := "Orange"
//					}
//					else if (currencyInfos.Is_Listed && poeTradeObj.buyout != matchingObj.buyout) {
//						_infos := "Price is different."
//						. "\npoe.trade: " matchingObj.buyout
//						. "\nwhisper " poeTradeObj.buyout
//						vColor := "Red"
//					}
//
//				}
//			}
//			else {
//				if (tabInfos.WhisperLang != "ENG") {
//					_infos := "Cannot verify price for"
//					. "\npathofexile.com/trade translated whispers."
//					vColor := "Orange"
//				}
//				else {
//					_infos := "Could not find any item matching the same stash location"
//					. "\nMake sure to set your account name in the settings."
//					. "\nAccounts: " accounts
//					vColor := "Orange"
//				}
//			}
//
//			GUI_Trades.SetTabVerifyColor(tabID, vColor)
//			GUI_Trades.UpdateSlotContent(tabID, "TradeVerifyInfos", _infos)
//		}        
//        
//        */        
//    }
//    public class PriceChecker implements Runnable {
//
//        @Override
//        public void run() {
//                    try {
//            poedotcom.PoEdotcomQueryHandler a = new poedotcom.PoEdotcomQueryHandler();
//            PoEdotcomQueryHandler poecomqueryhandler = new PoEdotcomQueryHandler();
//            String league = ((TradeNotificationDescriptor)notificationDescriptor).getLeague();
//            String playerNickName = Configuration.get().notificationConfiguration().get().getPlayerNickname();  //TODO need a new field for accoutnname!
//            String itemname = ((ItemTradeNotificationDescriptor) notificationDescriptor).getItemName();
//            String stash = ((ItemTradeNotificationDescriptor) notificationDescriptor).getTabName();
//            int stashX = ((ItemTradeNotificationDescriptor) notificationDescriptor).getLeft();
//            int stashY = ((ItemTradeNotificationDescriptor) notificationDescriptor).getTop();
//            //poecomqueryhandler.getOffers("Betrayal",Enums.QueryType.currency,"aserelite","Orb of Alchemy"); 
//            PoEdotcomOffers offers;
//            
////            pattern = Pattern.compile("<div id=\"resultStats\">About ([0-9,]+) results</div>");
////            m = pattern.matcher(response);
////            if( m.find() ) {          
//            //Pattern pattern = Pattern.compile(".*level (\\d+) (\\d+)%(.*)");
//            Pattern pattern = Pattern.compile(".*level (\\d+) (\\d+)%(.*)");
//            Matcher matcher = pattern.matcher(itemname);
//            
//            if (matcher.find()) {
//                String gemlevel = matcher.group(1);
//                String gemquality = matcher.group(2);
//                String gemname = matcher.group(3);
//                offers = poecomqueryhandler.getGemOffers(league,Enums.QueryType.gem,playerNickName,gemname, gemlevel, gemquality); 
//            } else if (notificationDescriptor instanceof ItemTradeNotificationDescriptor) 
//                //&& (!notificationDescriptor.getSourceString().matches(".*level \\d+ \\d+%.*")))
//                offers = poecomqueryhandler.getOffers(league,Enums.QueryType.item,playerNickName,itemname); 
//            else
//                offers = poecomqueryhandler.getOffers(league,Enums.QueryType.currency,playerNickName,itemname); 
//                
//            for (PoEdotcomOffer result : offers.getResult()) {
//                if (result.getListing().getAccount().getName().equals(playerNickName)) {
//                    System.out.println("accountName found!");
//                    if (result.getListing().getStash().getName().equals(stash)) {
//                        System.out.println("stash found!");
//                        if (result.getListing().getStash().getX() == (stashX-1)) {
//                            System.out.println("stashX found!");
//                            if (result.getListing().getStash().getY() == (stashY-1)) {
//                                System.out.println("stashY found!");
//                                //double offeredPrice = Double.parseDouble(((ItemTradeNotificationDescriptor) notificationDescriptor).getOffer());
//                                double offeredPrice = ((ItemTradeNotificationDescriptor) notificationDescriptor).getCurCount();
//                                
//                                double listedPrice = result.getListing().getPrice().getAmount();
//                                String listedCurrency = result.getListing().getPrice().getCurrency();
//                                String listedCurrencyType = result.getListing().getPrice().getType();
//                                double offerVsPrice = offeredPrice/listedPrice;
//                                
//                                OfferVsListingData ret = new OfferVsListingData();
//                                ret.setListedCurrencyType(listedCurrency);
//                                ret.setListedCurrencyAmount(listedPrice);
//                                
//                                ret.setOfferedCurrencyType(((ItemTradeNotificationDescriptor) notificationDescriptor).getCurrency());
//                                ret.setOfferedCurrencyAmount(((ItemTradeNotificationDescriptor) notificationDescriptor).getCurCount());
//                                
//                                ret.setRelation(offerVsPrice);
//                                //return ret;
//                            }
//                        }
//                    }
//                }
//            }
//                    
//        } catch (Exception ex) {
//            System.out.println("something went wrong!");
//        }
//        System.out.println("item wasnt found!");
//        }
//        
//    }
}
