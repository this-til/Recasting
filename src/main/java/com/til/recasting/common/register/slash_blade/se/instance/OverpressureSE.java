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
import com.til.recasting.common.event.EventDoAttack;
import com.til.recasting.common.register.attack_type.instance.LightningAttackType;
import com.til.recasting.common.register.recipe.SpecialRecipeSerializerRegister;
import com.til.recasting.common.register.slash_blade.se.SE_Register;
import com.til.recasting.common.register.world.item.SE_DepositItemRegister;
import mods.flammpfeil.slashblade.init.SBItems;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/***
 * 超压
 * 增加闪电伤害
 */
@VoluntarilyRegister
public class OverpressureSE extends SE_Register {

    @VoluntarilyAssignment
    protected LightningAttackType lightningAttackType;

    @ConfigField
    protected NumberPack attack;


    @SubscribeEvent
    protected void onEvent(EventDoAttack event) {
        if (!event.pack.getSlashBladePack().getIse().hasSE(this)) {
            return;
        }
        if (!event.attackTypeList.contains(lightningAttackType)) {
            return;
        }
        ISE.SE_Pack se_pack = event.pack.getSlashBladePack().getIse().getPack(this);
        event.modifiedRatio *= 1 + attack.of(se_pack.getLevel());
    }


    @Override
    public void defaultConfig() {
        super.defaultConfig();
        attack = new NumberPack(0, 0.35);
    }

    @VoluntarilyRegister
    public static class ShockSE_Recipe extends SpecialRecipeSerializerRegister.SpecialRecipeRegister {

        @VoluntarilyAssignment
        protected SE_DepositItemRegister se_depositItemRegister;

        @VoluntarilyAssignment
        protected OverpressureSE overpressureSE;


        @Override
        protected SpecialRecipeSerializerRegister.SpecialRecipePack defaultSpecialRecipePackDelayed() {
            return new SpecialRecipeSerializerRegister.SpecialRecipePack(
                    ListUtil.of(
                            "  A",
                            " V ",
                            "A  "
                    ),
                    MapUtil.of(
                            "V", new IRecipeInItemPack.OfItemEnchantment(Enchantments.CHANNELING),
                            "A", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(SBItems.proudsoul_crystal))
                    ),
                    new IResultPack.OfItemStack(se_depositItemRegister.mackItemStack(overpressureSE))
            );
        }
    }

}
