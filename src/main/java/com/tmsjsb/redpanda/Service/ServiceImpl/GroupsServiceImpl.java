package com.tmsjsb.redpanda.Service.ServiceImpl;

import lombok.*;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import com.tmsjsb.redpanda.Entity.GroupsEntity;
import com.tmsjsb.redpanda.Repository.GroupsRepository;
import com.tmsjsb.redpanda.Service.GroupsService;

import jakarta.validation.ValidationException;

import java.util.List;
import java.util.Objects;

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
    // additional logic/validations here

    // check if group already exists first, if yes throw error.
    if (groupExists(newGroup.getGroupName()) && (Objects.isNull(newGroup.getUsername()))) {
      System.out.println("newGroup.getGroupName(): " + newGroup.getGroupName());
      throw new ValidationException();
    }
    // add new group w/o username
    else if ((!(groupExists(newGroup.getGroupName())) && (Objects.isNull(newGroup.getUsername())))) {
      System.out.println("empty");
      newGroup.setUsername(" ");
    }
    groupsRepository.save(newGroup);
  }

  @Override
  public boolean groupExists(String groupname) {
    return groupsRepository.existsByGroupName(groupname);
  }

  @Override
  public String removeGroup(GroupsEntity oldGroup)
  {
    groupsRepository.delete(oldGroup);
    return "done";
  }

}
