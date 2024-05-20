package com.kamatchibotique.application.model.catalog.product.review;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kamatchibotique.application.model.catalog.product.Product;
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
@Table(name = "PRODUCT_REVIEW", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "CUSTOMERS_ID",
                "PRODUCT_ID"
        })
})
public class ProductReview extends Auditable<String> {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PRODUCT_REVIEW_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT",
            pkColumnValue = "PRODUCT_REVIEW_SEQ_NEXT_VAL")
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

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "CUSTOMERS_ID")
    private Customer customer;

    @OneToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "productReview")
    private Set<ProductReviewDescription> descriptions = new HashSet<ProductReviewDescription>();
}
