package com.til.recasting.common.register.recipe;

import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.config.ConfigManage;
import com.til.glowing_fire_glow.common.register.*;
import com.til.glowing_fire_glow.common.register.recipe.AllRecipeRegister;
import com.til.glowing_fire_glow.common.register.recipe.RecipeRegister;
import com.til.glowing_fire_glow.common.register.recipe.RecipeSerializerRegister;
import com.til.glowing_fire_glow.common.util.Delayed;
import com.til.glowing_fire_glow.common.util.gson.GsonManage;
import com.til.recasting.common.data.SlashBladePack;
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
public class SlashBladeRecipeSerializerRegister extends RecipeSerializerRegister<SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipe> {

    @VoluntarilyAssignment
    protected static GsonManage gsonManage;


    @Override
    public IRecipeSerializer<SlashBladeRecipeRecipe> initRecipeSerializer() {
        return new SlashBladeRecipeRecipeSerializer();
    }

    public static class SlashBladeRecipeRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<SlashBladeRecipeRecipe> {
        @Override
        public SlashBladeRecipeRecipe read(ResourceLocation recipeId, JsonObject json) {
            SlashBladeRecipeRecipePack slashBladeUpPack = gsonManage.getGson().fromJson(json, SlashBladeRecipeRecipePack.class);
            return SlashBladeRecipeRecipe.of(recipeId, slashBladeUpPack, this);
        }

        @Nullable
        @Override
        public SlashBladeRecipeRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            String json = buffer.readString();
            return read(recipeId, gsonManage.getGson().fromJson(json, JsonObject.class));
        }

