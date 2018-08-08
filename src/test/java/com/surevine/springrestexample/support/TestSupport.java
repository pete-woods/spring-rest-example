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

package com.surevine.springrestexample.support;

import java.time.Instant;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

public class TestSupport {
	public static SimpleGrantedAuthority sga(String role) {
		return new SimpleGrantedAuthority(role);
	}

	public static Authentication newAuthenticationToken() {
		ImmutableSet<GrantedAuthority> authorities = new ImmutableSet.Builder<GrantedAuthority>().add(sga("ROLE_USER"))
				.add(sga("ROLE_ADMIN")).build();
		ImmutableMap<String, Object> claims = new ImmutableMap.Builder<String, Object>().put("sub", "12345")
				.put("name", "Bob Person").put("email", "foo@bar.com").put("picture", "http://foo/bar.jpg").build();
		OidcIdToken oidcIdToken = new OidcIdToken("asd", Instant.ofEpochSecond(0), Instant.ofEpochSecond(1000), claims);
		OidcUserInfo oidcUserInfo = new OidcUserInfo(claims);

		OAuth2User oauth2User = new DefaultOidcUser(authorities, oidcIdToken, oidcUserInfo, "sub");
		OAuth2AuthenticationToken oAuth2AuthenticationToken = new OAuth2AuthenticationToken(oauth2User, authorities,
				"client-id");
		return oAuth2AuthenticationToken;
	}
}
