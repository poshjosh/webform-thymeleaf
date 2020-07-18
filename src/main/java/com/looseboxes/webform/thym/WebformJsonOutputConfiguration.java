package com.looseboxes.webform.thym;

import com.bc.webform.TypeTests;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.looseboxes.webform.json.WebformJsonOutputConfigurer;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author hp
 */
@Configuration
public class WebformJsonOutputConfiguration 
        extends WebformJsonOutputConfigurer implements WebMvcConfigurer{
    
    @Autowired
    public WebformJsonOutputConfiguration(TypeTests typeTests) {
        super(typeTests);
    }
    
    public Set<String> getFieldsToIgnore() {
        // @TODO Make this a property
        return Collections.singleton("password");
    }
    
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        this.configureHttpMessageConverter(converters);
    }
    
    @Bean public RestTemplate restTemplate() {
        return this.createConfiguredRestTemplate();
    }
    
    @Bean public MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter() {
        return this.createConfiguredHttpMessageConverter();
    }
    
    @Bean public ObjectMapper objectMapper() {
        return this.createConfiguredObjectMapper();
    }

    @Override
    public ObjectMapper configure(ObjectMapper objectMapper) {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return super.configure(objectMapper);
    }
}
