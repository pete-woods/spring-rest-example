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

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.surevine.springrestexample.config.SecurityContextProvider;
import com.surevine.springrestexample.entity.User;
import com.surevine.springrestexample.repository.UserRepository;

public class OAuth2AuditorAware implements AuditorAware<User> {
	private UserRepository userRepository;

	private SecurityContextProvider securityContextProvider;

	public OAuth2AuditorAware(UserRepository userRepository, SecurityContextProvider securityContextProvider) {
		this.userRepository = userRepository;
		this.securityContextProvider = securityContextProvider;
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
	@Override
	public Optional<User> getCurrentAuditor() {
		Authentication authentication = securityContextProvider.get().getAuthentication();

		if (authentication != null && authentication.isAuthenticated()) {
			return userRepository.findByPrincipalId(authentication.getName());
		}

		return Optional.empty();
	}
}