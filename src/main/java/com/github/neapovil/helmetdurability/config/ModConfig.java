package com.github.neapovil.helmetdurability.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "helmetdurability")
public final class ModConfig implements ConfigData
{
    @ConfigEntry.Category("general")
    public boolean enabled = true;
}
