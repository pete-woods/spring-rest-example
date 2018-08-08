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

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.util.Assert;

public class AttributeAuthoritiesMapper implements GrantedAuthoritiesMapper {

	private static final Logger LOG = LoggerFactory.getLogger(AttributeAuthoritiesMapper.class);

	private final String attribute;

	private final GrantedAuthoritiesMapper delegateMapper;

	public AttributeAuthoritiesMapper(String attribute, GrantedAuthoritiesMapper delegateMapper) {
		Assert.notNull(attribute, "attribute cannot be null");
		Assert.notNull(delegateMapper, "delegateMapper cannot be null");

		this.attribute = attribute;
		this.delegateMapper = delegateMapper;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
		Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

		authorities.forEach(authority -> {
			if (OidcUserAuthority.class.isInstance(authority)) {
				OidcUserAuthority oidcUserAuthority = (OidcUserAuthority) authority;

				OidcIdToken idToken = oidcUserAuthority.getIdToken();
				if (idToken != null) {
					addAuthorities(mappedAuthorities, idToken.getClaimAsStringList(attribute));
				} else {
					LOG.warn("idToken was null");
				}

				OidcUserInfo userInfo = oidcUserAuthority.getUserInfo();
				if (userInfo != null) {
					addAuthorities(mappedAuthorities, userInfo.getClaimAsStringList(attribute));
				} else {
					LOG.warn("userInfo was null");
				}
			} else if (OAuth2UserAuthority.class.isInstance(authority)) {
				OAuth2UserAuthority oauth2UserAuthority = (OAuth2UserAuthority) authority;

				Map<String, Object> userAttributes = oauth2UserAuthority.getAttributes();
				addAuthorities(mappedAuthorities, (List<String>) userAttributes.get(attribute));
			}
		});

		return delegateMapper.mapAuthorities(mappedAuthorities);
	}

	private static void addAuthorities(Set<GrantedAuthority> mappedAuthorities, List<String> claims) {
		if (claims == null) {
			return;
		}

		claims.forEach(role -> {
			LOG.debug("Mapping authority=[{}]", role);
			mappedAuthorities.add(new SimpleGrantedAuthority(role));
		});
	}

}
