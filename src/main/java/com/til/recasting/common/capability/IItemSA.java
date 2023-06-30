package com.til.recasting.common.capability;

import com.til.glowing_fire_glow.common.save.SaveField;
import com.til.recasting.common.register.sa.SA_Register;

public interface IItemSA {
    SA_Register getSA();

    void setSA(SA_Register sa_register);

    /***
     * 尝试更好
     */
    default void tryReplace(SlashBladePack slashBladePack) {
        slashBladePack.isa.setSA(getSA());
        //todo 状态的sa动作切换


    }

    class ItemSA implements IItemSA {

        @SaveField
        protected SA_Register sa_register;

        @Override
        public SA_Register getSA() {
            return sa_register;
        }

        @Override
        public void setSA(SA_Register sa_register) {
            this.sa_register = sa_register;
        }
    }
}
