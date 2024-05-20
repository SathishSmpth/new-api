package com.kamatchibotique.application.service.impl.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kamatchibotique.application.constants.Constants;
import com.kamatchibotique.application.email.Email;
import com.kamatchibotique.application.email.EmailConfig;
import com.kamatchibotique.application.email.HtmlEmailSender;
import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.system.MerchantConfiguration;
import com.kamatchibotique.application.service.services.system.EmailService;
import com.kamatchibotique.application.service.services.system.MerchantConfigurationService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("emailService")
public class EmailServiceImpl implements EmailService {

	private MerchantConfigurationService merchantConfigurationService;

	private HtmlEmailSender sender;

	public EmailServiceImpl() {
	}

	@Autowired
	public EmailServiceImpl(MerchantConfigurationService merchantConfigurationService, HtmlEmailSender sender) {
		this.merchantConfigurationService = merchantConfigurationService;
		this.sender = sender;
	}

	@Override
	public void sendHtmlEmail(MerchantStore store, Email email) throws ServiceException, Exception {

		EmailConfig emailConfig = getEmailConfiguration(store);
		
		sender.setEmailConfig(emailConfig);
		sender.send(email);
	}

	@Override
	public EmailConfig getEmailConfiguration(MerchantStore store) throws ServiceException {
		
		MerchantConfiguration configuration = merchantConfigurationService.getMerchantConfiguration(Constants.EMAIL_CONFIG, store);
		EmailConfig emailConfig = null;
		if(configuration!=null) {
			String value = configuration.getValue();
			
			ObjectMapper mapper = new ObjectMapper();
			try {
				emailConfig = mapper.readValue(value, EmailConfig.class);
			} catch(Exception e) {
				throw new ServiceException("Cannot parse json string " + value);
			}
		}
		return emailConfig;
	}
	
	
	@Override
	public void saveEmailConfiguration(EmailConfig emailConfig, MerchantStore store) throws ServiceException {
		MerchantConfiguration configuration = merchantConfigurationService.getMerchantConfiguration(Constants.EMAIL_CONFIG, store);
		if(configuration==null) {
			configuration = new MerchantConfiguration();
			configuration.setMerchantStore(store);
			configuration.setKey(Constants.EMAIL_CONFIG);
		}
		
		String value = emailConfig.toJSONString();
		configuration.setValue(value);
		merchantConfigurationService.saveOrUpdate(configuration);
	}

}
