package com.tmsjsb.redpanda.Entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class PlanKeyEntity implements Serializable {

  private String planMVPname;

  private String planappAcronym;

}
