package com.til.recasting.common.register.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.til.glowing_fire_glow.common.register.recipe.RecipeRegister;
import com.til.glowing_fire_glow.util.RecipeUtil;
import com.til.glowing_fire_glow.util.gson.ConfigGson;
import com.til.recasting.common.capability.SlashBladePack;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class SlashBladeUpRecipeRegister extends RecipeRegister<SlashBladeUpRecipeRegister.SlashBladeUpRecipe> {


    @Override
    public IRecipeSerializer<SlashBladeUpRecipe> initRecipeSerializer() {
        return new SlashBladeUpRecipeSerializer();
    }

    public static class SlashBladeUpRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<SlashBladeUpRecipe> {
        @Override
        public SlashBladeUpRecipe read(ResourceLocation recipeId, JsonObject json) {
            SlashBladeUpPack slashBladeUpPack = ConfigGson.getGson().fromJson(json, SlashBladeUpPack.class);
            return SlashBladeUpRecipe.of(recipeId, slashBladeUpPack, this);
        }

        @Nullable
        @Override
        public SlashBladeUpRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            String json = buffer.readString();
            return read(recipeId, ConfigGson.getGson().fromJson(json, JsonObject.class));
        }

        @Override
        public void write(PacketBuffer buffer, SlashBladeUpRecipe recipe) {
            buffer.writeString(ConfigGson.getGson().toJson(recipe.getSlashBladeUpPack()));
        }
    }


    public static class SlashBladeUpRecipe extends ShapedRecipe {
        protected SlashBladeUpPack slashBladeUpPack;
        protected IRecipeSerializer<SlashBladeUpRecipe> serializer;
        protected SlashBladePack mainSlashBladePack;
        protected int mainKeyId;
        protected SlashBladePack outSlashBladePack;

        protected NonNullList<Ingredient> recipeItems;

        public static SlashBladeUpRecipe of(ResourceLocation idIn, SlashBladeUpPack slashBladeUpPack, IRecipeSerializer<SlashBladeUpRecipe> serializer) {
            int maxHeight = slashBladeUpPack.pattern.size();
            int maxWidth = 0;
            for (String s : slashBladeUpPack.pattern) {
                maxWidth = Math.max(maxHeight, s.length());
            }
            for (String s : slashBladeUpPack.key.keySet()) {
                if (s.length() != 1) {
                    throw new JsonSyntaxException(String.format("SlashBladeUpRecipe配方key:[%s]不正确", s));
                }
            }
            if (!slashBladeUpPack.key.containsKey(slashBladeUpPack.mainSlashBladeKey) || slashBladeUpPack.mainSlashBladeKey.length() != 1) {
                throw new JsonSyntaxException(String.format("SlashBladeUpRecipe配方mainSlashBladeKey:[%s]不正确", slashBladeUpPack.mainSlashBladeKey));
            }
            char mainChar = slashBladeUpPack.mainSlashBladeKey.charAt(0);
            boolean hasMain = false;
            int mainId = -1;
            for (int j = 0; j < slashBladeUpPack.pattern.size(); j++) {
                String s = slashBladeUpPack.pattern.get(j);
                for (int i = 0; i < s.length(); i++) {
                    if (s.charAt(i) == mainChar) {
                        if (hasMain) {
                            throw new JsonSyntaxException(String.format("SlashBladeUpRecipe配方mainSlashBladeKey:[%s]重复", slashBladeUpPack.mainSlashBladeKey));
                        }
                        hasMain = true;
                        mainId = i + j * maxWidth;
                    }
                }
            }
            if (!hasMain) {
                throw new JsonSyntaxException(String.format("SlashBladeUpRecipe配方mainSlashBladeKey:[%s]缺失", slashBladeUpPack.mainSlashBladeKey));
            }
            Ingredient ingredient = slashBladeUpPack.key.get(slashBladeUpPack.mainSlashBladeKey);
            SlashBladePack mainSlashBladePack = new SlashBladePack(ingredient.getMatchingStacks()[0]);
            if (!mainSlashBladePack.isEffective()) {
                throw new JsonSyntaxException(String.format("SlashBladeUpRecipe配方mainSlashBladeKey:[%s]对应物品失效", slashBladeUpPack.mainSlashBladeKey));
            }
            SlashBladePack potSlashBladePack = new SlashBladePack(slashBladeUpPack.result);
            if (!potSlashBladePack.isEffective()) {
                throw new JsonSyntaxException("SlashBladeUpRecipe配方输出物品失效");
            }
            NonNullList<Ingredient> recipeItems = RecipeUtil.deserializeIngredients(slashBladeUpPack.pattern.toArray(new String[0]), slashBladeUpPack.key, maxWidth, maxHeight);

            return new SlashBladeUpRecipe(idIn, "", maxWidth, maxHeight, recipeItems, slashBladeUpPack.result, serializer, mainSlashBladePack, mainId, potSlashBladePack);
        }

        public SlashBladeUpRecipe(
                ResourceLocation idIn,
                String groupIn,
                int recipeWidthIn,
                int recipeHeightIn,
                NonNullList<Ingredient> recipeItemsIn,
                ItemStack recipeOutputIn,
                IRecipeSerializer<SlashBladeUpRecipe> serializer,
                SlashBladePack mainSlashBladePack,
                int mainKeyId,
                SlashBladePack outSlashBladePack) {
            super(idIn, groupIn, recipeWidthIn, recipeHeightIn, recipeItemsIn, recipeOutputIn);
            this.serializer = serializer;
            this.mainSlashBladePack = mainSlashBladePack;
            this.outSlashBladePack = outSlashBladePack;
            this.recipeItems = recipeItemsIn;
            this.mainKeyId = mainKeyId;
        }

        @Override
        public boolean matches(CraftingInventory inv, World worldIn) {
            for (int i = 0; i <= inv.getWidth() - this.getRecipeWidth(); ++i) {
                for (int j = 0; j <= inv.getHeight() - this.getRecipeHeight(); ++j) {
                    if (this.checkMatch(inv, i, j, true) || this.checkMatch(inv, i, j, false)) {
                        return true;
                    }
                }
            }
            return false;
        }

        public boolean checkMatch(CraftingInventory craftingInventory, int width, int height, boolean p_77573_4_) {
            for (int i = 0; i < craftingInventory.getWidth(); ++i) {
                for (int j = 0; j < craftingInventory.getHeight(); ++j) {
                    int k = i - width;
                    int l = j - height;
                    int id = p_77573_4_ ? this.getRecipeWidth() - k - 1 + l * this.getRecipeWidth() : k + l * this.getRecipeWidth();
                    if (k < 0 || l < 0 || k >= this.getRecipeWidth() || l >= this.getRecipeHeight()) {
                        return false;
                    }
                    ItemStack itemStack = craftingInventory.getStackInSlot(i + j * craftingInventory.getWidth());
                    if (!recipeItems.get(id).test(itemStack)) {
                        return false;
                    }
                    if (mainKeyId == id) {
                        SlashBladePack slashBladePack = new SlashBladePack(itemStack);
                        if (!slashBladePack.isEffective()) {
                            return false;
                        }
                        if (slashBladePack.slashBladeState.getKillCount() < slashBladePack.slashBladeState.getKillCount()) {
                            return false;
                        }
                        if (slashBladePack.slashBladeState.getRefine() < slashBladePack.slashBladeState.getRefine()) {
                            return false;
                        }
                    } else if (!recipeItems.get(id).test(itemStack)) {
                        return false;
                    }
                }
            }

            return true;
        }

        @Override
        public ItemStack getCraftingResult(CraftingInventory inv) {
            return null;
        }

        @Override
        public IRecipeSerializer<?> getSerializer() {
            return serializer;
        }

        public SlashBladeUpPack getSlashBladeUpPack() {
            return slashBladeUpPack;
        }


        @Override
        public boolean isDynamic() {
            return false;
        }


    }

    public static class SlashBladeUpPack {
        public List<String> pattern;
        public Map<String, Ingredient> key;
        public String mainSlashBladeKey;
        public ItemStack result;
    }

}
