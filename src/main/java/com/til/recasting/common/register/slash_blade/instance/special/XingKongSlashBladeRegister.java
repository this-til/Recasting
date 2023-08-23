package com.til.recasting.common.register.slash_blade.instance.special;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.ListUtil;
import com.til.glowing_fire_glow.common.util.MapUtil;
import com.til.glowing_fire_glow.common.util.Pos;
import com.til.glowing_fire_glow.common.util.RandomUtil;
import com.til.recasting.common.data.IRecipeInItemPack;
import com.til.recasting.common.data.IResultPack;
import com.til.recasting.common.data.SlashBladePack;
import com.til.recasting.common.data.UseSlashBladeEntityPack;
import com.til.recasting.common.entity.SummondSwordEntity;
import com.til.recasting.common.register.entity_predicate.DefaultEntityPredicateRegister;
import com.til.recasting.common.register.entity_type.SummondSwordEntityTypeRegister;
import com.til.recasting.common.register.recipe.SlashBladeRecipeSerializerRegister;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import com.til.recasting.common.register.slash_blade.instance.DragonSlashBladeRegister;
import com.til.recasting.common.register.slash_blade.sa.SA_Register;
import com.til.recasting.common.register.slash_blade.se.instance.DivinitySE;
import com.til.recasting.common.register.util.RayTraceUtil;
import com.til.recasting.common.register.util.StringFinal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@VoluntarilyRegister
public class XingKongSlashBladeRegister extends SlashBladeRegister {

    @VoluntarilyAssignment
    protected XingKongSlashBladeSA xingKongSlashSA;

    protected ResourceLocation saModel;

    @Override
    protected void init() {
        super.init();
        summondSwordModel = new ResourceLocation(getName().getNamespace(), String.join("/", StringFinal.SUMMOND_SWORD, getName().getPath(), "model.obj"));
        saModel = new ResourceLocation(getName().getNamespace(), String.join("/", StringFinal.SPECIAL, getName().getPath(), "model.obj"));
    }

    @Override
    protected void defaultItemStackConfig(ItemStack itemStack) {
        super.defaultItemStackConfig(itemStack);
        slashBladePack.getSlashBladeState().setBaseAttackModifier(7f);
        slashBladePack.getSlashBladeState().setColorCode(new Color(0, 17, 86).getRGB());
        slashBladePack.getSlashBladeStateSupplement().setDurable(26);
        slashBladePack.getSlashBladeStateSupplement().setDurable(1.25f);
        slashBladePack.setSA(xingKongSlashSA);
    }

    @VoluntarilyRegister
    public static class XingKongSlashBladeSA extends SA_Register {

        protected static final Map<Entity, Entity> ONLY_ONE_MAP = new HashMap<>();

        @VoluntarilyAssignment
        protected SummondSwordEntityTypeRegister summondSwordEntityTypeRegister;

        @VoluntarilyAssignment
        protected DefaultEntityPredicateRegister defaultEntityPredicateRegister;

        @VoluntarilyAssignment
        protected XingKongSlashBladeRegister xingKongSlashBladeRegister;

        @ConfigField
        protected float attack;

        @ConfigField
        protected int attackNumber;

        @ConfigField
        protected float attackRange;

        @ConfigField
        protected float generateRange;

        @ConfigField
        protected int life;


