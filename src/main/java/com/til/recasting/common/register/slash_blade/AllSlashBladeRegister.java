package com.til.recasting.common.register.slash_blade;

import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.common.register.RegisterManage;
import mods.flammpfeil.slashblade.client.renderer.SlashBladeTEISR;
import mods.flammpfeil.slashblade.client.renderer.model.BladeModel;
import mods.flammpfeil.slashblade.init.SBItems;
import mods.flammpfeil.slashblade.item.ItemTierSlashBlade;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.IItemTier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * @author til
 */
public class AllSlashBladeRegister extends RegisterManage<SlashBladeRegister> {

    @Override
    public void init(InitType initType) {
        switch (initType) {
            case NEW:
                GlowingFireGlow.getInstance().modEventBus.addListener(EventPriority.HIGHEST, this::onModelBakeEvent);
                break;
            case FML_COMMON_SETUP:
                break;
            case FML_DEDICATED_SERVER_SETUP:
                break;
            case FML_CLIENT_SETUP:
                break;
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected void onModelBakeEvent(final ModelBakeEvent event) {
        for (SlashBladeRegister slashBladeRegister : this.forAll()) {
            ModelResourceLocation loc = new ModelResourceLocation(
                    slashBladeRegister.getName(), "inventory");
            BladeModel model = new BladeModel(event.getModelRegistry().get(loc), event.getModelLoader());
            event.getModelRegistry().put(loc, model);
        }
    }

}
