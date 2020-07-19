package edu.cauc.flight_inquiry.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 解析 XML
 */
public class XMLUtil {

  private static SimpleDateFormat fromDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
  private static SimpleDateFormat toDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");


  /**
   * 对外只提供这一个方法。根据文件名分发给不同方法
   */
  public static List<HashMap<String, String>> parse(String fileName, InputStream is) {
	List<HashMap<String, String>> list = null;

	try {
	  if (fileName.contains("BLLS"))
		list = ParseBLLS(is);
	} catch (DocumentException | ParseException e) {
	  e.printStackTrace();
	}

	return list;
  }

  private static List<HashMap<String, String>> ParseBLLS(InputStream is) throws DocumentException, ParseException {
	SAXReader saxReader = new SAXReader();
	List<HashMap<String, String>> list = new ArrayList<>();
	HashMap<String, String> hashMap = new HashMap<>();
	Document document = saxReader.read(is);

	// 航班号
	Element element = (Element) document.selectObject("//FFID");
	String[] temp = element.getText().split("-");
	hashMap.put("flightNum", temp[0] + temp[1]);

	// 航班属性
	element = (Element) document.selectObject("//FATT");
	String flightType = null;
	if ("2403".equals(element.getText()))
	  flightType = "国内";
	else if ("2401".equals(element.getText()))
	  flightType = "国际";
	else if ("2404".equals(element.getText()))
	  flightType = "混合";
	else if ("2402".equals(element.getText()))
	  flightType = "地区";
	hashMap.put("flightType", flightType);

	List<Node> nodes = document.selectNodes("//BELT");
	for (Node node : nodes) {
	  Element ele = (Element) node;
	  HashMap<String, String> map = new HashMap<>(hashMap);   // 深拷贝
	  map.put("luggageNum", ele.elementText("ID"));  // 行李号
	  map.put("turnNum", ele.elementText("CODE"));   // 转盘号
	  String realStartTime = ele.elementText("RSTR");
	  if (!"".equals(realStartTime)) map.put("realStartTime", toDateFormat.format(fromDateFormat.parse(realStartTime)));
	  else map.put("realStartTime", null);
	  String realEndTime = ele.elementText("REND");
	  if (!"".equals(realEndTime)) map.put("realEndTime", toDateFormat.format(fromDateFormat.parse(realEndTime)));
	  else map.put("realEndTime", null);
	  map.put("terminal", ele.elementText("BTSC"));
	  list.add(map);
	}

	return list;
  }

}