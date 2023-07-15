package com.til.recasting.common.capability;

import com.til.glowing_fire_glow.common.save.SaveField;
import net.minecraft.world.biome.Biome;

import javax.annotation.Nullable;

public interface IItemBiome {
    @Nullable
    Biome getBiome();

    void setBiome(Biome biome);

    class ItemBiome implements IItemBiome {

        @SaveField
        @Nullable
        protected Biome biome;

        @Override
        public Biome getBiome() {
            return biome;
        }

        @Override
        @Nullable
        public void setBiome(Biome biome) {
            this.biome = biome;
        }
    }

}
