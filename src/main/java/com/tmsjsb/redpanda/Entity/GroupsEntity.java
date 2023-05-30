package com.tmsjsb.redpanda.Entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usergroup")
@IdClass(GroupKeyEntity.class)

public class GroupsEntity {
  @Id
  @Column(name = "groupname")
  @NotBlank
  @Pattern(regexp = "^[a-zA-Z]+$")
  private String groupName;

  @Id
  @Column(name = "username")
  @Nullable
  // @Pattern(regexp = "^[a-zA-Z0-9]+$")
  private String username;
}
