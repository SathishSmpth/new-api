package com.kamatchibotique.application.model.catalog.product.relationship;

import com.kamatchibotique.application.model.catalog.product.Product;
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
@Table(name = "PRODUCT_RELATIONSHIP")
public class ProductRelationship {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PRODUCT_RELATIONSHIP_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "PRODUCT_RELATION_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    @ManyToOne(targetEntity = MerchantStore.class)
    @JoinColumn(name = "MERCHANT_ID", nullable = false)
    private MerchantStore store;

    @ManyToOne(targetEntity = Product.class)
    @JoinColumn(name = "PRODUCT_ID", updatable = false, nullable = true)
    private Product product = null;

    @ManyToOne(targetEntity = Product.class)
    @JoinColumn(name = "RELATED_PRODUCT_ID", updatable = false, nullable = true)
    private Product relatedProduct = null;

    @Column(name = "CODE")
    private String code;

    @Column(name = "ACTIVE")
    private boolean active = true;
}
