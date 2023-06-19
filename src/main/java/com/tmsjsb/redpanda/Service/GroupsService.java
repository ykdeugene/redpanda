package com.tmsjsb.redpanda.Service;

import java.util.List;
import java.util.Map;

import com.tmsjsb.redpanda.Entity.GroupsEntity;

public interface GroupsService {

  // methods in interface are considered public in default
  // no need to explicitly declare as public

  List<GroupsEntity> getAllGroups();

  Map<String, Object> createGroup(GroupsEntity newGroup);

  boolean groupExists(String groupname);

  void removeGroup(GroupsEntity oldGroup);

  Object getGroup(String token);
  
  List<GroupsEntity> getGroupList();

}
