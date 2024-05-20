package com.kamatchibotique.application.model.order.orderproduct;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kamatchibotique.application.model.order.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ORDER_PRODUCT")
public class OrderProduct {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ORDER_PRODUCT_ID")
    @TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "ORDER_PRODUCT_ID_NEXT_VALUE")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    @Column(name = "PRODUCT_SKU")
    private String sku;

    @Column(name = "PRODUCT_NAME", length = 64, nullable = false)
    private String productName;

    @Column(name = "PRODUCT_QUANTITY")
    private int productQuantity;

    @Column(name = "ONETIME_CHARGE", nullable = false)
    private BigDecimal oneTimeCharge;

    @JsonIgnore
    @ManyToOne(targetEntity = Order.class)
    @JoinColumn(name = "ORDER_ID", nullable = false)
    private Order order;

    @OneToMany(mappedBy = "orderProduct", cascade = CascadeType.ALL)
    private Set<OrderProductAttribute> orderAttributes = new HashSet<OrderProductAttribute>();

    @OneToMany(mappedBy = "orderProduct", cascade = CascadeType.ALL)
    private Set<OrderProductPrice> prices = new HashSet<OrderProductPrice>();

    @OneToMany(mappedBy = "orderProduct", cascade = CascadeType.ALL)
    private Set<OrderProductDownload> downloads = new HashSet<OrderProductDownload>();
}
