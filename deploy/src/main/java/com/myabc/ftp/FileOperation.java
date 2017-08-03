package com.myabc.ftp;

//import java.awt.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.dom4j.io.SAXReader;
import org.dom4j.Document;
import org.dom4j.Element;


public class FileOperation {
	// 检查文件md5
	public static boolean CheckMD5(String path, String MD5) {
		boolean flag = false;
		try {
			String v = FileOperation.getMd5ByFile(new File(path));
			if (MD5.compareTo(v) == 0) {
				flag = true;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return flag;
	}

	// 检查文件 MD5方法2
	public static boolean CheckMD5_back(String path, String MD5)
			throws IOException {
		boolean flag = false;
		FileInputStream fis = new FileInputStream(path);
		String md5 = DigestUtils.md5Hex(IOUtils.toByteArray(fis));
		IOUtils.closeQuietly(fis);
		if (MD5.compareTo(md5) == 0) {
			flag = true;
		}

		return flag;
	}

	// 获取文件Md5
	public static String getMd5ByFile(File file) throws FileNotFoundException {
		String value = null;
		FileInputStream in = new FileInputStream(file);
		try {
			MappedByteBuffer byteBuffer = in.getChannel().map(
					FileChannel.MapMode.READ_ONLY, 0, file.length());
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(byteBuffer);
			BigInteger bi = new BigInteger(1, md5.digest());
			value = bi.toString(16);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return value;
	}

	/**
	 * Description: 从FTP服务器下载文件
	 * 
	 * @param url
	 *            FTP服务器hostname
	 * @param port
	 *            FTP服务器端口
	 * @param username
	 *            FTP登录账号
	 * @param password
	 *            FTP登录密码
	 * @param remotePath
	 *            FTP服务器上的相对路径
	 * @param fileName
	 *            要下载的文件名
	 * @param localPath
	 *            下载后保存到本地的路径
	 * @return
	 */
	public static boolean downFile(String url, int port, String username,
			String password, String remotePath, String fileName,
			String localPath) {
		boolean success = false;
		
		FTPClient ftp = new FTPClient();
		
		try {
			int reply;
		
			ftp.connect(url, port);
		
			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftp.login(username, password);// 登录
			
			reply = ftp.getReplyCode();
			
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return success;
			}
			/* ftp.changeWorkingDirectory(remotePath); */// 转移到FTP服务器目录
			boolean ftpRes = ftp.changeWorkingDirectory(remotePath);
			
			File localFile = new File(localPath + "/" + fileName);
            if(localFile.exists())//如果文件已存在
            { 
            	 System.out.println("本地存在原文件同名文件");
//            	 String md5_old=getMd5ByFile(localFile);
            	 int cishu=0;
            	 while(localFile.exists()&&cishu<500) 
            	 { 
            		 localFile.delete();
            	     cishu++;
            	 }
            	 if(cishu==500)
            	 { System.out.println("删除失败") ;
            		 return false;}
            		 System.out.println("第"+cishu+"次删除成功") ;	
//            	 localFile.setWritable(true);
//            	if(!localFile.setWritable(true))
//            		{
//            		System.out.println("文件不可写，返回");
//            		return true;
//            		}
            }
//            	return success;
			OutputStream is = new FileOutputStream(localFile);
			
			ftp.retrieveFile(fileName, is);
			
			is.close();
			
			// }
			// }

			ftp.logout();
			success = true;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return success;
	}
	

	/**
	 * Description: 向FTP服务器上传文件
	 * 
	 * @param url
	 *            FTP服务器hostname
	 * @param port
	 *            FTP服务器端口
	 * @param username
	 *            FTP登录账号
	 * @param password
	 *            FTP登录密码
	 * @param path
	 *            FTP服务器保存目录
	 * @param filename
	 *            上传到FTP服务器上的文件名
	 * @param input
	 *            输入流
	 * @return 成功返回true，否则返回false
	 */
	public static boolean uploadFile(String url, int port, String username,
			String password, String path, String filename, InputStream input) {
		boolean success = false;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			ftp.connect(url, port);// 连接FTP服务器
			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftp.login(username, password);// 登录
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return success;
			}
			ftp.changeWorkingDirectory(path);
			ftp.storeFile(filename, input);

			input.close();
			ftp.logout();
			success = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
//					PropertiesFunc.logger.error("上传文件 到 tulip出错 ");
					ioe.printStackTrace();
				}
			}
		}
		return success;
	}
	
	public static String downFileByAfts(String applCode, String filePath, String destNode){
		return afts("3", applCode, filePath, destNode);		
	}
	
	public static String uploadFileByAfts(String applCode, String filePath, String destNode){
		return afts("1", applCode, filePath, destNode);
	}
	
	private static String afts(String operCode, String applCode, String filePath, String destNode){
		String cmd="aftstcp "+operCode+" "+applCode+" "+filePath+" "+destNode;
		System.out.println(cmd);
		BufferedReader br=null;
		StringBuilder sb=new StringBuilder();
		try {
			Process p=Runtime.getRuntime().exec(cmd);
			br=new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line=null;
			while((line=br.readLine())!=null){
				sb.append(line+"\n");
			}
			System.out.println(sb);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(br!=null){
				try {
					br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return sb.substring(4, 7).trim();
		
	}
	
	public static void convertXML2SeparatorFile(String fileName){
		List<List<String>> list=readXMLFile(fileName);
		StringBuilder sb=new StringBuilder();
		
		for(List<String> row:list){
			for(String column:row){
				sb.append(column+"|");
			}
			sb.append("\n");
		}
		try {//复制源文件名加后缀bak作为备份
			FileUtils.copyFile(new File(fileName), new File(fileName+"bak"));
			
			FileWriter writer=new FileWriter(fileName, false);
			writer.write(sb.toString());
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private static List<List<String>> readXMLFile(String fileName){
		List<List<String>> list=new ArrayList<List<String>>();		
		SAXReader reader=new SAXReader();
		
		try {
			FileInputStream in=new FileInputStream(new File(fileName));
			Reader read=new InputStreamReader(in, "gbk");
//			Document document=reader.read(new File(fileName));
			Document document=reader.read(read);
			List<Element> buyerBatchPayDetails=document.selectNodes("BuyerBatchPayDetailListInfo/BuyerBatchPayDetails/BuyerBatchPayDetailInfo");
			for(Element buyerBatchPayDetail:buyerBatchPayDetails){
				List<Element> children=buyerBatchPayDetail.elements();
				
				ArrayList<String> li=new ArrayList<String>();
				for(Element child:children){
					li.add(child.getText());
				}
				list.add(li);
			}
			in.close();
//			System.out.println("list:\n"+list);
			return list;			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void fileTranscoding(String filePath, String sourceCharset, String destCharset){
		File file=new File(filePath);
		try {
			FileUtils.write(file, FileUtils.readFileToString(file, sourceCharset), destCharset, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void editSeparatorFile(String fileName, String separator){
//		BufferedReader br=null;
//		BufferedWriter bw=null;
		String line=null;
		try {
			FileUtils.copyFile(new File(fileName), new File(fileName+"bak"));
			BufferedReader br= new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName)), "gbk"));
//			bw= new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("a"+fileName)), "gbk"));
			int rowNo=1;
			StringBuilder sb=new StringBuilder();
			while((line=br.readLine())!=null){
				String[] columns= line.split(separator);
				if(rowNo==1){
					sb.append(columns[3]+"|"+columns[2]);
				}else{
					sb.append(columns[1]+"|"+columns[3]+"|"+columns[7]+"|"+dateConvert(columns[0]));
				}
				sb.append("\n");
				rowNo++;
			}
			
			FileWriterWithEncoding fw=new FileWriterWithEncoding(fileName, "gbk", false);
//			bw.write(sb.toString());
//			bw.flush();
//			bw.close();
			fw.write(sb.toString());
			fw.flush();
			fw.close();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String dateConvert(String dateString){
		String temp1="yyyyMMdd";
		String temp2="yyyy-MM-dd";
		SimpleDateFormat sdf1=new SimpleDateFormat(temp1);
		SimpleDateFormat sdf2=new SimpleDateFormat(temp2);
		Date d=null;
		try {
			d=sdf1.parse(dateString);
			return sdf2.format(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
