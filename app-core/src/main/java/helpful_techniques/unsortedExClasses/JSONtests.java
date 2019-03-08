/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpful_techniques.unsortedExClasses;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import currencydata.CurrencyData;
import currencydata.POETradeCurrencyData;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.ssl.SSLContext;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.protocol.RequestAddCookies;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import poedotcom.PoEdotcomQueryResults;



/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
//IMPORTANT: POEdotcomQueryHandler_working_preCleanUp - is the working version of this class
//  kept this class itself to prevent losing information possibly important in the future (comments/fails/whatever)
public class JSONtests {
    //HttpClient httpclient = HttpClients.createDefault();
    private final org.apache.logging.log4j.Logger logger = LogManager.getLogger(JSONtests.class.getSimpleName());
    CloseableHttpClient httpclient = null;
    SSLContext sslcontext;
    //HttpPost httppost = new HttpPost("http://www.a-domain.com/foo/");
    //String postData = "";
    String postData = "{ \"query\": { \"status\": { \"option\": \"online\" }, \"name\": \"The Pariah\", \"type\": \"Unset Ring\", \"stats\": [{ \"type\": \"and\", \"filters\": [] }] }, \"sort\": { \"price\": \"asc\" } }";
//"    \"query\": {\n" +
//"        \"status\": {\n" +
//"            \"option\": \"online\"\n" +
//"        },\n" +
//"        \"name\": \"The Pariah\",\n" +
//"        \"type\": \"Unset Ring\",\n" +
//"        \"stats\": [{\n" +
//"            \"type\": \"and\",\n" +
//"            \"filters\": []\n" +
//"        }]\n" +
//"    },\n" +
//"    \"sort\": {\n" +
//"        \"price\": \"asc\"\n" +
//"    }\n" +
//"}";
    HttpHost target;
    public JSONtests() throws IOException {
        
        
        
    //source: https://stackoverflow.com/questions/14561293/sending-post-data-to-https-without-ssl-cert-verification-with-apache-httpclient
        /////////////////
        // Create SSL Client
        /////////////////

        //CloseableHttpClient httpclient = null;
        target = new HttpHost("www.pathofexile.com", 443, "https");

        sslcontext = SSLContexts.createSystemDefault();
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
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

        /////////////////
        // Send POST
        /////////////////

        HttpPost httppost = new HttpPost("/api/trade/search/Standard");
        //HttpPost httppost = new HttpPost("/api/trade/fetch/4b2da56feb1a0c39f7c74872947cd11c742f78d7a3442c8269aa0ea742c03b8b,da70945e5ea68a3d441e57490715d815c5401a668dba8a2357ecd0670c82f8fc?query=9kqyhK");
        //for json:
        /* source: https://stackoverflow.com/questions/12059278/how-to-post-json-request-using-apache-httpclient
        StringRequestEntity requestEntity = new StringRequestEntity(
            JSON_STRING,
            "application/json",
            "UTF-8");

        PostMethod postMethod = new PostMethod("http://example.com/action");    
        
        AND/OR - most likely OR
        
        StringEntity requestEntity = new StringEntity(
            JSON_STRING,
            ContentType.APPLICATION_JSON);

        HttpPost postMethod = new HttpPost("http://example.com/action");
        postMethod.setEntity(requestEntity);

        HttpResponse rawResponse = httpclient.execute(postMethod);        
        */
        //SEE start and end below - following lines werent in before
        StringEntity requestEntity = new StringEntity(
            postData,
            ContentType.APPLICATION_JSON);        
        httppost.setEntity(requestEntity);
        CloseableHttpResponse response = httpclient.execute(target, httppost);
        //START
//        ByteArrayEntity postDataEntity = new ByteArrayEntity(postData.getBytes());
//        httppost.setEntity(postDataEntity);
//        CloseableHttpResponse response = httpclient.execute(target, httppost);

        //END WORKS with empty string (same response as in browser!

        /////////////////
        // Get RESPONSE
        /////////////////

        try {
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
            //System.out.println(result);
            readResponse(result);
            EntityUtils.consume(entity);
        } finally {
                response.close();
        }        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
//        // Request parameters and other properties.
//        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
//        params.add(new BasicNameValuePair("param-1", "12345")); 
//        params.add(new BasicNameValuePair("param-2", "Hello!"));
//        //httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
//
//        //Execute and get the response.
//        HttpResponse response = httpclient.execute(httppost);
//        HttpEntity entity = response.getEntity();
//
//        if (entity != null) {
//            try (InputStream instream = entity.getContent()) {
//                // do something useful
//            } catch (IOException ex) {
//                Logger.getLogger(JSONtests.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (UnsupportedOperationException ex) {
//                Logger.getLogger(JSONtests.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }           
    }
    
