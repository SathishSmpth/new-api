package com.kamatchibotique.application.service.services.user;

import java.util.List;

import org.springframework.data.domain.Page;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.service.services.common.CommonService;
import com.kamatchibotique.application.model.common.Criteria;
import com.kamatchibotique.application.model.common.GenericEntityList;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.user.User;
import com.kamatchibotique.application.model.user.UserCriteria;



public interface UserService extends CommonService<Long, User> {

  User getByUserName(String userName) throws ServiceException;
  User getByUserName(String userName, String storeCode) throws ServiceException;

  List<User> listUser() throws ServiceException;
  
  User getById(Long id, MerchantStore store);
  
  User getByPasswordResetToken(String storeCode, String token);

  void saveOrUpdate(User user) throws ServiceException;

  List<User> listByStore(MerchantStore store) throws ServiceException;

  User findByStore(Long userId, String storeCode) throws ServiceException;

  @Deprecated
  GenericEntityList<User> listByCriteria(Criteria criteria) throws ServiceException;
  
  Page<User> listByCriteria(UserCriteria criteria, int page, int count) throws ServiceException;
  
  User findByResetPasswordToken (String userName, String token, MerchantStore store) throws ServiceException;
}
