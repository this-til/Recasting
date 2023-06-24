package com.til.recasting;

import com.til.glowing_fire_glow.GlowingFireGlow;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author til
 */
@Mod(Recasting.MOD_ID)
@GlowingFireGlow.Manage
public class Recasting {

    public static final String MOD_ID = "recasting";
    public static final String MOD_MANE = "Recasting";
    public static final Logger LOGGER = LogManager.getLogger();
    public final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

    public Recasting() {
        modEventBus.register(this);
    }

}
