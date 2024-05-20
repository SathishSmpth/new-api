package com.kamatchibotique.application.service.impl.user;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.model.user.Group;
import com.kamatchibotique.application.model.user.Permission;
import com.kamatchibotique.application.model.user.PermissionCriteria;
import com.kamatchibotique.application.model.user.PermissionList;
import com.kamatchibotique.application.repository.user.PermissionRepository;
import com.kamatchibotique.application.service.impl.common.CommonServiceImpl;
import com.kamatchibotique.application.service.services.user.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;



@Service("permissionService")
public class PermissionServiceImpl extends
		CommonServiceImpl<Integer, Permission> implements
		PermissionService {

	private PermissionRepository permissionRepository;


	@Autowired
	public PermissionServiceImpl(PermissionRepository permissionRepository) {
		super(permissionRepository);
		this.permissionRepository = permissionRepository;

	}

	@Override
	public List<Permission> getByName() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Permission getById(Integer permissionId) {
		return permissionRepository.findOne(permissionId);

	}


	@Override
	public void deletePermission(Permission permission) throws ServiceException {
		permission = this.getById(permission.getId());//Prevents detached entity error
		permission.setGroups(null);
		
		this.delete(permission);
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public List<Permission> getPermissions(List<Integer> groupIds)
			throws ServiceException {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Set ids = new HashSet(groupIds);
		return permissionRepository.findByGroups(ids);
	}

	@Override
	public PermissionList listByCriteria(PermissionCriteria criteria)
			throws ServiceException {
		return permissionRepository.listByCriteria(criteria);
	}

	@Override
	public void removePermission(Permission permission,Group group) throws ServiceException {
		permission = this.getById(permission.getId());//Prevents detached entity error
	
		permission.getGroups().remove(group);
		

	}

	@Override
	public List<Permission> listPermission() throws ServiceException {
		return permissionRepository.findAll();
	}



}
