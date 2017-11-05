package org.inventivetalent.apihelper;

import org.bukkit.plugin.*;

public interface API
{
    void load();
    
    void init(final Plugin p0);
    
    void disable(final Plugin p0);
}
