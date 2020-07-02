package com.looseboxes.webform.thym;

import com.looseboxes.webform.config.AbstractWebformJpaConfiguration;
import javax.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * @author hp
 */
@Configuration
public class WebformJpaConfiguration extends AbstractWebformJpaConfiguration{
    
    @Autowired private EntityManagerFactory entityMangerFactory;

    @Override
    public EntityManagerFactory entityManagerFactory() {
        return this.entityMangerFactory;
    }
}
