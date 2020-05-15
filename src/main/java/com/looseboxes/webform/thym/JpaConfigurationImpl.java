package com.looseboxes.webform.thym;

import com.looseboxes.webform.FormController;
import com.looseboxes.webform.OnFormSubmittedImpl;
import com.looseboxes.webform.JpaConfiguration;
import javax.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @author hp
 */
@Configuration
public class JpaConfigurationImpl extends JpaConfiguration{
    
    @Autowired private EntityManagerFactory entityMangerFactory;

    @Override
    public EntityManagerFactory entityManagerFactory() {
        return this.entityMangerFactory;
    }
}
