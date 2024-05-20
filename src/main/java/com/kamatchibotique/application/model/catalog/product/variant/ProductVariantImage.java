package com.kamatchibotique.application.model.catalog.product.variant;

import com.kamatchibotique.application.model.common.Auditable;
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
@Table(name = "PRODUCT_VAR_IMAGE")
public class ProductVariantImage extends Auditable<String> {


    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PRODUCT_VAR_IMAGE_ID")
    @TableGenerator(name = "TABLE_GEN",
            table = "SM_SEQUENCER",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT",
            pkColumnValue = "PRD_VAR_IMG_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    @Column(name = "PRODUCT_IMAGE")
    private String productImage;

    @Column(name = "DEFAULT_IMAGE")
    private boolean defaultImage = true;

    @ManyToOne(targetEntity = ProductVariantGroup.class)
    @JoinColumn(name = "PRODUCT_VARIANT_GROUP_ID", nullable = false)
    private ProductVariantGroup productVariantGroup;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "productVariantImage", cascade = CascadeType.ALL)
    private Set<ProductVariantImageDescription> descriptions = new HashSet<ProductVariantImageDescription>();
}
