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

package com.surevine.springrestexample.config;

import static com.surevine.springrestexample.ApplicationConstants.API_PREFIX;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import com.surevine.springrestexample.ApplicationProfiles;
import com.surevine.springrestexample.oidc.AttributeAuthoritiesMapper;

@Configuration
@Profile(ApplicationProfiles.APPLICATION)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers(API_PREFIX + "admin/**")
					.authenticated()
				.anyRequest()
					.permitAll()
				.and()
			.exceptionHandling()
				.authenticationEntryPoint(authenticationEntryPoint())
				.and()
			.oauth2Login()
				.userInfoEndpoint()
					.userAuthoritiesMapper(grantedAuthoritiesMapper())
					.and()
				.defaultSuccessUrl("/", true);
	}

	private AuthenticationEntryPoint authenticationEntryPoint() {
		return new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED);
	}

	private GrantedAuthoritiesMapper grantedAuthoritiesMapper() {
		SimpleAuthorityMapper simpleMapper = new SimpleAuthorityMapper();
		simpleMapper.setConvertToUpperCase(true);
		simpleMapper.setDefaultAuthority("ROLE_USER");
		return new AttributeAuthoritiesMapper("roles", simpleMapper);
	}
}
