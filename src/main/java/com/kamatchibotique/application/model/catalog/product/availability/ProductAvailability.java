package com.kamatchibotique.application.model.catalog.product.availability;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kamatchibotique.application.constants.SchemaConstant;
import com.kamatchibotique.application.model.catalog.product.Product;
import com.kamatchibotique.application.model.catalog.product.ProductDimensions;
import com.kamatchibotique.application.model.catalog.product.price.ProductPrice;
import com.kamatchibotique.application.model.catalog.product.variant.ProductVariant;
import com.kamatchibotique.application.model.common.Auditable;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.utils.CloneUtils;
import jakarta.persistence.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "PRODUCT_AVAILABILITY",
        uniqueConstraints = @UniqueConstraint(columnNames = {"MERCHANT_ID", "PRODUCT_ID", "PRODUCT_VARIANT", "REGION_VARIANT"}),
        indexes =
                {
                        @Index(name = "PRD_AVAIL_STORE_PRD_IDX", columnList = "PRODUCT_ID,MERCHANT_ID"),
                        @Index(name = "PRD_AVAIL_PRD_IDX", columnList = "PRODUCT_ID")
                }
)
public class ProductAvailability extends Auditable<String> {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PRODUCT_AVAIL_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "PRODUCT_AVAIL_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    @JsonIgnore
    @ManyToOne(targetEntity = Product.class)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MERCHANT_ID", nullable = true)
    private MerchantStore merchantStore;

    @ManyToOne(targetEntity = ProductVariant.class)
    @JoinColumn(name = "PRODUCT_VARIANT", nullable = true)
    private ProductVariant productVariant;

    @Pattern(regexp = "^[a-zA-Z0-9_]*$")
    @Column(name = "SKU", nullable = true)
    private String sku;

    @Embedded
    private ProductDimensions dimensions;

    @NotNull
    @Column(name = "QUANTITY")
    private Integer productQuantity = 0;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_AVAILABLE")
    private Date productDateAvailable;

    @Column(name = "REGION")
    private String region = SchemaConstant.ALL_REGIONS;

    @Column(name = "REGION_VARIANT")
    private String regionVariant;

    @Column(name = "OWNER")
    private String owner;

    @Column(name = "STATUS")
    private boolean productStatus = true;

    @Column(name = "FREE_SHIPPING")
    private boolean productIsAlwaysFreeShipping;

    @Column(name = "AVAILABLE")
    private Boolean available;

    @Column(name = "QUANTITY_ORD_MIN")
    private Integer productQuantityOrderMin = 0;

    @Column(name = "QUANTITY_ORD_MAX")
    private Integer productQuantityOrderMax = 0;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "productAvailability", cascade = CascadeType.ALL)
    private Set<ProductPrice> prices = new HashSet<ProductPrice>();


    @Transient
    public ProductPrice defaultPrice() {
        for (ProductPrice price : prices) {
            if (price.isDefaultPrice()) {
                return price;
            }
        }
        return new ProductPrice();
    }

    public ProductAvailability() {
    }

    public ProductAvailability(Product product, MerchantStore store) {
        this.product = product;
        this.merchantStore = store;
    }

    public Integer getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }

    public Date getProductDateAvailable() {
        return CloneUtils.clone(productDateAvailable);
    }

    public void setProductDateAvailable(Date productDateAvailable) {
        this.productDateAvailable = CloneUtils.clone(productDateAvailable);
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegionVariant() {
        return regionVariant;
    }

    public void setRegionVariant(String regionVariant) {
        this.regionVariant = regionVariant;
    }

    public boolean getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(boolean productStatus) {
        this.productStatus = productStatus;
    }

    public boolean getProductIsAlwaysFreeShipping() {
        return productIsAlwaysFreeShipping;
    }

    public void setProductIsAlwaysFreeShipping(boolean productIsAlwaysFreeShipping) {
        this.productIsAlwaysFreeShipping = productIsAlwaysFreeShipping;
    }

    public Integer getProductQuantityOrderMin() {
        return productQuantityOrderMin;
    }

    public void setProductQuantityOrderMin(Integer productQuantityOrderMin) {
        this.productQuantityOrderMin = productQuantityOrderMin;
    }

    public Integer getProductQuantityOrderMax() {
        return productQuantityOrderMax;
    }

    public void setProductQuantityOrderMax(Integer productQuantityOrderMax) {
        this.productQuantityOrderMax = productQuantityOrderMax;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Set<ProductPrice> getPrices() {
        return prices;
    }

    public void setPrices(Set<ProductPrice> prices) {
        this.prices = prices;
    }

    public MerchantStore getMerchantStore() {
        return merchantStore;
    }

    public void setMerchantStore(MerchantStore merchantStore) {
        this.merchantStore = merchantStore;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public ProductDimensions getDimensions() {
        return dimensions;
    }

    public void setDimensions(ProductDimensions dimensions) {
        this.dimensions = dimensions;
    }

    public ProductVariant getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(ProductVariant productVariant) {
        this.productVariant = productVariant;
    }
}
