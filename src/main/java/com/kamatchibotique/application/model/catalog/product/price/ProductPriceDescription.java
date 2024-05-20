package com.kamatchibotique.application.model.catalog.product.price;

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
@Table(name = "PRODUCT_PRICE_DESCRIPTION",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {
                        "PRODUCT_PRICE_ID",
                        "LANGUAGE_ID"
                })
        })
@TableGenerator(name = "description_gen", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "product_price_description_seq", allocationSize = SchemaConstant.DESCRIPTION_ID_ALLOCATION_SIZE, initialValue = SchemaConstant.DESCRIPTION_ID_START_VALUE)
public class ProductPriceDescription extends Description {
    private static final long serialVersionUID = 1L;

    public final static String DEFAULT_PRICE_DESCRIPTION = "DEFAULT";

    @JsonIgnore
    @ManyToOne(targetEntity = ProductPrice.class)
    @JoinColumn(name = "PRODUCT_PRICE_ID", nullable = false)
    private ProductPrice productPrice;


    @Column(name = "PRICE_APPENDER")
    private String priceAppender;
}
