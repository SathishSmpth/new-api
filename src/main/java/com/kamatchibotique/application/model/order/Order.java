package com.kamatchibotique.application.model.order;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import javax.validation.Valid;

import com.kamatchibotique.application.enums.order.OrderChannel;
import com.kamatchibotique.application.enums.order.OrderType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kamatchibotique.application.model.common.Billing;
import com.kamatchibotique.application.model.common.Delivery;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.order.attributes.OrderAttribute;
import com.kamatchibotique.application.model.order.orderproduct.OrderProduct;
import com.kamatchibotique.application.enums.order.OrderStatus;
import com.kamatchibotique.application.model.order.orderstatus.OrderStatusHistory;
import com.kamatchibotique.application.model.order.payment.CreditCard;
import com.kamatchibotique.application.enums.payment.PaymentType;
import com.kamatchibotique.application.model.reference.currency.Currency;
import com.kamatchibotique.application.utils.CloneUtils;

@Entity
@Table (name="ORDERS")
public class Order  {
	private static final long serialVersionUID = 1L;

	@Id
	@Column (name ="ORDER_ID" , unique=true , nullable=false )
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT",
		pkColumnValue = "ORDER_ID_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	@Column (name ="ORDER_STATUS")
	@Enumerated(value = EnumType.STRING)
	private OrderStatus status;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column (name ="LAST_MODIFIED")
	private Date lastModified;
	
	//the customer object can be detached. An order can exist and the customer deleted
	@Column (name ="CUSTOMER_ID")
	private Long customerId;
	
	@Temporal(TemporalType.DATE)
	@Column (name ="DATE_PURCHASED")
	private Date datePurchased;
	
	//used for an order payable on multiple installment
	@Temporal(TemporalType.TIMESTAMP)
	@Column (name ="ORDER_DATE_FINISHED")
	private Date orderDateFinished;
	
	//What was the exchange rate
	@Column (name ="CURRENCY_VALUE")
	private BigDecimal currencyValue = new BigDecimal(1);//default 1-1
	
	@Column (name ="ORDER_TOTAL")
	private BigDecimal total;

	@Column (name ="IP_ADDRESS")
	private String ipAddress;
	
	@Column(name = "CART_CODE", nullable=true)
	private String shoppingCartCode;

	@Column (name ="CHANNEL")
	@Enumerated(value = EnumType.STRING)
	private OrderChannel channel;

	@Column (name ="ORDER_TYPE")
	@Enumerated(value = EnumType.STRING)
	private OrderType orderType = OrderType.ORDER;

	@Column (name ="PAYMENT_TYPE")
	@Enumerated(value = EnumType.STRING)
	private PaymentType paymentType;
	
	@Column (name ="PAYMENT_MODULE_CODE")
	private String paymentModuleCode;
	
	@Column (name ="SHIPPING_MODULE_CODE")
	private String shippingModuleCode;
	
	@Column(name = "CUSTOMER_AGREED")
	private Boolean customerAgreement = false;
	
	@Column(name = "CONFIRMED_ADDRESS")
	private Boolean confirmedAddress = false;

	@Embedded
	private Delivery delivery = null;
	
	@Valid
	@Embedded
	private Billing billing = null;
	
	@Embedded
	@Deprecated
	private CreditCard creditCard = null;

	
	@ManyToOne(targetEntity = Currency.class)
	@JoinColumn(name = "CURRENCY_ID")
	private Currency currency;

	@Column (name ="LOCALE")
	private Locale locale; 
	

	@JsonIgnore
	@ManyToOne(targetEntity = MerchantStore.class)
	@JoinColumn(name="MERCHANTID")
	private MerchantStore merchant;
	
