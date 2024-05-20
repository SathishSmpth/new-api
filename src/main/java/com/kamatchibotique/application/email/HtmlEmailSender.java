package com.kamatchibotique.application.email;


public interface HtmlEmailSender {
	
	void send(final Email email) throws Exception;

	void setEmailConfig(EmailConfig emailConfig);

}
