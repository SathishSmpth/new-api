package com.kamatchibotique.application.model.catalog.product.attribute;

import com.kamatchibotique.application.model.merchant.MerchantStore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

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
@Table(name = "PRODUCT_OPTION_VALUE",
        indexes = {@Index(name = "PRD_OPTION_VAL_CODE_IDX", columnList = "PRODUCT_OPTION_VAL_CODE")},
        uniqueConstraints =
        @UniqueConstraint(columnNames = {"MERCHANT_ID", "PRODUCT_OPTION_VAL_CODE"}))
public class ProductOptionValue {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PRODUCT_OPTION_VALUE_ID")
    @TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "PRODUCT_OPT_VAL_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    @Column(name = "PRODUCT_OPT_VAL_SORT_ORD")
    private Integer productOptionValueSortOrder;

    @Column(name = "PRODUCT_OPT_VAL_IMAGE")
    private String productOptionValueImage;

    @Column(name = "PRODUCT_OPT_FOR_DISP")
    private boolean productOptionDisplayOnly = false;

    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9_]*$")
    @Column(name = "PRODUCT_OPTION_VAL_CODE")
    private String code;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "productOptionValue")
    private Set<ProductOptionValueDescription> descriptions = new HashSet<ProductOptionValueDescription>();

    @Transient
    private MultipartFile image = null;

    @Transient
    private List<ProductOptionValueDescription> descriptionsList = new ArrayList<ProductOptionValueDescription>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MERCHANT_ID", nullable = false)
    private MerchantStore merchantStore;
}
