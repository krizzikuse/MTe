/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poedotcom.itemdata;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import poedotcom.itemdata.ItemTypeResponse.ItemCategory;
import poeninja.CurrencyExchangeEntry;
import poeninja.CurrencyOverviewResult;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
public class ItemExpert {
    /*
    !!!TODO!!! that was all i was able to find...
    -> for now we can just assign them correctly ...
    source: https://pathofexile.gamepedia.com/Public_stash_tab_API
    category	maps, currency, jewels, gems, cards, flasks or category object (explained below)
    category
    Each array contains only one string.

    Key	Value description	Type
    accessories	amulet, belt or ring	
    armour	helmet, gloves, chest, shield, quiver, boots	
    jewels	abyss	
    weapons	twosword, bow, dagger, staff, claw, onesword, wand, oneaxe, twoaxe, sceptre, onemace, twomace

    */
    
    
    private static HashSet<String> itemTypes = new HashSet<String>();
    
    private static HashSet<String> accessoryTypes = new HashSet<String>();
    private static HashSet<String> armourTypes = new HashSet<String>();
    private static HashSet<String> cardTypes = new HashSet<String>();
    private static HashSet<String> currencyTypes = new HashSet<String>();
    private static HashSet<String> flaskTypes = new HashSet<String>();
    private static HashSet<String> gemTypes = new HashSet<String>();
    private static HashSet<String> jewelTypes = new HashSet<String>();
    private static HashSet<String> mapTypes = new HashSet<String>();
    private static HashSet<String> weaponTypes = new HashSet<String>();
    private static HashSet<String> leaguestoneTypes = new HashSet<String>();
    private static HashSet<String> prophecyTypes = new HashSet<String>();
    private static HashSet<String> capturedBeastTypes = new HashSet<String>();
    
    private static ArrayList<String> itemTypesList = new ArrayList<String>();
    
    public void decipherData(JsonObject jo) {
        Gson gson = new Gson();
        Type mapType = new TypeToken<PoEdotcomItemsData>() {}.getType();
        JsonParser jsonParser = new JsonParser();
//        JsonReader reader = new JsonReader(new StringReader(jsonStr));
//        CurrencyOverviewResult exchangeRates = gson.fromJson(jsonParser.parse(reader), mapType);
        PoEdotcomItemsData itemData = gson.fromJson(jo, mapType);
        
        for (PoEdotcomItemData category : itemData.getResult()) {
            HashSet<String> types = new HashSet<String>();
            switch(category.getLabel()) {
		case "Accessories":
                    types = accessoryTypes;
                    break;
                case "Armour":
                    types = armourTypes;
			break;
		case "Cards":
                    types = cardTypes;
                    break;
		case "Currency":
                    types = currencyTypes;
                    break;
		case "Flasks":
                    types = flaskTypes;
                    break;
		case "Gems":
                    types = gemTypes;
                    break;
		case "Jewels":
                    types = jewelTypes;
                    break;
		case "Maps":
                    types = mapTypes;
                    break;
		case "Weapons":
                    types = weaponTypes;
                    break;
		case "Leaguestones":
                    types = leaguestoneTypes;
                    break;
		case "Prophecies":
                    types = prophecyTypes;
                    break;
		case "Captured Beasts":
                    types = capturedBeastTypes;
                    break;
            }
            for (PoEdotcomItemDataEntry entry : category.getEntries()) {
                itemTypes.add(entry.getType());
                types.add(entry.getType());
            }
        }
        itemTypesList.addAll(itemTypes);
        
        for (String type : itemTypes)
            System.out.println(type);
        System.out.println(itemTypes.size());
        
        //String test = "Gale Thrusting Mind Cage";
        String test = "Wurstl of wursthausen the abbisabebba";
        //itemTypes.c
        System.out.println("######################################################");
        System.out.println("ArrayList test START:" + System.currentTimeMillis());
        String match = stringContainsItemFromList(test,itemTypesList);
        System.out.println("ArrayList test END:" + System.currentTimeMillis() + " match = "+match);
        System.out.println("######################################################");
        
        System.out.println("######################################################");
        System.out.println("HashSet test START:" + System.currentTimeMillis());         
        String wurstl = stringContainsItemFromList(test,itemTypes);
        System.out.println("HashSet test END:" + System.currentTimeMillis() +" match = " + wurstl);
        System.out.println("######################################################");        
        //String test = "test";
        //test.contains(itemTypes);
        
        
        //System.out.println(match.toString());
        //for now we just make a Map that contains currencyType (key)
        // and chaos-equivalency (value)
        //  => we dont interpret all data in CurrencyOverview
        //Map<String,Double> chaosEquivalency = new HashMap<String,Double>();
        
//        for (CurrencyExchangeEntry currex : exchangeRates.getLines()) {
//            chaosEquivalency.put(currex.getCurrencyTypeName().toLowerCase(), currex.getChaosEquivalent());
//        }
        //return(chaosEquivalency);
    }
    //source: https://stackoverflow.com/questions/8992100/test-if-a-string-contains-any-of-the-strings-from-an-array
    public static String stringContainsItemFromList(String inputStr, ArrayList<String> items) {
        //boolean b = items.parallelStream().anyMatch(inputStr::contains);
        try {
            return items.parallelStream().filter(inputStr::contains).findAny().get();
        } catch (Exception ex) {
            return null;
        }    
    }        
    //https://stackoverflow.com/questions/150750/hashset-vs-list-performance
    public static String stringContainsItemFromList(String inputStr, HashSet<String> items) {
        //boolean b = items.parallelStream().anyMatch(inputStr::contains);
        
        try {
            return items.parallelStream().filter(inputStr::contains).findAny().get();
        } catch (Exception ex) {
            return null;
        }                   
    }   
    
