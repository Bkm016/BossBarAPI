package org.inventivetalent.bossbar;

import org.inventivetalent.reflection.resolver.minecraft.*;
import org.bukkit.entity.*;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.chat.*;
import java.util.*;
import org.bukkit.*;
import org.inventivetalent.reflection.resolver.*;

public class PacketBossBar implements BossBar
{
    static NMSClassResolver nmsClassResolver;
    static Class<?> PacketPlayOutBoss;
    static Class<?> PacketPlayOutBossAction;
    static Class<?> ChatSerializer;
    static Class<?> BossBattleBarColor;
    static Class<?> BossBattleBarStyle;
    static FieldResolver PacketPlayOutBossFieldResolver;
    static MethodResolver ChatSerializerMethodResolver;
    private final UUID uuid;
    private Collection<Player> receivers;
    private float progress;
    private String message;
    private BossBarAPI.Color color;
    private BossBarAPI.Style style;
    private boolean visible;
    private boolean darkenSky;
    private boolean playMusic;
    private boolean createFog;
    
    protected PacketBossBar(final String message, final BossBarAPI.Color color, final BossBarAPI.Style style, final float progress, final BossBarAPI.Property... properties) {
        this.receivers = new ArrayList<Player>();
        this.uuid = UUID.randomUUID();
        this.color = ((color != null) ? color : BossBarAPI.Color.PURPLE);
        this.style = ((style != null) ? style : BossBarAPI.Style.PROGRESS);
        this.setMessage(message);
        this.setProgress(progress);
        for (final BossBarAPI.Property property : properties) {
            this.setProperty(property, true);
        }
    }
    
    protected PacketBossBar(final BaseComponent message, final BossBarAPI.Color color, final BossBarAPI.Style style, final float progress, final BossBarAPI.Property... properties) {
        this(ComponentSerializer.toString(message), color, style, progress, properties);
    }
    
    @Override
    public Collection<? extends Player> getPlayers() {
        return new ArrayList<Player>(this.receivers);
    }
    
    @Override
    public void addPlayer(final Player player) {
        if (!this.receivers.contains(player)) {
            this.receivers.add(player);
            this.sendPacket(0, player);
            BossBarAPI.addBarForPlayer(player, this);
        }
    }
    
    @Override
    public void removePlayer(final Player player) {
        if (this.receivers.contains(player)) {
            this.receivers.remove(player);
            this.sendPacket(1, player);
            BossBarAPI.removeBarForPlayer(player, this);
        }
    }
    
    @Override
    public BossBarAPI.Color getColor() {
        return this.color;
    }
    
    @Override
    public void setColor(final BossBarAPI.Color color) {
        if (color == null) {
            throw new IllegalArgumentException("color cannot be null");
        }
        if (color != this.color) {
            this.color = color;
            this.sendPacket(4, null);
        }
    }
    
    @Override
    public BossBarAPI.Style getStyle() {
        return this.style;
    }
    
    @Override
    public void setStyle(final BossBarAPI.Style style) {
        if (style == null) {
            throw new IllegalArgumentException("style cannot be null");
        }
        if (style != this.style) {
            this.style = style;
            this.sendPacket(4, null);
        }
    }
    
    @Override
    public void setProperty(final BossBarAPI.Property property, final boolean flag) {
        switch (property) {
            case DARKEN_SKY: {
                this.darkenSky = flag;
                break;
            }
            case PLAY_MUSIC: {
                this.playMusic = flag;
                break;
            }
            case CREATE_FOG: {
                this.createFog = flag;
                break;
            }
        }
        this.sendPacket(5, null);
    }
    
    @Override
    public String getMessage() {
        return this.message;
    }
    
    @Override
    public void setMessage(final String message) {
        if (message == null) {
            throw new IllegalArgumentException("message cannot be null");
        }
        if (!message.startsWith("{") || !message.endsWith("}")) {
            throw new IllegalArgumentException("Invalid JSON");
        }
        if (!message.equals(this.message)) {
            this.message = message;
            this.sendPacket(3, null);
        }
    }
    
    @Override
    public float getProgress() {
        return this.progress;
    }
    
