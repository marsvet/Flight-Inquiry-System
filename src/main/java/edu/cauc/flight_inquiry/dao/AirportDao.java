package edu.cauc.flight_inquiry.dao;

import edu.cauc.flight_inquiry.po.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AirportDao extends JpaRepository<Airport, String>, JpaSpecificationExecutor<Airport> {

  @Query(value = "SELECT * FROM airport WHERE zh_simple_name LIKE CONCAT('%', ?, '%') AND en_simple_name LIKE CONCAT('%', ?, '%') AND zh_full_name LIKE CONCAT('%', ?, '%') AND en_full_name LIKE CONCAT('%', ?, '%') AND three_code LIKE CONCAT('%', ?, '%') AND four_code LIKE CONCAT('%', ?, '%') AND airport_type LIKE CONCAT('%', ?, '%') AND state = 1 LIMIT ?, ?", nativeQuery = true)
  public List<Airport> getAirports(String zhSimpleName, String enSimpleName, String zhFullName, String enFullName, String threeCode, String fourCode, String airportType, int startIndex, int pageSize);

  @Query(value = "SELECT COUNT(*) FROM airport WHERE zh_simple_name LIKE CONCAT('%', ?, '%') AND en_simple_name LIKE CONCAT('%', ?, '%') AND zh_full_name LIKE CONCAT('%', ?, '%') AND en_full_name LIKE CONCAT('%', ?, '%') AND three_code LIKE CONCAT('%', ?, '%') AND four_code LIKE CONCAT('%', ?, '%') AND airport_type LIKE CONCAT('%', ?, '%') AND state = 1", nativeQuery = true)
  public int getAirportsCount(String zhSimpleName, String enSimpleName, String zhFullName, String enFullName, String threeCode, String fourCode, String airportType);

}
