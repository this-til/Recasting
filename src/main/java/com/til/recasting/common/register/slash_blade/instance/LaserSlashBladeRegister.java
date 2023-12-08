package com.til.recasting.common.register.slash_blade.instance;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.overall_config.OverallConfigRegister;
import com.til.glowing_fire_glow.common.register.particle_register.particle_registers.LightningParticleRegister;
import com.til.glowing_fire_glow.common.util.*;
import com.til.recasting.common.data.IRecipeInItemPack;
import com.til.recasting.common.data.IResultPack;
import com.til.recasting.common.data.SlashBladePack;
import com.til.recasting.common.data.UseSlashBladeEntityPack;
import com.til.recasting.common.entity.BallLightningEntity;
import com.til.recasting.common.event.EventDoAttack;
import com.til.recasting.common.register.attack_type.instance.LightningAttackType;
import com.til.recasting.common.register.capability.ElectrificationCapabilityRegister;
import com.til.recasting.common.register.effect.ElectrificationEffectRegister;
import com.til.recasting.common.register.entity_type.BallLightningEntityTypeRegister;
import com.til.recasting.common.register.recipe.SlashBladeRecipeSerializerRegister;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import com.til.recasting.common.register.slash_blade.sa.SA_Register;
import com.til.recasting.common.register.slash_blade.se.instance.OverpressureSE;
import com.til.recasting.common.register.util.StringFinal;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.init.SBItems;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;
import java.util.Random;


public abstract class LaserSlashBladeRegister extends SlashBladeRegister {
    @Override
    protected void init() {
        super.init();
        model = new ResourceLocation(getName().getNamespace(), String.join("/", SlashBlade.modid, ResourceLocationUtil.ofPath(LaserSlashBladeRegister.class), StringFinal.STATE + getState(), StringFinal.MODEL));
        texture = new ResourceLocation(getName().getNamespace(), String.join("/", SlashBlade.modid, ResourceLocationUtil.ofPath(LaserSlashBladeRegister.class), StringFinal.STATE + getState(), StringFinal.TEXTURE));
    }

    protected abstract int getState();

    public abstract static class LaserSlashBlade_SA extends SA_Register {

        @ConfigField
        protected float attack;

        @ConfigField
        protected int interval;

        @ConfigField
        protected float seep;

        @ConfigField
        protected int life;

        @ConfigField
        protected float range;

        @ConfigField
        protected int time;

        @VoluntarilyAssignment
        protected BallLightningEntityTypeRegister ballLightningEntityTypeRegister;

        @Override
        public void trigger(UseSlashBladeEntityPack slashBladeEntityPack) {
            BallLightningEntity ballLightningEntity = new BallLightningEntity(
                    ballLightningEntityTypeRegister.getEntityType(),
                    slashBladeEntityPack.getEntity().world,
                    slashBladeEntityPack.getEntity()
            );
            ballLightningEntity.setSeep(seep);
            ballLightningEntity.setDamage(attack);
            ballLightningEntity.setMaxLifeTime(life);
            ballLightningEntity.setSize(range);
            ballLightningEntity.setAttackInterval(interval);
            ballLightningEntity.setTime(time);
            slashBladeEntityPack.getEntity().world.addEntity(ballLightningEntity);
        }

        @Override
        public void defaultConfig() {
            super.defaultConfig();
            attack = 0.2f;
            interval = 10;
            seep = 0.4f;
            life = 200;
            range = 16;
            time = 40;
        }

        @VoluntarilyRegister
        public static class LaserSlashBlade_SA_OverallConfigRegister extends OverallConfigRegister {
            @ConfigField
            protected float addAttack;

            @ConfigField
            protected float additionalAttack;

            @ConfigField
            protected float probability;

            @VoluntarilyAssignment
            @Deprecated
            protected ElectrificationCapabilityRegister electrificationCapabilityRegister;

            @VoluntarilyAssignment
            protected ElectrificationEffectRegister electrificationEffectRegister;

            @VoluntarilyAssignment
            protected LightningAttackType lightningAttackType;

