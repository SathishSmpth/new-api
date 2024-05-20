package com.kamatchibotique.application.model.catalog.product.attribute;

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
@Table(name = "PRODUCT_OPTION_VALUE_DESCRIPTION", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "PRODUCT_OPTION_VALUE_ID",
                "LANGUAGE_ID"
        })
})
@TableGenerator(name = "description_gen", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "product_option_value_description_seq", allocationSize = SchemaConstant.DESCRIPTION_ID_ALLOCATION_SIZE, initialValue = SchemaConstant.DESCRIPTION_ID_START_VALUE)
public class ProductOptionValueDescription extends Description {
    private static final long serialVersionUID = 1L;

    @JsonIgnore
    @ManyToOne(targetEntity = ProductOptionValue.class)
    @JoinColumn(name = "PRODUCT_OPTION_VALUE_ID")
    private ProductOptionValue productOptionValue;
}
