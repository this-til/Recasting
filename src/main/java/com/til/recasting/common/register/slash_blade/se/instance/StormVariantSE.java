package com.til.recasting.common.register.slash_blade.se.instance;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.ListUtil;
import com.til.glowing_fire_glow.common.util.MapUtil;
import com.til.glowing_fire_glow.common.util.RandomUtil;
import com.til.glowing_fire_glow.common.util.math.NumberPack;
import com.til.recasting.common.capability.ISE;
import com.til.recasting.common.data.IRecipeInItemPack;
import com.til.recasting.common.data.IResultPack;
import com.til.recasting.common.entity.SummondSwordEntity;
import com.til.recasting.common.event.EventDoJudgementCut;
import com.til.recasting.common.register.entity_type.SummondSwordEntityTypeRegister;
import com.til.recasting.common.register.recipe.SpecialRecipeSerializerRegister;
import com.til.recasting.common.register.slash_blade.se.SE_Register;
import com.til.recasting.common.register.world.item.SE_DepositItemRegister;
import mods.flammpfeil.slashblade.init.SBItems;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.Tags;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

/***
 * 风暴.变体
 * 触发审判时，召唤幻影剑进行攻击
 */
@VoluntarilyRegister
public class StormVariantSE extends SE_Register {

    @ConfigField
    protected NumberPack attack;

    @ConfigField
    protected NumberPack number;

    @VoluntarilyAssignment
    protected SummondSwordEntityTypeRegister summondSwordEntityTypeRegister;

    @SubscribeEvent
    protected void onEventDoJudgementCut(EventDoJudgementCut event) {
        if (!event.pack.getSlashBladePack().getIse().hasSE(this)) {
            return;
        }
        ISE.SE_Pack se_pack = event.pack.getSlashBladePack().getIse().getPack(this);
        Random random = event.pack.getEntity().getRNG();
        int n = (int) number.of(se_pack.getLevel());
        float a = (float) attack.of(se_pack.getLevel());
        for (int i = 0; i < n; i++) {
            SummondSwordEntity summondSwordEntity = new SummondSwordEntity(summondSwordEntityTypeRegister.getEntityType(), event.pack.getEntity().world, event.pack.getEntity());
            event.pack.getSlashBladePack().getSlashBladeStateSupplement().decorate(summondSwordEntity);
            Vector3d pos = event.pos.add(0, 8, 0).add(RandomUtil.nextVector3dInCircles(random, 4.5));
            summondSwordEntity.setPosition(pos.getX(), pos.getY(), pos.getZ());
            summondSwordEntity.lookAt(event.pos, false);
            summondSwordEntity.setColor(event.pack.getSlashBladePack().getSlashBladeState().getColorCode());
            summondSwordEntity.setDamage(a);
            summondSwordEntity.setStartDelay(random.nextInt(10));
            summondSwordEntity.setRoll(random.nextInt(360));
            event.pack.getEntity().world.addEntity(summondSwordEntity);
        }
        event.pack.getEntity().playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 0.2F, 1.45F);
    }

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        attack = new NumberPack(0, 0.05);
        number = new NumberPack(2, 1);
    }

    @VoluntarilyRegister
    public static class StormVariantSERecipe extends SpecialRecipeSerializerRegister.SpecialRecipeRegister {

        @VoluntarilyAssignment
        protected SE_DepositItemRegister se_depositItemRegister;

        @VoluntarilyAssignment
        protected StormSE stormSE;

        @VoluntarilyAssignment
        protected StormVariantSE stormVariantSE;

        @Override
        protected SpecialRecipeSerializerRegister.SpecialRecipePack defaultSpecialRecipePackDelayed() {
            return new SpecialRecipeSerializerRegister.SpecialRecipePack(
                    ListUtil.of(
                            "ACA",
                            "BVB",
                            "ACA"
                    ),
                    MapUtil.of(
                            "A", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(SBItems.proudsoul_tiny)),
                            "B", new IRecipeInItemPack.OfTag(Tags.Items.GEMS_DIAMOND.getName()),
                            "C", new IRecipeInItemPack.OfTag(Tags.Items.FEATHERS.getName()),
                            "V", new IRecipeInItemPack.OfItemSE(stormSE)
                    ),
                    new IResultPack.OfItemStack(se_depositItemRegister.mackItemStack(stormVariantSE))
            );
        }
    }

}
