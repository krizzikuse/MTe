/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mercury.platform.ui.components.panel.notification.priceverifier;

import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.entity.message.ItemTradeNotificationDescriptor;
import com.mercury.platform.shared.entity.message.NotificationDescriptor;
import com.mercury.platform.shared.entity.message.TradeNotificationDescriptor;
import com.mercury.platform.ui.components.panel.notification.ItemTradeIncNotificationPanel;
import currencydata.CurrencyAmount;
import currencydata.CurrencyData;
import currencydata.ExchangeHelper;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.SwingUtilities;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import poedotcom.OfferVsListingData;
import poedotcom.PoEdotcomOffers;
import poedotcom.PoEdotcomQueryHandler;
import poedotcom.basictypes.Enums;
import poedotcom.basictypes.Enums.QueryType;
import poedotcom.itemdata.ItemExpert;
import poedotcom.itemdata.ItemTypeResponse;
import poedotcom.itemdata.ItemTypeResponse.ItemCategory;
import poedotcom.offer.PoEdotcomOffer;
import poedotcom.queryrequest.query.StatusField;
import poedotcom.queryrequest.query.StatusField.StatusValues;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
public class PoEdotcomPriceVerifier implements Runnable {
    private final org.apache.logging.log4j.Logger logger = LogManager.getLogger(PoEdotcomPriceVerifier.class.getSimpleName());
    
    
    private String league;
    private String accountName;
    private String itemname;
    private String stash;
    private int stashX;
    private int stashY;
    
    NotificationDescriptor notificationDescriptor;
    ItemTradeIncNotificationPanel tradeNotification;
    
