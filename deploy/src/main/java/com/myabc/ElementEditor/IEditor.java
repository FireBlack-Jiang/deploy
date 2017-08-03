package com.myabc.ElementEditor;
import java.util.Map;
public interface IEditor {
	/**
	 * 修改tulip查询请求交易要素
	 * @param paraMap 存放交易要素的哈希映射
	 * @param args 控制参数
	 * @return 修改后的交易要素
	 */
	public Map<String, String> editTulipQry(Map<String, String> paraMap, String... args);
	
	/**
	 * 修改三方返回查询交易要素
	 * @param paraMap 存放交易要素的哈希映射
	 * @param args 控制参数
	 * @return 修改后的交易要素
	 */
	public Map<String, String> editThirdQry(Map<String, String> paraMap, String... args);
	
	/**
	 * 修改tulip缴费请求交易要素
	 * @param paraMap 存放交易要素的哈希映射
	 * @param args 控制参数
	 * @return 修改后的交易要素
	 */
	public Map<String, String> editTulipPay(Map<String, String> paraMap, String... args);
	
	/**
	 * 修改三方返回缴费交易要素
	 * @param paraMap 存放交易要素的哈希映射
	 * @param args 控制参数
	 * @return 修改后的交易要素
	 */
	public Map<String, String> editThirdPay(Map<String, String> paraMap, String... args);
	
	/**
	 * 修改tulip对账请求交易要素
	 * @param paraMap 存放交易要素的哈希映射
	 * @param args 控制参数
	 * @return 修改后的交易要素
	 */
	public Map<String, String> editTulipChk(Map<String, String> paraMap, String... args);
	
	/**
	 * 修改三方返回对账交易要素
	 * @param paraMap 存放交易要素的哈希映射
	 * @param args 控制参数
	 * @return 修改后的交易要素
	 */
	public Map<String, String> editThirdChk(Map<String, String> paraMap, String... args);

}
