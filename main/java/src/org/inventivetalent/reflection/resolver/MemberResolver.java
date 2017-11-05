package org.inventivetalent.reflection.resolver;

import java.lang.reflect.*;
import org.inventivetalent.reflection.resolver.wrapper.*;

public abstract class MemberResolver<T extends Member> extends ResolverAbstract<T>
{
    protected Class<?> clazz;
    
    public MemberResolver(final Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("class cannot be null");
        }
        this.clazz = clazz;
    }
    
    public MemberResolver(final String className) throws ClassNotFoundException {
        this(new ClassResolver().resolve(className));
    }
    
    public abstract T resolveIndex(final int p0) throws IndexOutOfBoundsException, ReflectiveOperationException;
    
    public abstract T resolveIndexSilent(final int p0);
    
    public abstract WrapperAbstract resolveIndexWrapper(final int p0);
}
