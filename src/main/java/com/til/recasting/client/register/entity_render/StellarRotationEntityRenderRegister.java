package com.til.recasting.client.register.entity_render;

import com.til.glowing_fire_glow.client.register.entity_render.IndependenceEntityRenderRegister;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.recasting.client.render.entity.StellarRotationEntityRender;
import com.til.recasting.common.entity.JudgementCutEntity;
import com.til.recasting.common.entity.StellarRotationEntity;
import com.til.recasting.common.register.entity_type.StellarRotationEntityTypeRegister;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@VoluntarilyRegister
public class StellarRotationEntityRenderRegister extends IndependenceEntityRenderRegister<StellarRotationEntity, StellarRotationEntityTypeRegister, StellarRotationEntityRender<?>> {
}
