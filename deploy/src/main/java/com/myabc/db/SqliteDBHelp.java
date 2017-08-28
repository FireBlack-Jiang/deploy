package com.myabc.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import com.myabc.config.PropConfig;
import com.myabc.config.config;
import com.myabc.logs.Log;

public class SqliteDBHelp {
	public static String pathSeparator = File.separator;
	static String DBPath=config.usrPath+pathSeparator+"Soucefiles"+pathSeparator+"mydatabase.db";
	static Connection conn = null;
	static String initSql=PropConfig.get("initSql");//初始化SQL语句@分割
	
	/**
	 * 初始化，根据配置里面的sql语句创建数据文件和所需要的表
	 */
	public static void initdb()
	{
		File dbfile=new File(DBPath);
		String[] ListSql=initSql.split("\n");
		if(!dbfile.exists())
		{
			try {
				dbfile.createNewFile();
				Class.forName("org.sqlite.JDBC");
				if (conn == null||conn.isClosed()) 
				{
					conn = DriverManager.getConnection("jdbc:sqlite:"+DBPath);
					
				}
				Statement stat = conn.createStatement();
				for(int i=0;i<ListSql.length;i++)
				{
					if(!ListSql[i].equals(""))
					{
						int m=stat.executeUpdate(ListSql[i]);
					}
					
				}
				stat.close();
			   } catch (Exception e) {
				// TODO Auto-generated catch block
				Log.log.error("初始化数据库文件失败,下面是堆栈信息：\r\n");
				Log.logException(e);
			} 	
		}	
	}
	/**
	 * 关闭连接
	 */
	public static void closeConn()
	{
		if(conn!=null)
		{
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * 增
	 */
	public static int insert(String sql)
	{
		int n=0;
		return n;
	}
	/**
	 * 
	 */
}
