package com.til.recasting.common.register.slash_blade.instance;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.*;
import com.til.recasting.common.data.IRecipeInItemPack;
import com.til.recasting.common.data.IResultPack;
import com.til.recasting.common.data.SlashBladePack;
import com.til.recasting.common.data.UseSlashBladeEntityPack;
import com.til.recasting.common.entity.JudgementCutEntity;
import com.til.recasting.common.entity.SummondSwordEntity;
import com.til.recasting.common.register.back_type.JudgementCutBackTypeRegister;
import com.til.recasting.common.register.back_type.SummondSwordBackTypeRegister;
import com.til.recasting.common.register.entity_predicate.DefaultEntityPredicateRegister;
import com.til.recasting.common.register.entity_type.JudgementCutEntityTypeRegister;
import com.til.recasting.common.register.entity_type.SummondSwordEntityTypeRegister;
import com.til.recasting.common.register.recipe.SlashBladeRecipeSerializerRegister;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import com.til.recasting.common.register.slash_blade.sa.SA_Register;
import com.til.recasting.common.register.slash_blade.sa.instance.EndingYanSakuraSA;
import com.til.recasting.common.register.slash_blade.se.instance.*;
import com.til.recasting.common.register.util.JudgementCutManage;
import com.til.recasting.common.register.util.RayTraceUtil;
import com.til.recasting.common.register.util.StringFinal;
import mods.flammpfeil.slashblade.SlashBlade;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;

import java.awt.*;
import java.util.List;

public abstract class StarBladeSlashBladeRegister extends SlashBladeRegister {
    @Override
    protected void init() {
        super.init();
        model = new ResourceLocation(getName().getNamespace(), String.join("/", SlashBlade.modid, ResourceLocationUtil.ofPath(StarBladeSlashBladeRegister.class), StringFinal.STATE + getState(), StringFinal.MODEL));
        texture = new ResourceLocation(getName().getNamespace(), String.join("/", SlashBlade.modid, ResourceLocationUtil.ofPath(StarBladeSlashBladeRegister.class), StringFinal.STATE + getState(), StringFinal.TEXTURE));

        summondSwordModel = new ResourceLocation(getName().getNamespace(), String.join("/", StringFinal.SUMMOND_SWORD, ResourceLocationUtil.ofPath(StarBladeSlashBladeRegister.class), StringFinal.MODEL));
        judgementCutModel = new ResourceLocation(getName().getNamespace(), String.join("/", StringFinal.JUDGEMENT_CUT, ResourceLocationUtil.ofPath(StarBladeSlashBladeRegister.class), StringFinal.MODEL));
    }

    @Override
    protected void defaultItemStackConfig(ItemStack itemStack) {
        super.defaultItemStackConfig(itemStack);
        slashBladePack.getSlashBladeState().setEffectColor(new Color(0xE7E5E6));
    }

    protected abstract int getState();

    public static abstract class StarBlade_SA extends SA_Register {

        @VoluntarilyAssignment
        protected SummondSwordEntityTypeRegister summondSwordEntityTypeRegister;

        @VoluntarilyAssignment
        protected DefaultEntityPredicateRegister defaultEntityPredicateRegister;

        @VoluntarilyAssignment
        protected SummondSwordBackTypeRegister.SummondSwordAttackBackTypeRegister attackBackTypeRegister;

        @ConfigField
        protected int attackNumber;

        @ConfigField
        protected float attack;

        @ConfigField
        protected float judgementCutAttack;

        @ConfigField
        protected float range;

