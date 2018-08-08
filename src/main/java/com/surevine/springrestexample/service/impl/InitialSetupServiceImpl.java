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

import static java.util.Arrays.asList;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.surevine.springrestexample.api.media.CreateMediaRequest;
import com.surevine.springrestexample.api.media.MediaResponse;
import com.surevine.springrestexample.entity.Beer;
import com.surevine.springrestexample.entity.Brewery;
import com.surevine.springrestexample.entity.Media;
import com.surevine.springrestexample.repository.BeerRepository;
import com.surevine.springrestexample.repository.BreweryRepository;
import com.surevine.springrestexample.service.InitialSetupService;
import com.surevine.springrestexample.service.MediaService;
import com.surevine.springrestexample.service.ResourceMultipartFile;

@Service
public class InitialSetupServiceImpl implements InitialSetupService {

	private final BreweryRepository breweryRepository;

	private final BeerRepository beerRepository;

	private final ResourceLoader resourceLoader;

	private final MediaService mediaService;

	public InitialSetupServiceImpl(BreweryRepository breweryRepository, BeerRepository beerRepository,
			ResourceLoader resourceLoader, MediaService mediaService) {
		this.breweryRepository = breweryRepository;
		this.beerRepository = beerRepository;
		this.resourceLoader = resourceLoader;
		this.mediaService = mediaService;
	}

	@Transactional
	@Override
	public void setup() {
		beerRepository.deleteAll();
		breweryRepository.deleteAll();
		mediaService.deleteAll();

		String brewdogDir = "brewdog/";
		Media brewdogThumb = loadPng(brewdogDir + "brewdog-logo.png", "Brewdog");
		Media brewdogImage1 = loadJpg(brewdogDir + "brewdog-1.jpg", "Brewdog founders 1");
		Media brewdogImage2 = loadJpg(brewdogDir + "brewdog-2.jpg", "Brewdog founders 2");

		Brewery brewdog = breweryRepository.save(new Brewery.Builder().name("Brewdog")
				.description("BrewDog is a British multinational brewery and pub chain based in Ellon, Scotland.")
				.thumbnail(brewdogThumb).images(asList(brewdogImage1, brewdogImage2)).build());

		{
			String beerDir = brewdogDir + "beers/punk-ipa/";
			Media thumbnail = loadJpg(beerDir + "punk-ipa-thumbnail.jpg", "Punk IPA thumbnail");
			Media image1 = loadJpg(beerDir + "punk-ipa-1.jpg", "Punk IPA 1");
			Media image2 = loadJpg(beerDir + "punk-ipa-2.jpg", "Punk IPA 2");
			Media image3 = loadJpg(beerDir + "punk-ipa-3.jpg", "Punk IPA 3");
			beerRepository.save(new Beer.Builder().brewery(brewdog).name("Punk IPA").description(
					"This 5.6% trans-atlantic fusion IPA is light golden in colour with tropical fruits and light caramel on the nose. The palate soon becomes assertive and resinous with the New Zealand hops balanced by the biscuit malt. The finish is aggressive and dry with the hops emerging over the warming alcohol.")
					.perfectFor(asList("Cheesecake with a passion-fruit swirl sauce", "Chicken wings",
							"Spicy carne asada with a pic de gallo sauce"))
					.thumbnail(thumbnail).images(asList(image1, image2, image3)).abv(56).ibu(35).build());
		}

		{
			String beerDir = brewdogDir + "beers/nanny-state/";
			Media thumbnail = loadPng(beerDir + "nanny-state-thumbnail.png", "Nanny State thumbnail");
			Media image1 = loadPng(beerDir + "nanny-state-1.png", "Nanny State 1");
			beerRepository.save(new Beer.Builder().brewery(brewdog).name("Nanny State").description(
					"Brewing a full flavoured craft beer at 0.5% is no easy task. Packed with loads of Centennial, Amarillo, Columbus, Cascade and Simcoe hops, dry hopped to the brink and back and sitting at 45 IBUS, Nanny State is a force to be reckoned with.")
					.perfectFor(asList("Earthy mushroom pasta")).thumbnail(thumbnail).images(asList(image1)).abv(5)
					.ibu(45).build());
		}

		{
			String beerDir = brewdogDir + "beers/jet-black-heart/";
			Media thumbnail = loadJpg(beerDir + "jet-black-heart-thumbnail.jpg", "Jet Black Heart thumbnail");
			Media image1 = loadJpg(beerDir + "jet-black-heart-1.jpg", "Jet Black Heart 1");
			Media image2 = loadJpg(beerDir + "jet-black-heart-2.jpg", "Jet Black Heart 2");
			beerRepository.save(new Beer.Builder().brewery(brewdog).name("Jet Black Heart").description(
					"This sable stout is black as pitch and smooth as hell. Jet Black Heart is a milk stout; roasty malt flavours of coffee and chocolate, bound to a decadent full-bodied richness.")
					.perfectFor(asList("A Shakin' Jesse", "Beef shin stew", "Oyster beignets")).thumbnail(thumbnail)
					.images(asList(image1, image2)).abv(47).ibu(30).build());
		}
	}

	private Media loadPng(String name, String alt) {
		return loadMedia(name, MediaType.IMAGE_PNG_VALUE, alt, null);
	}

	private Media loadJpg(String name, String alt) {
		return loadMedia(name, MediaType.IMAGE_JPEG_VALUE, alt, null);
	}

	private Media loadMedia(String name, String mimeType, String alt, Long sortHint) {
		Resource resource = resourceLoader.getResource(String.format("classpath:initialSetup/%s", name));
		ResourceMultipartFile file = new ResourceMultipartFile(mimeType, resource);
		CreateMediaRequest request = new CreateMediaRequest();
		request.setAlt(alt);
		request.setSortHint(sortHint);
		MediaResponse response = mediaService.create(request, file);
		return response.getData();
	}
}
