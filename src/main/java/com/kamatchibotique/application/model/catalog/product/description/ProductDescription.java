package com.kamatchibotique.application.model.catalog.product.description;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kamatchibotique.application.constants.SchemaConstant;
import com.kamatchibotique.application.model.catalog.product.Product;
import com.kamatchibotique.application.model.common.description.Description;
import jakarta.persistence.*;

@Entity
@Table(name = "PRODUCT_DESCRIPTION",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"PRODUCT_ID", "LANGUAGE_ID"})},
        indexes = {@Index(name = "PRODUCT_DESCRIPTION_SEF_URL", columnList = "SEF_URL")})

@TableGenerator(name = "description_gen", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "product_description_seq", allocationSize = SchemaConstant.DESCRIPTION_ID_ALLOCATION_SIZE, initialValue = SchemaConstant.DESCRIPTION_ID_START_VALUE)
public class ProductDescription extends Description {
    private static final long serialVersionUID = 1L;

    @JsonIgnore
    @ManyToOne(targetEntity = Product.class)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private Product product;

    @Column(name = "PRODUCT_HIGHLIGHT")
    private String productHighlight;

    @Column(name = "DOWNLOAD_LNK")
    private String productExternalDl;

    @Column(name = "SEF_URL")
    private String seUrl;

    @Column(name = "META_TITLE")
    private String metatagTitle;

    @Column(name = "META_KEYWORDS")
    private String metatagKeywords;

    @Column(name = "META_DESCRIPTION")
    private String metatagDescription;

    public ProductDescription() {
    }

    public String getProductHighlight() {
        return productHighlight;
    }

    public void setProductHighlight(String productHighlight) {
        this.productHighlight = productHighlight;
    }

    public String getProductExternalDl() {
        return productExternalDl;
    }

    public void setProductExternalDl(String productExternalDl) {
        this.productExternalDl = productExternalDl;
    }

    public String getSeUrl() {
        return seUrl;
    }

    public void setSeUrl(String seUrl) {
        this.seUrl = seUrl;
    }

    public String getMetatagTitle() {
        return metatagTitle;
    }

    public void setMetatagTitle(String metatagTitle) {
        this.metatagTitle = metatagTitle;
    }

    public String getMetatagKeywords() {
        return metatagKeywords;
    }

    public void setMetatagKeywords(String metatagKeywords) {
        this.metatagKeywords = metatagKeywords;
    }

    public String getMetatagDescription() {
        return metatagDescription;
    }

    public void setMetatagDescription(String metatagDescription) {
        this.metatagDescription = metatagDescription;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
