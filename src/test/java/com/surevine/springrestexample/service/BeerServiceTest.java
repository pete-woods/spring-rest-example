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

package com.surevine.springrestexample.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Test;
import org.springframework.data.domain.Sort;

import com.surevine.springrestexample.api.beer.BeerListResponse;
import com.surevine.springrestexample.api.beer.BeerResponse;
import com.surevine.springrestexample.entity.Beer;
import com.surevine.springrestexample.repository.BeerRepository;
import com.surevine.springrestexample.repository.BreweryRepository;
import com.surevine.springrestexample.repository.MediaRepository;
import com.surevine.springrestexample.service.impl.BeerServiceImpl;

public class BeerServiceTest {

	private final BeerRepository beerRepository = mock(BeerRepository.class);

	private final BreweryRepository breweryRepository = mock(BreweryRepository.class);

	private final MediaRepository mediaRepository = mock(MediaRepository.class);

	private final BeerService beerService = new BeerServiceImpl(beerRepository, breweryRepository, mediaRepository);

	@Test
	public void testFindAll() {
		List<Beer> result = Collections.singletonList(new Beer.Builder().name("bar").build());
		Sort sort = mock(Sort.class);
		when(beerRepository.findAll(sort)).thenReturn(result);
		BeerListResponse response = beerService.findAll(sort);
		assertThat(response.getData()).isEqualTo(result);
	}

	@Test
	public void testFindById() {
		UUID id = UUID.randomUUID();
		Optional<Beer> result = Optional.of(new Beer.Builder().name("foo").build());
		when(beerRepository.findById(id)).thenReturn(result);
		BeerResponse response = beerService.findById(id);
		assertThat(response.getData()).isEqualTo(result.get());
	}
}
