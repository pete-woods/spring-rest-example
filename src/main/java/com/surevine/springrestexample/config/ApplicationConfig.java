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

package com.surevine.springrestexample.config;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

import com.surevine.springrestexample.ApplicationConstants;
import com.surevine.springrestexample.entity.User;
import com.surevine.springrestexample.media.MediaHttpRequestHandler;
import com.surevine.springrestexample.media.RequestAttributesLoader;
import com.surevine.springrestexample.oidc.OAuth2AuditorAware;
import com.surevine.springrestexample.repository.UserRepository;
import com.surevine.springrestexample.service.MediaService;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class ApplicationConfig {

	@Bean
	public AuditorAware<User> auditorAware(UserRepository userRepository) {
		return new OAuth2AuditorAware(userRepository, new SecurityContextProvider() {
		});
	}

	@Bean
	public SimpleUrlHandlerMapping mediaUrlHandlerMapping(ApplicationContext applicationContext,
			ServletContext servletContext, ContentNegotiationManager contentNegotiationManager,
			MediaService mediaService, ResourceProperties resourceProperties) {
		MediaHttpRequestHandler handler = new MediaHttpRequestHandler(mediaService, new RequestAttributesLoader() {
		});

		handler.setServletContext(servletContext);
		handler.setApplicationContext(applicationContext);
		handler.setContentNegotiationManager(contentNegotiationManager);
		handler.setCacheControl(resourceProperties.getCache().getCachecontrol().toHttpCacheControl());

		try {
			handler.afterPropertiesSet();
		} catch (Exception e) {
			throw new BeanInitializationException("Failed to init MediaHttpRequestHandler", e);
		}

		Map<String, HttpRequestHandler> urlMap = new LinkedHashMap<>();
		urlMap.put(ApplicationConstants.API_PREFIX + "media/{key}", handler);

		SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
		handlerMapping.setOrder(0);
		handlerMapping.setUrlMap(urlMap);
		return handlerMapping;
	}

}
