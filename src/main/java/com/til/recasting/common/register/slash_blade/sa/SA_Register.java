package com.til.recasting.common.register.sa;

import com.til.glowing_fire_glow.common.register.RegisterBasics;
import com.til.glowing_fire_glow.common.util.StringUtil;
import com.til.recasting.common.capability.UseSlashBladeEntityPack;
import mods.flammpfeil.slashblade.ability.StunManager;
import mods.flammpfeil.slashblade.capability.slashblade.ComboState;
import mods.flammpfeil.slashblade.capability.slashblade.combo.Extra;
import mods.flammpfeil.slashblade.event.FallHandler;
import mods.flammpfeil.slashblade.event.client.UserPoseOverrider;
import mods.flammpfeil.slashblade.specialattack.JudgementCut;
import mods.flammpfeil.slashblade.specialattack.SlashArts;
import net.minecraft.entity.LivingEntity;

import java.util.function.Function;

import static mods.flammpfeil.slashblade.init.DefaultResources.ExMotionLocation;

public abstract class SA_Register extends RegisterBasics {

    protected SlashArts slashArts;

    @Override
    protected void init() {
        super.init();
        slashArts = initSlashArts();
    }


    protected SlashArts initSlashArts() {
        return new ExtendSlashArts(this);
    }

    public abstract void trigger(UseSlashBladeEntityPack slashBladeEntityPack);

    public SlashArts getSlashArts() {
        return slashArts;
    }

    public static class ExtendSlashArts extends SlashArts {

        protected ComboState comboState;

        protected SA_Register sa_register;

        public ExtendSlashArts(SA_Register sa_register) {
            super(StringUtil.formatLang(sa_register.getName()), e -> Extra.EX_VOID_SLASH);
            this.sa_register = sa_register;
            this.comboState = initComboState();
        }

        protected ComboState initComboState() {
            return new ComboState(getName(), 50,
                    () -> 1923, () -> 1928, () -> 0.5f, () -> false, () -> 0,
                    ExMotionLocation, e -> comboState, () -> Extra.EX_JUDGEMENT_CUT_SHEATH_AIR)
                    .addTickAction(ComboState.TimeLineTickAction.getBuilder().put(0, e -> {
                        UseSlashBladeEntityPack useSlashBladeEntityPack = new UseSlashBladeEntityPack(e);
                        if (!useSlashBladeEntityPack.isEffective()) {
                            return;
                        }
                        sa_register.trigger(useSlashBladeEntityPack);
                    }).build())
                    .addTickAction(FallHandler::fallResist)
                    .addTickAction(UserPoseOverrider::resetRot)
                    .addHitEffect(StunManager::setStun);
        }

        @Override
        public ComboState doArts(ArtsType type, LivingEntity user) {
            switch (type) {
                case Fail:
                case Success:
                case Jackpot:
                    return comboState;
                case Broken:
                    return Extra.EX_VOID_SLASH;
            }
            return ComboState.NONE;
        }
    }
}
