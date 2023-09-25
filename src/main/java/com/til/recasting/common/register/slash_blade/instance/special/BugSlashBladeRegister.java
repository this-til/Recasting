package com.til.recasting.common.register.slash_blade.instance.special;

import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.recasting.common.data.SlashBladePack;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import com.til.recasting.common.register.slash_blade.se.AllSE_Register;
import com.til.recasting.common.register.slash_blade.se.SE_Register;
import mods.flammpfeil.slashblade.SlashBlade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;


@VoluntarilyRegister
public class BugSlashBladeRegister extends SlashBladeRegister {

    @VoluntarilyAssignment
    protected AllSE_Register allSE_register;

    @Override
    protected void init() {
        super.init();
        model = new ResourceLocation(SlashBlade.modid, "model/blade.obj");
        texture = new ResourceLocation(SlashBlade.modid, "model/blade.png");
    }

    @Override
    protected void defaultItemStackConfig(ItemStack itemStack) {
        super.defaultItemStackConfig(itemStack);
        slashBladePack.getSlashBladeState().setTranslationKey("");
        slashBladePack.getSlashBladeState().setBaseAttackModifier(7f);
    }

    @Override
    public SlashBladePack getSlashBladePack() {
        SlashBladePack slashBladePack = super.getSlashBladePack();
        for (SE_Register se_register : allSE_register.forAll()) {
            slashBladePack.getIse().getPack(se_register).setLevel(se_register.getMaxLevel());
        }
        return slashBladePack;
    }
}
