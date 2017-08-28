package com.myabc.deploy;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.map.ListOrderedMap;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.xml.XMLSerializer;
/**
 * 
* 处理json的工具类.
* <br>本类为处理json的工具类
* @author slj
 */
public class JsonTools {
	 /** 
     * map to xml xml <node><key label="key1">value1</key><key 
     * label="key2">value2</key>......</node> 
     *  
     * @param map 
     * @return 
     */  
    public static String maptoXml(Map map) {  
        Document document = DocumentHelper.createDocument();  
        Element nodeElement = document.addElement("node");  
        for (Object obj : map.keySet()) {  
            Element keyElement = nodeElement.addElement("key");  
            keyElement.addAttribute("label", String.valueOf(obj));  
            keyElement.setText(String.valueOf(map.get(obj)));  
        }  
        return doc2String(document);  
    }  
  
    /** 
     * list to xml xml <nodes><node><key label="key1">value1</key><key 
     * label="key2">value2</key>......</node><node><key 
     * label="key1">value1</key><key 
     * label="key2">value2</key>......</node></nodes> 
     *  
     * @param list 
     * @return 
     */  
    public static String listtoXml(List list) throws Exception {  
        Document document = DocumentHelper.createDocument();  
        Element nodesElement = document.addElement("nodes");  
        int i = 0;  
        for (Object o : list) {  
            Element nodeElement = nodesElement.addElement("node");  
            if (o instanceof Map) {  
                for (Object obj : ((Map) o).keySet()) {  
                    Element keyElement = nodeElement.addElement("key");  
                    keyElement.addAttribute("label", String.valueOf(obj));  
                    keyElement.setText(String.valueOf(((Map) o).get(obj)));  
                }  
            } else {  
                Element keyElement = nodeElement.addElement("key");  
                keyElement.addAttribute("label", String.valueOf(i));  
                keyElement.setText(String.valueOf(o));  
            }  
            i++;  
        }  
        return doc2String(document);  
    }  
  
