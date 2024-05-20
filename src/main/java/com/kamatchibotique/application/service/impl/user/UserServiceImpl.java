package com.kamatchibotique.application.service.impl.user;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.model.common.Criteria;
import com.kamatchibotique.application.model.common.GenericEntityList;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.user.User;
import com.kamatchibotique.application.model.user.UserCriteria;
import com.kamatchibotique.application.repository.user.PageableUserRepository;
import com.kamatchibotique.application.repository.user.UserRepository;
import com.kamatchibotique.application.service.impl.common.CommonServiceImpl;
import com.kamatchibotique.application.service.services.merchant.MerchantStoreService;
import com.kamatchibotique.application.service.services.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Objects;

public class UserServiceImpl extends CommonServiceImpl<Long, User> implements UserService {

	private UserRepository userRepository;

	@Autowired
	private MerchantStoreService merchantStoreService;

	@Autowired
	private PageableUserRepository pageableUserRepository;

	@Autowired
	public UserServiceImpl(UserRepository userRepository) {
		super(userRepository);
		this.userRepository = userRepository;

	}

	@Override
	public User getByUserName(String userName) throws ServiceException {
		return userRepository.findByUserName(userName);
	}

	@Override
	public void delete(User user) throws ServiceException {
		User u = this.getById(user.getId());
		super.delete(u);

	}

	@Override
	public List<User> listUser() throws ServiceException {
		try {
			return userRepository.findAll();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<User> listByStore(MerchantStore store) throws ServiceException {
		try {
			return userRepository.findByStore(store.getId());
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void saveOrUpdate(User user) throws ServiceException {
		userRepository.save(user);
	}

	@Override
	public User findByStore(Long userId, String storeCode) throws ServiceException {

		User user = userRepository.findOne(userId);

		// store must be in lineage
		boolean isFound = merchantStoreService.isStoreInGroup(storeCode);

		if (isFound)
			return user;

		return null;

	}

	@Override
	public GenericEntityList<User> listByCriteria(Criteria criteria) throws ServiceException {
		return userRepository.listByCriteria(criteria);
	}

	@Override
	public User getByUserName(String userName, String storeCode) throws ServiceException {
		return userRepository.findByUserName(userName, storeCode);
	}

	@Override
	public Page<User> listByCriteria(UserCriteria criteria, int page, int count) throws ServiceException {

		Pageable pageRequest = PageRequest.of(page, count);
		Page<User> users = null;
		if (criteria.getStoreIds() != null) {// search within a predefined list
												// of stores
			users = pageableUserRepository.listByStoreIds(criteria.getStoreIds(), criteria.getAdminEmail(),
					pageRequest);
		} else if (StringUtils.isBlank(criteria.getStoreCode())) {// search for
																	// a
																	// specific
																	// store
			users = pageableUserRepository.listAll(criteria.getAdminEmail(), pageRequest);
		} else if (criteria.getStoreCode() != null) {// store code
			users = pageableUserRepository.listByStore(criteria.getStoreCode(), criteria.getAdminEmail(), pageRequest);
		}

		return users;
	}

	@Override
	public User getById(Long id, MerchantStore store) {
		Objects.requireNonNull(store, "MerchantStore cannot be null");
		return userRepository.findByUserId(id, store.getCode());
	}

	@Override
	public User findByResetPasswordToken(String userName, String token, MerchantStore store) throws ServiceException {
		Objects.requireNonNull(userName, "User name cannot be null");
		Objects.requireNonNull(token, "Token cannot be null");
		Objects.requireNonNull(store, "MerchantStore cannot be null");
		return null;
	}

	@Override
	public User getByPasswordResetToken(String storeCode, String token) {
		return userRepository.findByResetPasswordToken(token, storeCode);
	}

}
