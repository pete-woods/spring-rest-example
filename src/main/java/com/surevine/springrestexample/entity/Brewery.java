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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedEntityGraph;

import com.google.common.base.MoreObjects.ToStringHelper;
import com.surevine.springrestexample.api.brewery.AbstractBreweryRequest;
import com.surevine.springrestexample.api.brewery.CreateBreweryRequest;
import com.surevine.springrestexample.api.brewery.UpdateBreweryRequest;
import com.surevine.springrestexample.repository.MediaRepository;

@Entity
@NamedEntityGraph(name = "Brewery.full", includeAllAttributes = true)
@SuppressWarnings("serial")
public class Brewery extends AuditedEntity {

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String description;

	@JoinColumn(nullable = false)
	@ManyToOne
	private Media thumbnail;

	@ManyToMany
	@JoinTable(name = "brewery_images", joinColumns = @JoinColumn(name = "brewery_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "media_id", referencedColumnName = "id"))
	private Set<Media> images;

	public static Brewery from(CreateBreweryRequest request, MediaRepository mediaRepository) {
		return builder(request, mediaRepository).build();
	}

	public static Brewery from(UUID id, UpdateBreweryRequest request, MediaRepository mediaRepository) {
		return builder(request, mediaRepository).id(id).version(request.getVersion()).build();
	}

	private static Builder builder(AbstractBreweryRequest request, MediaRepository mediaRepository) {
		return new Builder().name(request.getName()).description(request.getDescription())
				.thumbnail(mediaRepository.getOne(request.getThumbnailId()))
				.images(mediaRepository.findAllById(request.getImageIds()));
	}

	public Brewery() {
	}

	@Override
	protected ToStringHelper toStringHelper() {
		return super.toStringHelper().add("name", name).add("description", description).add("thumbnail", thumbnail)
				.add("images", images);

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

	public Media getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(Media thumbnail) {
		this.thumbnail = thumbnail;
	}

	public Set<Media> getImages() {
		return images;
	}

	public void setImages(Set<Media> images) {
		this.images = images;
	}

	public static class Builder {
		private final Brewery brewery;

		public Builder() {
			brewery = new Brewery();
		}

		public Brewery build() {
			return brewery;
		}

		public Builder id(UUID id) {
			brewery.setId(id);
			return this;
		}

		public Builder version(Long version) {
			brewery.setVersion(version);
			return this;
		}

		public Builder name(String name) {
			brewery.setName(name);
			return this;
		}

		public Builder description(String description) {
			brewery.setDescription(description);
			return this;
		}

		public Builder thumbnail(Media thumbnail) {
			brewery.setThumbnail(thumbnail);
			return this;
		}

		public Builder images(List<Media> images) {
			if (images != null) {
				brewery.setImages(new HashSet<>(images));
			}
			return this;
		}
	}
}
