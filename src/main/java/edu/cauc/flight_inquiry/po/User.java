package edu.cauc.flight_inquiry.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Data
@Entity
@Table(name = "users")
public class User {

  @Id
  @Column(name = "id")
  private String id;

  @Column(name = "username")
  private String username;

  @Column(name = "password")
  private String password;

  @Column(name = "email")
  private String email;

  @Column(name = "name")
  private String name;

  @Column(name = "sex")
  private int sex;

  @Column(name = "phone")
  private String phone;

  @Column(name = "avatar")
  private String avatar;

  @Column(name = "birthday")
  private Date birthday;

  @Column(name = "enter_time")
  private Date enterTime;

  @Column(name = "state")
  private int state;

}
