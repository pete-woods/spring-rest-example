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

package com.surevine.springrestexample.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.Test;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import com.surevine.springrestexample.api.AuthenticationUrls;
import com.surevine.springrestexample.api.AuthenticationUrls.AuthenticationUrl;
import com.surevine.springrestexample.controller.AuthenticationUrlsController;

public class AuthenticationUrlsControllerTest {

	private static ClientRegistration cr() {
		return ClientRegistration.withRegistrationId("registration-id")
				.authorizationGrantType(new AuthorizationGrantType("authorization_code")).clientId("client-id")
				.clientSecret("client-secret").redirectUriTemplate("redirect-uri-template").scope("oidc")
				.scope("profile")
				.authorizationUri("http://localhost:8080/auth/realms/realm/protocol/openid-connect/auth")
				.tokenUri("http://localhost:8080/auth/realms/realm/protocol/openid-connect/token")
				.clientName("client-name").build();
	}

	@Test
	public void testGet() {
		ClientRegistrationRepository clientRegistrationRepository = new InMemoryClientRegistrationRepository(cr());
		AuthenticationUrlsController controller = new AuthenticationUrlsController(clientRegistrationRepository);

		assertThat(controller.get()).isEqualTo(new AuthenticationUrls(Arrays.asList(
				new AuthenticationUrl("registration-id", "client-name", "/oauth2/authorization/registration-id"))));
	}

}
