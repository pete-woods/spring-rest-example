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

package com.surevine.springrestexample.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.junit.Test;

import com.surevine.springrestexample.entity.AuditedEntity;
import com.surevine.springrestexample.entity.User;

public class AuditedEntityTest {

	@SuppressWarnings("serial")
	private static class Foo extends AuditedEntity {
	}

	@Test
	public void test() {
		LocalDateTime now = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
		User user = new User();
		Foo foo = new Foo();
		foo.setCreatedAt(now);
		foo.setCreatedBy(user);
		foo.setUpdatedAt(now);
		foo.setUpdatedBy(user);

		assertThat(foo.getCreatedAt()).isSameAs(now);
		assertThat(foo.getCreatedBy()).isSameAs(user);
		assertThat(foo.getUpdatedAt()).isSameAs(now);
		assertThat(foo.getUpdatedBy()).isSameAs(user);

		assertThat(foo.toString()).isEqualTo(
				"Foo{id=null, createdAt=1970-01-01T00:00, createdBy=User{id=null, principalId=null}, updatedAt=1970-01-01T00:00, updatedBy=User{id=null, principalId=null}, version=null}");
	}

}
