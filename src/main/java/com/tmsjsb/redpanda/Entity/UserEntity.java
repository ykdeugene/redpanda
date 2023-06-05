package com.tmsjsb.redpanda.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Null;

import com.tmsjsb.redpanda.Interface.UserOnUpdateEmail;
import com.tmsjsb.redpanda.Interface.CompositeValidationGroup;
import com.tmsjsb.redpanda.Interface.UpdateEmailUpdatePasswordGroup;
import com.tmsjsb.redpanda.Interface.UserOnCreate;

import jakarta.validation.GroupSequence;

@Entity
@Table(name = "user")
@GroupSequence({ UserEntity.class, CompositeValidationGroup.class })
public class UserEntity {

  @Id
  @Pattern(regexp = "^[a-zA-Z0-9]+$")
  @NotBlank
  private String username;

  @Column(name = "password")
  @NotBlank(groups = UserOnCreate.class)
  @Null(groups = UserOnUpdateEmail.class)
  private String password;

  @Column(name = "email")
  @Email
  // @Null(groups = CompositeValidationGroup.class)
  @NotEmpty(groups = CompositeValidationGroup.class)
  // @Pattern(regexp = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")
  private String email;

  @Column(name = "activestatus")
  @Null(groups = UpdateEmailUpdatePasswordGroup.class)
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
