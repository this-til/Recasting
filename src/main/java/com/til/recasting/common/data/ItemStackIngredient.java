package com.til.recasting.common.data;

import com.google.gson.JsonObject;
import com.til.glowing_fire_glow.common.util.ListUtil;
import com.til.glowing_fire_glow.common.util.NBTUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;


public class ItemStackIngredient extends Ingredient {

    protected ItemStack itemStack;


    public ItemStackIngredient(ItemStack itemStack) {
        super(ListUtil.of(new StackItemList(itemStack)).stream());
    }

    public static class StackItemList implements IItemList {

        protected ItemStack itemStack;

        public StackItemList(ItemStack itemStack) {
            this.itemStack = itemStack;
        }

        @Override
        public Collection<ItemStack> getStacks() {
            return ListUtil.of(itemStack);
        }

        @Override
        public JsonObject serialize() {
            return (JsonObject) NBTUtil.toJson(itemStack.serializeNBT(), true);
        }
    }
}
