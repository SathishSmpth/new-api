package com.kamatchibotique.application.service.impl.user;

import com.kamatchibotique.application.enums.GroupType;
import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.model.user.Group;
import com.kamatchibotique.application.repository.user.GroupRepository;
import com.kamatchibotique.application.service.impl.common.CommonServiceImpl;
import com.kamatchibotique.application.service.services.user.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service("groupService")
public class GroupServiceImpl extends CommonServiceImpl<Integer, Group>
    implements GroupService {

  GroupRepository groupRepository;


 @Autowired
  public GroupServiceImpl(GroupRepository groupRepository) {
    super(groupRepository);
    this.groupRepository = groupRepository;

  }


  @Override
  public List<Group> listGroup(GroupType groupType) throws ServiceException {
    try {
      return groupRepository.findByType(groupType);
    } catch (Exception e) {
      throw new ServiceException(e);
    }
  }

  public List<Group> listGroupByIds(Set<Integer> ids) throws ServiceException {

      try {
        return ids.isEmpty() ? new ArrayList<Group>() : groupRepository.findByIds(ids);
      } catch (Exception e) {
        throw new ServiceException(e);
      }

  }


  @Override
  public Group findByName(String groupName) throws ServiceException {
    return groupRepository.findByGroupName(groupName);
  }


  @Override
  public List<Group> listGroupByNames(List<String> names) throws ServiceException {
    return groupRepository.findByNames(names);
  }


}
