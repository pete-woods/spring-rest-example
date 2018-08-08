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

package com.surevine.springrestexample.api.beer;

import java.util.UUID;;

public class BeerSearchRequest {
	private UUID breweryId;

	private Long abvAtLeast;

	private Long abvAtMost;

	private Long ibuAtLeast;

	private Long ibuAtMost;

	public void validate() {
	}

	public UUID getBreweryId() {
		return breweryId;
	}

	public BeerSearchRequest setBreweryId(UUID breweryId) {
		this.breweryId = breweryId;
		return this;
	}

	public Long getAbvAtLeast() {
		return abvAtLeast;
	}

	public void setAbvAtLeast(Long abvAtLeast) {
		this.abvAtLeast = abvAtLeast;
	}

	public Long getAbvAtMost() {
		return abvAtMost;
	}

	public void setAbvAtMost(Long abvAtMost) {
		this.abvAtMost = abvAtMost;
	}

	public Long getIbuAtLeast() {
		return ibuAtLeast;
	}

	public void setIbuAtLeast(Long ibuAtLeast) {
		this.ibuAtLeast = ibuAtLeast;
	}

	public Long getIbuAtMost() {
		return ibuAtMost;
	}

	public void setIbuAtMost(Long ibuAtMost) {
		this.ibuAtMost = ibuAtMost;
	}

}
