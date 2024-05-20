package com.kamatchibotique.application.model.catalog.catalog;

import com.kamatchibotique.application.constants.SchemaConstant;
import com.kamatchibotique.application.model.catalog.category.Category;
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
@Table(name = "CATALOG_ENTRY", uniqueConstraints =
@UniqueConstraint(columnNames = {"CATEGORY_ID", "CATALOG_ID"}))
public class CatalogCategoryEntry extends Auditable<String> {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE,
            generator = "TABLE_GEN")

    @TableGenerator(name = "TABLE_GEN",
            table = "SM_SEQUENCER",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT",
            allocationSize = SchemaConstant.DESCRIPTION_ID_ALLOCATION_SIZE,
            initialValue = SchemaConstant.DESCRIPTION_ID_START_VALUE,
            pkColumnValue = "CATALOG_ENT_SEQ_NEXT_VAL")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID", nullable = false)
    Category category;

    @ManyToOne
    @JoinColumn(name = "CATALOG_ID", nullable = false)
    private Catalog catalog;

    @Column(name = "VISIBLE")
    private boolean visible;
}
