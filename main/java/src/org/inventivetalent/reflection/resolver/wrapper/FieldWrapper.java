package org.inventivetalent.reflection.resolver.wrapper;

import java.lang.reflect.*;

public class FieldWrapper<R> extends WrapperAbstract
{
    private final Field field;
    
    public FieldWrapper(final Field field) {
        this.field = field;
    }
    
    @Override
    public boolean exists() {
        return this.field != null;
    }
    
    public String getName() {
        return this.field.getName();
    }
    
    public R get(final Object object) {
        try {
            return (R)this.field.get(object);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public R getSilent(final Object object) {
        try {
            return (R)this.field.get(object);
        }
        catch (Exception e) {
            return null;
        }
    }
    
    public void set(final Object object, final R value) {
        try {
            this.field.set(object, value);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public void setSilent(final Object object, final R value) {
        try {
            this.field.set(object, value);
        }
        catch (Exception ex) {}
    }
    
    public Field getField() {
        return this.field;
    }
    
    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        final FieldWrapper<?> that = (FieldWrapper<?>)object;
        if (this.field != null) {
            if (this.field.equals(that.field)) {
                return true;
            }
        }
        else if (that.field == null) {
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return (this.field != null) ? this.field.hashCode() : 0;
    }
}
