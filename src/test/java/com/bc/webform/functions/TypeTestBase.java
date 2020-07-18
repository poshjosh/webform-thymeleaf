package com.bc.webform.functions;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author hp
 */
public class TypeTestBase<T extends Predicate<Class>> {
    
    private final Supplier<T> instanceSupplier;

    public TypeTestBase(Supplier<T> instanceSupplier) {
        this.instanceSupplier = Objects.requireNonNull(instanceSupplier);
    }
    
    public T newInstance() {
        return this.instanceSupplier.get();
    }
    
    public void test_givenType_ShouldReturn(Class type, boolean expResult) {
        System.out.println("test_givenType_ShouldReturn("+type.getName()+", "+expResult+")");
        final T instance = newInstance();
        boolean result = instance.test(type);
        assertThat(result, is(expResult));
    }
}
