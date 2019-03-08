/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package currencydata;

import static currencydata.CurrencyData.getProperCurrencyName;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
public class ExchangeHelper {
    private static Map<String,Double> exchangeRates = new HashMap<String,Double>();
//    public ExchangeHelper(Map<String,Double> rates) {
//        exchangeRates = rates;
//    }    
    public static void addExchangeRates(Map<String,Double> rates) {
        exchangeRates = rates;
    }    
    public static double getExchangeRate(String currtype) {
        String propername = getProperCurrencyName(currtype);
        propername = propername.toLowerCase();
        return(exchangeRates.get(propername));
    } 
    public static double toChaos(CurrencyAmount curr) {
        String propername = getProperCurrencyName(curr.getType());
        if (!propername.toLowerCase().equals("chaos orb")) {
            //String propername = getProperCurrencyName(curr.getType());
            return(curr.getAmount() * getExchangeRate(propername));
        } else 
            return curr.getAmount();
    } 
    public static double compareCurrAmount(CurrencyAmount curr1, CurrencyAmount curr2) {
//        String curr1proper = getProperCurrencyName(curr1.getType());
//        String curr2proper = getProperCurrencyName(curr2.getType());
//        if ()
        return roundto5Decimals((toChaos(curr1)/toChaos(curr2)));
    }
    public static double compareCurrAmount_noRounding(CurrencyAmount curr1, CurrencyAmount curr2) {
//        String curr1proper = getProperCurrencyName(curr1.getType());
//        String curr2proper = getProperCurrencyName(curr2.getType());
//        if ()
        return (toChaos(curr1)/toChaos(curr2));
    }
    public static double roundto5Decimals (double val) {
        return((double)Math.round(val * 1000000d) / 1000000d);
    }
    

}
