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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.google.common.io.ByteStreams;
import com.surevine.springrestexample.ApplicationProperties;
import com.surevine.springrestexample.api.ResourceStatus;
import com.surevine.springrestexample.api.media.CreateMediaRequest;
import com.surevine.springrestexample.api.media.MediaListResponse;
import com.surevine.springrestexample.api.media.MediaResponse;
import com.surevine.springrestexample.entity.Media;
import com.surevine.springrestexample.repository.MediaRepository;
import com.surevine.springrestexample.service.MediaService;

@Service
public class MediaServiceImpl implements MediaService {

	private static final Logger LOG = LoggerFactory.getLogger(MediaServiceImpl.class);

	private ResourceLoader resourceLoader;

	private ApplicationProperties properties;

	private MediaRepository mediaRepository;

	private Field amazonS3Field;

	private Field bucketNameField;

	private Field objectNameField;

	public MediaServiceImpl(ResourceLoader resourceLoader, ApplicationProperties properties,
			MediaRepository mediaRepository) {
		this.resourceLoader = resourceLoader;
		this.properties = properties;
		this.mediaRepository = mediaRepository;

		try {
			Class<?> simpleStorageResourceClazz = Class
					.forName("org.springframework.cloud.aws.core.io.s3.SimpleStorageResource");
			amazonS3Field = simpleStorageResourceClazz.getDeclaredField("amazonS3");
			amazonS3Field.setAccessible(true);
			bucketNameField = simpleStorageResourceClazz.getDeclaredField("bucketName");
			bucketNameField.setAccessible(true);
			objectNameField = simpleStorageResourceClazz.getDeclaredField("objectName");
			objectNameField.setAccessible(true);
		} catch (ClassNotFoundException | NoSuchFieldException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	@Transactional
	public MediaResponse create(CreateMediaRequest request, MultipartFile file) {
		Media media = Media.from(request, file);
		media = mediaRepository.save(media);

		try {
			WritableResource resource = (WritableResource) getResource(media);

			try (InputStream from = file.getInputStream(); OutputStream to = resource.getOutputStream()) {
				ByteStreams.copy(from, to);
			}

			LOG.info("Created resource: [{}]", resource.getURI());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return new MediaResponse(media, ResourceStatus.CREATED);
	}

	@Override
	@Transactional(readOnly = true)
	public MediaListResponse findAll() {
		return new MediaListResponse(mediaRepository.findAll(), ResourceStatus.OK);
	}

	@Override
	@Transactional(readOnly = true)
	public MediaResponse findById(UUID id) {
		return mediaRepository.findById(id).map(media -> new MediaResponse(media, ResourceStatus.OK))
				.orElse(new MediaResponse(null, ResourceStatus.NOT_FOUND));
	}

	@Override
	public Resource findResource(Media media) {
		try {
			return getResource(media);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	@Transactional
	public void deleteAll() {
		for (Media media : mediaRepository.findAll()) {
			internalDelete(media);
		}
		mediaRepository.deleteAll();

	}

	private void internalDelete(Media media) {
		try {
			Resource resource = getResource(media);
			URI uri = resource.getURI();
			String mediaLocation = properties.getMediaLocation();
			if (mediaLocation.startsWith("file:")) {
				try {
					Files.delete(Paths.get(uri));
				} catch (IOException e) {
					LOG.error("Failed to delete delete filesystem resource", e);
				}
				LOG.info("Deleted resource: [{}]", uri);
			} else if (mediaLocation.startsWith("s3:")) {
				try {
					AmazonS3 amazonS3 = (AmazonS3) amazonS3Field.get(resource);
					String bucketName = (String) bucketNameField.get(resource);
					String objectName = (String) objectNameField.get(resource);
					amazonS3.deleteObject(new DeleteObjectRequest(bucketName, objectName));
				} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
					throw new RuntimeException(String.format("Failed to delete S3 resource: [%s]", uri), e);
				}
				LOG.info("Deleted resource: [{}]", uri);
			} else {
				LOG.warn("Cannot delete resource: [{}]", uri);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	@Transactional
	public void deleteById(UUID id) {
		Optional<Media> optional = mediaRepository.findById(id);
		if (optional.isPresent()) {
			internalDelete(optional.get());
			mediaRepository.deleteById(id);
		}
	}

	private Resource getResource(Media media) throws IOException {
		String mediaLocation = properties.getMediaLocation();
		String location = String.format("%s%s%s", mediaLocation, mediaLocation.endsWith("/") ? "" : "/",
				media.getId().toString());
		Resource resource = resourceLoader.getResource(location);
		return resource;
	}

}
