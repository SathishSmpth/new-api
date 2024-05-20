package com.kamatchibotique.application.model.catalog.product.review;

import com.kamatchibotique.application.constants.SchemaConstant;
import com.kamatchibotique.application.model.common.description.Description;
import com.kamatchibotique.application.model.reference.language.Language;
import jakarta.persistence.*;

@Entity
@Table(name = "PRODUCT_REVIEW_DESCRIPTION", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "PRODUCT_REVIEW_ID",
                "LANGUAGE_ID"
        })
})
@TableGenerator(name = "description_gen", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "product_review_description_seq", allocationSize = SchemaConstant.DESCRIPTION_ID_ALLOCATION_SIZE, initialValue = SchemaConstant.DESCRIPTION_ID_START_VALUE)
public class ProductReviewDescription extends Description {
    private static final long serialVersionUID = 1L;

    @ManyToOne(targetEntity = ProductReview.class)
    @JoinColumn(name = "PRODUCT_REVIEW_ID")
    private ProductReview productReview;

    public ProductReviewDescription() {
    }

    public ProductReviewDescription(Language language, String name) {
        this.setLanguage(language);
        this.setName(name);
    }

    public ProductReview getProductReview() {
        return productReview;
    }

    public void setProductReview(ProductReview productReview) {
        this.productReview = productReview;
    }
}
