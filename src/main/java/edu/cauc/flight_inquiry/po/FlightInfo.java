package edu.cauc.flight_inquiry.po;

import lombok.Data;

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

  @Column(name = "start_time")
  private Date startTime;

  @Column(name = "arrive_time")
  private Date arriveTime;

  @Column(name = "plane_type")
  private String planeType;

  @Column(name = "state")
  private int state;

}
