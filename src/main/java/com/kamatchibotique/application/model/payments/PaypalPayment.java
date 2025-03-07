package com.kamatchibotique.application.model.payments;

import com.kamatchibotique.application.enums.payment.PaymentType;

/**
 * When the user performs a payment using paypal
 * @author Carl Samson
 *
 */
public class PaypalPayment extends Payment {
	
	//express checkout
	private String payerId;
	private String paymentToken;
	
	public PaypalPayment() {
		super.setPaymentType(PaymentType.PAYPAL);
	}
	
	public void setPayerId(String payerId) {
		this.payerId = payerId;
	}
	public String getPayerId() {
		return payerId;
	}
	public void setPaymentToken(String paymentToken) {
		this.paymentToken = paymentToken;
	}
	public String getPaymentToken() {
		return paymentToken;
	}

}
