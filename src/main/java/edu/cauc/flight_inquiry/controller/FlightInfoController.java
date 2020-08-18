package edu.cauc.flight_inquiry.controller;


import edu.cauc.flight_inquiry.dao.FlightInfoDao;
import edu.cauc.flight_inquiry.po.FlightInfo;
import edu.cauc.flight_inquiry.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;


@RestController
@CrossOrigin
public class FlightInfoController {

  @Autowired
  private FlightInfoDao flightInfoDao;


  @RequestMapping(value = "/flightInfo/add", method = RequestMethod.POST)
  public HashMap<String, Object> addFlightInfo(@RequestBody FlightInfo flightInfo) {
	flightInfo.setId(UUID.randomUUID().toString());
	flightInfo.setState(1);
	flightInfoDao.save(flightInfo);

	HashMap<String, Object> res = new HashMap<>();
	res.put("code", 0);
	res.put("msg", "success");
	return res;
  }

  @RequestMapping(value = "/flightInfo/update", method = RequestMethod.POST)
  public HashMap<String, Object> updateFlightInfo(@RequestBody FlightInfo flightInfo) {
	flightInfo.setState(1);
	flightInfoDao.save(flightInfo);

	HashMap<String, Object> res = new HashMap<>();
	res.put("code", 0);
	res.put("msg", "success");
	return res;
  }

  @RequestMapping(value = "/flightInfo/search", method = RequestMethod.POST)
  public HashMap<String, Object> getFlightInfos(@RequestBody HashMap<String, Object> params) {
	String flightNum = params.containsKey("flightNum") ? (String) params.get("flightNum") : "";
	String airline = params.containsKey("airline") ? (String) params.get("airline") : "";
	String startStation = params.containsKey("startStation") ? (String) params.get("startStation") : "";
	String destStation = params.containsKey("destStation") ? (String) params.get("destStation") : "";
	String startTime = params.containsKey("startTime") ? (String) params.get("startTime") : "";
	String arriveTime = params.containsKey("arriveTime") ? (String) params.get("arriveTime") : "";
	String planeType = params.containsKey("planeType") ? (String) params.get("planeType") : "";
	int limit = (int) params.get("limit");
	int offset = params.get("offset") == null ? ((int) params.get("page") - 1) * limit : (int) params.get("offset");

	int total = flightInfoDao.getFlightInfosCount(flightNum, airline, startStation, destStation, startTime, arriveTime, planeType);
	List<FlightInfo> flightInfos = flightInfoDao.getFlightInfos(flightNum, airline, startStation, destStation, startTime, arriveTime, planeType, offset, limit);
	for (FlightInfo flightInfo : flightInfos) {
	  System.out.println(flightInfo.getArriveTime());
	}

	HashMap<String, Object> res = new HashMap<>();
	res.put("code", 0);
	res.put("msg", "success");
	res.put("total", total);
	res.put("rows", flightInfos);

	return res;
  }

  @RequestMapping(value = "/flightInfo/del", method = RequestMethod.POST)
  public HashMap<String, Object> delFlightInfo(@RequestBody HashMap<String, String> params) {
	String id = params.get("id");
	Optional<FlightInfo> flightInfoOp = flightInfoDao.findById(id);
	if (!flightInfoOp.isPresent()) {
	  HashMap<String, Object> res = new HashMap<>();
	  res.put("code", 1);
	  res.put("msg", "删除失败，找不到信息");
	  return res;
	}

	FlightInfo flightInfo = flightInfoOp.get();
	flightInfo.setState(0);
	flightInfoDao.save(flightInfo);

	HashMap<String, Object> res = new HashMap<>();
	res.put("code", 0);
	res.put("msg", "success");
	return res;
  }

  @RequestMapping(value = "/flightInfo/exportExcel", method = RequestMethod.GET)
  public void exportExcel(HttpServletResponse response) {
	String fileName = "航班信息.xlsx";
	List<FlightInfo> list = flightInfoDao.findAll();

	try {
	  response.setContentType("application/octet-stream");
	  response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
	  OutputStream os = new BufferedOutputStream(response.getOutputStream());
	  ExcelUtil.writeFlightInfosExcel(fileName, list, os);
	  os.flush();
	  os.close();
	} catch (IOException e) {
	  e.printStackTrace();
	}
  }

}
