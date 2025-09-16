# GeminiAIService Troubleshooting Guide

This guide helps resolve common issues with the GeminiAIService implementation.

## Common Errors and Solutions

### 1. **Compilation Errors**

#### Error: "Cannot resolve symbol 'StringUtils'"
**Solution:** Make sure you have the correct import:
```java
import org.springframework.util.StringUtils;
```

#### Error: "Cannot resolve symbol 'List.of()'"
**Cause:** You're using Java 8 or older
**Solutions:**
- Option A: Upgrade to Java 11+
- Option B: Replace `List.of()` with `Arrays.asList()`

```java
// Replace this:
content.add("parts", gson.toJsonTree(List.of(textPart)));

// With this:
content.add("parts", gson.toJsonTree(Arrays.asList(textPart)));
```

#### Error: "HttpClient not found" or "java.net.http package not found"
**Cause:** Java 8 compatibility issue
**Solution:** Use the alternative `GeminiAIServiceSimple` class which uses RestTemplate instead.

### 2. **Runtime Errors**

#### Error: "Gemini API key is not configured"
**Solution:**
1. Get your API key from [Google AI Studio](https://makersuite.google.com/app/apikey)
2. Update `application.properties`:
```properties
gemini.api.key=your_actual_api_key_here
```

#### Error: "CORS policy blocks requests"
**Solution:** Make sure your `application.properties` includes:
```properties
spring.web.cors.allowed-origins=http://localhost:3000,http://localhost:5173
spring.web.cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
spring.web.cors.allowed-headers=*
spring.web.cors.allow-credentials=true
```

#### Error: "HTTP 401 Unauthorized"
**Causes & Solutions:**
- Invalid API key → Verify your API key is correct
- API key quota exceeded → Check your usage in Google AI Studio
- API key not activated → Make sure you've enabled the Gemini API

#### Error: "HTTP 429 Too Many Requests"
**Solution:** You've hit the rate limit. Implement retry logic or reduce request frequency.

### 3. **Service-Specific Issues**

#### Error: "Bean creation failed" or "Could not autowire"
**Solution:** Use constructor injection instead:

```java
// Replace @Autowired field injection:
@Autowired
private GeminiAIService geminiAIService;

// With constructor injection:
private final GeminiAIService geminiAIService;

public AIGenerationController(GeminiAIService geminiAIService) {
    this.geminiAIService = geminiAIService;
}
```

#### Error: "RestTemplate not found"
**Solution:** Add the RestTemplate bean configuration:

```java
@Configuration
public class HttpClientConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

### 4. **Java Version Compatibility**

If you're using **Java 8**, use this compatibility version of key methods:

#### Replace List.of() calls:
```java
// Java 11+
List.of(content)

// Java 8 compatible
Arrays.asList(content)
```

#### Replace java.net.http.HttpClient:
Use the `GeminiAIServiceSimple` class which uses Spring's RestTemplate instead.

### 5. **Alternative Implementation**

If you continue having issues with the main `GeminiAIService`, switch to the alternative:

#### Step 1: Update Controller
```java
@Autowired
@Qualifier("geminiAIServiceSimple")
private GeminiAIServiceSimple geminiAIService;
```

#### Step 2: Update method calls
The interface is the same, so no changes needed to method calls.

## Debugging Tips

### 1. Enable Debug Logging
Add to `application.properties`:
```properties
logging.level.com.edumate.backend.service=DEBUG
logging.level.org.springframework.web.client=DEBUG
```

### 2. Test API Key Manually
Use curl to test your API key:
```bash
curl -X POST \
  'https://generativelanguage.googleapis.com/v1/models/gemini-pro:generateContent?key=YOUR_API_KEY' \
  -H 'Content-Type: application/json' \
  -d '{
    "contents": [{
      "parts": [{"text": "Hello, how are you?"}]
    }]
  }'
```

### 3. Common Status Codes
- **200**: Success
- **400**: Bad Request (check request format)
- **401**: Unauthorized (check API key)
- **403**: Forbidden (check API permissions)
- **429**: Rate limited (wait and retry)
- **500**: Server error (temporary, retry later)

## Working Configuration

Here's a known working configuration:

### application.properties
```properties
server.port=3001
gemini.api.key=AIzaSyCxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
gemini.api.url=https://generativelanguage.googleapis.com/v1/models/gemini-pro:generateContent
spring.web.cors.allowed-origins=http://localhost:3000,http://localhost:5173
logging.level.com.edumate.backend.service=DEBUG
```

### Java Version Check
```bash
java -version
# Should show Java 11 or higher for full compatibility
```

### Dependencies in pom.xml
Make sure you have:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
</dependency>
```

## Still Having Issues?

1. **Check the application logs** for detailed error messages
2. **Verify your API key** is working with a direct API call
3. **Test with a simple prompt** first before complex content
4. **Use the SimpleService** if you have Java compatibility issues
5. **Check firewall/network settings** if requests are timing out

## Performance Optimization

1. **Cache responses** for identical prompts
2. **Use async processing** for multiple content generation
3. **Implement retry logic** with exponential backoff
4. **Monitor API usage** to avoid quota limits
