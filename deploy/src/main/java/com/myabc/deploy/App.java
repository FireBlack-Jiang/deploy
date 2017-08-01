package com.myabc.deploy;


import org.apache.log4j.Logger;

import com.myabc.config.config;

import coom.myabc.http.myhttp;

/**
 * @deprecated 工具
 * @author FireBlack-Jiang
 */
public class App 
{	
    public static void main( String[] args )
    {
//    	myhttp.httpclient_post("http://110.186.74.48:7001/qjyj/payinterface.do?method=findArrearsByUserCode&idCode=b7f514398c9271ff5330286c8ec3b07d&paramStr=65efb4f6e880b9cf652d34638b09fb5832c79250c71fe6a6026a96de94feca78e14303f628aa248f64929ccbca47d98afef0a6408adebb3babcaef803d8b3da5");
//    	QuartzManager.addJob("ftp", "com.myabc.ftp.job_ftp", "0/10 * * * * ?");    
    	QuartzManager.addJob("gas", "com.myabc.deploy.work", "0/10 * * * * ?");    
    	   
    }
}
