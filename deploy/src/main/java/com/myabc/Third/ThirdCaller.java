package com.myabc.Third;

public interface ThirdCaller {
	/**
	 * 调用三方交易
	 * @param thirdPkg 发送三方的交易报文
	 * @param args 调用交易需要的其它参数(可空)
	 * @return 三方返回报文
	 */
	public abstract String call(String thirdPkg, Object... args);
}

