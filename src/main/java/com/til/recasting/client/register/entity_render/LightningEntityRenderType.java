package com.til.recasting.client.register.entity_render;

import com.til.glowing_fire_glow.client.register.entity_render.IndependenceEntityRenderRegister;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.recasting.client.render.entity.LightningEntityRender;
import com.til.recasting.common.entity.LightningEntity;
import com.til.recasting.common.register.entity_type.LightningEntityTypeRegister;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@VoluntarilyRegister
@OnlyIn(Dist.CLIENT)
public class LightningEntityRenderType extends IndependenceEntityRenderRegister<LightningEntity, LightningEntityTypeRegister, LightningEntityRender> {
}
