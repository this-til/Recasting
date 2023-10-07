package com.til.recasting.common.register.slash_blade.instance;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.ListUtil;
import com.til.glowing_fire_glow.common.util.MapUtil;
import com.til.glowing_fire_glow.common.util.Pos;
import com.til.glowing_fire_glow.common.util.ResourceLocationUtil;
import com.til.glowing_fire_glow.common.util.math.NumberPack;
import com.til.recasting.common.capability.ISE;
import com.til.recasting.common.data.IRecipeInItemPack;
import com.til.recasting.common.data.IResultPack;
import com.til.recasting.common.data.SlashBladePack;
import com.til.recasting.common.data.UseSlashBladeEntityPack;
import com.til.recasting.common.entity.DriveEntity;
import com.til.recasting.common.entity.LightningEntity;
import com.til.recasting.common.entity.SummondSwordEntity;
import com.til.recasting.common.event.EventDoAttack;
import com.til.recasting.common.register.back_type.SlashEffectEntityBackTypeRegister;
import com.til.recasting.common.register.entity_predicate.DefaultEntityPredicateRegister;
import com.til.recasting.common.register.entity_type.DriveEntityTypeRegister;
import com.til.recasting.common.register.entity_type.LightningEntityTypeRegister;
import com.til.recasting.common.register.entity_type.SummondSwordEntityTypeRegister;
import com.til.recasting.common.register.recipe.SlashBladeRecipeSerializerRegister;
import com.til.recasting.common.register.recipe.SpecialRecipeSerializerRegister;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import com.til.recasting.common.register.slash_blade.instance.original.NamelessSlashBladeRegister;
import com.til.recasting.common.register.slash_blade.sa.SA_Register;
import com.til.recasting.common.register.slash_blade.se.SE_Register;
import com.til.recasting.common.register.target_selector.DefaultTargetSelectorRegister;
import com.til.recasting.common.register.util.*;
import com.til.recasting.common.register.world.item.SE_DepositItemRegister;
import com.til.recasting.common.register.world.item.SoulItemRegister;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.init.SBItems;
import mods.flammpfeil.slashblade.util.KnockBacks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.awt.*;
import java.util.List;

@VoluntarilyRegister
public abstract class FluorescenceSlashBladeRegister extends SlashBladeRegister {

    @VoluntarilyAssignment
    protected FluorescenceSE fluorescenceSE;

    @Override
    protected void init() {
        super.init();
        model = new ResourceLocation(getName().getNamespace(), String.join("/", SlashBlade.modid, ResourceLocationUtil.ofPath(FluorescenceSlashBladeRegister.class), StringFinal.STATE + getState(), "model.obj"));
        texture = new ResourceLocation(getName().getNamespace(), String.join("/", SlashBlade.modid, ResourceLocationUtil.ofPath(FluorescenceSlashBladeRegister.class), StringFinal.STATE + getState(), "texture.png"));

    }

    @Override
    protected void defaultItemStackConfig(ItemStack itemStack) {
        super.defaultItemStackConfig(itemStack);
        slashBladePack.getSlashBladeState().setEffectColor(new Color(51, 51, 255));
        slashBladePack.getSlashBladeStateSupplement().setDurable(12);
        slashBladePack.getSlashBladeStateSupplement().setAttackDistance(1.25f);
        slashBladePack.getSlashBladeState().setBaseAttackModifier(2f);

        slashBladePack.getIse().getPack(fluorescenceSE).setLevel(1);
    }

    public abstract int getState();

    @VoluntarilyRegister
    public static class FluorescenceSE extends SE_Register {


        @ConfigField
        protected NumberPack attack;

        @SubscribeEvent
        protected void onEventDoAttack(EventDoAttack event) {
            if (!event.pack.getSlashBladePack().getIse().hasSE(this)) {
                return;
            }
            ISE.SE_Pack se_pack = event.pack.getSlashBladePack().getIse().getPack(this);
            if (!(event.target instanceof LivingEntity)) {
                return;
            }
            LivingEntity target = (LivingEntity) event.target;
            if (target.getActivePotionEffect(Effects.GLOWING) == null) {
                return;
            }
            event.modifiedRatio *= (1 + attack.of(se_pack.getLevel()));
        }


