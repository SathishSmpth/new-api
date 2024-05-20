package com.kamatchibotique.application.model.common;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class CredentialsReset {
	@Column (name ="RESET_CREDENTIALS_REQ", length=256)
	private String credentialsRequest;

	@Temporal(TemporalType.DATE)
	@Column(name = "RESET_CREDENTIALS_EXP")
	private Date credentialsRequestExpiry = new Date();
}
