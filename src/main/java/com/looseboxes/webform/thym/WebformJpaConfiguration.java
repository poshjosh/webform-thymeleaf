package com.looseboxes.webform.thym;

import com.looseboxes.webform.config.AbstractWebformJpaConfiguration;
import com.looseboxes.webform.thym.domain.enums.BlogType;
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

    /**
     * Use this to add additional packages to search for entity classes.
     * That is, in addition to those specified in the
     * {@link javax.persistence.EntityManagerFactory EntityManagerFactory} i.e
     * the <tt>META-INF/persistence.xml</tt> file.
     * 
     * In this case we add the package which contains enum types, as enum types 
     * are often not captured whenever database is generated from existing entity 
     * classes. 
     * @return 
     */
    @Override
    public String [] getAdditionalEntityPackageNames() {
        // We add an enumeration type
        return new String[]{BlogType.class.getPackage().getName()};
    }

}
