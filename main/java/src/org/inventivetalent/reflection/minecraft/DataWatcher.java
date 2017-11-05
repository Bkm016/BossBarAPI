package org.inventivetalent.reflection.minecraft;

import org.inventivetalent.reflection.resolver.minecraft.*;
import javax.annotation.*;
import org.inventivetalent.reflection.resolver.*;
import java.lang.reflect.*;
import java.util.*;

public class DataWatcher
{
    static ClassResolver classResolver;
    static NMSClassResolver nmsClassResolver;
    static Class<?> ItemStack;
    static Class<?> ChunkCoordinates;
    static Class<?> BlockPosition;
    static Class<?> Vector3f;
    static Class<?> DataWatcher;
    static Class<?> Entity;
    static Class<?> TIntObjectMap;
    static ConstructorResolver DataWacherConstructorResolver;
    static FieldResolver DataWatcherFieldResolver;
    static MethodResolver TIntObjectMapMethodResolver;
    static MethodResolver DataWatcherMethodResolver;
    
    public static Object newDataWatcher(@Nullable final Object entity) throws ReflectiveOperationException {
        return org.inventivetalent.reflection.minecraft.DataWatcher.DataWacherConstructorResolver.resolve((Class<?>[][])new Class[][] { { org.inventivetalent.reflection.minecraft.DataWatcher.Entity } }).newInstance(entity);
    }
    
    public static Object setValue(final Object dataWatcher, final int index, final Object dataWatcherObject, final Object value) throws ReflectiveOperationException {
        if (Minecraft.VERSION.olderThan(Minecraft.Version.v1_9_R1)) {
            return V1_8.setValue(dataWatcher, index, value);
        }
        return V1_9.setItem(dataWatcher, index, dataWatcherObject, value);
    }
    
    public static Object setValue(final Object dataWatcher, final int index, final V1_9.ValueType type, final Object value) throws ReflectiveOperationException {
        return setValue(dataWatcher, index, type.getType(), value);
    }
    
    public static Object setValue(final Object dataWatcher, final int index, final Object value, final FieldResolver dataWatcherObjectFieldResolver, final String... dataWatcherObjectFieldNames) throws ReflectiveOperationException {
        if (Minecraft.VERSION.olderThan(Minecraft.Version.v1_9_R1)) {
            return V1_8.setValue(dataWatcher, index, value);
        }
        final Object dataWatcherObject = dataWatcherObjectFieldResolver.resolve(dataWatcherObjectFieldNames).get(null);
        return V1_9.setItem(dataWatcher, index, dataWatcherObject, value);
    }
    
    public static Object getValue(final DataWatcher dataWatcher, final int index) throws ReflectiveOperationException {
        if (Minecraft.VERSION.olderThan(Minecraft.Version.v1_9_R1)) {
            return V1_8.getValue(dataWatcher, index);
        }
        return V1_9.getValue(dataWatcher, index);
    }
    
    public static int getValueType(final Object value) {
        int type = 0;
        if (value instanceof Number) {
            if (value instanceof Byte) {
                type = 0;
            }
            else if (value instanceof Short) {
                type = 1;
            }
            else if (value instanceof Integer) {
                type = 2;
            }
            else if (value instanceof Float) {
                type = 3;
            }
        }
        else if (value instanceof String) {
            type = 4;
        }
        else if (value != null && value.getClass().equals(org.inventivetalent.reflection.minecraft.DataWatcher.ItemStack)) {
            type = 5;
        }
        else if (value != null && (value.getClass().equals(org.inventivetalent.reflection.minecraft.DataWatcher.ChunkCoordinates) || value.getClass().equals(org.inventivetalent.reflection.minecraft.DataWatcher.BlockPosition))) {
            type = 6;
        }
        else if (value != null && value.getClass().equals(org.inventivetalent.reflection.minecraft.DataWatcher.Vector3f)) {
            type = 7;
        }
        return type;
    }
    
