package edu.cauc.flight_inquiry.controller;


import edu.cauc.flight_inquiry.dao.FlightInfoDao;
import edu.cauc.flight_inquiry.po.Airline;
import edu.cauc.flight_inquiry.po.FlightInfo;
import edu.cauc.flight_inquiry.dto.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@CrossOrigin
public class FlightInfoController {

  @Autowired
  private FlightInfoDao flightInfoDao;


  @RequestMapping(value = "/flightInfo/add", method = RequestMethod.POST)
  public JsonResult<Object> addFlightInfo(@RequestBody FlightInfo flightInfo) {
    flightInfo.setId(UUID.randomUUID().toString());
    flightInfo.setState(1);
    flightInfoDao.save(flightInfo);
    return new JsonResult<>();
  }

  @RequestMapping(value = "/flightInfo/update", method = RequestMethod.POST)
  public JsonResult<Object> updateFlightInfo(@RequestBody FlightInfo flightInfo) {
    flightInfoDao.save(flightInfo);
    return new JsonResult<>();
  }

  @RequestMapping(value = "/flightInfo/del", method = RequestMethod.POST)
  public JsonResult<Object> delFlightInfo(@RequestBody Map<String, String> params) {
    String id = params.get("id");
    Optional<FlightInfo> flightInfoOp = flightInfoDao.findById(id);
    if (!flightInfoOp.isPresent())
      return new JsonResult<>(1, "删除失败，找不到信息");

    FlightInfo flightInfo = flightInfoOp.get();
    flightInfo.setState(0);
    flightInfoDao.save(flightInfo);

    return new JsonResult<>();
  }

//  @RequestMapping(value = "/flightInfo/getAll", method = RequestMethod.GET)
//  public JsonResult<List<FlightInfo>> getAllFlightInfo() {
//    List<FlightInfo> list = flightInfoDao.findAll();
//    JsonResult<List<FlightInfo>> result = new JsonResult<>(list);
//    return result;
//  }

  @RequestMapping(value = "/flightInfo/getById", method = RequestMethod.GET)
  public JsonResult<FlightInfo> getFlightInfoById(@RequestParam String id) {
    Optional<FlightInfo> flightInfoOp = flightInfoDao.findById(id);
    FlightInfo flightInfo = null;
    if (flightInfoOp.isPresent())
      flightInfo = flightInfoOp.get();
    else
      return new JsonResult<>(1, "找不到");

    String airlineId = flightInfo.getAirlineId();
    Optional<Airline> airlineOp = airlineDao.findById(airlineId);
    Airline airline = null;
    if (airlineOp.isPresent())
      airline = airlineOp.get();

    flightInfo.setAirlineId(airline.getZhFullName());

    return new JsonResult<>(flightInfo);
  }

  @RequestMapping(value = "/flightInfo/getByZhFullNameLike", method = RequestMethod.GET)
  public JsonResult<List<FlightInfo>> getFlightInfoByZhFullNameLike(@RequestParam String zhFullNameLike) {
    List<Airline> airlineList = airlineDao.findAllByZhFullNameLike("%" + zhFullNameLike + "%");
    List<FlightInfo> result = new LinkedList<>();

    for (Airline airline:airlineList) {
      String airlineId = airline.getId();
      String airlineZhFullName = airline.getZhFullName();
      List<FlightInfo> flightInfoList = flightInfoDao.findAllByAirlineId(airlineId);
      for (FlightInfo flightInfo : flightInfoList) {
        flightInfo.setAirlineId(airlineZhFullName);
        result.add(flightInfo);
      }
    }

    return new JsonResult<>(result);
  }

}
