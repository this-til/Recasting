package com.til.recasting.client.register.entity_render;

import com.til.glowing_fire_glow.client.register.entity_render.IndependenceEntityRenderRegister;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.recasting.client.render.entity.JudgementCutEntityRender;
import com.til.recasting.client.render.entity.SummondSwordEntityRender;
import com.til.recasting.common.entity.JudgementCutEntity;
import com.til.recasting.common.entity.SummondSwordEntity;
import com.til.recasting.common.register.entity_type.JudgementCutEntityTypeRegister;
import com.til.recasting.common.register.entity_type.SummondSwordEntityTypeRegister;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author til
 */
@OnlyIn(Dist.CLIENT)
@VoluntarilyRegister
public class JudgementCutEntityRenderRegister extends IndependenceEntityRenderRegister<JudgementCutEntity, JudgementCutEntityTypeRegister, JudgementCutEntityRender<?>> {
}
