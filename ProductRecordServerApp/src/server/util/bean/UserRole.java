package server.util.bean;

import java.io.Serializable;
import java.util.NoSuchElementException;

public enum UserRole implements Serializable {
	SYSADMIN("сисадмин"), 
	ADMIN("администратор"),
	EMPLOYEE("работник");
	
	private String name;
	
	private UserRole(String roleName) {
		name = roleName;
	}
	
	public String getName() {
		return name;
	}
	
	public static UserRole byName(String roleName) {
		for (UserRole role : UserRole.values()) {
			if (role.getName().equals(roleName)) {
				return role;
			}
		}
		throw new NoSuchElementException();
	}
	
}
