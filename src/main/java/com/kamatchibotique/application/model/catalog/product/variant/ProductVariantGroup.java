package com.kamatchibotique.application.model.catalog.product.variant;

import com.kamatchibotique.application.model.common.Auditable;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "PRODUCT_VARIANT_GROUP")
public class ProductVariantGroup extends Auditable<String> {

    private static final long serialVersionUID = 1L;


    @Id
    @Column(name = "PRODUCT_VARIANT_GROUP_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN",
            table = "SM_SEQUENCER",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT", pkColumnValue = "PRODUCT_VAR_GROUP_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "productVariantGroup")
    private List<ProductVariantImage> images = new ArrayList<ProductVariantImage>();

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH}, mappedBy = "productVariantGroup")
    private Set<ProductVariant> productVariants = new HashSet<ProductVariant>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MERCHANT_ID", nullable = false)
    private MerchantStore merchantStore;
}
