package edu.cauc.flight_inquiry.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "airport")
public class Airport {

  @Id
  @Column(name = "id")
  private String id;

  @Column(name = "zh_simple_name")
  private String zhSimpleName;

  @Column(name = "en_simple_name")
  private String enSimpleName;

  @Column(name = "zh_full_name")
  private String zhFullName;

  @Column(name = "en_full_name")
  private String enFullName;

  @Column(name = "three_code")
  private String threeCode;

  @Column(name = "four_code")
  private String fourCode;

  @Column(name = "airport_type")
  private String airportType;

  @Column(name = "state")
  private int state;

}
