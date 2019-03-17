/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package currencydata;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
//@Data
//@AllArgsConstructor
public class CurrencyData {
    private static Map<String, POETradeCurrencyData> poeTradeData = new HashMap<String, POETradeCurrencyData>();
    private static Map<String, String> poeDotComData = new HashMap<String, String>();
    private static List<String> currencyNames = new LinkedList<String>();
    
    private static BiMap<String, String> biPoEDotComData = HashBiMap.create();

    
    private static Map<String,String> iconNameExceptions = new HashMap<String,String>();
    private static Map<String,String> properNameExceptions = new HashMap<String,String>();
    static {
 	iconNameExceptions.put("ancient orb", "Ancient Orb");   // Ancient Orb
 	iconNameExceptions.put("chromatic orb", "chrome");	// chromatic / chrome
 	iconNameExceptions.put("engineer's orb", "Engineer's Orb"); // Engineer's Orb
 	iconNameExceptions.put("eternal orb", "eternal");   // eternal
 	iconNameExceptions.put("exalted orb", "exalted");   // exalted
 	iconNameExceptions.put("harbinger's orb", "Harbinger's Orb");   // Harbinger's Orb	
 	iconNameExceptions.put("jeweller's orb", "jeweller's");	// jeweller's
 	iconNameExceptions.put("jewellers", "jeweller's");	// jeweller's
	
 	iconNameExceptions.put("regal orb", "regal");       // regal
 	iconNameExceptions.put("vaal orb", "vaal");         // vaal
 	iconNameExceptions.put("divine orb", "divine");     // divine
 	iconNameExceptions.put("chaos orb", "chaos");       // chaos
 	iconNameExceptions.put("blessed orb", "blessed");   // blessed  
 	iconNameExceptions.put("cartographer's chisel", "chisel"); // chisel  
 	iconNameExceptions.put("mirror of kalandra", "mirror"); // mirror  
 	iconNameExceptions.put("silver coin", "silver"); // silver coin
        
        properNameExceptions.put("jewellers", "jeweller's orb");
    }
      //public POETradeCurrencyDataEntry2(Map.Entry<String,JsonElement> entry) {
    public static void addPOETradeCurrency(Map.Entry<String,JsonElement> entry) {
        Gson gson = new Gson();
        //Map.Entry<String,JsonElement> entry
        if (entry.getValue().isJsonObject()) {
            //System.out.println("Key:"+entry.getKey()+"has Object Type:" +entry.getValue());
            //entry.getKey()

            Type mapType = new TypeToken<POETradeCurrencyData>(){}.getType();
            POETradeCurrencyData result= gson.fromJson(entry.getValue(), mapType);    //needs to be parsed 2 times, because it can either be structure or string...
            //TODO - be wary with the following changes (.toLowerCase() x 2)
            poeTradeData.put(entry.getKey().toLowerCase(), result);

        } else if (entry.getValue().isJsonPrimitive()) {
            //System.out.println("Key:"+entry.getKey()+"has primitive Type:" +entry.getValue());
            String text = gson.fromJson(entry.getValue(), String.class);

            poeTradeData.put(entry.getKey(), new POETradeCurrencyData(null,null,text.toLowerCase()));
            //poeTradeData.put(entry.getKey().toLowerCase(), new POETradeCurrencyData(null,null,text));
        //data.put(key, value)
        }

    }

    public static POETradeCurrencyData getPoeTradeData(String key) {
        key = key.toLowerCase();
        POETradeCurrencyData data = poeTradeData.get(key);
        if (data != null) {
            String name = data.getName();
        //if (data.get(key).getName()!=null) 
        if (poeTradeData.get(name)!=null)
            return(poeTradeData.get(name));
        else 
            return (poeTradeData.get(key));
        } else 
            return(null);
    }
    
    //before calling this method - by all means you HAVE TO call isCurrencyKnown
    // first!
    public static String getPoETradeName(String key) {
        POETradeCurrencyData data = poeTradeData.get(key.toLowerCase());
        if (data != null) {
            String name = data.getName();
            if (name != null)
                return name;    // it was the abridged name as key
            else 
                return key;     // it was the name anyway
        } else
            return null; // !!!TODO!!! weird and very unlikely Exception, print into logfile!
        
    }
      
    public static String getPoeDotComData(String key) {
        //if (poeDotComData.get(key.toLowerCase())!=null)
        return(poeDotComData.get(key.toLowerCase()));   //we'll need the reverse map aswell....
//        else
//            return(null);
      }
      
