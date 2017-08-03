package com.myabc.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import com.google.code.regexp.Matcher;
import com.google.code.regexp.Pattern;

public class PropConfig {
	private static final String FILENAME = config.configPath;
	public static boolean REMOVEBLANK=false;
	private static final Properties PROPS = loadConfigFile();

	private static Properties loadConfigFile() {
		String line = null;
		String key = null;
		StringBuilder values = new StringBuilder();
		Properties props = new Properties();
		String usrPath=System.getProperty("user.dir");
		try {
//			BufferedReader reader = new BufferedReader(new InputStreamReader(
//					new FileInputStream(PropConfig.class.getResource("/")
//							.getPath() + FILENAME), "UTF-8"));
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(FILENAME), "utf-8"));
			while ((line = reader.readLine()) != null) {
				String str = line.trim();
				if (str.startsWith("#")) {
					continue;
				}
				if (str.startsWith("[") && str.endsWith("]")) {
					if (key != null) { // save last key/value
						props.put( key,formatVar(values.toString().trim()));//trim(): remove last "\n" at the end
					}
					key = str.substring(1, str.length() - 1).trim();
					values.setLength(0);
				}else if(str.equals("")){
					continue;//ignore empty line
				} else {
					values.append(line).append("\n");
				}
			}

			if (key != null) { // save last key/value
				props.put(key, formatVar(values.toString().trim()));
			}

			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		System.out.println("加载配置完成:"+props);
		return props;
	}
	
	/**
	 * 根据config.properties中的配置名获取配置值
	 * @param key 配置名
	 * @return 配置值
	 */
	public static String get(String key){
		return PROPS.getProperty(key);
	}
	
	private static String formatVar(String propValue) {
		// 替换模板中的${变量名}为(?<变量名>.*?)，以便后续使用正则表达式
		Matcher m = Pattern.compile("\\$\\{(.*?)\\}").matcher(propValue);

		StringBuffer sb = new StringBuffer();

		while (m.find()) {
			String param = m.group(1);
			String value = "(?<"+param+">.*?)";

			m.appendReplacement(sb, value);
		}

		m.appendTail(sb);
		
		if(REMOVEBLANK==true){
			m=Pattern.compile("\\s*|\t|\r|\n").matcher(sb.toString());
			return m.replaceAll("");
		}
		
		return sb.toString();
	}

}
