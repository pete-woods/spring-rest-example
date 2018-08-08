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

import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.surevine.springrestexample.api.ResourceStatus;
import com.surevine.springrestexample.api.brewery.BreweryListResponse;
import com.surevine.springrestexample.api.brewery.BreweryResponse;
import com.surevine.springrestexample.api.brewery.CreateBreweryRequest;
import com.surevine.springrestexample.api.brewery.UpdateBreweryRequest;
import com.surevine.springrestexample.entity.Brewery;
import com.surevine.springrestexample.repository.BreweryRepository;
import com.surevine.springrestexample.repository.MediaRepository;
import com.surevine.springrestexample.service.BreweryService;

@Service
public class BreweryServiceImpl implements BreweryService {

	private final BreweryRepository breweryRepository;

	private final MediaRepository mediaRepository;

	public BreweryServiceImpl(BreweryRepository breweryRepository, MediaRepository mediaRepository) {
		this.breweryRepository = breweryRepository;
		this.mediaRepository = mediaRepository;
	}

	@Transactional(readOnly = true)
	@Override
	public BreweryListResponse findAll(Sort sort) {
		return new BreweryListResponse(breweryRepository.findAll(sort));
	}

	@Transactional(readOnly = true)
	@Override
	public BreweryResponse findById(UUID id) {
		return breweryRepository.findById(id).map(brewery -> new BreweryResponse(brewery, ResourceStatus.OK))
				.orElse(new BreweryResponse(null, ResourceStatus.NOT_FOUND));
	}

	@Transactional
	@Override
	public BreweryResponse create(CreateBreweryRequest request) {
		Brewery brewery = Brewery.from(request, mediaRepository);
		return new BreweryResponse(breweryRepository.save(brewery), ResourceStatus.CREATED);
	}

	@Transactional
	@Override
	public BreweryResponse update(UUID id, UpdateBreweryRequest request) {
		boolean alreadyExists = breweryRepository.existsById(id);
		Brewery brewery = Brewery.from(id, request, mediaRepository);
		return new BreweryResponse(breweryRepository.save(brewery),
				alreadyExists ? ResourceStatus.OK : ResourceStatus.CREATED);
	}

	@Transactional
	@Override
	public void deleteById(UUID id) {
		breweryRepository.deleteById(id);
	}

}
