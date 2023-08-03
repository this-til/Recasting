package com.til.recasting.common.register.slash_blade.se.instance;

import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.ListUtil;
import com.til.glowing_fire_glow.common.util.MapUtil;
import com.til.glowing_fire_glow.common.util.math.NumberPack;
import com.til.recasting.common.capability.ISE;
import com.til.recasting.common.data.IRecipeInItemPack;
import com.til.recasting.common.data.IResultPack;
import com.til.recasting.common.entity.SlashEffectEntity;
import com.til.recasting.common.event.EventSlashBladeDoSlash;
import com.til.recasting.common.register.entity_type.SlashEffectEntityTypeRegister;
import com.til.recasting.common.register.recipe.SpecialRecipeSerializerRegister;
import com.til.recasting.common.register.slash_blade.se.SE_Register;
import com.til.recasting.common.register.world.item.SE_DepositItemRegister;
import mods.flammpfeil.slashblade.util.VectorHelper;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/***
 * 十字斩
 * 挥刀时追加一道剑气
 */
@VoluntarilyRegister
public class CrossChopSE extends SE_Register {

    @ConfigField
    protected NumberPack attackRatio;

    @SubscribeEvent
    protected void onEventSlashBladeDoSlash(EventSlashBladeDoSlash event) {
        if (!event.pack.getSlashBladePack().getIse().hasSE(this)) {
            return;
        }

        Vector3d pos = event.pack.getEntity().getPositionVec()
                .add(0.0D, (double) event.pack.getEntity().getEyeHeight() * 0.75D, 0.0D)
                .add(event.pack.getEntity().getLookVec().scale(0.3f));

        pos = pos.add(VectorHelper.getVectorForRotation(-90.0F, event.pack.getEntity().getYaw(0)).scale(event.centerOffset.y))
                .add(VectorHelper.getVectorForRotation(0, event.pack.getEntity().getYaw(0) + 90).scale(event.centerOffset.z))
                .add(event.pack.getEntity().getLookVec().scale(event.centerOffset.z));

        ISE.SE_Pack se_pack = event.pack.getSlashBladePack().getIse().getPack(this);
        SlashEffectEntity jc = new SlashEffectEntity(GlowingFireGlow.getInstance().getReflexManage().getVoluntarilyRegisterOfClass(SlashEffectEntityTypeRegister.class).getEntityType(),
                event.pack.getEntity().world, event.pack.getEntity());
        event.pack.getSlashBladePack().getSlashBladeStateSupplement().decorate(jc);
        jc.setPosition(pos.getX(), pos.getY(), pos.getZ());
        jc.setRoll(event.slashEffectEntity.getRoll() + 90);
        jc.setColor(event.pack.getSlashBladePack().getSlashBladeState().getColorCode());
        jc.setMute(event.slashEffectEntity.isMute());
        jc.setDamage((float) (event.slashEffectEntity.getDamage() * attackRatio.of(se_pack.getLevel())));
        jc.setBackRunPack(event.slashEffectEntity.getBackRunPack());
        jc.setSize(event.basicsRange);
        event.pack.getEntity().world.addEntity(jc);
    }

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        attackRatio = new NumberPack(0, 0.1);
    }

    @VoluntarilyRegister
    public static class CrossChopSE_Recipe extends SpecialRecipeSerializerRegister.SpecialRecipeRegister {

        @VoluntarilyAssignment
        protected KillingAuraSE killingAuraSE;

        @VoluntarilyAssignment
        protected CrossChopSE crossChopSE;

        @VoluntarilyAssignment
        protected SE_DepositItemRegister se_depositItemRegister;

        @Override
        protected SpecialRecipeSerializerRegister.SpecialRecipePack defaultSpecialRecipePackDelayed() {
            return new SpecialRecipeSerializerRegister.SpecialRecipePack(
                    ListUtil.of(
                            " A ",
                            "AVA",
                            " A "
                    ),
                    MapUtil.of(
                            "A", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(Items.END_CRYSTAL)),
                            "V", new IRecipeInItemPack.OfItemSE(killingAuraSE)
                    ),
                    new IResultPack.OfItemStack(se_depositItemRegister.mackItemStack(crossChopSE))

            );
        }
    }
}
