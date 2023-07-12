package com.til.recasting.common.data;

import com.til.glowing_fire_glow.common.util.gson.AcceptTypeJson;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import net.minecraft.item.ItemStack;

@AcceptTypeJson
public interface IResultPack {

    ItemStack getOutItemStack();

    class OfItemStack implements IResultPack {
        protected ItemStack itemStack;

        public OfItemStack() {

        }

        public OfItemStack(ItemStack itemStack) {
            this.itemStack = itemStack;
        }

        @Override
        public ItemStack getOutItemStack() {
            return itemStack;
        }
    }

    class OfSlashBladeRegister implements IResultPack {
        protected SlashBladeRegister slashBladeRegister;

        public OfSlashBladeRegister() {

        }

        public OfSlashBladeRegister(SlashBladeRegister slashBladeRegister) {
            this.slashBladeRegister = slashBladeRegister;
        }

        @Override
        public ItemStack getOutItemStack() {
            return slashBladeRegister.getSlashBladePack().itemStack;
        }
    }
}
