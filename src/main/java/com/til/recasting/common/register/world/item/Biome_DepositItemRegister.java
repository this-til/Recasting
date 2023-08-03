package com.til.recasting.common.register.world.item;

import com.til.glowing_fire_glow.common.capability.CapabilityProvider;
import com.til.glowing_fire_glow.common.register.StaticVoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.world.item.ItemRegister;
import com.til.glowing_fire_glow.common.util.ListUtil;
import com.til.glowing_fire_glow.common.util.MapUtil;
import com.til.glowing_fire_glow.common.util.StringUtil;
import com.til.recasting.common.capability.CapabilityEvent;
import com.til.recasting.common.capability.IItemBiome;
import com.til.recasting.common.data.IRecipeInItemPack;
import com.til.recasting.common.data.IResultPack;
import com.til.recasting.common.register.capability.IItemBiome_CapabilityRegister;
import com.til.recasting.common.register.recipe.SpecialRecipeSerializerRegister;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.init.SBItems;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.Tag;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;

@VoluntarilyRegister
@StaticVoluntarilyAssignment
public class Biome_DepositItemRegister extends ItemRegister {

    @VoluntarilyAssignment
    protected static IItemBiome_CapabilityRegister iItemBiome_capabilityRegister;

    @Override
    protected Item initItem() {
        return new Biome_DepositItem(new Item.Properties().group(SlashBlade.SLASHBLADE));
    }

    public ItemStack mackItemStack(Biome biome) {
        ItemStack itemStack = new ItemStack(getItem());
        itemStack.getCapability(iItemBiome_capabilityRegister.getCapability()).ifPresent(pack -> pack.setBiome(biome));
        return itemStack;
    }

    @StaticVoluntarilyAssignment
    public static class Biome_DepositItem extends Item implements CapabilityEvent.ICustomCapability {

        @VoluntarilyAssignment
        protected static Biome_DepositItemRegister biome_depositItemRegister;

        public Biome_DepositItem(Properties properties) {
            super(properties);
        }

        @Override
        public boolean hasEffect(@Nonnull ItemStack stack) {
            return true;
        }


        @Override
        public void customCapability(ItemStack itemStack, CapabilityProvider capabilityProvider) {
            capabilityProvider.addCapability(iItemBiome_capabilityRegister.getCapability(), new IItemBiome.ItemBiome());
        }

        @Override
        public void addInformation(@Nonnull ItemStack stack, @Nullable World worldIn, @Nonnull List<ITextComponent> tooltip, @Nonnull ITooltipFlag flagIn) {
            super.addInformation(stack, worldIn, tooltip, flagIn);
            stack.getCapability(iItemBiome_capabilityRegister.getCapability()).ifPresent(pack -> {
                Biome biome = pack.getBiome();
                tooltip.add(new TranslationTextComponent("Biome:%s",
                        biome == null || biome.getRegistryName() == null ? "null" : new TranslationTextComponent(StringUtil.formatLang(
                                "biome",
                                biome.getRegistryName().getNamespace(),
                                biome.getRegistryName().getPath()
                        ))));
            });
        }

        @Override
        public void fillItemGroup(@Nonnull ItemGroup group, @Nonnull NonNullList<ItemStack> items) {
            if (!this.isInGroup(group)) {
                return;
            }
            items.add(biome_depositItemRegister.mackItemStack(null));
            ForgeRegistries.BIOMES.getValues()
                    .stream()
                    .sorted(Comparator.comparing(a -> a.getRegistryName().toString()))
                    .forEach(biome -> items.add(biome_depositItemRegister.mackItemStack(biome)));
        }
    }

    @VoluntarilyRegister
    public static class Biome_DepositItemSpecialRecipeRegister extends SpecialRecipeSerializerRegister.SpecialRecipeRegister {

        @VoluntarilyAssignment
        protected Biome_DepositItemRegister biome_depositItemRegister;

        @VoluntarilyAssignment
        protected SoulItemRegister.SoulCubeItemRegister soulCubeItemRegister;

        @Override
        protected SpecialRecipeSerializerRegister.SpecialRecipePack defaultSpecialRecipePackDelayed() {
            return new SpecialRecipeSerializerRegister.SpecialRecipePack(
                    ListUtil.of(
                            "AAA",
                            "BVC",
                            "AAA"
                    ),
                    MapUtil.of(
                            "A", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(SBItems.proudsoul)),
                            "B", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(Items.COMPASS)),
                            "C", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(Items.CLOCK)),
                            "V", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(soulCubeItemRegister.getItem()))
                    ),
                    new IResultPack.OfItemStack(biome_depositItemRegister.mackItemStack(null))
            );
        }
    }
}