    public static String getItemTypeString(String inputStr) {
        //boolean b = items.parallelStream().anyMatch(inputStr::contains);
        try {
            return itemTypes.parallelStream().filter(inputStr::contains).findAny().get();
        } catch (Exception ex) {
            return null;
        }                   
    }        
    
    public static ItemTypeResponse getItemType(String inputStr) {
        //boolean b = items.parallelStream().anyMatch(inputStr::contains);
        ItemTypeResponse response = new ItemTypeResponse(ItemCategory.none, "");
        try {
            response.setType(accessoryTypes.parallelStream().filter(inputStr::contains).findAny().get());
            response.setCategory(ItemCategory.accessory);
            return response;
        } catch (Exception ex) { }
        try {
            response.setType(armourTypes.parallelStream().filter(inputStr::contains).findAny().get());
            response.setCategory(ItemCategory.armour);
            return response;
        } catch (Exception ex) { }
        try {
            response.setType(currencyTypes.parallelStream().filter(inputStr::contains).findAny().get());
            response.setCategory(ItemCategory.currency);
            return response;
        } catch (Exception ex) { }
        try {
            response.setType(flaskTypes.parallelStream().filter(inputStr::contains).findAny().get());
            response.setCategory(ItemCategory.flask);
            return response;
        } catch (Exception ex) { }
        try {
            response.setType(gemTypes.parallelStream().filter(inputStr::contains).findAny().get());
            response.setCategory(ItemCategory.gem);
            return response;
        } catch (Exception ex) { }
        try {
            response.setType(jewelTypes.parallelStream().filter(inputStr::contains).findAny().get());
            response.setCategory(ItemCategory.jewel);
            return response;
        } catch (Exception ex) { }
        try {
            response.setType(mapTypes.parallelStream().filter(inputStr::contains).findAny().get());
            response.setCategory(ItemCategory.map);
            return response;
        } catch (Exception ex) { }
        try {
            response.setType(weaponTypes.parallelStream().filter(inputStr::contains).findAny().get());
            response.setCategory(ItemCategory.weapon);
            return response;
        } catch (Exception ex) { }
        try {
            response.setType(leaguestoneTypes.parallelStream().filter(inputStr::contains).findAny().get());
            response.setCategory(ItemCategory.leaguestone);
            return response;
        } catch (Exception ex) { }
        try {
            response.setType(prophecyTypes.parallelStream().filter(inputStr::contains).findAny().get());
            response.setCategory(ItemCategory.prophecy);
            return response;
        } catch (Exception ex) { }
        try {
            response.setType(capturedBeastTypes.parallelStream().filter(inputStr::contains).findAny().get());
            response.setCategory(ItemCategory.capturedBeasts);
            return response;
        } catch (Exception ex) { }
        try {
            response.setType(cardTypes.parallelStream().filter(inputStr::contains).findAny().get());
            response.setCategory(ItemCategory.card);
            return response;
        } catch (Exception ex) { }        
        return response;
    }        
  
//    public static boolean stringContainsItemFromList(String inputStr, String[] items) {
//        return Arrays.stream(items).parallel().anyMatch(inputStr::contains);
//    }    

//    public static Optional<String> stringContainsItemFromListTTT(String inputStr, String[] items) {
//        return Arrays.stream(items).parallel().filter(inputStr::contains).findAny();
//    }    
//    
//    public static String getMatchingItemFromList(String inputStr, String[] items) {
//        return Arrays.stream(items).parallel().filter(inputStr::contains).findAny();
//    }    

    
}
