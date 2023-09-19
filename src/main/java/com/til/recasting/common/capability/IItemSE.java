package com.til.recasting.common.capability;

import com.til.glowing_fire_glow.common.save.SaveField;
import com.til.recasting.common.register.slash_blade.se.SE_Register;

import java.util.Random;

/**
 * @author til
 */
public interface IItemSE {

    Random RANDOM = new Random();

    SE_Register getSE();

    void setSE(SE_Register se_register);

    /***
     * 获得成功率
     */
    float getBasicsSuccessRate();

    void setBasicsSuccessRate(float successRate);


    class ItemSE implements IItemSE {

        @SaveField
        protected SE_Register se_register;

        @SaveField
        protected float successRate;


        @Override
        public SE_Register getSE() {
            return se_register;
        }

        @Override
        public void setSE(SE_Register se_register) {
            this.se_register = se_register;
        }

        @Override
        public float getBasicsSuccessRate() {
            return successRate;
        }

        @Override
        public void setBasicsSuccessRate(float successRate) {
            this.successRate = successRate;
        }

    }
}
