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
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.surevine.springrestexample.api.AuthenticationUrls;
import com.surevine.springrestexample.api.AuthenticationUrls.AuthenticationUrl;

public class AuthenticationUrlsTest {

	@Test
	public void testGetAuthenticationUrls() {
		List<AuthenticationUrl> urls = Collections.singletonList(newUrl("id", "name", "url"));
		AuthenticationUrls authenticationUrls = new AuthenticationUrls(urls);
		assertThat(authenticationUrls.getAuthenticationUrls()).isEqualTo(urls);
	}

	@Test
	public void testEqualsHashCode() {
		AuthenticationUrls a = new AuthenticationUrls(Collections.singletonList(newUrl("id", "name", "url")));
		AuthenticationUrls b = null;
		assertThat(a).isNotEqualTo(b);
		assertThat(a).isNotEqualTo(new Object());

		b = a;
		assertThat(a).isEqualTo(b);
		assertThat(a.hashCode()).isEqualTo(b.hashCode());

		b = new AuthenticationUrls(Collections.emptyList());
		assertThat(a).isNotEqualTo(b);
		assertThat(a.hashCode()).isNotEqualTo(b.hashCode());

		b = new AuthenticationUrls(Collections.singletonList(newUrl("id", "name", "url")));
		assertThat(a).isEqualTo(b);
		assertThat(a.hashCode()).isEqualTo(b.hashCode());

		b = new AuthenticationUrls(Collections.emptyList());
		assertThat(a).isNotEqualTo(b);
		assertThat(a.hashCode()).isNotEqualTo(b.hashCode());
	}

	@Test
	public void testUrlEquals() {
		AuthenticationUrl a = newUrl("id", "name", "url");
		AuthenticationUrl b = null;
		assertThat(a).isNotEqualTo(b);
		assertThat(a).isNotEqualTo(new Object());

		b = a;
		assertThat(a).isEqualTo(b);
		assertThat(a.hashCode()).isEqualTo(b.hashCode());

		b = newUrl("id", "name", "url");
		assertThat(a).isEqualTo(b);
		assertThat(a.hashCode()).isEqualTo(b.hashCode());

		b = newUrl("id2", "name", "url");
		assertThat(a).isNotEqualTo(b);
		assertThat(a.hashCode()).isNotEqualTo(b.hashCode());

		b = newUrl("id", "name2", "url");
		assertThat(a).isNotEqualTo(b);
		assertThat(a.hashCode()).isNotEqualTo(b.hashCode());

		b = newUrl("id", "name", "url3");
		assertThat(a).isNotEqualTo(b);
		assertThat(a.hashCode()).isNotEqualTo(b.hashCode());
	}

	@Test
	public void testToString() {
		List<AuthenticationUrl> urls = Arrays.asList(newUrl("id", "name", "url"), newUrl("id2", "name2", "url2"));
		AuthenticationUrls a = new AuthenticationUrls(urls);
		assertThat(a.toString()).isEqualTo(
				"AuthenticationUrls{authenticationUrls=[AuthenticationUrl{id=id, name=name, url=url}, AuthenticationUrl{id=id2, name=name2, url=url2}]}");
	}

	private AuthenticationUrl newUrl(String a, String b, String c) {
		return new AuthenticationUrl(a, b, c);
	}

}
