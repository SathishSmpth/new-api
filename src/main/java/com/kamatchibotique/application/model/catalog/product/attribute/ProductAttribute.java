package com.kamatchibotique.application.model.catalog.product.attribute;

import com.kamatchibotique.application.model.catalog.product.Product;
import com.kamatchibotique.application.model.common.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PRODUCT_ATTRIBUTE",
        indexes = @Index(columnList = "PRODUCT_ID"),
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {
                        "OPTION_ID",
                        "OPTION_VALUE_ID",
                        "PRODUCT_ID"
                })
        }
)
public class ProductAttribute extends Auditable<String> {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PRODUCT_ATTRIBUTE_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "PRODUCT_ATTR_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    @Column(name = "PRODUCT_ATRIBUTE_PRICE")
    private BigDecimal productAttributePrice;

    @Column(name = "PRODUCT_ATTRIBUTE_SORT_ORD")
    private Integer productOptionSortOrder;

    @Column(name = "PRODUCT_ATTRIBUTE_FREE")
    private boolean productAttributeIsFree;

    @Column(name = "PRODUCT_ATTRIBUTE_WEIGHT")
    private BigDecimal productAttributeWeight;

    @Column(name = "PRODUCT_ATTRIBUTE_DEFAULT")
    private boolean attributeDefault = false;

    @Column(name = "PRODUCT_ATTRIBUTE_REQUIRED")
    private boolean attributeRequired = false;

    @Column(name = "PRODUCT_ATTRIBUTE_FOR_DISP")
    private boolean attributeDisplayOnly = false;

    @Column(name = "PRODUCT_ATTRIBUTE_DISCOUNTED")
    private boolean attributeDiscounted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OPTION_ID", nullable = false)
    private ProductOption productOption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OPTION_VALUE_ID", nullable = false)
    private ProductOptionValue productOptionValue;

    @Transient
    private String attributePrice = "0";

    @Transient
    private String attributeSortOrder = "0";


    @ManyToOne(targetEntity = Product.class)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private Product product;
}
