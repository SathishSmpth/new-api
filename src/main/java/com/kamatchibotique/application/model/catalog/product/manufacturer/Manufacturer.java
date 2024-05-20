package com.kamatchibotique.application.model.catalog.product.manufacturer;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotEmpty;

import com.kamatchibotique.application.model.common.Auditable;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "MANUFACTURER", uniqueConstraints=
@UniqueConstraint(columnNames = {"MERCHANT_ID", "CODE"}) )
public class Manufacturer extends Auditable<String> {
	private static final long serialVersionUID = 1L;
	
	public static final String DEFAULT_MANUFACTURER = "DEFAULT";
	
	@Id
	@Column(name = "MANUFACTURER_ID", unique=true, nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "MANUFACT_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	@OneToMany(mappedBy = "manufacturer", cascade = CascadeType.ALL , fetch = FetchType.EAGER)
	private Set<ManufacturerDescription> descriptions = new HashSet<ManufacturerDescription>();
	
	@Column(name = "MANUFACTURER_IMAGE")
	private String image;
	
	@Column(name="SORT_ORDER")
	private int order = 0;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="MERCHANT_ID", nullable=false)
	private MerchantStore merchantStore;
	
	@NotEmpty
	@Column(name="CODE", length=100, nullable=false)
	private String code;
}
