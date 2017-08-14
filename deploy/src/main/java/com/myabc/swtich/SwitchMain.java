package com.myabc.swtich;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myabc.ElementEditor.IEditor;
import com.myabc.Third.ThirdCaller;
import com.myabc.config.PropConfig;
import com.myabc.ftp.FileOperation;

public class SwitchMain {
	private final static Logger logger = LoggerFactory.getLogger(PropConfig.get("Switch_Name"));
	private static boolean stopped = false;
	private static Socket socket;
	
	private static IEditor editor;
	private static ThirdCaller caller;
	
	static{
		initSwitch();
	}
	
	private static void initSwitch() {
		try {
			editor=(IEditor)Class.forName(PropConfig.get("Editor")).newInstance();
			caller=(ThirdCaller)Class.forName(PropConfig.get("Caller")).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)  throws Exception{
		int port=Integer.parseInt(PropConfig.get("port"));
		final ServerSocket server = new ServerSocket(port);
		logger.info("创建socket监听线程[端口:" + port + "]...");
		ExecutorService   pool = Executors.newFixedThreadPool(4); 
		while (!stopped) {
			try {
				logger.info("开始监听...");
				socket = server.accept();
				logger.info("有连接...");
				pool.execute(new Thread(new Runnable() 
				{//多线程处理请求
					public void run() {
						try {
							onConnect(socket);
						} 
						catch (IOException e) 
						{e.printStackTrace();}
						
					}
				}));
			} catch (Exception e) {
				logger.error("服务器异常: " + e.getMessage());
			}
		}
		logger.info("socket监听线程已停止");
		server.close();
	}

	/**
	 * 接收指令
	 * @param socket
	 * @throws IOException
	 */
	private static void onConnect(Socket socket) throws IOException {
		DataInputStream dis = null;
		DataOutputStream dos = null;
		logger.info("开始接收指令...");
		try {
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
			// read input
			int packLen=Integer.parseInt(PropConfig.get("PackLen_Bytes"));
			byte[] packLenBytes = new byte[packLen];
			dis.read(packLenBytes, 0, packLen);
			String packLenStr = new String(packLenBytes, "gbk");
			System.out.println(packLenStr);
			packLen = Integer.parseInt(packLenStr);
			byte[] packBytes = new byte[packLen];
			dis.read(packBytes, 0, packLen);
			String packContent = new String(packBytes, "gbk");
			// process packet
			String outputStr = execCommand(packContent);
			logger.info("返回报文：" + outputStr);

			// write output
			dos.write(outputStr.getBytes("GBK"));
//			dos.write(packLenStr.getBytes("GBK"));
			dos.flush();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("处理指令出错: " + e.getMessage());
		} finally {
			if (dos != null) {
				dos.close();
			}
			if (dis != null) {
				dis.close();
			}
			if (socket != null) {
				socket.close();
			}
		}
	}
	
	/**
	 * 处理报文
	 * @param packContent
	 * @return
	 */
	private static String execCommand(String packContent) {
		logger.info("开始处理报文: [" + packContent + "]");		
		String transCode = xmlContent(packContent, "TxCode");
		logger.info("TxCode is + ["+transCode+"]");
		
		if (transCode.equals("01")) {
			Map<String, String> paraMap=PkgConverter.extractParaMap(PropConfig.get("test_qry"), PropConfig.get("Tulip_Req_Qry"));
			paraMap=editor.editTulipQry(paraMap);
			logger.info("paraMap:"+paraMap);
			String thirdPkg=PkgConverter.renderOutPkg(paraMap, PropConfig.get("Third_Req_Qry"));
			logger.info("发送三方报文:"+thirdPkg);
//			String thirdRet=CallThirdBusiness.callQry(thirdPkg);
			//调用三方的方法名 findArrearsByUserCode
			String thirdRet=caller.call(thirdPkg,"findArrearsByUserCode");
			logger.info("三方返回报文:"+thirdRet);
			
			String retCode=thirdRet.substring(3, 5);
			
			if(retCode.equalsIgnoreCase("00")){
				paraMap=PkgConverter.extractParaMap(thirdRet, PropConfig.get("Third_Resp_Qry"));
			}else {
				paraMap=PkgConverter.extractParaMap(thirdRet, PropConfig.get("Third_Resp_Fail"));
			}
			
			paraMap=editor.editThirdQry(paraMap, retCode);
			logger.info("paraMap:"+paraMap);
			
			return  PkgConverter.renderOutPkg(paraMap, PropConfig.get("Tulip_Resp_Qry"));
			
		} else if (transCode.equals("02")) {
			Map<String, String> paraMap=PkgConverter.extractParaMap(packContent, PropConfig.get("Tulip_Req_Pay"));
			paraMap=editor.editTulipPay(paraMap);
			logger.info("paraMap:"+paraMap);
			String thirdPkg=PkgConverter.renderOutPkg(paraMap, PropConfig.get("Third_Req_Pay"));
			logger.info("发送三方报文:"+thirdPkg);
			
			String thirdRet=caller.call(thirdPkg);
			logger.info("三方返回报文:"+thirdRet);
			
			String retCode=thirdRet.substring(3, 5);
			
			if(retCode.equalsIgnoreCase("00")){
				paraMap=PkgConverter.extractParaMap(thirdRet, PropConfig.get("Third_Resp_Pay"));
			}else {
				paraMap=PkgConverter.extractParaMap(thirdRet, PropConfig.get("Third_Resp_Fail"));
			}
			paraMap=editor.editThirdPay(paraMap, retCode);
			logger.info("paraMap:"+paraMap);
			
			return  PkgConverter.renderOutPkg(paraMap, PropConfig.get("Tulip_Resp_Pay"));
			
		} else if (transCode.equals("04")){
			Map<String, String> paraMap=PkgConverter.extractParaMap(packContent, PropConfig.get("Tulip_Req_Chk"));
			String fileName=paraMap.get("Body.FileName");
			logger.info("收到tulip对账文件名:"+fileName);
			String filePath=PropConfig.get("SWITCH_UPLOADPATH")+File.separator+fileName;
			FileOperation.editSeparatorFile(filePath, "\\|");
			sendFile2Third(filePath, fileName);
			
			paraMap=editor.editTulipChk(paraMap);
			logger.info("paraMap:"+paraMap);
			String thirdPkg=PkgConverter.renderOutPkg(paraMap, PropConfig.get("Third_Req_Chk"));
			logger.info("发送三方报文:"+thirdPkg);
			
			String thirdRet=caller.call(thirdPkg);
			logger.info("三方返回报文:"+thirdRet);
			
			String retCode=thirdRet.substring(3, 5);
			
			if(retCode.equalsIgnoreCase("00")){
				paraMap=PkgConverter.extractParaMap(thirdRet, PropConfig.get("Third_Resp_Chk"));
				String thirdFileName=paraMap.get("Third.FileName");
				getFileFromThird(thirdFileName);
//				String aftsRetCode=FileOperation.uploadFileByAfts("ABIBFILE", PropConfig.get("SWITCH_DOWNLOADPATH")+File.separator+thirdFileName, "22@000");	
//
//				if(aftsRetCode.equals("0") || aftsRetCode.equals("-1")){
//					logger.info("上传分析文件到报表服务器成功:"+aftsRetCode);
//					new HandleRespFile(thirdFileName, paraMap.get("Third.TxDate"), logger).start();
//				}else {
//					logger.info("上传分析文件到报表服务器失败,返回码:"+aftsRetCode);
//				}								
				
			}else {
				paraMap=PkgConverter.extractParaMap(thirdRet, PropConfig.get("Third_Resp_Fail"));
			}
			paraMap=editor.editThirdChk(paraMap, retCode);
			logger.info("paraMap:"+paraMap);
								
			return  PkgConverter.renderOutPkg(paraMap, PropConfig.get("Tulip_Resp_Chk"));
			
		} else {
			logger.error("未知的交易码 : " + transCode);
			return execErrorResp("未知的交易码 ");
		}
		
	}
	
	private static String xmlContent(String strXml,String element){
		int index_start=strXml.indexOf("<"+element+">");
		int index_end=strXml.indexOf("</"+element+">");
		int index_length=element.length()+2;
		String xmlContent=strXml.substring(index_start+index_length,index_end);
		return xmlContent; 
	}
	
	private static String execErrorResp(String errMsg) {
		
		return null;
	}
	
	private static void sendFile2Third(String filePath,String fileName){
		//3、上传对账文件
		boolean uploadflag=false;
		try {
			InputStream upstream = new FileInputStream(filePath);
			uploadflag= FileOperation.uploadFile(PropConfig.get("THIRD_IP"), Integer.parseInt(PropConfig.get("THIRD_FTP_PORT")), PropConfig.get("THIRD_FTP_USER"), PropConfig.get("THIRD_FTP_PASSWORD"), PropConfig.get("THIRD_FTP_FILEPATH"), fileName,upstream);			      
			upstream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}			
		if(uploadflag)
	    {
	    	logger.info("上传到三方文件："+fileName+"成功");			    
		}else{
			logger.info("上传到三方文件："+fileName+"失败");
		}
	}
	
	private static void getFileFromThird(String fileName){
		boolean downloadflag= FileOperation.downFile(PropConfig.get("THIRD_IP"), Integer.parseInt(PropConfig.get("THIRD_FTP_PORT")), PropConfig.get("THIRD_FTP_USER"), PropConfig.get("THIRD_FTP_PASSWORD"), PropConfig.get("THIRD_FTP_RESPFILEPATH"), fileName, PropConfig.get("SWITCH_DOWNLOADPATH"));
		if(downloadflag)
	    {
	    	logger.info("下载三方文件："+fileName+"成功");			    
		}else{
			logger.info("下载三方文件："+fileName+"失败");
		}
		
	}

}
