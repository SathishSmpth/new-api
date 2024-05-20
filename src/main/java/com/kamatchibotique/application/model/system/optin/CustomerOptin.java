package com.kamatchibotique.application.model.system.optin;

import com.kamatchibotique.application.model.common.Auditable;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.util.Date;

@Entity
@Table(name = "CUSTOMER_OPTIN", uniqueConstraints =
@UniqueConstraint(columnNames = {"EMAIL", "OPTIN_ID"}))
public class CustomerOptin extends Auditable<String> {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "CUSTOMER_OPTIN_ID")
    @TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "CUST_OPT_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "OPTIN_DATE")
    private Date optinDate;


    @ManyToOne(targetEntity = Optin.class)
    @JoinColumn(name = "OPTIN_ID")
    private Optin optin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MERCHANT_ID", nullable = false)
    private MerchantStore merchantStore;

    @Column(name = "FIRST")
    private String firstName;

    @Column(name = "LAST")
    private String lastName;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "VALUE")
    private String value;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Date getOptinDate() {
        return optinDate;
    }

    public void setOptinDate(Date optinDate) {
        this.optinDate = optinDate;
    }

    public Optin getOptin() {
        return optin;
    }

    public void setOptin(Optin optin) {
        this.optin = optin;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public MerchantStore getMerchantStore() {
        return merchantStore;
    }

    public void setMerchantStore(MerchantStore merchantStore) {
        this.merchantStore = merchantStore;
    }

}
