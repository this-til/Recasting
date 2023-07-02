package com.til.recasting.common.register.world.item;

import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.world.item.ItemRegister;
import mods.flammpfeil.slashblade.SlashBlade;
import net.minecraft.item.Item;

public abstract class SoulItemRegister extends ItemRegister {


    @Override
    protected Item initItem() {
        return new Item(new Item.Properties().group(SlashBlade.SLASHBLADE));
    }

    @VoluntarilyRegister(priority = 160)
    public static class Soul_1_ItemRegister extends SoulItemRegister {
    }

    @VoluntarilyRegister(priority = 150)
    public static class Soul_2_ItemRegister extends SoulItemRegister {
    }

    @VoluntarilyRegister(priority = 140)
    public static class Soul_3_ItemRegister extends SoulItemRegister {
    }

    @VoluntarilyRegister(priority = 130)
    public static class Soul_4_ItemRegister extends SoulItemRegister {
    }

    @VoluntarilyRegister(priority = 120)
    public static class Soul_5_ItemRegister extends SoulItemRegister {
    }

    @VoluntarilyRegister(priority = 110)
    public static class Soul_6_ItemRegister extends SoulItemRegister {
    }

    @VoluntarilyRegister(priority = 100)
    public static class Soul_7_ItemRegister extends SoulItemRegister {
    }

    @VoluntarilyRegister(priority = 90)
    public static class Soul_8_ItemRegister extends SoulItemRegister {
    }
}
