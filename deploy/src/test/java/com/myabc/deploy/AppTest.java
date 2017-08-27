package com.myabc.deploy;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

import net.sf.json.xml.XMLSerializer;

import com.myabc.config.PropConfig;
import com.myabc.config.config;
import com.myabc.http.myhttp;
import com.myabc.logs.Log;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
//	 static List Table=new ArrayList();
	 private static LinkedBlockingDeque<List<String>> Table = new LinkedBlockingDeque<List<String>>();
	 boolean flag=false;
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
    public void testExcel()
    {
    	try {
			Workbook book=Workbook.getWorkbook(new File("C:\\Users\\Administrator\\git\\fireblack-jiang\\deploy\\Soucefiles\\ip.et"));
			Sheet sheet=book.getSheet(0);
			
			for(int row=0;row<sheet.getRows();row++)
			{
				List RowList=new ArrayList(); 
				for(int col=0;col<sheet.getColumns();col++)
				{
					Cell cell1=sheet.getCell(col,row);
					RowList.add(cell1.getContents());
				}
				Table.add(RowList);
			}
			ExecutorService  pool = Executors.newFixedThreadPool(1); 
			while(Table.size()>0)
			{
				try
				{
					pool.execute(new Thread(new Runnable() 
					{//多线程处理请求
						public void run() {
							mytest2();	
						}
					}));
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public  void mytest2()
    {
    	//synchronized
    	 Log.log.error("table size:"+Table.size());	
    	 try {
			
			Table.takeFirst();
			Thread.sleep(1089);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         		 
    }
    public void test1()
    {
    	 List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
    	 String s="{yhwd:\"007\",yhgy:\"0001\",gsdm: \"6003\",yhbm:\"46400116\"}";
    	 s=PropConfig.get("test_qry");
    	 /* 第二种方法，使用json-lib提供的方法 */  
    	 //创建 XMLSerializer对象  
    	 XMLSerializer xmlSerializer = new XMLSerializer();  
    	 //将xml转为json（注：如果是元素的属性，会在json里的key前加一个@标识）  
    	 String result = xmlSerializer.read(s).toString();  
    	 //输出json内容  
    	 System.out.println(result); 
    	 s=PropConfig.get("test_json");
    	 System.out.println(JsonTools.jsontoXml(s));
    }
    public void test()
    {

    	//查询应交气费
    	String s="{yhwd:\"007\",yhgy:\"0001\",gsdm: \"6003\",yhbm:\"46400116\"}";
    	s="{yhwd:\"004\",yhgy:\"0001\",gsdm: \"7001\",yhbm:\"008070961\"}";
    	try {
			String str_Joson=DesUtils.Get_Instance().encrypt(s);
			String str_url=config.getPropertieByName("url")+config.getPropertieByName("idCode")+"&paramStr="+str_Joson;
			System.out.println(str_url);
			String get=myhttp.httpclient_post(str_url);
			System.out.println(DesUtils.Get_Instance().decrypt(get));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	//阀值查询
    	String teststr;
		try {
			teststr = DesUtils.Get_Instance().encrypt("{gsdm:\"7001\",yhwd:\"004\"}");
			String url=config.getPropertieByName("post_adress")+"findThreshold&idCode="+config.getPropertieByName("idCode")+"&paramStr="+teststr;
	    	System.out.println(url);
	    	String get=myhttp.httpclient_post(url);
	    	System.out.println(DesUtils.Get_Instance().decrypt(get));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//缴费  insertPay  and insertPrestore
		/**
		 * 
		 
		teststr="{lsh: \"201708012371001\",yhwd:\"004\",yhgy:\"0001\",gsdm: \"7001\",yhbm:\"008070961\",qfyskId: \"449777277\",zje: \"81\"}";
		try {
			teststr = DesUtils.Get_Instance().encrypt(teststr);
			String url=config.getPropertieByName("post_adress")+"insertPay&idCode="+config.getPropertieByName("idCode")+"&paramStr="+teststr;
			System.out.println(url);
	    	String get=myhttp.httpclient_post(url);
	    	System.out.println(DesUtils.Get_Instance().decrypt(get));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		/**
		 * 预交费insertPrestore
		 
		teststr="{lsh: \"20170801003\",yhwd:\"004\",yhgy:\"0001\",gsdm: \"7001\",yhbm:\"008070961\",zje: \"88\"}";
		try {
			teststr = DesUtils.Get_Instance().encrypt(teststr);
			String url=config.getPropertieByName("post_adress")+"insertPrestore&idCode="+config.getPropertieByName("idCode")+"&paramStr="+teststr;
			System.out.println(url);
	    	String get=myhttp.httpclient_post(url);
	    	System.out.println(DesUtils.Get_Instance().decrypt(get));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		//抹账 updateWipe
		/**
		 * 
		
		teststr="{lsh:\"20170801003\",yhwd:\"004\", gsdm:\"7001\",yhbm:\"008070961\" ,je:\"88\"}";
		try {
			teststr = DesUtils.Get_Instance().encrypt(teststr);
			String url=config.getPropertieByName("post_adress")+"updateWipe&idCode="+config.getPropertieByName("idCode")+"&paramStr="+teststr;
			System.out.println(url);
	    	String get=myhttp.httpclient_post(url);
	    	System.out.println(DesUtils.Get_Instance().decrypt(get));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 */
    }
}
