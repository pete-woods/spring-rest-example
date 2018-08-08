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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.Test;

import com.surevine.springrestexample.api.UserInfo;

public class UserInfoTest {

	@Test
	public void testEqualsHashCode() {
		UserInfo a = new UserInfo();
		UserInfo b = null;
		assertThat(a).isNotEqualTo(b);
		assertThat(a).isNotEqualTo(new Object());

		b = a;
		assertThat(a).isEqualTo(b);
		assertThat(a.hashCode()).isEqualTo(b.hashCode());

		b = new UserInfo();
		assertThat(a).isEqualTo(b);
		assertThat(a.hashCode()).isEqualTo(b.hashCode());

		a.setEmail("email");
		a.setFullName("fullName");
		a.setPicture("picture");
		a.setPrincipalId("principalId");
		a.setRoles(Arrays.asList("ROLE_USER", "ROLE_ADMIN"));
		assertThat(a).isNotEqualTo(b);
		assertThat(a.hashCode()).isNotEqualTo(b.hashCode());

		b.setEmail("email");
		assertThat(a).isNotEqualTo(b);
		assertThat(a.hashCode()).isNotEqualTo(b.hashCode());

		b.setFullName("fullName");
		assertThat(a).isNotEqualTo(b);
		assertThat(a.hashCode()).isNotEqualTo(b.hashCode());

		b.setPicture("picture");
		assertThat(a).isNotEqualTo(b);
		assertThat(a.hashCode()).isNotEqualTo(b.hashCode());

		b.setPrincipalId("principalId");
		assertThat(a).isNotEqualTo(b);
		assertThat(a.hashCode()).isNotEqualTo(b.hashCode());

		b.setRoles(Arrays.asList("ROLE_USER", "ROLE_ADMIN"));
		assertThat(a).isEqualTo(b);
		assertThat(a.hashCode()).isEqualTo(b.hashCode());
	}

	@Test
	public void testToString() {
		UserInfo a = new UserInfo();
		a.setEmail("email");
		a.setFullName("fullName");
		a.setPicture("picture");
		a.setPrincipalId("principalId");
		a.setRoles(Arrays.asList("ROLE_USER", "ROLE_ADMIN"));
		assertThat(a.toString()).isEqualTo(
				"UserInfo{principalId=principalId, fullName=fullName, picture=picture, email=email, roles=[ROLE_USER, ROLE_ADMIN]}");
	}
}
