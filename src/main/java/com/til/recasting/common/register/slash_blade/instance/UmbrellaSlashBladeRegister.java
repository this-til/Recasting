package com.til.recasting.common.register.slash_blade.instance;

import com.til.glowing_fire_glow.common.capability.time_run.TimerCell;
import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.ListUtil;
import com.til.glowing_fire_glow.common.util.MapUtil;
import com.til.glowing_fire_glow.common.util.Pos;
import com.til.glowing_fire_glow.common.util.ResourceLocationUtil;
import com.til.recasting.common.capability.ISE;
import com.til.recasting.common.data.IRecipeInItemPack;
import com.til.recasting.common.data.IResultPack;
import com.til.recasting.common.data.SlashBladePack;
import com.til.recasting.common.data.UseSlashBladeEntityPack;
import com.til.recasting.common.entity.JudgementCutEntity;
import com.til.recasting.common.event.EventDoAttack;
import com.til.recasting.common.register.entity_predicate.DefaultEntityPredicateRegister;
import com.til.recasting.common.register.recipe.SlashBladeRecipeSerializerRegister;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import com.til.recasting.common.register.slash_blade.instance.original.TukumoSlashBladeRegister;
import com.til.recasting.common.register.slash_blade.sa.SA_Register;
import com.til.recasting.common.register.slash_blade.se.SE_Register;
import com.til.recasting.common.register.slash_blade.se.instance.DivinitySE;
import com.til.recasting.common.register.target_selector.DefaultTargetSelectorRegister;
import com.til.recasting.common.register.util.AttackManager;
import com.til.recasting.common.register.util.JudgementCutManage;
import com.til.recasting.common.register.util.RayTraceUtil;
import com.til.recasting.common.register.world.item.SoulItemRegister;
import mods.flammpfeil.slashblade.SlashBlade;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.awt.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


public abstract class UmbrellaSlashBladeRegister extends SlashBladeRegister {


    @VoluntarilyAssignment
    protected UmbrellaSlashBladeSE umbrellaSlashBladeSE;

    @Override
    protected void init() {
        super.init();
        model = new ResourceLocation(getName().getNamespace(), String.join("/", SlashBlade.modid, ResourceLocationUtil.ofPath(UmbrellaSlashBladeRegister.class), "state" + getState(), "model.obj"));
        texture = new ResourceLocation(getName().getNamespace(), String.join("/", SlashBlade.modid, ResourceLocationUtil.ofPath(UmbrellaSlashBladeRegister.class), "texture.png"));
    }

    public abstract int getState();

    @Override
    protected void defaultItemStackConfig(ItemStack itemStack) {
        super.defaultItemStackConfig(itemStack);
        slashBladePack.getSlashBladeState().setEffectColor(new Color(0xC191FF));
        slashBladePack.getSlashBladeStateSupplement().setDurable(12);
        slashBladePack.getSlashBladeState().setBaseAttackModifier(5);
        slashBladePack.getIse().getPack(umbrellaSlashBladeSE).setLevel(1);
    }


    @VoluntarilyRegister
    public static class UmbrellaSlash_1_BladeRegister extends UmbrellaSlashBladeRegister {

        @VoluntarilyAssignment
        protected MultipleDimensionalChoppingSA multipleDimensionalChoppingSA;

        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.setSA(multipleDimensionalChoppingSA);
        }

        @Override
        public int getState() {
            return 1;
        }

        /***
         * //次元斩·决
         * 多从次元斩
         */
        @VoluntarilyRegister
        public static class MultipleDimensionalChoppingSA extends SA_Register {

            @ConfigField
            protected int attackNumber;

            @ConfigField
            protected float hit;

            @ConfigField
            protected int delay;

