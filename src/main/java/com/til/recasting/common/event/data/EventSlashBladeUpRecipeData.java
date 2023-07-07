package com.til.recasting.common.event.data;

import com.til.recasting.common.register.recipe.SlashBladeUpRecipeRegister;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.Event;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class EventSlashBladeUpRecipeData extends Event {
    protected final Map<ResourceLocation, SlashBladeUpRecipeRegister.SlashBladeUpPack> map;

    public EventSlashBladeUpRecipeData(Map<ResourceLocation, SlashBladeUpRecipeRegister.SlashBladeUpPack> map) {
        this.map = map;
    }

    public void put(ResourceLocation name, SlashBladeUpRecipeRegister.SlashBladeUpPack pack) {
        map.put(name, pack);
    }
}
