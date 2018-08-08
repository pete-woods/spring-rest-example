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

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.util.Assert;

import com.google.common.base.MoreObjects;

public class AuthenticationUrls {

	private final List<AuthenticationUrl> authenticationUrls;

	public AuthenticationUrls(List<AuthenticationUrl> authenticationUrls) {
		Assert.notNull(authenticationUrls, "authenticationUrls cannot be null");
		this.authenticationUrls = Collections.unmodifiableList(authenticationUrls);
	}

	public static final class AuthenticationUrl {
		private final String id;

		private final String name;

		private final String url;

		public AuthenticationUrl(String id, String name, String url) {
			this.id = id;
			this.name = name;
			this.url = url;
		}

		public String getName() {
			return name;
		}

		public String getUrl() {
			return url;
		}

		public String getId() {
			return id;
		}

		@Override
		public int hashCode() {
			return Objects.hash(id, name, url);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final AuthenticationUrl other = (AuthenticationUrl) obj;
			return Objects.equals(id, other.id) && Objects.equals(name, other.name) && Objects.equals(url, other.url);
		}

		@Override
		public String toString() {
			return MoreObjects.toStringHelper(this).add("id", id).add("name", name).add("url", url).toString();
		}
	}

	public List<AuthenticationUrl> getAuthenticationUrls() {
		return authenticationUrls;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final AuthenticationUrls other = (AuthenticationUrls) obj;
		return Objects.equals(authenticationUrls, other.authenticationUrls);
	}

	@Override
	public int hashCode() {
		return Objects.hash(authenticationUrls);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("authenticationUrls", authenticationUrls).toString();
	}
}
