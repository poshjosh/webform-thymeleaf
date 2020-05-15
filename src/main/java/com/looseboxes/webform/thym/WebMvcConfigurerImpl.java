package com.looseboxes.webform.thym;

import com.looseboxes.webform.thym.domain.converters.StringIdToBlogTypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author hp
 */
@Configuration
public class WebMvcConfigurerImpl extends com.looseboxes.webform.WebMvcConfigurerImpl {
    
    private static final Logger LOG = LoggerFactory.getLogger(WebMvcConfigurer.class);
    
    @Override
    public void addFormatters(FormatterRegistry registry) {
        
        LOG.debug("Adding formatters");
        
        super.addFormatters(registry);

        registry.addConverter(this.stringIdToBlogTypeConverter());
    }
    
    @Bean public StringIdToBlogTypeConverter stringIdToBlogTypeConverter() {
        return new StringIdToBlogTypeConverter();
    }
}
