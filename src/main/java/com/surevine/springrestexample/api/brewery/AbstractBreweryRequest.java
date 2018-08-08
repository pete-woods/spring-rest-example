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

package com.surevine.springrestexample.api.brewery;

import java.util.List;
import java.util.UUID;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.surevine.springrestexample.api.AbstractRequest;

public abstract class AbstractBreweryRequest extends AbstractRequest {

	private String name;

	private String description;

	private UUID thumbnailId;

	private List<UUID> imageIds;

	@Override
	protected ToStringHelper toStringHelper() {
		return MoreObjects.toStringHelper(this).add("name", name).add("description", description)
				.add("thumbnailId", thumbnailId).add("imageIds", imageIds);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public UUID getThumbnailId() {
		return thumbnailId;
	}

	public void setThumbnailId(UUID thumbnailId) {
		this.thumbnailId = thumbnailId;
	}

	public List<UUID> getImageIds() {
		return imageIds;
	}

	public void setImageIds(List<UUID> imageIds) {
		this.imageIds = imageIds;
	}
}