	//@OneToMany(mappedBy = "order")
	//private Set<OrderAccount> orderAccounts = new HashSet<OrderAccount>();
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private Set<OrderProduct> orderProducts = new LinkedHashSet<OrderProduct>();
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private Set<OrderTotal> orderTotal = new LinkedHashSet<OrderTotal>();
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private Set<OrderStatusHistory> orderHistory = new LinkedHashSet<OrderStatusHistory>();
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private Set<OrderAttribute> orderAttributes = new LinkedHashSet<OrderAttribute>();
	
	public Order() {
	}
	
	@Column (name ="CUSTOMER_EMAIL_ADDRESS", length=50, nullable=false)
	private String customerEmailAddress;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public Date getLastModified() {
		return CloneUtils.clone(lastModified);
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = CloneUtils.clone(lastModified);
	}

	public Date getDatePurchased() {
		return CloneUtils.clone(datePurchased);
	}

	public void setDatePurchased(Date datePurchased) {
		this.datePurchased = CloneUtils.clone(datePurchased);
	}

	public Date getOrderDateFinished() {
		return CloneUtils.clone(orderDateFinished);
	}

	public void setOrderDateFinished(Date orderDateFinished) {
		this.orderDateFinished = CloneUtils.clone(orderDateFinished);
	}

	public BigDecimal getCurrencyValue() {
		return currencyValue;
	}

	public void setCurrencyValue(BigDecimal currencyValue) {
		this.currencyValue = currencyValue;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}


	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}


	public String getPaymentModuleCode() {
		return paymentModuleCode;
	}

	public void setPaymentModuleCode(String paymentModuleCode) {
		this.paymentModuleCode = paymentModuleCode;
	}



	public String getShippingModuleCode() {
		return shippingModuleCode;
	}

	public void setShippingModuleCode(String shippingModuleCode) {
		this.shippingModuleCode = shippingModuleCode;
	}



	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public MerchantStore getMerchant() {
		return merchant;
	}

	public void setMerchant(MerchantStore merchant) {
		this.merchant = merchant;
	}

	public Set<OrderProduct> getOrderProducts() {
		return orderProducts;
	}

	public void setOrderProducts(Set<OrderProduct> orderProducts) {
		this.orderProducts = orderProducts;
	}

	public Set<OrderTotal> getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal(Set<OrderTotal> orderTotal) {
		this.orderTotal = orderTotal;
	}

	public Set<OrderStatusHistory> getOrderHistory() {
		return orderHistory;
	}

	public void setOrderHistory(Set<OrderStatusHistory> orderHistory) {
		this.orderHistory = orderHistory;
	}


	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
	}

	public Delivery getDelivery() {
		return delivery;
	}

	public void setBilling(Billing billing) {
		this.billing = billing;
	}

	public Billing getBilling() {
		return billing;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	

	public String getCustomerEmailAddress() {
		return customerEmailAddress;
	}

	public void setCustomerEmailAddress(String customerEmailAddress) {
		this.customerEmailAddress = customerEmailAddress;
	}


	public void setChannel(OrderChannel channel) {
		this.channel = channel;
	}


	public OrderChannel getChannel() {
		return channel;
	}


	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}


	public CreditCard getCreditCard() {
		return creditCard;
	}


	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}


	public PaymentType getPaymentType() {
		return paymentType;
	}
	
	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}
	
	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
	public Boolean getCustomerAgreement() {
		return customerAgreement;
	}

	public void setCustomerAgreement(Boolean customerAgreement) {
		this.customerAgreement = customerAgreement;
	}

	public Boolean getConfirmedAddress() {
		return confirmedAddress;
	}

	public void setConfirmedAddress(Boolean confirmedAddress) {
		this.confirmedAddress = confirmedAddress;
	}
	
	public Set<OrderAttribute> getOrderAttributes() {
		return orderAttributes;
	}

	public void setOrderAttributes(Set<OrderAttribute> orderAttributes) {
		this.orderAttributes = orderAttributes;
	}
	
	public String getShoppingCartCode() {
		return shoppingCartCode;
	}

	public void setShoppingCartCode(String shoppingCartCode) {
		this.shoppingCartCode = shoppingCartCode;
	}

}