        @Override
        public void trigger(UseSlashBladeEntityPack slashBladeEntityPack) {
            if (ONLY_ONE_MAP.containsKey(slashBladeEntityPack.getEntity())) {
                Entity entity = ONLY_ONE_MAP.get(slashBladeEntityPack.getEntity());
                if (entity.isAlive()) {
                    entity.remove();
                }
            }
            AtomicReference<List<Entity>> attackEntityList = new AtomicReference<>(slashBladeEntityPack.getEntity().world.getEntitiesInAABBexcluding(
                    slashBladeEntityPack.getEntity(),
                    new Pos(slashBladeEntityPack.getEntity()).axisAlignedBB(attackRange),
                    entity -> defaultEntityPredicateRegister.canTarget(slashBladeEntityPack.getEntity(), entity)));
            AtomicReference<Vector3d> pos = new AtomicReference<>();
            SummondSwordEntity summondSwordEntity = new SummondSwordEntity(
                    summondSwordEntityTypeRegister.getEntityType(),
                    slashBladeEntityPack.getEntity().world,
                    slashBladeEntityPack.getEntity()
            ) {
                @Override
                public void tick() {
                    super.tick();
                    if (world.isRemote) {
                        return;
                    }
                    Vector3d attackPos = null;
                    if (ticksExisted % 20 == 0) {
                        attackEntityList.set(slashBladeEntityPack.getEntity().world.getEntitiesInAABBexcluding(
                                slashBladeEntityPack.getEntity(),
                                new Pos(slashBladeEntityPack.getEntity()).axisAlignedBB(attackRange),
                                entity -> defaultEntityPredicateRegister.canTarget(slashBladeEntityPack.getEntity(), entity)));
                    }
                    while (!attackEntityList.get().isEmpty()) {
                        Entity attackEntity = attackEntityList.get().get(slashBladeEntityPack.getEntity().getRNG().nextInt(attackEntityList.get().size()));
                        if (!attackEntity.isAlive()) {
                            attackEntityList.get().remove(attackEntity);
                            continue;
                        }
                        attackPos = RayTraceUtil.getPosition(attackEntity);
                        break;
                    }
                    for (int i = 0; i < attackNumber; i++) {
                        SummondSwordEntity _summondSwordEntity = new SummondSwordEntity(
                                summondSwordEntityTypeRegister.getEntityType(),
                                slashBladeEntityPack.getEntity().world,
                                slashBladeEntityPack.getEntity()
                        );
                        slashBladeEntityPack.getSlashBladePack().getSlashBladeStateSupplement().decorate(_summondSwordEntity);
                        Vector3d _pos = pos.get().add(RandomUtil.nextVector3dInCircles(slashBladeEntityPack.getEntity().getRNG(), generateRange));
                        _summondSwordEntity.setPosition(_pos.getX(), _pos.getY(), _pos.getZ());
                        _summondSwordEntity.setColor(slashBladeEntityPack.getSlashBladePack().getSlashBladeState().getColorCode());
                        _summondSwordEntity.setDamage(attack);
                        _summondSwordEntity.setMaxDelay(10);
                        _summondSwordEntity.setRoll(slashBladeEntityPack.getEntity().getRNG().nextInt(360));
                        _summondSwordEntity.setStartDelay(10);
                        if (attackPos != null) {
                            _summondSwordEntity.lookAt(attackPos, false);
                        }
                        slashBladeEntityPack.getEntity().world.addEntity(_summondSwordEntity);
                        //slashBladeEntityPack.getEntity().playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 0.2F, 1.45F);
                    }
                }
            };
            summondSwordEntity.setModel(xingKongSlashBladeRegister.saModel);
            Vector3d entityPos = slashBladeEntityPack.getEntity().getPositionVec();
            pos.set(new Vector3d(entityPos.getX(),
                    entityPos.getY() + 10,
                    entityPos.getZ()));
            summondSwordEntity.setPosition(
                    pos.get().getX(),
                    pos.get().getY(),
                    pos.get().getZ()
            );
            summondSwordEntity.setColor(slashBladeEntityPack.getSlashBladePack().getSlashBladeState().getColorCode());
            summondSwordEntity.setSize(12);
            summondSwordEntity.setMaxDelay(life);
            summondSwordEntity.setStartDelay(life);
            summondSwordEntity.setSeep(0);
            slashBladeEntityPack.getEntity().world.addEntity(summondSwordEntity);
            ONLY_ONE_MAP.put(summondSwordEntity.getEntity(), summondSwordEntity);
        }

        @Override
        public void defaultConfig() {
            super.defaultConfig();
            attack = 0.12f;
            attackNumber = 4;
            life = 100;
            attackRange = 128;
            generateRange = 12;
        }
    }

    @VoluntarilyRegister
    public static class XingKongSlashBladeRecipeRegister extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {
        @VoluntarilyAssignment
        protected XingKongSlashBladeRegister xingKongSlashBladeRegister;

        @VoluntarilyAssignment
        protected DragonSlashBladeRegister.DragonLambdaSlashBladeRegister dragonLambdaSlashBladeRegister;

        @VoluntarilyAssignment
        protected DivinitySE.DivinityLambdaSE divinityLambdaSE;


        @Override
        protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {
            SlashBladePack dragonLambda = dragonLambdaSlashBladeRegister.getSlashBladePack();
            dragonLambda.getSlashBladeState().setKillCount(20000);
            dragonLambda.getSlashBladeState().setRefine(1000);

            return new SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack(
                    ListUtil.of(
                            "A A",
                            " V ",
                            "A A"
                    ),
                    MapUtil.of(
                            "A", new IRecipeInItemPack.OfItemSE(divinityLambdaSE, 7.5f),
                            "V", new IRecipeInItemPack.OfSlashBlade(dragonLambda.getItemStack())
                    ),
                    "V",
                    new IResultPack.OfSlashBladeRegister(xingKongSlashBladeRegister)
            );
        }
    }

}
