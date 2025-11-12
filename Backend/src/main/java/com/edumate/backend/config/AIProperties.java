package com.edumate.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "ai.gemini")
public class AIProperties {
    
    private List<String> apiKeys;
    private String baseUrl;
    private int maxTokens;
    
    public List<String> getApiKeys() {
        return apiKeys;
    }
    
    public void setApiKeys(List<String> apiKeys) {
        this.apiKeys = apiKeys;
    }
    
    public String getBaseUrl() {
        return baseUrl;
    }
    
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
    
    public int getMaxTokens() {
        return maxTokens;
    }
    
    public void setMaxTokens(int maxTokens) {
        this.maxTokens = maxTokens;
    }
}
