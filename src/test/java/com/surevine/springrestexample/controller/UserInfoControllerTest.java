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

import static com.surevine.springrestexample.support.TestSupport.newAuthenticationToken;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.Test;
import org.springframework.security.core.Authentication;

import com.surevine.springrestexample.api.UserInfo;
import com.surevine.springrestexample.controller.UserInfoController;

public class UserInfoControllerTest {

	private UserInfoController controller = new UserInfoController();

	@Test
	public void testGet() {
		Authentication oAuth2AuthenticationToken = newAuthenticationToken();
		UserInfo expectedUserInfo = new UserInfo();
		expectedUserInfo.setPrincipalId("12345");
		expectedUserInfo.setFullName("Bob Person");
		expectedUserInfo.setPicture("http://foo/bar.jpg");
		expectedUserInfo.setEmail("foo@bar.com");
		expectedUserInfo.setRoles(Arrays.asList("ROLE_USER", "ROLE_ADMIN"));
		assertThat(controller.get(oAuth2AuthenticationToken)).isEqualTo(expectedUserInfo);
	}
}
