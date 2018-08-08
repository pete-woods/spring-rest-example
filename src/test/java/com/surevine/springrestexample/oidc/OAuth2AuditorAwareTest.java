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

import static com.surevine.springrestexample.support.TestSupport.newAuthenticationToken;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Test;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

import com.surevine.springrestexample.config.SecurityContextProvider;
import com.surevine.springrestexample.entity.User;
import com.surevine.springrestexample.oidc.OAuth2AuditorAware;
import com.surevine.springrestexample.repository.UserRepository;

public class OAuth2AuditorAwareTest {

	private final UserRepository userRepository = mock(UserRepository.class);

	private final SecurityContext securityContext = mock(SecurityContext.class);

	private final SecurityContextProvider securityContextProvider = mock(SecurityContextProvider.class);

	private final AuditorAware<User> auditorAware = new OAuth2AuditorAware(userRepository, securityContextProvider);

	@Test
	public void testAuthenticated() {
		Authentication authentication = newAuthenticationToken();
		User user = new User(authentication.getName());

		when(securityContextProvider.get()).thenReturn(securityContext);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		when(userRepository.findByPrincipalId(user.getPrincipalId())).thenReturn(Optional.of(user));

		Optional<User> auditor = auditorAware.getCurrentAuditor();
		assertThat(auditor.get()).isSameAs(user);
	}

	@Test
	public void testUnauthenticated() {
		when(securityContextProvider.get()).thenReturn(securityContext);
		when(securityContext.getAuthentication()).thenReturn(null);

		Optional<User> auditor = auditorAware.getCurrentAuditor();
		assertThat(auditor.isPresent()).isFalse();
	}

}
