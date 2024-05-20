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
@Table(name = "PRODUCT_OPTION_DESC",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"PRODUCT_OPTION_ID", "LANGUAGE_ID"})}
)
@TableGenerator(name = "description_gen", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "product_option_description_seq", allocationSize = SchemaConstant.DESCRIPTION_ID_ALLOCATION_SIZE, initialValue = SchemaConstant.DESCRIPTION_ID_START_VALUE)
public class ProductOptionDescription extends Description {
    private static final long serialVersionUID = 1L;

    @JsonIgnore
    @ManyToOne(targetEntity = ProductOption.class)
    @JoinColumn(name = "PRODUCT_OPTION_ID", nullable = false)
    private ProductOption productOption;

    @Column(name = "PRODUCT_OPTION_COMMENT", length = 4000)
    private String productOptionComment;
}
