package com.kamatchibotique.application.model.user;

import com.kamatchibotique.application.model.common.Auditable;
import com.kamatchibotique.application.model.common.CredentialsReset;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.reference.language.Language;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "USERS",
        indexes = {@Index(name = "USR_NAME_IDX", columnList = "ADMIN_NAME")},
        uniqueConstraints =
        @UniqueConstraint(columnNames = {"MERCHANT_ID", "ADMIN_NAME"}))
public class User extends Auditable<String> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "USER_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "USER_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    public User(String userName, String password, String email) {
        this.adminName = userName;
        this.adminPassword = password;
        this.adminEmail = email;
    }

    @NotEmpty
    @Column(name = "ADMIN_NAME", length = 100)
    private String adminName;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    @JoinTable(
            name = "USER_GROUP",
            joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "GROUP_ID", referencedColumnName = "GROUP_ID")
    )
    @Cascade({
            org.hibernate.annotations.CascadeType.DETACH,
            org.hibernate.annotations.CascadeType.LOCK,
            org.hibernate.annotations.CascadeType.REFRESH,
            org.hibernate.annotations.CascadeType.REPLICATE
    })
    private List<Group> groups = new ArrayList<>();

    @NotEmpty
    @Email
    @Column(name = "ADMIN_EMAIL")
    private String adminEmail;

    @NotEmpty
    @Column(name = "ADMIN_PASSWORD", length = 60)
    private String adminPassword;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MERCHANT_ID", nullable = false)
    private MerchantStore merchantStore;

    @Column(name = "ADMIN_FIRST_NAME")
    private String firstName;

    @Column(name = "ACTIVE")
    private boolean active = true;

    @Column(name = "ADMIN_LAST_NAME")
    private String lastName;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Language.class)
    @JoinColumn(name = "LANGUAGE_ID")
    private Language defaultLanguage;

    @Column(name = "ADMIN_Q1")
    private String question1;

    @Column(name = "ADMIN_Q2")
    private String question2;

    @Column(name = "ADMIN_Q3")
    private String question3;

    @Column(name = "ADMIN_A1")
    private String answer1;

    @Column(name = "ADMIN_A2")
    private String answer2;

    @Column(name = "ADMIN_A3")
    private String answer3;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_ACCESS")
    private Date lastAccess;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LOGIN_ACCESS")
    private Date loginTime;

    @Embedded
    private CredentialsReset credentialsResetRequest = null;
}