        @Override
        public void write(PacketBuffer buffer, SlashBladeRecipeRecipe recipe) {
            buffer.writeString(gsonManage.getGson().toJson(recipe.getSlashBladeUpPack()));
        }
    }

    public static class SlashBladeRecipeRecipe extends ShapedRecipe {

        public static SlashBladeRecipeRecipe of(ResourceLocation idIn, SlashBladeRecipeRecipePack slashBladeUpPack, IRecipeSerializer<SlashBladeRecipeRecipe> serializer) {
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
            IRecipeInItemPack iRecipeInItemPack = slashBladeUpPack.key.get(slashBladeUpPack.mainSlashBladeKey);
            if (!(iRecipeInItemPack instanceof IRecipeInItemPack.OfSlashBlade) && !(iRecipeInItemPack instanceof IRecipeInItemPack.OfSlashBladeRegister)) {
                throw new JsonSyntaxException(String.format("SlashBladeUpRecipe配方mainSlashBladeKey:[%s]错误的类型", slashBladeUpPack.mainSlashBladeKey));
            }
            /*SlashBladePack mainSlashBladePack = ((IRecipeInItemPack.OfSlashBlade) iRecipeInItemPack).getSlashBladePack();
            if (!mainSlashBladePack.isEffective()) {
                throw new JsonSyntaxException(String.format("SlashBladeUpRecipe配方mainSlashBladeKey:[%s]对应物品失效", slashBladeUpPack.mainSlashBladeKey));
            }*/
            SlashBladePack potSlashBladePack = new SlashBladePack(slashBladeUpPack.result.getOutItemStack());
            if (!potSlashBladePack.isEffective(SlashBladePack.EffectiveType.isSlashBlade)) {
                throw new JsonSyntaxException("SlashBladeUpRecipe配方输出物品失效");
            }
            NonNullList<IRecipeInItemPack> recipeItems = deserializeIngredients(slashBladeUpPack.pattern.toArray(new String[0]), slashBladeUpPack.key, maxWidth, maxHeight);
            return new SlashBladeRecipeRecipe(idIn, "", maxWidth, maxHeight, recipeItems, serializer, mainId, potSlashBladePack, slashBladeUpPack);
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

        protected SlashBladeRecipeRecipePack slashBladeUpPack;
        protected IRecipeSerializer<SlashBladeRecipeRecipe> serializer;
        protected int mainKeyId;
        protected SlashBladePack outSlashBladePack;

        protected NonNullList<IRecipeInItemPack> recipeItems;
        protected NonNullList<Ingredient> ingredientNonNullList;

        public SlashBladeRecipeRecipe(
                ResourceLocation idIn,
                String groupIn,
                int recipeWidthIn,
                int recipeHeightIn,
                NonNullList<IRecipeInItemPack> recipeItemsIn,
                IRecipeSerializer<SlashBladeRecipeRecipe> serializer,
                int mainKeyId,
                SlashBladePack outSlashBladePack,
                SlashBladeRecipeRecipePack slashBladeUpPack) {
            super(idIn, groupIn, recipeWidthIn, recipeHeightIn, null, outSlashBladePack.getItemStack());
            this.serializer = serializer;
            this.outSlashBladePack = outSlashBladePack;
            this.recipeItems = recipeItemsIn;
            this.mainKeyId = mainKeyId;
            this.slashBladeUpPack = slashBladeUpPack;
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
                    int id = this.getRecipeWidth() - k - 1 + l * this.getRecipeWidth() /*: k + l * this.getRecipeWidth()*/;
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
        public ItemStack getCraftingResult(CraftingInventory inv) {
            SlashBladePack slashBladePack = null;
            for (int i = 0; i <= inv.getWidth() - this.getRecipeWidth(); ++i) {
                for (int j = 0; j <= inv.getHeight() - this.getRecipeHeight(); ++j) {
                    slashBladePack = this.getResultSlashBladePack(inv, i, j);
                    if (slashBladePack != null) {
                        return outSlashBladePack.getRecipeResult(slashBladePack).getItemStack();
                    }
                }
            }

            return outSlashBladePack.getItemStack();
        }

        @Nullable
        protected SlashBladePack getResultSlashBladePack(CraftingInventory craftingInventory, int width, int height) {
            for (int i = 0; i < craftingInventory.getWidth(); ++i) {
                for (int j = 0; j < craftingInventory.getHeight(); ++j) {
                    int k = i - width;
                    int l = j - height;
                    int id = this.getRecipeWidth() - k - 1 + l * this.getRecipeWidth() /*: k + l * this.getRecipeWidth()*/;
                    if (k < 0 || l < 0 || k >= this.getRecipeWidth() || l >= this.getRecipeHeight()) {
                        return null;
                    }
                    if (id == mainKeyId) {
                        ItemStack itemStack = craftingInventory.getStackInSlot(i + j * craftingInventory.getWidth());
                        return new SlashBladePack(itemStack);
                    }
                }
            }
            return null;
        }

        @Override
        public IRecipeSerializer<?> getSerializer() {
            return serializer;
        }

        public SlashBladeRecipeRecipePack getSlashBladeUpPack() {
            return slashBladeUpPack;
        }
    }

    public static class SlashBladeRecipeRecipePack {
        public List<String> pattern;
        public Map<String, IRecipeInItemPack> key;
        public String mainSlashBladeKey;
        public IResultPack result;

        public SlashBladeRecipeRecipePack(List<String> pattern, Map<String, IRecipeInItemPack> key, String mainSlashBladeKey, IResultPack result) {
            this.pattern = pattern;
            this.key = key;
            this.mainSlashBladeKey = mainSlashBladeKey;
            this.result = result;
        }
    }


    public abstract static class SlashBladeRecipeRegister extends RecipeRegister<SlashBladeRecipeRecipe, SlashBladeRecipeSerializerRegister> {
        @VoluntarilyAssignment
        protected ConfigManage configManage;

        @ConfigField
        protected Delayed<SlashBladeRecipeRecipePack> slashBladeUpPack;

        @Override
        protected Class<SlashBladeRecipeRecipe> initRecipeClass() {
            return SlashBladeRecipeRecipe.class;
        }

        @Override
        protected Class<SlashBladeRecipeSerializerRegister> initRecipeSerializerClass() {
            return SlashBladeRecipeSerializerRegister.class;
        }

        @Override
        public void defaultConfig() {
            configManage.addDelayedWrite(this);
            slashBladeUpPack = new SlashBladeRecipeRecipePackDelayed(this::defaultConfigSlashBladeRecipeRecipePack);
        }

        @Override
        public SlashBladeRecipeRecipe mackRecipe() {
            return SlashBladeRecipeRecipe.of(getName(), getSlashBladeRecipeRecipePack(), getRecipeSerializer().getRecipeSerializer());
        }

        protected abstract SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack();

        public SlashBladeRecipeRecipePack getSlashBladeRecipeRecipePack() {
            return slashBladeUpPack.get();
        }

        public static class SlashBladeRecipeRecipePackDelayed extends Delayed<SlashBladeRecipeRecipePack> {
            public SlashBladeRecipeRecipePackDelayed(Supplier<SlashBladeRecipeRecipePack> supplier) {
                super(supplier);
            }
        }
    }

    public static class AllSlashBladeRecipeRegister extends RegisterManage<SlashBladeRecipeRegister> {
        @Nullable
        @Override
        public Class<? extends RegisterManage<?>> getBasicsRegisterManageClass() {
            return AllRecipeRegister.class;
        }

        @Override
        public int getVoluntarilyRegisterTime() {
            return -100;
        }
    }
}
