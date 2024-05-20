package com.kamatchibotique.application.model.reference.language;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kamatchibotique.application.model.common.Auditable;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "LANGUAGE", indexes = {@Index(name = "CODE_IDX2", columnList = "CODE")})
@Cacheable
public class Language extends Auditable<String> {
    private static final long serialVersionUID = 1L;


    @Id
    @Column(name = "LANGUAGE_ID")
    @TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT", pkColumnValue = "LANG_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Integer id;

    @Column(name = "CODE", nullable = false)
    private String code;

    @JsonIgnore
    @Column(name = "SORT_ORDER")
    private Integer sortOrder;

    @JsonIgnore
    @OneToMany(mappedBy = "defaultLanguage", targetEntity = MerchantStore.class)
    private List<MerchantStore> storesDefaultLanguage;

    @JsonIgnore
    @ManyToMany(mappedBy = "languages", targetEntity = MerchantStore.class, fetch = FetchType.LAZY)
    private List<MerchantStore> stores = new ArrayList<MerchantStore>();

    public Language() {
    }

    public Language(String code) {
        this.setCode(code);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj)
            return false;
        if (!(obj instanceof Language)) {
            return false;
        } else {
            Language language = (Language) obj;
            return (this.id == language.getId());
        }
    }
}
