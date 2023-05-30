package com.tmsjsb.redpanda.Entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class GroupKeyEntity implements Serializable {

  private String groupName;

  private String username;

}
