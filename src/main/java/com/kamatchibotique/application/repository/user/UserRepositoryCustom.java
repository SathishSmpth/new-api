package com.kamatchibotique.application.repository.user;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.model.common.Criteria;
import com.kamatchibotique.application.model.common.GenericEntityList;
import com.kamatchibotique.application.model.user.User;

public interface UserRepositoryCustom {
  
  GenericEntityList<User> listByCriteria(Criteria criteria) throws ServiceException;

}
