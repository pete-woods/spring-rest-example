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
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.surevine.springrestexample.api.beer.AbstractBeerRequest;
import com.surevine.springrestexample.api.beer.CreateBeerRequest;
import com.surevine.springrestexample.api.beer.UpdateBeerRequest;
import com.surevine.springrestexample.repository.BreweryRepository;
import com.surevine.springrestexample.repository.MediaRepository;

@Entity
@NamedEntityGraphs({ @NamedEntityGraph(name = "Beer.full", includeAllAttributes = true),
		@NamedEntityGraph(name = "Beer.search", attributeNodes = { @NamedAttributeNode("createdBy"),
				@NamedAttributeNode("updatedBy"), @NamedAttributeNode("perfectFor"), @NamedAttributeNode("thumbnail"),
				@NamedAttributeNode("images") }) })
@SuppressWarnings("serial")
public class Beer extends AuditedEntity {
	@Column(nullable = false)
	private String name;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String description;

	@JoinColumn(nullable = false)
	@ManyToOne
	@JsonIgnore
	private Brewery brewery;

	@ElementCollection
	private Set<String> perfectFor;

	@JoinColumn(nullable = false)
	@ManyToOne
	private Media thumbnail;

	@Column(nullable = false)
	private long abv;

	@Column(nullable = false)
	private long ibu;

	@ManyToMany
	@JoinTable(name = "beer_images", joinColumns = @JoinColumn(name = "beer_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "media_id", referencedColumnName = "id"))
	private Set<Media> images;

	public static Beer from(CreateBeerRequest request, BreweryRepository breweryRepository,
			MediaRepository mediaRepository) {
		return builder(request, breweryRepository, mediaRepository).build();
	}

	public static Beer from(UUID id, UpdateBeerRequest request, BreweryRepository breweryRepository,
			MediaRepository mediaRepository) {
		return builder(request, breweryRepository, mediaRepository).id(id).version(request.getVersion()).build();
	}

	private static Builder builder(AbstractBeerRequest request, BreweryRepository breweryRepository,
			MediaRepository mediaRepository) {
		Builder builder = new Beer.Builder().name(request.getName()).description(request.getDescription())
				.brewery(breweryRepository.getOne(request.getBreweryId())).perfectFor(request.getPerfectFor())
				.thumbnail(mediaRepository.getOne(request.getThumbnailId())).abv(request.getAbv())
				.ibu(request.getIbu());
		if (request.getImageIds() != null) {
			builder.images(request.getImageIds().stream().map(imageId -> mediaRepository.getOne(imageId))
					.collect(Collectors.toList()));
		}
		return builder;
	}

	public Beer() {
	}

	@JsonProperty("breweryId")
	public UUID getBreweryId() {
		return brewery.getId();
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

	public Brewery getBrewery() {
		return brewery;
	}

	public void setBrewery(Brewery brewery) {
		this.brewery = brewery;
	}

	public Set<String> getPerfectFor() {
		return perfectFor;
	}

	public void setPerfectFor(Set<String> perfectFor) {
		this.perfectFor = perfectFor;
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

	public long getAbv() {
		return abv;
	}

	public void setAbv(long abv) {
		this.abv = abv;
	}

	public long getIbu() {
		return ibu;
	}

	public void setIbu(long ibu) {
		this.ibu = ibu;
	}

	@Override
	protected ToStringHelper toStringHelper() {
		return super.toStringHelper().add("name", name).add("description", description).add("brewery", brewery)
				.add("perfectFor", perfectFor).add("thumbnail", thumbnail).add("images", images).add("abv", abv)
				.add("ibu", ibu);
	}

	public static class Builder {
		private final Beer beer;

		public Builder() {
			beer = new Beer();
		}

		public Beer build() {
			return beer;
		}

		public Builder id(UUID id) {
			beer.setId(id);
			return this;
		}

		public Builder name(String name) {
			beer.setName(name);
			return this;
		}

		public Builder version(Long version) {
			beer.setVersion(version);
			return this;
		}

		public Builder description(String description) {
			beer.setDescription(description);
			return this;
		}

		public Builder brewery(Brewery brewery) {
			beer.setBrewery(brewery);
			return this;
		}

		public Builder perfectFor(List<String> perfectFor) {
			if (perfectFor != null) {
				beer.setPerfectFor(new HashSet<String>(perfectFor));
			}
			return this;
		}

		public Builder thumbnail(Media thumbnail) {
			beer.setThumbnail(thumbnail);
			return this;
		}

		public Builder images(List<Media> images) {
			if (images != null) {
				beer.setImages(new HashSet<Media>(images));
			}
			return this;
		}

		public Builder abv(long abv) {
			beer.setAbv(abv);
			return this;
		}

		public Builder ibu(long ibu) {
			beer.setIbu(ibu);
			return this;
		}
	}

}