    static {
        org.inventivetalent.reflection.minecraft.DataWatcher.classResolver = new ClassResolver();
        org.inventivetalent.reflection.minecraft.DataWatcher.nmsClassResolver = new NMSClassResolver();
        org.inventivetalent.reflection.minecraft.DataWatcher.ItemStack = (Class<?>)org.inventivetalent.reflection.minecraft.DataWatcher.nmsClassResolver.resolveSilent("ItemStack");
        org.inventivetalent.reflection.minecraft.DataWatcher.ChunkCoordinates = (Class<?>)org.inventivetalent.reflection.minecraft.DataWatcher.nmsClassResolver.resolveSilent("ChunkCoordinates");
        org.inventivetalent.reflection.minecraft.DataWatcher.BlockPosition = (Class<?>)org.inventivetalent.reflection.minecraft.DataWatcher.nmsClassResolver.resolveSilent("BlockPosition");
        org.inventivetalent.reflection.minecraft.DataWatcher.Vector3f = (Class<?>)org.inventivetalent.reflection.minecraft.DataWatcher.nmsClassResolver.resolveSilent("Vector3f");
        org.inventivetalent.reflection.minecraft.DataWatcher.DataWatcher = (Class<?>)org.inventivetalent.reflection.minecraft.DataWatcher.nmsClassResolver.resolveSilent("DataWatcher");
        org.inventivetalent.reflection.minecraft.DataWatcher.Entity = (Class<?>)org.inventivetalent.reflection.minecraft.DataWatcher.nmsClassResolver.resolveSilent("Entity");
        org.inventivetalent.reflection.minecraft.DataWatcher.TIntObjectMap = (Class<?>)org.inventivetalent.reflection.minecraft.DataWatcher.classResolver.resolveSilent("gnu.trove.map.TIntObjectMap", "net.minecraft.util.gnu.trove.map.TIntObjectMap");
        org.inventivetalent.reflection.minecraft.DataWatcher.DataWacherConstructorResolver = new ConstructorResolver(org.inventivetalent.reflection.minecraft.DataWatcher.DataWatcher);
        org.inventivetalent.reflection.minecraft.DataWatcher.DataWatcherFieldResolver = new FieldResolver(org.inventivetalent.reflection.minecraft.DataWatcher.DataWatcher);
        org.inventivetalent.reflection.minecraft.DataWatcher.TIntObjectMapMethodResolver = new MethodResolver(org.inventivetalent.reflection.minecraft.DataWatcher.TIntObjectMap);
        org.inventivetalent.reflection.minecraft.DataWatcher.DataWatcherMethodResolver = new MethodResolver(org.inventivetalent.reflection.minecraft.DataWatcher.DataWatcher);
    }
    
    public static class V1_9
    {
        static Class<?> DataWatcherItem;
        static Class<?> DataWatcherObject;
        static ConstructorResolver DataWatcherItemConstructorResolver;
        static FieldResolver DataWatcherItemFieldResolver;
        static FieldResolver DataWatcherObjectFieldResolver;
        
        public static Object newDataWatcherItem(final Object dataWatcherObject, final Object value) throws ReflectiveOperationException {
            if (V1_9.DataWatcherItemConstructorResolver == null) {
                V1_9.DataWatcherItemConstructorResolver = new ConstructorResolver(V1_9.DataWatcherItem);
            }
            return V1_9.DataWatcherItemConstructorResolver.resolveFirstConstructor().newInstance(dataWatcherObject, value);
        }
        
        public static Object setItem(final Object dataWatcher, final int index, final Object dataWatcherObject, final Object value) throws ReflectiveOperationException {
            return setItem(dataWatcher, index, newDataWatcherItem(dataWatcherObject, value));
        }
        
        public static Object setItem(final Object dataWatcher, final int index, final Object dataWatcherItem) throws ReflectiveOperationException {
            final Map<Integer, Object> map = (Map<Integer, Object>)org.inventivetalent.reflection.minecraft.DataWatcher.DataWatcherFieldResolver.resolve("c").get(dataWatcher);
            map.put(index, dataWatcherItem);
            return dataWatcher;
        }
        
        public static Object getItem(final Object dataWatcher, final Object dataWatcherObject) throws ReflectiveOperationException {
            return org.inventivetalent.reflection.minecraft.DataWatcher.DataWatcherMethodResolver.resolve(new ResolverQuery("c", (Class<?>[])new Class[] { V1_9.DataWatcherObject })).invoke(dataWatcher, dataWatcherObject);
        }
        
        public static Object getValue(final Object dataWatcher, final Object dataWatcherObject) throws ReflectiveOperationException {
            return org.inventivetalent.reflection.minecraft.DataWatcher.DataWatcherMethodResolver.resolve("get").invoke(dataWatcher, dataWatcherObject);
        }
        
        public static Object getValue(final Object dataWatcher, final ValueType type) throws ReflectiveOperationException {
            return getValue(dataWatcher, type.getType());
        }
        
