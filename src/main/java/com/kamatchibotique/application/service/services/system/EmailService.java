package com.kamatchibotique.application.service.services.system;



import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.email.Email;
import com.kamatchibotique.application.email.EmailConfig;
import com.kamatchibotique.application.model.merchant.MerchantStore;


public interface EmailService {

	void sendHtmlEmail(MerchantStore store, Email email) throws ServiceException, Exception;
	
	EmailConfig getEmailConfiguration(MerchantStore store) throws ServiceException;
	
	void saveEmailConfiguration(EmailConfig emailConfig, MerchantStore store) throws ServiceException;
	
}