        @Override
        public void trigger(UseSlashBladeEntityPack slashBladeEntityPack) {
            Vector3d attackPos = slashBladeEntityPack.getAttackPos();
            List<Entity> entityList = slashBladeEntityPack.getEntity().world.getEntitiesInAABBexcluding(slashBladeEntityPack.getEntity(), new Pos(attackPos).axisAlignedBB(range), entity -> defaultEntityPredicateRegister.canTarget(slashBladeEntityPack.getEntity(), entity));
            for (int i = 0; i < attackNumber; i++) {
                SummondSwordEntity summondSwordEntity = new SummondSwordEntity(summondSwordEntityTypeRegister.getEntityType(), slashBladeEntityPack.getEntity().world, slashBladeEntityPack.getEntity());
                slashBladeEntityPack.getSlashBladePack().getSlashBladeStateSupplement().decorate(summondSwordEntity);
                summondSwordEntity.setColor(slashBladeEntityPack.getSlashBladePack().getSlashBladeState().getColorCode());
                summondSwordEntity.setDamage(attack);
                summondSwordEntity.setMaxDelay(20);
                summondSwordEntity.setStartDelay(slashBladeEntityPack.getEntity().getRNG().nextInt(10));
                if (!entityList.isEmpty()) {
                    Entity entity = entityList.get(slashBladeEntityPack.getEntity().getRNG().nextInt(entityList.size()));
                    summondSwordEntity.lookAt(RayTraceUtil.getPosition(entity), false);
                } else {
                    summondSwordEntity.lookAt(attackPos, false);
                }
                summondSwordEntity.getBackRunPack().addRunBack(attackBackTypeRegister, (summondSwordEntity1, hitEntity) -> JudgementCutManage.doJudgementCut(slashBladeEntityPack.getEntity(), judgementCutAttack, 10, null, hitEntity, null));
                slashBladeEntityPack.getEntity().world.addEntity(summondSwordEntity);
            }
            slashBladeEntityPack.getEntity().playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 0.2F, 1.45F);
        }

