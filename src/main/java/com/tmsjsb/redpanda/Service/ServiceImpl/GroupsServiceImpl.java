package com.tmsjsb.redpanda.Service.ServiceImpl;

import lombok.*;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import com.tmsjsb.redpanda.Entity.GroupsEntity;
import com.tmsjsb.redpanda.Repository.GroupsRepository;
import com.tmsjsb.redpanda.Service.GroupsService;

import jakarta.validation.ValidationException;

import java.util.List;

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
  public void createGroup(GroupsEntity newGroup) throws ValidationException {
    GroupsEntity newUserGroupEntry = new GroupsEntity();

    // check if group already exists first, if yes throw error.
    if (groupExists(newGroup.getGroupName()) && (newGroup.getUsername().isEmpty())) {
      System.out.println("newGroup.getGroupName(): " + newGroup.getGroupName());
      throw new ValidationException();
    }
    // add new group w/o username
    else if ((!(groupExists(newGroup.getGroupName())) && (newGroup.getUsername().isEmpty() == true))) {
      newUserGroupEntry.setGroupName(newGroup.getGroupName());
      newUserGroupEntry.setUsername(" ");
    }
    // add new group w username
    else if (((groupExists(newGroup.getGroupName())) && (newGroup.getUsername().isEmpty() == false))) {
      newUserGroupEntry.setGroupName(newGroup.getGroupName());
      newUserGroupEntry.setUsername(newGroup.getUsername());
    }
    groupsRepository.save(newUserGroupEntry);
  }

  @Override
  public boolean groupExists(String groupname) {
    return groupsRepository.existsByGroupName(groupname);
  }

  @Override
  public String removeGroup(GroupsEntity oldGroup) {
    groupsRepository.delete(oldGroup);
    return "done";
  }

}
