package com.kamatchibotique.application.model.catalog.catalog;


import com.kamatchibotique.application.constants.SchemaConstant;
import com.kamatchibotique.application.model.common.Auditable;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CATALOG",
        uniqueConstraints = @UniqueConstraint(columnNames = {"MERCHANT_ID", "CODE"}))
public class Catalog extends Auditable<String> {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE,
            generator = "TABLE_GEN")
    @TableGenerator(name = "TABLE_GEN",
            table = "SM_SEQUENCER",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT",
            pkColumnValue = "CATALOG_SEQ_NEXT_VAL",
            allocationSize = SchemaConstant.DESCRIPTION_ID_ALLOCATION_SIZE,
            initialValue = SchemaConstant.DESCRIPTION_ID_START_VALUE
    )
    private Long id;

    @Valid
    @OneToMany(mappedBy = "catalog", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<CatalogCategoryEntry> entry = new HashSet<CatalogCategoryEntry>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MERCHANT_ID", nullable = false)
    private MerchantStore merchantStore;


    @Column(name = "VISIBLE")
    private boolean visible;

    @Column(name = "DEFAULT_CATALOG")
    private boolean defaultCatalog;

    @NotEmpty
    @Column(name = "CODE", length = 100, nullable = false)
    private String code;

    @Column(name = "SORT_ORDER")
    private Integer sortOrder = 0;
}