        @Override
        public void defaultConfig() {
            super.defaultConfig();
            attackNumber = 6;
            attack = 0.25f;
            judgementCutAttack = 0.5f;
            range = 12;
        }
    }

    @VoluntarilyRegister
    public static class StarBlade_1_SlashBladeRegister extends StarBladeSlashBladeRegister {

        @VoluntarilyAssignment
        protected StarBlade_1_SA starBlade_1_sa;


        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.getSlashBladeState().setBaseAttackModifier(4f);
            slashBladePack.getSlashBladeStateSupplement().setAttackDistance(0.75f);
            slashBladePack.getSlashBladeStateSupplement().setDurable(6);
            slashBladePack.setSA(starBlade_1_sa);
        }

        @Override
        protected int getState() {
            return 1;
        }

        @VoluntarilyRegister
        public static class StarBlade_1_SlashBladeRecipeRegister extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {
            @VoluntarilyAssignment
            protected StarBlade_1_SlashBladeRegister starBlade_1_slashBladeRegister;

            @VoluntarilyAssignment
            protected OverloadSE overloadSE;

            @VoluntarilyAssignment
            protected BlackSlashBladeRegister blackSlashBladeRegister;

            @Override
            protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {

                SlashBladePack blackSlashBlade = blackSlashBladeRegister.getSlashBladePack();
                blackSlashBlade.getSlashBladeState().setKillCount(250);
                blackSlashBlade.getSlashBladeState().setRefine(35);
                blackSlashBlade.getIse().getPack(overloadSE).setLevel(1);
                EnchantmentHelper.setEnchantments(MapUtil.of(Enchantments.PROTECTION, 1, Enchantments.INFINITY, 1), blackSlashBlade.getItemStack());

                return new SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack(ListUtil.of(" AB", "AVA", "BA "), MapUtil.of("A", new IRecipeInItemPack.OfItemSE(overloadSE, 0.5f), "B", new IRecipeInItemPack.OfEntity(EntityType.PHANTOM), "V", new IRecipeInItemPack.OfSlashBlade(blackSlashBlade.getItemStack())), "V", new IResultPack.OfSlashBladeRegister(starBlade_1_slashBladeRegister));
            }
        }

        @VoluntarilyRegister
        public static class StarBlade_1_SA extends StarBlade_SA {
        }

    }

    @VoluntarilyRegister
    public static class StarBlade_2_SlashBladeRegister extends StarBladeSlashBladeRegister {

        @VoluntarilyAssignment
        protected StarBlade_2_SA starBlade_2_sa;


        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.getSlashBladeState().setColorCode(0xE7E5E6);
            slashBladePack.getSlashBladeState().setBaseAttackModifier(5f);
            slashBladePack.setSA(starBlade_2_sa);
            slashBladePack.getSlashBladeStateSupplement().setDurable(12);
        }

        @Override
        protected int getState() {
            return 2;
        }

        @VoluntarilyRegister
        public static class StarBlade_2_SlashBladeRecipeRegister extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {

            @VoluntarilyAssignment
            protected StarBlade_2_SlashBladeRegister starBlade_2_slashBladeRegister;

            @VoluntarilyAssignment
            protected StarBlade_1_SlashBladeRegister starBlade_1_slashBladeRegister;

            @VoluntarilyAssignment
            protected OverloadSE overloadSE;

            @VoluntarilyAssignment
            protected StormSE stormSE;

            @VoluntarilyAssignment
            protected StormVariantSE stormVariantSE;


            @Override
            protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {
                SlashBladePack starBlade_1 = starBlade_1_slashBladeRegister.getSlashBladePack();
                starBlade_1.getSlashBladeState().setKillCount(1000);
                starBlade_1.getSlashBladeState().setRefine(75);
                starBlade_1.getIse().getPack(overloadSE).setLevel(1);
                starBlade_1.getIse().getPack(stormSE).setLevel(1);
                starBlade_1.getIse().getPack(stormVariantSE).setLevel(1);
                EnchantmentHelper.setEnchantments(MapUtil.of(Enchantments.PROTECTION, 2, Enchantments.EFFICIENCY, 2, Enchantments.INFINITY, 1), starBlade_1.getItemStack());

                return new SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack(ListUtil.of("  A", " V ", "B  "), MapUtil.of("A", new IRecipeInItemPack.OfItemSE(stormSE, 1), "B", new IRecipeInItemPack.OfItemSE(stormVariantSE, 1), "V", new IRecipeInItemPack.OfSlashBlade(starBlade_1.getItemStack())), "V", new IResultPack.OfSlashBladeRegister(starBlade_2_slashBladeRegister));
            }
        }

        @VoluntarilyRegister
        public static class StarBlade_2_SA extends StarBlade_SA {
            @Override
            public void defaultConfig() {
                super.defaultConfig();
                attackNumber = 8;
                range = 16;
            }
        }

    }

    @VoluntarilyRegister
    public static class StarBlade_3_SlashBladeRegister extends StarBladeSlashBladeRegister {

        @VoluntarilyAssignment
        protected StarBlade_3_SA starBlade_3_sa;

        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.getSlashBladeState().setBaseAttackModifier(6f);
            slashBladePack.getSlashBladeStateSupplement().setAttackDistance(1.5f);
            slashBladePack.setSA(starBlade_3_sa);
            slashBladePack.getSlashBladeStateSupplement().setDurable(24);

        }

        @Override
        protected int getState() {
            return 3;
        }

        @VoluntarilyRegister
        public static class StarBlade_3_SlashBladeRecipeRegister extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {

            @VoluntarilyAssignment
            protected StarBlade_3_SlashBladeRegister starBlade_3_slashBladeRegister;

            @VoluntarilyAssignment
            protected StarBlade_2_SlashBladeRegister starBlade_2_slashBladeRegister;

            @VoluntarilyAssignment
            protected OverloadSE overloadSE;

            @VoluntarilyAssignment
            protected StormSE stormSE;

            @VoluntarilyAssignment
            protected StormVariantSE stormVariantSE;


            @Override
            protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {
                SlashBladePack starBlade_2 = starBlade_2_slashBladeRegister.getSlashBladePack();
                starBlade_2.getSlashBladeState().setKillCount(2000);
                starBlade_2.getSlashBladeState().setRefine(275);
                starBlade_2.getIse().getPack(overloadSE).setLevel(1);
                starBlade_2.getIse().getPack(stormSE).setLevel(1);
                starBlade_2.getIse().getPack(stormVariantSE).setLevel(1);
                EnchantmentHelper.setEnchantments(MapUtil.of(Enchantments.PROTECTION, 2, Enchantments.EFFICIENCY, 2, Enchantments.INFINITY, 1), starBlade_2.getItemStack());

                return new SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack(ListUtil.of("ABC", "DVD", "CBA"), MapUtil.of("A", new IRecipeInItemPack.OfItemSE(overloadSE, 1), "B", new IRecipeInItemPack.OfItemSE(stormSE, 1), "C", new IRecipeInItemPack.OfItemSE(stormVariantSE, 1), "D", new IRecipeInItemPack.OfEntity(EntityType.WITCH), "V", new IRecipeInItemPack.OfSlashBlade(starBlade_2.getItemStack())), "V", new IResultPack.OfSlashBladeRegister(starBlade_3_slashBladeRegister));
            }
        }

        @VoluntarilyRegister
        public static class StarBlade_3_SA extends StarBlade_SA {
            @Override
            public void defaultConfig() {
                super.defaultConfig();
                attackNumber = 12;
                range = 20;
            }
        }

    }

    @VoluntarilyRegister
    public static class StarBlade_4_SlashBladeRegister extends StarBladeSlashBladeRegister {
        @VoluntarilyAssignment
        protected StarBlade_4_SA starBlade_4_sa;


        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.getSlashBladeState().setBaseAttackModifier(7f);
            slashBladePack.getSlashBladeStateSupplement().setAttackDistance(2f);
            slashBladePack.setSA(starBlade_4_sa);
            slashBladePack.getSlashBladeStateSupplement().setDurable(48);
        }

        @Override
        protected int getState() {
            return 4;
        }

        @VoluntarilyRegister
        public static class StarBlade_4_SlashBladeRecipeRegister extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {

            @VoluntarilyAssignment
            protected StarBlade_3_SlashBladeRegister starBlade_3_slashBladeRegister;

            @VoluntarilyAssignment
            protected StarBlade_4_SlashBladeRegister starBlade_4_slashBladeRegister;

            @VoluntarilyAssignment
            protected BlackSlashBladeRegister blackSlashBladeRegister;

            @VoluntarilyAssignment
            protected OverloadSE overloadSE;

            @VoluntarilyAssignment
            protected StormSE stormSE;

            @VoluntarilyAssignment
            protected StormVariantSE stormVariantSE;

            @VoluntarilyAssignment
            protected SeverBreakSE severBreakSE;


            @Override
            protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {

                SlashBladePack blackSlash = blackSlashBladeRegister.getSlashBladePack();
                blackSlash.getSlashBladeState().setKillCount(1000);
                blackSlash.getSlashBladeState().setRefine(125);
                blackSlash.getIse().getPack(overloadSE).setLevel(2);
                blackSlash.getIse().getPack(stormSE).setLevel(2);
                blackSlash.getIse().getPack(stormVariantSE).setLevel(2);
                blackSlash.getIse().getPack(severBreakSE).setLevel(2);


                SlashBladePack starBlade_3 = starBlade_3_slashBladeRegister.getSlashBladePack();
                starBlade_3.getSlashBladeState().setKillCount(6000);
                starBlade_3.getSlashBladeState().setRefine(650);
                starBlade_3.getIse().getPack(overloadSE).setLevel(4);
                starBlade_3.getIse().getPack(stormSE).setLevel(4);
                starBlade_3.getIse().getPack(stormVariantSE).setLevel(4);
                starBlade_3.getIse().getPack(severBreakSE).setLevel(4);

                EnchantmentHelper.setEnchantments(MapUtil.of(Enchantments.PROTECTION, 3, Enchantments.EFFICIENCY, 3, Enchantments.INFINITY, 1), starBlade_3.getItemStack());

                return new SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack(
                        ListUtil.of(
                                "  A",
                                " V ",
                                "A  "),
                        MapUtil.of(
                                "A", new IRecipeInItemPack.OfSlashBlade(blackSlash.getItemStack()),
                                "V", new IRecipeInItemPack.OfSlashBlade(starBlade_3.getItemStack())),
                        "V",
                        new IResultPack.OfSlashBladeRegister(starBlade_4_slashBladeRegister));
            }
        }

        @VoluntarilyRegister
        public static class StarBlade_4_SA extends StarBlade_SA {
            @Override
            public void defaultConfig() {
                super.defaultConfig();
                attackNumber = 16;
                range = 24;
            }
        }

        @VoluntarilyRegister
        public static class StarBlade_4_LambdaSlashBladeRegister extends StarBlade_4_SlashBladeRegister {

            @VoluntarilyAssignment
            protected StarBlade_4_Lambda_SA starBlade_4_lambda_sa;

            protected ResourceLocation saLambdaModel;

            @Override
            protected void init() {
                super.init();
                saLambdaModel = new ResourceLocation(getName().getNamespace(), String.join("/", StringFinal.JUDGEMENT_CUT, ResourceLocationUtil.ofPath(StarBladeSlashBladeRegister.class), StringFinal.MODEL_LAMBDA));

            }

            @Override
            protected void defaultItemStackConfig(ItemStack itemStack) {
                super.defaultItemStackConfig(itemStack);
                slashBladePack.getSlashBladeState().setBaseAttackModifier(8f);
                slashBladePack.getSlashBladeStateSupplement().setAttackDistance(2f);
                slashBladePack.setSA(starBlade_4_lambda_sa);
                slashBladePack.getSlashBladeStateSupplement().setDurable(64);
            }

            public ResourceLocation getSaLambdaModel() {
                return saLambdaModel;
            }

            @VoluntarilyRegister
            public static class StarBlade_4_Lambda_SA extends StarBlade_SA {

                @VoluntarilyAssignment
                protected StarBlade_4_LambdaSlashBladeRegister starBlade_4_lambdaSlashBladeRegister;

                @VoluntarilyAssignment
                protected JudgementCutEntityTypeRegister judgementCutEntityTypeRegister;

                @VoluntarilyAssignment
                protected JudgementCutBackTypeRegister.JudgementCutTickBackTypeRegister judgementCutTickBackTypeRegister;

                @ConfigField
                protected int time;

                @ConfigField
                protected float attackProbability;


                @ConfigField
                protected float summondSwordAttack;

                @ConfigField
                protected int number;

                @Override
                public void trigger(UseSlashBladeEntityPack slashBladeEntityPack) {
                    super.trigger(slashBladeEntityPack);
                    for (int i = 0; i < number; i++) {
                        Vector3d pos = slashBladeEntityPack.getEntity().getPositionVec().add(RandomUtil.nextVector3dInCircles(slashBladeEntityPack.getEntity().getRNG(), 16));
                        JudgementCutEntity judgementCutEntity = new JudgementCutEntity(judgementCutEntityTypeRegister.getEntityType(), slashBladeEntityPack.getEntity().world, slashBladeEntityPack.getEntity());
                        judgementCutEntity.setModel(starBlade_4_lambdaSlashBladeRegister.getSaLambdaModel());
                        judgementCutEntity.setPosition(pos.getX(), pos.getY(), pos.getZ());
                        judgementCutEntity.setMaxLifeTime(time);
                        judgementCutEntity.setColor(slashBladeEntityPack.getSlashBladePack().getSlashBladeState().getColorCode());
                        judgementCutEntity.setDamage(0);
                        judgementCutEntity.getBackRunPack().addRunBack(judgementCutTickBackTypeRegister, judgementCutEntity1 -> {
                            if (slashBladeEntityPack.getEntity().getRNG().nextDouble() >= attackProbability) {
                                return;
                            }
                            SummondSwordEntity summondSwordEntity = new SummondSwordEntity(summondSwordEntityTypeRegister.getEntityType(), slashBladeEntityPack.getEntity().world, slashBladeEntityPack.getEntity());
                            slashBladeEntityPack.getSlashBladePack().getSlashBladeStateSupplement().decorate(summondSwordEntity);
                            summondSwordEntity.setPosition(pos.getX(), pos.getY(), pos.getZ());
                            summondSwordEntity.setColor(slashBladeEntityPack.getSlashBladePack().getSlashBladeState().getColorCode());
                            summondSwordEntity.setDamage(summondSwordAttack);
                            summondSwordEntity.setMaxDelay(20);
                            summondSwordEntity.setStartDelay(10);
                            summondSwordEntity.lookAt(slashBladeEntityPack.getAttackPos(), false);
                            summondSwordEntity.getBackRunPack().addRunBack(attackBackTypeRegister, (summondSwordEntity1, hitEntity) -> JudgementCutManage.doJudgementCut(slashBladeEntityPack.getEntity(), judgementCutAttack, 10, null, hitEntity, null));
                            slashBladeEntityPack.getEntity().world.addEntity(summondSwordEntity);
                            slashBladeEntityPack.getEntity().playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 0.2F, 1.45F);
                        });
                        slashBladeEntityPack.getEntity().world.addEntity(judgementCutEntity);

                    }
                }

                @Override
                public void defaultConfig() {
                    super.defaultConfig();
                    attackNumber = 18;
                    range = 24;
                    number = 5;
                    time = 160;
                    attackProbability = 1 / 20f;
                    summondSwordAttack = 0.02f;
                }
            }

            @VoluntarilyRegister
            public static class StarBlade_4_LambdaSlashBladeRecipeRegister extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {

                @VoluntarilyAssignment
                protected StarBlade_4_LambdaSlashBladeRegister starBlade_4_lambdaSlashBladeRegister;

                @VoluntarilyAssignment
                protected StarBlade_4_SlashBladeRegister starBlade_4_slashBladeRegister;

                @VoluntarilyAssignment
                protected DivinitySE divinitySE;

                @VoluntarilyAssignment
                protected OverloadSE overloadSE;

                @VoluntarilyAssignment
                protected StormSE stormSE;

                @VoluntarilyAssignment
                protected StormVariantSE stormVariantSE;

                @VoluntarilyAssignment
                protected SeverBreakSE severBreakSE;

                @VoluntarilyAssignment
                protected EndingYanSakuraSA endingYanSakuraSA;

                @Override
                protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {
                    SlashBladePack starBlade_4 = starBlade_4_slashBladeRegister.getSlashBladePack();
                    starBlade_4.getSlashBladeState().setKillCount(20000);
                    starBlade_4.getSlashBladeState().setRefine(50);
                    starBlade_4.getIse().getPack(overloadSE).setLevel(4);
                    starBlade_4.getIse().getPack(stormSE).setLevel(4);
                    starBlade_4.getIse().getPack(stormVariantSE).setLevel(4);
                    starBlade_4.getIse().getPack(severBreakSE).setLevel(4);

                    return new SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack(
                            ListUtil.of(
                                    " BA",
                                    " V ",
                                    "AB "
                            ),
                            MapUtil.of(
                                    "A", new IRecipeInItemPack.OfItemSE(divinitySE, 5),
                                    "B", new IRecipeInItemPack.OfItemSA(endingYanSakuraSA),
                                    "V", new IRecipeInItemPack.OfSlashBlade(starBlade_4.getItemStack())
                            ),
                            "V",
                            new IResultPack.OfSlashBladeRegister(starBlade_4_lambdaSlashBladeRegister)
                    );
                }
            }
        }
    }
}
