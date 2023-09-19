package com.til.recasting.common.register.slash_blade;

import com.til.glowing_fire_glow.common.register.RegisterManage;

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
