package com.github.neapovil.helmetdurability;

import com.github.neapovil.helmetdurability.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class HelmetDurability implements ClientModInitializer
{
    public static final Logger LOGGER = LoggerFactory.getLogger("HelmetDurability");
    public static HelmetDurability INSTANCE;

    @Override
    public void onInitializeClient()
    {
        INSTANCE = this;

        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
    }

    public ModConfig config()
    {
        return AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }
}
