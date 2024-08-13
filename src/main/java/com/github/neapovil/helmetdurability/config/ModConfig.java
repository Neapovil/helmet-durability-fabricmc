package com.github.neapovil.helmetdurability.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "helmetdurability")
public final class ModConfig implements ConfigData
{
    public boolean enabled = true;
}
