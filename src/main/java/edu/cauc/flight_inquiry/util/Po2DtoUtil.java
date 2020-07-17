package edu.cauc.flight_inquiry.util;

import edu.cauc.flight_inquiry.dao.AirlineDao;
import edu.cauc.flight_inquiry.dto.FlightInf;
import edu.cauc.flight_inquiry.po.Airline;
import edu.cauc.flight_inquiry.po.FlightInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class Po2DtoUtil {

  @Autowired
  private static AirlineDao airlineDao;


  public static FlightInf flightInfo2FlightInf(FlightInfo flightInfo) {

	Optional<Airline> airlineOp = airlineDao.findById(flightInfo.getAirlineId());

	if (!airlineOp.isPresent())
	  return null;

	FlightInf flightInf = new FlightInf();
	flightInf.setId(flightInfo.getId());
	flightInf.setFlightNum(flightInfo.getFlightNum());
	flightInf.setAirline(airlineOp.get().getZhFullName());
	flightInf.setStartStation(flightInfo.getStartStation());
	flightInf.setStartTerminal(flightInfo.getStartTerminal());
	flightInf.setDestStation(flightInfo.getDestTerminal());
	flightInf.setDestTerminal(flightInfo.getDestTerminal());
	flightInf.setStartTime(flightInfo.getStartTime());
	flightInf.setArriveTime(flightInfo.getArriveTime());
	flightInf.setPlaneType(flightInfo.getPlaneType());
	flightInf.setState(flightInfo.getState());

	return flightInf;

  }

}
