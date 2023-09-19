package com.til.recasting.common.capability;

import com.til.glowing_fire_glow.common.save.SaveField;
import net.minecraft.enchantment.Enchantment;

import java.util.Random;

public interface IItemEnchantment {
    Random RANDOM = new Random();

    Enchantment getEnchantment();

    void setEnchantment(Enchantment enchantment);


    float getBasicsSuccessRate();

    void setBasicsSuccessRate(float successRate);


    class ItemEnchantment implements IItemEnchantment {

        @SaveField
        protected Enchantment enchantment;

        @SaveField
        protected float successRate;

        @Override
        public Enchantment getEnchantment() {
            return enchantment;
        }

        @Override
        public void setEnchantment(Enchantment enchantment) {
            this.enchantment = enchantment;
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
