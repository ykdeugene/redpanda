package com.tmsjsb.redpanda.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "application")

public class AppEntity {
  @Id
  @JsonProperty("App_Acronym")
  @Column(name = "app_acronym")
  private String app_Acronym;

  @JsonProperty("App_Description")
  @Column(name = "app_description")
  private String app_Description;

  @JsonProperty("App_Rnumber")
  @Column(name = "app_rnumber")
  private Integer app_Rnumber;

  @JsonProperty("App_startDate")
  @Column(name = "app_startdate")
  private String app_startDate;

  @JsonProperty("App_endDate")
  @Column(name = "app_enddate")
  private String app_endDate;

  @JsonProperty("App_permit_Create")
  @Column(name = "app_permit_create")
  private String app_permit_Create;

  @JsonProperty("App_permit_Open")
  @Column(name = "app_permit_open")
  private String app_permit_Open;

  @JsonProperty("App_permit_toDoList")
  @Column(name = "app_permit_todolist")
  private String app_permit_toDoList;

  @JsonProperty("App_permit_Doing")
  @Column(name = "app_permit_doing")
  private String app_permit_Doing;

  @JsonProperty("App_permit_Done")
  @Column(name = "app_permit_done")
  private String app_permit_Done;
}
