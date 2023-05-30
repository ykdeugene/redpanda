package com.tmsjsb.redpanda.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user")
public class UserEntity {

  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private String username;

  @Column(name = "password")
  private String password;

  @Column(name = "email")
  private String email;

  @Column(name = "activestatus")
  private int activeStatus;

  public String getUsername() {
    return username;
  }
  public String getPassword() {
    return password;
  }
  public String getEmail() {
    return email;
  }
  public int getActiveStatus() {
    return activeStatus;
  }

  public void setUsername(String username){
    this.username = username;
  }
  public void setPassword(String password){
    this.password = password;
  }
  public void setEmail(String email){
    this.email = email;
  }
  public void setActiveStatus(int activeStatus){
    this.activeStatus = activeStatus;
  }

}

