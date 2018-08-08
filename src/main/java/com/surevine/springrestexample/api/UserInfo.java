//  Copyright (c) 2018 Surevine Ltd.
//
//  Permission is hereby granted, free of charge, to any person
//  obtaining a copy of this software and associated documentation
//  files (the "Software"), to deal in the Software without
//  restriction, including without limitation the rights to use, copy,
//  modify, merge, publish, distribute, sublicense, and/or sell copies
//  of the Software, and to permit persons to whom the Software is
//  furnished to do so, subject to the following conditions:
//
//  The above copyright notice and this permission notice shall be
//  included in all copies or substantial portions of the Software.
//  
//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
//  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
//  OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
//  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
//  HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
//  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
//  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
//  OTHER DEALINGS IN THE SOFTWARE.

package com.surevine.springrestexample.api;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.google.common.base.MoreObjects;

public class UserInfo {
	private String principalId;

	private String fullName;

	private String picture;

	private String email;

	private List<String> roles;

	public static UserInfo fromAuthentication(Authentication authentication) {
		UserInfo userInfo = new UserInfo();
		if (authentication != null) {
			userInfo.setPrincipalId(authentication.getName());
			readToken(authentication, userInfo);
		}
		return userInfo;
	}

	private static void readToken(Authentication authentication, UserInfo userInfo) {
		OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
		OAuth2User principal = token.getPrincipal();
		Map<String, Object> attributes = principal.getAttributes();
		userInfo.setFullName((String) attributes.get("name"));
		userInfo.setEmail((String) attributes.get("email"));
		userInfo.setPicture((String) attributes.get("picture"));
		userInfo.setRoles(authentication.getAuthorities().stream().map(authority -> authority.getAuthority())
				.collect(Collectors.toList()));
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("principalId", principalId).add("fullName", fullName)
				.add("picture", picture).add("email", email).add("roles", roles).toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(principalId, fullName, picture, email, roles);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final UserInfo other = (UserInfo) obj;
		return Objects.equals(principalId, other.principalId) && Objects.equals(fullName, other.fullName)
				&& Objects.equals(picture, other.picture) && Objects.equals(email, other.email)
				&& Objects.equals(roles, other.roles);
	}

	public String getPrincipalId() {
		return principalId;
	}

	public void setPrincipalId(String principalId) {
		this.principalId = principalId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
}
