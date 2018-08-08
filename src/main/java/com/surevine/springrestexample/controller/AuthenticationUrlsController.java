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

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.context.annotation.Profile;
import org.springframework.core.ResolvableType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.surevine.springrestexample.ApplicationConstants;
import com.surevine.springrestexample.ApplicationProfiles;
import com.surevine.springrestexample.api.AuthenticationUrls;
import com.surevine.springrestexample.api.AuthenticationUrls.AuthenticationUrl;

@RestController
@Profile(ApplicationProfiles.APPLICATION)
@RequestMapping(ApplicationConstants.API_PREFIX + "auth")
public final class AuthenticationUrlsController {

	public static final String AUTHORIZATION_REQUEST_BASE_URI = "/oauth2/authorization";

	private final AuthenticationUrls authenticationUrls;

	@SuppressWarnings("unchecked")
	public AuthenticationUrlsController(ClientRegistrationRepository clientRegistrationRepository) {
		Iterable<ClientRegistration> clientRegistrations = null;
		ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository).as(Iterable.class);
		if (type != ResolvableType.NONE && ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
			clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
			authenticationUrls = new AuthenticationUrls(StreamSupport.stream(clientRegistrations.spliterator(), false)
					.map(registration -> new AuthenticationUrl(registration.getRegistrationId(),
							registration.getClientName(),
							AUTHORIZATION_REQUEST_BASE_URI + "/" + registration.getRegistrationId()))
					.collect(Collectors.toList()));
		} else {
			throw new IllegalStateException(
					"ClientRegistrationRepository could not be converted to Iterable<ClientRegistration>");
		}

	}

	@GetMapping
	public AuthenticationUrls get() {
		return authenticationUrls;
	}
}