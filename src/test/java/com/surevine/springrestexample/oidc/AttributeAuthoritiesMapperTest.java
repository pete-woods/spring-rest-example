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

package com.surevine.springrestexample.oidc;

import static com.surevine.springrestexample.support.TestSupport.sga;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.surevine.springrestexample.oidc.AttributeAuthoritiesMapper;

public class AttributeAuthoritiesMapperTest {

	private GrantedAuthoritiesMapper delegate;

	private AttributeAuthoritiesMapper mapper;

	@Before
	public void setUp() {
		delegate = new NullAuthoritiesMapper();
		mapper = new AttributeAuthoritiesMapper("roles", delegate);
	}

	@Test
	public void testMapOidcClaims() {
		ImmutableMap<String, Object> claims = new ImmutableMap.Builder<String, Object>().put("sub", "12345")
				.put("name", "Bob Person").put("roles", Arrays.asList("a", "b")).build();
		OidcIdToken oidcIdToken = new OidcIdToken("asd", Instant.ofEpochSecond(0), Instant.ofEpochSecond(1000), claims);
		OidcUserInfo oidcUserInfo = new OidcUserInfo(claims);
		GrantedAuthority grantedAuthority = new OidcUserAuthority(oidcIdToken, oidcUserInfo);
		Collection<GrantedAuthority> input = Collections.singleton(grantedAuthority);

		Collection<? extends GrantedAuthority> mappedAuthorities = mapper.mapAuthorities(input);
		assertThat(mappedAuthorities)
				.isEqualTo(new ImmutableSet.Builder<GrantedAuthority>().add(sga("a")).add(sga("b")).build());
	}

	@Test
	public void testMapOauth2Authorities() {
		ImmutableMap<String, Object> claims = new ImmutableMap.Builder<String, Object>().put("name", "Other Guy")
				.put("roles", Arrays.asList("c", "d", "e")).build();
		GrantedAuthority grantedAuthority = new OAuth2UserAuthority(claims);
		Collection<GrantedAuthority> input = Collections.singleton(grantedAuthority);

		Collection<? extends GrantedAuthority> mappedAuthorities = mapper.mapAuthorities(input);
		assertThat(mappedAuthorities).isEqualTo(
				new ImmutableSet.Builder<GrantedAuthority>().add(sga("c")).add(sga("d")).add(sga("e")).build());
	}

}
