package com.kamatchibotique.application.model.system.optin;

import java.io.Serializable;
import java.util.Date;

import com.kamatchibotique.application.enums.system.OptinType;
import com.kamatchibotique.application.model.common.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;
import com.kamatchibotique.application.model.merchant.MerchantStore;


@Entity
@Table(name = "OPTIN",uniqueConstraints=
@UniqueConstraint(columnNames = {"MERCHANT_ID", "CODE"}))
public class Optin extends Auditable<String>  {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "OPTIN_ID")
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "OPTIN_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column (name ="START_DATE")
	private Date startDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column (name ="END_DATE")
	private Date endDate;
	
	@Column(name="TYPE", nullable=false)
	@Enumerated(value = EnumType.STRING)
	private OptinType optinType;
	
	@ManyToOne(targetEntity = MerchantStore.class)
	@JoinColumn(name="MERCHANT_ID")
	private MerchantStore merchant;
	
	@Column(name="CODE", nullable=false)
	private String code;
	
	@Column(name="DESCRIPTION")
	private String description;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;	
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public MerchantStore getMerchant() {
		return merchant;
	}

	public void setMerchant(MerchantStore merchant) {
		this.merchant = merchant;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public OptinType getOptinType() {
		return optinType;
	}

	public void setOptinType(OptinType optinType) {
		this.optinType = optinType;
	}

}
