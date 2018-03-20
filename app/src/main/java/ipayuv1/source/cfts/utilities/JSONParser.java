package ipayuv1.source.cfts.utilities;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParser {
 
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = null;
    static String agent = System.getProperty("http.agent");
 
    // constructor
    public JSONParser() {
 
    }
    
    public JSONObject makeHttpRequest(String url,
            JSONObject params) {

            String json="";
         try {


     	 		//url = "http://fams.zz.mu/mobile/" + url;
        	 	//url = url;
     	 		DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader("User-Agent", agent);

                json = params.toString();
                StringEntity se = new StringEntity(json);
                httpPost.setEntity(se);

                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();

                is = httpEntity.getContent();
               
        } catch (Exception e){
        	Log.e("HttpError", e.toString());
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
            //Log.d("ParserString", json);
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
 
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
            Log.i("tagconvertstr", "["+json+"]");
        }
        return jObj;
    }
    
    public JSONObject makeHttpRequest(String url) {
        try {     	     
       	 	//url = "http://fams.zz.mu/mobile/" + url;
            url = url;
       		DefaultHttpClient httpClient = new DefaultHttpClient();
               HttpPost httpPost = new HttpPost(url);
               httpPost.setHeader("User-Agent", agent);

               HttpResponse httpResponse = httpClient.execute(httpPost);
               HttpEntity httpEntity = httpResponse.getEntity();
               is = httpEntity.getContent();
              
       } catch (Exception e){
       	Log.e("HttpError", e.toString());
       }
       try {
           BufferedReader reader = new BufferedReader(new InputStreamReader(
                   is, "iso-8859-1"), 8);
           StringBuilder sb = new StringBuilder();
           String line = null;
           while ((line = reader.readLine()) != null) {
               sb.append(line + "\n");
           }
           is.close();
           json = sb.toString();
           //Log.d("ParserString", json);
       } catch (Exception e) {
           Log.e("Buffer Error", "Error converting result " + e.toString());
       }

       try {
           jObj = new JSONObject(json);
       } catch (JSONException e) {
           Log.e("JSON Parser", "Error parsing data " + e.toString());
           Log.i("tagconvertstr", "["+json+"]");
       }
       return jObj;
   }

}
