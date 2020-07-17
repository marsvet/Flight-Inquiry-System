package edu.cauc.flight_inquiry.controller;


import edu.cauc.flight_inquiry.dao.FlightInfoDao;
import edu.cauc.flight_inquiry.po.FlightInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
	String flightNum = (String) params.get("flightNum");
	String airline = (String) params.get("airline");
	String startStation = (String) params.get("startStation");
	String destStation = (String) params.get("destStation");
	String startTime = (String) params.get("startTime");
	String arriveTime = (String) params.get("arriveTime");
	String planeType = (String) params.get("planeType");
	int pageIndex = (int) params.get("pageIndex");	// 第几页
	int pageSize = (int) params.get("pageSize");	// 每页几条数据
	int startIndex =  pageIndex * pageSize;// mysql 数据库中 limit 的第一个参数

	int total = flightInfoDao.getFlightInfosCount(flightNum, airline, startStation, destStation, startTime, arriveTime, planeType);
	List<FlightInfo> flightInfos = flightInfoDao.getFlightInfos(flightNum, airline, startStation, destStation, startTime, arriveTime, planeType, startIndex, pageSize);

	HashMap<String, Object> res = new HashMap<>();
	res.put("code", 0);
	res.put("msg", "success");
	res.put("total", total);
	res.put("rows", flightInfos);

	return res;
  }

  @RequestMapping(value = "/flightInfo/del", method = RequestMethod.POST)
  public HashMap<String, Object> delFlightInfo(@RequestBody Map<String, String> params) {
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

}
