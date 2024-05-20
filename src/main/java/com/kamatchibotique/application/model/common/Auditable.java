package com.kamatchibotique.application.model.common;


import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Auditable<U> implements Serializable {
    @CreatedBy
    @Column(name = "created_by", nullable = false)
    private U createdBy;

    @CreationTimestamp
    @Column(name = "created_date", nullable = false)
    private Date createdDate;

    @UpdateTimestamp
    @Column(name = "updated_by", nullable = false)
    private U updatedBy;

    @LastModifiedBy
    @Column(name = "updated_date", nullable = false)
    private U updatedDate;
}
