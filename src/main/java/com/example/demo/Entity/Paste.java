package com.example.demo.Entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Paste {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    private Instant expiresAt; // TTL
    private Integer maxViews; // max views allowed
    private Integer views = 0; // current views
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Instant getExpiresAt() {
		return expiresAt;
	}
	public void setExpiresAt(Instant expiresAt) {
		this.expiresAt = expiresAt;
	}
	public Integer getMaxViews() {
		return maxViews;
	}
	public void setMaxViews(Integer maxViews) {
		this.maxViews = maxViews;
	}
	public Integer getViews() {
		return views;
	}
	public void setViews(Integer views) {
		this.views = views;
	}

    
}


