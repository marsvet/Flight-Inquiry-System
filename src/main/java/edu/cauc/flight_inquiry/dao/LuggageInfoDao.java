package edu.cauc.flight_inquiry.dao;

import edu.cauc.flight_inquiry.po.LuggageInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LuggageInfoDao extends JpaRepository<LuggageInfo, String>, JpaSpecificationExecutor<LuggageInfo> {

  @Query(value = "SELECT * FROM luggage_info WHERE flight_num LIKE CONCAT('%', ?, '%') AND real_start_time LIKE CONCAT('%', ?, '%') AND real_end_time LIKE CONCAT('%', ?, '%') AND luggage_num LIKE CONCAT('%', ?, '%') AND turn_num LIKE CONCAT('%', ?, '%') AND terminal LIKE CONCAT('%', ?, '%') AND flight_type LIKE CONCAT('%', ?, '%') AND state = 1 LIMIT ?, ?", nativeQuery = true)
  public List<LuggageInfo> getLuggageInfos(String flightNum, String realStartTime, String realEndTime, String luggageNum, String turnNum, String terminal, String flightType, int startIndex, int pageSize);

  @Query(value = "SELECT COUNT(*) FROM luggage_info WHERE flight_num LIKE CONCAT('%', ?, '%') AND real_start_time LIKE CONCAT('%', ?, '%') AND real_end_time LIKE CONCAT('%', ?, '%') AND luggage_num LIKE CONCAT('%', ?, '%') AND turn_num LIKE CONCAT('%', ?, '%') AND terminal LIKE CONCAT('%', ?, '%') AND flight_type LIKE CONCAT('%', ?, '%') AND state = 1", nativeQuery = true)
  public int getLuggageInfosCount(String flightNum, String realStartTime, String realEndTime, String luggageNum, String turnNum, String terminal, String flightType);

}
