package com.myabc.deploy;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import com.google.gson.Gson;
import com.myabc.config.config;
import com.myabc.logs.Log;

import coom.myabc.http.myhttp;

public class work implements Job{
    //046413350  46400448
	static long i=8070961;
	String result,yhhm,qryString;
	static String url=config.getPropertieByName("url")+config.getPropertieByName("idCode")+"&paramStr=";
	public void execute(JobExecutionContext arg0)
	{
		/**
008070961
008071635
008071643
008071665
		 */
		
		yhhm=String.format("%09d",i );
		System.out.println("用户编号："+yhhm);
		i++;
		//qryString="{yhwd:\"004\",yhgy:\"0001\",gsdm: \"7001\",yhbm:\""+yhhm+"\"}";
		qryString="{yhwd:\"004\",yhgy:\"0001\",gsdm: \"7001\",yhbm:\""+yhhm+"\"}";
//		System.out.println("查询字符串："+qryString);
//		System.out.println("url:"+url);
		try {
			String str_Joson=DesUtils.Get_Instance().encrypt(qryString);
			String str_url=url+str_Joson;
			//System.out.println(str_url);
			String get=myhttp.httpclient_post(str_url);
			result=DesUtils.Get_Instance().decrypt(get);
			Gson gson = new Gson();
			Result myresult=gson.fromJson(result, Result.class);
			if(myresult.getResultCode().compareTo("0000")==0)
			{
				Log.log.error("找到用户号："+i);
			}
			System.out.println(myresult.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	class Result{
		@Override
		public String toString() {
			return "Result [errInfo=" + errInfo + ", gsdm=" + gsdm
					+ ", isBankWart=" + isBankWart + ", jflx=" + jflx
					+ ", resultCode=" + resultCode + ", yhgy=" + yhgy
					+ ", yhwd=" + yhwd + "]";
		}
		String errInfo;
		String gsdm,isBankWart,jflx,resultCode,yhgy,yhwd;
		public String getErrInfo() {
			return errInfo;
		}
		public void setErrInfo(String errInfo) {
			this.errInfo = errInfo;
		}
		public String getGsdm() {
			return gsdm;
		}
		public void setGsdm(String gsdm) {
			this.gsdm = gsdm;
		}
		public String getIsBankWart() {
			return isBankWart;
		}
		public void setIsBankWart(String isBankWart) {
			this.isBankWart = isBankWart;
		}
		public String getJflx() {
			return jflx;
		}
		public void setJflx(String jflx) {
			this.jflx = jflx;
		}
		public String getResultCode() {
			return resultCode;
		}
		public void setResultCode(String resultCode) {
			this.resultCode = resultCode;
		}
		public String getYhgy() {
			return yhgy;
		}
		public void setYhgy(String yhgy) {
			this.yhgy = yhgy;
		}
		public String getYhwd() {
			return yhwd;
		}
		public void setYhwd(String yhwd) {
			this.yhwd = yhwd;
		}
	}
	
}
