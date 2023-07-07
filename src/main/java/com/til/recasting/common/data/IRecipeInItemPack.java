package com.til.recasting.common.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.til.glowing_fire_glow.util.gson.AcceptTypeJson;
import com.til.recasting.common.capability.SlashBladePack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;

import javax.annotation.Nullable;
import java.util.function.Predicate;

@AcceptTypeJson
public interface IRecipeInItemPack extends Predicate<ItemStack> {
    Ingredient toIngredient();

    IRecipeInItemPack EMPTY = new IRecipeInItemPack() {
        @Override
        public Ingredient toIngredient() {
            return Ingredient.EMPTY;
        }

        @Override
        public boolean test(ItemStack itemStack) {
            return itemStack.isEmpty();
        }
    };


    class OfItem implements IRecipeInItemPack {
        protected Item item;

        public OfItem() {
        }

        public OfItem(Item item) {
            this.item = item;
        }

        @Override
        public boolean test(ItemStack itemStack) {
            return itemStack.getItem().equals(item);
        }

        @Override
        public Ingredient toIngredient() {
            return Ingredient.fromItems(item);
        }
    }

    class OfTag implements IRecipeInItemPack {

        protected ITag.INamedTag<Item> itemTag;


        public OfTag() {
        }

        public OfTag(ITag.INamedTag<Item> itemTag) {
            this.itemTag = itemTag;
        }


        @Override
        public boolean test(ItemStack itemStack) {
            return itemTag.contains(itemStack.getItem());
        }


        @Override
        public Ingredient toIngredient() {
            return Ingredient.fromTag(itemTag);
        }
    }

    class OfItemStack implements IRecipeInItemPack {

        protected ItemStack itemStack;

        public OfItemStack() {
        }

        public OfItemStack(ItemStack itemStack) {
            this.itemStack = itemStack;
        }

        @Override
        public boolean test(ItemStack itemStack) {
            return itemStack.equals(this.itemStack);
        }

        @Override
        public Ingredient toIngredient() {
            return Ingredient.fromStacks(itemStack);
        }
    }

    class OfSlashBlade implements IRecipeInItemPack {

        protected ItemStack itemStack;

        @Expose
        protected SlashBladePack slashBladePack;

        public OfSlashBlade() {
        }

        public OfSlashBlade(ItemStack itemStack) {
            this.itemStack = itemStack;
        }

        @Override
        public boolean test(ItemStack itemStack) {
            SlashBladePack inSlashBladePack = new SlashBladePack(itemStack);
            if (!inSlashBladePack.isEffective()) {
                return false;
            }
            return getSlashBladePack().isExtends(inSlashBladePack);
        }

        public SlashBladePack getSlashBladePack() {
            if (slashBladePack == null) {
                slashBladePack = new SlashBladePack(itemStack);
            }
            if (!slashBladePack.isEffective()) {
                throw new RuntimeException("错误的拔刀剑物品");
            }
            return slashBladePack;
        }

        @Override
        public Ingredient toIngredient() {
            return Ingredient.fromStacks(itemStack);
        }
    }
}
