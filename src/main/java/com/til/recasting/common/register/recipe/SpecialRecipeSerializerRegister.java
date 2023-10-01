package com.til.recasting.common.register.recipe;

import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.config.ConfigManage;
import com.til.glowing_fire_glow.common.register.StaticVoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.recipe.RecipeRegister;
import com.til.glowing_fire_glow.common.register.recipe.RecipeSerializerRegister;
import com.til.glowing_fire_glow.common.util.Delayed;
import com.til.glowing_fire_glow.common.util.gson.GsonManage;
import com.til.recasting.common.data.IRecipeInItemPack;
import com.til.recasting.common.data.IResultPack;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;


@VoluntarilyRegister
@StaticVoluntarilyAssignment
public class SpecialRecipeSerializerRegister extends RecipeSerializerRegister<SpecialRecipeSerializerRegister.SpecialRecipe> {
    @VoluntarilyAssignment
    protected static GsonManage gsonManage;

    @Override
    public IRecipeSerializer<SpecialRecipe> initRecipeSerializer() {
        return new SpecialRecipeSerializer();
    }


    public static class SpecialRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<SpecialRecipe> {

        @Override
        public SpecialRecipe read(ResourceLocation recipeId, JsonObject json) {
            SpecialRecipePack slashBladeUpPack = gsonManage.getGson().fromJson(json, SpecialRecipePack.class);
            return SpecialRecipe.of(recipeId, slashBladeUpPack, this);
        }

        @Nullable
        @Override
        public SpecialRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            String json = buffer.readString();
            return read(recipeId, gsonManage.getGson().fromJson(json, JsonObject.class));
        }

