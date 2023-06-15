package com.tmsjsb.redpanda.Entity;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

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
  @NotBlank
  private String plan_MVP_name;

  @Id
  @Column(name = "plan_appacronym")
  @JsonProperty("Plan_appAcronym")
  @NotBlank
  private String planappAcronym;

  @Column(name = "plan_startdate")
  @NotBlank
  private String PlanstartDate;

  @Column(name = "plan_enddate")
  @NotBlank
  private String PlanendDate;

  @Column(name = "plan_colour")
  @NotBlank
  private String Plancolour;

}
