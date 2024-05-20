package com.kamatchibotique.application.model.catalog.category;

import com.kamatchibotique.application.model.common.Auditable;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CATEGORY",
        indexes = @Index(columnList = "LINEAGE"),
        uniqueConstraints =
        @UniqueConstraint(columnNames = {"MERCHANT_ID", "CODE"}))
public class Category extends Auditable<String> {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "CATEGORY_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "CATEGORY_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    @Valid
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<CategoryDescription> descriptions = new HashSet<CategoryDescription>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MERCHANT_ID", nullable = false)
    private MerchantStore merchantStore;

    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
    private List<Category> categories = new ArrayList<Category>();

    @Column(name = "CATEGORY_IMAGE", length = 100)
    private String categoryImage;

    @Column(name = "SORT_ORDER")
    private Integer sortOrder = 0;

    @Column(name = "CATEGORY_STATUS")
    private boolean categoryStatus;

    @Column(name = "VISIBLE")
    private boolean visible;

    @Column(name = "DEPTH")
    private Integer depth;

    @Column(name = "LINEAGE")
    private String lineage;

    @Column(name = "FEATURED")
    private boolean featured;

    @NotEmpty
    @Column(name = "CODE", length = 100, nullable = false)
    private String code;
}