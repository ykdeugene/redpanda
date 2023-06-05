package com.tmsjsb.redpanda.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tmsjsb.redpanda.Entity.GroupKeyEntity;
import com.tmsjsb.redpanda.Entity.GroupsEntity;

public interface GroupsRepository extends JpaRepository<GroupsEntity, GroupKeyEntity> {

  List<GroupsEntity> findByUsername(String username);

  boolean existsByGroupName(String groupname);

}
