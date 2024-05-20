package com.kamatchibotique.application.model.customer.attribute;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kamatchibotique.application.model.customer.Customer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CUSTOMER_ATTRIBUTE",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {
                        "OPTION_ID",
                        "CUSTOMER_ID"
                })
        }
)
public class CustomerAttribute {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "CUSTOMER_ATTRIBUTE_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "CUST_ATTR_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OPTION_ID", nullable = false)
    private CustomerOption customerOption;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OPTION_VALUE_ID", nullable = false)
    private CustomerOptionValue customerOptionValue;

    @Column(name = "CUSTOMER_ATTR_TXT_VAL")
    private String textValue;

    @JsonIgnore
    @ManyToOne(targetEntity = Customer.class)
    @JoinColumn(name = "CUSTOMER_ID", nullable = false)
    private Customer customer;
}
