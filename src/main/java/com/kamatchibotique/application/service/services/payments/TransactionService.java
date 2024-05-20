package com.kamatchibotique.application.service.services.payments;

import java.util.Date;
import java.util.List;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.service.services.common.CommonService;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.order.Order;
import com.kamatchibotique.application.model.payments.Transaction;




public interface TransactionService extends CommonService<Long, Transaction> {
	Transaction getCapturableTransaction(Order order) throws ServiceException;

	Transaction getRefundableTransaction(Order order) throws ServiceException;

	List<Transaction> listTransactions(Order order) throws ServiceException;
	
	List<Transaction> listTransactions(Date startDate, Date endDate) throws ServiceException;
	
	Transaction lastTransaction(Order order, MerchantStore store) throws ServiceException;
}