            @VoluntarilyAssignment
            protected LightningParticleRegister lightningParticleRegister;

            protected Random random = new Random();


            @SubscribeEvent
            protected void onEvent(EventDoAttack event) {

                if (!(event.target instanceof LivingEntity)) {
                    return;
                }
                EffectInstance effectInstance =((LivingEntity) event.target).getActivePotionEffect(electrificationEffectRegister.getEffect());
                if (effectInstance == null) {
                    return;
                }
                if (random.nextFloat() < probability) {
                    event.modifiedRatio *= (additionalAttack + 1);
                }
                if (event.attackTypeList.contains(lightningAttackType)) {
                    event.modifiedRatio *= (addAttack + 1);
                    lightningParticleRegister.add(
                            event.target.getEntityWorld(),
                            new GlowingFireGlowColor[]{new GlowingFireGlowColor(event.pack.getSlashBladePack().getSlashBladeState().getColorCode())},
                            0,
                            null,
                            new Pos(event.target).add(new Pos(RandomUtil.nextVector3dInCircles(random, 5))),
                            new Pos(event.target)
                    );
                }
            }

            @Override
            public void defaultConfig() {
                super.defaultConfig();
                addAttack = 1;
                additionalAttack = 0.3f;
                probability = 0.3333f;
            }
        }

    }

    @VoluntarilyRegister
    public static class Laser_1_SlashBladeRegister extends LaserSlashBladeRegister {

        @VoluntarilyAssignment
        protected LaserSlashBlade_1_SA laserSlashBlade_1_sa;

        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.getSlashBladeState().setBaseAttackModifier(5f);
            slashBladePack.getSlashBladeStateSupplement().setDurable(5);
            slashBladePack.setSA(laserSlashBlade_1_sa);
        }

        @Override
        protected int getState() {
            return 1;
        }

        @VoluntarilyRegister
        public static class LaserSlashBlade_1_SA extends LaserSlashBlade_SA {
        }

        public static class LaserSlashBlade_1_SlashBladeRecipeRegister extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {
            @VoluntarilyAssignment
            protected FluorescenceSlashBladeRegister.Fluorescence_6_SlashBladeRegister fluorescence_6_slashBladeRegister;

            @VoluntarilyAssignment
            protected Laser_1_SlashBladeRegister laser_1_slashBladeRegister;

