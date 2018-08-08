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

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import com.surevine.springrestexample.api.media.MediaResponse;
import com.surevine.springrestexample.entity.Media;
import com.surevine.springrestexample.service.MediaService;

public class MediaHttpRequestHandler extends ResourceHttpRequestHandler implements InitializingBean {
	public static final String MEDIA_ATTRIBUTE = MediaHttpRequestHandler.class.getName() + ".MEDIA";

	private final MediaService mediaService;

	private final RequestAttributesLoader requestAttributesLoader;

	public MediaHttpRequestHandler(MediaService mediaService, RequestAttributesLoader requestAttributesLoader) {
		this.mediaService = mediaService;
		this.requestAttributesLoader = requestAttributesLoader;
	}

	@Override
	protected Resource getResource(HttpServletRequest request) throws IOException {
		@SuppressWarnings("unchecked")
		Map<String, Object> pathVariables = (Map<String, Object>) request
				.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		UUID id = UUID.fromString((String) pathVariables.get("key"));

		MediaResponse response = mediaService.findById(id);
		if (response.getData() == null) {
			return null;
		}

		Media media = response.getData();
		requestAttributesLoader.getRequestAttributes().setAttribute(MEDIA_ATTRIBUTE, media,
				RequestAttributes.SCOPE_REQUEST);

		Resource resource = mediaService.findResource(media);
		return resource.exists() ? resource : null;
	}

	@Override
	protected MediaType getMediaType(HttpServletRequest request, Resource resource) {
		Media media = (Media) requestAttributesLoader.getRequestAttributes().getAttribute(MEDIA_ATTRIBUTE,
				RequestAttributes.SCOPE_REQUEST);
		return MediaType.parseMediaType(media.getMimeType());
	}

	@Override
	protected void setHeaders(HttpServletResponse response, Resource resource, MediaType mediaType) throws IOException {
		super.setHeaders(response, resource, mediaType);
		// Set content disposition for non-images
		if (!"image".equals(mediaType.getType())) {
			Media media = (Media) requestAttributesLoader.getRequestAttributes().getAttribute(MEDIA_ATTRIBUTE,
					RequestAttributes.SCOPE_REQUEST);
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
					"attachment; filename*=UTF-8''" + URLEncoder.encode(media.getOriginalName(), "UTF-8"));
		}
	}
}