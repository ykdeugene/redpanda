package com.tmsjsb.redpanda.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class SampleEntity {

  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private String username;

  private String password;
  private String email;
  private int active;

  public String getusername() {
    return username;
  }
  public String getpassword() {
    return password;
  }
  public String getemail() {
    return email;
  }
  public int getactive() {
    return active;
  }

  public void setusername(String username){
    this.username = username;
  }
  public void setpassword(String password){
    this.password = password;
  }
  public void setemail(String email){
    this.email = email;
  }
  public void setactive(int active){
    this.active = active;
  }

}

