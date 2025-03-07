package com.kamatchibotique.application.model.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kamatchibotique.application.enums.customer.CustomerGender;
import com.kamatchibotique.application.model.catalog.product.review.ProductReview;
import com.kamatchibotique.application.model.common.Auditable;
import com.kamatchibotique.application.model.common.Billing;
import com.kamatchibotique.application.model.common.CredentialsReset;
import com.kamatchibotique.application.model.common.Delivery;
import com.kamatchibotique.application.model.customer.attribute.CustomerAttribute;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.reference.language.Language;
import com.kamatchibotique.application.model.user.Group;
import com.kamatchibotique.application.utils.CloneUtils;
import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.*;

@Entity
@Table(name = "CUSTOMER",
        uniqueConstraints =
        @UniqueConstraint(columnNames = {"MERCHANT_ID", "CUSTOMER_NICK"}))
public class Customer extends Auditable<String> {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "CUSTOMER_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT",
            pkColumnValue = "CUSTOMER_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "customer")
    private Set<CustomerAttribute> attributes = new HashSet<CustomerAttribute>();

    @Column(name = "CUSTOMER_GENDER", length = 1, nullable = true)
    @Enumerated(value = EnumType.STRING)
    private CustomerGender gender;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CUSTOMER_DOB")
    private Date dateOfBirth;

    @Email
    @NotEmpty
    @Column(name = "CUSTOMER_EMAIL_ADDRESS", length = 96, nullable = false)
    private String emailAddress;

    @Column(name = "CUSTOMER_NICK", length = 96)
    private String userName;// unique username per store

    @Column(name = "CUSTOMER_COMPANY", length = 100)
    private String company;

    @JsonIgnore
    @Column(name = "CUSTOMER_PASSWORD", length = 60)
    private String password;

    @Column(name = "CUSTOMER_ANONYMOUS")
    private boolean anonymous;

    @Column(name = "REVIEW_AVG")
    private BigDecimal customerReviewAvg;

    @Column(name = "REVIEW_COUNT")
    private Integer customerReviewCount;

    @Column(name = "PROVIDER")
    private String provider;


    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Language.class)
    @JoinColumn(name = "LANGUAGE_ID", nullable = false)
    private Language defaultLanguage;


    @OneToMany(mappedBy = "customer", targetEntity = ProductReview.class)
    private List<ProductReview> reviews = new ArrayList<ProductReview>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MERCHANT_ID", nullable = false)
    private MerchantStore merchantStore;


    @Embedded
    private Delivery delivery = null;

    @Valid
    @Embedded
    private Billing billing = null;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    @JoinTable(
            name = "CUSTOMER_GROUP",
            joinColumns = @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "CUSTOMER_ID"),
            inverseJoinColumns = @JoinColumn(name = "GROUP_ID", referencedColumnName = "GROUP_ID")
    )
    @Cascade({
            org.hibernate.annotations.CascadeType.DETACH,
            org.hibernate.annotations.CascadeType.LOCK,
            org.hibernate.annotations.CascadeType.REFRESH,
            org.hibernate.annotations.CascadeType.REPLICATE

    })
    private List<Group> groups = new ArrayList<Group>();

    @JsonIgnore
    @Transient
    private String showCustomerStateList;

    @JsonIgnore
    @Transient
    private String showBillingStateList;

    @JsonIgnore
    @Transient
    private String showDeliveryStateList;

    @Embedded
    private CredentialsReset credentialsResetRequest = null;

    public Customer() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Date getDateOfBirth() {
        return CloneUtils.clone(dateOfBirth);
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = CloneUtils.clone(dateOfBirth);
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }


    public List<ProductReview> getReviews() {
        return reviews;
    }

    public void setReviews(List<ProductReview> reviews) {
        this.reviews = reviews;
    }

    public void setMerchantStore(MerchantStore merchantStore) {
        this.merchantStore = merchantStore;
    }

    public MerchantStore getMerchantStore() {
        return merchantStore;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public void setBilling(Billing billing) {
        this.billing = billing;
    }

    public Billing getBilling() {
        return billing;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public String getShowCustomerStateList() {
        return showCustomerStateList;
    }

    public void setShowCustomerStateList(String showCustomerStateList) {
        this.showCustomerStateList = showCustomerStateList;
    }

    public String getShowBillingStateList() {
        return showBillingStateList;
    }

    public void setShowBillingStateList(String showBillingStateList) {
        this.showBillingStateList = showBillingStateList;
    }

    public String getShowDeliveryStateList() {
        return showDeliveryStateList;
    }

    public void setShowDeliveryStateList(String showDeliveryStateList) {
        this.showDeliveryStateList = showDeliveryStateList;
    }

    public Language getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(Language defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    public void setAttributes(Set<CustomerAttribute> attributes) {
        this.attributes = attributes;
    }

    public Set<CustomerAttribute> getAttributes() {
        return attributes;
    }

    public void setGender(CustomerGender gender) {
        this.gender = gender;
    }

    public CustomerGender getGender() {
        return gender;
    }

    public BigDecimal getCustomerReviewAvg() {
        return customerReviewAvg;
    }

    public void setCustomerReviewAvg(BigDecimal customerReviewAvg) {
        this.customerReviewAvg = customerReviewAvg;
    }

    public Integer getCustomerReviewCount() {
        return customerReviewCount;
    }

    public void setCustomerReviewCount(Integer customerReviewCount) {
        this.customerReviewCount = customerReviewCount;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public CredentialsReset getCredentialsResetRequest() {
        return credentialsResetRequest;
    }

    public void setCredentialsResetRequest(CredentialsReset credentialsResetRequest) {
        this.credentialsResetRequest = credentialsResetRequest;
    }

}
