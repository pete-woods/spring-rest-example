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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.UUID;

import org.junit.Test;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

import com.surevine.springrestexample.api.ResourceStatus;
import com.surevine.springrestexample.api.brewery.BreweryListResponse;
import com.surevine.springrestexample.api.brewery.BreweryResponse;
import com.surevine.springrestexample.entity.Brewery;
import com.surevine.springrestexample.service.BreweryService;

public class BreweryControllerTest {

	private final BreweryService breweryService = mock(BreweryService.class);

	private BreweryController breweryController = new BreweryController(breweryService);

	@Test
	public void listBreweries() {
		BreweryListResponse result = new BreweryListResponse(
				Collections.singletonList(new Brewery.Builder().name("test").build()));
		Sort sort = mock(Sort.class);
		when(breweryService.findAll(sort)).thenReturn(result);
		BreweryListResponse response = breweryController.list(sort);
		assertEquals(1, response.getData().size());
	}

	@Test
	public void showBrewery() {
		Brewery expected = new Brewery.Builder().id(UUID.randomUUID()).name("expected").build();
		BreweryResponse result = new BreweryResponse(expected, ResourceStatus.OK);

		when(breweryService.findById(expected.getId())).thenReturn(result);
		ResponseEntity<BreweryResponse> response = breweryController.show(expected.getId());
		assertEquals(expected.getId(), response.getBody().getData().getId());
	}
}
