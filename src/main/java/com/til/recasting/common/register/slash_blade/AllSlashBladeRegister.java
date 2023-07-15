package com.til.recasting.common.register.slash_blade;

import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.common.register.RegisterManage;
import mods.flammpfeil.slashblade.client.renderer.model.BladeModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.eventbus.api.EventPriority;

/**
 * @author til
 */
public class AllSlashBladeRegister extends RegisterManage<SlashBladeRegister> {

    /*@Override
    public void initNew() {
        super.initNew();
        GlowingFireGlow.getInstance().modEventBus.addListener(EventPriority.HIGHEST, this::onModelBakeEvent);

    }*/

   /* @OnlyIn(Dist.CLIENT)
    protected void onModelBakeEvent(final ModelBakeEvent event) {
        for (SlashBladeRegister slashBladeRegister : this.forAll()) {
            ModelResourceLocation loc = new ModelResourceLocation(
                    slashBladeRegister.getName(), "inventory");
            BladeModel model = new BladeModel(event.getModelRegistry().get(loc), event.getModelLoader());
            event.getModelRegistry().put(loc, model);
        }
    }*/

}
