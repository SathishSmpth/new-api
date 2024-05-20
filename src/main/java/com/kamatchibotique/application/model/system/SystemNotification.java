package com.kamatchibotique.application.model.system;

import com.kamatchibotique.application.model.common.Auditable;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.user.User;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "SYSTEM_NOTIFICATION", uniqueConstraints =
@UniqueConstraint(columnNames = {"MERCHANT_ID", "CONFIG_KEY"}))
public class SystemNotification extends Auditable<String> {

    private static final long serialVersionUID = -6269172313628887000L;

    @Id
    @Column(name = "SYSTEM_NOTIF_ID")
    @TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "SYST_NOTIF_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    @Column(name = "CONFIG_KEY")
    private String key;

    @Column(name = "VALUE")
    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MERCHANT_ID", nullable = true)
    private MerchantStore merchantStore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = true)
    private User user;

    @Temporal(TemporalType.DATE)
    @Column(name = "START_DATE")
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "END_DATE")
    private Date endDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setMerchantStore(MerchantStore merchantStore) {
        this.merchantStore = merchantStore;
    }

    public MerchantStore getMerchantStore() {
        return merchantStore;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
