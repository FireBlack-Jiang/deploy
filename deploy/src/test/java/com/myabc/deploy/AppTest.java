package com.myabc.deploy;

import com.myabc.config.config;

import coom.myabc.http.myhttp;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
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
    
    
    public void test()
    {
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
    }
}
