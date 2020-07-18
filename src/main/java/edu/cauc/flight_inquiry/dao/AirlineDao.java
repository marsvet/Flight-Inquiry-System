package edu.cauc.flight_inquiry.dao;

import edu.cauc.flight_inquiry.po.Airline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AirlineDao extends JpaRepository<Airline, String>, JpaSpecificationExecutor<AirlineDao> {

  @Query(value = "SELECT * FROM airline WHERE zh_simple_name LIKE CONCAT('%', ?, '%') AND en_simple_name LIKE CONCAT('%', ?, '%') AND zh_full_name LIKE CONCAT('%', ?, '%') AND en_full_name LIKE CONCAT('%', ?, '%') AND two_code LIKE CONCAT('%', ?, '%') AND three_code LIKE CONCAT('%', ?, '%') AND airline_type LIKE CONCAT('%', ?, '%') AND state = 1 LIMIT ?, ?", nativeQuery = true)
  public List<Airline> getAirlines(String zhSimpleName, String enSimpleName, String zhFullName, String enFullName, String twoCode, String threeCode, String airlineType, int startIndex, int pageSize);

  @Query(value = "SELECT COUNT(*) FROM airline WHERE zh_simple_name LIKE CONCAT('%', ?, '%') AND en_simple_name LIKE CONCAT('%', ?, '%') AND zh_full_name LIKE CONCAT('%', ?, '%') AND en_full_name LIKE CONCAT('%', ?, '%') AND two_code LIKE CONCAT('%', ?, '%') AND three_code LIKE CONCAT('%', ?, '%') AND airline_type LIKE CONCAT('%', ?, '%') AND state = 1", nativeQuery = true)
  public int getAirlinesCount(String zhSimpleName, String enSimpleName, String zhFullName, String enFullName, String twoCode, String threeCode, String airlineType);

}
