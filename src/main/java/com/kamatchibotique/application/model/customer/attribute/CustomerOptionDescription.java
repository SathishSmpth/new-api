package com.kamatchibotique.application.model.customer.attribute;

import com.kamatchibotique.application.constants.SchemaConstant;
import com.kamatchibotique.application.model.common.description.Description;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CUSTOMER_OPTION_DESC", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "CUSTOMER_OPTION_ID",
                "LANGUAGE_ID"
        })
})
@TableGenerator(name = "description_gen", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "customer_option_description_seq", allocationSize = SchemaConstant.DESCRIPTION_ID_ALLOCATION_SIZE, initialValue = SchemaConstant.DESCRIPTION_ID_START_VALUE)
public class CustomerOptionDescription extends Description {
    private static final long serialVersionUID = 1L;

    @ManyToOne(targetEntity = CustomerOption.class)
    @JoinColumn(name = "CUSTOMER_OPTION_ID", nullable = false)
    private CustomerOption customerOption;

    @Column(name = "CUSTOMER_OPTION_COMMENT", length = 4000)
    private String customerOptionComment;
}
