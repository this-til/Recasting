package com.til.recasting.client.register.entity_render;

import com.til.glowing_fire_glow.client.register.entity_render.IndependenceEntityRenderRegister;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.recasting.client.render.entity.StrengthenBladeStandEntityRender;
import com.til.recasting.common.entity.StrengthenBladeStandEntity;
import com.til.recasting.common.register.entity_type.StrengthenBladeStandEntityTypeRegister;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author til
 */
@VoluntarilyRegister
@OnlyIn(Dist.CLIENT)
public class StrengthenBladeStandEntityRenderRegister extends IndependenceEntityRenderRegister<StrengthenBladeStandEntity, StrengthenBladeStandEntityTypeRegister, StrengthenBladeStandEntityRender<?>> {
}
