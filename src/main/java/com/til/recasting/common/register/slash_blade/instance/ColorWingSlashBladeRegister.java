package com.til.recasting.common.register.slash_blade.instance;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.ListUtil;
import com.til.glowing_fire_glow.common.util.MapUtil;
import com.til.recasting.common.capability.ISE;
import com.til.recasting.common.data.IRecipeInItemPack;
import com.til.recasting.common.data.IResultPack;
import com.til.recasting.common.data.SlashBladePack;
import com.til.recasting.common.data.UseSlashBladeEntityPack;
import com.til.recasting.common.entity.LightningEntity;
import com.til.recasting.common.entity.SummondSwordEntity;
import com.til.recasting.common.event.EventDoAttack;
import com.til.recasting.common.event.EventSlashBladeDoSlash;
import com.til.recasting.common.register.back_type.SummondSwordBackTypeRegister;
import com.til.recasting.common.register.entity_type.LightningEntityTypeRegister;
import com.til.recasting.common.register.entity_type.SummondSwordEntityTypeRegister;
import com.til.recasting.common.register.recipe.SlashBladeRecipeSerializerRegister;
import com.til.recasting.common.register.recipe.SpecialRecipeSerializerRegister;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import com.til.recasting.common.register.slash_blade.sa.SA_Register;
import com.til.recasting.common.register.slash_blade.se.SE_Register;
import com.til.recasting.common.register.world.item.SE_DepositItemRegister;
import com.til.recasting.common.register.world.item.SoulItemRegister;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.awt.*;
import java.util.Random;

@VoluntarilyRegister
public class ColorWingSlashBladeRegister extends SlashBladeRegister {

    @VoluntarilyAssignment
    protected HeavenTwelveHitSA heavenTwelveHitSA;

    @VoluntarilyAssignment
    protected ColorWingSlashBladeSE colorWingSlashBladeSE;


    @Override
    protected void defaultItemStackConfig(ItemStack itemStack) {
        super.defaultItemStackConfig(itemStack);
        slashBladePack.getSlashBladeState().setBaseAttackModifier(8f);
        slashBladePack.setSA(heavenTwelveHitSA);
        slashBladePack.getIse().getPack(colorWingSlashBladeSE).setLevel(1);
        slashBladePack.getSlashBladeStateSupplement().setDurable(14f);
    }

    @VoluntarilyRegister
    public static class ColorWingSlashBladeSE extends SE_Register {
        protected final Random random = new Random();


        @ConfigField
        protected float amplifyProbability;

        @ConfigField
        protected float amplifyAttack;

