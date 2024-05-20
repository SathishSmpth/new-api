package com.kamatchibotique.application.utils;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.constants.Constants;
import com.kamatchibotique.application.exception.UnauthorizedException;

@Component
public class AuthorizationUtils {
	
//	@Autowired
//	private UserFacade userFacade;
//
//	public String authenticatedUser() {
//		String authenticatedUser = userFacade.authenticatedUser();
//		if (authenticatedUser == null) {
//			throw new UnauthorizedException();
//		}
//		return authenticatedUser;
//	}
//
//	public void authorizeUser(String authenticatedUser, List<String> roles, MerchantStore store) {
//		userFacade.authorizedGroup(authenticatedUser, roles);
//		if (!userFacade.userInRoles(authenticatedUser, Arrays.asList(Constants.GROUP_SUPERADMIN))) {
//			if (!userFacade.authorizedStore(authenticatedUser, store.getCode())) {
//				throw new UnauthorizedException("Operation unauthorized for user [" + authenticatedUser
//						+ "] and store [" + store.getCode() + "]");
//			}
//		}
//	}

}