    OfferVsListingData retF;
    public PoEdotcomPriceVerifier(NotificationDescriptor notificationDescriptor, ItemTradeIncNotificationPanel tradeNotification) {
        this.notificationDescriptor = notificationDescriptor;
        this.tradeNotification = tradeNotification;
//            poedotcom.PoEdotcomQueryHandler a = new poedotcom.PoEdotcomQueryHandler();

            //poecomqueryhandler.getOffers("Betrayal",Enums.QueryType.currency,"aserelite","Orb of Alchemy"); 
            
//            pattern = Pattern.compile("<div id=\"resultStats\">About ([0-9,]+) results</div>");
//            m = pattern.matcher(response);
//            if( m.find() ) {          
            //Pattern pattern = Pattern.compile(".*level (\\d+) (\\d+)%(.*)");

    }        
    @Override
    public void run() {
        boolean notpriced = false;
        boolean noSpecificOffer = false;
        try {
            league = ((TradeNotificationDescriptor)notificationDescriptor).getLeague();
            accountName = Configuration.get().notificationConfiguration().get().getAccountName();  //TODO need a new field for accoutnname!
            itemname = ((ItemTradeNotificationDescriptor) notificationDescriptor).getItemName();
            stash = ((ItemTradeNotificationDescriptor) notificationDescriptor).getTabName();
            stashX = ((ItemTradeNotificationDescriptor) notificationDescriptor).getLeft();
            stashY = ((ItemTradeNotificationDescriptor) notificationDescriptor).getTop();            
            
            if (((ItemTradeNotificationDescriptor) notificationDescriptor).getCurrency().equals("???")
                && (((ItemTradeNotificationDescriptor) notificationDescriptor).getCurCount() == 0) )
                noSpecificOffer = true;
            
            PoEdotcomQueryHandler poecomqueryhandler = new PoEdotcomQueryHandler();
            
            PoEdotcomOffers offers = null;
            
            Pattern gempattern1 = Pattern.compile(".*level (\\d+) (\\d+)%(.*)");
            Matcher gemmatcher1 = gempattern1.matcher(itemname);
            
            Pattern gempattern2 = Pattern.compile("(.[^\\(]*)\\s+\\((\\d+)\\/(\\d+)%\\)");
            Matcher gemmatcher2 = gempattern1.matcher(itemname);
            
            //String mapDetailPatternStr = "^(\\d+(\\.\\d+)?)? ?(.[^\\(^\\)]+)(\\s+\\(T(\\d+)\\))?$";
            String mapDetailPatternStr = "^(\\d+(\\.\\d+)?)? ?(.[^\\(^\\)]+Map) ?(\\s+\\(T(\\d+)\\))?$";
            Pattern mapDetailPattern = Pattern.compile(mapDetailPatternStr);            
            
            OfferVsListingData ret = null;
            retF = ret;
            QueryType querytype;
            String gemlevel="";
            String gemquality="";
            String gemname="";           
            
//            ItemTypeResponse type = ItemExpert.getItemType(itemname);
//            if (type.getCategory() != ItemCategory.none)
//                itemname = itemname.replace(type.getType(), "").trim();
            
            if (gemmatcher1.find()) {
                gemlevel = gemmatcher1.group(1);
                gemquality = gemmatcher1.group(2);
                gemname = gemmatcher1.group(3);
                querytype = QueryType.gem;
            } else if (gemmatcher2.find()) {
                gemname = gemmatcher1.group(1);
                gemlevel = gemmatcher1.group(2);
                gemquality = gemmatcher1.group(3);
                querytype = QueryType.gem;                
//                offers = poecomqueryhandler.getGemOffers(league,Enums.QueryType.gem,playerNickName,gemname, gemlevel, gemquality); 
            } else if ((notificationDescriptor instanceof ItemTradeNotificationDescriptor) 
                && (CurrencyData.isCurrencyKnown(itemname)==CurrencyData.NOPE) && (!itemname.matches("(?i).*Scarab$"))) { //!!TODO!! Scarab part is a temporary workaround - wont be needed anymore once scarabs are in the currency-lists
                //&& (!notificationDescriptor.getSourceString().matches(".*level \\d+ \\d+%.*")))
                Matcher mdm = mapDetailPattern.matcher(itemname);
                //if ((itemname.toLowerCase().contains("map"))
                if (mdm.find()) {
                    //itemname.replaceAll(" Map ", "");
                    querytype = QueryType.mapitem;
                    String map = mdm.group(3);                    
                    String amount = mdm.group(1);
                    String tier = mdm.group(5);
                    if ((!tier.equals("")) && (tier != null))
                        map = map.replaceFirst("Shaped ", "");
                    poecomqueryhandler.addMapDetails(map, tier, amount);
                    itemname = map;
                } else
                    querytype = QueryType.item;
//                offers = poecomqueryhandler.getOffers(league,Enums.QueryType.item,playerNickName,itemname); 
            } else
                querytype = QueryType.currency;
//                offers = poecomqueryhandler.getOffers(league,Enums.QueryType.currency,playerNickName,itemname); 
            
            switch (querytype) {
                case gem:
                    offers = poecomqueryhandler.getGemOffers(league,querytype,
                            accountName,gemname, gemlevel, gemquality);
                    if (offers.getResult().size()<1)
                        offers = poecomqueryhandler.getGemOffers(StatusValues.any,league,querytype,
                                accountName,gemname, gemlevel, gemquality);
                    break;
                case item:
                    offers = poecomqueryhandler.getOffers(league,querytype,
                            accountName,itemname); 
                    if (offers.getResult().size()<1)
                        offers = poecomqueryhandler.getOffers(StatusValues.any,
                                league,querytype,accountName,itemname); 
                    if (offers.getResult().size()<1) {
                        ItemTypeResponse type = ItemExpert.getItemType(itemname);
                        if (type.getCategory() != ItemCategory.none)
                            itemname = itemname.replace(type.getType(), "").trim();                        
                        
                        offers = poecomqueryhandler.getOffersByTypeOnly(StatusValues.any,
                                league,querytype,accountName,type.getType());
                        
                    }
                    break;
                case currency:
                case mapitem:
                    offers = poecomqueryhandler.getOffers(league,querytype,
                            accountName,itemname); 
                    if (offers.getResult().size()<1)
                        offers = poecomqueryhandler.getOffers(StatusValues.any,
                                league,querytype,accountName,itemname); 
                    break;               
            }
            
            for (PoEdotcomOffer result : offers.getResult()) {
                if (result.getListing().getAccount().getName().equals(accountName)) {
                    logger.log(Level.DEBUG, "accountName found!");
//                    System.out.println("accountName found!");
                    if (result.getListing().getStash().getName().equals(stash)) {
                        logger.log(Level.DEBUG, "stash found!");
//                        System.out.println("stash found!");
                        if (result.getListing().getStash().getX() == (stashX-1)) {
                            logger.log(Level.DEBUG, "stashX found!");
//                            System.out.println("stashX found!");
                            if ((result.getListing().getStash().getY() == (stashY-1))
                                || querytype == QueryType.mapitem ) {
                                logger.log(Level.DEBUG, "stashY found!");
//                                System.out.println("stashY found!");
                                //double offeredPrice = Double.parseDouble(((ItemTradeNotificationDescriptor) notificationDescriptor).getOffer());
                                //double offeredPrice = ((ItemTradeNotificationDescriptor) notificationDescriptor).getCurCount();
                                //TODO - what if it's not priced?
                                if (result.getListing().getPrice() == null) {
                                    notpriced = true;
                                    break;
                                }
                                double listedPrice = result.getListing().getPrice().getAmount();
                                String listedCurrency = result.getListing().getPrice().getCurrency();
                                String listedCurrencyType = result.getListing().getPrice().getType();   // == Type of offer ~b/o or other...
                                double offerVsPrice = 0;
                                if (!noSpecificOffer)
                                    offerVsPrice = ExchangeHelper.compareCurrAmount(
                                            new CurrencyAmount(
                                                ((ItemTradeNotificationDescriptor) 
                                                        notificationDescriptor).getCurrency(),
                                                ((ItemTradeNotificationDescriptor) 
                                                        notificationDescriptor).getCurCount()),                                        
                                            new CurrencyAmount(listedCurrency, listedPrice)
                                    );
                                //double offerVsPrice = offeredPrice/listedPrice;
                                
                                ret = new OfferVsListingData();
                                listedCurrency = CurrencyData.getMTeCurrencyIconName(listedCurrency);
                                ret.setListedCurrencyType(listedCurrency);
                                ret.setListedCurrencyAmount(listedPrice);
                                
                                ret.setOfferedCurrencyType(((ItemTradeNotificationDescriptor) notificationDescriptor).getCurrency());
                                ret.setOfferedCurrencyAmount(((ItemTradeNotificationDescriptor) notificationDescriptor).getCurCount());
//                                if (!listedCurrency.equals(
//                                        ((ItemTradeNotificationDescriptor) notificationDescriptor).getCurrency())) {
//                                    String properListedCurrency = CurrencyData.getProperCurrencyName(result.getListing().getPrice().getCurrency());
//                                    String properOfferCurrency = CurrencyData.getProperCurrencyName(((ItemTradeNotificationDescriptor) notificationDescriptor).getCurrency());
//                                    ;   //TODO - compare currency
//                                }
                                
                                
                                ret.setRelation(offerVsPrice);
                                retF = ret;
                                break;
//                                tradeNotification.setPriceVerificationResult(ret);
                            }
                        }
                    }
                }
            }
//            tradeNotification.setPriceVerificationResult(ret);    //previous place of this call - before it went to the last line!
            
//            SwingUtilities.invokeLater(new Runnable() {
//                public void run() {
//                    tradeNotification.setPriceVerificationResult(retF);
//                }
//            });      
        if ((retF==null) && (!notpriced)) {
            logger.log(Level.DEBUG, "item wasnt found!");
//            System.out.println("item wasnt found!");
            retF = new OfferVsListingData();
            retF.setListedCurrencyAmount(-1);
            retF.setListedCurrencyType("");
            retF.setOfferedCurrencyAmount(-1);
            retF.setOfferedCurrencyType("");
            retF.setRelation(-1);
        } else if (notpriced) {
            logger.log(Level.DEBUG, "item found, but no price set!");
//            System.out.println("item found, but no price set!");
            retF = new OfferVsListingData();
            retF.setListedCurrencyAmount(-2);
            retF.setListedCurrencyType("");
            retF.setOfferedCurrencyAmount(-2);
            retF.setOfferedCurrencyType("");
            retF.setRelation(-2);            
        } else if (noSpecificOffer) {
            logger.log(Level.DEBUG, "item found, priced, but buyer offered nothing");
            retF.setRelation(-3);
        }
        
        tradeNotification.setPriceVerificationResult(retF);
        
        } catch (Exception ex) {
            logger.log(Level.ERROR, "*!*!*!*!*!*!*!*!* Exception occured in PriceVerification! *!*!*!*!*!*!*!*!*!*!*!*!*");
            logger.log(Level.ERROR, ex);
//            System.out.println("*!*!*!*!*!*!*!*!* Exception occured in PriceVerification! *!*!*!*!*!*!*!*!*!*!*!*!*");
//            System.out.println(ex);
            tradeNotification.setPriceVerificationException();
        }
        
        //tradeNotification.setPriceVerificationResult(retF);
        //return null;        
    }
    
}
