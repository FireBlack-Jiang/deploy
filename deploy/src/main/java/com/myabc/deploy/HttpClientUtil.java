package com.myabc.deploy;

import org.apache.commons.httpclient.*;  
import org.apache.commons.httpclient.methods.DeleteMethod;  
import org.apache.commons.httpclient.methods.GetMethod;  
import org.apache.commons.httpclient.methods.PostMethod;  
import org.apache.commons.httpclient.methods.PutMethod;  
import org.apache.commons.httpclient.params.HttpMethodParams;  
import sun.net.www.protocol.http.HttpURLConnection;  
import java.io.*;  
import java.net.URL;  
import java.util.ArrayList;  
import java.util.List;  
import java.util.Map;  
import java.io.IOException;  
import java.io.InputStream;  
 
/** 
* http对象， 
* Created by sff on 2017/2/28. 
*/  
public class HttpClientUtil{  
 
   /** 
    * get请求   这里面的请求的数据要用@QueryParam来获取，@FormParam获取不到 
    * @param url 
    * @param params 
    * @return 
    */  
   public static  HttpClientResponse getHttp( String url, Map<String, String> params){  
       return getHttp(url, params, 60000);  
   }  
   public static  HttpClientResponse getHttp(String url, Map<String, String> params, int timeout){  
       HttpClient httpClient = new HttpClient();  
       GetMethod getMethod = new GetMethod(url);  
       getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());  
       getMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");  
       List<NameValuePair> list = new ArrayList<NameValuePair>();  
       for(Map.Entry<String, String> param : params.entrySet()){  
           list.add(new NameValuePair(param.getKey(), param.getValue()));  
       }  
       NameValuePair[] paramsArray = new NameValuePair[list.size()];  
       for(int i = 0; i < list.size(); i++){  
           paramsArray[i] = list.get(i);  
       }  
       /** 
        * 如果是用 setQueryString(paramsArray);方法的在restful的java端是用@QueryParam 获取的 
        * 如果是用  postMethod.addParameter(new NameValuePair(entry.getKey(), entry.getValue()));;方法的在restful的java端是用@FormParam 获取的 
        * */  
       getMethod.setQueryString(paramsArray);  
       //链接超时  
       httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);  
       //读取超时  
       httpClient.getHttpConnectionManager().getParams().setSoTimeout(timeout);  
       return httpConnect(httpClient, getMethod);  
   }  
 
   /** 
    * post请求  这里面的请求的数据要用@FormParam来获取，@QueryParam获取不到 
    * @param url 
    * @param params 
    * @return 
    */  
   public static HttpClientResponse postHttp(String url, Map<String, String> params){  
       return postHttp(url, params, 60000);  
   }  
   public static  HttpClientResponse postHttp( String url, Map<String, String> params, int timeout){  
       HttpClient httpClient = new HttpClient();  
       httpClient.getParams().setContentCharset("UTF-8");  
       PostMethod postMethod = new PostMethod(url);  
       for (Map.Entry<String, String> entry : params.entrySet()) {  
           postMethod.addParameter(new NameValuePair(entry.getKey(), entry.getValue()));  
       }  
       //链接超时  
       httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);  
       //读取超时  
       httpClient.getHttpConnectionManager().getParams().setSoTimeout(timeout);  
       return httpConnect(httpClient, postMethod);  
   }  
 
   /** 
    * put请求   这里面的请求的数据要用@QueryParam来获取，@FormParam获取不到 
    * @param url 
    * @param params 
    * put请求问题，这里的put请求，传参的时候，putmethod只有setQueryString方法，这样的话在restful后台只能用@QueryParam来获取 
    *              就不能用@FormParam来获取参数，但是前端的js($.ajax)如果用put请求的话，是要用@FormParam获取参数，所以这里出现了问题 
    * @return 
    */  
   @Deprecated  
   public static HttpClientResponse putHttp(String type, String url, Map<String, String> params){  
       return putHttp(url, params, 60000);  
   }  
   @Deprecated  
   public static HttpClientResponse putHttp(String url, Map<String, String> params, int timeout){  
       List<NameValuePair> list = new ArrayList<NameValuePair>();  
       for(Map.Entry<String, String> param : params.entrySet()){  
           list.add(new NameValuePair(param.getKey(), param.getValue()));  
       }  
       HttpClient httpClient = new HttpClient();  
       PutMethod putMethod = new PutMethod(url);  
       NameValuePair[] paramsArray = new NameValuePair[list.size()];  
       for(int i = 0; i < list.size(); i++){  
           paramsArray[i] = list.get(i);  
       }  
       putMethod.setQueryString(paramsArray);  
       putMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");  
       //链接超时  
       httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);  
       //读取超时  
       httpClient.getHttpConnectionManager().getParams().setSoTimeout(timeout);  
       return httpConnect(httpClient, putMethod);  
   }  
 
   /** 
    * delete请求  这里面的请求的数据要用@QueryParam来获取，@FormParam获取不到 
    * @param url 
    * @param params 
    * 和put请求一模一样，前端js访问要@FormParam获取参数，@QueryParam获取不到js传的参数。 
    * @return 
    */  
   @Deprecated  
   public static HttpClientResponse deleteHttp(String url, Map<String, String> params){  
       return deleteHttp(url, params, 60000);  
   }  
   @Deprecated  
   public static HttpClientResponse deleteHttp(String url, Map<String, String> params, int timeout){  
       List<NameValuePair> list = new ArrayList<NameValuePair>();  
       for(Map.Entry<String, String> param : params.entrySet()){  
           list.add(new NameValuePair(param.getKey(), param.getValue()));  
       }  
       NameValuePair[] paramsArray = new NameValuePair[list.size()];  
       for(int i = 0; i < list.size(); i++){  
           paramsArray[i] = list.get(i);  
       }  
       HttpClient httpClient = new HttpClient();  
       DeleteMethod deleteMethod = new DeleteMethod(url);  
       deleteMethod.setQueryString(paramsArray);  
       deleteMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");  
       //链接超时  
       httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);  
       //读取超时  
       httpClient.getHttpConnectionManager().getParams().setSoTimeout(timeout);  
       return httpConnect(httpClient, deleteMethod);  
   }  
 
   public static HttpClientResponse httpConnect(HttpClient httpClient, HttpMethodBase httpMethodBase){  
       HttpClientResponse httpClientResponse = new HttpClientResponse();  
       httpClientResponse.setSuccess(true);  
       try{  
           httpClient.executeMethod(httpMethodBase);  
           ByteArrayOutputStream out = new ByteArrayOutputStream();  
           InputStream in = httpMethodBase.getResponseBodyAsStream();  
           InputStreamReader isr = new InputStreamReader(in);  
           BufferedReader br = new BufferedReader(isr);//为输入流添加缓冲  
           String content = "";  
           String info = "";  
           while((info = br.readLine())!= null) {  
               content = content + info;  
           }  
           return new HttpClientResponse(content);  
       } catch (HttpException e){  
           e.printStackTrace();  
           httpClientResponse.setSuccess(false);  
           httpClientResponse.setMsg(e.getMessage());  
           httpClientResponse.setThrowable(e);  
       } catch (IOException e){  
           e.printStackTrace();  
           httpClientResponse.setSuccess(false);  
           httpClientResponse.setMsg(e.getMessage());  
           httpClientResponse.setThrowable(e);  
       } catch (Exception e){  
           e.printStackTrace();  
           httpClientResponse.setSuccess(false);  
           httpClientResponse.setMsg(e.getMessage());  
           httpClientResponse.setThrowable(e);  
       }  finally{  
           //释放连接  
           httpMethodBase.releaseConnection();  
       }  
       return httpClientResponse;  
   }  
 
   /** 
    * put请求   这里面的请求的数据要用@FormParam来获取，@QueryParam获取不到 
    * @param url 
    * @param params 
    * @return 
    */  
   public static HttpClientResponse putHttpURLConnection(String url, Map<String, String> params){  
       return putDeleteHttpConnect("PUT", url, params, 60000);  
   }  
   public static HttpClientResponse putHttpURLConnection(String url, Map<String, String> params, int timeout){  
       return putDeleteHttpConnect("PUT", url, params, timeout);  
   }  
 
   /** 
    * delete请求   这里面的请求的数据要用@FormParam来获取，@QueryParam获取不到 
    * @param url 
    * @param params 
    * @return 
    */  
   public static HttpClientResponse deleteHttpURLConnection(String url, Map<String, String> params){  
       return putDeleteHttpConnect("DELETE", url, params, 60000);  
   }  
   public static HttpClientResponse deleteHttpURLConnection(String url, Map<String, String> params, int timeout){  
       return putDeleteHttpConnect("DELETE", url, params, timeout);  
   }  
 
   public static HttpClientResponse putDeleteHttpConnect(String type, String url, Map<String, String> params, int timeout){  
       HttpURLConnection conn = null;  
       try{  
           URL url1 = new URL(url);  
           //打开restful链接  
           conn = (HttpURLConnection) url1.openConnection();  
           // 提交模式  
           conn.setRequestMethod(type);//POST GET PUT DELETE 必须大写PUT，put为错误的  
           //设置访问提交模式，表单提交  
           conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");  
           conn.setConnectTimeout(timeout);//连接超时 单位毫秒  
           conn.setReadTimeout(timeout);//读取超时 单位毫秒  
           conn.setDoOutput(true);// 是否输入参数  
           StringBuffer paramsBuffer = new StringBuffer();  
           // 表单参数与get形式一样  
           for (Map.Entry<String, String> entry : params.entrySet()) {  
               paramsBuffer.append(entry.getKey()).append("=").append(entry.getValue());  
           }  
           byte[] bypes = paramsBuffer.toString().getBytes();  
           conn.getOutputStream().write(bypes);// 输入参数  
           //读取请求返回值  
           InputStream inStream=conn.getInputStream();  
           InputStreamReader isr = new InputStreamReader(inStream);  
           BufferedReader br = new BufferedReader(isr);//为输入流添加缓冲  
           String content = "";  
           String info = "";  
           while((info = br.readLine())!= null) {  
               content = content + info;  
           }  
           return new HttpClientResponse(new String(content.getBytes(), "UTF-8"));  
       } catch (Exception e){  
           return new HttpClientResponse(e.getMessage(), e);  
       } finally {  
           if(conn != null){  
               conn.disconnect();  
           }  
       }  
   }  
}  
