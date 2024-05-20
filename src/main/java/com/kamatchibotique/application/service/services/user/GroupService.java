package com.kamatchibotique.application.service.services.user;

import java.util.List;
import java.util.Set;

import com.kamatchibotique.application.enums.GroupType;
import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.service.services.common.CommonService;
import com.kamatchibotique.application.model.user.Group;


public interface GroupService extends CommonService<Integer, Group> {


	List<Group> listGroup(GroupType groupType) throws ServiceException;
	List<Group> listGroupByIds(Set<Integer> ids) throws ServiceException;
	List<Group> listGroupByNames(List<String> names) throws ServiceException;
	Group findByName(String groupName) throws ServiceException;

}
