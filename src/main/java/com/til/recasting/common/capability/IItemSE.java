package com.til.recasting.common.capability;

import com.til.glowing_fire_glow.common.save.SaveField;
import com.til.recasting.common.data.SlashBladePack;
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


    /***
     * 尝试升级
     */
    default boolean tryUp(SlashBladePack slashBladePack) {
        ISE.SE_Pack se_pack = slashBladePack.ise.getPack(getSE());
        float successRate = getBasicsSuccessRate();
        int allLevel = 1;
        for (ISE.SE_Pack value : slashBladePack.ise.getAllSE().values()) {
            allLevel += value.getLevel();
        }
        successRate = successRate / (allLevel + 1);
        if (RANDOM.nextDouble() < successRate) {
            se_pack.setLevel(se_pack.getLevel() + 1);
            return true;
        }
        return false;
    }

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
