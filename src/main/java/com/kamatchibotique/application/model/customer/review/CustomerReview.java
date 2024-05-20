package com.kamatchibotique.application.model.customer.review;

import com.kamatchibotique.application.model.common.Auditable;
import com.kamatchibotique.application.model.customer.Customer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CUSTOMER_REVIEW", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "CUSTOMERS_ID",
                "REVIEWED_CUSTOMER_ID"
        })
})
public class CustomerReview extends Auditable<String> {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "CUSTOMER_REVIEW_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT",
            pkColumnValue = "CUSTOMER_REVIEW_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    @Column(name = "REVIEWS_RATING")
    private Double reviewRating;

    @Column(name = "REVIEWS_READ")
    private Long reviewRead;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REVIEW_DATE")
    private Date reviewDate;

    @Column(name = "STATUS")
    private Integer status;

    @ManyToOne
    @JoinColumn(name = "CUSTOMERS_ID")
    private Customer customer;

    @OneToOne
    @JoinColumn(name = "REVIEWED_CUSTOMER_ID")
    private Customer reviewedCustomer;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "customerReview")
    private Set<CustomerReviewDescription> descriptions = new HashSet<CustomerReviewDescription>();
}
