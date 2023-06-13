package com.tmsjsb.redpanda.Entity;

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
  @Column(name = "app_acronym")
  private String App_Acronym;

  @Column(name = "app_description")
  private String App_Description;

  @Column(name = "app_rnumber")
  private Integer App_Rnumber;

  @Column(name = "app_startdate")
  private String App_startDate;

  @Column(name = "app_enddate")
  private String App_endDate;

  @Column(name = "app_permit_create")
  private String App_permit_Create;

  @Column(name = "app_permit_open")
  private String App_permit_Open;

  @Column(name = "app_permit_todolist")
  private String App_permit_toDoList;

  @Column(name = "app_permit_doing")
  private String App_permit_Doing;

  @Column(name = "app_permit_done")
  private String App_permit_Done;
}
