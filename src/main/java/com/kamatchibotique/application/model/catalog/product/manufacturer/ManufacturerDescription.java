package com.kamatchibotique.application.model.catalog.product.manufacturer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kamatchibotique.application.constants.SchemaConstant;
import com.kamatchibotique.application.model.common.description.Description;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "MANUFACTURER_DESCRIPTION", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "MANUFACTURER_ID",
                "LANGUAGE_ID"
        })
}
)
@TableGenerator(name = "description_gen", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "manufacturer_description_seq", allocationSize = SchemaConstant.DESCRIPTION_ID_ALLOCATION_SIZE, initialValue = SchemaConstant.DESCRIPTION_ID_START_VALUE)
public class ManufacturerDescription extends Description {
    private static final long serialVersionUID = 1L;

    @JsonIgnore
    @ManyToOne(targetEntity = Manufacturer.class)
    @JoinColumn(name = "MANUFACTURER_ID", nullable = false)
    private Manufacturer manufacturer;

    @Column(name = "MANUFACTURERS_URL")
    private String url;

    @Column(name = "URL_CLICKED")
    private Integer urlClicked;

    @Column(name = "DATE_LAST_CLICK")
    private Date dateLastClick;
}
