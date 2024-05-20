package com.kamatchibotique.application.model.shoppingcart;

import com.kamatchibotique.application.model.common.Auditable;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import jakarta.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "SHOPPING_CART", indexes = {@Index(name = "SHP_CART_CODE_IDX", columnList = "SHP_CART_CODE"), @Index(name = "SHP_CART_CUSTOMER_IDX", columnList = "CUSTOMER_ID")})
public class ShoppingCart extends Auditable<String> {


    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "SHP_CART_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "SHP_CRT_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    @Column(name = "SHP_CART_CODE", unique = true, nullable = false)
    private String shoppingCartCode;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "shoppingCart")
    private Set<ShoppingCartItem> lineItems = new HashSet<ShoppingCartItem>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MERCHANT_ID", nullable = false)
    private MerchantStore merchantStore;

    @Column(name = "CUSTOMER_ID", nullable = true)
    private Long customerId;

    @Column(name = "ORDER_ID", nullable = true)
    private Long orderId;

    @Column(name = "IP_ADDRESS")
    private String ipAddress;

    @Column(name = "PROMO_CODE")
    private String promoCode;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PROMO_ADDED")
    private Date promoAdded;

    @Transient
    private boolean obsolete = false;//when all items are obsolete

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;

    }

    public boolean isObsolete() {
        return obsolete;
    }

    public void setObsolete(boolean obsolete) {
        this.obsolete = obsolete;
    }

    public Set<ShoppingCartItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(Set<ShoppingCartItem> lineItems) {
        this.lineItems = lineItems;
    }

    public String getShoppingCartCode() {
        return shoppingCartCode;
    }

    public void setShoppingCartCode(String shoppingCartCode) {
        this.shoppingCartCode = shoppingCartCode;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setMerchantStore(MerchantStore merchantStore) {
        this.merchantStore = merchantStore;
    }

    public MerchantStore getMerchantStore() {
        return merchantStore;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }


    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public Date getPromoAdded() {
        return promoAdded;
    }

    public void setPromoAdded(Date promoAdded) {
        this.promoAdded = promoAdded;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