    public CurrencyData readResponse(String source) {
       // try {
            
            //Reader reader = new FileReader(dataSource);
        //see https://stackoverflow.com/questions/5796948/how-to-parse-dynamic-json-fields-with-gson            
        
        
        
        try {
            Gson gson = new Gson();
        //    Type mapType = new TypeToken<Map<String, POETradeCurrencyDataEntry> >() {}.getType(); // define generic type  WORKS AS WELL when "jdaslkasdjl":"sadjkdjsakdasl" doesnt occur
        //    JsonParser jsonParser = new JsonParser();
        //    JsonReader reader = new JsonReader(new FileReader(dataSource));
        //    Map<String, POETradeCurrencyDataEntry>  as = gson.fromJson(jsonParser.parse(reader), mapType); //WORKS AS WELL when "jdaslkasdjl":"sadjkdjsakdasl" doesnt occur
            //Type mapType = new TypeToken<Map<String, Map<String,Integer> > >() {}.getType(); // WORKS! -> define generic type

        //Map<String, POETradeCurrencyDataEntry> result= gson.fromJson(jsonParser.parse(reader), mapType);
        //Map<String, Map<String,Integer> > as = gson.fromJson(jsonParser.parse(reader), mapType);// WORKS!
            
            //Type mapType = new TypeToken<Map<String, POETradeCurrencyData> >() {}.getType(); // define generic type  WORKS AS WELL when "jdaslkasdjl":"sadjkdjsakdasl" doesnt occur  
            Type mapType = new TypeToken<PoEdotcomQueryResults>() {}.getType();
            JsonParser jsonParser = new JsonParser();
            JsonReader reader = new JsonReader(new StringReader(source));
            //JsonObject jsonObj = gson.fromJson (jsonParser.parse(reader), JsonElement.class).getAsJsonObject();
            PoEdotcomQueryResults poeDotComQueryResults = gson.fromJson(jsonParser.parse(reader), mapType);
            //jsonObj.get(source);  //NEEDED?
            //https://www.pathofexile.com/api/trade/fetch/RESULT_LINES_HERE?query=ID_HERE
            //  where RESULT_LINES_HERE is all the elements in the 
            //  returned result array joined by comma (,) and the query parameter 
            //is the string returned as ID. So a complete fetch would be
            //String allresults = String.join(",", poeDotComQueryResults.getResult());
            String allresults = "";
            for (int i=0;i<2;i++)
                if (i>0)
                    //allresults += "," + poeDotComQueryResults.getResult()[i];
                    allresults += "," + poeDotComQueryResults.getResult().get(i);
                else
                    //allresults += poeDotComQueryResults.getResult()[i];//something,something
                    allresults += poeDotComQueryResults.getResult().get(i);//something,something
                    
            
            //String offerSearchURLstring = "www.pathofexile.com/api/trade/fetch/" + allresults + "?query=" + poeDotComQueryResults.getId();
            String offerSearchURLstring = "/api/trade/fetch/" + allresults + "?query=" + poeDotComQueryResults.getId();
            
            runMyTest(offerSearchURLstring);
                    
            queryPoEdotcom(offerSearchURLstring,"");
            //Set<Map.Entry<String, JsonElement>> entrySet = poeDotComQueryResults.entrySet();
            int i=0;
            //Hashtable<String key,T data> aa = new Hashtable();
            Hashtable<String, CurrencyData> poetcd = new Hashtable();

            //CurrencyData cl = new CurrencyData();
            //for(Map.Entry<String,JsonElement> entry : entrySet){
                //Map.Entry<String,JsonElement> entry
                //POETradeCurrencyDataEntry2 ab = new POETradeCurrencyDataEntry2(entry);
                //parseComplexJSON(entry);
//                System.out.println("this is entry #" + i +":" + entry +"\n");
//                if (entry.getValue().isJsonObject()) { 
//                    System.out.println("Key:"+entry.getKey()+"has Object Type:" +entry.getValue());
//
//                } else if (entry.getValue().isJsonPrimitive())
//                    System.out.println("Key:"+entry.getKey()+"has primitive Type:" +entry.getValue());
            //}
            //System.out.println("\"Success!!!\"");
            
            //return cl;
            
        } catch (Exception ex) {
            logger.log(org.apache.logging.log4j.Level.ERROR, ex);
            //System.out.println(ex);
        }
        return(null);
    }    
    public void parseComplexJSON(Map.Entry<String,JsonElement> entry) {
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
            //    System.out.println("found results!");
            //    for (String result : results)
            //        System.out.println(result+"\n");
            //    //return results;
            //}
        }

    }
    
    public void queryPoEdotcom(String httpPostString, String queryString) throws IOException {
        


        target = new HttpHost("www.pathofexile.com", 443, "https");

        sslcontext = SSLContexts.createSystemDefault();
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
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



        /////////////////
        // Send POST
        /////////////////
        HttpPost httpPost = new HttpPost(httpPostString); 
        StringEntity requestEntity = new StringEntity(
            queryString,
            ContentType.APPLICATION_JSON);        
        //httpPost.setEntity(requestEntity);
        CloseableHttpResponse response = null;
        try {
            
            
List<NameValuePair> params = new ArrayList<NameValuePair>();
params.add(new BasicNameValuePair("id", "10"));            
UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, "UTF-8");
httpPost.setEntity(ent);            
            
            //httpPost.set
            //httpclient.
            response = httpclient.execute(target, httpPost);
            
            //System.out.println(response.toString());
        //} catch (Exception ex) { 
        //    System.out.println(ex);
        //}
        //START
//        ByteArrayEntity postDataEntity = new ByteArrayEntity(postData.getBytes());
//        httppost.setEntity(postDataEntity);
//        CloseableHttpResponse response = httpclient.execute(target, httppost);

        //END WORKS with empty string (same response as in browser!

        /////////////////
        // Get RESPONSE
        /////////////////

        //try {
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity);
                //System.out.println(result);
                readResponse(result);
                EntityUtils.consume(entity);
        } catch (Exception ex) {  
            logger.log(org.apache.logging.log4j.Level.ERROR, ex);
            //System.out.println(ex);
        } finally {
                response.close();
        }                
    }
    
