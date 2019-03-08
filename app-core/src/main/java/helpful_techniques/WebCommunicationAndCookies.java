/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpful_techniques;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.protocol.RequestAddCookies;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
public class WebCommunicationAndCookies {
   //source: http://www.simplenetworks.io/blog/2015/5/26/managing-csrf-tokens-in-apache-httpclient-44x
    public void runMyTest(String url) throws Exception {

        HttpClientContext context = HttpClientContext.create();
        CookieStore cookieStore = new BasicCookieStore();
        context.setCookieStore(cookieStore);

        //HttpGet get = new HttpGet("https://example.com/");
        HttpGet get = new HttpGet("https://www.pathofexile.com");
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(get, context);

        //HttpPost post = new HttpPost("https://example.com/potentially/harmful/path");
        //HttpPost post = new HttpPost("https://www.pathofexile.com" + url);
        HttpGet post = new HttpGet("https://www.pathofexile.com" + url);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("_csrf", getCookieValue(cookieStore, "_csrf")));
        //Add any other needed post parameters
        //UrlEncodedFormEntity paramEntity = new UrlEncodedFormEntity(params);
        StringEntity paramEntity = new StringEntity("",ContentType.APPLICATION_JSON);

        //post.setEntity(paramEntity);
        //Make sure cookie headers are written 
        RequestAddCookies addCookies = new RequestAddCookies();
        addCookies.process(post, context);

//        StringEntity requestEntity = new StringEntity(
//            postData,
//            ContentType.APPLICATION_JSON);        
//        httppost.setEntity(requestEntity);
//        CloseableHttpResponse response = httpclient.execute(target, httppost)            


        response = httpClient.execute(post, context);

        System.out.println("Response HTTP Status Code:" +response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity);
        System.out.println(result);            
        System.out.println(readResponse(response));


//            HttpEntity entity = response.getEntity();
//            String result = EntityUtils.toString(entity);
        System.out.println(result);
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
}
