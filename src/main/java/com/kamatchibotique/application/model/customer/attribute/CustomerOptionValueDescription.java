package com.kamatchibotique.application.model.customer.attribute;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kamatchibotique.application.constants.SchemaConstant;
import com.kamatchibotique.application.model.common.description.Description;
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
@Table(name = "CUSTOMER_OPT_VAL_DESCRIPTION", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "CUSTOMER_OPT_VAL_ID",
                "LANGUAGE_ID"
        })
})
@TableGenerator(name = "description_gen", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "customer_option_value_description_seq", allocationSize = SchemaConstant.DESCRIPTION_ID_ALLOCATION_SIZE, initialValue = SchemaConstant.DESCRIPTION_ID_START_VALUE)
public class CustomerOptionValueDescription extends Description {
    private static final long serialVersionUID = 1L;

    @JsonIgnore
    @ManyToOne(targetEntity = CustomerOptionValue.class)
    @JoinColumn(name = "CUSTOMER_OPT_VAL_ID")
    private CustomerOptionValue customerOptionValue;
}
