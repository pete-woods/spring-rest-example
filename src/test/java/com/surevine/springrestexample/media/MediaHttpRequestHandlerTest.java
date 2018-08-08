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

package com.surevine.springrestexample.media;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import com.google.common.collect.ImmutableMap;
import com.surevine.springrestexample.api.ResourceStatus;
import com.surevine.springrestexample.api.media.MediaResponse;
import com.surevine.springrestexample.entity.Media;
import com.surevine.springrestexample.media.MediaHttpRequestHandler;
import com.surevine.springrestexample.media.RequestAttributesLoader;
import com.surevine.springrestexample.service.MediaService;

public class MediaHttpRequestHandlerTest {

	private final MediaService mediaService = mock(MediaService.class);

	private final RequestAttributesLoader requestAttributesLoader = mock(RequestAttributesLoader.class);

	private final MediaHttpRequestHandler handler = new MediaHttpRequestHandler(mediaService, requestAttributesLoader);

	private static class FakeRequestAttributes implements RequestAttributes {

		public Map<String, Object> attributes = new HashMap<>();

		@Override
		public Object getAttribute(String name, int scope) {
			return attributes.get(scope + ":" + name);
		}

		@Override
		public void setAttribute(String name, Object value, int scope) {
			attributes.put(scope + ":" + name, value);
		}

		@Override
		public void removeAttribute(String name, int scope) {
			throw new UnsupportedOperationException();
		}

		@Override
		public String[] getAttributeNames(int scope) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void registerDestructionCallback(String name, Runnable callback, int scope) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Object resolveReference(String key) {
			throw new UnsupportedOperationException();
		}

		@Override
		public String getSessionId() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Object getSessionMutex() {
			throw new UnsupportedOperationException();
		}

	}

	@Test
	public void test() throws IOException {
		UUID id = UUID.fromString("d76f9a46-307b-444a-a024-9f1dfe250aa9");

		Resource resource = mock(Resource.class);
		when(resource.exists()).thenReturn(true);

		MediaResponse mediaResponse = new MediaResponse(new Media(), ResourceStatus.OK);
		when(mediaService.findById(id)).thenReturn(mediaResponse);
		when(mediaService.findResource(mediaResponse.getData())).thenReturn(resource);

		FakeRequestAttributes requestAttributes = new FakeRequestAttributes();
		when(requestAttributesLoader.getRequestAttributes()).thenReturn(requestAttributes);

		MockHttpServletRequest request = new MockHttpServletRequest();
		ImmutableMap<String, Object> pathVariables = new ImmutableMap.Builder<String, Object>()
				.put("key", id.toString()).build();
		request.setAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, pathVariables);
		assertThat(handler.getResource(request)).isSameAs(resource);

		assertThat(requestAttributes.attributes).isEqualTo(new ImmutableMap.Builder<String, Object>()
				.put("0:" + MediaHttpRequestHandler.MEDIA_ATTRIBUTE, mediaResponse.getData()).build());
	}

}
