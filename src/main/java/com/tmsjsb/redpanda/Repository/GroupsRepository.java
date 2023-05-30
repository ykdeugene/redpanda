package com.tmsjsb.redpanda.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tmsjsb.redpanda.Entity.GroupKeyEntity;
import com.tmsjsb.redpanda.Entity.GroupsEntity;

public interface GroupsRepository extends JpaRepository<GroupsEntity, GroupKeyEntity> {

  boolean existsByGroupName(String groupname);

}
