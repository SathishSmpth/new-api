package com.kamatchibotique.application.model.user;

import java.util.HashSet;
import java.util.Set;

import com.kamatchibotique.application.enums.GroupType;
import com.kamatchibotique.application.model.common.Auditable;
import jakarta.persistence.*;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "SM_GROUP", indexes = {
		@Index(name = "SM_GROUP_GROUP_TYPE", columnList = "GROUP_TYPE") })
public class Group extends Auditable<String> {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "GROUP_ID", unique = true, nullable = false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "GROUP_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Integer id;

	@Column(name = "GROUP_TYPE")
	@Enumerated(value = EnumType.STRING)
	private GroupType groupType;

	@NotEmpty
	@Column(name = "GROUP_NAME", unique = true)
	private String groupName;

	public Group(String groupName) {
		this.groupName = groupName;
	}

	@JsonIgnore
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(
			name = "PERMISSION_GROUP",
			joinColumns = @JoinColumn(name = "GROUP_ID"),
			inverseJoinColumns = @JoinColumn(name = "PERMISSION_ID")
	)
	private Set<Permission> permissions = new HashSet<>();
}
