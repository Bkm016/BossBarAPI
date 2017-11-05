package org.inventivetalent.reflection.resolver;

import org.inventivetalent.reflection.util.*;
import org.inventivetalent.reflection.resolver.wrapper.*;
import java.lang.reflect.*;

public class MethodResolver extends MemberResolver<Method>
{
    public MethodResolver(final Class<?> clazz) {
        super(clazz);
    }
    
    public MethodResolver(final String className) throws ClassNotFoundException {
        super(className);
    }
    
    @Override
    public Method resolveIndex(final int index) throws IndexOutOfBoundsException, ReflectiveOperationException {
        return AccessUtil.setAccessible(this.clazz.getDeclaredMethods()[index]);
    }
    
    @Override
    public Method resolveIndexSilent(final int index) {
        try {
            return this.resolveIndex(index);
        }
        catch (IndexOutOfBoundsException | ReflectiveOperationException ex2) {
            return null;
        }
    }
    
    @Override
    public MethodWrapper resolveIndexWrapper(final int index) {
        return new MethodWrapper(this.resolveIndexSilent(index));
    }
    
    public MethodWrapper resolveWrapper(final String... names) {
        return new MethodWrapper(this.resolveSilent(names));
    }
    
    public MethodWrapper resolveWrapper(final ResolverQuery... queries) {
        return new MethodWrapper(this.resolveSilent(queries));
    }
    
    public Method resolveSilent(final String... names) {
        try {
            return this.resolve(names);
        }
        catch (Exception e) {
            return null;
        }
    }
    
    public Method resolveSilent(final ResolverQuery... queries) {
        return super.resolveSilent(queries);
    }
    
    public Method resolve(final String... names) throws NoSuchMethodException {
        final ResolverQuery.Builder builder = ResolverQuery.builder();
        for (final String name : names) {
            builder.with(name);
        }
        return this.resolve(builder.build());
    }
    
    public Method resolve(final ResolverQuery... queries) throws NoSuchMethodException {
        try {
            return super.resolve(queries);
        }
        catch (ReflectiveOperationException e) {
            throw (NoSuchMethodException)e;
        }
    }
    
    @Override
    protected Method resolveObject(final ResolverQuery query) throws ReflectiveOperationException {
        for (final Method method : this.clazz.getDeclaredMethods()) {
            if (method.getName().equals(query.getName()) && (query.getTypes().length == 0 || ClassListEqual(query.getTypes(), method.getParameterTypes()))) {
                return AccessUtil.setAccessible(method);
            }
        }
        throw new NoSuchMethodException();
    }
    
    @Override
    protected NoSuchMethodException notFoundException(final String joinedNames) {
        return new NoSuchMethodException("Could not resolve method for " + joinedNames + " in class " + this.clazz);
    }
    
    static boolean ClassListEqual(final Class<?>[] l1, final Class<?>[] l2) {
        boolean equal = true;
        if (l1.length != l2.length) {
            return false;
        }
        for (int i = 0; i < l1.length; ++i) {
            if (l1[i] != l2[i]) {
                equal = false;
                break;
            }
        }
        return equal;
    }
}
