package com.myabc.Third;

import com.myabc.config.PropConfig;
import com.myabc.config.config;
import com.myabc.deploy.DesUtils;
import com.myabc.http.myhttp;
import com.myabc.logs.Log;

public class HttpThirdCaller implements ThirdCaller {
    public static String idCode=PropConfig.get("idCode");
    public static String pre_url=PropConfig.get("url");
    
	public String call(String thirdPkg, Object... args) {
		// TODO Auto-generated method stub	
		try {
			if(args.length>0)
			{
				String FunctionName=args[0].toString();
				String str_encrypt=DesUtils.Get_Instance().encrypt(thirdPkg);
				 //findArrearsByUserCode
				String str_url=pre_url+FunctionName+"&idCode="+idCode+"&paramStr="+str_encrypt;
				//Log.log.info(str_url);
				String get=myhttp.httpclient_post(str_url);
				//Log.log.info(DesUtils.Get_Instance().decrypt(get));
				return DesUtils.Get_Instance().decrypt(get);
			}
			else
			{
				
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return null;
	}

}
