package com.myabc.deploy;
import javax.sql.rowset.serial.SerialException;

/** 
 * http请求返回结果 
 * Created by sff on 2017/3/3. 
 */  
public class HttpClientResponse {  
    //返回结果是否正确  
    private boolean isSuccess;  
    //返回的结果  
    private String content ;  
    //请求异常时提示信息  
    private String msg;  
    //请求异常  
    private Throwable throwable;  
  
    public HttpClientResponse() {  
  
    }  
    public HttpClientResponse(String content) {  
        this.content = content;  
        this.isSuccess = true;  
    }  
  
    public HttpClientResponse(String msg, Throwable cause) {  
        this.isSuccess = false;  
        this.msg = msg;  
        this.throwable = cause;  
    }  
  
    public HttpClientResponse(String content, boolean isSuccess, String msg, SerialException cause) {  
        this.content = content;  
        this.isSuccess = isSuccess;  
        this.msg = msg;  
        this.throwable = cause;  
    }  
  
    public Throwable getThrowable() {  
        return throwable;  
    }  
  
    public void setThrowable(Throwable throwable) {  
        this.throwable = throwable;  
    }  
  
    public boolean isSuccess() {  
        return isSuccess;  
    }  
  
    public void setSuccess(boolean success) {  
        isSuccess = success;  
    }  
  
    public String getContent() {  
        return content;  
    }  
  
    public void setContent(String content) {  
        this.content = content;  
    }  
  
    public String getMsg() {  
        return msg;  
    }  
  
    public void setMsg(String msg) {  
        this.msg = msg;  
    }  
}  
