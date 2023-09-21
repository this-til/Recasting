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
import com.til.recasting.common.register.attack_type.instance.SlashBladeAttackType;
import com.til.recasting.common.register.recipe.SpecialRecipeSerializerRegister;
import com.til.recasting.common.register.slash_blade.se.SE_Register;
import com.til.recasting.common.register.world.item.SE_DepositItemRegister;
import mods.flammpfeil.slashblade.init.SBItems;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/***
 * 锋利
 * 增加挥刀的伤害
 */
@VoluntarilyRegister
public class SharpSE extends SE_Register {


    @VoluntarilyAssignment
    protected SlashBladeAttackType slashBladeAttackType;

    @ConfigField
    protected NumberPack attack;


    @SubscribeEvent
    protected void onEvent(EventDoAttack event) {
        if (!event.pack.getSlashBladePack().getIse().hasSE(this)) {
            return;
        }
        if (!event.attackTypeList.contains(slashBladeAttackType)) {
            return;
        }
        ISE.SE_Pack se_pack = event.pack.getSlashBladePack().getIse().getPack(this);
        event.modifiedRatio *= 1 + attack.of(se_pack.getLevel());
    }


    @Override
    public void defaultConfig() {
        super.defaultConfig();
        attack = new NumberPack(0, 0.2);
    }

    @VoluntarilyRegister
    public static class SharpSE_Recipe extends SpecialRecipeSerializerRegister.SpecialRecipeRegister {

        @VoluntarilyAssignment
        protected SE_DepositItemRegister se_depositItemRegister;

        @VoluntarilyAssignment
        protected SharpSE sharpSE;


        @Override
        protected SpecialRecipeSerializerRegister.SpecialRecipePack defaultSpecialRecipePackDelayed() {
            return new SpecialRecipeSerializerRegister.SpecialRecipePack(
                    ListUtil.of(
                            "  A",
                            " V ",
                            "A  "
                    ),
                    MapUtil.of(
                            "V", new IRecipeInItemPack.OfItemEnchantment(Enchantments.SHARPNESS),
                            "A", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(SBItems.proudsoul_crystal))
                    ),
                    new IResultPack.OfItemStack(se_depositItemRegister.mackItemStack(sharpSE))
            );
        }
    }

}
