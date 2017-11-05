package org.inventivetalent.reflection.resolver.wrapper;

public class ClassWrapper<R> extends WrapperAbstract
{
    private final Class<R> clazz;
    
    public ClassWrapper(final Class<R> clazz) {
        this.clazz = clazz;
    }
    
    @Override
    public boolean exists() {
        return this.clazz != null;
    }
    
    public Class<R> getClazz() {
        return this.clazz;
    }
    
    public String getName() {
        return this.clazz.getName();
    }
    
    public R newInstance() {
        try {
            return this.clazz.newInstance();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public R newInstanceSilent() {
        try {
            return this.clazz.newInstance();
        }
        catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        final ClassWrapper<?> that = (ClassWrapper<?>)object;
        return (this.clazz != null) ? this.clazz.equals(that.clazz) : (that.clazz == null);
    }
    
    @Override
    public int hashCode() {
        return (this.clazz != null) ? this.clazz.hashCode() : 0;
    }
}