//    public void testUrl(String httpPostString, String queryString) throws IOException {
//        URLConnection connection = new URL("https://www.google.com/search?q=" + query).openConnection();
//        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
//        connection.connect();
//
//        BufferedReader r  = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));
//
//        StringBuilder sb = new StringBuilder();
//        String line;
//        while ((line = r.readLine()) != null) {
//            sb.append(line);
//        }
//        System.out.println(sb.toString());
//        
//        String cookie = connection.getHeaderField( "Set-Cookie").split(";")[0];
//        Pattern pattern = Pattern.compile("content=\\\"0;url=(.*?)\\\"");
//        Matcher m = pattern.matcher(response);
//        if( m.find() ) {
//            String url = m.group(1);
//            connection = new URL(url).openConnection();
//            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
//            connection.setRequestProperty("Cookie", cookie );
//            connection.connect();
//            r  = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));
//            sb = new StringBuilder();
//            while ((line = r.readLine()) != null) {
//                sb.append(line);
//            }
//            response = sb.toString();
//            pattern = Pattern.compile("<div id=\"resultStats\">About ([0-9,]+) results</div>");
//            m = pattern.matcher(response);
//            if( m.find() ) {
//                long amount = Long.parseLong(m.group(1).replaceAll(",", ""));
//                return amount;
//            }
//        
//    }



        public void runMyTest(String url) throws Exception {

            HttpClientContext context = HttpClientContext.create();
            CookieStore cookieStore = new BasicCookieStore();
            context.setCookieStore(cookieStore);

            //HttpGet get = new HttpGet("https://example.com/");
            HttpGet get = new HttpGet("https://www.pathofexile.com");
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(get, context);

            //HttpPost post = new HttpPost("https://example.com/potentially/harmful/path");
            HttpPost post = new HttpPost("https://www.pathofexile.com/api/trade/fetch/4b2da56feb1a0c39f7c74872947cd11c742f78d7a3442c8269aa0ea742c03b8b,da70945e5ea68a3d441e57490715d815c5401a668dba8a2357ecd0670c82f8fc?query=9kqyhK");
            List params = new ArrayList();
            params.add(new BasicNameValuePair("_csrf", getCookieValue(cookieStore, "_csrf")));
            //Add any other needed post parameters
            UrlEncodedFormEntity paramEntity = new UrlEncodedFormEntity(params);
            post.setEntity(paramEntity);
            //Make sure cookie headers are written 
            RequestAddCookies addCookies = new RequestAddCookies();
            addCookies.process(post, context);

            response = httpClient.execute(get, context);

            //System.out.println("Response HTTP Status Code:" +response.getStatusLine().getStatusCode());
            logger.log(org.apache.logging.log4j.Level.INFO, "Response HTTP Status Code:" +response.getStatusLine().getStatusCode());
            System.out.println(readResponse(response));
            
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
            System.out.println(result);
            readResponse(result);
            EntityUtils.consume(entity);
        }

        public String readResponse(CloseableHttpResponse response) 
            throws Exception {
                BufferedReader reader = null;
                String content = "";
                String line = null;
                HttpEntity entity = response.getEntity();

                reader = new BufferedReader(new InputStreamReader(entity.getContent())); 
                while ((line = reader.readLine()) != null) {
                    content += line;
                }
                // ensure response is fully consumed
                EntityUtils.consume(entity);
                return content;
        }

        public String getCookieValue(CookieStore cookieStore, String cookieName) {
            String value = null;
            for (Cookie cookie: cookieStore.getCookies()) {
                if (cookie.getName().equals(cookieName)) {
                    value = cookie.getValue();
                }
            }
            return value;
        }
//        public static final void main(final String[] args) throws Exception {
//            MyTest x = new MyTest();
//            x.runMyTest();
//        }
}
