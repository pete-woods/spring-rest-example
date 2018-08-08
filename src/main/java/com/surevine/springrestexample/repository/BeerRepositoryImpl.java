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

package com.surevine.springrestexample.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import com.querydsl.core.BooleanBuilder;
import com.surevine.springrestexample.api.beer.BeerSearchRequest;
import com.surevine.springrestexample.entity.Beer;
import com.surevine.springrestexample.entity.QBeer;

public class BeerRepositoryImpl implements BeerRepositoryCustom {

	@Autowired
	private BeerRepository beerRepository;

	public BeerRepositoryImpl() {
	}

	public BeerRepositoryImpl(BeerRepository beerRepository) {
		this.beerRepository = beerRepository;
	}

	@Override
	public List<Beer> beerSearch(BeerSearchRequest request, Sort sort) {
		QBeer beer = QBeer.beer;
		BooleanBuilder where = new BooleanBuilder();
		if (request.getBreweryId() != null) {
			where.and(beer.brewery.id.eq(request.getBreweryId()));
		}
		if (request.getAbvAtLeast() != null) {
			where.and(beer.abv.goe(request.getAbvAtLeast()));
		}
		if (request.getAbvAtMost() != null) {
			where.and(beer.abv.loe(request.getAbvAtMost()));
		}
		if (request.getIbuAtLeast() != null) {
			where.and(beer.ibu.goe(request.getIbuAtLeast()));
		}
		if (request.getIbuAtMost() != null) {
			where.and(beer.ibu.loe(request.getIbuAtMost()));
		}
		return beerRepository.findAll(where, sort);
	}

}
