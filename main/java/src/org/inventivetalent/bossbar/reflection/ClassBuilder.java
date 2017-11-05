package org.inventivetalent.bossbar.reflection;

import java.util.*;
import org.bukkit.*;
import org.inventivetalent.reflection.minecraft.*;

public abstract class ClassBuilder
{
    public static Object buildWitherSpawnPacket(final int id, final UUID uuid, final Location loc, final Object dataWatcher) throws Exception {
        final Object packet = NMSClass.PacketPlayOutSpawnEntityLiving.newInstance();
        if (Minecraft.VERSION.olderThan(Minecraft.Version.v1_9_R1)) {
            AccessUtil.setAccessible(NMSClass.PacketPlayOutSpawnEntityLiving.getDeclaredField("a")).set(packet, id);
            AccessUtil.setAccessible(NMSClass.PacketPlayOutSpawnEntityLiving.getDeclaredField("b")).set(packet, 64);
            AccessUtil.setAccessible(NMSClass.PacketPlayOutSpawnEntityLiving.getDeclaredField("c")).set(packet, (int)loc.getX());
            AccessUtil.setAccessible(NMSClass.PacketPlayOutSpawnEntityLiving.getDeclaredField("d")).set(packet, MathUtil.floor(loc.getY() * 32.0));
            AccessUtil.setAccessible(NMSClass.PacketPlayOutSpawnEntityLiving.getDeclaredField("e")).set(packet, (int)loc.getZ());
            AccessUtil.setAccessible(NMSClass.PacketPlayOutSpawnEntityLiving.getDeclaredField("i")).set(packet, (byte)MathUtil.d(loc.getYaw() * 256.0f / 360.0f));
            AccessUtil.setAccessible(NMSClass.PacketPlayOutSpawnEntityLiving.getDeclaredField("j")).set(packet, (byte)MathUtil.d(loc.getPitch() * 256.0f / 360.0f));
            AccessUtil.setAccessible(NMSClass.PacketPlayOutSpawnEntityLiving.getDeclaredField("k")).set(packet, (byte)MathUtil.d(loc.getPitch() * 256.0f / 360.0f));
            AccessUtil.setAccessible(NMSClass.PacketPlayOutSpawnEntityLiving.getDeclaredField("l")).set(packet, dataWatcher);
        }
        else {
            AccessUtil.setAccessible(NMSClass.PacketPlayOutSpawnEntityLiving.getDeclaredField("a")).set(packet, id);
            AccessUtil.setAccessible(NMSClass.PacketPlayOutSpawnEntityLiving.getDeclaredField("b")).set(packet, uuid);
            AccessUtil.setAccessible(NMSClass.PacketPlayOutSpawnEntityLiving.getDeclaredField("c")).set(packet, 64);
            AccessUtil.setAccessible(NMSClass.PacketPlayOutSpawnEntityLiving.getDeclaredField("d")).set(packet, loc.getX());
            AccessUtil.setAccessible(NMSClass.PacketPlayOutSpawnEntityLiving.getDeclaredField("e")).set(packet, loc.getY());
            AccessUtil.setAccessible(NMSClass.PacketPlayOutSpawnEntityLiving.getDeclaredField("f")).set(packet, loc.getZ());
            AccessUtil.setAccessible(NMSClass.PacketPlayOutSpawnEntityLiving.getDeclaredField("j")).set(packet, (byte)MathUtil.d(loc.getYaw() * 256.0f / 360.0f));
            AccessUtil.setAccessible(NMSClass.PacketPlayOutSpawnEntityLiving.getDeclaredField("k")).set(packet, (byte)MathUtil.d(loc.getPitch() * 256.0f / 360.0f));
            AccessUtil.setAccessible(NMSClass.PacketPlayOutSpawnEntityLiving.getDeclaredField("l")).set(packet, (byte)MathUtil.d(loc.getPitch() * 256.0f / 360.0f));
            AccessUtil.setAccessible(NMSClass.PacketPlayOutSpawnEntityLiving.getDeclaredField("m")).set(packet, dataWatcher);
        }
        return packet;
    }
    
    public static Object buildNameMetadataPacket(final int id, final Object dataWatcher, final int nameIndex, final int visibilityIndex, final String name) throws Exception {
        DataWatcher.setValue(dataWatcher, nameIndex, DataWatcher.V1_9.ValueType.ENTITY_NAME, (name != null) ? name : "");
        DataWatcher.setValue(dataWatcher, visibilityIndex, DataWatcher.V1_9.ValueType.ENTITY_NAME_VISIBLE, Minecraft.VERSION.olderThan(Minecraft.Version.v1_9_R1) ? ((byte)((name != null && !name.isEmpty()) ? 1 : 0)) : (name != null && !name.isEmpty()));
        final Object metaPacket = NMSClass.PacketPlayOutEntityMetadata.getConstructor(Integer.TYPE, NMSClass.DataWatcher, Boolean.TYPE).newInstance(id, dataWatcher, true);
        return metaPacket;
    }
    
    public static Object updateEntityLocation(final Object entity, final Location loc) throws Exception {
        NMSClass.Entity.getDeclaredField("locX").set(entity, loc.getX());
        NMSClass.Entity.getDeclaredField("locY").set(entity, loc.getY());
        NMSClass.Entity.getDeclaredField("locZ").set(entity, loc.getZ());
        return entity;
    }
    
    public static Object buildArmorStandSpawnPacket(final Object armorStand) throws Exception {
        final Object spawnPacket = NMSClass.PacketPlayOutSpawnEntityLiving.getConstructor(NMSClass.EntityLiving).newInstance(armorStand);
        AccessUtil.setAccessible(NMSClass.PacketPlayOutSpawnEntityLiving.getDeclaredField("b")).setInt(spawnPacket, 30);
        return spawnPacket;
    }
    
    public static Object buildTeleportPacket(final int id, final Location loc, final boolean onGround, final boolean heightCorrection) throws Exception {
        final Object packet = NMSClass.PacketPlayOutEntityTeleport.newInstance();
        AccessUtil.setAccessible(NMSClass.PacketPlayOutEntityTeleport.getDeclaredField("a")).set(packet, id);
        AccessUtil.setAccessible(NMSClass.PacketPlayOutEntityTeleport.getDeclaredField("b")).set(packet, (int)(loc.getX() * 32.0));
        AccessUtil.setAccessible(NMSClass.PacketPlayOutEntityTeleport.getDeclaredField("c")).set(packet, (int)(loc.getY() * 32.0));
        AccessUtil.setAccessible(NMSClass.PacketPlayOutEntityTeleport.getDeclaredField("d")).set(packet, (int)(loc.getZ() * 32.0));
        AccessUtil.setAccessible(NMSClass.PacketPlayOutEntityTeleport.getDeclaredField("e")).set(packet, (byte)(loc.getYaw() * 256.0f / 360.0f));
        AccessUtil.setAccessible(NMSClass.PacketPlayOutEntityTeleport.getDeclaredField("f")).set(packet, (byte)(loc.getPitch() * 256.0f / 360.0f));
        return packet;
    }
}
