package edu.cauc.flight_inquiry.dao;

import edu.cauc.flight_inquiry.po.FlightInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface FlightInfoDao extends JpaRepository<FlightInfo, String>, JpaSpecificationExecutor<FlightInfo> {

  public List<FlightInfo> findAllByFlightNum(String flightNum);

  public Optional<FlightInfo> findByAirlineId(String airlineId);

  public List<FlightInfo> findAllByStartStationLike(String startStationLike);

  public List<FlightInfo> findAllByStartTerminal(String startTerminal);

  public List<FlightInfo> findAllByDestStationLike(String destStationLike);

  public List<FlightInfo> findAllByDestTerminal(String destTerminal);

  public List<FlightInfo> findAllByStartTimeLike(Date startTimeLike);

  public List<FlightInfo> findAllByArriveTimeLike(Date arriveTimeLike);

  public List<FlightInfo> findAllByPlaneTypeLike(String planeTypeLike);

}
