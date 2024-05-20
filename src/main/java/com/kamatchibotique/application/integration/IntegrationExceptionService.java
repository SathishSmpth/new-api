package com.kamatchibotique.application.integration;

import java.util.List;

import com.kamatchibotique.application.exception.ServiceException;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IntegrationExceptionService extends ServiceException {
	private static final long serialVersionUID = 1L;
	
	public static final int ERROR_VALIDATION_SAVE = 100;
	public static final int TRANSACTION_EXCEPTION = 99;
	
	private List<String> errorFields;
	
	private int errorCode = 0;

	public IntegrationExceptionService(Exception e) {
		super(e);
	}
	
	public IntegrationExceptionService(String message, Exception e) {
		super(message,e);
	}
	
	public IntegrationExceptionService(int code, String message) {
		
		super(message);
		this.errorCode = code;
	}
	
	public IntegrationExceptionService(int code) {
		
		this.errorCode = code;
	}

	public IntegrationExceptionService(String message) {
		super(message);
	}
}
