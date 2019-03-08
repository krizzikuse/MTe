/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpful_techniques;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import currencydata.POETradeCurrencyData;
import java.lang.reflect.Type;
import java.util.Map;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
public class ComplexJSONParser {
    public static void parseComplexJSON(Map.Entry<String,JsonElement> entry) {
        Gson gson = new Gson();
        //Map.Entry<String,JsonElement> entry
        if (entry.getValue().isJsonObject()) {
            //System.out.println("Key:"+entry.getKey()+"has Object Type:" +entry.getValue());
            //entry.getKey()

            Type mapType = new TypeToken<POETradeCurrencyData>(){}.getType();
            POETradeCurrencyData result= gson.fromJson(entry.getValue(), mapType);    //needs to be parsed 2 times, because it can either be structure or string...

            //poeTradeData.put(entry.getKey(), result);

        } else if (entry.getValue().isJsonPrimitive()) {
            //System.out.println("Key:"+entry.getKey()+"has primitive Type:" +entry.getValue());
            String text = gson.fromJson(entry.getValue(), String.class);

            //poeTradeData.put(entry.getKey(), new POETradeCurrencyData(null,null,text));
        //data.put(key, value)
        } else if (entry.getValue().isJsonArray()) {
            Type mapType = new TypeToken<String[]>(){}.getType();
            String[] results = gson.fromJson(entry.getValue(), mapType);   
            //if (entry.getKey().equals("result")) {
                //System.out.println("found results!");
                //for (String result : results)
                //    System.out.println(result+"\n");
                //return results;
            //}
        }
    }    
}
