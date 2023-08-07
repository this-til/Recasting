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
import com.til.recasting.common.event.EventDoAttack;
import com.til.recasting.common.register.entity_type.SlashEffectEntityTypeRegister;
import com.til.recasting.common.register.recipe.SpecialRecipeSerializerRegister;
import com.til.recasting.common.register.slash_blade.se.SE_Register;
import com.til.recasting.common.register.world.item.SE_DepositItemRegister;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

/***
 * 杀戮光环
 * 对目标造成伤害后，追加一道剑气攻击目标
 */
@VoluntarilyRegister
public class KillingAuraSE extends SE_Register {

    @ConfigField
    protected NumberPack cool;

    @ConfigField
    protected NumberPack attack;

    @SubscribeEvent
    protected void onEventSlashBladeDoSlash(EventDoAttack event) {
        if (!event.pack.getSlashBladePack().getIse().hasSE(this)) {
            return;
        }
        ISE.SE_Pack se_pack = event.pack.getSlashBladePack().getIse().getPack(this);
        if (!se_pack.tryTime(event.pack.getEntity().world.getGameTime() , (long) cool.of(se_pack.getLevel()))) {
            return;
        }
        Vector3d attackPos = event.target.getPositionVec();
        Random random = event.pack.getEntity().getRNG();
        double x = random.nextDouble() * 2 - 1;
        double y = random.nextDouble() * 2 - 1;
        double z = random.nextDouble() * 2 - 1;
        double desiredLength = 4 * event.pack.getSlashBladePack().getSlashBladeStateSupplement().getAttackDistance();
        x *= desiredLength;
        y *= desiredLength;
        z *= desiredLength;
        Vector3d pos = attackPos.add(x, y, z);
        SlashEffectEntity jc = new SlashEffectEntity(GlowingFireGlow.getInstance().getReflexManage().getVoluntarilyRegisterOfClass(SlashEffectEntityTypeRegister.class).getEntityType(),
                event.pack.getEntity().world, event.pack.getEntity());
        event.pack.getSlashBladePack().getSlashBladeStateSupplement().decorate(jc);
        jc.setPosition(pos.getX(), pos.getY(), pos.getZ());
        jc.setRoll(random.nextInt(360));
        jc.lookAt(attackPos, false);
        jc.setColor(event.pack.getSlashBladePack().getSlashBladeState().getColorCode());
        jc.setMute(false);
        jc.setDamage((float) attack.of(se_pack.getLevel()));
        jc.setSize((float) (desiredLength / 4));
        event.pack.getEntity().world.addEntity(jc);
    }

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        cool = new NumberPack(10, -1);
        attack = new NumberPack(0, 0.2);
    }

    @VoluntarilyRegister
    public static class KillingAuraSE_Recipe extends SpecialRecipeSerializerRegister.SpecialRecipeRegister {

        @VoluntarilyAssignment
        protected CooperateWithSE cooperateWithSE;

        @VoluntarilyAssignment
        protected KillingAuraSE killingAuraSE;

        @VoluntarilyAssignment
        protected SE_DepositItemRegister se_depositItemRegister;

        @Override
        protected SpecialRecipeSerializerRegister.SpecialRecipePack defaultSpecialRecipePackDelayed() {
            return new SpecialRecipeSerializerRegister.SpecialRecipePack(
                    ListUtil.of(
                            "CAD",
                            "BVB",
                            "DAC"
                    ),
                    MapUtil.of(
                            "A", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(Items.IRON_SWORD)),
                            "B", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(Items.GOLDEN_SWORD)),
                            "C", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(Items.DIAMOND_SWORD)),
                            "D", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(Items.NETHERITE_SWORD)),
                            "V", new IRecipeInItemPack.OfItemSE(cooperateWithSE)
                    ),
                    new IResultPack.OfItemStack(se_depositItemRegister.mackItemStack(killingAuraSE))

            );
        }
    }
}