        @Override
        public void defaultConfig() {
            super.defaultConfig();
            attack = new NumberPack(0, 0.333f);
        }

        @Override
        public int getMaxLevel() {
            return 1;
        }

        @VoluntarilyRegister
        public static class FluorescenceSE_RecipeRegister extends SpecialRecipeSerializerRegister.SpecialRecipeRegister {

            @VoluntarilyAssignment
            protected SoulItemRegister.SoulCubeItemRegister soulCubeItemRegister;

            @VoluntarilyAssignment
            protected Fluorescence_1_SlashBladeRegister.Fluorescence_1_SA fluorescence_1_sa;


            @VoluntarilyAssignment
            protected SE_DepositItemRegister se_depositItemRegister;

            @VoluntarilyAssignment
            protected FluorescenceSE fluorescenceSE1;

            @Override
            protected SpecialRecipeSerializerRegister.SpecialRecipePack defaultSpecialRecipePackDelayed() {
                return new SpecialRecipeSerializerRegister.SpecialRecipePack(
                        ListUtil.of(
                                " A ",
                                " B ",
                                "   "),
                        MapUtil.of(
                                "A", new IRecipeInItemPack.OfItemSA(fluorescence_1_sa),
                                "B", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(soulCubeItemRegister.getItem()))
                        ),
                        new IResultPack.OfItemStack(se_depositItemRegister.mackItemStack(fluorescenceSE1))
                );
            }
        }
    }


    @VoluntarilyRegister
    public static class Fluorescence_1_SlashBladeRegister extends FluorescenceSlashBladeRegister {

        @VoluntarilyAssignment
        protected Fluorescence_1_SA fluorescence_1_sa;

        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.getSlashBladeState().setBaseAttackModifier(4f);
            slashBladePack.setSA(fluorescence_1_sa);
        }

        @Override
        public int getState() {
            return 1;
        }

        @VoluntarilyRegister
        public static class Fluorescence_1_Recipe_SlashBladeRegister extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {

            @VoluntarilyAssignment
            protected Fluorescence_1_SlashBladeRegister fluorescence_1_slashBladeRegister;

            @VoluntarilyAssignment
            protected NamelessSlashBladeRegister namelessSlashBladeRegister;

            @Override
            protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {
                SlashBladePack nameless = namelessSlashBladeRegister.getSlashBladePack();
                nameless.getSlashBladeState().setKillCount(150);
                nameless.getSlashBladeState().setRefine(30);
                EnchantmentHelper.setEnchantments(
                        MapUtil.of(
                                Enchantments.UNBREAKING, 3,
                                Enchantments.MENDING, 1),
                        nameless.getItemStack()
                );
                return new SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack(
                        ListUtil.of(
                                " AB",
                                "AVA",
                                "BA "
                        ),
                        MapUtil.of(
                                "A", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(Items.PAPER)),
                                "B", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(SBItems.proudsoul_crystal)),
                                "V", new IRecipeInItemPack.OfSlashBlade(nameless.getItemStack())
                        ),
                        "V",
                        new IResultPack.OfSlashBladeRegister(fluorescence_1_slashBladeRegister)
                );
            }
        }

        @VoluntarilyRegister
        public static class Fluorescence_1_SA extends SA_Register {

            @VoluntarilyAssignment
            protected DefaultEntityPredicateRegister defaultEntityPredicateRegister;

            @VoluntarilyAssignment
            protected DefaultTargetSelectorRegister defaultTargetSelectorRegister;

            @VoluntarilyAssignment
            protected SummondSwordEntityTypeRegister summondSwordEntityTypeRegister;

            @ConfigField
            protected int time;

            @ConfigField
            protected float attack;


            @Override
            public void trigger(UseSlashBladeEntityPack slashBladeEntityPack) {
                JudgementCutManage.doJudgementCut(slashBladeEntityPack.getEntity(), attack, 10, null, null,
                        judgementCutEntity -> judgementCutEntity.setEffectInstanceList(ListUtil.of(new EffectInstance(Effects.GLOWING, time))));
            }

            @Override
            public void defaultConfig() {
                super.defaultConfig();
                attack = 0.4f;
                time = 100;
            }

        }
    }

    @VoluntarilyRegister
    public static class Fluorescence_2_SlashBladeRegister extends FluorescenceSlashBladeRegister {
        @VoluntarilyAssignment
        protected Fluorescence_2_SA fluorescence_2_sa;

        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.getSlashBladeState().setBaseAttackModifier(4f);
            slashBladePack.setSA(fluorescence_2_sa);
        }

        @Override
        public int getState() {
            return 2;
        }

        @VoluntarilyRegister
        public static class Fluorescence_2_SA extends SA_Register {

            @VoluntarilyAssignment
            protected DefaultEntityPredicateRegister defaultEntityPredicateRegister;

            @VoluntarilyAssignment
            protected DefaultTargetSelectorRegister defaultTargetSelectorRegister;

            @VoluntarilyAssignment
            protected SummondSwordEntityTypeRegister summondSwordEntityTypeRegister;

            //@VoluntarilyRegister
            //protected JudgementCutBackTypeRegister.AttackBackTypeRegister attackBackTypeRegister;

            @ConfigField
            protected float range;

            @ConfigField
            protected int time;

            @ConfigField
            protected float attack;

            @ConfigField
            protected float number;

            @ConfigField
            protected float judgementAttack;

            @Override
            public void trigger(UseSlashBladeEntityPack slashBladeEntityPack) {
                JudgementCutManage.doJudgementCut(slashBladeEntityPack.getEntity(), attack, 10, null, null,
                        judgementCutEntity -> judgementCutEntity.setEffectInstanceList(ListUtil.of(new EffectInstance(Effects.GLOWING, time))));
                Vector3d attackPos = slashBladeEntityPack.getAttackPos();
                List<Entity> entityList = slashBladeEntityPack.getEntity().world.getEntitiesInAABBexcluding(
                        slashBladeEntityPack.getEntity(),
                        new Pos(attackPos).axisAlignedBB(range),
                        entity -> defaultEntityPredicateRegister.canTarget(slashBladeEntityPack.getEntity(), entity));
                for (int i = 0; i < number; i++) {
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
                    summondSwordEntity.setEffectInstanceList(ListUtil.of(new EffectInstance(Effects.GLOWING, time)));
                    slashBladeEntityPack.getEntity().world.addEntity(summondSwordEntity);
                }
                slashBladeEntityPack.getEntity().playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 0.2F, 1.45F);
            }

            @Override
            public void defaultConfig() {
                super.defaultConfig();
                range = 8;
                time = 100;
                judgementAttack = 0.4f;
                attack = 0.15f;
                number = 12;
            }
        }

        @VoluntarilyRegister
        public static class Fluorescence_2_Recipe_SA extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {

            @VoluntarilyAssignment
            protected Fluorescence_2_SlashBladeRegister fluorescence_2_slashBladeRegister;

            @VoluntarilyAssignment
            protected Fluorescence_1_SlashBladeRegister fluorescence_1_slashBladeRegister;

            @VoluntarilyAssignment
            protected BaGuaSlashBladeRegister baGuaSlashBladeRegister;

            @Override
            protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {
                SlashBladePack baGuaSlashBlade = baGuaSlashBladeRegister.getSlashBladePack();
                baGuaSlashBlade.getSlashBladeState().setKillCount(450);
                baGuaSlashBlade.getSlashBladeState().setRefine(45);

                SlashBladePack fluorescence_1 = fluorescence_1_slashBladeRegister.getSlashBladePack();
                fluorescence_1.getSlashBladeState().setKillCount(850);
                fluorescence_1.getSlashBladeState().setRefine(85);
                EnchantmentHelper.setEnchantments(
                        MapUtil.of(
                                Enchantments.UNBREAKING, 4,
                                Enchantments.MENDING, 1),
                        fluorescence_1.getItemStack()
                );


                return new SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack(
                        ListUtil.of(
                                " AC",
                                "AVA",
                                "BA "
                        ),
                        MapUtil.of(
                                "A", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(SBItems.proudsoul_tiny)),
                                "B", new IRecipeInItemPack.OfSlashBlade(baGuaSlashBlade.getItemStack()),
                                "C", new IRecipeInItemPack.OfEntity(EntityType.ZOMBIE),
                                "V", new IRecipeInItemPack.OfSlashBlade(fluorescence_1.getItemStack())
                        ),
                        "V",
                        new IResultPack.OfSlashBladeRegister(fluorescence_2_slashBladeRegister)
                );
            }
        }
    }

    /***
     * 砍刀
     */
    @VoluntarilyRegister
    public static class Fluorescence_3_SlashBladeRegister extends FluorescenceSlashBladeRegister {

        @VoluntarilyAssignment
        protected Fluorescence_3_SA fluorescence_3_sa;

        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.getSlashBladeState().setBaseAttackModifier(4f);
            slashBladePack.setSA(fluorescence_3_sa);
        }

        @Override
        public int getState() {
            return 3;
        }

        @VoluntarilyRegister
        public static class Fluorescence_3_SA extends SA_Register {

            @VoluntarilyAssignment
            protected SlashEffectEntityBackTypeRegister.SlashEffectAttackBackTypeRegister slashEffectAttackBackTypeRegister;

            @ConfigField
            protected float attack;

            @ConfigField
            protected float basicsRange;

            @ConfigField
            protected int life;

            @ConfigField
            protected int time;

            @Override
            public void trigger(UseSlashBladeEntityPack slashBladeEntityPack) {
                AttackManager.doSlash(
                        slashBladeEntityPack.getEntity(),
                        135,
                        slashBladeEntityPack.getSlashBladePack().getSlashBladeState().getColorCode(),
                        Vector3d.ZERO,
                        false,
                        true,
                        attack,
                        basicsRange,
                        slashEffectEntity -> {
                            slashEffectEntity.setMaxLifeTime(life);
                            slashEffectEntity.setMultipleAttack(true);
                            slashEffectEntity.setEffectInstanceList(ListUtil.of(new EffectInstance(Effects.GLOWING, time)));
                            BackRunPackUtil.addKnockBacks(slashEffectEntity, KnockBacks.cancel);
                        }
                );
            }

            @Override
            public void defaultConfig() {
                super.defaultConfig();
                attack = 0.1f;
                basicsRange = 1.5f;
                life = 20;
                time = 100;
            }
        }

        @VoluntarilyRegister
        public static class Fluorescence_3_SlashBladeRecipeRegister extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {
            @VoluntarilyAssignment
            protected Fluorescence_3_SlashBladeRegister fluorescence_3_slashBladeRegister;

            @VoluntarilyAssignment
            protected Fluorescence_2_SlashBladeRegister fluorescence_2_slashBladeRegister;

            @VoluntarilyAssignment
            protected SoulItemRegister.SoulCubeItemRegister soulItemRegister;

            @Override
            protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {
                SlashBladePack fluorescence_2_slashBlade = fluorescence_2_slashBladeRegister.getSlashBladePack();
                fluorescence_2_slashBlade.getSlashBladeState().setKillCount(1800);
                fluorescence_2_slashBlade.getSlashBladeState().setRefine(145);
                EnchantmentHelper.setEnchantments(
                        MapUtil.of(
                                Enchantments.UNBREAKING, 4,
                                Enchantments.MENDING, 1),
                        fluorescence_2_slashBlade.getItemStack()
                );

                return new SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack(
                        ListUtil.of(
                                "BA ",
                                " V ",
                                " AB"
                        ),
                        MapUtil.of(
                                "A", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(SBItems.proudsoul_crystal)),
                                "B", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(soulItemRegister.getItem())),
                                "V", new IRecipeInItemPack.OfSlashBlade(fluorescence_2_slashBlade)
                        ),
                        "V",
                        new IResultPack.OfSlashBladeRegister(fluorescence_3_slashBladeRegister)
                );
            }
        }
    }

    @VoluntarilyRegister
    public static class Fluorescence_4_SlashBladeRegister extends FluorescenceSlashBladeRegister {

        @VoluntarilyAssignment
        protected Fluorescence_4_SA fluorescence_4_sa;

        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.getSlashBladeState().setBaseAttackModifier(4f);
            slashBladePack.setSA(fluorescence_4_sa);
        }

        @Override
        public int getState() {
            return 4;
        }


        @VoluntarilyRegister
        public static class Fluorescence_4_SA extends SA_Register {

            @VoluntarilyAssignment
            protected DriveEntityTypeRegister driveEntityTypeRegister;

            @ConfigField
            protected float attack;

            @ConfigField
            protected float attackNumber;

            @ConfigField
            protected int life;

            @ConfigField
            protected float range;

            @ConfigField
            protected int time;

            @Override
            public void trigger(UseSlashBladeEntityPack slashBladeEntityPack) {
                for (int i = 0; i < attackNumber; i++) {
                    DriveEntity driveEntity = new DriveEntity(
                            driveEntityTypeRegister.getEntityType(),
                            slashBladeEntityPack.getEntity().world,
                            slashBladeEntityPack.getEntity()
                    );
                    driveEntity.setColor(slashBladeEntityPack.getSlashBladePack().getSlashBladeState().getColorCode());
                    driveEntity.setSize((slashBladeEntityPack.getEntity().getRNG().nextFloat() + range)
                                        * slashBladeEntityPack.getSlashBladePack().getSlashBladeStateSupplement().getAttackDistance());
                    driveEntity.setDamage(attack);
                    driveEntity.setMaxLifeTime(life);
                    driveEntity.setRoll(slashBladeEntityPack.getEntity().getRNG().nextInt(360));
                    driveEntity.setEffectInstanceList(ListUtil.of(new EffectInstance(Effects.GLOWING, time)));
                    slashBladeEntityPack.getEntity().world.addEntity(driveEntity);
                }
            }

            @Override
            public void defaultConfig() {
                super.defaultConfig();
                attack = 0.3f;
                attackNumber = 4;
                life = 40;
                range = 1;
                time = 100;
            }
        }

        @VoluntarilyRegister
        public static class Fluorescence_4_SlashBladeRecipeRegister extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {
            @VoluntarilyAssignment
            protected Fluorescence_4_SlashBladeRegister fluorescence_4_slashBladeRegister;

            @VoluntarilyAssignment
            protected Fluorescence_3_SlashBladeRegister fluorescence_3_slashBladeRegister;


            @VoluntarilyAssignment
            protected SoulItemRegister.SoulCubeChangeItemRegister soulCubeChangeItemRegister;

            @Override
            protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {
                SlashBladePack fluorescence_3_slashBlade = fluorescence_3_slashBladeRegister.getSlashBladePack();
                EnchantmentHelper.setEnchantments(
                        MapUtil.of(
                                Enchantments.UNBREAKING, 4,
                                Enchantments.MENDING, 1),
                        fluorescence_3_slashBlade.getItemStack()
                );
                return new SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack(
                        ListUtil.of(
                                "A  ",
                                " V ",
                                "  A"
                        ),
                        MapUtil.of(
                                "V", new IRecipeInItemPack.OfSlashBlade(fluorescence_3_slashBlade),
                                "A", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(soulCubeChangeItemRegister.getItem()))
                        ),
                        "V",
                        new IResultPack.OfSlashBladeRegister(fluorescence_4_slashBladeRegister)
                );
            }
        }
    }

    @VoluntarilyRegister
    public static class Fluorescence_5_SlashBladeRegister extends FluorescenceSlashBladeRegister {

        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.getSlashBladeState().setBaseAttackModifier(4f);
        }

        @Override
        public int getState() {
            return 5;
        }
    }

    @VoluntarilyRegister
    public static class Fluorescence_6_SlashBladeRegister extends FluorescenceSlashBladeRegister {

        @VoluntarilyAssignment
        public Fluorescence_6_SlashBladeSA fluorescence_6_slashBladeSA;

        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.getSlashBladeState().setBaseAttackModifier(4f);
            slashBladePack.setSA(fluorescence_6_slashBladeSA);
        }

        @Override
        public int getState() {
            return 6;
        }


        @VoluntarilyRegister
        public static class Fluorescence_6_SlashBladeSA extends SA_Register {

            @VoluntarilyAssignment
            protected LightningEntityTypeRegister lightningEntityTypeRegister;


            @ConfigField
            protected float attack;

            @ConfigField
            protected int time;

            @Override
            public void trigger(UseSlashBladeEntityPack slashBladeEntityPack) {
                Vector3d vector3d = slashBladeEntityPack.getAttackPos();
                LightningEntity lightningEntity = new LightningEntity(lightningEntityTypeRegister.getEntityType(), slashBladeEntityPack.getEntity().world, slashBladeEntityPack.getEntity());
                lightningEntity.setPosition(vector3d.getX(), vector3d.getY(), vector3d.getZ());
                lightningEntity.setColor(slashBladeEntityPack.getSlashBladePack().getSlashBladeState().getColorCode());
                lightningEntity.setDamage(attack);
                lightningEntity.setEffectInstanceList(ListUtil.of(new EffectInstance(Effects.GLOWING, time)));
                slashBladeEntityPack.getEntity().world.addEntity(lightningEntity);
            }

            @Override
            public void defaultConfig() {
                super.defaultConfig();
                attack = 1.1f;
                time = 100;
            }
        }

        @VoluntarilyRegister
        public static class Fluorescence_6_SlashBladeRecipeRegister extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {

            @VoluntarilyAssignment
            protected Fluorescence_6_SlashBladeRegister fluorescence_6_slashBladeRegister;

            @VoluntarilyAssignment
            protected Fluorescence_1_SlashBladeRegister fluorescence_1_slashBladeRegister;

            @VoluntarilyAssignment
            protected Fluorescence_1_SlashBladeRegister.Fluorescence_1_SA fluorescence_1_sa;

            @VoluntarilyAssignment
            protected Fluorescence_2_SlashBladeRegister.Fluorescence_2_SA fluorescence_2_sa;

            @VoluntarilyAssignment
            protected Fluorescence_3_SlashBladeRegister.Fluorescence_3_SA fluorescence_3_sa;

            @VoluntarilyAssignment
            protected Fluorescence_4_SlashBladeRegister.Fluorescence_4_SA fluorescence_4_sa;

            @Override
            protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {
                SlashBladePack fluorescence_1 = fluorescence_1_slashBladeRegister.getSlashBladePack();
                fluorescence_1.getSlashBladeState().setKillCount(2000);
                fluorescence_1.getSlashBladeState().setRefine(125);

                return new SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack(
                        ListUtil.of(
                                " A ",
                                "BVC",
                                " D "
                        ),
                        MapUtil.of(
                                "V", new IRecipeInItemPack.OfSlashBlade(fluorescence_1),
                                "A", new IRecipeInItemPack.OfItemSA(fluorescence_1_sa),
                                "B", new IRecipeInItemPack.OfItemSA(fluorescence_2_sa),
                                "C", new IRecipeInItemPack.OfItemSA(fluorescence_3_sa),
                                "D", new IRecipeInItemPack.OfItemSA(fluorescence_4_sa)),
                        "V",
                        new IResultPack.OfSlashBladeRegister(fluorescence_6_slashBladeRegister)
                );
            }
        }

    }


    @VoluntarilyRegister
    public static class Fluorescence_7_SlashBladeRegister extends FluorescenceSlashBladeRegister {

        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.getSlashBladeState().setBaseAttackModifier(4f);
        }

        @Override
        public int getState() {
            return 7;
        }
    }

    @VoluntarilyRegister
    public static class Fluorescence_8_SlashBladeRegister extends FluorescenceSlashBladeRegister {

        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.getSlashBladeState().setBaseAttackModifier(4f);
        }

        @Override
        public int getState() {
            return 8;
        }
    }


}