        @SubscribeEvent
        protected void onEventSlashBladeDoSlash(EventSlashBladeDoSlash event) {
            if (!event.pack.getSlashBladePack().getIse().hasSE(this)) {
                return;
            }
            event.pack.getSlashBladePack().getSlashBladeState().setColorCode(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)).getRGB());
        }

        @SubscribeEvent
        protected void onEventDoAttack(EventDoAttack event) {
            if (!event.pack.getSlashBladePack().getIse().hasSE(this)) {
                return;
            }
            if (event.pack.getEntity().getRNG().nextDouble() > amplifyProbability) {
                return;
            }
            event.modifiedRatio += amplifyAttack;
        }

        @Override
        public int getMaxLevel() {
            return 1;
        }

        @Override
        public void defaultConfig() {
            super.defaultConfig();
            amplifyProbability = 0.2f;
            amplifyAttack = 0.35f;
        }

        @VoluntarilyRegister
        public static class ColorWingSlashBladeSE_RecipeRegister extends SpecialRecipeSerializerRegister.SpecialRecipeRegister {

            @VoluntarilyAssignment
            protected SoulItemRegister.SoulCubeItemRegister soulCubeItemRegister;

            @VoluntarilyAssignment
            protected HeavenTwelveHitSA heavenTwelveHitSA;


            @VoluntarilyAssignment
            protected SE_DepositItemRegister se_depositItemRegister;

            @VoluntarilyAssignment
            protected ColorWingSlashBladeSE colorWingSlashBladeSE;

            @Override
            protected SpecialRecipeSerializerRegister.SpecialRecipePack defaultSpecialRecipePackDelayed() {
                return new SpecialRecipeSerializerRegister.SpecialRecipePack(
                        ListUtil.of(
                                " A ",
                                " B ",
                                "   "),
                        MapUtil.of(
                                "A", new IRecipeInItemPack.OfItemSA(heavenTwelveHitSA),
                                "B", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(soulCubeItemRegister.getItem()))
                        ),
                        new IResultPack.OfItemStack(se_depositItemRegister.mackItemStack(colorWingSlashBladeSE))
                );
            }
        }
    }

    @VoluntarilyRegister
    public static class HeavenTwelveHitSA extends SA_Register {

        @VoluntarilyAssignment
        protected SummondSwordEntityTypeRegister summondSwordEntityTypeRegister;

        @VoluntarilyAssignment
        protected LightningEntityTypeRegister lightningEntityTypeRegister;

        @VoluntarilyAssignment
        protected SummondSwordBackTypeRegister.SummondSwordAttackBackTypeRegister attackBackTypeRegister;

        @ConfigField
        protected int lightningNumber;

        @ConfigField
        protected float attack;

        @ConfigField
        protected float lightningAttack;

        @Override
        public void trigger(UseSlashBladeEntityPack slashBladeEntityPack) {
            Vector3d attackPos = slashBladeEntityPack.getAttackPos();

            for (int i = 0; i < lightningNumber; i++) {
                SummondSwordEntity summondSwordEntity = new SummondSwordEntity(summondSwordEntityTypeRegister.getEntityType(), slashBladeEntityPack.getEntity().world, slashBladeEntityPack.getEntity());
                summondSwordEntity.setColor(slashBladeEntityPack.getSlashBladePack().getSlashBladeState().getColorCode());
                summondSwordEntity.setSize(1.25f);
                summondSwordEntity.setDamage(attack);
                //summondSwordEntity.setStartDelay(i * 2);
                summondSwordEntity.lookAt(attackPos, false);
                summondSwordEntity.getBackRunPack().addRunBack(attackBackTypeRegister,
                        (summondSwordEntity1, hitEntity) -> {
                            LightningEntity lightningEntity = new LightningEntity(lightningEntityTypeRegister.getEntityType(), slashBladeEntityPack.getEntity().world, slashBladeEntityPack.getEntity());
                            lightningEntity.setPosition(hitEntity.getPosX(), hitEntity.getPosY(), hitEntity.getPosZ());
                            lightningEntity.setColor(slashBladeEntityPack.getSlashBladePack().getSlashBladeState().getColorCode());
                            lightningEntity.setDamage(lightningAttack);
                            slashBladeEntityPack.getEntity().world.addEntity(lightningEntity);
                        });
                slashBladeEntityPack.getEntity().world.addEntity(summondSwordEntity);
            }
        }

        @Override
        public void defaultConfig() {
            super.defaultConfig();
            lightningNumber = 12;
            attack = 0.1f;
            lightningAttack = 1;
        }
    }

    @VoluntarilyRegister
    public static class ColorWingSlashBladeRecipeRegister extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {
        @VoluntarilyAssignment
        protected SilverWingSlashBladeRegister.SilverWingLambdaSlashBladeRegister silverWingLambdaSlashBladeRegister;

        @VoluntarilyAssignment
        protected ColorWingSlashBladeRegister colorWingSlashRegisterBasics;

        @VoluntarilyAssignment
        protected SilverWingSlashBladeRegister.SilverWingLambdaSlashBladeRegister.CloudWheelStormSA cloudWheelStormSA;


        @Override
        protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {
            SlashBladePack silverWingLambdaSlashBlade = silverWingLambdaSlashBladeRegister.getSlashBladePack();
            silverWingLambdaSlashBlade.getSlashBladeState().setKillCount(4500);
            silverWingLambdaSlashBlade.getSlashBladeState().setRefine(650);

            return new SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack(
                    ListUtil.of(
                            " AB",
                            "AVA",
                            "BA "
                    ),
                    MapUtil.of(
                            "A", new IRecipeInItemPack.OfItemSA(cloudWheelStormSA),
                            "B", new IRecipeInItemPack.OfEntity(EntityType.ELDER_GUARDIAN),
                            "V", new IRecipeInItemPack.OfSlashBlade(silverWingLambdaSlashBlade)
                    ),
                    "V",
                    new IResultPack.OfSlashBladeRegister(colorWingSlashRegisterBasics)
            );
        }
    }
}
