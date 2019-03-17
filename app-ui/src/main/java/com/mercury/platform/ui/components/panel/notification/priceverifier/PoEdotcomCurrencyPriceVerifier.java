/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mercury.platform.ui.components.panel.notification.priceverifier;

import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.entity.message.CurrencyTradeNotificationDescriptor;
import com.mercury.platform.shared.entity.message.ItemTradeNotificationDescriptor;
import com.mercury.platform.shared.entity.message.NotificationDescriptor;
import com.mercury.platform.shared.entity.message.TradeNotificationDescriptor;
import com.mercury.platform.ui.components.panel.notification.CurrencyTradeIncNotificationPanel;
import com.mercury.platform.ui.components.panel.notification.ItemTradeIncNotificationPanel;
import currencydata.CurrencyAmount;
import currencydata.CurrencyData;
import currencydata.ExchangeHelper;
import currencydata.MapTradeDetails;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.SwingUtilities;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import poedotcom.OfferVsListingData;
import poedotcom.PoEdotcomOffers;
import poedotcom.PoEdotcomQueryHandler;
import poedotcom.basictypes.Enums;
import poedotcom.basictypes.Enums.QueryType;
import poedotcom.offer.PoEdotcomOffer;
import poedotcom.queryrequest.query.StatusField;
import poedotcom.queryrequest.query.StatusField.StatusValues;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
public class PoEdotcomCurrencyPriceVerifier implements Runnable {
    private final org.apache.logging.log4j.Logger logger = LogManager.getLogger(PoEdotcomCurrencyPriceVerifier.class.getSimpleName());

    //final double THRESHOLD = .000001;
    
    private String league;
    private String accountName;
    private String itemname;
    private String stash;
    private int stashX;
    private int stashY;
    
    NotificationDescriptor notificationDescriptor;
    CurrencyTradeIncNotificationPanel tradeNotification;
    
