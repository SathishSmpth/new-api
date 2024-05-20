package com.kamatchibotique.application.model.catalog.product.type;

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
@Table(name = "PRODUCT_TYPE_DESCRIPTION",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"PRODUCT_TYPE_ID", "LANGUAGE_ID"})})
@TableGenerator(name = "description_gen", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "product_type_description_seq", allocationSize = SchemaConstant.DESCRIPTION_ID_ALLOCATION_SIZE, initialValue = SchemaConstant.DESCRIPTION_ID_START_VALUE)
public class ProductTypeDescription extends Description {

    private static final long serialVersionUID = 1L;

    @ManyToOne(targetEntity = ProductType.class)
    @JoinColumn(name = "PRODUCT_TYPE_ID", nullable = false)
    private ProductType productType;
}
