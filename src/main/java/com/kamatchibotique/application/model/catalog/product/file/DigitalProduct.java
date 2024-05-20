package com.kamatchibotique.application.model.catalog.product.file;


import com.kamatchibotique.application.model.catalog.product.Product;
import com.kamatchibotique.application.model.common.Auditable;
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
@Table(name = "PRODUCT_DIGITAL", uniqueConstraints =
@UniqueConstraint(columnNames = {"PRODUCT_ID", "FILE_NAME"}))
public class DigitalProduct extends Auditable<String> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PRODUCT_DIGITAL_ID")
    @TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "PRODUCT_DGT_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;


    @ManyToOne(targetEntity = Product.class)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private Product product;


    @Column(name = "FILE_NAME", nullable = false)
    private String productFileName;
}
