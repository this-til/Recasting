package com.til.recasting.common.register.slash_blade.instance;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.ListUtil;
import com.til.glowing_fire_glow.common.util.MapUtil;
import com.til.glowing_fire_glow.common.util.math.NumberPack;
import com.til.recasting.common.data.IRecipeInItemPack;
import com.til.recasting.common.data.IResultPack;
import com.til.recasting.common.data.SlashBladePack;
import com.til.recasting.common.data.UseSlashBladeEntityPack;
import com.til.recasting.common.entity.MatrixEntity;
import com.til.recasting.common.event.EventDoAttack;
import com.til.recasting.common.register.back_type.JudgementCutBackTypeRegister;
import com.til.recasting.common.register.capability.ChaosLayerCapabilityRegister;
import com.til.recasting.common.register.entity_type.MatrixEntityTypeRegister;
import com.til.recasting.common.register.entity_type.SummondSwordEntityTypeRegister;
import com.til.recasting.common.register.recipe.SlashBladeRecipeSerializerRegister;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import com.til.recasting.common.register.slash_blade.instance.original.FoxBlackSlashBladeRegister;
import com.til.recasting.common.register.slash_blade.instance.original.FoxWhiteSlashBladeRegister;
import com.til.recasting.common.register.slash_blade.sa.SA_Register;
import com.til.recasting.common.register.util.StringFinal;
import com.til.recasting.common.register.world.item.SoulItemRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@VoluntarilyRegister
public class BaGuaBigSlashBladeRegister extends SlashBladeRegister {

    @VoluntarilyAssignment
    protected BaGuaBigSlashBlade_SA baGuaBigSlashBlade_sa;

    protected ResourceLocation saTexture;

    protected ResourceLocation saModel;


    @Override
    protected void init() {
        super.init();
        summondSwordModel = new ResourceLocation(getName().getNamespace(), String.join("/", StringFinal.SUMMOND_SWORD, getName().getPath(), StringFinal.MODEL));
        summondSwordTexture = new ResourceLocation(getName().getNamespace(), String.join("/", StringFinal.SUMMOND_SWORD, getName().getPath(), StringFinal.TEXTURE));

        saTexture = new ResourceLocation(getName().getNamespace(), String.join("/", StringFinal.SPECIAL, getName().getPath(), StringFinal.TEXTURE));
        saModel = new ResourceLocation(getName().getNamespace(), String.join("/", StringFinal.SPECIAL, getName().getPath(), StringFinal.MODEL));
    }

    @Override
    protected void defaultItemStackConfig(ItemStack itemStack) {
        super.defaultItemStackConfig(itemStack);
        slashBladePack.getSlashBladeState().setBaseAttackModifier(6f);
        slashBladePack.getSlashBladeState().setEffectColor(new Color(255, 255, 255));
        slashBladePack.getSlashBladeStateSupplement().setDurable(12);
        slashBladePack.setSA(baGuaBigSlashBlade_sa);
    }

    public ResourceLocation getSaModel() {
        return saModel;
    }

    @VoluntarilyRegister
    public static class BaGuaBigSlashBlade_SA extends SA_Register {

        protected static Map<Entity, MatrixEntity> matrixEntity = new HashMap<>();

        @VoluntarilyAssignment
        protected SummondSwordEntityTypeRegister summondSwordEntityTypeRegister;

        @VoluntarilyAssignment
        protected MatrixEntityTypeRegister matrixEntityTypeRegister;

        @VoluntarilyAssignment
        protected BaGuaBigSlashBladeRegister baGuaBigSlashBladeRegister;


        @VoluntarilyAssignment
        protected JudgementCutBackTypeRegister.JudgementCutAttackBackTypeRegister judgementCutAttackBackTypeRegister;

        @VoluntarilyAssignment
        protected ChaosLayerCapabilityRegister chaosLayerCapabilityRegister;

        @ConfigField
        protected float attack;

        @ConfigField
        protected int attackInterval;

        @ConfigField
        protected int life;

        @ConfigField
        protected float size;

        @ConfigField
        protected int maxLayer;

        @ConfigField
        protected NumberPack increasedAttack;

        @ConfigField
        protected int duration;

