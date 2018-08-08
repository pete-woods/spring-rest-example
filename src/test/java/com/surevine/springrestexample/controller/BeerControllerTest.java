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

package com.surevine.springrestexample.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.google.common.collect.ImmutableMap;
import com.surevine.springrestexample.api.ResourceStatus;
import com.surevine.springrestexample.api.beer.BeerListResponse;
import com.surevine.springrestexample.api.beer.BeerResponse;
import com.surevine.springrestexample.api.beer.BeerSearchRequest;
import com.surevine.springrestexample.api.beer.CreateBeerRequest;
import com.surevine.springrestexample.api.beer.UpdateBeerRequest;
import com.surevine.springrestexample.entity.Beer;
import com.surevine.springrestexample.service.BeerService;

public class BeerControllerTest {

	private static class FakeBeerService implements BeerService {
		private final static UUID ID1 = UUID.fromString("d76f9a46-307b-444a-a024-9f1dfe250aa9");

		private final static UUID ID2 = UUID.fromString("d76f9a46-307b-444a-a024-9f1dfe250aaa");

		private final static Beer beer1 = new Beer.Builder().name("Beer 1").id(ID1).build();

		private final static Beer beer2 = new Beer.Builder().name("Beer 2").id(ID2).build();

		private final static Map<UUID, Beer> beers = new ImmutableMap.Builder<UUID, Beer>().put(ID1, beer1)
				.put(ID2, beer2).build();

		@Override
		public BeerListResponse findAll(Sort sort) {
			return new BeerListResponse(new ArrayList<>(beers.values()), ResourceStatus.OK);
		}

		@Override
		public BeerResponse findById(UUID id) {
			return beers.containsKey(id) ? new BeerResponse(beers.get(id), ResourceStatus.OK)
					: new BeerResponse(null, ResourceStatus.NOT_FOUND);
		}

		@Override
		public BeerResponse create(CreateBeerRequest request) {
			throw new UnsupportedOperationException();
		}

		@Override
		public BeerResponse update(UUID id, UpdateBeerRequest request) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void deleteById(UUID id) {
			throw new UnsupportedOperationException();
		}

		@Override
		public BeerListResponse search(BeerSearchRequest request, Sort sort) {
			throw new UnsupportedOperationException();
		}

	}

	private BeerService beerService = new FakeBeerService();

	private BeerController beerController = new BeerController(beerService);

	@Test
	public void testList() {
		List<Beer> data = beerController.list(mock(Sort.class)).getData();
		assertThat(data.size()).isEqualTo(2);
	}

	@Test
	public void testGetBeerFound1() {
		ResponseEntity<BeerResponse> responseEntity = beerController.show(FakeBeerService.ID1);
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		BeerResponse response = responseEntity.getBody();
		assertThat(response.getData()).isEqualTo(FakeBeerService.beer1);
	}

	@Test
	public void testGetBeerFound2() {
		ResponseEntity<BeerResponse> responseEntity = beerController.show(FakeBeerService.ID2);
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		BeerResponse response = responseEntity.getBody();
		assertThat(response.getData()).isEqualTo(FakeBeerService.beer2);
	}

	@Test
	public void testGetBeerNotFound() {
		ResponseEntity<BeerResponse> responseEntity = beerController.show(UUID.randomUUID());
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

}
