package com.looseboxes.webform.thym.domain.converters;

import com.looseboxes.webform.thym.domain.enums.BlogType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

/**
 * @author hp
 */
public class StringIdToBlogTypeConverter implements Converter<String, BlogType>{

    private static final Logger LOG = LoggerFactory
            .getLogger(StringIdToBlogTypeConverter.class);
    
    public StringIdToBlogTypeConverter() { }

    @Override
    public BlogType convert(String source) {
        final int id = Integer.parseInt(source);
        final BlogType blogType = BlogType.values()[id - 1];
        LOG.trace("Converted: {} to: {}", source, blogType);
        return blogType;
    }
}
