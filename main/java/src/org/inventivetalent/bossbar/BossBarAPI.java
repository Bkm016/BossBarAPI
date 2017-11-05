package org.inventivetalent.bossbar;

import java.util.logging.*;
import org.bukkit.entity.*;
import net.md_5.bungee.api.chat.*;
import java.util.*;
import javax.annotation.*;
import org.inventivetalent.bossbar.reflection.*;
import org.bukkit.plugin.*;
import org.inventivetalent.apihelper.*;
import org.bukkit.event.server.*;
import java.lang.reflect.*;
import org.bukkit.event.*;
import org.inventivetalent.reflection.minecraft.*;
import org.bukkit.*;
import org.bukkit.scheduler.*;
import org.bukkit.event.player.*;
import java.util.concurrent.*;

public class BossBarAPI implements API, Listener
{
    protected static Map<UUID, List<BossBar>> barMap;
    public static boolean is1_9;
    Logger logger;
    
    public static BossBar addBar(List<Player> players, String message, Color color, Style style, float progress, Property... properties) {
        validate1_9();
        BossBar bossBar = new PacketBossBar(message, color, style, progress, properties);
        for (Player player : players) {
            addBarForPlayer(player, bossBar);
        }
        return bossBar;
    }
    
    public static BossBar addBar(List<Player> players, BaseComponent component, Color color, Style style, float progress, Property... properties) {
        validate1_9();
        BossBar bossBar = new PacketBossBar(component, color, style, progress, properties);
        for (Player player : players) {
            addBarForPlayer(player, bossBar);
        }
        return bossBar;
    }
    
    public static BossBar addBar(List<Player> players, BaseComponent component, Color color, Style style, float progress, int timeout, long interval, Property... properties) {
        validate1_9();
        BossBar bossBar = addBar(players, component, color, style, progress, properties);
        new BossBarTimer((PacketBossBar)bossBar, progress, timeout).runTaskTimer(BossBarPlugin.instance, interval, interval);
        return bossBar;
    }
    
    public static BossBar addBar(Player player, BaseComponent component, Color color, Style style, float progress, Property... properties) {
        if (BossBarAPI.is1_9) {
            BossBar bossBar = new PacketBossBar(component, color, style, progress, properties);
            addBarForPlayer(player, bossBar);
            return bossBar;
        }
        setMessage(player, component.toLegacyText(), progress * 100.0f);
        return getBossBar(player);
    }
    
    public static BossBar addBar(Player player, BaseComponent component, Color color, Style style, float progress, int timeout, long interval, Property... properties) {
        if (BossBarAPI.is1_9) {
            BossBar bossBar = addBar(player, component, color, style, progress, properties);
            new BossBarTimer((PacketBossBar)bossBar, progress, timeout).runTaskTimer(BossBarPlugin.instance, interval, interval);
            return bossBar;
        }
        setMessage(player, component.toLegacyText(), progress * 100.0f, timeout);
        return getBossBar(player);
    }
    
    public static BossBar addBar(BaseComponent component, Color color, Style style, float progress, Property... properties) {
        validate1_9();
        return new PacketBossBar(component, color, style, progress, properties);
    }
    
    public static List<BossBar> getBossBars(Player player) {
        if (!BossBarAPI.barMap.containsKey(player.getUniqueId())) {
            return new ArrayList<BossBar>();
        }
        return new ArrayList<BossBar>(BossBarAPI.barMap.get(player.getUniqueId()));
    }
    
    protected static void addBarForPlayer(Player player, BossBar bossBar) {
        bossBar.addPlayer(player);
        List<BossBar> collection = BossBarAPI.barMap.get(player.getUniqueId());
        if (collection == null) {
            collection = new ArrayList<BossBar>();
        }
        collection.add(bossBar);
        BossBarAPI.barMap.put(player.getUniqueId(), collection);
    }
    
