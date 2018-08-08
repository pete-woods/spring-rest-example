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

package com.surevine.springrestexample.controller.admin;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.surevine.springrestexample.ApplicationConstants;
import com.surevine.springrestexample.api.beer.BeerResponse;
import com.surevine.springrestexample.api.beer.CreateBeerRequest;
import com.surevine.springrestexample.api.beer.UpdateBeerRequest;
import com.surevine.springrestexample.service.BeerService;

@RestController
@RequestMapping(ApplicationConstants.API_PREFIX + "admin/beers")
public final class BeerAdminController {
	private BeerService beerService;

	public BeerAdminController(BeerService beerService) {
		this.beerService = beerService;
	}

	@PostMapping
	public ResponseEntity<BeerResponse> create(@RequestBody CreateBeerRequest request) {
		BeerResponse response = beerService.create(request);
		return new ResponseEntity<>(response, response.getStatus().httpStatus);
	}

	@PutMapping("/{id}")
	public ResponseEntity<BeerResponse> update(@PathVariable UUID id, @RequestBody UpdateBeerRequest request) {
		BeerResponse response = beerService.update(id, request);
		return new ResponseEntity<>(response, response.getStatus().httpStatus);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable UUID id) {
		beerService.deleteById(id);
	}

}
