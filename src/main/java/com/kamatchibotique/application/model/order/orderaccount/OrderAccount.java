package com.kamatchibotique.application.model.order.orderaccount;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.kamatchibotique.application.model.order.Order;
import com.kamatchibotique.application.utils.CloneUtils;
import jakarta.persistence.*;

@Entity
@Table(name = "ORDER_ACCOUNT")
public class OrderAccount {
private static final long serialVersionUID = -2429388347536330540L;

	@Id
	@Column(name = "ORDER_ACCOUNT_ID", unique = true, nullable = false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "ORDER_ACCOUNT_ID_NEXT_VALUE")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "ORDER_ID", nullable = false)
	private Order order;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "ORDER_ACCOUNT_START_DATE", nullable = false, length = 0)
	private Date orderAccountStartDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "ORDER_ACCOUNT_END_DATE", length = 0)
	private Date orderAccountEndDate;

	@Column(name = "ORDER_ACCOUNT_BILL_DAY", nullable = false)
	private Integer orderAccountBillDay;

	@OneToMany(mappedBy = "orderAccount", cascade = CascadeType.ALL)
	private Set<OrderAccountProduct> orderAccountProducts = new HashSet<OrderAccountProduct>();

	public OrderAccount() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Date getOrderAccountStartDate() {
		return CloneUtils.clone(orderAccountStartDate);
	}

	public void setOrderAccountStartDate(Date orderAccountStartDate) {
		this.orderAccountStartDate = CloneUtils.clone(orderAccountStartDate);
	}

	public Date getOrderAccountEndDate() {
		return CloneUtils.clone(orderAccountEndDate);
	}

	public void setOrderAccountEndDate(Date orderAccountEndDate) {
		this.orderAccountEndDate = CloneUtils.clone(orderAccountEndDate);
	}

	public Integer getOrderAccountBillDay() {
		return orderAccountBillDay;
	}

	public void setOrderAccountBillDay(Integer orderAccountBillDay) {
		this.orderAccountBillDay = orderAccountBillDay;
	}

	public Set<OrderAccountProduct> getOrderAccountProducts() {
		return orderAccountProducts;
	}

	public void setOrderAccountProducts(
			Set<OrderAccountProduct> orderAccountProducts) {
		this.orderAccountProducts = orderAccountProducts;
	}
}
