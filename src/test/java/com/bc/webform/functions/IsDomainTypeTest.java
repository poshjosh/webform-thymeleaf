package com.bc.webform.functions;

import com.looseboxes.webform.thym.domain.Blog;
import org.junit.jupiter.api.Test;

/**
 * @author hp
 */
public class IsDomainTypeTest extends TypeTestBase<IsDomainType>{
    
    public IsDomainTypeTest() { 
        super(() -> new IsDomainType());
    }

    @Test
    public void test_givenNonDomainType_ShouldReturnFalse() {
        System.out.println("test_givenNonDomainType_ShouldReturnFalse");
        test_givenType_ShouldReturn(Object.class, false);
    }

    @Test
    public void test_givenValidDomainType_ShouldReturnTrue() {
        System.out.println("test_givenValidDomainType_ShouldReturnTrue");
        test_givenType_ShouldReturn(Blog.class, true);
    }
}