    protected static void removeBarForPlayer(Player player, BossBar bossBar) {
        bossBar.removePlayer(player);
        List<BossBar> collection = BossBarAPI.barMap.get(player.getUniqueId());
        if (collection != null) {
            collection.remove(bossBar);
            if (!collection.isEmpty()) {
                BossBarAPI.barMap.put(player.getUniqueId(), collection);
            }
            else {
                BossBarAPI.barMap.remove(player.getUniqueId());
            }
        }
    }
    
    public static void removeAllBars(Player player) {
        for (BossBar bossBar : getBossBars(player)) {
            removeBarForPlayer(player, bossBar);
        }
    }
    
    @Deprecated
    public static void setMessage(Player player, String message) {
        setMessage(player, message, 100.0f);
    }
    
    @Deprecated
    public static void setMessage(Player player, String message, float percentage) {
        setMessage(player, message, percentage, 0);
    }
    
    @Deprecated
    public static void setMessage(Player player, String message, float percentage, int timeout) {
        setMessage(player, message, percentage, timeout, 100.0f);
    }
    
    @Deprecated
    public static void setMessage(Player player, String message, float percentage, int timeout, float minHealth) {
        if (BossBarAPI.is1_9) {
            removeAllBars(player);
            addBar(player, (BaseComponent)new TextComponent(message), Color.PURPLE, Style.PROGRESS, percentage / 100.0f, new Property[0]);
        }
        else {
            if (!BossBarAPI.barMap.containsKey(player.getUniqueId())) {
                ArrayList<BossBar> list = new ArrayList<BossBar>();
                list.add(new EntityBossBar(player, message, percentage, timeout, minHealth));
                BossBarAPI.barMap.put(player.getUniqueId(), list);
            }
            BossBar bar = BossBarAPI.barMap.get(player.getUniqueId()).get(0);
            if (!bar.getMessage().equals(message)) {
                bar.setMessage(message);
            }
            float newHealth = percentage / 100.0f * bar.getMaxHealth();
            if (bar.getHealth() != newHealth) {
                bar.setHealth(percentage);
            }
            if (!bar.isVisible()) {
                bar.setVisible(true);
            }
        }
    }
    
    @Deprecated
    public static String getMessage(Player player) {
        BossBar bar = getBossBar(player);
        if (bar == null) {
            return null;
        }
        return bar.getMessage();
    }
    
    @Deprecated
    public static boolean hasBar(@Nonnull Player player) {
        return BossBarAPI.barMap.containsKey(player.getUniqueId());
    }
    
    @Deprecated
    public static void removeBar(@Nonnull Player player) {
        BossBar bar = getBossBar(player);
        if (bar != null) {
            bar.setVisible(false);
        }
        removeAllBars(player);
    }
    
    @Deprecated
    public static void setHealth(Player player, float percentage) {
        BossBar bar = getBossBar(player);
        if (bar == null) {
            return;
        }
        bar.setHealth(percentage);
    }
    
    @Deprecated
    public static float getHealth(@Nonnull Player player) {
        BossBar bar = getBossBar(player);
        if (bar == null) {
            return -1.0f;
        }
        return bar.getHealth();
    }
    
    @Nullable
    @Deprecated
    public static BossBar getBossBar(@Nonnull Player player) {
        if (player == null) {
            return null;
        }
        List<BossBar> list = BossBarAPI.barMap.get(player.getUniqueId());
        return (list != null) ? list.get(0) : null;
    }
    
    @Deprecated
    public static List<BossBar> getBossBars() {
        List<BossBar> list = new ArrayList<BossBar>();
        for (List<BossBar> collection : BossBarAPI.barMap.values()) {
            list.add((collection).get(0));
        }
        return list;
    }
    