            @Override
            public void trigger(UseSlashBladeEntityPack slashBladeEntityPack) {
                AtomicReference<Vector3d> pos = new AtomicReference<>();
                for (int i = 0; i < attackNumber; i++) {
                    int _delay = delay * i;
                    slashBladeEntityPack.getTimeRun().addTimerCell(new TimerCell(() -> {
                        AtomicReference<JudgementCutEntity> judgementCutEntity = new AtomicReference<>();
                        JudgementCutManage.doJudgementCut(slashBladeEntityPack.getEntity(), hit, 10, pos.get(), null, judgementCutEntity::set);
                        if (judgementCutEntity.get() != null) {
                            pos.set(judgementCutEntity.get().getPositionVec());
                        }
                    }, _delay, 0));
                }
            }

            @Override
            public void defaultConfig() {
                super.defaultConfig();
                attackNumber = 4;
                delay = 4;
                hit = 0.3f;
            }
        }

        @VoluntarilyRegister
        public static class UmbrellaSlash_1_SlashBladeRecipeRegister extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {
            @VoluntarilyAssignment
            protected UmbrellaSlash_1_BladeRegister umbrellaSlash_1_bladeRegister;


            @VoluntarilyAssignment
            protected TukumoSlashBladeRegister tukumoSlashBladeRegister;

            @VoluntarilyAssignment
            protected SoulItemRegister.SoulCubeChangeItemRegister soulCubeChangeItemRegister;

            @VoluntarilyAssignment
            protected SoulItemRegister.SoulCubeItemRegister soulCubeItemRegister;

