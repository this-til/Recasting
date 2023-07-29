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
import com.til.recasting.common.data.SlashBladePack;
import com.til.recasting.common.data.UseSlashBladeEntityPack;
import com.til.recasting.common.data.IRecipeInItemPack;
import com.til.recasting.common.data.IResultPack;
import com.til.recasting.common.entity.SummondSwordEntity;
import com.til.recasting.common.event.EventDoAttack;
import com.til.recasting.common.register.entity_predicate.DefaultEntityPredicateRegister;
import com.til.recasting.common.register.entity_type.SummondSwordEntityTypeRegister;
import com.til.recasting.common.register.recipe.SlashBladeRecipeSerializerRegister;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import com.til.recasting.common.register.slash_blade.sa.SA_Register;
import com.til.recasting.common.register.slash_blade.se.SE_Register;
import com.til.recasting.common.register.target_selector.DefaultTargetSelectorRegister;
import com.til.recasting.common.register.util.JudgementCutManage;
import com.til.recasting.common.register.util.RayTraceUtil;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.init.SBItems;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.awt.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@VoluntarilyRegister
public abstract class FluorescenceSlashBladeRegister extends SlashBladeRegister {

    @VoluntarilyAssignment
    protected FluorescenceSE fluorescenceSE;

    @Override
    protected void init() {
        super.init();
        model = new ResourceLocation(getName().getNamespace(), String.join("/", SlashBlade.modid, ResourceLocationUtil.ofPath(FluorescenceSlashBladeRegister.class), "state" + getState(), "model.obj"));
        texture = new ResourceLocation(getName().getNamespace(), String.join("/", SlashBlade.modid, ResourceLocationUtil.ofPath(FluorescenceSlashBladeRegister.class), "state" + getState(), "texture.png"));

    }

    @Override
    protected void defaultItemStackConfig(ItemStack itemStack) {
        super.defaultItemStackConfig(itemStack);
        slashBladePack.slashBladeState.setColorCode(new Color(51, 51, 255).getRGB());
        slashBladePack.iSlashBladeStateSupplement.setDurable(12);
        slashBladePack.iSlashBladeStateSupplement.setAttackDistance(1.25f);
        slashBladePack.slashBladeState.setBaseAttackModifier(2f);

        slashBladePack.ise.getPack(fluorescenceSE).setLevel(1);
    }

    public abstract int getState();

    @VoluntarilyRegister
    public static class FluorescenceSE extends SE_Register {


        @ConfigField
        protected NumberPack attack;

        @SubscribeEvent
        protected void onEventDoAttack(EventDoAttack event) {
            if (!event.pack.slashBladePack.ise.hasSE(this)) {
                return;
            }
            ISE.SE_Pack se_pack = event.pack.slashBladePack.ise.getPack(this);
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
    }

    @VoluntarilyRegister
    public static class Fluorescence_1_SlashBladeRegister extends FluorescenceSlashBladeRegister {

        @VoluntarilyAssignment
        protected Fluorescence_1_SA fluorescence_1_sa;

        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.slashBladeState.setBaseAttackModifier(4f);
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
                nameless.slashBladeState.setKillCount(150);
                nameless.slashBladeState.setRefine(30);
                EnchantmentHelper.setEnchantments(
                        MapUtil.of(
                                Enchantments.UNBREAKING, 3,
                                Enchantments.MENDING, 1),
                        nameless.itemStack
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
                                "V", new IRecipeInItemPack.OfSlashBlade(nameless.itemStack)
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
                JudgementCutManage.doJudgementCut(slashBladeEntityPack.entity, attack, 10, null, null,
                        judgementCutEntity -> judgementCutEntity.setEffectInstanceList(ListUtil.of(new EffectInstance(Effects.GLOWING, time))));
            }

            @Override
            public void defaultConfig() {
                super.defaultConfig();
                attack = 0.75f;
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
            slashBladePack.slashBladeState.setBaseAttackModifier(4f);
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
                JudgementCutManage.doJudgementCut(slashBladeEntityPack.entity, attack, 10, null, null,
                        judgementCutEntity -> judgementCutEntity.setEffectInstanceList(ListUtil.of(new EffectInstance(Effects.GLOWING, time))));
                Vector3d attackPos = slashBladeEntityPack.getAttackPos();
                List<Entity> entityList = slashBladeEntityPack.entity.world.getEntitiesInAABBexcluding(
                        slashBladeEntityPack.entity,
                        new Pos(attackPos).axisAlignedBB(range),
                        entity -> defaultEntityPredicateRegister.canTarget(slashBladeEntityPack.entity, entity));
                for (int i = 0; i < number; i++) {
                    SummondSwordEntity summondSwordEntity = new SummondSwordEntity(summondSwordEntityTypeRegister.getEntityType(), slashBladeEntityPack.entity.world, slashBladeEntityPack.entity);
                    slashBladeEntityPack.slashBladePack.iSlashBladeStateSupplement.decorate(summondSwordEntity);
                    summondSwordEntity.setColor(slashBladeEntityPack.slashBladePack.slashBladeState.getColorCode());
                    summondSwordEntity.setDamage(attack);
                    summondSwordEntity.setMaxDelay(20);
                    summondSwordEntity.setStartDelay(slashBladeEntityPack.entity.getRNG().nextInt(10));
                    if (!entityList.isEmpty()) {
                        Entity entity = entityList.get(slashBladeEntityPack.entity.getRNG().nextInt(entityList.size()));
                        summondSwordEntity.lookAt(RayTraceUtil.getPosition(entity), false);
                    } else {
                        summondSwordEntity.lookAt(attackPos, false);
                    }
                    summondSwordEntity.setEffectInstanceList(ListUtil.of(new EffectInstance(Effects.GLOWING, time)));
                    slashBladeEntityPack.entity.world.addEntity(summondSwordEntity);
                }
                slashBladeEntityPack.entity.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 0.2F, 1.45F);
            }

            @Override
            public void defaultConfig() {
                super.defaultConfig();
                range = 8;
                time = 100;
                judgementAttack = 0.75f;
                attack = 0.15f;
                number = 12;
            }
        }
    }

    @VoluntarilyRegister
    public static class Fluorescence_3_SlashBladeRegister extends FluorescenceSlashBladeRegister {

        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.slashBladeState.setBaseAttackModifier(4f);
        }

        @Override
        public int getState() {
            return 3;
        }
    }

    @VoluntarilyRegister
    public static class Fluorescence_4_SlashBladeRegister extends FluorescenceSlashBladeRegister {

        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.slashBladeState.setBaseAttackModifier(4f);
        }

        @Override
        public int getState() {
            return 4;
        }
    }

    @VoluntarilyRegister
    public static class Fluorescence_5_SlashBladeRegister extends FluorescenceSlashBladeRegister {

        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.slashBladeState.setBaseAttackModifier(4f);
        }

        @Override
        public int getState() {
            return 5;
        }
    }

    @VoluntarilyRegister
    public static class Fluorescence_6_SlashBladeRegister extends FluorescenceSlashBladeRegister {

        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.slashBladeState.setBaseAttackModifier(4f);
        }

        @Override
        public int getState() {
            return 6;
        }
    }


    @VoluntarilyRegister
    public static class Fluorescence_7_SlashBladeRegister extends FluorescenceSlashBladeRegister {

        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.slashBladeState.setBaseAttackModifier(4f);
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
            slashBladePack.slashBladeState.setBaseAttackModifier(4f);
        }

        @Override
        public int getState() {
            return 8;
        }
    }


}
