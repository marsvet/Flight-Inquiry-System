package edu.cauc.flight_inquiry.controller;


import edu.cauc.flight_inquiry.dao.AirlineDao;
import edu.cauc.flight_inquiry.dao.FlightInfoDao;
import edu.cauc.flight_inquiry.dto.FlightInf;
import edu.cauc.flight_inquiry.po.Airline;
import edu.cauc.flight_inquiry.po.FlightInfo;
import edu.cauc.flight_inquiry.dto.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
@CrossOrigin
public class FlightInfoController {

  @Autowired
  private FlightInfoDao flightInfoDao;
  
  @Autowired
  private AirlineDao airlineDao;


  @RequestMapping(value = "/addFlightInfo", method = RequestMethod.POST)
  public JsonResult<Object> addFlightInfo(@RequestBody FlightInf flightInf) {
    Optional<Airline> airlineOp = airlineDao.findByZhFullName(flightInf.getAirline());
    String airlineId = null;
    if (!airlineOp.isPresent()) {
      Airline airline = new Airline();
      airlineId = UUID.randomUUID().toString();
      airline.setId(airlineId);
      airline.setZhFullName(flightInf.getAirline());
      airlineDao.save(airline);
    } else {
      airlineId = airlineOp.get().getId();
    }
    
    FlightInfo flightInfo = new FlightInfo();
    flightInfo.setId(UUID.randomUUID().toString());
    flightInfo.setFlightNum(flightInf.getFlightNum());
    flightInfo.setAirlineId(airlineId);
    flightInfo.setStartStation(flightInf.getStartStation());
    flightInfo.setStartTerminal(flightInf.getStartTerminal());
    flightInfo.setDestStation(flightInf.getDestTerminal());
    flightInfo.setDestTerminal(flightInf.getDestTerminal());
    flightInfo.setStartTime(flightInf.getStartTime());
    flightInfo.setArriveTime(flightInf.getArriveTime());
    flightInfo.setPlaneType(flightInf.getPlaneType());
    flightInfo.setState(1);
    flightInfoDao.save(flightInfo);
    
    return new JsonResult<>();
  }

  @RequestMapping(value = "/updateFlightInfo", method = RequestMethod.POST)
  public JsonResult<Object> updateFlightInfo(@RequestBody FlightInf flightInf) {
    Optional<Airline> airlineOp = airlineDao.findByZhFullName(flightInf.getAirline());
    String airlineId = null;
    if (!airlineOp.isPresent()) {
      Airline airline = new Airline();
      airlineId = UUID.randomUUID().toString();
      airline.setId(airlineId);
      airline.setZhFullName(flightInf.getAirline());
      airlineDao.save(airline);
    } else {
      airlineId = airlineOp.get().getId();
    }

    FlightInfo flightInfo = new FlightInfo();
    flightInfo.setFlightNum(flightInf.getFlightNum());
    flightInfo.setAirlineId(airlineId);
    flightInfo.setStartStation(flightInf.getStartStation());
    flightInfo.setStartTerminal(flightInf.getStartTerminal());
    flightInfo.setDestStation(flightInf.getDestTerminal());
    flightInfo.setDestTerminal(flightInf.getDestTerminal());
    flightInfo.setStartTime(flightInf.getStartTime());
    flightInfo.setArriveTime(flightInf.getArriveTime());
    flightInfo.setPlaneType(flightInf.getPlaneType());
    flightInfoDao.save(flightInfo);

    return new JsonResult<>();
  }

  @RequestMapping(value = "/delFlightInfo", method = RequestMethod.POST)
  public JsonResult<Object> delFlightInfo(@RequestBody String id) {
    flightInfoDao.deleteById(id);
    return new JsonResult<>();
  }

  @RequestMapping(value = "/getAllFlightInfo", method = RequestMethod.GET)
  public JsonResult<List<FlightInfo>> getAllFlightInfo() {
    List<FlightInfo> list = flightInfoDao.findAll();
    JsonResult<List<FlightInfo>> result = new JsonResult<>(list);
    return result;
  }

  @RequestMapping(value = "/getFlightInfoById", method = RequestMethod.GET)
  public JsonResult<FlightInfo> getFlightInfo(@RequestParam String id) {
    Optional<FlightInfo> flightInfo = flightInfoDao.findById(id);
    if (flightInfo.isPresent())
      return new JsonResult<>(flightInfo.get());
    else
      return new JsonResult<>(1, "没有对应的航班信息", null);
  }

}
