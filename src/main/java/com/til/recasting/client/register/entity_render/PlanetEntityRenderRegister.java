package com.til.recasting.client.register.entity_render;

import com.til.glowing_fire_glow.client.register.entity_render.IndependenceEntityRenderRegister;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.recasting.client.render.entity.PlanetEntityRender;
import com.til.recasting.common.entity.DriveEntity;
import com.til.recasting.common.entity.PlanetEntity;
import com.til.recasting.common.register.entity_type.PlanetEntityTypeRegister;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@VoluntarilyRegister
public class PlanetEntityRenderRegister extends IndependenceEntityRenderRegister<PlanetEntity, PlanetEntityTypeRegister, PlanetEntityRender<?>> {
}
