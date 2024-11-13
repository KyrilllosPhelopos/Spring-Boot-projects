package com.urlshortener.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.urlshortener.entity.UrlMapping;

@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long>{


	    Optional<UrlMapping> findByShortCode(String shortCode);
	    Optional<UrlMapping> findByOriginalUrl(String originalUrl);
		void save(Optional<UrlMapping> urlMapping);
	    
}

