package com.kamatchibotique.application.model.catalog.product.attribute;

import com.kamatchibotique.application.model.catalog.product.type.ProductType;
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
@Table(name = "PRODUCT_OPTION_SET",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {
                        "MERCHANT_ID",
                        "PRODUCT_OPTION_SET_CODE"
                })
        }
)
public class ProductOptionSet {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PRODUCT_OPTION_SET_ID")
    @TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "PRODUCT_OPT_SET_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9_]*$")
    @Column(name = "PRODUCT_OPTION_SET_CODE")
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_OPTION_ID", nullable = false)
    private ProductOption option;

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = ProductOptionValue.class)
    @JoinTable(name = "PRODUCT_OPT_SET_OPT_VALUE")
    private List<ProductOptionValue> values = new ArrayList<ProductOptionValue>();

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = ProductType.class)
    @JoinTable(name = "PRODUCT_OPT_SET_PRD_TYPE")
    private Set<ProductType> productTypes = new HashSet<ProductType>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MERCHANT_ID", nullable = false)
    private MerchantStore store;

    @Column(name = "PRODUCT_OPTION_SET_DISP")
    private boolean optionDisplayOnly = false;
}
