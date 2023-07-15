package com.til.recasting.common.register.slash_blade.se.instance;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.ListUtil;
import com.til.glowing_fire_glow.common.util.MapUtil;
import com.til.glowing_fire_glow.common.util.math.NumberPack;
import com.til.recasting.common.capability.ISE;
import com.til.recasting.common.data.IRecipeInItemPack;
import com.til.recasting.common.data.IResultPack;
import com.til.recasting.common.entity.SummondSwordEntity;
import com.til.recasting.common.event.EventDoAttack;
import com.til.recasting.common.register.entity_type.SummondSwordEntityTypeRegister;
import com.til.recasting.common.register.recipe.SpecialRecipeSerializerRegister;
import com.til.recasting.common.register.slash_blade.se.SE_Register;
import com.til.recasting.common.register.util.RayTraceUtil;
import com.til.recasting.common.register.world.item.SE_DepositItemRegister;
import mods.flammpfeil.slashblade.init.SBItems;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.Tags;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

/***
 * 冲击，造成伤害有几率召唤幻影剑造成瞬间伤害
 */
@VoluntarilyRegister
public class ImpactSE extends SE_Register {

    @ConfigField
    protected NumberPack probability;

    @ConfigField
    protected NumberPack attack;

    @VoluntarilyAssignment
    protected SummondSwordEntityTypeRegister summondSwordEntityTypeRegister;

    @SubscribeEvent
    protected void onEventSlashBladeDoSlash(EventDoAttack event) {
        if (!event.pack.slashBladePack.ise.hasSE(this)) {
            return;
        }
        ISE.SE_Pack se_pack = event.pack.slashBladePack.ise.getPack(this);
        if (event.pack.entity.getRNG().nextDouble() >= probability.of(se_pack.getLevel())) {
            return;
        }
        SummondSwordEntity summondSwordEntity = new SummondSwordEntity(summondSwordEntityTypeRegister.getEntityType(), event.pack.entity.world, event.pack.entity);
        event.pack.slashBladePack.iSlashBladeStateSupplement.decorate(summondSwordEntity);
        Vector3d pos = RayTraceUtil.getPosition(event.target);
        summondSwordEntity.lookAt(pos, false);
        summondSwordEntity.setPosition(pos.getX(), pos.getY(), pos.getZ());
        summondSwordEntity.setColor(event.pack.slashBladePack.slashBladeState.getColorCode());
        summondSwordEntity.setDamage(attack.of(se_pack.getLevel()));
        summondSwordEntity.setDelay(100);
        summondSwordEntity.doForceHitEntity(event.target);
        event.pack.entity.world.addEntity(summondSwordEntity);
        event.target.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 0.2F, 1.45F);
    }


    @Override
    public void defaultConfig() {
        super.defaultConfig();
        probability = new NumberPack(0, 0.07);
        attack = new NumberPack(0, 0.15);
    }

    @VoluntarilyRegister
    public static class ImpactSE_Recipe extends SpecialRecipeSerializerRegister.SpecialRecipeRegister {

        @VoluntarilyAssignment
        protected ImpactSE impactSE;

        @VoluntarilyAssignment
        protected SE_DepositItemRegister se_depositItemRegister;

        @Override
        protected SpecialRecipeSerializerRegister.SpecialRecipePack defaultSpecialRecipePackDelayed() {
            return new SpecialRecipeSerializerRegister.SpecialRecipePack(
                    ListUtil.of(
                            " AC",
                            "BVB",
                            "CA "
                    ),
                    MapUtil.of(
                            "A", new IRecipeInItemPack.OfTag(Tags.Items.GUNPOWDER.getName()),
                            "B", new IRecipeInItemPack.OfTag(Tags.Items.RODS_BLAZE.getName()),
                            "C", new IRecipeInItemPack.OfTag(Tags.Items.DUSTS_GLOWSTONE.getName()),
                            "V", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(SBItems.proudsoul))
                    ),
                    new IResultPack.OfItemStack(se_depositItemRegister.mackItemStack(impactSE))
            );
        }
    }

}
