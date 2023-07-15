package com.til.recasting.common.event.data;

import com.til.recasting.common.register.recipe.SlashBladeRecipeSerializerRegister;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.Event;

import java.util.Map;

public class EventSlashBladeUpRecipeData extends Event {
    protected final Map<ResourceLocation, SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack> map;

    public EventSlashBladeUpRecipeData(Map<ResourceLocation, SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack> map) {
        this.map = map;
    }

    public void put(ResourceLocation name, SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack pack) {
        map.put(name, pack);
    }
}
