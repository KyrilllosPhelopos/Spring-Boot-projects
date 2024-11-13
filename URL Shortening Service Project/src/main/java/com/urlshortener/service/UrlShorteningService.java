package com.urlshortener.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.urlshortener.dao.UrlMappingRepository;
import com.urlshortener.dto.UrlResponse;
import com.urlshortener.dto.UrlStatsResponse;
import com.urlshortener.entity.UrlMapping;
import com.urlshortener.exception.UrlAlreadyExistsException;
import com.urlshortener.exception.UrlNotFoundException;


@Service
public class UrlShorteningService {

	@Autowired
	private UrlMappingRepository repo;


	private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private static final int SHORT_CODE_LENGTH = 6;

	public Optional<UrlResponse> createShortUrl(String originalUrl) {
		Optional<UrlMapping> existing = repo.findByOriginalUrl(originalUrl);
		if (existing.isPresent()) {
			throw new UrlAlreadyExistsException("URL already exists");
		}

		String shortCode;
		do {
			shortCode = generateShortCode();
		} while (repo.findByShortCode(shortCode).isPresent());

		UrlMapping mapping = new UrlMapping(originalUrl ,shortCode );

		repo.save(mapping);

		return Optional.ofNullable(new UrlResponse(mapping.getId(), mapping.getOriginalUrl(), mapping.getShortCode(),
				mapping.getCreatedAt(), mapping.getUpdatedAt()));
	}


	public Optional<UrlResponse> getOriginalUrl(String shortCode) {
		Optional<UrlMapping> urlMapping = repo.findByShortCode(shortCode);
		return urlMapping.map(entity -> {
			// Increment the access count each time the URL is retrieved
			entity.setHitCount(entity.getHitCount() + 1);
			repo.save(entity); // Save the updated entity

			return new UrlResponse(entity.getId(), entity.getOriginalUrl(), entity.getShortCode(),
					entity.getCreatedAt(), entity.getUpdatedAt());
		});
	}


	public Optional <UrlResponse>updateOriginalUrl(String shortCode , String newUrl)
	{
		Optional<UrlMapping> optionalUrlMapping = repo.findByShortCode(shortCode);

		if (optionalUrlMapping.isEmpty())
			 throw new UrlNotFoundException("Short URL not found");

		UrlMapping urlMapping = optionalUrlMapping.get() ;
		urlMapping.setOriginalUrl(newUrl);
		urlMapping.setUpdatedAt(LocalDateTime.now());

		repo.save(urlMapping);
		
		return Optional.ofNullable(new UrlResponse(urlMapping.getId(), urlMapping.getOriginalUrl(), urlMapping.getShortCode(),
				urlMapping.getCreatedAt(), urlMapping.getUpdatedAt()));


	}


	public boolean deleteShortUrl(String shortCode) {
		Optional<UrlMapping> optionalUrl = repo.findByShortCode(shortCode);
		if (optionalUrl.isPresent()) {
			repo.delete(optionalUrl.get());
			return true;
		}
		return false;
	}


	public Optional<UrlStatsResponse> getUrlStats(String shortCode) {
		Optional<UrlMapping> urlEntity = repo.findByShortCode(shortCode);
		if (urlEntity.isEmpty()) {
			return Optional.empty();
		}

		UrlMapping entity = urlEntity.get();
		UrlStatsResponse statsResponse = new UrlStatsResponse(entity.getId(), entity.getOriginalUrl(), entity.getShortCode(),
				entity.getCreatedAt(), entity.getUpdatedAt(), entity.getHitCount());
		return Optional.of(statsResponse);
	}

	private String generateShortCode() {
		Random random = new Random();
		StringBuilder sb = new StringBuilder(SHORT_CODE_LENGTH);
		for (int i = 0; i < SHORT_CODE_LENGTH; i++) {
			sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
		}
		return sb.toString();
	}

}
