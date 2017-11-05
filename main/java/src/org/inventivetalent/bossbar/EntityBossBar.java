package org.inventivetalent.bossbar;

import org.bukkit.scheduler.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import java.util.*;
import org.inventivetalent.reflection.minecraft.*;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;

import org.inventivetalent.bossbar.reflection.*;

public class EntityBossBar extends BukkitRunnable implements BossBar
{
    protected static int ENTITY_DISTANCE;
    protected final int ID;
    protected final UUID uuid;
    protected final Player receiver;
    protected String message;
    protected float health;
    protected float healthMinus;
    protected float minHealth;
    protected Location location;
    protected World world;
    protected boolean visible;
    protected Object dataWatcher;
    
    protected EntityBossBar(final Player player, final String message, final float percentage, final int timeout, final float minHealth) {
        this.minHealth = 1.0f;
        this.visible = false;
        this.ID = new Random().nextInt();
        this.uuid = UUID.randomUUID();
        this.receiver = player;
        this.message = message;
        this.health = percentage / 100.0f * this.getMaxHealth();
        this.minHealth = minHealth;
        this.world = player.getWorld();
        this.location = this.makeLocation(player.getLocation());
        if (percentage <= minHealth) {
            BossBarAPI.removeBar(player);
        }
        if (timeout > 0) {
            this.healthMinus = this.getMaxHealth() / timeout;
            this.runTaskTimer(BossBarPlugin.instance, 20L, 20L);
        }
    }
    
    protected Location makeLocation(final Location base) {
        return base.getDirection().multiply(EntityBossBar.ENTITY_DISTANCE).add(base.toVector()).toLocation(this.world);
    }
    
    public Player getReceiver() {
        return this.receiver;
    }
    
    public float getMaxHealth() {
        return 300.0f;
    }
    
    public void setHealth(final float percentage) {
        this.health = percentage / 100.0f * this.getMaxHealth();
        if (this.health <= this.minHealth) {
            BossBarAPI.removeBar(this.receiver);
        }
        else {
            this.sendMetadata();
        }
    }
    
    public float getHealth() {
        return this.health;
    }
    
    public void setMessage(final String message) {
        this.message = message;
        if (this.isVisible()) {
            this.sendMetadata();
        }
    }
    
	public void setTitle(BaseComponent paramString) {
		this.message = ComponentSerializer.toString(paramString);
        if (this.isVisible()) {
            this.sendMetadata();
        }
	}
    
    public Collection<? extends Player> getPlayers() {
        return Collections.singletonList(this.getReceiver());
    }
    
    public void addPlayer(final Player player) {
    }
    
    public void removePlayer(final Player player) {
        this.setVisible(false);
    }
    
    public BossBarAPI.Color getColor() {
        return null;
    }
    
    public void setColor(final BossBarAPI.Color color) {
    }
    
    public BossBarAPI.Style getStyle() {
        return null;
    }
    
    public void setStyle(final BossBarAPI.Style style) {
    }
    
    public void setProperty(final BossBarAPI.Property property, final boolean flag) {
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public Location getLocation() {
        return this.location;
    }
    
    public void run() {
        this.health -= this.healthMinus;
        if (this.health <= this.minHealth) {
            BossBarAPI.removeBar(this.receiver);
        }
        else {
            this.sendMetadata();
        }
    }
    
    public void setVisible(final boolean flag) {
        if (flag == this.visible) {
            return;
        }
        if (flag) {
            this.spawn();
        }
        else {
            this.destroy();
        }
    }
    
    public boolean isVisible() {
        return this.visible;
    }
    
    public void setProgress(final float progress) {
        this.setHealth(progress * 100.0f);
    }
    
    public float getProgress() {
        return this.getHealth() / 100.0f;
    }
    
    public void updateMovement() {
        if (!this.visible) {
            return;
        }
        this.location = this.makeLocation(this.receiver.getLocation());
        try {
            final Object packet = ClassBuilder.buildTeleportPacket(this.ID, this.getLocation(), false, false);
            BossBarAPI.sendPacket(this.receiver, packet);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected void updateDataWatcher() {
        if (this.dataWatcher == null) {
            try {
                DataWatcher.setValue(this.dataWatcher = DataWatcher.newDataWatcher(null), 17, DataWatcher.V1_9.ValueType.ENTITY_WITHER_a, new Integer(0));
                DataWatcher.setValue(this.dataWatcher, 18, DataWatcher.V1_9.ValueType.ENTITY_WIHER_b, new Integer(0));
                DataWatcher.setValue(this.dataWatcher, 19, DataWatcher.V1_9.ValueType.ENTITY_WITHER_c, new Integer(0));
                DataWatcher.setValue(this.dataWatcher, 20, DataWatcher.V1_9.ValueType.ENTITY_WITHER_bw, new Integer(1000));
                DataWatcher.setValue(this.dataWatcher, 0, DataWatcher.V1_9.ValueType.ENTITY_FLAG, (byte)32);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        try {
            DataWatcher.setValue(this.dataWatcher, 6, DataWatcher.V1_9.ValueType.ENTITY_LIVING_HEALTH, this.health);
            DataWatcher.setValue(this.dataWatcher, 10, DataWatcher.V1_9.ValueType.ENTITY_NAME, this.message);
            DataWatcher.setValue(this.dataWatcher, 2, DataWatcher.V1_9.ValueType.ENTITY_NAME, this.message);
            DataWatcher.setValue(this.dataWatcher, 11, DataWatcher.V1_9.ValueType.ENTITY_NAME_VISIBLE, (byte)1);
            DataWatcher.setValue(this.dataWatcher, 3, DataWatcher.V1_9.ValueType.ENTITY_NAME_VISIBLE, (byte)1);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected void sendMetadata() {
        this.updateDataWatcher();
        try {
            final Object metaPacket = ClassBuilder.buildNameMetadataPacket(this.ID, this.dataWatcher, 2, 3, this.message);
            BossBarAPI.sendPacket(this.receiver, metaPacket);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected void spawn() {
        try {
            this.updateMovement();
            this.updateDataWatcher();
            final Object packet = ClassBuilder.buildWitherSpawnPacket(this.ID, this.uuid, this.getLocation(), this.dataWatcher);
            BossBarAPI.sendPacket(this.receiver, packet);
            this.visible = true;
            this.sendMetadata();
            this.updateMovement();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected void destroy() {
        try {
            this.cancel();
        }
        catch (IllegalStateException ex) {}
        try {
            final Object packet = NMSClass.PacketPlayOutEntityDestroy.getConstructor(int[].class).newInstance(new int[] { this.ID });
            BossBarAPI.sendPacket(this.receiver, packet);
            this.visible = false;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    static {
        EntityBossBar.ENTITY_DISTANCE = 32;
    }
}
