package edu.cauc.flight_inquiry.dao;

import edu.cauc.flight_inquiry.po.FlightInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FlightInfoDao extends JpaRepository<FlightInfo, String>, JpaSpecificationExecutor<FlightInfo> {

  @Query(value = "SELECT * FROM flight_info WHERE flight_num LIKE CONCAT('%', ?, '%') AND airline LIKE CONCAT('%', ?, '%') AND start_station LIKE CONCAT('%', ?, '%') AND dest_station LIKE CONCAT('%', ?, '%') AND start_time LIKE CONCAT('%', ?, '%') AND arrive_time LIKE CONCAT('%', ?, '%') AND plane_type LIKE CONCAT('%', ?, '%') AND state = 1 LIMIT ?, ?", nativeQuery = true)
  public List<FlightInfo> getFlightInfos(String flightNum, String airline, String startStation, String destStation, String startTime, String arriveTime, String planeType, int startIndex, int pageSize);

  @Query(value = "SELECT COUNT(*) FROM flight_info WHERE flight_num LIKE CONCAT('%', ?, '%') AND airline LIKE CONCAT('%', ?, '%') AND start_station LIKE CONCAT('%', ?, '%') AND dest_station LIKE CONCAT('%', ?, '%') AND start_time LIKE CONCAT('%', ?, '%') AND arrive_time LIKE CONCAT('%', ?, '%') AND plane_type LIKE CONCAT('%', ?, '%') AND state = 1", nativeQuery = true)
  public int getFlightInfosCount(String flightNum, String airline, String startStation, String destStation, String startTime, String arriveTime, String planeType);

}
