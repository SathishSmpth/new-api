package com.kamatchibotique.application.model.order.orderproduct;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table (name="ORDER_PRODUCT_DOWNLOAD")
public class OrderProductDownload {
	private static final long serialVersionUID = -8935511990745477240L;
	
	public final static int DEFAULT_DOWNLOAD_MAX_DAYS = 31;
	
	@Id
	@Column (name="ORDER_PRODUCT_DOWNLOAD_ID")
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "ORDER_PRODUCT_DL_ID_NEXT_VALUE")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "ORDER_PRODUCT_ID", nullable = false)
	private OrderProduct orderProduct; 

	@Column(name = "ORDER_PRODUCT_FILENAME", nullable = false)
	private String orderProductFilename;
	
	@Column(name = "DOWNLOAD_MAXDAYS", nullable = false)
	private Integer maxdays = DEFAULT_DOWNLOAD_MAX_DAYS;
	
	@Column(name = "DOWNLOAD_COUNT", nullable = false)
	private Integer downloadCount;
	

	
	public OrderProductDownload() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public OrderProduct getOrderProduct() {
		return orderProduct;
	}

	public void setOrderProduct(OrderProduct orderProduct) {
		this.orderProduct = orderProduct;
	}

	public String getOrderProductFilename() {
		return orderProductFilename;
	}

	public void setOrderProductFilename(String orderProductFilename) {
		this.orderProductFilename = orderProductFilename;
	}

	public Integer getMaxdays() {
		return maxdays;
	}

	public void setMaxdays(Integer maxdays) {
		this.maxdays = maxdays;
	}

	public Integer getDownloadCount() {
		return downloadCount;
	}

	public void setDownloadCount(Integer downloadCount) {
		this.downloadCount = downloadCount;
	}


}