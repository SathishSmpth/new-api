package com.kamatchibotique.application.email;

public interface EmailModule {
  
  void send(final Email email) throws Exception;

  void setEmailConfig(EmailConfig emailConfig);

}
