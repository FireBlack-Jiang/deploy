package com.myabc.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import com.myabc.logs.Log;

public class myftp {

	static String pathSeparator = File.separator;
	// 检查文件md5
		public static boolean CheckMD5(String path, String MD5) {
			boolean flag = false;
			try {
				String v = myftp.getMd5ByFile(new File(path));
				if (MD5.compareTo(v) == 0) {
					flag = true;
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return flag;
		}
		/**
		 * Description: 检查文件是否为空,为空返回true
		 * @param filePath 文件路径
		 * @param Filename 文件名称
		 * 
		 */
	    public static boolean file_isnull(String filePath,String Filename)
	    {
	    	boolean flag=true;
	    	File file=new File(filePath+pathSeparator+Filename);
	    	if(file.exists()&&file.length()!=0)
	    	{
	    		flag=false;
	    	}  
	    	return flag;
	    }
//		// 检查文件 MD5方法2
//		public static boolean CheckMD5_back(String path, String MD5)
//				throws IOException {
//			boolean flag = false;
//			FileInputStream fis = new FileInputStream(path);
//			String md5 = DigestUtils.md5Hex(IOUtils.toByteArray(fis));
//			IOUtils.closeQuietly(fis);
//			if (MD5.compareTo(md5) == 0) {
//				flag = true;
//			}
//
//			return flag;
//		}

	// 获取文件Md5
	/**
	 * 
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 */
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
			//设置文件传输方式为二进制
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			
			if (!ftpRes) {
				return success;
			}
			// 社保ftp可能无遍历权限，listFiles()执行后直接挑转到finally问题，，暂时注释掉
			// FTPFile[] fs = ftp.listFiles();
			// for(FTPFile ff:fs){
			// if(ff.getName().equals(fileName)){
			File filepath=new File(localPath);
			
			//文件分隔符
			
			if(!filepath.exists())
			{
				filepath.mkdir();
			}
			File localFile = new File(localPath + pathSeparator + fileName);
			
            if(localFile.exists())//如果文件已存在
            { 
            	 localFile.delete();         	
            	 localFile.createNewFile(); 
            }
			OutputStream is = new FileOutputStream(localFile);		
			ftp.retrieveFile(fileName, is);		
			is.close();
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
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
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
				  ioe.printStackTrace();
				}
			}
			if(input!=null)
			{
				try {
					input.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return success;
	}
	/**
	 * Description: 测试ftp连通
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
	public static boolean testftp(String url, int port, String username,String password)
	{
		boolean success = false;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			ftp.connect(url, port);// 连接FTP服务器
			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftp.login(username, password);// 登录
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) 
			{
				ftp.disconnect();
				return success;
			}
			success = true;
			}
		    catch (IOException e) 
		    {
				Log.logException(e);
			}
		finally 
		{
			if (ftp.isConnected()) 
			{
				try 
				{
				ftp.disconnect();
				} 
				catch (IOException ioe) 
				{
				ioe.printStackTrace();
				}
			}
		}
		return success;
	}
}