        @Override
        public void write(PacketBuffer buffer, SpecialRecipe recipe) {
            buffer.writeString(gsonManage.getGson().toJson(recipe.getSpecialPack()));
        }
    }


    public static class SpecialRecipe extends ShapedRecipe {

        public static SpecialRecipe of(ResourceLocation idIn, SpecialRecipePack specialPack, IRecipeSerializer<SpecialRecipe> serializer) {
            int maxHeight = specialPack.pattern.size();
            int maxWidth = 0;
            for (String s : specialPack.pattern) {
                maxWidth = Math.max(maxHeight, s.length());
            }
            NonNullList<IRecipeInItemPack> recipeItems = deserializeIngredients(specialPack.pattern.toArray(new String[0]), specialPack.key, maxWidth, maxHeight);
            return new SpecialRecipe(idIn, "", maxWidth, maxHeight, recipeItems, serializer, specialPack);
        }

        public static NonNullList<IRecipeInItemPack> deserializeIngredients(String[] pattern, Map<String, IRecipeInItemPack> keys, int patternWidth, int patternHeight) {
            NonNullList<IRecipeInItemPack> nonNullList = NonNullList.withSize(patternWidth * patternHeight, IRecipeInItemPack.EMPTY);
            Set<String> set = Sets.newHashSet(keys.keySet());
            set.remove(" ");

            for (int i = 0; i < pattern.length; ++i) {
                for (int j = 0; j < pattern[i].length(); ++j) {
                    String s = pattern[i].substring(j, j + 1);
                    if (s.equals(" ")) {
                        continue;
                    }
                    IRecipeInItemPack ingredient = keys.get(s);
                    if (ingredient == null) {
                        throw new JsonSyntaxException("Pattern references symbol '" + s + "' but it's not defined in the key");
                    }

                    set.remove(s);
                    nonNullList.set(j + patternWidth * i, ingredient);
                }
            }

            if (!set.isEmpty()) {
                throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
            } else {
                return nonNullList;
            }
        }

        protected SpecialRecipePack specialPack;
        protected IRecipeSerializer<SpecialRecipe> serializer;
        protected NonNullList<IRecipeInItemPack> recipeItems;
        protected NonNullList<Ingredient> ingredientNonNullList;

        public SpecialRecipe(
                ResourceLocation idIn,
                String groupIn,
                int recipeWidthIn,
                int recipeHeightIn,
                NonNullList<IRecipeInItemPack> recipeItemsIn,
                IRecipeSerializer<SpecialRecipe> serializer,
                SpecialRecipePack specialPack) {
            super(idIn, groupIn, recipeWidthIn, recipeHeightIn, null, specialPack.result.getOutItemStack());
            this.serializer = serializer;
            this.recipeItems = recipeItemsIn;
            this.specialPack = specialPack;
        }

        @Override
        public NonNullList<Ingredient> getIngredients() {
            if (ingredientNonNullList == null) {
                ingredientNonNullList = NonNullList.withSize(recipeItems.size(), Ingredient.EMPTY);
            }
            for (int i = 0; i < recipeItems.size(); i++) {
                ingredientNonNullList.set(i, recipeItems.get(i).toIngredient());
            }
            return ingredientNonNullList;
        }

        @Override
        public boolean matches(CraftingInventory inv, World worldIn) {
            for (int i = 0; i <= inv.getWidth() - this.getRecipeWidth(); ++i) {
                for (int j = 0; j <= inv.getHeight() - this.getRecipeHeight(); ++j) {
                    if (this.checkMatch(inv, i, j)) {
                        return true;
                    }
                }
            }
            return false;
        }

        protected boolean checkMatch(CraftingInventory craftingInventory, int width, int height) {
            for (int i = 0; i < craftingInventory.getWidth(); ++i) {
                for (int j = 0; j < craftingInventory.getHeight(); ++j) {
                    int k = i - width;
                    int l = j - height;
                    //int id = this.getRecipeWidth() - k - 1 + l * this.getRecipeWidth() /*: k + l * this.getRecipeWidth()*/;
                    int id = k + l * this.getRecipeWidth();
                    if (k < 0 || l < 0 || k >= this.getRecipeWidth() || l >= this.getRecipeHeight()) {
                        return false;
                    }
                    ItemStack itemStack = craftingInventory.getStackInSlot(i + j * craftingInventory.getWidth());
                    if (!recipeItems.get(id).test(itemStack)) {
                        return false;
                    }
                }
            }
            return true;
        }

        @Override
        public IRecipeSerializer<?> getSerializer() {
            return serializer;
        }

        public SpecialRecipePack getSpecialPack() {
            return specialPack;
        }
    }

    public static class SpecialRecipePack {
        public List<String> pattern;
        public Map<String, IRecipeInItemPack> key;
        public IResultPack result;

        public SpecialRecipePack(List<String> pattern, Map<String, IRecipeInItemPack> key, IResultPack result) {
            this.pattern = pattern;
            this.key = key;
            this.result = result;
        }
    }

    public static abstract class SpecialRecipeRegister extends RecipeRegister<SpecialRecipe, SpecialRecipeSerializerRegister> {

        @VoluntarilyAssignment
        protected ConfigManage configManage;

        @ConfigField
        protected Delayed<SpecialRecipePack> specialRecipePack;

        @Override
        protected Class<SpecialRecipe> initRecipeClass() {
            return SpecialRecipe.class;
        }

        @Override
        protected Class<SpecialRecipeSerializerRegister> initRecipeSerializerClass() {
            return SpecialRecipeSerializerRegister.class;
        }

        @Override
        public void defaultConfig() {
            configManage.addDelayedWrite(this);
            specialRecipePack = new SpecialRecipePackDelayed(this::defaultSpecialRecipePackDelayed);
        }

        @Override
        public SpecialRecipe mackRecipe() {
            return SpecialRecipe.of(getName(), getSpecialRecipePack(), getRecipeSerializer().getRecipeSerializer());
        }

        protected abstract SpecialRecipePack defaultSpecialRecipePackDelayed();

        public SpecialRecipePack getSpecialRecipePack() {
            return specialRecipePack.get();
        }

        public static class SpecialRecipePackDelayed extends Delayed<SpecialRecipePack> {
            public SpecialRecipePackDelayed(Supplier<SpecialRecipePack> supplier) {
                super(supplier);
            }
        }
    }
}
