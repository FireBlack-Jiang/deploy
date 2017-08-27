package com.myabc.swtich;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.github.tsohr.JSONObject;
import com.google.code.regexp.Matcher;
import com.google.code.regexp.Pattern;
import com.google.gson.Gson;
import com.myabc.config.PropConfig;
import com.myabc.logs.Log;

public class PkgConverter {
	/**
	 * 根据输入报文及其格式抽取交易要素
	 * @param inPkg 输入报文
	 * @param inFormat 输入报文格式
	 * @return 存放交易要素的哈希映射
	 */
	public static Map<String, String> extractParaMap(String inPkg,String inFormat){
//		System.out.println("inFormat:"+inFormat);
//		System.out.println("escapeExprSpecialWord(inFormat):"+escapeExprSpecialWord(inFormat));
		 Map<String, String> paraMap = Pattern.compile(escapeExprSpecialWord(inFormat)).matcher(inPkg).namedGroups();
		if(paraMap.isEmpty())
		{
			Log.log.error("error: regexp not match");
			Log.log.error("inPkg:\n"+inPkg);
			Log.log.error("inFormat:\n"+inFormat);
			paraMap=null;
		}
		return paraMap;
	}
	/**
	 * 根据交易要素渲染输出报文模板，生成输出报文
	 * @param paraMap 交易要素
	 * @param outFormat 输出报文模板
	 * @return 输出报文
	 */
	public static String renderOutPkg( Map<String, String> paraMap, String outFormat) {
		Matcher m = Pattern.compile("\\(\\?<(.*?)>\\.\\*\\?\\)").matcher(outFormat);

		StringBuffer outPkg = new StringBuffer();

		while (m.find()) {
			String param=m.group(1);
			String value = paraMap.get(param);
			m.appendReplacement(outPkg, value == null ? "" : value);
		}

		m.appendTail(outPkg);

		return outPkg.toString();
	}
	/** 
	 * 转义正则特殊字符 （$()*+.[]?\^{},|） 
	 *  
	 * @param keyword 
	 * @return 
	 */  
	public static String escapeExprSpecialWord(String keyword) {  
	    if (keyword!=null && !keyword.equals("")) {  
//	        String[] fbsArr = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|" };
	    	String[] fbsArr = { "\\", "$", "[", "]", "^", "{", "}", "|" };
	        for (String key : fbsArr) {  
	            if (keyword.contains(key)) {  
	                keyword = keyword.replace(key, "\\" + key);  
	            }  
	        }  
	    }  
	    return keyword;  
	} 


}
