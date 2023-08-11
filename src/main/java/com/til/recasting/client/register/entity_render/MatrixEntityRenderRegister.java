package com.til.recasting.client.register.entity_render;

import com.til.glowing_fire_glow.client.register.entity_render.IndependenceEntityRenderRegister;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.recasting.client.render.entity.MatrixEntityRender;
import com.til.recasting.common.entity.MatrixEntity;
import com.til.recasting.common.register.entity_type.MatrixEntityTypeRegister;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@VoluntarilyRegister
@OnlyIn(Dist.CLIENT)
public class MatrixEntityRenderRegister extends IndependenceEntityRenderRegister<MatrixEntity, MatrixEntityTypeRegister, MatrixEntityRender<?>> {
}
