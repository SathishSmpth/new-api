package com.kamatchibotique.application.model.order.attributes;

import com.kamatchibotique.application.model.order.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ORDER_ATTRIBUTE")
public class OrderAttribute {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ORDER_ATTRIBUTE_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "ORDER_ATTR_ID_NEXT_VALUE")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    @Column(name = "IDENTIFIER", nullable = false)
    private String key;

    @Column(name = "VALUE", nullable = false)
    private String value;

    @ManyToOne(targetEntity = Order.class)
    @JoinColumn(name = "ORDER_ID", nullable = false)
    private Order order;
}
