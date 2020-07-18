package com.looseboxes.webform.thym;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * @author hp
 */
public class RestTestBase {
    
    private static final Logger LOG = LoggerFactory.getLogger(RestTestBase.class);
    
    private TestRestTemplate restTemplate;
    
    public <T> ResponseEntity<T> shouldReturnSuccess(ResponseEntity<T> response) {
        if( ! response.getStatusCode().is2xxSuccessful()) {
            fail("Expected response status code to be less than 300 but found: " + response.getStatusCodeValue());
        }
        return response;
    }
    
    public <T> ResponseEntity<T> shouldReturnError(ResponseEntity<T> response) {
        final HttpStatus status = response.getStatusCode();
        if( ! status.is4xxClientError() && ! status.is5xxServerError()) {
            fail("Expected response status code to be greater or equals to 400 but found: " + response.getStatusCodeValue());
        }
        return response;
    }

    public <T> ResponseEntity<T> shouldReturnStatusCode(ResponseEntity<T> response, int code) {
        assertThat("Response status code", response.getStatusCodeValue(), is(code));
        return response;
    }

    public ResponseEntity<String> givenUrl_whenRestApiCalled(
            String url, HttpMethod method) {
        
        return this.givenUrl_whenRestApiCalled(url, method, String.class);
    }
    
    public <T> ResponseEntity<T> givenUrl_whenRestApiCalled(
            String url, HttpMethod method, Class<T> responseType) {
        
        return this.givenUrl_whenRestApiCalled(url, method, 
                Collections.EMPTY_LIST, responseType, Collections.EMPTY_MAP);
    }
    
    public <T> ResponseEntity<T> givenUrl_whenRestApiCalled(
            String url, HttpMethod method, List<String> cookies, 
            Class<T> responseType, Map params) {
        
        LOG.debug("-> Request: {}", url);
        LOG.debug("-> Cookies: {}", cookies);
        LOG.debug("->  Params: {}", params);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Cookie",cookies.stream().collect(Collectors.joining(";")));
        
        final HttpEntity request;
        final ResponseEntity<T> response;
        if(HttpMethod.POST.equals(method)) {
            // If you don't use MultiValueMap you get error
            // org.springframework.web.client.RestClientException: No HttpMessageConverter for java.util.HashMap and content type "application/x-www-form-urlencoded"
            final MultiValueMap<String, String> mvm = new LinkedMultiValueMap<>();
            params.forEach((k, v) -> {
                if(k != null && v != null) {
                    mvm.put(k.toString(), Collections.singletonList(v.toString()));
                }
            });
            request = new HttpEntity<>(mvm, headers);
            response = restTemplate.postForEntity(url, request, responseType);
        }else{
            request = new HttpEntity<>(headers);
            response = restTemplate.exchange(
                    url, method, request, responseType, params);
        }
        
        this.getCookies(response);

        return response;
    }
    
    public List<String> getCookies(ResponseEntity responseEntity) {
        final List<String> cookiesReceived = responseEntity.getHeaders().get("Set-Cookie");
        LOG.trace("Cookies received: {}", cookiesReceived);
        return cookiesReceived == null ? Collections.EMPTY_LIST : cookiesReceived;
    }

    public TestRestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
