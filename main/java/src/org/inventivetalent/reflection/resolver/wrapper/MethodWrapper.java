package org.inventivetalent.reflection.resolver.wrapper;

import java.lang.reflect.*;

public class MethodWrapper<R> extends WrapperAbstract
{
    private final Method method;
    
    public MethodWrapper(final Method method) {
        this.method = method;
    }
    
    @Override
    public boolean exists() {
        return this.method != null;
    }
    
    public String getName() {
        return this.method.getName();
    }
    
    public R invoke(final Object object, final Object... args) {
        try {
            return (R)this.method.invoke(object, args);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public R invokeSilent(final Object object, final Object... args) {
        try {
            return (R)this.method.invoke(object, args);
        }
        catch (Exception e) {
            return null;
        }
    }
    
    public Method getMethod() {
        return this.method;
    }
    
    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        final MethodWrapper<?> that = (MethodWrapper<?>)object;
        return (this.method != null) ? this.method.equals(that.method) : (that.method == null);
    }
    
    @Override
    public int hashCode() {
        return (this.method != null) ? this.method.hashCode() : 0;
    }
}