    OfferVsListingData retF;
    public PoEdotcomCurrencyPriceVerifier(NotificationDescriptor notificationDescriptor, CurrencyTradeIncNotificationPanel tradeNotification) {
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
        boolean differentcurrency = false;
        boolean isMultiTrade = false;
        try {
            league = ((TradeNotificationDescriptor)notificationDescriptor).getLeague();
            accountName = Configuration.get().notificationConfiguration().get().getAccountName();  //TODO need a new field for accoutnname!
            
            if (((CurrencyTradeNotificationDescriptor)notificationDescriptor).getItems().size() > 0) {
                isMultiTrade = true;
                doMultiProcessing();
            } else {
//            ArrayList<CurrencyAmount> want = new ArrayList<CurrencyAmount>();
//            want.add(new CurrencyAmount(
//                    CurrencyData.getPoEdotcomBulkExchangeName(((CurrencyTradeNotificationDescriptor)notificationDescriptor).getCurrForSaleTitle()),
//                    ((CurrencyTradeNotificationDescriptor)notificationDescriptor).getCurrForSaleCount()));
                String mapDetailPatternStr = "^(\\d+(\\.\\d+)?)? ?(.[^\\(^\\)]+Map) ?(\\s+\\(T(\\d+)\\))?$";
                Pattern mapDetailPattern = Pattern.compile(mapDetailPatternStr); 
                Matcher mdm = mapDetailPattern.matcher(((CurrencyTradeNotificationDescriptor)notificationDescriptor).getCurrForSaleTitle());
                //if ((itemname.toLowerCase().contains("map"))
                String searchTerm = "";
                if (mdm.find()) {
                    //itemname.replaceAll(" Map ", "");
                    String map = mdm.group(3);                    
                    String amount = mdm.group(1);
                    String tier = mdm.group(5);
                    searchTerm = map;
                } else {
                    searchTerm = ((CurrencyTradeNotificationDescriptor)notificationDescriptor).getCurrForSaleTitle();
                }
                CurrencyAmount want = new CurrencyAmount(
                        CurrencyData.getPoEdotcomBulkExchangeName(searchTerm),
                        ((CurrencyTradeNotificationDescriptor)notificationDescriptor).getCurrForSaleCount());          
                PoEdotcomQueryHandler poecomqueryhandler = new PoEdotcomQueryHandler();                
                
                if ((want.getType().equals("")) || want.getType() == null) {
                    logger.log(Level.ERROR, "*!*!*!*!*!*!*!*!* Error in PriceVerification! *!*!*!*!*!*!*!*!*!*!*!*!*");
                    logger.log(Level.ERROR, "item the buyer wants wasnt found in exchange-list!");            
        //            System.out.println("*!*!*!*!*!*!*!*!* Exception occured in PriceVerification! *!*!*!*!*!*!*!*!*!*!*!*!*");
        //            System.out.println(ex);
                    tradeNotification.setPriceVerificationException();                    
                } else {

                    PoEdotcomOffers offers = null;

                    OfferVsListingData ret = null;
                    retF = ret;

                    offers = poecomqueryhandler.getBulkOffer(StatusValues.online, league, accountName,
                            CurrencyData.getPoEdotcomBulkExchangeName(
                                    ((TradeNotificationDescriptor)notificationDescriptor).getCurrency()), want);
                    if (offers.getResult().size()<1)
                        offers = poecomqueryhandler.getBulkOffer(StatusValues.any, league, accountName,
                            CurrencyData.getPoEdotcomBulkExchangeName(
                                    ((TradeNotificationDescriptor)notificationDescriptor).getCurrency()), want);

                    if (offers.getResult().size()<1) {
                        offers = poecomqueryhandler.getBulkOffer(StatusValues.any, league, accountName,
                            "", want);   
                        if (offers.getResult().size()>0)
                            differentcurrency = true;
                    }

                    //AnthrópiniSkoúraTrypa: Hi, I'd like to buy your 10 Chaos Orb for my 1 Exalted Orb in Betrayal.
        //            if (offers.getResult().size()!=0) {
        //             
        //            }
                    for (PoEdotcomOffer result : offers.getResult()) {
                        //double listedPrice = result.getListing().getPrice().getAmount();
                        //double listedPrice = result.getListing().getPrice().getExchange().getAmount();
                        //String listedPriceCurrency = result.getListing().getPrice().getExchange().getCurrency();
                        if ((!(want.getType().equals(result.getListing().getPrice().getItem().getCurrency()))) ||
                            (!(want.getAmount()>=result.getListing().getPrice().getItem().getAmount())))
                            continue;
                        CurrencyAmount listedPrice = new CurrencyAmount(    //the listed Price
                                result.getListing().getPrice().getExchange().getCurrency(),
                                result.getListing().getPrice().getExchange().getAmount()
                        );

                        String listedCurrency =  result.getListing().getPrice().getItem().getCurrency();    //what's offered for the listed price
                        double listedCurrencyAmount = result.getListing().getPrice().getItem().getAmount(); //^the associated amount

                        double offerToListingAmount = want.getAmount()/listedCurrencyAmount;

                        CurrencyAmount listedPricetimesWantAmount = new CurrencyAmount(
                                listedPrice.getType(),
                                listedPrice.getAmount()*offerToListingAmount
                        );

                        double offerVsPrice = ExchangeHelper.compareCurrAmount(
                                new CurrencyAmount(
                                    ((TradeNotificationDescriptor) 
                                        notificationDescriptor).getCurrency(),
                                    ((TradeNotificationDescriptor) 
                                        notificationDescriptor).getCurCount()),                                        
                                    listedPricetimesWantAmount
                        );

                        ret = new OfferVsListingData();
                        ret.setListedCurrencyType(CurrencyData.getMTeCurrencyIconName(listedPricetimesWantAmount.getType()));
                        ret.setListedCurrencyAmount(listedPricetimesWantAmount.getAmount());

                        ret.setOfferedCurrencyType(((TradeNotificationDescriptor) notificationDescriptor).getCurrency());
                        ret.setOfferedCurrencyAmount(((TradeNotificationDescriptor) notificationDescriptor).getCurCount());                
                        ret.setRelation(offerVsPrice);
                        retF = ret;

                    //String listedCurrencyType = result.getListing().getPrice().getType();    


    //                if (result.getListing().getAccount().getName().equals(accountName)) {
    //                    System.out.println("accountName found!");
    //                    if (result.getListing().getStash().getName().equals(stash)) {
    //                        System.out.println("stash found!");
    //                        if (result.getListing().getStash().getX() == (stashX-1)) {
    //                            System.out.println("stashX found!");
    //                            if (result.getListing().getStash().getY() == (stashY-1)) {
    //                                System.out.println("stashY found!");
    //                                //double offeredPrice = Double.parseDouble(((ItemTradeNotificationDescriptor) notificationDescriptor).getOffer());
    //                                //double offeredPrice = ((ItemTradeNotificationDescriptor) notificationDescriptor).getCurCount();
    //                                //TODO - what if it's not priced?
    //                                if (result.getListing().getPrice() == null) {
    //                                    notpriced = true;
    //                                    break;
    //                                }
    //                                double listedPrice = result.getListing().getPrice().getAmount();
    //                                String listedCurrency = result.getListing().getPrice().getCurrency();
    //                                String listedCurrencyType = result.getListing().getPrice().getType();   // == Type of offer ~b/o or other...
    //                                double offerVsPrice = ExchangeHelper.compareCurrAmount(
    //                                        new CurrencyAmount(
    //                                            ((ItemTradeNotificationDescriptor) 
    //                                                    notificationDescriptor).getCurrency(),
    //                                            ((ItemTradeNotificationDescriptor) 
    //                                                    notificationDescriptor).getCurCount()),                                        
    //                                        new CurrencyAmount(listedCurrency, listedPrice)
    //                                );
    //                                //double offerVsPrice = offeredPrice/listedPrice;
    //                                
    //                                ret = new OfferVsListingData();
    //                                listedCurrency = CurrencyData.getMTeCurrencyIconName(listedCurrency);
    //                                ret.setListedCurrencyType(listedCurrency);
    //                                ret.setListedCurrencyAmount(listedPrice);
    //                                
    //                                ret.setOfferedCurrencyType(((ItemTradeNotificationDescriptor) notificationDescriptor).getCurrency());
    //                                ret.setOfferedCurrencyAmount(((ItemTradeNotificationDescriptor) notificationDescriptor).getCurCount());
    ////                                if (!listedCurrency.equals(
    ////                                        ((ItemTradeNotificationDescriptor) notificationDescriptor).getCurrency())) {
    ////                                    String properListedCurrency = CurrencyData.getProperCurrencyName(result.getListing().getPrice().getCurrency());
    ////                                    String properOfferCurrency = CurrencyData.getProperCurrencyName(((ItemTradeNotificationDescriptor) notificationDescriptor).getCurrency());
    ////                                    ;   //TODO - compare currency
    ////                                }
    //                                
    //                                
    //                                ret.setRelation(offerVsPrice);
    //                                retF = ret;
    //                                break;
    ////                                tradeNotification.setPriceVerificationResult(ret);
    //                            }
    //                        }
    //                    }
    //                }
                }
    //            tradeNotification.setPriceVerificationResult(ret);    //previous place of this call - before it went to the last line!

    //            SwingUtilities.invokeLater(new Runnable() {
    //                public void run() {
    //                    tradeNotification.setPriceVerificationResult(retF);
    //                }
    //            });      
                    if ((retF==null) && (!notpriced)) {
                        logger.log(Level.DEBUG, "item wasnt found!");
    //                    System.out.println("item wasnt found!");
                        retF = new OfferVsListingData();
                        retF.setListedCurrencyAmount(-1);
                        retF.setListedCurrencyType("");
                        retF.setOfferedCurrencyAmount(-1);
                        retF.setOfferedCurrencyType("");
                        retF.setRelation(-1);
                    } else if (notpriced) {
                        logger.log(Level.DEBUG, "item found, but no price set!");
    //                    System.out.println("item found, but no price set!");
                        retF = new OfferVsListingData();
                        retF.setListedCurrencyAmount(-2);
                        retF.setListedCurrencyType("");
                        retF.setOfferedCurrencyAmount(-2);
                        retF.setOfferedCurrencyType("");
                        retF.setRelation(-2);            
                    }

                    if (differentcurrency)
                        retF.setDiffCurrency(true);
                    tradeNotification.setPriceVerificationResult(retF);
                }
            }
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
    
    private void doMultiProcessing() {
//            ArrayList<CurrencyAmount> want = new ArrayList<CurrencyAmount>();
//            want.add(new CurrencyAmount(
//                    CurrencyData.getPoEdotcomBulkExchangeName(((CurrencyTradeNotificationDescriptor)notificationDescriptor).getCurrForSaleTitle()),
//                    ((CurrencyTradeNotificationDescriptor)notificationDescriptor).getCurrForSaleCount()));
        boolean notpriced = false;
        boolean differentcurrency = false;
        
        MultiOfferVsListingData ret = new MultiOfferVsListingData();

        //whats offered for the items from by the buyer
        ArrayList<CurrencyAmount> wants = new ArrayList<CurrencyAmount>();
        ArrayList<CurrencyAmount> have = new ArrayList<CurrencyAmount>();
        if (((CurrencyTradeNotificationDescriptor)notificationDescriptor).getCurrencies().size()>0) {
            //have.addAll(((TradeNotificationDescriptor)notificationDescriptor).getCurrencies());
            for (CurrencyAmount curr : ((CurrencyTradeNotificationDescriptor)notificationDescriptor).getCurrencies()) 
                have.add(new CurrencyAmount(CurrencyData.getPoEdotcomBulkExchangeName(curr.getType()),curr.getAmount()));
        } else
            have.add(
                    new CurrencyAmount( CurrencyData.getPoEdotcomBulkExchangeName(((TradeNotificationDescriptor)notificationDescriptor).getCurrency()),
                            ((TradeNotificationDescriptor)notificationDescriptor).getCurCount()));
        
        try {
            String mapDetailPatternStr = "^(\\d+(\\.\\d+)?)? (.[^\\(^\\)]+)(\\s+\\(T(\\d+)\\))?$";
            Pattern mapDetailPattern = Pattern.compile(mapDetailPatternStr);
            PoEdotcomOffers offers = new PoEdotcomOffers();
            for (String item : ((CurrencyTradeNotificationDescriptor)notificationDescriptor).getItems()) {
                Matcher mdm = mapDetailPattern.matcher(item);
                if (mdm.find()) {
                    String map = mdm.group(3);                    
                    String amount = mdm.group(1);
                    String tier = mdm.group(5);
//                    MapTradeDetails details = tier != null ? 
//                            new MapTradeDetails(map,Double.parseDouble(amount),Byte.parseByte(tier)) :
//                            new MapTradeDetails(map,Double.parseDouble(amount));
                    CurrencyAmount want = new CurrencyAmount(
                            CurrencyData.getPoEdotcomBulkExchangeName(
                                    map),
                                    Double.parseDouble(amount)); 
                    wants.add(want);
                    //offers.addResult(doSingleSearch(want,have));  //search item(currency/map) for item
                }
            }
            offers.addResult(doBundledSearch(wants,have));  //search all of them at once and set minimum amount to 1
            //offers = doBundledSearch(wants,have);  //search all of them at once and set minimum amount to 1
            
//            Map<String, Boolean> wantFound = new HashMap<String,Boolean>(); //to determine whether everything was found yet
//            for (CurrencyAmount w : wants)
//                wantFound.put(w.getType(), false);
//                        
//            for (PoEdotcomOffer offer : offers.getResult()) {
//                wantFound.put(offer.getListing().getPrice().getItem().getCurrency(),true);
//            }
//            ArrayList<CurrencyAmount> remaining = new ArrayList<CurrencyAmount>();
//            for (Map.Entry<String,Boolean> f : wantFound.entrySet())
//                if (f.getValue() == false)
//                    for (CurrencyAmount w : wants)
//                        if (f.getKey().equals(w.))
//                    remaining.add(new CurrencyAmount(f.getKey(),1));
//            ArrayList<CurrencyAmount> remainingWants = new ArrayList<CurrencyAmount>();
//            remainingWants = ((ArrayList<CurrencyAmount>)((ArrayList<CurrencyAmount>)wants).clone());
            ArrayList<CurrencyAmount> remainingWants = new ArrayList<CurrencyAmount>(wants);
            //Iterator<CurrencyAmount> iterator = remainingWants.iterator();
            //while(iterator.hasNext()){
            //    CurrencyAmount entry = iterator.next();
            //    System.out.println("Removing " + entry);
            //    iterator.remove();
            //}            
            for (PoEdotcomOffer offer : offers.getResult()) {
                Iterator<CurrencyAmount> iterator = remainingWants.iterator();
                while(iterator.hasNext()){
                    CurrencyAmount entry = iterator.next();
                    if ((offer.getListing().getPrice().getItem().getCurrency().equals(entry.getType()))
                        && offer.getListing().getPrice().getItem().getAmount()<= entry.getAmount()) {
                        System.out.println("Removing " + entry);
                        iterator.remove();
                    }
                } 
                if (remainingWants.size()==0)
                    break;
            }
            
//            for (CurrencyAmount rw : remainingWants) {
//                remainingWants.remove(rw);
//            }
            
            
            if ((offers.getResult().size()<1) || (remainingWants.size()>0)) {
                ArrayList<CurrencyAmount> haveany = new ArrayList<CurrencyAmount>();
                haveany.add(new CurrencyAmount("",0));
                //offers.addResult(doBundledSearch(remainingWants,haveany));
                ArrayList<PoEdotcomOffer> remainingOffers = doBundledSearch(remainingWants,haveany);
                offers.addResult(remainingOffers);
                for (PoEdotcomOffer offer : remainingOffers) {
                    Iterator<CurrencyAmount> iterator = remainingWants.iterator();
                    while(iterator.hasNext()){
                        CurrencyAmount entry = iterator.next();
                        if ((offer.getListing().getPrice().getItem().getCurrency().equals(entry.getType()))
                            && offer.getListing().getPrice().getItem().getAmount()<= entry.getAmount())
                        System.out.println("Removing " + entry);
                        iterator.remove();
                    }
                    if (remainingWants.size()==0)
                        break;
                }
                //offers = doBundledSearch(wants,haveany);
                differentcurrency = true;
            }
            
            ArrayList<CurrencyAmount> listingPrices = new ArrayList<CurrencyAmount>();
            
            Map<CurrencyAmount,ArrayList<PriceAndPriceInChaos>> allPricesFor = new HashMap<CurrencyAmount,ArrayList<PriceAndPriceInChaos>>();
            
            for (PoEdotcomOffer result : offers.getResult()) {
                for (CurrencyAmount w : wants) {
                    if (w.getType().equals(result.getListing().getPrice().getItem().getCurrency()))
                        if (w.getAmount()>=result.getListing().getPrice().getItem().getAmount()) {  //TODO - what if there's mutliple offers??
                            logger.log(Level.DEBUG, "found "+result.getListing().getPrice().getItem().getCurrency() + " => "
                                    + result.getListing().getPrice().getItem().getAmount() + " for " + result.getListing().getPrice().getExchange().getAmount() +" "
                            + result.getListing().getPrice().getExchange().getCurrency());
//                            System.out.println("found "+result.getListing().getPrice().getItem().getCurrency() + " => "
//                                    + result.getListing().getPrice().getItem().getAmount() + " for " + result.getListing().getPrice().getExchange().getAmount() +" "
//                            + result.getListing().getPrice().getExchange().getCurrency());
                            double offerToListingAmount = w.getAmount()/result.getListing().getPrice().getItem().getAmount();// need to find the most fitting offer then...
                            if (result.getListing().getPrice().getExchange() != null) {
                                CurrencyAmount price = new CurrencyAmount(
                                        CurrencyData.getProperCurrencyName(result.getListing().getPrice().getExchange().getCurrency()),
                                        result.getListing().getPrice().getExchange().getAmount()*offerToListingAmount
                                );
                                CurrencyAmount priceInChaos = new CurrencyAmount("Chaos Orb",ExchangeHelper.toChaos(price));
                                if (allPricesFor.containsKey(w)) {
                                    ArrayList<PriceAndPriceInChaos> pricePut = allPricesFor.get(w);
                                    PriceAndPriceInChaos priceAndChaosPrice = new PriceAndPriceInChaos(price,priceInChaos);
                                    pricePut.add(priceAndChaosPrice);
                                    allPricesFor.put(w, pricePut);
                                } else {
                                    ArrayList<PriceAndPriceInChaos> pricePut = new ArrayList<PriceAndPriceInChaos>();
                                    PriceAndPriceInChaos priceAndChaosPrice = new PriceAndPriceInChaos(price,priceInChaos);
                                    pricePut.add(priceAndChaosPrice);
                                    allPricesFor.put(w, pricePut);
                                }
                                //listingPrices.add(price);
                                //wants.remove(w);  
                                break;                                
                            }
//                            } else {
//                                notpriced = true;
//                                break;
//                            }
                        }
                }
            }
            
            for (Map.Entry<CurrencyAmount,ArrayList<PriceAndPriceInChaos>> entry : allPricesFor.entrySet()) {
                allPricesFor.get(entry.getKey()).sort((PriceAndPriceInChaos z1, PriceAndPriceInChaos z2) -> {
                    if (z1.getPriceInChaos().getAmount() > z2.getPriceInChaos().getAmount())
                        return 1;
                    if (z1.getPriceInChaos().getAmount() < z2.getPriceInChaos().getAmount())
                        return -1;
                    return 0;
                }) ;
            }
            
            Map<CurrencyAmount,PriceAndPriceInChaos> bestPrices = new HashMap<CurrencyAmount,PriceAndPriceInChaos>();
            for (Map.Entry<CurrencyAmount,ArrayList<PriceAndPriceInChaos>> entry : allPricesFor.entrySet()) {
                bestPrices.put(entry.getKey(), entry.getValue().get(0));
                logger.log(Level.DEBUG, "best price for " + entry.getKey().getAmount() + " " + entry.getKey().getType() + " = " +
                    entry.getValue().get(0).getPrice().getAmount() +" "+ entry.getValue().get(0).getPrice().getType() + " ("
                            + entry.getValue().get(0).getPriceInChaos().getAmount() + " " +entry.getValue().get(0).getPriceInChaos().getType() +")"
                );
//                System.out.println("best price for " + entry.getKey().getAmount() + " " + entry.getKey().getType() + " = " +
//                    entry.getValue().get(0).getPrice().getAmount() +" "+ entry.getValue().get(0).getPrice().getType() + " ("
//                            + entry.getValue().get(0).getPriceInChaos().getAmount() + " " +entry.getValue().get(0).getPriceInChaos().getType() +")"
//                );
            }
            
            //worked well like that but we still need to preserve the original Prices
//            for (Map.Entry<CurrencyAmount,ArrayList<CurrencyAmount>> entry : allPricesFor.entrySet()) {
//                allPricesFor.get(entry.getKey()).sort((CurrencyAmount z1, CurrencyAmount z2) -> {
//                    if (z1.getAmount()> z2.getAmount())
//                        return 1;
//                    if (z1.getAmount() < z2.getAmount())
//                        return -1;
//                    return 0;
//                }) ;
//            }

            for (CurrencyAmount w : wants) {
                if (!bestPrices.containsKey(w))
                    notpriced = true;
            }
            
            double total = 0;
//            for (CurrencyAmount price : listingPrices) {
//                total += ExchangeHelper.toChaos(price);
//            }
            
            for (Map.Entry<CurrencyAmount,PriceAndPriceInChaos> bp : bestPrices.entrySet()) 
                total += bp.getValue().getPriceInChaos().getAmount();
            
            CurrencyAmount totalListingPriceInChaos = new CurrencyAmount("Chaos Orb", total);
            
            total = 0;
            for (CurrencyAmount offer : have) {
                total += ExchangeHelper.toChaos(offer);
            }
            CurrencyAmount totalOfferInChaos = new CurrencyAmount("Chaos Orb", total);
            
            Map<String,Double> currencyAmountMap = new HashMap<String,Double>();
            for(Map.Entry<CurrencyAmount,PriceAndPriceInChaos> bp : bestPrices.entrySet())
                if (currencyAmountMap.containsKey(bp.getValue().getPrice().getType())) {
                    Double summarised = currencyAmountMap.get(bp.getValue().getPrice().getType()) + bp.getValue().getPrice().getAmount();
                    currencyAmountMap.put(bp.getValue().getPrice().getType(), summarised);
                } else {
                    currencyAmountMap.put(bp.getValue().getPrice().getType(), bp.getValue().getPrice().getAmount());
                }
            
            ArrayList<PriceAndPriceInChaos> listed = new ArrayList<PriceAndPriceInChaos>();
            for (Map.Entry<String,Double> currencyAmount : currencyAmountMap.entrySet()) {
                CurrencyAmount price = new CurrencyAmount(CurrencyData.getMTeCurrencyIconName(currencyAmount.getKey()), currencyAmount.getValue());
                double chaosvalue = ExchangeHelper.toChaos(new CurrencyAmount(currencyAmount.getKey(),currencyAmount.getValue()));
                CurrencyAmount chaosPrice = new CurrencyAmount(CurrencyData.getMTeCurrencyIconName("chaos orb"),chaosvalue);
                listed.add(new PriceAndPriceInChaos(price,chaosPrice));
            }
            ArrayList<PriceAndPriceInChaos> offered = new ArrayList<PriceAndPriceInChaos>();
            //for (CurrencyAmount curr : ((CurrencyTradeNotificationDescriptor)notificationDescriptor).getCurrencies())
            for (CurrencyAmount offer : have) {
                //offer.get
                CurrencyAmount price = new CurrencyAmount(CurrencyData.getMTeCurrencyIconName(offer.getType()), offer.getAmount());
                double chaosvalue = ExchangeHelper.toChaos(offer);
                CurrencyAmount chaosPrice = new CurrencyAmount(CurrencyData.getMTeCurrencyIconName("chaos orb"),chaosvalue);
                offered.add(new PriceAndPriceInChaos(offer,chaosPrice));
            }
            
                    
            double offerVsListing = ExchangeHelper.compareCurrAmount(totalOfferInChaos, totalListingPriceInChaos);
            ret.setListed(listed);
            ret.setOffered(offered);
            ret.setRelation(offerVsListing);
            
            if ((ret==null) && (!notpriced)) {
                logger.log(Level.DEBUG, "an item wasnt found!");
//                System.out.println("an item wasnt found!");
                ret = new MultiOfferVsListingData();
                ArrayList<PriceAndPriceInChaos> retListed = new  ArrayList<PriceAndPriceInChaos>();
                retListed.add(new PriceAndPriceInChaos(
                        new CurrencyAmount("",-1),
                        new CurrencyAmount("",-1)
                ));
                ret.setListed(retListed);
                ArrayList<PriceAndPriceInChaos> retOffered = new  ArrayList<PriceAndPriceInChaos>();
                retOffered.add(new PriceAndPriceInChaos(
                        new CurrencyAmount("",-1),
                        new CurrencyAmount("",-1)
                ));
                ret.setRelation(-1);
            } else if (notpriced) {
                logger.log(Level.DEBUG, "items found, but no price set for one or more of them!");
//                System.out.println("item found, but no price set!");
                ret = new MultiOfferVsListingData();
                ArrayList<PriceAndPriceInChaos> retListed = new  ArrayList<PriceAndPriceInChaos>();
                retListed.add(new PriceAndPriceInChaos(
                        new CurrencyAmount("",-2),
                        new CurrencyAmount("",-2)
                ));
                ret.setListed(retListed);
                ArrayList<PriceAndPriceInChaos> retOffered = new  ArrayList<PriceAndPriceInChaos>();
                retOffered.add(new PriceAndPriceInChaos(
                        new CurrencyAmount("",-2),
                        new CurrencyAmount("",-2)
                ));
                ret.setRelation(-2);                         
            } else if (differentcurrency)
                ret.setDiffCurrency(true);            
            
            
            logger.log(Level.DEBUG, "we did it!");
//            System.out.println("we did it!");
            tradeNotification.setPriceVerificationResult(ret);
            
        } catch (Exception ex) {
            logger.log(Level.ERROR, "*!*!*!*!*!*!*!*!* Exception occured in PriceVerification (MultiItem!) ! *!*!*!*!*!*!*!*!*!*!*!*!*");
            logger.log(Level.ERROR, ex);
//            System.out.println("*!*!*!*!*!*!*!*!* Exception occured in PriceVerification (MultiItem!) ! *!*!*!*!*!*!*!*!*!*!*!*!*");
//            System.out.println(ex);
            tradeNotification.setPriceVerificationException();              
        }
            
            
            
//            CurrencyAmount want = new CurrencyAmount(
//                    CurrencyData.getPoEdotcomBulkExchangeName(
//                            ((CurrencyTradeNotificationDescriptor)notificationDescriptor).getCurrForSaleTitle()),
//                    ((CurrencyTradeNotificationDescriptor)notificationDescriptor).getCurrForSaleCount());          
//            
//            
//            PoEdotcomOffers offers = null;
            
            //OfferVsListingData ret = null;
            //retF = ret;

        
    }
    
    public ArrayList<PoEdotcomOffer> doSingleSearch(CurrencyAmount want, ArrayList<CurrencyAmount> have) throws IOException {
        try {
        OfferVsListingData ret = null;
        PoEdotcomQueryHandler poecomqueryhandler = new PoEdotcomQueryHandler();
        PoEdotcomOffers offers = null;
        offers = poecomqueryhandler.getMultiBulkOffer(StatusValues.online, league, accountName,
                    have, want);
            if (offers.getResult().size()<1)
                offers = poecomqueryhandler.getMultiBulkOffer(StatusValues.any, league, accountName,
                    have, want);
            
            if (offers.getResult().size()>0)
                return(offers.getResult());
        } catch (Exception ex) {
            logger.log(Level.ERROR, "*!*!*!*!*!*!*!*!* Exception occured in PriceVerification (MultiItem - doSingleSearch!) ! *!*!*!*!*!*!*!*!*!*!*!*!*");
            logger.log(Level.ERROR, ex);
//            System.out.println("*!*!*!*!*!*!*!*!* Exception occured in PriceVerification (MultiItem - doSingleSearch!) ! *!*!*!*!*!*!*!*!*!*!*!*!*");
//            System.out.println(ex);
            tradeNotification.setPriceVerificationException();                 
        }
            //AnthrópiniSkoúraTrypa: Hi, I'd like to buy your 10 Chaos Orb for my 1 Exalted Orb in Betrayal.
//            if (offers.getResult().size()!=0) {
//             
//            }
//            for (PoEdotcomOffer result : offers.getResult()) {
//                //double listedPrice = result.getListing().getPrice().getAmount();
//                //double listedPrice = result.getListing().getPrice().getExchange().getAmount();
//                //String listedPriceCurrency = result.getListing().getPrice().getExchange().getCurrency();
//                CurrencyAmount listedPrice = new CurrencyAmount(    //the listed Price
//                        result.getListing().getPrice().getExchange().getCurrency(),
//                        result.getListing().getPrice().getExchange().getAmount()
//                );
//                
//                String listedCurrency =  result.getListing().getPrice().getItem().getCurrency();    //what's offered for the listed price
//                double listedCurrencyAmount = result.getListing().getPrice().getItem().getAmount(); //^the associated amount
//                
//                double offerToListingAmount = want.getAmount()/listedCurrencyAmount;
//                
//                CurrencyAmount listedPricetimesWantAmount = new CurrencyAmount(
//                        listedPrice.getType(),
//                        listedPrice.getAmount()*offerToListingAmount
//                );
//                
//                double offerVsPrice = ExchangeHelper.compareCurrAmount(
//                        new CurrencyAmount(
//                            ((TradeNotificationDescriptor) 
//                                notificationDescriptor).getCurrency(),
//                            ((TradeNotificationDescriptor) 
//                                notificationDescriptor).getCurCount()),                                        
//                            listedPricetimesWantAmount
//                );
//                
//                ret = new OfferVsListingData();
//                ret.setListedCurrencyType(CurrencyData.getMTeCurrencyIconName(listedPricetimesWantAmount.getType()));
//                ret.setListedCurrencyAmount(listedPricetimesWantAmount.getAmount());
//
//                ret.setOfferedCurrencyType(((TradeNotificationDescriptor) notificationDescriptor).getCurrency());
//                ret.setOfferedCurrencyAmount(((TradeNotificationDescriptor) notificationDescriptor).getCurCount());                
//                ret.setRelation(offerVsPrice);
//                retF = ret;
//            }
//            return(null);
//        } catch (Exception ex) {
//            System.out.println("*!*!*!*!*!*!*!*!* Exception occured in PriceVerification (MultiItem!) ! *!*!*!*!*!*!*!*!*!*!*!*!*");
//            System.out.println(ex);
//            tradeNotification.setPriceVerificationException();    
//            return(null);
//        }
        return(null);
    }
    
    public ArrayList<PoEdotcomOffer> doBundledSearch(ArrayList<CurrencyAmount> wants, ArrayList<CurrencyAmount> have) throws IOException {
        try {
        PoEdotcomQueryHandler poecomqueryhandler = new PoEdotcomQueryHandler();
        PoEdotcomOffers offers = null;
        offers = poecomqueryhandler.getBundledBulkOffers(StatusValues.online, league, accountName,
                    have, wants);
            if (offers.getResult().size()<1)
                offers = poecomqueryhandler.getBundledBulkOffers(StatusValues.any, league, accountName,
                    have, wants);
            
            //if (offers.getResult().size()>0)
            return(offers.getResult());
            //else return null;
        } catch (Exception ex) {
            logger.log(Level.ERROR, "*!*!*!*!*!*!*!*!* Exception occured in PriceVerification (MultiItem - doBundledSearch!) ! *!*!*!*!*!*!*!*!*!*!*!*!*");
            logger.log(Level.ERROR, ex);
//            System.out.println("*!*!*!*!*!*!*!*!* Exception occured in PriceVerification (MultiItem - doBundledSearch!) ! *!*!*!*!*!*!*!*!*!*!*!*!*");
//            System.out.println(ex);
            tradeNotification.setPriceVerificationException();                 
        }
        return(null);
    }
}