            @Override
            protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {
                SlashBladePack fluorescence_6 = fluorescence_6_slashBladeRegister.getSlashBladePack();
                fluorescence_6.getSlashBladeState().setKillCount(1500);
                fluorescence_6.getSlashBladeState().setRefine(325);
                EnchantmentHelper.setEnchantments(MapUtil.of(Enchantments.CHANNELING, 1, Enchantments.PIERCING, 4), fluorescence_6.getItemStack());
                return new SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack(
                        ListUtil.of(
                                " CA",
                                "BVB",
                                "AC "
                        ),
                        MapUtil.of(
                                "A", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(SBItems.proudsoul_ingot)),
                                "B", new IRecipeInItemPack.OfItemEnchantment(Enchantments.CHANNELING, 8),
                                "C", new IRecipeInItemPack.OfItemEnchantment(Enchantments.PIERCING, 8),
                                "V", new IRecipeInItemPack.OfSlashBlade(fluorescence_6)
                        ),
                        "V",
                        new IResultPack.OfSlashBladeRegister(laser_1_slashBladeRegister)
                );
            }
        }
    }

    @VoluntarilyRegister
    public static class Laser_2_SlashBladeRegister extends LaserSlashBladeRegister {
        @VoluntarilyAssignment
        protected LaserSlashBlade_2_SA laserSlashBlade_2_sa;

        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.getSlashBladeState().setBaseAttackModifier(6f);
            slashBladePack.getSlashBladeStateSupplement().setDurable(10);
            slashBladePack.setSA(laserSlashBlade_2_sa);
        }

        @Override
        protected int getState() {
            return 2;
        }

        @VoluntarilyRegister
        public static class LaserSlashBlade_2_SA extends LaserSlashBlade_SA {
            @Override
            public void trigger(UseSlashBladeEntityPack slashBladeEntityPack) {
                super.trigger(slashBladeEntityPack);
                attack = 0.3f;
                interval = 5;
                range = 20;
                time = 80;
            }
        }

        @VoluntarilyRegister
        public static class LaserSlashBlade_2_SlashBladeRecipeRegister extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {
            @VoluntarilyAssignment
            protected Laser_1_SlashBladeRegister laser_1_slashBladeRegister;

            @VoluntarilyAssignment
            protected Laser_2_SlashBladeRegister laser_2_slashBladeRegister;

            @VoluntarilyAssignment
            protected OverpressureSE overpressureSE;

            @Override
            protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {
                SlashBladePack laser_1 = laser_1_slashBladeRegister.getSlashBladePack();
                laser_1.getSlashBladeState().setKillCount(3000);
                laser_1.getSlashBladeState().setRefine(625);
                laser_1.getIse().getPack(overpressureSE).setLevel(5);
                EnchantmentHelper.setEnchantments(MapUtil.of(Enchantments.CHANNELING, 1, Enchantments.PIERCING, 4), laser_1.getItemStack());
                return new SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack(
                        ListUtil.of(
                                "  A",
                                " V ",
                                "A  "
                        ),
                        MapUtil.of(
                                "A", new IRecipeInItemPack.OfItemSE(overpressureSE, 6),
                                "V", new IRecipeInItemPack.OfSlashBlade(laser_1)
                        ),
                        "V",
                        new IResultPack.OfSlashBladeRegister(laser_2_slashBladeRegister)
                );
            }
        }
    }

    @VoluntarilyRegister
    public static class Laser_3_SlashBladeRegister extends LaserSlashBladeRegister {

        @VoluntarilyAssignment
        protected LaserSlashBlade_3_SA laserSlashBlade_3_sa;

        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.getSlashBladeState().setBaseAttackModifier(7f);
            slashBladePack.getSlashBladeStateSupplement().setDurable(20);
            slashBladePack.getSlashBladeStateSupplement().setAttackDistance(1.2f);
            slashBladePack.setSA(laserSlashBlade_3_sa);
        }

        @Override
        protected int getState() {
            return 3;
        }

        @VoluntarilyRegister
        public static class LaserSlashBlade_3_SA extends LaserSlashBlade_SA {
            @Override
            public void trigger(UseSlashBladeEntityPack slashBladeEntityPack) {
                super.trigger(slashBladeEntityPack);
                attack = 0.4f;
                interval = 2;
                range = 24;
                time = 120;
            }
        }

        @VoluntarilyRegister
        public static class LaserSlashBlade_2_SlashBladeRecipeRegister extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {
            @VoluntarilyAssignment
            protected Laser_1_SlashBladeRegister laser_1_slashBladeRegister;

            @VoluntarilyAssignment
            protected Laser_2_SlashBladeRegister laser_2_slashBladeRegister;

            @VoluntarilyAssignment
            protected OverpressureSE overpressureSE;

            @Override
            protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {
                SlashBladePack laser_1 = laser_1_slashBladeRegister.getSlashBladePack();
                laser_1.getSlashBladeState().setKillCount(6000);
                laser_1.getSlashBladeState().setRefine(1225);
                laser_1.getIse().getPack(overpressureSE).setLevel(5);
                EnchantmentHelper.setEnchantments(MapUtil.of(Enchantments.CHANNELING, 1, Enchantments.PIERCING, 4), laser_1.getItemStack());
                return new SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack(
                        ListUtil.of(
                                "  A",
                                " V ",
                                "A  "
                        ),
                        MapUtil.of(
                                "A", new IRecipeInItemPack.OfItemSE(overpressureSE, 16),
                                "V", new IRecipeInItemPack.OfSlashBlade(laser_1)
                        ),
                        "V",
                        new IResultPack.OfSlashBladeRegister(laser_2_slashBladeRegister)
                );
            }
        }

    }


}
