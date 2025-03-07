package com.kamatchibotique.application.model.common;

import com.kamatchibotique.application.model.reference.country.Country;
import com.kamatchibotique.application.model.reference.zone.Zone;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Delivery {

    @Column(name = "DELIVERY_LAST_NAME", length = 64)
    private String lastName;

    @Column(name = "DELIVERY_FIRST_NAME", length = 64)
    private String firstName;

    @Column(name = "DELIVERY_COMPANY", length = 100)
    private String company;

    @Column(name = "DELIVERY_STREET_ADDRESS", length = 256)
    private String address;

    @Column(name = "DELIVERY_CITY", length = 100)
    private String city;

    @Column(name = "DELIVERY_POSTCODE", length = 20)
    private String postalCode;

    @Column(name = "DELIVERY_STATE", length = 100)
    private String state;

    @Column(name = "DELIVERY_TELEPHONE", length = 32)
    private String telephone;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Country.class)
    @JoinColumn(name = "DELIVERY_COUNTRY_ID", nullable = true)
    private Country country;


    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Zone.class)
    @JoinColumn(name = "DELIVERY_ZONE_ID", nullable = true)
    private Zone zone;

    @Transient
    private String latitude = null;

    @Transient
    private String longitude = null;
}
