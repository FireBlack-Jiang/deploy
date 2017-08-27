package com.myabc.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import com.myabc.config.PropConfig;
import com.myabc.config.config;
import com.myabc.logs.Log;

public class job_ftp  implements Job{
	final static String pathSeparator = File.separator;
	static String locaPath=config.getPropertieByName("ftp_localpath");
	static String ftp_ip,username,password,remotepath;
	static int port;
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		init_ftp();
		Log.log.info("初始化ftp参数成功");
		Log.log.info(PropConfig.get("Tulip_Req_Pay"));
		Log.log.info(locaPath);
		Log.log.info(username);
		Log.log.info(password);
		Log.log.info(remotepath);
		Log.log.info(locaPath);
		Log.log.info(username);
		Log.log.info(password);
		Log.log.info(remotepath);
		Log.log.info(locaPath);
		Log.log.info(username);
		Log.log.info(password);
		Log.log.info(remotepath);
		Log.log.info(locaPath);
		Log.log.info(username);
		Log.log.info(password);
		Log.log.info(remotepath);
		Log.log.info(locaPath);
		Log.log.info(username);
		Log.log.info(password);
		Log.log.info(remotepath);
		Log.log.info(locaPath);
		Log.log.info(username);
		Log.log.info(password);
		Log.log.info(remotepath);
		Log.log.info(locaPath);
		Log.log.info(username);
		Log.log.info(password);
		Log.log.info(remotepath);
		Log.log.info(locaPath);
		Log.log.info(username);
		Log.log.info(password);
		Log.log.info(remotepath);
		Log.log.info(locaPath);
		Log.log.info(username);
		Log.log.info(password);
		Log.log.info(remotepath);
		Log.log.info(locaPath);
		Log.log.info(username);
		Log.log.info(password);
		Log.log.info(remotepath);
		Log.log.info(locaPath);
		Log.log.info(username);
		Log.log.info(password);
		Log.log.info(remotepath);
		Log.log.info(locaPath);
		Log.log.info(username);
		Log.log.info(password);
		Log.log.info(remotepath);
		Log.log.info(locaPath);
		Log.log.info(username);
		Log.log.info(password);
		Log.log.info(remotepath);
		Log.log.info(locaPath);
		Log.log.info(username);
		Log.log.info(password);
		Log.log.info(remotepath);
		Log.log.info(locaPath);
		Log.log.info(username);
		Log.log.info(password);
		Log.log.info(remotepath);
		Log.log.info(locaPath);
		Log.log.info(username);
		Log.log.info(password);
		Log.log.info(remotepath);
		Log.log.info(locaPath);
		Log.log.info(username);
		Log.log.info(password);
		Log.log.info(remotepath);
		Log.log.info(locaPath);
		Log.log.info(username);
		Log.log.info(password);
		Log.log.info(remotepath);
		Log.log.info(locaPath);
		Log.log.info(username);
		Log.log.info(password);
		Log.log.info(remotepath);
		Log.log.info(locaPath);
		Log.log.info(username);
		Log.log.info(password);
		Log.log.info(remotepath);
		if(myftp.testftp(ftp_ip, port, username, password))
		{
			Log.log.info("ftp is OK !");
		}
		else
		{
			Log.log.error("ftp is BAD !");
			return;
		}
//		SearchFile();
		try {
			bat_uploadfiles_by_path(locaPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.logException(e);
		}
	}
    private boolean init_ftp()
    {
    	ftp_ip=PropConfig.get("ftp_ip");
    	username=PropConfig.get("ftp_user");
    	password=PropConfig.get("ftp_password");
    	remotepath=PropConfig.get("ftp_remotepath");
    	Log.log.info("ftp:"+ftp_ip);
		return true;
    	
    }

	private void SearchFile() {
		// TODO Auto-generated method stub
		LinkedList<String> folderList = new LinkedList<String>();   
		folderList.add(locaPath);   
		while (folderList.size() > 0) {   
		    File file = new File(folderList.peek());
		    Log.log.info("路径："+file.getPath());
		    folderList.removeLast();   
		    File[] files = file.listFiles();   
		    ArrayList<File> fileList = new ArrayList<File>();   
		    for (int i = 0; i < files.length; i++) {   
		        if (files[i].isDirectory()) {   
		            folderList.add(files[i].getPath());   
		        } else {   
		            fileList.add(files[i]);   
		        }   
		    }
		    Log.log.info("搜索文件数量："+fileList.size());
		    for (File f : fileList) {   
			       // f.getAbsoluteFile();
			    	String name=f.getName();
			    	Log.log.info(f.getPath());
			 }
	    }
		 
	}
	/**
	 * 遍历文件夹下的所有文件 非递归调用
	 * @param path
	 * @throws IOException 
	 */
	public void bat_uploadfiles_by_path(String path) throws IOException {
        int fileNum = 0, folderNum = 0;
        File file = new File(path);
        if (file.exists()) {
            LinkedList<File> list = new LinkedList<File>();
            File[] files = file.listFiles();
            for (File file2 : files) {
                if (file2.isDirectory()) {
                    System.out.println("文件夹:" + file2.getAbsolutePath());
                    list.add(file2);
                    folderNum++;
                }
                else 
                {
                	//处理文件夹的文件
                    System.out.println("文件:" + file2.getAbsolutePath());
                    myftp.uploadFile(ftp_ip, port, username, password, path, file2.getName(), new FileInputStream(file2));
                    fileNum++;
                }
            }
            File temp_file;
            while (!list.isEmpty()) {
                temp_file = list.removeFirst();
                files = temp_file.listFiles();
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        System.out.println("文件夹:" + file2.getAbsolutePath());
                        list.add(file2);
                        folderNum++;
                    } else {
                        System.out.println("文件:" + file2.getAbsolutePath());
                        myftp.uploadFile(ftp_ip, port, username, password, path, file2.getName(), new FileInputStream(file2));
                        fileNum++;
                    }
                }
            }
        } 
        else 
        {
            System.out.println("文件不存在!");
        }
        System.out.println("文件夹共有:" + folderNum + ",文件共有:" + fileNum);

    }
	

}
