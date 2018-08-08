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

package com.surevine.springrestexample.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.surevine.springrestexample.api.ResourceStatus;
import com.surevine.springrestexample.api.beer.CreateBeerRequest;
import com.surevine.springrestexample.api.beer.BeerListResponse;
import com.surevine.springrestexample.api.beer.BeerResponse;
import com.surevine.springrestexample.api.beer.BeerSearchRequest;
import com.surevine.springrestexample.api.beer.UpdateBeerRequest;
import com.surevine.springrestexample.entity.Beer;
import com.surevine.springrestexample.repository.BreweryRepository;
import com.surevine.springrestexample.repository.MediaRepository;
import com.surevine.springrestexample.repository.BeerRepository;
import com.surevine.springrestexample.service.BeerService;

@Service
public class BeerServiceImpl implements BeerService {

	private final BeerRepository beerRepository;

	private final BreweryRepository breweryRepository;

	private final MediaRepository mediaRepository;

	public BeerServiceImpl(BeerRepository beerRepository, BreweryRepository breweryRepository,
			MediaRepository mediaRepository) {
		this.beerRepository = beerRepository;
		this.breweryRepository = breweryRepository;
		this.mediaRepository = mediaRepository;
	}

	@Transactional(readOnly = true)
	@Override
	public BeerListResponse findAll(Sort sort) {
		return new BeerListResponse(beerRepository.findAll(sort), ResourceStatus.OK);
	}

	@Transactional(readOnly = true)
	@Override
	public BeerListResponse search(BeerSearchRequest request, Sort sort) {
		request.validate();
		List<Beer> results = beerRepository.beerSearch(request, sort);
		return new BeerListResponse(results, ResourceStatus.OK);
	}

	@Transactional(readOnly = true)
	@Override
	public BeerResponse findById(UUID id) {
		return beerRepository.findById(id).map(beer -> new BeerResponse(beer, ResourceStatus.OK))
				.orElse(new BeerResponse(null, ResourceStatus.NOT_FOUND));
	}

	@Transactional
	@Override
	public BeerResponse create(CreateBeerRequest request) {
		Beer beer = Beer.from(request, breweryRepository, mediaRepository);
		return new BeerResponse(beerRepository.save(beer), ResourceStatus.CREATED);
	}

	@Transactional
	@Override
	public BeerResponse update(UUID id, UpdateBeerRequest request) {
		boolean alreadyExists = beerRepository.existsById(id);
		Beer beer = Beer.from(id, request, breweryRepository, mediaRepository);
		return new BeerResponse(beerRepository.save(beer), alreadyExists ? ResourceStatus.OK : ResourceStatus.CREATED);
	}

	@Transactional
	@Override
	public void deleteById(UUID id) {
		beerRepository.deleteById(id);
	}

}
