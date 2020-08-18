package edu.cauc.flight_inquiry.controller;


import edu.cauc.flight_inquiry.dao.LuggageInfoDao;
import edu.cauc.flight_inquiry.po.LuggageInfo;
import edu.cauc.flight_inquiry.util.ExcelUtil;
import edu.cauc.flight_inquiry.util.XMLUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin
public class LuggageInfoController {

  @Autowired
  private LuggageInfoDao luggageInfoDao;


  /**
   * 添加行李信息
   */
  @RequestMapping(value = "/luggageInfo/add", method = RequestMethod.POST)
  public HashMap<String, Object> addLuggageInfo(@RequestBody LuggageInfo luggageInfo) {
	luggageInfo.setId(UUID.randomUUID().toString());
	luggageInfo.setState(1);
	luggageInfoDao.save(luggageInfo);

	HashMap<String, Object> res = new HashMap<>();
	res.put("code", 0);
	res.put("msg", "success");
	return res;
  }

  @RequestMapping(value = "/luggageInfo/update", method = RequestMethod.POST)
  public HashMap<String, Object> updateLuggageInfo(@RequestBody LuggageInfo luggageInfo) {
	luggageInfo.setState(1);
	luggageInfoDao.save(luggageInfo);

	HashMap<String, Object> res = new HashMap<>();
	res.put("code", 0);
	res.put("msg", "success");
	return res;
  }

  @RequestMapping(value = "/luggageInfo/search", method = RequestMethod.POST)
  public HashMap<String, Object> getLuggageInfos(@RequestBody HashMap<String, Object> params) {
	String flightNum = params.containsKey("flightNum") ? (String) params.get("flightNum") : "";
	String realStartTime = params.containsKey("realStartTime") ? (String) params.get("realStartTime") : "";
	String realEndTime = params.containsKey("realEndTime") ? (String) params.get("realEndTime") : "";
	String luggageNum = params.containsKey("luggageNum") ? (String) params.get("luggageNum") : "";
	String turnNum = params.containsKey("turnNum") ? (String) params.get("turnNum") : "";
	String terminal = params.containsKey("terminal") ? (String) params.get("terminal") : "";
	String flightType = params.containsKey("flightType") ? (String) params.get("flightType") : "";
	int limit = (int) params.get("limit");
	int offset = params.get("offset") == null ? ((int) params.get("page") - 1) * limit : (int) params.get("offset");

	int total = luggageInfoDao.getLuggageInfosCount(flightNum, realStartTime, realEndTime, luggageNum, turnNum, terminal, flightType);
	List<LuggageInfo> luggageInfos = luggageInfoDao.getLuggageInfos(flightNum, realStartTime, realEndTime, luggageNum, turnNum, terminal, flightType, offset, limit);

	HashMap<String, Object> res = new HashMap<>();
	res.put("code", 0);
	res.put("msg", "success");
	res.put("total", total);
	res.put("rows", luggageInfos);

	return res;
  }

  @RequestMapping(value = "/luggageInfo/del", method = RequestMethod.POST)
  public HashMap<String, Object> delLuggageInfo(@RequestBody HashMap<String, String> params) {
	String id = params.get("id");
	Optional<LuggageInfo> luggageInfoOp = luggageInfoDao.findById(id);
	if (!luggageInfoOp.isPresent()) {
	  HashMap<String, Object> res = new HashMap<>();
	  res.put("code", 1);
	  res.put("msg", "删除失败，找不到信息");
	  return res;
	}

	LuggageInfo luggageInfo = luggageInfoOp.get();
	luggageInfo.setState(0);
	luggageInfoDao.save(luggageInfo);

	HashMap<String, Object> res = new HashMap<>();
	res.put("code", 0);
	res.put("msg", "success");
	return res;
  }

  /**
   * 解析 XML，存入数据库并返回数据
   */
  @RequestMapping(value = "/luggageInfo/parseXML", method = RequestMethod.POST)
  public HashMap<String, Object> parseXML(@RequestParam("file") MultipartFile file) {
	String fileName = file.getOriginalFilename();

	if (!fileName.endsWith("xml")) {
	  HashMap<String, Object> res = new HashMap<>();
	  res.put("code", 1);
	  res.put("msg", "不支持此文件格式");
	  return res;
	}

	InputStream is = null;
	List<HashMap<String, String>> list = null;

	try {
	  is = file.getInputStream();
	  list = XMLUtil.parse(fileName, is);
	  is.close();
	} catch (IOException e) {
	  e.printStackTrace();
	}

	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	LuggageInfo luggageInfo = null;

	for (HashMap<String, String> map : list) {
	  luggageInfo = new LuggageInfo();

	  luggageInfo.setFlightNum(map.get("flightNum"));
	  try {
		String realStartTime = map.get("realStartTime");
		if (realStartTime != null) luggageInfo.setRealStartTime(dateFormat.parse(realStartTime));
		else luggageInfo.setRealStartTime(null);
		String realEndTime = map.get("realEndTime");
		if (realEndTime != null) luggageInfo.setRealEndTime(dateFormat.parse(realEndTime));
		else luggageInfo.setRealEndTime(null);
	  } catch (ParseException e) {
		e.printStackTrace();
	  }
	  luggageInfo.setLuggageNum(map.get("luggageNum"));
	  luggageInfo.setTurnNum(map.get("turnNum"));
	  luggageInfo.setTerminal(map.get("terminal"));
	  luggageInfo.setFlightType(map.get("flightType"));
	  luggageInfo.setId(UUID.randomUUID().toString());
	  luggageInfo.setState(1);

	  luggageInfoDao.save(luggageInfo);
	}

	HashMap<String, Object> map = new HashMap<>();
	map.put("code", 0);
	map.put("msg", "success");
	map.put("data", list);
	return map;
  }

  @RequestMapping(value = "/luggageInfo/exportExcel", method = RequestMethod.GET)
  public void exportExcel(HttpServletResponse response) {
	String fileName = "行李信息.xlsx";
	List<LuggageInfo> list = luggageInfoDao.findAll();

	try {
	  response.setContentType("application/octet-stream");
	  response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
	  OutputStream os = new BufferedOutputStream(response.getOutputStream());
	  ExcelUtil.writeLuggageInfosExcel(fileName, list, os);
	  os.flush();
	  os.close();
	} catch (IOException e) {
	  e.printStackTrace();
	}
  }

}
