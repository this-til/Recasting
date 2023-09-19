package com.til.recasting.common.register.slash_blade.se.instance;

import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.ListUtil;
import com.til.glowing_fire_glow.common.util.MapUtil;
import com.til.glowing_fire_glow.common.util.math.NumberPack;
import com.til.recasting.common.capability.ISE;
import com.til.recasting.common.data.IRecipeInItemPack;
import com.til.recasting.common.data.IResultPack;
import com.til.recasting.common.entity.SlashEffectEntity;
import com.til.recasting.common.event.EventSlashBladeDoSlash;
import com.til.recasting.common.register.entity_type.SlashEffectEntityTypeRegister;
import com.til.recasting.common.register.recipe.SpecialRecipeSerializerRegister;
import com.til.recasting.common.register.slash_blade.se.SE_Register;
import com.til.recasting.common.register.world.item.SE_DepositItemRegister;
import com.til.recasting.common.register.world.item.SoulItemRegister;
import mods.flammpfeil.slashblade.init.SBItems;
import mods.flammpfeil.slashblade.util.VectorHelper;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.awt.*;


/***
 * 神狐祝灵
 */
@VoluntarilyRegister
public class GodWishSE extends SE_Register {


    @ConfigField
    protected NumberPack cool;

    @ConfigField
    protected NumberPack attack;

    @ConfigField
    protected int blackColor;

    @ConfigField
    protected int whiterColor;

    @SubscribeEvent
    protected void onEventSlashBladeDoSlash(EventSlashBladeDoSlash event) {
        if (!event.pack.getSlashBladePack().getIse().hasSE(this)) {
            return;
        }
        ISE.SE_Pack se_pack = event.pack.getSlashBladePack().getIse().getPack(this);
        if (!se_pack.tryTime(event.pack.getEntity().world.getGameTime(), (long) cool.of(se_pack.getLevel()))) {
            return;
        }
        Vector3d pos = event.pack.getEntity().getPositionVec()
                .add(0.0D, (double) event.pack.getEntity().getEyeHeight() * 0.75D, 0.0D)
                .add(event.pack.getEntity().getLookVec().scale(0.3f));

        pos = pos.add(VectorHelper.getVectorForRotation(-90.0F, event.pack.getEntity().getYaw(0)).scale(event.centerOffset.y))
                .add(VectorHelper.getVectorForRotation(0, event.pack.getEntity().getYaw(0) + 90).scale(event.centerOffset.z))
                .add(event.pack.getEntity().getLookVec().scale(event.centerOffset.z));

        int roll = event.pack.getEntity().getRNG().nextInt(360);

        SlashEffectEntity jc_1 = new SlashEffectEntity(GlowingFireGlow.getInstance().getReflexManage().getVoluntarilyRegisterOfClass(SlashEffectEntityTypeRegister.class).getEntityType(),
                event.pack.getEntity().world, event.pack.getEntity());
        event.pack.getSlashBladePack().getSlashBladeStateSupplement().decorate(jc_1);
        jc_1.setPosition(pos.getX(), pos.getY(), pos.getZ());
        jc_1.setRoll(roll);
        jc_1.setColor(blackColor);
        jc_1.setMute(event.slashEffectEntity.isMute());
        jc_1.setDamage((float) attack.of(se_pack.getLevel()));
        jc_1.setSize(event.basicsRange);
        event.pack.getEntity().world.addEntity(jc_1);

        SlashEffectEntity jc_2 = new SlashEffectEntity(GlowingFireGlow.getInstance().getReflexManage().getVoluntarilyRegisterOfClass(SlashEffectEntityTypeRegister.class).getEntityType(),
                event.pack.getEntity().world, event.pack.getEntity());
        event.pack.getSlashBladePack().getSlashBladeStateSupplement().decorate(jc_2);
        jc_2.setPosition(pos.getX(), pos.getY(), pos.getZ());
        jc_2.setRoll(roll + 90);
        jc_2.setColor(whiterColor);
        jc_2.setMute(event.slashEffectEntity.isMute());
        jc_2.setDamage((float) attack.of(se_pack.getLevel()));
        jc_2.setSize(event.basicsRange);
        event.pack.getEntity().world.addEntity(jc_2);
    }

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        attack = new NumberPack(0.1, 0.15f);
        cool = new NumberPack(10, -1);
        blackColor = new Color(0, 0, 0, 255).getRGB();
        whiterColor = new Color(255, 255, 255, 255).getRGB();
    }

    @VoluntarilyRegister
    public static class GodWishSE_Recipe extends SpecialRecipeSerializerRegister.SpecialRecipeRegister {

        @VoluntarilyAssignment
        protected GodWishSE godWishSE;

        @VoluntarilyAssignment
        protected SE_DepositItemRegister se_depositItemRegister;

        @VoluntarilyAssignment
        protected SoulItemRegister.SoulCubeItemRegister soulItemRegister;

        @VoluntarilyAssignment
        protected FoxWishBasicsSE.BlackFoxWishSE blackFoxWishSE;

        @VoluntarilyAssignment
        protected FoxWishBasicsSE.WhiteFoxWishSE whiteFoxWishSE;

        @VoluntarilyAssignment
        protected CrossChopSE crossChopSE;

        @Override
        protected SpecialRecipeSerializerRegister.SpecialRecipePack defaultSpecialRecipePackDelayed() {
            return new SpecialRecipeSerializerRegister.SpecialRecipePack(
                    ListUtil.of(
                            " E ",
                            "BAC",
                            " D "
                    ),
                    MapUtil.of(
                            "A", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(soulItemRegister.getItem())),
                            "E", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(SBItems.proudsoul_crystal)),
                            "B", new IRecipeInItemPack.OfItemSE(blackFoxWishSE),
                            "C", new IRecipeInItemPack.OfItemSE(whiteFoxWishSE),
                            "D", new IRecipeInItemPack.OfItemSE(crossChopSE)

                    ),
                    new IResultPack.OfItemStack(se_depositItemRegister.mackItemStack(godWishSE))
            );
        }
    }

}
