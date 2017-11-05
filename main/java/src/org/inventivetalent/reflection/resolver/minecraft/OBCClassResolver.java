package org.inventivetalent.reflection.resolver.minecraft;

import org.inventivetalent.reflection.resolver.*;
import org.inventivetalent.reflection.minecraft.*;

public class OBCClassResolver extends ClassResolver
{
    @Override
    public Class resolve(final String... names) throws ClassNotFoundException {
        for (int i = 0; i < names.length; ++i) {
            if (!names[i].startsWith("org.bukkit.craftbukkit")) {
                names[i] = "org.bukkit.craftbukkit." + Minecraft.getVersion() + names[i];
            }
        }
        return super.resolve(names);
    }
}
