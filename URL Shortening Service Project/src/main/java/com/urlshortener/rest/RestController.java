package com.urlshortener.rest;


import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.urlshortener.dto.UrlResponse;
import com.urlshortener.dto.UrlStatsResponse;
import com.urlshortener.entity.UrlMapping;
import com.urlshortener.exception.UrlAlreadyExistsException;
import com.urlshortener.exception.UrlNotFoundException;
import com.urlshortener.service.UrlShorteningService;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/shorten")
public class RestController {

	@Autowired
    private UrlShorteningService service;


    
    @PostMapping
    public ResponseEntity<Object> createShortUrl(@RequestBody Map<String, String> requestBody) {
        try {
            Optional<UrlResponse> urlResponse = service.createShortUrl(requestBody.get("url"));
            return new ResponseEntity<>(urlResponse, HttpStatus.CREATED);
        } catch (UrlAlreadyExistsException e) {
            return ResponseEntity.badRequest().body("URL already exists");
        }
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<UrlResponse> getOriginalUrl(@PathVariable String shortCode) {
        Optional<UrlResponse> urlResponse = service.getOriginalUrl(shortCode);
        
        return urlResponse.map(response -> ResponseEntity.ok(response))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{shortCode}")
    public ResponseEntity<Object> updateOriginalUrl(@PathVariable String shortCode , @RequestBody Map<String, String> requestBody ){
       String url = requestBody.get("url");
       
       try {
           Optional<UrlResponse> updatedUrl = service.updateOriginalUrl(shortCode , url);
           return ResponseEntity.ok(updatedUrl);
       } catch (UrlNotFoundException e) {
    	   return ResponseEntity.notFound().build();
       }
       
    } 
    
    @DeleteMapping("/{shortCode}")
    public ResponseEntity<Void> deleteUrl(@PathVariable String shortCode)
    {
    	boolean status = service.deleteShortUrl(shortCode);
    	if(status)
    		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    	else 
    		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    		
    }
    
    @GetMapping("/{shortCode}/stats")
    public ResponseEntity<UrlStatsResponse> getUrlStats(@PathVariable String shortCode) {
        Optional<UrlStatsResponse> stats = service.getUrlStats(shortCode);
        
        return stats.map(response -> ResponseEntity.ok(response))
                    .orElseGet(() -> ResponseEntity.notFound().build());
    }

    
	 
}