    /** 
     * json to xml {"node":{"key":{"@label":"key1","#text":"value1"}}} conver 
     * <o><node class="object"><key class="object" 
     * label="key1">value1</key></node></o> 
     *  
     * @param json 
     * @return 
     */  
    public static String jsontoXml(String json) {  
        try {  
            XMLSerializer serializer = new XMLSerializer();  
            JSON jsonObject = JSONSerializer.toJSON(json);
            
            return serializer.write(jsonObject);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
  
    /** 
     * xml to map xml <node><key label="key1">value1</key><key 
     * label="key2">value2</key>......</node> 
     *  
     * @param xml 
     * @return 
     */  
    public static Map xmltoMap(String xml) {  
        try {  
            Map map = new HashMap();  
            Document document = DocumentHelper.parseText(xml);  
            Element nodeElement = document.getRootElement();  
            List node = nodeElement.elements();  
            for (Iterator it = node.iterator(); it.hasNext();) {  
                Element elm = (Element) it.next();  
                map.put(elm.attributeValue("label"), elm.getText());  
                elm = null;  
            }  
            node = null;  
            nodeElement = null;  
            document = null;  
            return map;  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
  
    /** 
     * xml to list xml <nodes><node><key label="key1">value1</key><key 
     * label="key2">value2</key>......</node><node><key 
     * label="key1">value1</key><key 
     * label="key2">value2</key>......</node></nodes> 
     *  
     * @param xml 
     * @return 
     */  
    public static List xmltoList(String xml) {  
        try {  
            List<Map> list = new ArrayList<Map>();  
            Document document = DocumentHelper.parseText(xml);  
            Element nodesElement = document.getRootElement();  
            List nodes = nodesElement.elements();  
            for (Iterator its = nodes.iterator(); its.hasNext();) {  
                Element nodeElement = (Element) its.next();  
                Map map = xmltoMap(nodeElement.asXML());  
                list.add(map);  
                map = null;  
            }  
            nodes = null;  
            nodesElement = null;  
            document = null;  
            return list;  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
  
    /** 
     * xml to json <node><key label="key1">value1</key></node> 转化为 
     * {"key":{"@label":"key1","#text":"value1"}} 
     *  
     * @param xml 
     * @return 
     */  
    public static String xmltoJson(String xml) {  
        XMLSerializer xmlSerializer = new XMLSerializer();  
        return xmlSerializer.read(xml).toString();  
    }  
  
    /** 
     *  
     * @param document 
     * @return 
     */  
    public static String doc2String(Document document) {  
        String s = "";  
        try {  
            // 使用输出流来进行转化  
            ByteArrayOutputStream out = new ByteArrayOutputStream();  
            // 使用UTF-8编码  
            OutputFormat format = new OutputFormat("   ", true, "UTF-8");  
            XMLWriter writer = new XMLWriter(out, format);  
            writer.write(document);  
            s = out.toString("UTF-8");  
        } catch (Exception ex) {  
            ex.printStackTrace();  
        }  
        return s;  
    }  
	/**
	* XML转换为JSON
	* 2017-4-27 15:32:53
	* @param xml
	* @return
	*/
	public static String xmlToJson(String xml) {
	XMLSerializer serializer = new XMLSerializer();
	return serializer.read(xml).toString();
	}
	/**
	* JSON 转换为XML
	* 2017年4月27日 15:34:24
	* @param json
	* @return
	*/
	public static String jsonToXML(String json) {
	XMLSerializer xmlSerializer = new XMLSerializer();
	// 根节点名称
	xmlSerializer.setRootName("xml");
	// 不对类型进行设置
	xmlSerializer.setTypeHintsEnabled(false);
	String xmlStr = "";
	if (json.contains("[") && json.contains("]")) {
	// jsonArray
	JSONArray jobj = JSONArray.fromObject(json);
	xmlStr = xmlSerializer.write(jobj);
	} else {
	// jsonObject
	JSONObject jobj = JSONObject.fromObject(json);
	xmlStr = xmlSerializer.write(jobj);
	}
	System.out.println("转换后的参数：" + xmlStr);
	return xmlStr;
	}

    /**
     * 
    * json转换list.
    * <br>详细说明
    * @param jsonStr json字符串
    * @return
    * @return List<Map<String,Object>> list
    * @throws
    * @author slj
    * @date 2013年12月24日 下午1:08:03
     */
    public static List<Map<String, Object>> parseJSON2List(String jsonStr){
        JSONArray jsonArr = JSONArray.fromObject(jsonStr);
        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
        Iterator<JSONObject> it = jsonArr.iterator();
        while(it.hasNext()){
            JSONObject json2 = it.next();
            list.add(parseJSON2Map(json2.toString()));
        }
        return list;
    }

   /**
    * 
   * json转换map.
   * <br>详细说明
   * @param jsonStr json字符串
   * @return
   * @return Map<String,Object> 集合
   * @throws
   * @author slj
    */
    public static Map<String, Object> parseJSON2Map(String jsonStr){
        ListOrderedMap map = new ListOrderedMap();
        //最外层解析
        JSONObject json = JSONObject.fromObject(jsonStr);
        for(Object k : json.keySet()){
            Object v = json.get(k); 
            //如果内层还是数组的话，继续解析
            if(v instanceof JSONArray){
                List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
                Iterator<JSONObject> it = ((JSONArray)v).iterator();
                while(it.hasNext()){
                    JSONObject json2 = it.next();
                    list.add(parseJSON2Map(json2.toString()));
                }
                map.put(k.toString(), list);
            } else {
                map.put(k.toString(), v);
            }
        }
        return map;
    }
    public static Map<String,String> parseJSON2Map2(String jsonStr){
        ListOrderedMap map = new ListOrderedMap();
        //最外层解析
        JSONObject json = JSONObject.fromObject(jsonStr);
        for(Object k : json.keySet()){
            Object v = json.get(k); 
            //如果内层还是数组的话，继续解析
            if(v instanceof JSONArray){
                List<Map<String, String>> list = new ArrayList<Map<String,String>>();
                Iterator<JSONObject> it = ((JSONArray)v).iterator();
                while(it.hasNext()){
                    JSONObject json2 = it.next();
                    list.add(parseJSON2Map2(json2.toString()));
                }
                map.put(k.toString(), list.toString());
            } else {
                map.put(k.toString(), v.toString());
            }
        }
        return map;
    }
    /**
     * 
    * 通过HTTP获取JSON数据.
    * <br>通过HTTP获取JSON数据返回list
    * @param url 链接
    * @return
    * @return List<Map<String,Object>> list
    * @throws
    * @author slj
     */
    public static List<Map<String, Object>> getListByUrl(String url){
        try {
            //通过HTTP获取JSON数据
            InputStream in = new URL(url).openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line;
            while((line=reader.readLine())!=null){
                sb.append(line);
            }
            return parseJSON2List(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

   /**
    * 
   * 通过HTTP获取JSON数据.
   * <br>通过HTTP获取JSON数据返回map
   * @param url 链接
   * @return
   * @return Map<String,Object> 集合
   * @throws
   * @author slj
    */
    public static Map<String, Object> getMapByUrl(String url){
        try {
            //通过HTTP获取JSON数据
            InputStream in = new URL(url).openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line;
            while((line=reader.readLine())!=null){
                sb.append(line);
            }
            return parseJSON2Map(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 
    * map转换json.
    * <br>详细说明
    * @param map 集合
    * @return
    * @return String json字符串
    * @throws
    * @author slj
     */
    public static String mapToJson(Map<String, String> map) {
        Set<String> keys = map.keySet();
        String key = "";
        String value = "";
        StringBuffer jsonBuffer = new StringBuffer();
        jsonBuffer.append("{");
        for (Iterator<String> it = keys.iterator(); it.hasNext();) {
            key = (String) it.next();
            value = map.get(key);
            jsonBuffer.append(key + ":" +"\""+ value+"\"");
            if (it.hasNext()) {
                jsonBuffer.append(",");
            }
        }
        jsonBuffer.append("}");
        return jsonBuffer.toString();
    }

    //test
    public static void main(String[] args) {
        String url = "http://...";
        List<Map<String,Object>> list = getListByUrl(url);
        System.out.println(list);
    }
}
