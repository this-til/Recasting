package com.til.recasting.common.register.slash_blade.se.instance;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.ListUtil;
import com.til.glowing_fire_glow.common.util.MapUtil;
import com.til.glowing_fire_glow.common.util.math.NumberPack;
import com.til.recasting.common.capability.ISE;
import com.til.recasting.common.data.IRecipeInItemPack;
import com.til.recasting.common.data.IResultPack;
import com.til.recasting.common.entity.SummondSwordEntity;
import com.til.recasting.common.event.EventSlashBladeDoSlash;
import com.til.recasting.common.register.entity_type.SummondSwordEntityTypeRegister;
import com.til.recasting.common.register.recipe.SpecialRecipeSerializerRegister;
import com.til.recasting.common.register.slash_blade.se.SE_Register;
import com.til.recasting.common.register.world.item.SE_DepositItemRegister;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.awt.*;

public abstract class FoxWishBasicsSE extends SE_Register {
    @VoluntarilyAssignment
    protected SummondSwordEntityTypeRegister summondSwordEntityTypeRegister;

    @ConfigField
    protected NumberPack cool;

    @ConfigField
    protected NumberPack attack;
    @ConfigField
    protected NumberPack attackNumber;

    @ConfigField
    protected int color;


    @SubscribeEvent
    protected void onEventSlashBladeDoSlash(EventSlashBladeDoSlash event) {
        if (!event.pack.getSlashBladePack().getIse().hasSE(this)) {
            return;
        }
        ISE.SE_Pack se_pack = event.pack.getSlashBladePack().getIse().getPack(this);
        if (!se_pack.tryTime(event.pack.getEntity().world.getGameTime(), (long) cool.of(se_pack.getLevel()))) {
            return;
        }
        int n = (int) attackNumber.of(se_pack.getLevel());
        for (int i = 0; i < n; i++) {
            SummondSwordEntity summondSwordEntity = new SummondSwordEntity(summondSwordEntityTypeRegister.getEntityType(), event.pack.getEntity().world, event.pack.getEntity());
            event.pack.getSlashBladePack().getSlashBladeStateSupplement().decorate(summondSwordEntity);
            summondSwordEntity.setSize(0.6f);
            summondSwordEntity.setColor(color);
            summondSwordEntity.setDamage((float) attack.of(se_pack.getLevel()));
            summondSwordEntity.setMaxDelay(5);
            summondSwordEntity.setStartDelay(event.pack.getEntity().getRNG().nextInt(10));
            event.pack.getEntity().world.addEntity(summondSwordEntity);
        }
        event.pack.getEntity().playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 0.2F, 1.45F);
    }

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        cool = new NumberPack(10, -1);
        attack = new NumberPack(0, 0.05);
        attackNumber = new NumberPack(2, 0.5);
        color = new Color(0, 0, 0, 255).getRGB();
    }

    /***
     * 黑狐祝灵
     */
    @VoluntarilyRegister
    public static class BlackFoxWishSE extends FoxWishBasicsSE {

        @VoluntarilyRegister
        public static class BlackFoxWishSE_RecipeRegister extends SpecialRecipeSerializerRegister.SpecialRecipeRegister {

            @VoluntarilyAssignment
            protected SE_DepositItemRegister se_depositItemRegister;

            @VoluntarilyAssignment
            protected BlackFoxWishSE blackFoxWishSE;


            @VoluntarilyAssignment
            protected MomentSE momentSE;


            @Override
            protected SpecialRecipeSerializerRegister.SpecialRecipePack defaultSpecialRecipePackDelayed() {
                return new SpecialRecipeSerializerRegister.SpecialRecipePack(
                        ListUtil.of(
                                "  A",
                                " V ",
                                "A  "
                        ),
                        MapUtil.of(
                                "A", new IRecipeInItemPack.OfItemEnchantment(Enchantments.SMITE),
                                "V", new IRecipeInItemPack.OfItemSE(momentSE)
                        ),
                        new IResultPack.OfItemStack(se_depositItemRegister.mackItemStack(blackFoxWishSE))
                );
            }
        }

    }

    /***
     * 白狐祝灵
     */
    @VoluntarilyRegister
    public static class WhiteFoxWishSE extends FoxWishBasicsSE {

        @Override
        public void defaultConfig() {
            super.defaultConfig();
            color = new Color(255, 255, 255, 255).getRGB();
        }

        @VoluntarilyRegister
        public static class WhiteFoxWishSE_RecipeRegister extends SpecialRecipeSerializerRegister.SpecialRecipeRegister {

            @VoluntarilyAssignment
            protected SE_DepositItemRegister se_depositItemRegister;

            @VoluntarilyAssignment
            protected WhiteFoxWishSE whiteFoxWishSE;

            @VoluntarilyAssignment
            protected MomentSE momentSE;

            @Override
            protected SpecialRecipeSerializerRegister.SpecialRecipePack defaultSpecialRecipePackDelayed() {
                return new SpecialRecipeSerializerRegister.SpecialRecipePack(
                        ListUtil.of(
                                "  A",
                                " V ",
                                "A  "
                        ),
                        MapUtil.of(
                                "A", new IRecipeInItemPack.OfItemEnchantment(Enchantments.BANE_OF_ARTHROPODS),
                                "V", new IRecipeInItemPack.OfItemSE(momentSE)
                        ),
                        new IResultPack.OfItemStack(se_depositItemRegister.mackItemStack(whiteFoxWishSE))
                );
            }
        }
    }
}
