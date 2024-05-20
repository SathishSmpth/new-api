package com.kamatchibotique.application.model.system;

import com.kamatchibotique.application.enums.system.MerchantConfigurationType;
import com.kamatchibotique.application.model.common.Auditable;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import jakarta.persistence.*;

@Entity
@Table(name = "MERCHANT_CONFIGURATION",
        uniqueConstraints = @UniqueConstraint(columnNames = {"MERCHANT_ID", "CONFIG_KEY"}))
public class MerchantConfiguration extends Auditable<String> {
    private static final long serialVersionUID = 4246917986731953459L;

    @Id
    @Column(name = "MERCHANT_CONFIG_ID")
    @TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT", pkColumnValue = "MERCH_CONF_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MERCHANT_ID", nullable = true)
    private MerchantStore merchantStore;

    @Column(name = "CONFIG_KEY")
    private String key;

    /**
     * activate and deactivate configuration
     */
    @Column(name = "ACTIVE", nullable = true)
    private boolean active = false;


    @Column(name = "VALUE")
    private String value;

    @Column(name = "TYPE")
    @Enumerated(value = EnumType.STRING)
    private MerchantConfigurationType merchantConfigurationType =
            MerchantConfigurationType.INTEGRATION;

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public MerchantStore getMerchantStore() {
        return merchantStore;
    }

    public void setMerchantStore(MerchantStore merchantStore) {
        this.merchantStore = merchantStore;
    }

    public void setMerchantConfigurationType(MerchantConfigurationType merchantConfigurationType) {
        this.merchantConfigurationType = merchantConfigurationType;
    }

    public MerchantConfigurationType getMerchantConfigurationType() {
        return merchantConfigurationType;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }


}
