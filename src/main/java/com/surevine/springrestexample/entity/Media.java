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

import javax.persistence.Column;
import javax.persistence.Entity;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.surevine.springrestexample.ApplicationConstants;
import com.surevine.springrestexample.api.media.CreateMediaRequest;

@Entity
@SuppressWarnings("serial")
public class Media extends AuditedEntity {
	@Column(nullable = false)
	private String alt;

	@JsonIgnore
	@Column(nullable = false, updatable = false)
	private String mimeType;

	@Column(length = 512, updatable = false)
	private String originalName;

	@Column
	private Long sortHint;

	public static Media from(CreateMediaRequest request, MultipartFile file) {
		Media media = new Media();
		media.setAlt(request.getAlt());
		media.setMimeType(file.getContentType());
		media.setOriginalName(file.getOriginalFilename());
		media.setSortHint(request.getSortHint());
		return media;
	}

	public Media() {
	}

	@Override
	protected ToStringHelper toStringHelper() {
		return super.toStringHelper().add("alt", alt).add("mimeType", mimeType).add("originalName", originalName)
				.add("sortHint", sortHint);
	}

	@JsonProperty("url")
	public String getUrl() {
		return String.format("%smedia/%s", ApplicationConstants.API_PREFIX, getId());
	}

	public String getAlt() {
		return alt;
	}

	public void setAlt(String alt) {
		this.alt = alt;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	public Long getSortHint() {
		return sortHint;
	}

	public void setSortHint(Long sortHint) {
		this.sortHint = sortHint;
	}
}
