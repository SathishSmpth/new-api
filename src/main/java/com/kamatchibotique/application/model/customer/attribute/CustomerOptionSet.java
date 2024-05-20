package com.kamatchibotique.application.model.customer.attribute;

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
@Table(name = "CUSTOMER_OPTION_SET",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {
                        "CUSTOMER_OPTION_ID",
                        "CUSTOMER_OPTION_VALUE_ID"
                })
        }
)
public class CustomerOptionSet {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "CUSTOMER_OPTIONSET_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "CUST_OPTSET_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_OPTION_ID", nullable = false)
    private CustomerOption customerOption = null;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_OPTION_VALUE_ID", nullable = false)
    private CustomerOptionValue customerOptionValue = null;

    @Column(name = "SORT_ORDER")
    private int sortOrder = 0;
}
