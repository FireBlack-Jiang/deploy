package com.myabc.ElementEditor;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public  class myGasEditor implements IEditor{

	/**
	 * 修改tulip查询请求交易要素
	 * @param paraMap 存放交易要素的哈希映射
	 * @return 修改后的交易要素
	 */
	public Map<String, String> editTulipQry(Map<String, String> paraMap,
			String... args) {
		// TODO Auto-generated method stub
		String yhbm=paraMap.get("Body.PaymentNo");
		StringBuilder sb=new StringBuilder();
		return paraMap;
	}
	/**
     * 
    * map转换json.
    * <br>详细说明
    * @param map 集合
    * @return
    * @return String json字符串
    * @throws
    * @author slj
     */
    public static String mapToJson(Map<String, String> map) {
        Set<String> keys = map.keySet();
        String key = "";
        String value = "";
        StringBuffer jsonBuffer = new StringBuffer();
        jsonBuffer.append("{");
        for (Iterator<String> it = keys.iterator(); it.hasNext();) {
            key = (String) it.next();
            value = map.get(key);
            jsonBuffer.append(key + ":" +"\""+ value+"\"");
            if (it.hasNext()) {
                jsonBuffer.append(",");
            }
        }
        jsonBuffer.append("}");
        return jsonBuffer.toString();
    }
	/**
	 * 修改三方返回查询交易要素
	 * @param paraMap 存放交易要素的哈希映射
	 * @return 修改后的交易要素
	 */
	public Map<String, String> editThirdQry(Map<String, String> paraMap,
			String... args) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 修改tulip缴费请求交易要素
	 * @param paraMap 存放交易要素的哈希映射
	 * @return 修改后的交易要素
	 */
	public Map<String, String> editTulipPay(Map<String, String> paraMap,
			String... args) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 修改三方返回缴费交易要素
	 * @param paraMap 存放交易要素的哈希映射
	 * @return 修改后的交易要素
	 */
	public Map<String, String> editThirdPay(Map<String, String> paraMap,
			String... args) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 修改tulip对账请求交易要素
	 * @param paraMap 存放交易要素的哈希映射
	 * @return 修改后的交易要素
	 */
	public Map<String, String> editTulipChk(Map<String, String> paraMap,
			String... args) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 修改三方返回对账交易要素
	 * @param paraMap 存放交易要素的哈希映射
	 * @return 修改后的交易要素
	 */
	public Map<String, String> editThirdChk(Map<String, String> paraMap,
			String... args) {
		// TODO Auto-generated method stub
		return null;
	}

}
