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
import com.til.recasting.common.entity.SummondSwordEntity;
import com.til.recasting.common.register.entity_type.SummondSwordEntityTypeRegister;
import com.til.recasting.common.register.overall_config.SummondSwordOverallConfig;
import com.til.recasting.common.register.recipe.SlashBladeRecipeSerializerRegister;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import com.til.recasting.common.register.slash_blade.sa.SA_Register;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.Tags;

import java.awt.*;
import java.util.Random;

@VoluntarilyRegister
public class KamuytukiSlashBladeRegister extends SlashBladeRegister {

    @VoluntarilyAssignment
    protected SpatialEarthquakeSA spatialEarthquakeSA;


    @Override
    protected void defaultItemStackConfig(ItemStack itemStack) {
        super.defaultItemStackConfig(itemStack);
        slashBladePack.getSlashBladeState().setBaseAttackModifier(10);
        slashBladePack.getSlashBladeState().setEffectColor(new Color(15066290));
        slashBladePack.setSA(spatialEarthquakeSA);
    }

    /***
     * 空间震
     */
    @VoluntarilyRegister
    public static class SpatialEarthquakeSA extends SA_Register {

        @VoluntarilyAssignment
        protected SummondSwordOverallConfig summondSwordOverallConfig;

        @VoluntarilyAssignment
        protected SummondSwordEntityTypeRegister summondSwordEntityTypeRegister;

        @ConfigField
        protected int attackAmount;

        @ConfigField
        protected float attack;

        @VoluntarilyAssignment
        protected KannazukiSlashBladeRegister.KannazukiSA kannazukiSA;

        @Override
        public void trigger(UseSlashBladeEntityPack slashBladeEntityPack) {
            kannazukiSA.trigger(slashBladeEntityPack);
            Vector3d attackPos = slashBladeEntityPack.getAttackPos();

            Vector3d centerAttackPos = attackPos.add(0, summondSwordOverallConfig.getHeavyRainYOffset(), 0);
            Vector3d offset = summondSwordOverallConfig.getHeavyRainOffset();
            Random random = slashBladeEntityPack.getEntity().getRNG();
            for (int i = 0; i < attackAmount; i++) {
                Vector3d pos = centerAttackPos.add(
                        -offset.getX() / 2 + random.nextDouble() * offset.getX(),
                        -offset.getY() / 2 + random.nextDouble() * offset.getY(),
                        -offset.getZ() / 2 + random.nextDouble() * offset.getZ());

                SummondSwordEntity summondSwordEntity = new SummondSwordEntity(
                        summondSwordEntityTypeRegister.getEntityType(),
                        slashBladeEntityPack.getEntity().world,
                        slashBladeEntityPack.getEntity()
                );
                slashBladeEntityPack.getSlashBladePack().getSlashBladeStateSupplement().decorate(summondSwordEntity);
                summondSwordEntity.setColor(slashBladeEntityPack.getSlashBladePack().getSlashBladeState().getColorCode());
                summondSwordEntity.setDamage(attack);
                summondSwordEntity.setMaxDelay(10 + random.nextInt(30));
                summondSwordEntity.setStartDelay(random.nextInt(10));
                summondSwordEntity.setPosition(pos.getX(), pos.getY(), pos.getZ());
                summondSwordEntity.lookAt(new Vector3d(
                        -offset.getX() / 2 + random.nextDouble() * offset.getX(),
                        -summondSwordOverallConfig.getHeavyRainYOffset() - offset.getY() / 2 + random.nextDouble() * offset.getY(),
                        -offset.getZ() / 2 + random.nextDouble() * offset.getZ()
                ), true);
                summondSwordEntity.setRoll(slashBladeEntityPack.getEntity().getRNG().nextInt(360));
                slashBladeEntityPack.getEntity().world.addEntity(summondSwordEntity);
            }
        }

        @Override
        public void defaultConfig() {
            super.defaultConfig();
            attackAmount = 42;
            attack = 0.4f;
        }
    }

    @VoluntarilyRegister
    public static class KamuytukiSlashBladeRecipeRegister extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {
        @VoluntarilyAssignment
        protected KamuytukiSlashBladeRegister kamuytukiSlashBladeRegister;

        @VoluntarilyAssignment
        protected KannazukiSlashBladeRegister kannazukiSlashBladeRegister;

        @VoluntarilyAssignment
        protected ShimotsukiSlashBladeRegister shimotsukiSlashBladeRegister;

        @VoluntarilyAssignment
        protected ShiwasuSlashBladeRegister shiwasuSlashBladeRegister;

        @Override
        protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {
            SlashBladePack kannazukiSlashBlade = kannazukiSlashBladeRegister.getSlashBladePack();
            kannazukiSlashBlade.getSlashBladeState().setKillCount(1000);
            kannazukiSlashBlade.getSlashBladeState().setRefine(100);

            SlashBladePack shimotsukiSlashBlade = shimotsukiSlashBladeRegister.getSlashBladePack();
            shimotsukiSlashBlade.getSlashBladeState().setKillCount(500);
            shimotsukiSlashBlade.getSlashBladeState().setRefine(50);

            SlashBladePack shiwasuSlashBlade = shiwasuSlashBladeRegister.getSlashBladePack();
            shiwasuSlashBlade.getSlashBladeState().setKillCount(500);
            shiwasuSlashBlade.getSlashBladeState().setRefine(50);

            return new SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack(
                    ListUtil.of(
                            "AAA",
                            "BVC",
                            "AAA"
                    ),
                    MapUtil.of(
                            "A", new IRecipeInItemPack.OfTag(Tags.Items.STORAGE_BLOCKS_EMERALD.getName()),
                            "V", new IRecipeInItemPack.OfSlashBlade(kannazukiSlashBlade),
                            "B", new IRecipeInItemPack.OfSlashBlade(shiwasuSlashBlade),
                            "C", new IRecipeInItemPack.OfSlashBlade(shimotsukiSlashBlade)
                    ),
                    "V",
                    new IResultPack.OfSlashBladeRegister(kamuytukiSlashBladeRegister)
            );
        }
    }
}
