package com.urlshortener.dto;

import java.time.LocalDateTime;

public class UrlStatsResponse {
	private long id;
    private String url;
    private String shortCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int accessCount;
	public String getUrl() {
		return url;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	public String getShortCode() {
		return shortCode;
	}
	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	public int getAccessCount() {
		return accessCount;
	}
	public void setAccessCount(int accessCount) {
		this.accessCount = accessCount;
	}

	public UrlStatsResponse(long id, String url, String shortCode, LocalDateTime createdAt, LocalDateTime updatedAt,
			int accessCount) {
		super();
		this.id = id;
		this.url = url;
		this.shortCode = shortCode;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.accessCount = accessCount;
	}

	public UrlStatsResponse() {
		super();
	}
    
    
}