        @Override
        public void trigger(UseSlashBladeEntityPack slashBladeEntityPack) {
            if (matrixEntity.containsKey(slashBladeEntityPack.getEntity())) {
                MatrixEntity matrix = matrixEntity.get(slashBladeEntityPack.getEntity());
                if (matrix.isAlive()) {
                    matrix.remove();
                }
            }
            Vector3d attackPos = slashBladeEntityPack.getAttackPos();
            MatrixEntity matrix = new MatrixEntity(
                    matrixEntityTypeRegister.getEntityType(),
                    slashBladeEntityPack.getEntity().world,
                    slashBladeEntityPack.getEntity()
            );
            matrix.setPosition(
                    attackPos.getX(),
                    attackPos.getY() + 0.01,
                    attackPos.getZ()
            );
            assert baGuaBigSlashBladeRegister.summondSwordModel != null;
            matrix.setModel(baGuaBigSlashBladeRegister.summondSwordModel);
            matrix.setTexture(baGuaBigSlashBladeRegister.saTexture);
            matrix.setMaxLifeTime(life);
            matrix.setAttackInterval(attackInterval);
            matrix.setDamage(attack);
            matrix.setColor(new Color(255, 255, 255).getRGB());
            matrix.getBackRunPack().addRunBack(
                    judgementCutAttackBackTypeRegister,
                    (judgementCutEntity1, hitEntity) -> hitEntity.getCapability(chaosLayerCapabilityRegister.getCapability())
                            .ifPresent(chaosLayer -> {
                                chaosLayer.addLayer(hitEntity.world.getGameTime());
                                chaosLayer.setColor(matrix.getColor());
                            })
            );
            matrix.setSize(size);
            slashBladeEntityPack.getEntity().world.addEntity(matrix);
            matrixEntity.put(slashBladeEntityPack.getEntity(), matrix);
        }

        @SubscribeEvent
        protected void onEvent(EventDoAttack event) {
            event.target.getCapability(chaosLayerCapabilityRegister.getCapability())
                    .ifPresent(chaosLayer -> {
                        int layer = chaosLayer.getLayer(event.pack.getEntity().world.getGameTime());
                        if (layer <= 0) {
                            return;
                        }
                        event.modifiedRatio *= (1 + increasedAttack.of(layer));
                    });
        }

        @Override
        public void defaultConfig() {
            super.defaultConfig();
            attack = 0.05f;
            attackInterval = 10;
            life = 200;
            size = 16;
            maxLayer = 16;
            increasedAttack = new NumberPack(0, 0.03);
            duration = 12;
        }

        public int getMaxLayer() {
            return maxLayer;
        }


        public int getDuration() {
            return duration;
        }
    }

    @VoluntarilyRegister
    public static class BaGuaBigSlashBladeRecipeRegister extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {

        @VoluntarilyAssignment
        protected BaGuaBigSlashBladeRegister baGuaBigSlashBladeRegister;

        @VoluntarilyAssignment
        protected BaGuaSlashBladeRegister baGuaSlashBladeRegister;

        @VoluntarilyAssignment
        protected SoulItemRegister.SoulCubeChangeItemRegister soulCubeChangeItemRegister;

        @VoluntarilyAssignment
        protected SoulItemRegister.SoulCubeItemRegister soulCubeItemRegister;

        @VoluntarilyAssignment
        protected FoxWhiteSlashBladeRegister.FoxWhiteLambdaSlashBladeRegister foxWhiteLambdaSlashBladeRegister;

        @VoluntarilyAssignment
        protected FoxBlackSlashBladeRegister.FoxBlackLambdaSlashBladeRegister foxBlackLambdaSlashBladeRegister;

        @Override
        protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {

            SlashBladePack baGuaSlashBlade = baGuaSlashBladeRegister.getSlashBladePack();
            baGuaSlashBlade.getSlashBladeState().setKillCount(1500);
            baGuaSlashBlade.getSlashBladeState().setRefine(150);

            SlashBladePack foxWhiteLambdaSlashBlade = foxWhiteLambdaSlashBladeRegister.getSlashBladePack();
            foxWhiteLambdaSlashBlade.getSlashBladeState().setKillCount(1000);
            foxWhiteLambdaSlashBlade.getSlashBladeState().setRefine(50);

            SlashBladePack foxBlackLambdaSlashBlade = foxBlackLambdaSlashBladeRegister.getSlashBladePack();
            foxBlackLambdaSlashBlade.getSlashBladeState().setKillCount(1000);
            foxBlackLambdaSlashBlade.getSlashBladeState().setRefine(50);

            return new SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack(
                    ListUtil.of(
                            " AC",
                            "BVB",
                            "DA "
                    ),
                    MapUtil.of(
                            "A", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(soulCubeItemRegister.getItem())),
                            "B", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(soulCubeChangeItemRegister.getItem())),
                            "C", new IRecipeInItemPack.OfSlashBlade(foxWhiteLambdaSlashBlade),
                            "D", new IRecipeInItemPack.OfSlashBlade(foxBlackLambdaSlashBlade),
                            "V", new IRecipeInItemPack.OfSlashBlade(baGuaSlashBlade.getItemStack())
                    ),
                    "V",
                    new IResultPack.OfSlashBladeRegister(baGuaBigSlashBladeRegister)
            );
        }
    }
}
