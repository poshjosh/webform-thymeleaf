package com.looseboxes.webform.thym.jpa.converters;

import com.looseboxes.webform.thym.domain.enums.BlogType;
import java.util.stream.Stream;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hp
 */
@Converter(autoApply = true)
public class BlogTypeConverter implements AttributeConverter<BlogType, String> {
  
    private static final Logger LOG = LoggerFactory.getLogger(BlogTypeConverter.class);
    
    @Override
    public String convertToDatabaseColumn(BlogType entityAttribute) {
        if (entityAttribute == null) {
            return null;
        }
        final String databaseValue = String.valueOf(entityAttribute.ordinal() + 1);
        LOG.trace("Converted: {} to: {}", entityAttribute, databaseValue);
        return databaseValue;
    }
 
    @Override
    public BlogType convertToEntityAttribute(String databaseValue) {
        if (databaseValue == null) {
            return null;
        }
        BlogType entityAttribute;
        try{
            final int id = Integer.parseInt(databaseValue);
            entityAttribute = BlogType.values()[id - 1];
        }catch(NumberFormatException ignored) {
            entityAttribute = Stream.of(BlogType.values())
              .filter(type -> type.name().equals(databaseValue))
              .findFirst()
              .orElseThrow(IllegalArgumentException::new);
        }
        LOG.trace("Converted: {} to: {}", databaseValue, entityAttribute);
        return entityAttribute;
    }
}
