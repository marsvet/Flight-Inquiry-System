package edu.cauc.flight_inquiry.dto;

import lombok.Data;

import java.util.Date;

@Data
public class FlightInf {

  private String id;

  private String flightNum;

  private String airline;

  private String startStation;

  private String startTerminal;

  private String destStation;

  private String destTerminal;

  private Date startTime;

  private Date arriveTime;

  private String planeType;

}
