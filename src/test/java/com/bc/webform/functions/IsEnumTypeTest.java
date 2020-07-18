package com.bc.webform.functions;

import com.looseboxes.webform.thym.domain.Blog;
import com.looseboxes.webform.thym.domain.enums.BlogType;
import org.junit.jupiter.api.Test;

/**
 * @author hp
 */
public class IsEnumTypeTest extends TypeTestBase<IsEnumType>{
    
    public IsEnumTypeTest() { 
        super(() -> new IsEnumType());
    }

    @Test
    public void test_givenNonEnumType_ShouldReturnFalse() {
        System.out.println("test_givenNonEnumType_ShouldReturnFalse");
        test_givenType_ShouldReturn(Blog.class, false);
    }

    @Test
    public void test_givenValidEnumType_ShouldReturnTrue() {
        System.out.println("test_givenValidEnumType_ShouldReturnTrue");
        test_givenType_ShouldReturn(BlogType.class, true);
    }
}

