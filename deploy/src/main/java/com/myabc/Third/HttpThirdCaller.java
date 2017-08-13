package com.myabc.Third;

import com.myabc.config.config;
import com.myabc.deploy.DesUtils;
import com.myabc.http.myhttp;

public class HttpThirdCaller implements ThirdCaller {

	public String call(String thirdPkg, Object... args) {
		// TODO Auto-generated method stub
		String str_Joson;
		try {
			str_Joson = DesUtils.Get_Instance().encrypt(thirdPkg);
			String str_url=config.getPropertieByName("url")+config.getPropertieByName("idCode")+"&paramStr="+str_Joson;
			System.out.println(str_url);
			String get=myhttp.httpclient_post(str_url);
			System.out.println(DesUtils.Get_Instance().decrypt(get));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return null;
	}

}
