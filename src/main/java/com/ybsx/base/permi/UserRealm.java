package com.ybsx.base.permi;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.wf.etp.authz.IUserRealm;

@Component
public class UserRealm extends IUserRealm {

	@Override
	public Set<String> getUserPermissions(String arg0) {
		return null;
	}

	@Override
	public Set<String> getUserRoles(String arg0) {
		return null;
	}

}