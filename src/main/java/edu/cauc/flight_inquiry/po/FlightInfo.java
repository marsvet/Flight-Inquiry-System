package edu.cauc.flight_inquiry.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "flight_info")
public class FlightInfo {

  @Id
  @Column(name = "id")
  private String id;

  @Column(name = "flight_num")
  private String flightNum;

  @Column(name = "airline")
  private String airline;

  @Column(name = "start_station")
  private String startStation;

  @Column(name = "start_terminal")
  private String startTerminal;

  @Column(name = "dest_station")
  private String destStation;

  @Column(name = "dest_terminal")
  private String destTerminal;

  @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
  @Column(name = "start_time")
  private Date startTime;

  @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
  @Column(name = "arrive_time")
  private Date arriveTime;

  @Column(name = "plane_type")
  private String planeType;

  @Column(name = "state")
  private int state;

}
