package com.myabc.logs;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.sqlite.JDBC;

import com.myabc.config.config;

public class Log {
	final public static Logger log = Logger.getLogger(Log.class);
	public static String pathSeparator = File.separator;
	static String DBPath=config.usrPath+pathSeparator+"Soucefiles"+pathSeparator+"mydatabase.db";
	public static Log logdb = new Log();
	static Connection conn = null;
	// 待插库日志缓存最大条数,当前缓存数
	static int Max_cache = 150, Cur_Count_Cache = 0;

	// Log()
	// {
	//
	// }
	public synchronized static Log getInstance() {
		if (logdb == null) {
			logdb = new Log();
		}
		return logdb;
	}

	public static void logException(Exception e) {
		if (e!= null) 
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			pw.flush();
			pw.close();
			log.error(sw.toString());
		} else {
			log.error("No Exception");
		}
	}
	public static void init()
	{
		File dbfile=new File(DBPath);
		if(!dbfile.exists())
		{
			try {
				dbfile.createNewFile();
				Class.forName("org.sqlite.JDBC");
				if (conn == null) 
				{
					conn = DriverManager.getConnection("jdbc:sqlite:"+DBPath);
				}
				Statement stat = conn.createStatement();
				String sql="CREATE TABLE XML(KeyId varchar PRIMARY KEY NOT NULL,KeyIdTag varchar NOT NULL,XmlString text ,Date varchar ,Time varchar)";
				stat.executeUpdate(sql);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error("初始化数据库文件失败,下面是堆栈信息：\r\n");
				logException(e);
			} 
			
			
		}
	}
/**
 * 
 * @param LogStr
 * @throws ClassNotFoundException
 * @throws SQLException
 */
	public void write_DB_log(String LogStr) throws ClassNotFoundException,SQLException 
	{
		Class.forName("org.sqlite.JDBC");
		if (conn == null) 
		{
			conn = DriverManager.getConnection("jdbc:sqlite:"+DBPath);
		}
		try {

			Statement stat = conn.createStatement();
			
			// stat.executeUpdate("drop table if exists people;");
			// String
			// sql="CREATE TABLE log_myabc(Id INTEGER PRIMARY KEY AUTOINCREMENT,"
			// + "level varchar NOT NULL DEFAULT '',"
			// + "category varchar NOT NULL DEFAULT '',"
			// + "thread varchar NOT NULL DEFAULT '' ,"
			// + "time varchar NOT NULL DEFAULT '' ,"
			// + "location varchar NOT NULL DEFAULT '', "
			// + "note text) ";
			// stat.executeUpdate(sql);
			PreparedStatement prep = conn
					.prepareStatement("insert into log_myabc values (?, ?);");
			prep.setString(1, "Gandhi");
			prep.setString(2, "politics");
			prep.addBatch();
			prep.setString(1, "Turing");
			prep.setString(2, "computers");
			prep.addBatch();
			prep.setString(1, "Wittgenstein");
			prep.setString(2, "smartypants");
			prep.addBatch();
			conn.setAutoCommit(false);
			prep.executeBatch();
			conn.setAutoCommit(true);

			ResultSet rs = stat.executeQuery("select * from people;");
			while (rs.next()) {
				System.out.println("name = " + rs.getString("name"));
				System.out.println("job = " + rs.getString("occupation"));
			}
			rs.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
