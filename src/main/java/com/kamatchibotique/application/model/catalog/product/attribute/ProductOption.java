package com.kamatchibotique.application.model.catalog.product.attribute;

import com.kamatchibotique.application.model.common.Auditable;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Table(name = "PRODUCT_OPTION",
        indexes = {@Index(name = "PRD_OPTION_CODE_IDX", columnList = "PRODUCT_OPTION_CODE")},
        uniqueConstraints = @UniqueConstraint(columnNames = {"MERCHANT_ID", "PRODUCT_OPTION_CODE"}))
public class ProductOption extends Auditable<String> {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PRODUCT_OPTION_ID")
    @TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "PRODUCT_OPTION_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    @Column(name = "PRODUCT_OPTION_SORT_ORD")
    private Integer productOptionSortOrder;

    @Column(name = "PRODUCT_OPTION_TYPE", length = 10)
    private String productOptionType;


    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "productOption")
    private Set<ProductOptionDescription> descriptions = new HashSet<ProductOptionDescription>();

    @Transient
    private List<ProductOptionDescription> descriptionsList = new ArrayList<ProductOptionDescription>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MERCHANT_ID", nullable = false)
    private MerchantStore merchantStore;

    @Column(name = "PRODUCT_OPTION_READ")
    private boolean readOnly;

    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9_]*$")
    @Column(name = "PRODUCT_OPTION_CODE")
    private String code;
}
