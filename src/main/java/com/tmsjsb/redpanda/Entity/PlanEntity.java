package com.tmsjsb.redpanda.Entity;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

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
  @Pattern(regexp = "^[a-zA-Z0-9 ]*$")
  private String planMVPname;

  @Id
  @Column(name = "plan_appacronym")
  @JsonProperty("Plan_appAcronym")
  @NotBlank
  private String planappAcronym;

  @Column(name = "plan_startdate")
  @JsonProperty("Plan_startDate")
  @NotBlank
  @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$")
  private String planstartDate;

  @Column(name = "plan_enddate")
  @JsonProperty("Plan_endDate")
  @NotBlank
  @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$")
  private String planendDate;

  @Column(name = "plan_colour")
  @JsonProperty("Plan_colour")
  @NotBlank
  @Pattern(regexp = "^#([A-Fa-f0-9]{6})$")
  private String plancolour;

}
