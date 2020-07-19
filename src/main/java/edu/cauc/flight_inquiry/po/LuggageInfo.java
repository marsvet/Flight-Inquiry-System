package edu.cauc.flight_inquiry.po;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "luggage_info")
public class LuggageInfo {

  @Id
  @Column(name = "id")
  private String id;

  @Column(name = "flight_num")
  private String flightNum;

  @Column(name = "real_start_time")
  private Date realStartTime;

  @Column(name = "real_end_time")
  private Date realEndTime;

  @Column(name = "luggage_num")
  private String luggageNum;

  @Column(name = "turn_num")
  private String turnNum;

  @Column(name = "terminal")
  private String terminal;

  @Column(name = "flight_type")
  private String flightType;

  @Column(name = "state")
  private int state;

}
