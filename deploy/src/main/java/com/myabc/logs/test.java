package com.myabc.logs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class test implements Runnable{
	private static LinkedBlockingDeque<List<String>> Table = new LinkedBlockingDeque<List<String>>();
	   static{
	        //创建并启动线程，该线程实现日志的入库
		   testExcel();
	        new Thread( new test()).start();
	    }
	   public void run() {
		   ExecutorService  pool = Executors.newFixedThreadPool(4); 
	        List<List<String>> items = new ArrayList<List<String>>();
	        while (true) {
	            //从缓存中取出1条日志，但不从缓存中删除。用于检查队列中是否有数据
	            List item = Table.peek();
	            if (items.size() > 0&&items.size()<2) {
	                //item为null说明缓存中没有日志了，这时如果临时队列items中有记录，就可以批量入库了
	                
	            	work(items);
	            }
	            //从缓存中取出并删除最前面的日志，如果无数据，在堵塞
	            List result = deleteItem();
	            //将取出的记录放到临时队列中
	            items.add(result);
	        }
	    }
	   private List<String> deleteItem() {
	        List<String> result = null;
	        try {
	            //取出并删除最前面的一条数据，如果缓存中无数据，则堵塞住
	            result = Table.takeFirst();
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	        return result;
	    }
	   private void work(List<List<String>> items) {
	        //TODO 完成入库。这里代码没有提供
	        //入库后删除临时队列中记录
		   
		   Log.log.info("table size:"+Table.size());
	        items.clear();
	    }
	public static void testExcel()
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
			} catch (BiffException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
}
