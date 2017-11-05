package org.inventivetalent.reflection.resolver;

import org.inventivetalent.reflection.resolver.wrapper.*;

public class ClassResolver extends ResolverAbstract<Class>
{
    public ClassWrapper resolveWrapper(final String... names) {
        return new ClassWrapper(this.resolveSilent(names));
    }
    
    public Class resolveSilent(final String... names) {
        try {
            return this.resolve(names);
        }
        catch (Exception e) {
            return null;
        }
    }
    
    public Class resolve(final String... names) throws ClassNotFoundException {
        final ResolverQuery.Builder builder = ResolverQuery.builder();
        for (final String name : names) {
            builder.with(name);
        }
        try {
            return super.resolve(builder.build());
        }
        catch (ReflectiveOperationException e) {
            throw (ClassNotFoundException)e;
        }
    }
    
    @Override
    protected Class resolveObject(final ResolverQuery query) throws ReflectiveOperationException {
        return Class.forName(query.getName());
    }
    
    @Override
    protected ClassNotFoundException notFoundException(final String joinedNames) {
        return new ClassNotFoundException("Could not resolve class for " + joinedNames);
    }
}