    protected static void sendPacket(Player p, Object packet) {
        if (p == null || packet == null) {
            throw new IllegalArgumentException("player and packet cannot be null");
        }
        try {
            Object handle = Reflection.getHandle(p);
            Object connection = Reflection.getField(handle.getClass(), "playerConnection").get(handle);
            Reflection.getMethod(connection.getClass(), "sendPacket", Reflection.getNMSClass("Packet")).invoke(connection, packet);
        }
        catch (Exception ex) {}
    }
    
    static void validate1_9() {
        if (!BossBarAPI.is1_9) {
            throw new RuntimeException(new UnsupportedOperationException("This method is not compatible with versions < 1.9"));
        }
    }
    
    @Override
    public void load() {
    }
    
    @Override
    public void init(Plugin plugin) {
        APIManager.registerEvents(this, (Listener)this);
        BossBarPlugin.instance = APIManager.getAPIHost(this);
        for (Player player : Bukkit.getOnlinePlayers()) {
            removeAllBars(player);
        }
    }
    
    @Override
    public void disable(Plugin plugin) {
    }
    
    @EventHandler
    public void onPluginEnable(PluginEnableEvent e) {
        if ("BarAPI".equals(e.getPlugin().getName()) && Bukkit.getPluginManager().isPluginEnabled("BarAPI")) {
            try {
                Class<?> barAPI = Class.forName("me.confuser.barapi.BarAPI");
                Method method = barAPI.getDeclaredMethod("enabled", new Class[0]);
                method.setAccessible(true);
                if (((Boolean) method.invoke(null, new Object[0]))) {
                	this.logger.info("Successfully replaced BarAPI.");
                    return;
                }
            }
            catch (Exception ex) {}
            this.logger.warning("Failed to replace BarAPI.");
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent e) {
        removeBar(e.getPlayer());
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onKick(PlayerKickEvent e) {
        removeBar(e.getPlayer());
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onTeleport(PlayerTeleportEvent e) {
        if (Minecraft.VERSION.olderThan(Minecraft.Version.v1_9_R1)) {
            this.handlePlayerTeleport(e.getPlayer(), e.getFrom(), e.getTo());
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onRespawn(PlayerRespawnEvent e) {
        if (Minecraft.VERSION.olderThan(Minecraft.Version.v1_9_R1)) {
            this.handlePlayerTeleport(e.getPlayer(), e.getPlayer().getLocation(), e.getRespawnLocation());
        }
    }
    
    protected void handlePlayerTeleport(Player player, Location from, Location to) {
        if (!hasBar(player)) {
            return;
        }
        BossBar bar = getBossBar(player);
        bar.setVisible(false);
        new BukkitRunnable() {
            public void run() {
                bar.setVisible(true);
            }
        }.runTaskLater(APIManager.getAPIHost(this), 2L);
    }
    
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (Minecraft.VERSION.olderThan(Minecraft.Version.v1_9_R1)) {
            BossBar bar = getBossBar(e.getPlayer());
            if (bar != null) {
                new BukkitRunnable() {
                    public void run() {
                        if (!e.getPlayer().isOnline()) {
                            return;
                        }
                        bar.updateMovement();
                    }
                }.runTaskLater(APIManager.getAPIHost(this), 0L);
            }
        }
    }
    
    public BossBarAPI() {
        this.logger = Logger.getLogger("BossBarAPI");
    }
    
    static {
        barMap = new ConcurrentHashMap<UUID, List<BossBar>>();
        BossBarAPI.is1_9 = Minecraft.VERSION.newerThan(Minecraft.Version.v1_9_R1);
    }
    
    public enum Color
    {
        PINK, 
        BLUE, 
        RED, 
        GREEN, 
        YELLOW, 
        PURPLE, 
        WHITE;
    }
    
    public enum Style
    {
        PROGRESS, 
        NOTCHED_6, 
        NOTCHED_10, 
        NOTCHED_12, 
        NOTCHED_20;
    }
    
    public enum Property
    {
        DARKEN_SKY, 
        PLAY_MUSIC, 
        CREATE_FOG;
    }
}
