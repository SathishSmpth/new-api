package com.kamatchibotique.application.model.customer.attribute;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CUSTOMER_OPTION_VALUE", indexes = {@Index(name = "CUST_OPT_VAL_CODE_IDX", columnList = "CUSTOMER_OPT_VAL_CODE")}, uniqueConstraints =
@UniqueConstraint(columnNames = {"MERCHANT_ID", "CUSTOMER_OPT_VAL_CODE"}))
public class CustomerOptionValue {
    private static final long serialVersionUID = 3736085877929910891L;

    @Id
    @Column(name = "CUSTOMER_OPTION_VALUE_ID")
    @TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "CUSTOMER_OPT_VAL_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    @Column(name = "SORT_ORDER")
    private Integer sortOrder = 0;

    @Column(name = "CUSTOMER_OPT_VAL_IMAGE")
    private String customerOptionValueImage;

    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9_]*$")
    @Column(name = "CUSTOMER_OPT_VAL_CODE")
    private String code;


    @Valid
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "customerOptionValue")
    private Set<CustomerOptionValueDescription> descriptions = new HashSet<CustomerOptionValueDescription>();

    @Transient
    private List<CustomerOptionValueDescription> descriptionsList = new ArrayList<CustomerOptionValueDescription>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MERCHANT_ID", nullable = false)
    private MerchantStore merchantStore;
}
