package com.til.recasting.common.register.slash_blade.instance.yamazakura;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.ListUtil;
import com.til.glowing_fire_glow.common.util.MapUtil;
import com.til.recasting.common.data.IRecipeInItemPack;
import com.til.recasting.common.data.IResultPack;
import com.til.recasting.common.data.SlashBladePack;
import com.til.recasting.common.data.UseSlashBladeEntityPack;
import com.til.recasting.common.register.back_type.JudgementCutBackTypeRegister;
import com.til.recasting.common.register.recipe.SlashBladeRecipeSerializerRegister;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import com.til.recasting.common.register.slash_blade.instance.original.NamelessSlashBladeRegister;
import com.til.recasting.common.register.slash_blade.sa.SA_Register;
import com.til.recasting.common.register.slash_blade.sa.instance.DriveSA;
import com.til.recasting.common.register.slash_blade.sa.instance.EndingYanSakuraSA;
import com.til.recasting.common.register.util.AttackManager;
import com.til.recasting.common.register.util.JudgementCutManage;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.entity.EntitySlashEffect;
import mods.flammpfeil.slashblade.init.SBItems;
import mods.flammpfeil.slashblade.specialattack.JudgementCut;
import mods.flammpfeil.slashblade.util.KnockBacks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.Tags;

import java.awt.*;

@VoluntarilyRegister
public class KannazukiSlashBladeRegister extends SlashBladeRegister {


    @VoluntarilyAssignment
    protected KannazukiSA kannazukiSA;

    @Override
    protected void defaultItemStackConfig(ItemStack itemStack) {
        super.defaultItemStackConfig(itemStack);
        slashBladePack.getSlashBladeState().setBaseAttackModifier(9);
        slashBladePack.getSlashBladeState().setEffectColor(new Color(6815705));
        slashBladePack.setSA(kannazukiSA);
    }


    @VoluntarilyRegister
    public static class KannazukiSA extends SA_Register {

        @ConfigField
        protected float attackBasics;

        @ConfigField
        protected float attackSlash;

        @ConfigField
        protected int life;

        @ConfigField
        protected int interval;

        @ConfigField
        protected float size;

        @VoluntarilyAssignment
        protected JudgementCutBackTypeRegister.JudgementCutTickBackTypeRegister judgementCutTickBackTypeRegister;

        @VoluntarilyAssignment
        protected EndingYanSakuraSA endingYanSakuraSA;

        @Override
        public void trigger(UseSlashBladeEntityPack useSlashBladeEntityPack) {
            endingYanSakuraSA.trigger(useSlashBladeEntityPack);
            JudgementCutManage.doJudgementCut(
                    useSlashBladeEntityPack.getEntity(),
                    attackBasics,
                    life,
                    null,
                    null,
                    judgementCutEntity -> {
                        judgementCutEntity.setSize(size);
                        judgementCutEntity.getBackRunPack().addRunBack(judgementCutTickBackTypeRegister, judgementCutEntity1 -> {
                            if (judgementCutEntity1.ticksExisted % interval == 0) {
                                AttackManager.doSlash(
                                        useSlashBladeEntityPack.getEntity(),
                                        30,
                                        useSlashBladeEntityPack.getSlashBladePack().getSlashBladeState().getColorCode(),
                                        Vector3d.ZERO,
                                        false,
                                        true,
                                        attackSlash,
                                        0.5f * size,
                                        slashEffectEntity -> {
                                            slashEffectEntity.setPositionAndRotation(
                                                    judgementCutEntity1.getPosX(),
                                                    judgementCutEntity1.getPosY(),
                                                    judgementCutEntity1.getPosZ(),
                                                    120.0F * (float) judgementCutEntity1.ticksExisted, 0.0F);
                                        }
                                );
                            }
                        });
                    }
            );
        }

        @Override
        public void defaultConfig() {
            super.defaultConfig();
            attackBasics = 0.45f;
            attackSlash = 0.25f;
            life = 40;
            interval = 5;
            size = 2;
        }
    }

    @VoluntarilyRegister
    public static class HazukiSlashBladeRecipeRegister extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {
        @VoluntarilyAssignment
        protected KannazukiSlashBladeRegister kannazukiSlashBladeRegister;

        @VoluntarilyAssignment
        protected HazukiSlashBladeRegister hazukiSlashBladeRegister;

        @VoluntarilyAssignment
        protected NagatsukiSlashBladeRegister nagatsukiSlashBladeRegister;

        @VoluntarilyAssignment
        protected FumizukiSlashBladeRegister fumizukiSlashBladeRegister;


        @Override
        protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {


            SlashBladePack fumizukiSlashBlade = fumizukiSlashBladeRegister.getSlashBladePack();
            fumizukiSlashBlade.getSlashBladeState().setKillCount(1000);
            fumizukiSlashBlade.getSlashBladeState().setRefine(100);

            SlashBladePack hazukiSlashBlade = hazukiSlashBladeRegister.getSlashBladePack();
            hazukiSlashBlade.getSlashBladeState().setKillCount(500);
            hazukiSlashBlade.getSlashBladeState().setRefine(50);

            SlashBladePack nagatsukiSlashBlade = nagatsukiSlashBladeRegister.getSlashBladePack();
            nagatsukiSlashBlade.getSlashBladeState().setKillCount(500);
            nagatsukiSlashBlade.getSlashBladeState().setRefine(50);

            return new SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack(
                    ListUtil.of(
                            "AAA",
                            "BVC",
                            "AAA"
                    ),
                    MapUtil.of(
                            "A", new IRecipeInItemPack.OfTag(Tags.Items.STORAGE_BLOCKS_EMERALD.getName()),
                            "V", new IRecipeInItemPack.OfSlashBlade(fumizukiSlashBlade),
                            "B", new IRecipeInItemPack.OfSlashBlade(hazukiSlashBlade),
                            "C", new IRecipeInItemPack.OfSlashBlade(nagatsukiSlashBlade)
                    ),
                    "V",
                    new IResultPack.OfSlashBladeRegister(kannazukiSlashBladeRegister)
            );
        }
    }
}
