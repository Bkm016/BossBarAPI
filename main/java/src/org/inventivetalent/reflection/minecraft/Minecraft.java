package org.inventivetalent.reflection.minecraft;

import org.inventivetalent.reflection.resolver.minecraft.*;
import org.inventivetalent.reflection.util.*;
import org.bukkit.entity.*;
import sun.reflect.*;
import org.inventivetalent.reflection.resolver.*;
import java.lang.reflect.*;
import org.bukkit.*;
import java.util.regex.*;

public class Minecraft
{
    static final Pattern NUMERIC_VERSION_PATTERN;
    public static final Version VERSION;
    private static OBCClassResolver obcClassResolver;
    private static Class<?> CraftEntity;
    
    static {
        NUMERIC_VERSION_PATTERN = Pattern.compile("v([0-9])_([0-9])_R([0-9])");
        Minecraft.obcClassResolver = new OBCClassResolver();
        VERSION = Version.getVersion();
        System.out.println("[ReflectionHelper] Version is " + Minecraft.VERSION);
        try {
            Minecraft.CraftEntity = Minecraft.obcClassResolver.resolve("entity.CraftEntity");
        }
        catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static String getVersion() {
        return String.valueOf(Minecraft.VERSION.name()) + ".";
    }
    
    public static Object getHandle(final Object object) throws ReflectiveOperationException {
        Method method;
        try {
            method = AccessUtil.setAccessible(object.getClass().getDeclaredMethod("getHandle", new Class[0]));
        }
        catch (ReflectiveOperationException e) {
            method = AccessUtil.setAccessible(Minecraft.CraftEntity.getDeclaredMethod("getHandle", new Class[0]));
        }
        return method.invoke(object, new Object[0]);
    }
    
    public static Entity getBukkitEntity(final Object object) throws ReflectiveOperationException {
        Method method;
        try {
            method = AccessUtil.setAccessible(object.getClass().getDeclaredMethod("getBukkitEntity", new Class[0]));
        }
        catch (ReflectiveOperationException e) {
            method = AccessUtil.setAccessible(Minecraft.CraftEntity.getDeclaredMethod("getHandle", new Class[0]));
        }
        return (Entity)method.invoke(object, new Object[0]);
    }
    
    public static Object getHandleSilent(final Object object) {
        try {
            return getHandle(object);
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    public static Object newEnumInstance(final Class clazz, final Class[] types, final Object[] values) throws ReflectiveOperationException {
        final Constructor constructor = new ConstructorResolver(clazz).resolve((Class<?>[][])new Class[][] { types });
        final Field accessorField = new FieldResolver(Constructor.class).resolve("constructorAccessor");
        ConstructorAccessor constructorAccessor = (ConstructorAccessor)accessorField.get(constructor);
        if (constructorAccessor == null) {
            new MethodResolver(Constructor.class).resolve("acquireConstructorAccessor").invoke(constructor, new Object[0]);
            constructorAccessor = (ConstructorAccessor)accessorField.get(constructor);
        }
        return constructorAccessor.newInstance(values);
    }
    
    public enum Version
    {
        UNKNOWN("UNKNOWN", 0, -1), 
        v1_7_R1("v1_7_R1", 1, 10701), 
        v1_7_R2("v1_7_R2", 2, 10702), 
        v1_7_R3("v1_7_R3", 3, 10703), 
        v1_7_R4("v1_7_R4", 4, 10704), 
        v1_8_R1("v1_8_R1", 5, 10801), 
        v1_8_R2("v1_8_R2", 6, 10802), 
        v1_8_R3("v1_8_R3", 7, 10803), 
        v1_8_R4("v1_8_R4", 8, 10804), 
        v1_9_R1("v1_9_R1", 9, 10901), 
        v1_9_R2("v1_9_R2", 10, 10902), 
        v1_10_R1("v1_10_R1", 11, 11001), 
        v1_11_R1("v1_11_R1", 12, 11101), 
        v1_12_R1("v1_12_R1", 13, 11201),
    	v1_12_R2("v1_12_R1", 13, 11202);
        
        private int version;
        
        private Version(final String s, final int n, final int version) {
            this.version = version;
        }
        
        public int version() {
            return this.version;
        }
        
        public boolean olderThan(final Version version) {
            return this.version() < version.version();
        }
        
        public boolean newerThan(final Version version) {
            return this.version() >= version.version();
        }
        
        public boolean inRange(final Version oldVersion, final Version newVersion) {
            return this.newerThan(oldVersion) && this.olderThan(newVersion);
        }
        
        public boolean matchesPackageName(final String packageName) {
            return packageName.toLowerCase().contains(this.name().toLowerCase());
        }
        
        public static Version getVersion() {
            final String name = Bukkit.getServer().getClass().getPackage().getName();
            final String versionPackage = String.valueOf(name.substring(name.lastIndexOf(46) + 1)) + ".";
            Version[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final Version version = values[i];
                if (version.matchesPackageName(versionPackage)) {
                    return version;
                }
            }
            System.err.println("[ReflectionHelper] Failed to find version enum for '" + name + "'/'" + versionPackage + "'");
            System.out.println("[ReflectionHelper] Generating dynamic constant...");
            final Matcher matcher = Minecraft.NUMERIC_VERSION_PATTERN.matcher(versionPackage);
            while (matcher.find()) {
                if (matcher.groupCount() >= 3) {
                    final String majorString = matcher.group(1);
                    String minorString = matcher.group(2);
                    if (minorString.length() == 1) {
                        minorString = "0" + minorString;
                    }
                    String patchString = matcher.group(3);
                    if (patchString.length() == 1) {
                        patchString = "0" + patchString;
                    }
                    final String numVersionString = String.valueOf(majorString) + minorString + patchString;
                    final int numVersion = Integer.parseInt(numVersionString);
                    final String packge = versionPackage.substring(0, versionPackage.length() - 1);
                    try {
                        final Field valuesField = new FieldResolver(Version.class).resolve("$VALUES");
                        final Version[] oldValues = (Version[])valuesField.get(null);
                        final Version[] newValues = new Version[oldValues.length + 1];
                        System.arraycopy(oldValues, 0, newValues, 0, oldValues.length);
                        final Version dynamicVersion = (Version)Minecraft.newEnumInstance(Version.class, new Class[] { String.class, Integer.TYPE, Integer.TYPE }, new Object[] { packge, newValues.length - 1, numVersion });
                        newValues[newValues.length - 1] = dynamicVersion;
                        valuesField.set(null, newValues);
                        System.out.println("[ReflectionHelper] Injected dynamic version " + packge + " (#" + numVersion + ").");
                        System.out.println("[ReflectionHelper] Please inform inventivetalent about the outdated version, as this is not guaranteed to work.");
                        return dynamicVersion;
                    }
                    catch (ReflectiveOperationException e) {
                        e.printStackTrace();
                    }
                }
            }
            return Version.UNKNOWN;
        }
        
        @Override
        public String toString() {
            return String.valueOf(this.name()) + " (" + this.version() + ")";
        }
    }
}
