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


package com.surevine.springrestexample.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

public class ResourceMultipartFile implements MultipartFile {

	private String contentType;

	private final Resource resource;

	public ResourceMultipartFile(String contentType, Resource resource) {
		Assert.hasLength(contentType, "contentType must not be empty");
		Assert.notNull(resource, "resource must not be null");
		this.contentType = contentType;
		this.resource = resource;
	}

	@Override
	public String getName() {
		return resource.getDescription();
	}

	@Override
	public String getOriginalFilename() {
		return resource.getFilename();
	}

	@Override
	@Nullable
	public String getContentType() {
		return contentType;
	}

	@Override
	public boolean isEmpty() {
		return resource.exists();
	}

	@Override
	public long getSize() {
		try {
			return resource.contentLength();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return resource.getInputStream();
	}

	@Override
	public byte[] getBytes() throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void transferTo(File dest) throws IOException, IllegalStateException {
		throw new UnsupportedOperationException();
	}

}
