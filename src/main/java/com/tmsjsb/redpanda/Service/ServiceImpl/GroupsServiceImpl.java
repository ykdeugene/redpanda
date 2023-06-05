package com.tmsjsb.redpanda.Service.ServiceImpl;

import lombok.*;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tmsjsb.redpanda.Entity.GroupsEntity;
import com.tmsjsb.redpanda.Repository.GroupsRepository;
import com.tmsjsb.redpanda.Service.ErrorMgrService;
import com.tmsjsb.redpanda.Service.GroupsService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class GroupsServiceImpl implements GroupsService {

  private GroupsRepository groupsRepository;

  @Override
  public List<GroupsEntity> getAllGroups() {
    Sort sortByUsername = Sort.by(Sort.Direction.ASC, "username");
    return groupsRepository.findAll(sortByUsername);
  }

  @Override
  public Map<String, Object> createGroup(GroupsEntity newGroup) {

    Map<String, Object> jsonObject = new HashMap<>(0);

    GroupsEntity newUserGroupEntry = new GroupsEntity();

    // check if group already exists first, if yes throw error.
    if (groupExists(newGroup.getGroupName()) && (newGroup.getUsername() == null)) {
      jsonObject = ErrorMgrService.errorHandler("data exists", Thread.currentThread().getStackTrace()[1]);
    }
    // add new group w/o username
    else if ((!(groupExists(newGroup.getGroupName())) && (newGroup.getUsername() == null))) {
      newUserGroupEntry.setGroupName(newGroup.getGroupName());
      newUserGroupEntry.setUsername(" ");
      jsonObject.put("results", "true");
    }
    // add new group w username
    else {
      newUserGroupEntry.setGroupName(newGroup.getGroupName());
      newUserGroupEntry.setUsername(newGroup.getUsername());
      jsonObject.put("results", "true");
    }
    groupsRepository.save(newUserGroupEntry);
    return jsonObject;
  }

  /*
   * old code:
   * 
   * @Override
   * public void createGroup(GroupsEntity newGroup) throws ValidationException {
   * GroupsEntity newUserGroupEntry = new GroupsEntity();
   * 
   * // check if group already exists first, if yes throw error.
   * if (groupExists(newGroup.getGroupName()) &&
   * (newGroup.getUsername().isEmpty())) {
   * System.out.println("newGroup.getGroupName(): " + newGroup.getGroupName());
   * throw new ValidationException();
   * }
   * // add new group w/o username
   * else if ((!(groupExists(newGroup.getGroupName())) &&
   * (newGroup.getUsername().isEmpty() == true))) {
   * newUserGroupEntry.setGroupName(newGroup.getGroupName());
   * newUserGroupEntry.setUsername(" ");
   * }
   * // add new group w username
   * else if (((groupExists(newGroup.getGroupName())) &&
   * (newGroup.getUsername().isEmpty() == false))) {
   * newUserGroupEntry.setGroupName(newGroup.getGroupName());
   * newUserGroupEntry.setUsername(newGroup.getUsername());
   * }
   * groupsRepository.save(newUserGroupEntry);
   * }
   */

  @Override
  public boolean groupExists(String groupname) {
    return groupsRepository.existsByGroupName(groupname);
  }

  @Override
  public void removeGroup(GroupsEntity oldGroup) {
    groupsRepository.delete(oldGroup);
  }

  @Override
  public Object getGroup(String token) {
    try {
      DecodedJWT decoded = JWT.decode(token);
      String username = decoded.getClaim("username").asString();
      List<GroupsEntity> groups = groupsRepository.findByUsername(username);
      return groups;
    } catch (Exception e) {
      Map<String, Object> jsonObject = new HashMap<>(0);
      jsonObject = ErrorMgrService.errorHandler(e, Thread.currentThread().getStackTrace()[1]);
      return jsonObject;
    }

  }

}
