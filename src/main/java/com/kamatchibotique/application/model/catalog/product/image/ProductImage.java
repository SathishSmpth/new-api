package com.kamatchibotique.application.model.catalog.product.image;

import com.kamatchibotique.application.model.catalog.product.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PRODUCT_IMAGE")
public class ProductImage {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PRODUCT_IMAGE_ID")
    @TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "PRODUCT_IMG_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "productImage", cascade = CascadeType.ALL)
    private List<ProductImageDescription> descriptions = new ArrayList<ProductImageDescription>();

    @Column(name = "PRODUCT_IMAGE")
    private String productImage;

    @Column(name = "DEFAULT_IMAGE")
    private boolean defaultImage = true;

    @Column(name = "IMAGE_TYPE")
    private int imageType;

    @Column(name = "PRODUCT_IMAGE_URL")
    private String productImageUrl;


    @Column(name = "IMAGE_CROP")
    private boolean imageCrop;

    @ManyToOne(targetEntity = Product.class)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private Product product;

    @Column(name = "SORT_ORDER")
    private Integer sortOrder = 0;

    @Transient
    private InputStream image = null;
}
