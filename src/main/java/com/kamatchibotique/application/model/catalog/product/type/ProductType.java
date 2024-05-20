package com.kamatchibotique.application.model.catalog.product.type;

import com.kamatchibotique.application.model.common.Auditable;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PRODUCT_TYPE")
public class ProductType extends Auditable<String> {
    private static final long serialVersionUID = 1L;

    public final static String GENERAL_TYPE = "GENERAL";

    @Id
    @Column(name = "PRODUCT_TYPE_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT", pkColumnValue = "PRD_TYPE_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "productType")
    private Set<ProductTypeDescription> descriptions = new HashSet<ProductTypeDescription>();

    @Column(name = "PRD_TYPE_CODE")
    private String code;

    @Column(name = "PRD_TYPE_ADD_TO_CART")
    private Boolean allowAddToCart;

    @Column(name = "PRD_TYPE_VISIBLE")
    private Boolean visible;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MERCHANT_ID", nullable = true)
    private MerchantStore merchantStore;
}
