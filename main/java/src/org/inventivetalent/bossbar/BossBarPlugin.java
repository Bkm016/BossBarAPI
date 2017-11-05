package org.inventivetalent.bossbar;

import org.bukkit.plugin.java.*;
import org.bukkit.plugin.*;
import org.inventivetalent.apihelper.*;

public class BossBarPlugin extends JavaPlugin
{
    protected static Plugin instance;
    BossBarAPI apiInstance;
    
    public BossBarPlugin() {
        this.apiInstance = new BossBarAPI();
    }
    
    public void onLoad() {
        APIManager.registerAPI(this.apiInstance, this);
    }
    
    public void onEnable() {
        BossBarPlugin.instance = this;
        APIManager.initAPI(BossBarAPI.class);
    }
}