    public static void addPOEdotcomCurrency(Map<String, String> poedotcomcurrencies) {
        poeDotComData = poedotcomcurrencies;   //TODO : i think we have to read key as value and vice versa!
        for (Map.Entry<String,String> poecomentry : poeDotComData.entrySet()) {
            biPoEDotComData.put(poecomentry.getKey(), poecomentry.getValue().toLowerCase());
        }
        //biPoEDotComData = HashBiMap.create(poeDotComData);//biPoEDotComData.c
    }
    public static void addCurrencyList(List<String> cl) {
        currencyNames = cl;
    }
    public static boolean isInCurrencyList(String currency) {
        return (currencyNames.contains(currency.trim()));
    }
    
    public static final byte CURRENCYLIST= 3;
    public static final byte POEDOTCOM   = 2;
    public static final byte POETRADE    = 1;    
    public static final byte NOPE        = 0;
    
    public static byte isCurrencyKnown(String currency) {
//        if(isInCurrencyList(currency))
//            return CURRENCYLIST;
        currency = currency.trim();
        if(getPoeDotComData(currency)!=null)
            return POEDOTCOM;
        else if (getPoeTradeData(currency) != null)
            return POETRADE;
        else if (isInCurrencyList(currency))
            return CURRENCYLIST;
        else
            return NOPE;
    }
    
    //check the lists (aquired at startup) for the entered String
    // and if found remove the "orb of " from the beginning + make it all lowercase 
    // so the appropriate MercuryTrade- and (-enhanced-) icons can be loaded 
    // using the return value of this method
    // if nothing is found in the lists or there is no icon - the original string is returned
    public static String getMTeCurrencyIconName(String raw) {
        String traw = raw.trim();
        String mtename = raw;
        
        //to get rid of the "orb of " in e.g. "orb of alchemy", we use the following regex
        String orbexp = "(?i)^Orb of ";
        String scrollexp = "(?i)^Scroll of ";
        byte currencyFound = isCurrencyKnown(traw);
        switch (currencyFound) {
            case POEDOTCOM:
                //mtename = getPoeDotComData(traw).replaceAll(orbexp, "");
//                mtename = iconNameExceptions.containsKey(mtename) ?
//                        iconNameExceptions.get(mtename) : mtename;
                mtename = getPoeDotComData(mtename);
                //mtename = mtename.toLowerCase();
                break;
                
            //!TODO! for case POETRADE: maybe the abridged name is sometimes not appropriate?
            case POETRADE:
                POETradeCurrencyData poetrade = getPoeTradeData(traw);
                mtename = poetrade.getAbridged() != null ? 
                        poetrade.getAbridged() : traw;   
                //mtename = mtename.toLowerCase();
                break;
                
            //following 2 cases + default have intended fallthrough because we 
            //  do the same in all of those: return the raw string because because
            //  we dont know what it is
            //CURRENCYLIST: if it reaches this case that basically means that it's a valid "currency"
            // but comes neither from PathofExile.com, nor PoE.trade    => !!TODO!! realize poeapp aswell!!!
            case CURRENCYLIST:
                //mtename = traw.replaceAll(orbexp, "");
                //mtename = mtename.toLowerCase();
            //NOPE: "currency" isnt even in the list...
            case NOPE:
            //default: WTF happened ?!
            default:
                //TODO - print MASSIVE Exception - this should never happen!
                break;                
        }
        if (currencyFound != NOPE) {
            mtename = mtename.toLowerCase();
            mtename = mtename.replaceAll(orbexp, "");
            mtename = mtename.replaceAll(scrollexp, "");
            mtename = iconNameExceptions.containsKey(mtename) ?
                    iconNameExceptions.get(mtename) : mtename;
            return mtename;
        } else
            return(raw);
    }
    //!!!TODO!!!
    public static String getProperCurrencyName(String currterm) {
        currterm = currterm.trim();
        byte currencyFound = isCurrencyKnown(currterm);
        switch (currencyFound) {
            case CURRENCYLIST:
                //done, is term thats also found in data from poe.ninja
                break;
            case POEDOTCOM:
                //in (PoEdotcom-)map schauen und value liefern
                currterm = getPoeDotComData(currterm);
                break;
            case POETRADE:
                //done, is term thats also found in data from poe.ninja
                // OR its not the full name("abridged") and we have to get the name from the class
                currterm = getPoETradeName(currterm);
                break;
            case NOPE:
                break;
        }
        System.out.println("ProperCurrencyName=" + currterm);
        currterm = properNameExceptions.containsKey(currterm) ? properNameExceptions.get(currterm) : currterm;
        return(currterm);
    }    
    public static String getPoEdotcomBulkExchangeName(String currterm) {
        if (biPoEDotComData.inverse().containsKey(getProperCurrencyName(currterm).toLowerCase()))
            return(biPoEDotComData.inverse().get(getProperCurrencyName(currterm).toLowerCase()));
        else
            return(biPoEDotComData.inverse().get(getProperCurrencyName(currterm).toLowerCase() + " map"));
    }
}