    @Override
    public void setProgress(float progress) {
        if (progress > 1.0f) {
            progress /= 100.0f;
        }
        if (progress != this.progress) {
            this.progress = progress;
            this.sendPacket(2, null);
        }
    }
    
    @Override
    public boolean isVisible() {
        return this.visible;
    }
    
    @Override
    public void setVisible(final boolean flag) {
        if (flag != this.visible) {
            this.visible = flag;
            this.sendPacket(flag ? 0 : 1, null);
        }
    }
    
    void sendPacket(final int action, final Player player) {
        try {
            final Object packet = PacketBossBar.PacketPlayOutBoss.newInstance();
            PacketBossBar.PacketPlayOutBossFieldResolver.resolve("a").set(packet, this.uuid);
            PacketBossBar.PacketPlayOutBossFieldResolver.resolve("b").set(packet, PacketBossBar.PacketPlayOutBossAction.getEnumConstants()[action]);
            PacketBossBar.PacketPlayOutBossFieldResolver.resolve("c").set(packet, serialize(this.message));
            PacketBossBar.PacketPlayOutBossFieldResolver.resolve("d").set(packet, this.progress);
            PacketBossBar.PacketPlayOutBossFieldResolver.resolve("e").set(packet, PacketBossBar.BossBattleBarColor.getEnumConstants()[this.color.ordinal()]);
            PacketBossBar.PacketPlayOutBossFieldResolver.resolve("f").set(packet, PacketBossBar.BossBattleBarStyle.getEnumConstants()[this.style.ordinal()]);
            PacketBossBar.PacketPlayOutBossFieldResolver.resolve("g").set(packet, this.darkenSky);
            PacketBossBar.PacketPlayOutBossFieldResolver.resolve("h").set(packet, this.playMusic);
            PacketBossBar.PacketPlayOutBossFieldResolver.resolve("i").set(packet, this.createFog);
            if (player != null) {
                BossBarAPI.sendPacket(player, packet);
            }
            else {
                for (final Player player2 : this.getPlayers()) {
                    BossBarAPI.sendPacket(player2, packet);
                }
            }
        }
        catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public float getMaxHealth() {
        return 100.0f;
    }
    
    @Override
    public void setHealth(final float percentage) {
        this.setProgress(percentage / 100.0f);
    }
    
    @Override
    public float getHealth() {
        return this.getProgress() * 100.0f;
    }
    
    @Override
    public Player getReceiver() {
        return null;
    }
    
    @Override
    public Location getLocation() {
        return null;
    }
    
    @Override
    public void updateMovement() {
    }
    
    static Object serialize(final String json) throws ReflectiveOperationException {
        return PacketBossBar.ChatSerializerMethodResolver.resolve(new ResolverQuery("a", new Class[] { String.class })).invoke(null, json);
    }
    
    static {
        PacketBossBar.nmsClassResolver = new NMSClassResolver();
        PacketBossBar.PacketPlayOutBoss = PacketBossBar.nmsClassResolver.resolveSilent("PacketPlayOutBoss");
        PacketBossBar.PacketPlayOutBossAction = PacketBossBar.nmsClassResolver.resolveSilent("PacketPlayOutBoss$Action");
        PacketBossBar.ChatSerializer = PacketBossBar.nmsClassResolver.resolveSilent("ChatSerializer", "IChatBaseComponent$ChatSerializer");
        PacketBossBar.BossBattleBarColor = PacketBossBar.nmsClassResolver.resolveSilent("BossBattle$BarColor");
        PacketBossBar.BossBattleBarStyle = PacketBossBar.nmsClassResolver.resolveSilent("BossBattle$BarStyle");
        PacketBossBar.PacketPlayOutBossFieldResolver = new FieldResolver(PacketBossBar.PacketPlayOutBoss);
        PacketBossBar.ChatSerializerMethodResolver = new MethodResolver(PacketBossBar.ChatSerializer);
    }

	@Override
	public void setTitle(BaseComponent paramString) {
		if (paramString == null) {
			this.message = ComponentSerializer.toString(new TextComponent("<none>"));
			return;
        }
        if (!ComponentSerializer.toString(paramString).equals(this.message)) {
            this.message = ComponentSerializer.toString(paramString);
            this.sendPacket(3, null);
        }
	}
}
