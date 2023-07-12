package com.til.recasting.common.event.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.til.glowing_fire_glow.common.config.ConfigManage;
import com.til.glowing_fire_glow.common.main.IWorldComponent;
import com.til.glowing_fire_glow.common.register.StaticVoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.util.IOUtil;
import com.til.glowing_fire_glow.common.util.gson.GsonManage;
import com.til.recasting.Recasting;
import com.til.recasting.common.register.recipe.SlashBladeUpRecipeRegister;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class RecastingDataGenerate implements IWorldComponent {

    @VoluntarilyAssignment
    protected SlashBladeUpRecipeRegister slashBladeUpRecipeRegister;

    @VoluntarilyAssignment
    protected GsonManage gsonManage;

    @Override
    public void registerModEvent(IEventBus eventBus) {
        eventBus.addListener(this::onEvent);
    }

    @SubscribeEvent
    protected void onEvent(GatherDataEvent event) {
        MinecraftForge.EVENT_BUS.start();
        //ModLoader modLoader = ModLoader.get();
        event.getGenerator().addProvider(new IDataProvider() {
            @Override
            public void act(DirectoryCache cache) throws IOException {
                Map<ResourceLocation, SlashBladeUpRecipeRegister.SlashBladeUpPack> map = new HashMap<>();
                MinecraftForge.EVENT_BUS.post(new EventSlashBladeUpRecipeData(map));
                for (Map.Entry<ResourceLocation, SlashBladeUpRecipeRegister.SlashBladeUpPack> entry : map.entrySet()) {
                    JsonObject jsonObject = ((JsonObject) gsonManage.getGson().toJsonTree(entry.getValue()));
                    jsonObject.addProperty("type", slashBladeUpRecipeRegister.getName().toString());
                    Path mainOutput = event.getGenerator().getOutputFolder();
                    String pathSuffix = String.format("data/%s/recipes/%s.json",
                            entry.getKey().getNamespace(),
                            entry.getKey().getPath());
                    Path outputPath = mainOutput.resolve(pathSuffix);
                    String text = gsonManage.getGson().toJson(jsonObject);
                    String textHas = HASH_FUNCTION.hashUnencodedChars(text).toString();
                    IOUtil.writer(outputPath.toFile(), text);
                    cache.recordHash(outputPath, textHas);
                }

            }

            @Override
            public String getName() {
                return "SlashBladeUpRecipe";
            }
        });
        try {
            event.getGenerator().run();
        } catch (Exception e) {
            Recasting.LOGGER.error("生成数据错误", e);
        }

    }


}
