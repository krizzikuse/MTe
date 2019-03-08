/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poeninja;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import static helpful_techniques.FileHelper.getPath;
import helpful_techniques.JSONBeautifier;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.SSLContext;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import poedotcom.PoEdotcomOffers;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
public class PoENinjaCommunicator {
    private final org.apache.logging.log4j.Logger logger = LogManager.getLogger(PoENinjaCommunicator.class.getSimpleName());

    
    CloseableHttpClient httpclient = null;
    SSLContext sslcontext;
    String postData = "";
    HttpHost target;
    Gson gson = new Gson();
    SSLConnectionSocketFactory sslConnectionSocketFactory;    
    public PoENinjaCommunicator() throws IOException {
        /////////////////
        // Create SSL Client
        /////////////////
        target = new HttpHost("poe.ninja", 443, "https");
        sslcontext = SSLContexts.createSystemDefault();
        sslConnectionSocketFactory = new SSLConnectionSocketFactory(
                sslcontext, new String[] { "TLSv1", "SSLv3" }, null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());

        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", sslConnectionSocketFactory)
                .build();
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);

        httpclient = HttpClients.custom()
        .setSSLSocketFactory(sslConnectionSocketFactory)
        .setConnectionManager(cm)
        .build();        
    }
    
    public enum POENINJACATEGORIES {
        CurrencyRates
        
    }
    
    public Map<String,Double> getCurrencyExchangeRates() throws IOException {
        //poe.ninja/api/Data/GetCurrencyOverview?league=Betrayal
        JsonObject result = getPoENinjadata("/api/Data/GetCurrencyOverview?league=Standard",true,
                        "Enhancements_config/json/"
                        + "PoE.ninja.API.Data.CurrencyOverview.Standard.json"); 
        
        return(decipherRates(result.toString()));
        
        
    }
    
    // Queries currency exchange rates from poe.ninja
    //   https://poe.ninja/api/Data/GetCurrencyOverview?league=Betrayal
    public JsonObject getPoENinjadata(String httpPostString, boolean write2File,String filePath) throws IOException {
        //https://poe.ninja/api/Data/GetCurrencyOverview?league=Betrayal
        /////////////////
        // Send GET
        /////////////////
        HttpGet httpPost = new HttpGet(httpPostString);    
        CloseableHttpResponse response = null;
        response = httpclient.execute(target, httpPost);
            
        /////////////////
        // Get RESPONSE
        /////////////////
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity);
        logger.log(Level.DEBUG,result); 
//        System.out.println(result); 
        
        EntityUtils.consume(entity);

        gson = new Gson();
        //Type mapType = new TypeToken<CurrencyOverviewResult>() {}.getType();
        JsonParser jsonParser = new JsonParser();
        JsonReader reader = new JsonReader(new StringReader(result));
        JsonObject jsonObj = gson.fromJson (jsonParser.parse(reader), JsonElement.class).getAsJsonObject();   //do like this to parse anything (for example to beatufy, write to file and then model after the file :)
        //CurrencyOverviewResult exchangeRates = gson.fromJson(jsonParser.parse(reader), mapType);
        
        response.close();
        
        logger.log(Level.DEBUG,"+++++++++++++++++ now showing all data queried "
                + "from www.pathofexile.com/api/trade/data/stats +++++++++++++++++");
//        System.out.println("+++++++++++++++++ now showing all data queried "
//                + "from www.pathofexile.com/api/trade/data/stats +++++++++++++++++");
        //System.out.println(JSONBeautifier.fomat(modStats.toString()));
        String jsonString = JSONBeautifier.format(jsonObj.toString());
        logger.log(Level.DEBUG,jsonString);
        logger.log(Level.DEBUG,"+++++++++++++++++ done showing all data queried "
                + "from www.pathofexile.com/api/trade/data/stats +++++++++++++++++");
//        System.out.println(jsonString);
//        System.out.println("+++++++++++++++++ done showing all data queried "
//                + "from www.pathofexile.com/api/trade/data/stats +++++++++++++++++");
        
        if (write2File) {
            // poe.ninja/api/Data/GetCurrencyOverview?league=Betrayal
            PrintWriter out = new PrintWriter(getPath(filePath));
            out.println(jsonString);
            out.flush();
            out.close();
        }
        
        return(jsonObj);
    }    
    
    private Map<String,Double> decipherRates(String jsonStr) {
        gson = new Gson();
        Type mapType = new TypeToken<CurrencyOverviewResult>() {}.getType();
        JsonParser jsonParser = new JsonParser();
        JsonReader reader = new JsonReader(new StringReader(jsonStr));
        CurrencyOverviewResult exchangeRates = gson.fromJson(jsonParser.parse(reader), mapType);
        
        //for now we just make a Map that contains currencyType (key)
        // and chaos-equivalency (value)
        //  => we dont interpret all data in CurrencyOverview
        Map<String,Double> chaosEquivalency = new HashMap<String,Double>();
        for (CurrencyExchangeEntry currex : exchangeRates.getLines()) {
            chaosEquivalency.put(currex.getCurrencyTypeName().toLowerCase(), currex.getChaosEquivalent());
        }
        return(chaosEquivalency);
        
        // I guess none of that is needed any longer,
        //  since i modelled the whole result
//        Map<Integer,CurrencyDetails> idMap = new HashMap<Integer,CurrencyDetails>();        
//        for (CurrencyDetails curr : exchangeRates.getCurrencyDetails()) {
//            idMap.put(curr.getId(), curr);
//        }
//        Map<String,Double> chaosEquivalency = new HashMap<String,Double>();
//        for (CurrencyExchangeEntry currex : exchangeRates.getLines()) {
//            chaosEquivalency.put(currex.getCurrencyTypeName(), Double.NaN)
//        }
        
                /*
        Gson gson = new Gson();
        Type mapType = new TypeToken<PoEdotcomOffers>() {}.getType();
        JsonParser jsonParser = new JsonParser();
        JsonReader reader = new JsonReader(new StringReader(result));
        //JsonObject jsonObj = gson.fromJson (jsonParser.parse(reader), JsonElement.class).getAsJsonObject();
        PoEdotcomOffers poedotComOffers = gson.fromJson(jsonParser.parse(reader), mapType);          
        */
    }
}
