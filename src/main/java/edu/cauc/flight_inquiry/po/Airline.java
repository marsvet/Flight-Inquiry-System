package edu.cauc.flight_inquiry.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "airline")
public class Airline {

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

  @Column(name = "two_code")
  private String twoCode;

  @Column(name = "three_code")
  private String threeCode;

  @Column(name = "type")
  private String type;

  @Column(name = "state")
  private int state;

}
