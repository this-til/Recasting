package com.til.recasting.common.capability;

import com.til.glowing_fire_glow.common.save.SaveField;
import com.til.recasting.common.register.sa.SA_Register;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nullable;

public interface ISA {
    @Nullable
    SA_Register getSA();


    void setSA(SA_Register sa);

    CompoundNBT getCustomData();

    class SA implements ISA {
        @SaveField
        protected SA_Register sa_register;

        @SaveField
        protected CompoundNBT customData;

        @Override
        @Nullable
        public SA_Register getSA() {
            return sa_register;
        }

        @Override
        public void setSA(SA_Register sa_register) {
            this.sa_register = sa_register;
        }

        @Override
        public CompoundNBT getCustomData() {
            if (customData == null) {
                customData = new CompoundNBT();
            }
            return customData;
        }
    }
}
