package org.inventivetalent.bossbar;

import org.bukkit.scheduler.*;
import org.bukkit.entity.*;
import java.util.*;

public class BossBarTimer extends BukkitRunnable
{
    private PacketBossBar bossBar;
    final float progressMinus;
    
    public BossBarTimer(final PacketBossBar packetBossBar, final float progress, final int timeout) {
        this.bossBar = packetBossBar;
        this.progressMinus = progress / timeout;
    }
    
    public void run() {
        final float newProgress = this.bossBar.getProgress() - this.progressMinus;
        if (newProgress <= 0.0f) {
            for (final Player player : this.bossBar.getPlayers()) {
                this.bossBar.removePlayer(player);
            }
        }
        else {
            this.bossBar.setProgress(newProgress);
        }
    }
}
