package com.kamatchibotique.application.model.customer.attribute;

import com.kamatchibotique.application.model.merchant.MerchantStore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CUSTOMER_OPTION", indexes = {@Index(name = "CUST_OPT_CODE_IDX", columnList = "CUSTOMER_OPT_CODE")}, uniqueConstraints =
@UniqueConstraint(columnNames = {"MERCHANT_ID", "CUSTOMER_OPT_CODE"}))
public class CustomerOption {
    private static final long serialVersionUID = -2019269055342226086L;

    @Id
    @Column(name = "CUSTOMER_OPTION_ID")
    @TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "CUSTOMER_OPTION_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    @Column(name = "SORT_ORDER")
    private Integer sortOrder = 0;

    @Column(name = "CUSTOMER_OPTION_TYPE", length = 10)
    private String customerOptionType;

    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9_]*$")
    @Column(name = "CUSTOMER_OPT_CODE")
    //@Index(name="CUST_OPT_CODE_IDX")
    private String code;

    @Column(name = "CUSTOMER_OPT_ACTIVE")
    private boolean active;

    @Column(name = "CUSTOMER_OPT_PUBLIC")
    private boolean publicOption;

    @Valid
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "customerOption")
    private Set<CustomerOptionDescription> descriptions = new HashSet<CustomerOptionDescription>();

    @Transient
    private List<CustomerOptionDescription> descriptionsList = new ArrayList<CustomerOptionDescription>();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MERCHANT_ID", nullable = false)
    private MerchantStore merchantStore;
}
