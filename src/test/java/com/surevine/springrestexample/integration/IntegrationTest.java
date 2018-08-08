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

package com.surevine.springrestexample.integration;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import com.surevine.springrestexample.api.beer.BeerResponse;
import com.surevine.springrestexample.api.beer.CreateBeerRequest;
import com.surevine.springrestexample.api.beer.UpdateBeerRequest;
import com.surevine.springrestexample.api.brewery.BreweryResponse;
import com.surevine.springrestexample.api.brewery.CreateBreweryRequest;
import com.surevine.springrestexample.api.brewery.UpdateBreweryRequest;
import com.surevine.springrestexample.api.media.CreateMediaRequest;
import com.surevine.springrestexample.api.media.MediaResponse;
import com.surevine.springrestexample.controller.BeerController;
import com.surevine.springrestexample.controller.BreweryController;
import com.surevine.springrestexample.controller.admin.BeerAdminController;
import com.surevine.springrestexample.controller.admin.BreweryAdminController;
import com.surevine.springrestexample.controller.admin.MediaAdminController;
import com.surevine.springrestexample.entity.Beer;
import com.surevine.springrestexample.entity.Brewery;
import com.surevine.springrestexample.entity.User;
import com.surevine.springrestexample.repository.UserRepository;
import com.surevine.springrestexample.service.ResourceMultipartFile;
import com.surevine.springrestexample.support.TestSupport;

public class IntegrationTest extends AbstractIntegrationTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BeerController beerController;

	@Autowired
	private BeerAdminController beerAdminController;

	@Autowired
	private MediaAdminController mediaAdminController;

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	private BreweryController breweryController;

	@Autowired
	private BreweryAdminController breweryAdminController;

	private Authentication authentication;

	private ResponseEntity<BeerResponse> beerResponseEntity;

	@Before
	public void setUpAuthentication() throws IOException {
		authentication = TestSupport.newAuthenticationToken();
		userRepository.save(new User(authentication.getName()));
	}

	@Test
	public void showEmpty() throws IOException {
		Sort sort = mock(Sort.class);
		assertThat(breweryController.list(sort).getData().isEmpty()).isTrue();
		assertThat(beerController.list(sort).getData().isEmpty()).isTrue();
		assertThat(mediaAdminController.list().getData().isEmpty()).isTrue();
	}

	@Test
	public void createUpdateBreweryAndBeer() throws IOException {
		ResponseEntity<MediaResponse> breweryImageResponse = loadMedia("brewdog/brewdog-logo.png",
				"Brewdog");
		assertThat(breweryImageResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		CreateBreweryRequest createBreweryRequest = new CreateBreweryRequest();
		createBreweryRequest.setName("brewery name");
		createBreweryRequest.setDescription("brewery description");
		createBreweryRequest.setThumbnailId(breweryImageResponse.getBody().getData().getId());
		createBreweryRequest.setImageIds(asList(breweryImageResponse.getBody().getData().getId()));
		ResponseEntity<BreweryResponse> breweryResponseEntity = breweryAdminController.create(createBreweryRequest);
		assertThat(breweryResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		Brewery brewery = breweryResponseEntity.getBody().getData();
		assertThat(brewery.getVersion()).isEqualTo(0);

		UpdateBreweryRequest updateBreweryRequest = new UpdateBreweryRequest();
		updateBreweryRequest.setName("brewery name updated");
		updateBreweryRequest.setDescription("brewery description updated");
		updateBreweryRequest.setThumbnailId(breweryImageResponse.getBody().getData().getId());
		updateBreweryRequest.setImageIds(asList(breweryImageResponse.getBody().getData().getId()));
		updateBreweryRequest.setVersion(brewery.getVersion());
		breweryResponseEntity = breweryAdminController.update(brewery.getId(), updateBreweryRequest);
		assertThat(breweryResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		brewery = breweryResponseEntity.getBody().getData();
		assertThat(brewery.getName()).isEqualTo("brewery name updated");
		assertThat(brewery.getDescription()).isEqualTo("brewery description updated");

		Sort sort = mock(Sort.class);

		assertThat(breweryController.list(sort).getData().size()).isEqualTo(1);

		ResponseEntity<MediaResponse> thumbnailResponse = loadMedia(
				"brewdog/beers/punk-ipa/punk-ipa-thumbnail.jpg", "Punk IPA");
		assertThat(thumbnailResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		ResponseEntity<MediaResponse> image1Response = loadMedia("brewdog/beers/punk-ipa/punk-ipa-1.jpg",
				"Punk IPA 1");
		assertThat(image1Response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		assertThat(mediaAdminController.list().getData().size()).isEqualTo(3);

		CreateBeerRequest request = new CreateBeerRequest();
		request.setName("beer name");
		request.setDescription("beer description");
		request.setBreweryId(brewery.getId());
		request.setImageIds(asList(image1Response.getBody().getData().getId()));
		request.setThumbnailId(thumbnailResponse.getBody().getData().getId());
		request.setPerfectFor(asList("p1", "p2"));
		request.setAbv(58L);
		request.setIbu(1000L);
		beerResponseEntity = beerAdminController.create(request);
		assertThat(beerResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		Beer beer = beerResponseEntity.getBody().getData();
		assertThat(beer.getName()).isEqualTo("beer name");
		assertThat(beer.getDescription()).isEqualTo("beer description");
		assertThat(beer.getVersion()).isEqualTo(0);
		assertThat(beer.getAbv()).isEqualTo(58L);
		assertThat(beer.getIbu()).isEqualTo(1000L);

		List<Beer> beers = beerController.list(sort).getData();
		assertThat(beers.size()).isEqualTo(1);

		UpdateBeerRequest updateRequest = new UpdateBeerRequest();
		updateRequest.setName("beer name updated");
		updateRequest.setDescription("beer description updated");
		updateRequest.setBreweryId(brewery.getId());
		updateRequest.setThumbnailId(thumbnailResponse.getBody().getData().getId());
		updateRequest.setVersion(beer.getVersion());
		updateRequest.setAbv(68L);
		updateRequest.setIbu(3000L);
		beerResponseEntity = beerAdminController.update(beer.getId(), updateRequest);
		assertThat(beerResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		beer = beerResponseEntity.getBody().getData();
		assertThat(beer.getName()).isEqualTo("beer name updated");
		assertThat(beer.getDescription()).isEqualTo("beer description updated");
		assertThat(beer.getAbv()).isEqualTo(68L);
		assertThat(beer.getIbu()).isEqualTo(3000L);

		beerAdminController.delete(beer.getId());

		beers = beerController.list(sort).getData();
		assertThat(beers.isEmpty()).isTrue();

		breweryAdminController.delete(brewery.getId());
		assertThat(breweryController.list(sort).getData().isEmpty()).isTrue();
	}

	private ResponseEntity<MediaResponse> loadMedia(String name, String alt) {
		Resource resource = resourceLoader.getResource(String.format("classpath:initialSetup/%s", name));
		ResourceMultipartFile file = new ResourceMultipartFile(MediaType.IMAGE_PNG_VALUE, resource);
		CreateMediaRequest request = new CreateMediaRequest();
		request.setAlt(alt);
		return mediaAdminController.create(request, file);
	}
}
