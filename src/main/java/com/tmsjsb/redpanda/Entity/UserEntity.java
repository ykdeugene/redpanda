package com.tmsjsb.redpanda.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;

import com.tmsjsb.redpanda.Interface.UserOnUpdateEmail;
import com.tmsjsb.redpanda.Interface.UserOnUpdatePassword;
import com.tmsjsb.redpanda.Interface.UserOnCreate;

@Entity
@Table(name = "user")
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Pattern(regexp = "^[a-zA-Z0-9]+$")
  @NotBlank
  private String username;

  @Column(name = "password")
  // @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&+=]).{8,10}$")
  @NotBlank(groups = UserOnCreate.class)
  @Null(groups = UserOnUpdateEmail.class)
  private String password;

  @Column(name = "email")
  @Email
  @Pattern(regexp = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")
  @Null(groups = UserOnUpdatePassword.class)
  private String email;

  @Column(name = "activestatus")
  @Null(groups = UserOnUpdateEmail.class)
  @Null(groups = UserOnUpdatePassword.class)
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

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setActiveStatus(int activeStatus) {
    this.activeStatus = activeStatus;
  }

}
