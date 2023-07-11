package com.til.recasting.common.capability;

import com.til.glowing_fire_glow.common.save.SaveField;
import com.til.glowing_fire_glow.common.util.MathUtil;
import com.til.recasting.common.register.sa.SA_Register;
import com.til.recasting.common.register.slash_blade.se.SE_Register;
import net.minecraft.nbt.CompoundNBT;

import java.util.HashMap;
import java.util.Map;

public interface ISE {


    Map<SE_Register, SE_Pack> getAllSE();

    /***
     * 获取se描述
     */
    SE_Pack getPack(SE_Register se_register);

    /***
     * 删除SE
     */
    void delete(SE_Register se_register);

    /***
     * 有没有se
     */
    boolean hasSE(SE_Register se_register);


    class SE_Pack {

        protected SE_Register se_register;

        /***
         * 当前se的等级
         */
        protected int level;

        /***
         * se的自定义数据
         */
        protected CompoundNBT customData;

        public SE_Pack() {
        }

        public SE_Pack(SE_Register se_register) {
            this.se_register = se_register;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = MathUtil.clamp(level, se_register.getMaxLevel(), 0);
        }


        public CompoundNBT getCustomData() {
            if (customData == null) {
                customData = new CompoundNBT();
            }
            return customData;
        }

        public boolean isEmpty() {
            return level <= 0;
        }

    }

    class SE implements ISE {

        @SaveField
        protected Map<SE_Register, SE_Pack> map = new HashMap<>();


        @Override
        public Map<SE_Register, SE_Pack> getAllSE() {
            return map;
        }

        @Override
        public SE_Pack getPack(SE_Register se_register) {
            if (map.containsKey(se_register)) {
                return map.get(se_register);
            }
            SE_Pack se_pack = new SE_Pack(se_register);
            map.put(se_register, se_pack);
            return se_pack;
        }


        @Override
        public void delete(SE_Register se_register) {
            map.remove(se_register);
        }

        @Override
        public boolean hasSE(SE_Register se_register) {
            if (map.containsKey(se_register)) {
                SE_Pack se_pack = map.get(se_register);
                return !se_pack.isEmpty();
            }
            return false;
        }
    }
}
