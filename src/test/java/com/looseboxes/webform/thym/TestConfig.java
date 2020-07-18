package com.looseboxes.webform.thym;

import com.bc.jpa.spring.DomainClasses;
import com.bc.jpa.spring.DomainClassesBuilder;
import com.looseboxes.webform.store.PropertyStoreImpl;
import com.looseboxes.webform.util.PropertySearchImpl;
import com.looseboxes.webform.util.PropertySearch;
import com.bc.jpa.spring.TypeFromNameResolver;
import com.bc.jpa.spring.TypeFromNameResolverUsingClassNames;
import com.bc.webform.TypeTests;
import com.bc.webform.TypeTestsImpl;
import com.looseboxes.webform.util.PropertySuffixes;
import java.util.Properties;
import java.util.Set;
import org.springframework.context.annotation.Bean;

/**
 * @author hp
 */
public class TestConfig{
    
//    private static final Logger LOG = LoggerFactory.getLogger(TestConfig.class);
    
    @Bean public TestUrls testUrls() {
        return new TestUrls(this.typeTests());
    }
    
    @Bean public TypeTests typeTests() {
        return new TypeTestsImpl();
    }
    
    @Bean public PropertySearch propertySearch(String prefix, Properties props) {
        return new PropertySearchImpl(prefix, 
                new PropertyStoreImpl(props), 
                this.propertySuffixes(),
                ""
        );
    }
    
    @Bean public PropertySuffixes propertySuffixes() {
        return new PropertySuffixes(this.typeFromNameResolver());
    }
    
    public String [] getEntityPackageNames() {
        return new String[]{com.looseboxes.webform.thym.domain.Blog.class.getPackage().getName()};
    }

    @Bean public TypeFromNameResolver typeFromNameResolver() {
        final Set<Class> classes = this.domainClasses().get();
        return new TypeFromNameResolverUsingClassNames(classes);
    }

    @Bean DomainClasses domainClasses() {
        return this.domainClassesBuilder()
                .reset()
//                .addFrom(this.entityManagerFactory())
                .addFromPersistenceXmlFile()
                .addFromPackages(this.getEntityPackageNames())
                .build();
    }

    @Bean DomainClassesBuilder domainClassesBuilder() {
        return new DomainClasses.Builder();
    }
}
