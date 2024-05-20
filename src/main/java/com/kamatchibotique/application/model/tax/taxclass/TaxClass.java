package com.kamatchibotique.application.model.tax.taxclass;

import com.kamatchibotique.application.model.catalog.product.Product;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.tax.taxrate.TaxRate;
import jakarta.persistence.*;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TAX_CLASS",
        indexes = {@Index(name = "TAX_CLASS_CODE_IDX", columnList = "TAX_CLASS_CODE")},
        uniqueConstraints =
        @UniqueConstraint(columnNames = {"MERCHANT_ID", "TAX_CLASS_CODE"}))
public class TaxClass {
    private static final long serialVersionUID = 1L;

    public final static String DEFAULT_TAX_CLASS = "DEFAULT";

    public TaxClass(String code) {
        this.code = code;
        this.title = code;
    }

    @Id
    @Column(name = "TAX_CLASS_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "TX_CLASS_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    @NotEmpty
    @Column(name = "TAX_CLASS_CODE", nullable = false, length = 10)
    private String code;

    @NotEmpty
    @Column(name = "TAX_CLASS_TITLE", nullable = false, length = 32)
    private String title;


    @OneToMany(mappedBy = "taxClass", targetEntity = Product.class)
    private List<Product> products = new ArrayList<Product>();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MERCHANT_ID", nullable = true)
    private MerchantStore merchantStore;


    @OneToMany(mappedBy = "taxClass")
    private List<TaxRate> taxRates = new ArrayList<TaxRate>();

    public TaxClass() {
        super();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<TaxRate> getTaxRates() {
        return taxRates;
    }

    public void setTaxRates(List<TaxRate> taxRates) {
        this.taxRates = taxRates;
    }


    public MerchantStore getMerchantStore() {
        return merchantStore;
    }

    public void setMerchantStore(MerchantStore merchantStore) {
        this.merchantStore = merchantStore;
    }

}
