package com.tmsjsb.redpanda.Service;

import java.util.List;

import com.tmsjsb.redpanda.Entity.GroupsEntity;

public interface GroupsService {

  // methods in interface are considered public in default
  // no need to explicitly declare as public

  List<GroupsEntity> getAllGroups();

  void createGroup(GroupsEntity newGroup);

  boolean groupExists(String groupname);

  String removeGroup(GroupsEntity oldGroup);

}
