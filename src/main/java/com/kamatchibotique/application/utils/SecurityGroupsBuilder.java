package com.kamatchibotique.application.utils;

import java.util.ArrayList;
import java.util.List;

import com.kamatchibotique.application.enums.GroupType;
import com.kamatchibotique.application.model.user.Group;
import com.kamatchibotique.application.model.user.Permission;

public class SecurityGroupsBuilder {
	
	private List<Group> groups = new ArrayList<Group>();
	private Group lastGroup = null;
	
	
	public SecurityGroupsBuilder addGroup(String name, GroupType type) {
		
		Group g = new Group();
		g.setGroupName(name);
		g.setGroupType(type);
		groups.add(g);
		this.lastGroup = g;
		
		return this;
	}
	
	public SecurityGroupsBuilder addPermission(String name) {
		if(this.lastGroup == null) {
			Group g = this.groups.get(0);
			if(g == null) {
				g = new Group();
				g.setGroupName("UNDEFINED");
				g.setGroupType(GroupType.ADMIN);
				groups.add(g);
				this.lastGroup = g;
			}
		}
		
		Permission permission = new Permission();
		permission.setPermissionName(name);
		lastGroup.getPermissions().add(permission);
		
		return this;
	}
	
	public SecurityGroupsBuilder addPermission(Permission permission) {
		
		if(this.lastGroup == null) {
			Group g = this.groups.get(0);
			if(g == null) {
				g = new Group();
				g.setGroupName("UNDEFINED");
				g.setGroupType(GroupType.ADMIN);
				groups.add(g);
				this.lastGroup = g;
			}
		}
		

		lastGroup.getPermissions().add(permission);
		
		return this;
	}
	
	public List<Group> build() {
		return groups;
	}

}