            @Override
            protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {
                SlashBladePack tukumoSlashBlade = tukumoSlashBladeRegister.getSlashBladePack();
                tukumoSlashBlade.getSlashBladeState().setKillCount(1500);
                tukumoSlashBlade.getSlashBladeState().setRefine(150);

                return new SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack(
                        ListUtil.of(
                                " A ",
                                "BVB",
                                " A "
                        ),
                        MapUtil.of(
                                "V", new IRecipeInItemPack.OfSlashBlade(tukumoSlashBlade),
                                "A", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(soulCubeChangeItemRegister.getItem())),
                                "B", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(soulCubeItemRegister.getItem()))
                        ),
                        "V",
                        new IResultPack.OfSlashBladeRegister(umbrellaSlash_1_bladeRegister)
                );
            }
        }
    }

    @VoluntarilyRegister
    public static class UmbrellaSlash_2_BladeRegister extends UmbrellaSlashBladeRegister {

        @VoluntarilyAssignment
        protected InfiniteDimensionalChoppingSA infiniteDimensionalChoppingSA;

        @Override
        public int getState() {
            return 2;
        }

        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.getSlashBladeState().setBaseAttackModifier(6f);
            slashBladePack.setSA(infiniteDimensionalChoppingSA);
        }

        /***
         * 无限次元斩
         */
        @VoluntarilyRegister
        public static class InfiniteDimensionalChoppingSA extends SA_Register {

            @ConfigField
            protected float attackRange;

            @ConfigField
            protected int attackNumber;

            @ConfigField
            protected float hit;

            @ConfigField
            protected int delay;

            @VoluntarilyAssignment
            protected DefaultTargetSelectorRegister defaultTargetSelectorRegister;


            @VoluntarilyAssignment
            protected DefaultEntityPredicateRegister defaultEntityPredicateRegister;


            @Override
            public void trigger(UseSlashBladeEntityPack slashBladeEntityPack) {

                Entity targetEntity = slashBladeEntityPack.getSlashBladePack().getSlashBladeState().getTargetEntity(slashBladeEntityPack.getEntity().world);
                RayTraceResult rayTraceResult = targetEntity == null ? defaultTargetSelectorRegister.selector(slashBladeEntityPack.getEntity()) : new EntityRayTraceResult(targetEntity);
                Vector3d attackPos = targetEntity == null ? rayTraceResult.getHitVec() : RayTraceUtil.getPosition(targetEntity);

                List<Entity> attackEntity = slashBladeEntityPack.getEntity().world
                        .getEntitiesInAABBexcluding(
                                slashBladeEntityPack.getEntity(),
                                new Pos(attackPos).axisAlignedBB(attackRange),
                                entity -> defaultEntityPredicateRegister.canTarget(slashBladeEntityPack.getEntity(), entity));


                for (int i = 0; i < attackNumber; i++) {
                    int _delay = delay * i;
                    slashBladeEntityPack.getTimeRun().addTimerCell(new TimerCell(() -> {
                        while (true) {
                            if (attackEntity.isEmpty()) {
                                JudgementCutManage.doJudgementCut(slashBladeEntityPack.getEntity(), hit, 10, attackPos, null, null);
                                return;
                            }
                            Entity entity = attackEntity.get(slashBladeEntityPack.getEntity().getRNG().nextInt(attackEntity.size()));
                            if (!entity.isAlive()) {
                                attackEntity.remove(entity);
                                continue;
                            }
                            JudgementCutManage.doJudgementCut(slashBladeEntityPack.getEntity(), hit, 10, RayTraceUtil.getPosition(entity), null, null);
                            return;
                        }
                    }, _delay, 0));
                }
            }

            @Override
            public void defaultConfig() {
                super.defaultConfig();
                attackRange = 12f;
                attackNumber = 12;
                hit = 0.5f;
                delay = 2;
            }
        }

        @VoluntarilyRegister
        public static class UmbrellaSlash_2_SlashBladeRecipeRegister extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {
            @VoluntarilyAssignment
            protected UmbrellaSlash_1_BladeRegister umbrellaSlash_1_bladeRegister;

            @VoluntarilyAssignment
            protected UmbrellaSlash_2_BladeRegister umbrellaSlash_2_bladeRegister;

            @VoluntarilyAssignment
            protected DivinitySE divinitySE;

            @Override
            protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {
                SlashBladePack umbrellaSlashBlade = umbrellaSlash_1_bladeRegister.getSlashBladePack();
                umbrellaSlashBlade.getSlashBladeState().setKillCount(300);
                umbrellaSlashBlade.getSlashBladeState().setRefine(300);

                return new SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack(
                        ListUtil.of(
                                "  A",
                                " V ",
                                "A  "
                        ),
                        MapUtil.of(
                                "V", new IRecipeInItemPack.OfSlashBlade(umbrellaSlashBlade),
                                "A", new IRecipeInItemPack.OfItemSE(divinitySE, 5f)

                        ),
                        "V",
                        new IResultPack.OfSlashBladeRegister(umbrellaSlash_2_bladeRegister)
                );
            }
        }
    }

    @VoluntarilyRegister
    public static class UmbrellaSlashBladeSE extends SE_Register {

        @ConfigField
        protected float attack;

        @ConfigField
        protected int attackNumber;

        @ConfigField
        protected int attackInterval;

        @ConfigField
        protected int cool;

        @SubscribeEvent
        protected void onEventDoAttack(EventDoAttack event) {
            if (!event.pack.getSlashBladePack().getIse().hasSE(this)) {
                return;
            }
            ISE.SE_Pack se_pack = event.pack.getSlashBladePack().getIse().getPack(this);
            if (!se_pack.tryTime(event.pack.getEntity().world.getGameTime(), cool)) {
                return;
            }
            for (int i = 0; i < attackNumber; i++) {
                event.pack.getTimeRun().addTimerCell(
                        new TimerCell(
                                () -> {
                                    if (!event.target.isAlive()) {
                                        return;
                                    }
                                    AttackManager.doAttack(
                                            event.pack.getEntity(),
                                            event.target,
                                            attack,
                                            true,
                                            true,
                                            false
                                    );
                                    event.target.setMotion(Vector3d.ZERO);
                                },
                                (1 + i) * attackInterval,
                                0
                        )
                );
            }
        }


        @Override
        public int getMaxLevel() {
            return 1;
        }

        @Override
        public void defaultConfig() {
            super.defaultConfig();
            cool = 5;
            attack = 0.2f;
            attackNumber = 4;
            attackInterval = 20;
        }
    }

}
