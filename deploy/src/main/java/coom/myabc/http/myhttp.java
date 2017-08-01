package coom.myabc.http;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import com.myabc.logs.Log;

public class myhttp {

	public static void httpclient_get(String url)
    {
   	 HttpGet request = new HttpGet(url);
   	 try {
   	 HttpClient df=new DefaultHttpClient();
   	 df.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
   	 
   	 //df.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);
   	 //request.addHeader("Accept-Language", "zh");
   	 HttpResponse response = df.execute(request);
   	 if(response.getStatusLine().getStatusCode() == 200){ 
   	   Log.log.info("链接OK");
   	 HttpEntity entity=response.getEntity();
	   	Log.log.info(EntityUtils.toString(entity, "UTF-8"));
   	 }
   	  
   	 } catch (Exception e) { 
   		 Log.log.info("链接BAD"+e);
   		e.printStackTrace();
   	 }
   	 
    }
	public static void httpclient_post(String url,String requestStr)
	{
		
		HttpPost request=new HttpPost(url);
		 try {
		   	 HttpClient df=new DefaultHttpClient();
		   	 df.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
		   	request.setEntity(new StringEntity(requestStr, ContentType.create("application/html", Consts.UTF_8)));
		   	 //df.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);
		   	 //request.addHeader("Accept-Language", "zh");
		   	 HttpResponse response = df.execute(request);
		   	 if(response.getStatusLine().getStatusCode() == 200){ 
		   	  
//		   	   HttpEntity entity=response.getEntity();
		   	 }
		   	  
		   	 } catch (Exception e) { 
		   		 Log.log.info("链接BAD"+e);
		   	 }
	}
	public static String httpclient_post(String url)
	{
		
		HttpPost request=new HttpPost(url);
		 try {
		   	 HttpClient df=new DefaultHttpClient();
		   	 df.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
		   	 //df.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);
		   	 //request.addHeader("Accept-Language", "zh");
		   	 HttpResponse response = df.execute(request);
		   	 if(response.getStatusLine().getStatusCode() == 200){ 
		   	   HttpEntity entity=response.getEntity();
		   	  return EntityUtils.toString(entity, "GBK");  
		   	 }
		   	  Log.log.info(response.getStatusLine().getStatusCode());
		   	 } catch (Exception e) { 
		   		 Log.log.info("链接BAD"+e);
		   	 }
		return "";
	}
}
