package com.kamatchibotique.application.model.catalog.product;

import com.kamatchibotique.application.enums.RentalStatus;
import com.kamatchibotique.application.enums.product.ProductCondition;
import com.kamatchibotique.application.model.catalog.category.Category;
import com.kamatchibotique.application.model.catalog.product.attribute.ProductAttribute;
import com.kamatchibotique.application.model.catalog.product.availability.ProductAvailability;
import com.kamatchibotique.application.model.catalog.product.description.ProductDescription;
import com.kamatchibotique.application.model.catalog.product.image.ProductImage;
import com.kamatchibotique.application.model.catalog.product.manufacturer.Manufacturer;
import com.kamatchibotique.application.model.catalog.product.relationship.ProductRelationship;
import com.kamatchibotique.application.model.catalog.product.type.ProductType;
import com.kamatchibotique.application.model.catalog.product.variant.ProductVariant;
import com.kamatchibotique.application.model.common.Auditable;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.tax.taxclass.TaxClass;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PRODUCT", uniqueConstraints =
@UniqueConstraint(columnNames = {"MERCHANT_ID", "SKU"}))
public class Product extends Auditable<String> {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PRODUCT_ID", unique = true, nullable = false)
    @TableGenerator(
            name = "TABLE_GEN",
            table = "SM_SEQUENCER",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT",
            pkColumnValue = "PRODUCT_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "product")
    private Set<ProductDescription> descriptions = new HashSet<ProductDescription>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "product")
    private Set<ProductAvailability> availabilities = new HashSet<ProductAvailability>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "product")
    private Set<ProductAttribute> attributes = new HashSet<ProductAttribute>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "product")
    private Set<ProductImage> images = new HashSet<ProductImage>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "product")
    private Set<ProductRelationship> relationships = new HashSet<ProductRelationship>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MERCHANT_ID", nullable = false)
    private MerchantStore merchantStore;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    @JoinTable(
            name = "PRODUCT_CATEGORY",
            joinColumns = @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "PRODUCT_ID"),
            inverseJoinColumns = @JoinColumn(name = "CATEGORY_ID", referencedColumnName = "CATEGORY_ID")
    )
    @Cascade({
            org.hibernate.annotations.CascadeType.DETACH,
            org.hibernate.annotations.CascadeType.LOCK,
            org.hibernate.annotations.CascadeType.REFRESH,
            org.hibernate.annotations.CascadeType.REPLICATE

    })
    private Set<Category> categories = new HashSet<Category>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "product")
    private Set<ProductVariant> variants = new HashSet<ProductVariant>();

    @Column(name = "DATE_AVAILABLE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateAvailable = new Date();


    @Column(name = "AVAILABLE")
    private boolean available = true;


    @Column(name = "PREORDER")
    private boolean preOrder = false;


    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "MANUFACTURER_ID", nullable = true)
    private Manufacturer manufacturer;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "PRODUCT_TYPE_ID", nullable = true)
    private ProductType type;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "TAX_CLASS_ID", nullable = true)
    private TaxClass taxClass;

    @Column(name = "PRODUCT_VIRTUAL")
    private boolean productVirtual = false;

    @Column(name = "PRODUCT_SHIP")
    private boolean productShipeable = false;

    @Column(name = "PRODUCT_FREE")
    private boolean productIsFree;

    @Column(name = "PRODUCT_LENGTH")
    private BigDecimal productLength;

    @Column(name = "PRODUCT_WIDTH")
    private BigDecimal productWidth;

    @Column(name = "PRODUCT_HEIGHT")
    private BigDecimal productHeight;

    @Column(name = "PRODUCT_WEIGHT")
    private BigDecimal productWeight;

    @Column(name = "REVIEW_AVG")
    private BigDecimal productReviewAvg;

    @Column(name = "REVIEW_COUNT")
    private Integer productReviewCount;

    @Column(name = "QUANTITY_ORDERED")
    private Integer productOrdered;

    @Column(name = "SORT_ORDER")
    private int sortOrder = 0;

    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9_]*$")
    @Column(name = "SKU")
    private String sku;

    @Column(name = "REF_SKU")
    private String refSku;

    @Column(name = "COND", nullable = true)
    private ProductCondition condition;

    @Column(name = "RENTAL_STATUS", nullable = true)
    private RentalStatus rentalStatus;


    @Column(name = "RENTAL_DURATION", nullable = true)
    private Integer rentalDuration;

    @Column(name = "RENTAL_PERIOD", nullable = true)
    private Integer rentalPeriod;

    public ProductDescription getProductDescription() {
        if (this.getDescriptions() != null && this.getDescriptions().size() > 0) {
            return this.getDescriptions().iterator().next();
        }
        return null;
    }

    public ProductImage getProductImage() {
        ProductImage productImage = null;
        if (this.getImages() != null && this.getImages().size() > 0) {
            for (ProductImage image : this.getImages()) {
                productImage = image;
                if (productImage.isDefaultImage()) {
                    break;
                }
            }
        }
        return productImage;
    }
}
