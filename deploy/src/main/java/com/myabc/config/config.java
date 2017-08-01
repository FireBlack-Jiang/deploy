package com.myabc.config;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Logger;

public class config {
	public static String usrPath= System.getProperty("user.dir");
	public static String pathSeparator = File.separator;
	static String configPath = usrPath + pathSeparator +"Soucefiles"+pathSeparator+ "config.properties";
	public static Logger logger = Logger.getLogger(config.class);
	// 这个getPropertieByName效率比较慢
	public static String getPropertieByName(String propName)
	{
		String propValue = new String();
		Properties mSSoinsProp = new Properties();	
		
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(configPath));	
			mSSoinsProp.load(in);
			propValue = mSSoinsProp.getProperty(propName);
			in.close();		
		} catch (Exception e) {
			logger.info("读取config配置文件失败");
			e.printStackTrace();
			return "ERROR";
			
		}
		return propValue;
	}
}
