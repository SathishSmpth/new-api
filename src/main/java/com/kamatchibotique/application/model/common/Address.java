package com.kamatchibotique.application.model.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address implements Serializable {
	private static final long serialVersionUID = 1L;
	private String city;
	private String postalCode;
	private String stateProvince;
	private String zone;//code
	private String country;//code
}

