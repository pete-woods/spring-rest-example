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

import java.util.UUID;

import org.springframework.data.domain.Sort;

import com.surevine.springrestexample.api.beer.CreateBeerRequest;
import com.surevine.springrestexample.api.beer.BeerListResponse;
import com.surevine.springrestexample.api.beer.BeerResponse;
import com.surevine.springrestexample.api.beer.BeerSearchRequest;
import com.surevine.springrestexample.api.beer.UpdateBeerRequest;

public interface BeerService {

	BeerListResponse findAll(Sort sort);

	BeerResponse findById(UUID id);

	BeerResponse create(CreateBeerRequest request);

	BeerResponse update(UUID id, UpdateBeerRequest request);

	void deleteById(UUID id);

	BeerListResponse search(BeerSearchRequest request, Sort sort);
}
