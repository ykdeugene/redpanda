package com.tmsjsb.redpanda.Entity;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "plan")
@IdClass(PlanKeyEntity.class)
public class PlanEntity {

  @Id
  @Column(name = "plan_mvp_name")
  @JsonProperty("Plan_MVP_name")
  private String planMVPname;

  @Id
  @Column(name = "plan_appacronym")
  @JsonProperty("Plan_appAcronym")
  private String planappAcronym;

  @Column(name = "plan_startdate")
  @JsonProperty("Plan_startDate")
  private String PlanstartDate;

  @Column(name = "plan_enddate")
  @JsonProperty("Plan_endDate")
  private String PlanendDate;

  @Column(name = "plan_colour")
  @JsonProperty("Plan_colour")
  private String Plancolour;

}
