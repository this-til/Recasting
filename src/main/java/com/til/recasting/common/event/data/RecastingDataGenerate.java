package com.til.recasting.common.event.data;

import com.google.gson.JsonObject;
import com.til.glowing_fire_glow.common.main.IWorldComponent;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.util.IOUtil;
import com.til.glowing_fire_glow.common.util.gson.GsonManage;
import com.til.recasting.Recasting;
import com.til.recasting.common.register.recipe.SlashBladeRecipeSerializerRegister;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class RecastingDataGenerate implements IWorldComponent {

    @VoluntarilyAssignment
    protected SlashBladeRecipeSerializerRegister slashBladeUpRecipeRegister;

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
                Map<ResourceLocation, SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack> map = new HashMap<>();
                MinecraftForge.EVENT_BUS.post(new EventSlashBladeUpRecipeData(map));
                for (Map.Entry<ResourceLocation, SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack> entry : map.entrySet()) {
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
