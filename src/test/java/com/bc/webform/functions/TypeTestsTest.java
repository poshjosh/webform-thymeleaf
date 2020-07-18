package com.bc.webform.functions;

import com.bc.webform.TypeTestsImpl;
import com.bc.webform.TypeTests;
import com.looseboxes.webform.thym.domain.Blog;
import com.looseboxes.webform.thym.domain.enums.BlogType;
import java.util.Collection;
import java.util.function.Predicate;
import org.junit.jupiter.api.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * @author hp
 */
public class TypeTestsTest {
    
    public TypeTestsTest() { }
    
    @Test
    public void isContainerType_givenNonContainerType_ShouldReturnFalse() {
        System.out.println("isContainerType_givenNonContainerType_ShouldReturnFalse");
        this.isContainerType_givenType_ShouldReturn(Object.class, false);
    }

    @Test
    public void isContainerType_givenValidContainerType_ShouldReturnTrue() {
        System.out.println("isContainerType_givenValidContainerType_ShouldReturnTrue");
        this.isContainerType_givenType_ShouldReturn(Collection.class, true);
    }
    
    public void isContainerType_givenType_ShouldReturn(Class type, boolean expResult) {
        System.out.println("isContainerType_givenType_ShouldReturn("+type.getName()+", "+expResult+")");
        final TypeTests instance = getInstance();
        boolean result = instance.isContainerType(type);
        assertThat(result, is(expResult));
        assertThat(result, is(getContainerTypeTest().test(type)));
    }

    @Test
    public void isEnumType_givenNonEnumType_ShouldReturnFalse() {
        System.out.println("isEnumType_givenNonEnumType_ShouldReturnFalse");
        this.isEnumType_givenType_ShouldReturn(Object.class, false);
    }

    @Test
    public void isEnumType_givenValidEnumType_ShouldReturnTrue() {
        System.out.println("isEnumType_givenValidEnumType_ShouldReturnTrue");
        this.isEnumType_givenType_ShouldReturn(BlogType.class, true);
    }
    
    public void isEnumType_givenType_ShouldReturn(Class type, boolean expResult) {
        System.out.println("isEnumType_givenType_ShouldReturn("+type.getName()+", "+expResult+")");
        final TypeTests instance = getInstance();
        boolean result = instance.isEnumType(type);
        assertThat(result, is(expResult));
        assertThat(result, is(getEnumTypeTest().test(type)));
    }

    @Test
    public void isDomainType_givenNonDomainType_ShouldReturnTrue() {
        System.out.println("isDomainType_givenNonDomainType_ShouldReturnTrue");
        this.isDomainType_givenType_ShouldReturn(Object.class, false);
    }
    
    @Test
    public void isDomainType_givenValidDomainType_ShouldReturnTrue() {
        System.out.println("isDomainType_givenValidDomainType_ShouldReturnTrue");
        this.isDomainType_givenType_ShouldReturn(Blog.class, true);
    }
    
    public void isDomainType_givenType_ShouldReturn(Class type, boolean expResult) {
        System.out.println("isDomainType_givenType_ShouldReturn("+type.getName()+", "+expResult+")");
        final TypeTests instance = getInstance();
        boolean result = instance.isDomainType(type);
        assertThat(result, is(expResult));
        assertThat(result, is(getDomainTypeTest().test(type)));
    }

    public Predicate<Class> getContainerTypeTest() {
        return new IsContainerType();
    }
    
    public Predicate<Class> getEnumTypeTest() {
        return new IsEnumType();
    }
    
    public Predicate<Class> getDomainTypeTest() {
        return new IsDomainType();
    }
    
    public TypeTests getInstance() {
        return new TypeTestsImpl();
    }
}
