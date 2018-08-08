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

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.junit.Test;

public class BeerTest {

	@Test
	public void test() {
		UUID id = UUID.fromString("d76f9a46-307b-444a-a024-9f1dfe250aa9");
		String name = "name";
		String description = "description";
		Brewery brewery = new Brewery.Builder().name("brewery-name").build();
		List<String> perfectFor = asList("c", "d");
		Media thumbnail = new Media();
		thumbnail.setAlt("alt-thumbnail");
		thumbnail.setSortHint(1L);
		Media image1 = new Media();
		image1.setAlt("image1-thumbnail");
		List<Media> images = asList(image1);

		Beer beer = new Beer.Builder().name(name).id(id).description(description).brewery(brewery)
				.perfectFor(perfectFor).thumbnail(thumbnail).images(images).build();

		assertThat(beer.getId()).isEqualTo(id);
		assertThat(beer.getName()).isEqualTo(name);
		assertThat(beer.getDescription()).isEqualTo(description);
		assertThat(beer.getBrewery()).isSameAs(brewery);
		assertThat(beer.getPerfectFor()).isEqualTo(new HashSet<>(perfectFor));
		assertThat(beer.getThumbnail()).isSameAs(thumbnail);
		assertThat(beer.getImages().contains(image1)).isTrue();

		assertThat(beer.toString()).isEqualTo(
				"Beer{id=d76f9a46-307b-444a-a024-9f1dfe250aa9, createdAt=null, createdBy=null, updatedAt=null, updatedBy=null, version=null, name=name, description=description, brewery=Brewery{id=null, createdAt=null, createdBy=null, updatedAt=null, updatedBy=null, version=null, name=brewery-name, description=null, thumbnail=null, images=null}, perfectFor=[c, d], thumbnail=Media{id=null, createdAt=null, createdBy=null, updatedAt=null, updatedBy=null, version=null, alt=alt-thumbnail, mimeType=null, originalName=null, sortHint=1}, images=[Media{id=null, createdAt=null, createdBy=null, updatedAt=null, updatedBy=null, version=null, alt=image1-thumbnail, mimeType=null, originalName=null, sortHint=null}], abv=0, ibu=0}");
	}
}