        public static Object getItemObject(final Object item) throws ReflectiveOperationException {
            if (V1_9.DataWatcherItemFieldResolver == null) {
                V1_9.DataWatcherItemFieldResolver = new FieldResolver(V1_9.DataWatcherItem);
            }
            return V1_9.DataWatcherItemFieldResolver.resolve("a").get(item);
        }
        
        public static int getItemIndex(final Object dataWatcher, final Object item) throws ReflectiveOperationException {
            int index = -1;
            final Map<Integer, Object> map = (Map<Integer, Object>)org.inventivetalent.reflection.minecraft.DataWatcher.DataWatcherFieldResolver.resolve("c").get(dataWatcher);
            for (final Map.Entry<Integer, Object> entry : map.entrySet()) {
                if (entry.getValue().equals(item)) {
                    index = entry.getKey();
                    break;
                }
            }
            return index;
        }
        
        public static Type getItemType(final Object item) throws ReflectiveOperationException {
            if (V1_9.DataWatcherObjectFieldResolver == null) {
                V1_9.DataWatcherObjectFieldResolver = new FieldResolver(V1_9.DataWatcherObject);
            }
            final Object object = getItemObject(item);
            final Object serializer = V1_9.DataWatcherObjectFieldResolver.resolve("b").get(object);
            final Type[] genericInterfaces = serializer.getClass().getGenericInterfaces();
            if (genericInterfaces.length > 0) {
                final Type type = genericInterfaces[0];
                if (type instanceof ParameterizedType) {
                    final Type[] actualTypes = ((ParameterizedType)type).getActualTypeArguments();
                    if (actualTypes.length > 0) {
                        return actualTypes[0];
                    }
                }
            }
            return null;
        }
        
        public static Object getItemValue(final Object item) throws ReflectiveOperationException {
            if (V1_9.DataWatcherItemFieldResolver == null) {
                V1_9.DataWatcherItemFieldResolver = new FieldResolver(V1_9.DataWatcherItem);
            }
            return V1_9.DataWatcherItemFieldResolver.resolve("b").get(item);
        }
        
        public static void setItemValue(final Object item, final Object value) throws ReflectiveOperationException {
            V1_9.DataWatcherItemFieldResolver.resolve("b").set(item, value);
        }
        
        static {
            V1_9.DataWatcherItem = (Class<?>)org.inventivetalent.reflection.minecraft.DataWatcher.nmsClassResolver.resolveSilent("DataWatcher$Item");
            V1_9.DataWatcherObject = (Class<?>)org.inventivetalent.reflection.minecraft.DataWatcher.nmsClassResolver.resolveSilent("DataWatcherObject");
        }
        
        public enum ValueType
        {
            ENTITY_FLAG("Entity", 57), 
            ENTITY_AIR_TICKS("Entity", 58), 
            ENTITY_NAME("Entity", 59), 
            ENTITY_NAME_VISIBLE("Entity", 60), 
            ENTITY_SILENT("Entity", 61), 
            ENTITY_as("EntityLiving", 2), 
            ENTITY_LIVING_HEALTH("EntityLiving", new String[] { "HEALTH" }), 
            ENTITY_LIVING_f("EntityLiving", 2), 
            ENTITY_LIVING_g("EntityLiving", 3), 
            ENTITY_LIVING_h("EntityLiving", 4), 
            ENTITY_INSENTIENT_FLAG("EntityInsentient", 0), 
            ENTITY_SLIME_SIZE("EntitySlime", 0), 
            ENTITY_WITHER_a("EntityWither", 0), 
            ENTITY_WIHER_b("EntityWither", 1), 
            ENTITY_WITHER_c("EntityWither", 2), 
            ENTITY_WITHER_bv("EntityWither", 3), 
            ENTITY_WITHER_bw("EntityWither", 4), 
            ENTITY_HUMAN_ABSORPTION_HEARTS("EntityHuman", 0), 
            ENTITY_HUMAN_SCORE("EntityHuman", 1), 
            ENTITY_HUMAN_SKIN_LAYERS("EntityHuman", 2), 
            ENTITY_HUMAN_MAIN_HAND("EntityHuman", 3);
            
            private Object type;
            
