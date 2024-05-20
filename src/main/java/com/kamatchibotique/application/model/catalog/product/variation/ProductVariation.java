package com.kamatchibotique.application.model.catalog.product.variation;

import com.kamatchibotique.application.model.catalog.product.attribute.ProductOption;
import com.kamatchibotique.application.model.catalog.product.attribute.ProductOptionValue;
import com.kamatchibotique.application.model.common.Auditable;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PRODUCT_VARIATION", uniqueConstraints =
@UniqueConstraint(columnNames = {"MERCHANT_ID", "PRODUCT_OPTION_ID", "OPTION_VALUE_ID"}))
public class ProductVariation extends Auditable<String> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PRODUCT_VARIATION_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "PRODUCT_VARIN_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MERCHANT_ID", nullable = false)
    private MerchantStore merchantStore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_OPTION_ID", nullable = false)
    private ProductOption productOption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OPTION_VALUE_ID", nullable = false)
    private ProductOptionValue productOptionValue;

    @NotEmpty
    @Column(name = "CODE", length = 100, nullable = false)
    private String code;

    @Column(name = "SORT_ORDER")
    private Integer sortOrder;

    @Column(name = "VARIANT_DEFAULT")
    private boolean variantDefault = false;
}
