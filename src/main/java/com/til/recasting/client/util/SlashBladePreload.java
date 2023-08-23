package com.til.recasting.client.util;

import com.til.glowing_fire_glow.common.main.IWorldComponent;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.recasting.client.register.overall_config.PreloadOverallConfigRegister;
import com.til.recasting.common.data.SlashBladePack;
import com.til.recasting.common.register.slash_blade.AllSlashBladeRegister;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import mods.flammpfeil.slashblade.client.renderer.model.BladeModelManager;
import mods.flammpfeil.slashblade.client.renderer.model.obj.WavefrontObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;

@OnlyIn(Dist.CLIENT)
public class SlashBladePreload implements IWorldComponent {

    @VoluntarilyAssignment
    protected AllSlashBladeRegister allSlashBladeRegister;

    @VoluntarilyAssignment
    protected PreloadOverallConfigRegister preloadOverallConfigRegister;

    @Override
    public void initClientSetup() {
        IWorldComponent.super.initClientSetup();
        if (!preloadOverallConfigRegister.isPreload()) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        TextureManager textureManager = minecraft.getTextureManager();
        for (SlashBladeRegister slashBladeRegister : allSlashBladeRegister.forAll()) {

            if (slashBladeRegister.getTexture() != null) {
                textureManager.bindTexture(slashBladeRegister.getTexture());
            }
            if (slashBladeRegister.getModel() != null) {
                BladeModelManager.getInstance().getModel(slashBladeRegister.getModel());
            }
            if (slashBladeRegister.getSummondSwordTexture() != null) {
                textureManager.bindTexture(slashBladeRegister.getSummondSwordTexture());
            }
            if (slashBladeRegister.getSummondSwordModel() != null) {
                BladeModelManager.getInstance().getModel(slashBladeRegister.getSummondSwordModel());
            }
            if (slashBladeRegister.getJudgementCutTexture() != null) {
                textureManager.bindTexture(slashBladeRegister.getJudgementCutTexture());
            }
            if (slashBladeRegister.getJudgementCutModel() != null) {
                BladeModelManager.getInstance().getModel(slashBladeRegister.getJudgementCutModel());
            }
            if (slashBladeRegister.getSlashEffectTexture() != null) {
                textureManager.bindTexture(slashBladeRegister.getSlashEffectTexture());
            }

        }
    }


}