            private ValueType(final String className, final String[] fieldNames) {
                try {
                    this.type = new FieldResolver(org.inventivetalent.reflection.minecraft.DataWatcher.nmsClassResolver.resolve(className)).resolve(fieldNames).get(null);
                }
                catch (Exception e) {
                    if (Minecraft.VERSION.newerThan(Minecraft.Version.v1_9_R1)) {
                        System.err.println("[ReflectionHelper] Failed to find DataWatcherObject for " + className + " " + Arrays.toString(fieldNames));
                    }
                }
            }
            
            private ValueType(final String className, final int index) {
                try {
                    this.type = new FieldResolver(org.inventivetalent.reflection.minecraft.DataWatcher.nmsClassResolver.resolve(className)).resolveIndex(index).get(null);
                }
                catch (Exception e) {
                    if (Minecraft.VERSION.newerThan(Minecraft.Version.v1_9_R1)) {
                        System.err.println("[ReflectionHelper] Failed to find DataWatcherObject for " + className + " #" + index);
                    }
                }
            }
            
            public boolean hasType() {
                return this.getType() != null;
            }
            
            public Object getType() {
                return this.type;
            }
        }
    }
    
    public static class V1_8
    {
        static Class<?> WatchableObject;
        static ConstructorResolver WatchableObjectConstructorResolver;
        static FieldResolver WatchableObjectFieldResolver;
        
        public static Object newWatchableObject(final int index, final Object value) throws ReflectiveOperationException {
            return newWatchableObject(org.inventivetalent.reflection.minecraft.DataWatcher.getValueType(value), index, value);
        }
        
        public static Object newWatchableObject(final int type, final int index, final Object value) throws ReflectiveOperationException {
            if (V1_8.WatchableObjectConstructorResolver == null) {
                V1_8.WatchableObjectConstructorResolver = new ConstructorResolver(V1_8.WatchableObject);
            }
            return V1_8.WatchableObjectConstructorResolver.resolve((Class<?>[][])new Class[][] { { Integer.TYPE, Integer.TYPE, Object.class } }).newInstance(type, index, value);
        }
        
        public static Object setValue(final Object dataWatcher, final int index, final Object value) throws ReflectiveOperationException {
            final int type = org.inventivetalent.reflection.minecraft.DataWatcher.getValueType(value);
            final Object map = org.inventivetalent.reflection.minecraft.DataWatcher.DataWatcherFieldResolver.resolve("dataValues").get(dataWatcher);
            org.inventivetalent.reflection.minecraft.DataWatcher.TIntObjectMapMethodResolver.resolve(new ResolverQuery("put", (Class<?>[])new Class[] { Integer.TYPE, Object.class })).invoke(map, index, newWatchableObject(type, index, value));
            return dataWatcher;
        }
        
        public static Object getValue(final Object dataWatcher, final int index) throws ReflectiveOperationException {
            final Object map = org.inventivetalent.reflection.minecraft.DataWatcher.DataWatcherFieldResolver.resolve("dataValues").get(dataWatcher);
            return org.inventivetalent.reflection.minecraft.DataWatcher.TIntObjectMapMethodResolver.resolve(new ResolverQuery("get", (Class<?>[])new Class[] { Integer.TYPE })).invoke(map, index);
        }
        
        public static int getWatchableObjectIndex(final Object object) throws ReflectiveOperationException {
            if (V1_8.WatchableObjectFieldResolver == null) {
                V1_8.WatchableObjectFieldResolver = new FieldResolver(V1_8.WatchableObject);
            }
            return V1_8.WatchableObjectFieldResolver.resolve("b").getInt(object);
        }
        
        public static int getWatchableObjectType(final Object object) throws ReflectiveOperationException {
            if (V1_8.WatchableObjectFieldResolver == null) {
                V1_8.WatchableObjectFieldResolver = new FieldResolver(V1_8.WatchableObject);
            }
            return V1_8.WatchableObjectFieldResolver.resolve("a").getInt(object);
        }
        
        public static Object getWatchableObjectValue(final Object object) throws ReflectiveOperationException {
            if (V1_8.WatchableObjectFieldResolver == null) {
                V1_8.WatchableObjectFieldResolver = new FieldResolver(V1_8.WatchableObject);
            }
            return V1_8.WatchableObjectFieldResolver.resolve("c").get(object);
        }
        
        static {
            V1_8.WatchableObject = (Class<?>)org.inventivetalent.reflection.minecraft.DataWatcher.nmsClassResolver.resolveSilent("WatchableObject", "DataWatcher$WatchableObject");
        }
    }
}
