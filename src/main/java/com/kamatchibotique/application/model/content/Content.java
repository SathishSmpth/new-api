package com.kamatchibotique.application.model.content;

import com.kamatchibotique.application.enums.content.ContentPosition;
import com.kamatchibotique.application.enums.content.ContentType;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CONTENT",
        indexes = {@Index(name = "CODE_IDX", columnList = "CODE")},
        uniqueConstraints = @UniqueConstraint(columnNames = {"MERCHANT_ID", "CODE"}))
public class Content implements Serializable {

    private static final long serialVersionUID = 1772757159185494620L;

    @Id
    @Column(name = "CONTENT_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "CONTENT_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    @Valid
    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ContentDescription> descriptions = new ArrayList<ContentDescription>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MERCHANT_ID", nullable = false)
    private MerchantStore merchantStore;

    @NotEmpty
    @Column(name = "CODE", length = 100, nullable = false)
    private String code;

    @Column(name = "VISIBLE")
    private boolean visible;

    @Column(name = "LINK_TO_MENU")
    private boolean linkToMenu;

    @Column(name = "CONTENT_POSITION", length = 10, nullable = true)
    @Enumerated(value = EnumType.STRING)
    private ContentPosition contentPosition;

    //Used for grouping
    //BOX, SECTION, PAGE
    @Column(name = "CONTENT_TYPE", length = 10, nullable = true)
    @Enumerated(value = EnumType.STRING)
    private ContentType contentType;

    @Column(name = "SORT_ORDER")
    private Integer sortOrder = 0;

    //A page can contain one product listing
    @Column(name = "PRODUCT_GROUP", nullable = true)
    private String productGroup;

    public ContentDescription getDescription() {
        if (this.getDescriptions() != null && this.getDescriptions().size() > 0) {
            return this.getDescriptions().get(0);
        }
        return null;
    }
}