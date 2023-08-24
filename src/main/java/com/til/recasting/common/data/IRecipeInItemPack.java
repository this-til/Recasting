package com.til.recasting.common.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.til.glowing_fire_glow.common.register.StaticVoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.util.gson.AcceptTypeJson;
import com.til.glowing_fire_glow.common.util.gson.type_adapter.factory.ForgeRegistryItemTypeAdapterFactory;
import com.til.recasting.common.capability.IItemEnchantment;
import com.til.recasting.common.capability.IItemSA;
import com.til.recasting.common.capability.IItemSE;
import com.til.recasting.common.register.capability.*;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import com.til.recasting.common.register.slash_blade.sa.SA_Register;
import com.til.recasting.common.register.slash_blade.se.SE_Register;
import com.til.recasting.common.register.world.item.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

@AcceptTypeJson
@StaticVoluntarilyAssignment
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


    class OfIngredient implements IRecipeInItemPack {
        protected Ingredient ingredient;


        public OfIngredient(Ingredient ingredient) {
            this.ingredient = ingredient;
        }

        @Override
        public Ingredient toIngredient() {
            return ingredient;
        }

        @Override
        public boolean test(ItemStack itemStack) {
            return ingredient.test(itemStack);
        }
    }

    class OfTag implements IRecipeInItemPack {
        @Expose
        protected OfIngredient ofIngredient;

        protected ResourceLocation tagName;

        public OfTag(ResourceLocation tagName) {
            this.tagName = tagName;
        }

        public OfIngredient getOfIngredient() {
            if (ofIngredient == null) {
                ofIngredient = new OfIngredient(Ingredient.fromTag(ItemTags.makeWrapperTag(tagName.toString())));
            }
            return ofIngredient;
        }

        @Override
        public Ingredient toIngredient() {
            return getOfIngredient().toIngredient();
        }

        @Override
        public boolean test(ItemStack itemStack) {
            return getOfIngredient().test(itemStack);
        }
    }

    @StaticVoluntarilyAssignment
    class OfItemSE implements IRecipeInItemPack {

        @VoluntarilyAssignment
        protected static ItemSE_CapabilityRegister itemSE_capabilityRegister;

        @VoluntarilyAssignment
        protected static SE_DepositItemRegister se_depositItemRegister;

        protected SE_Register se_register;
        protected float successRate;


        public OfItemSE(SE_Register se_register, float successRate) {
            this.se_register = se_register;
            this.successRate = successRate;
        }

        public OfItemSE(SE_Register se_register) {
            this(se_register, 0);
        }

        @Override
        public Ingredient toIngredient() {
            return new ItemStackIngredient(se_depositItemRegister.mackItemStack(se_register, successRate));
        }

        @Override
        public boolean test(ItemStack itemStack) {

            Optional<IItemSE> itemSEOptional = itemStack.getCapability(itemSE_capabilityRegister.getCapability()).resolve();
            if (!itemSEOptional.isPresent()) {
                return false;
            }
            IItemSE iItemSE = itemSEOptional.get();
            if (!iItemSE.getSE().equals(se_register)) {
                return false;
            }
            if (iItemSE.getBasicsSuccessRate() - successRate > 0.01) {
                return false;
            }
            return true;
        }
    }

    @StaticVoluntarilyAssignment
    class OfItemEnchantment implements IRecipeInItemPack {

        @VoluntarilyAssignment
        protected static EnchantmentDepositItemRegister enchantment_depositItemRegister;

        @VoluntarilyAssignment
        protected static ItemEnchantmentCapabilityRegister itemEnchantmentCapabilityRegister;

        protected Enchantment enchantment;

        protected float successRate;

        @Override
        public Ingredient toIngredient() {
            return Ingredient.fromStacks(enchantment_depositItemRegister.mackItemStack(enchantment, successRate));
        }

        @Override
        public boolean test(ItemStack itemStack) {
            Optional<IItemEnchantment> itemSEOptional = itemStack.getCapability(itemEnchantmentCapabilityRegister.getCapability()).resolve();
            if (!itemSEOptional.isPresent()) {
                return false;
            }
            Enchantment enchantment = itemSEOptional.get().getEnchantment();
            if (!enchantment.equals(this.enchantment)) {
                return false;
            }
            if (itemSEOptional.get().getBasicsSuccessRate() - successRate > 0.01) {
                return false;
            }
            return true;
        }
    }

    @StaticVoluntarilyAssignment
    class OfItemSA implements IRecipeInItemPack {

        @VoluntarilyAssignment
        protected static SA_DepositItemRegister sa_depositItemRegister;

        @VoluntarilyAssignment
        protected static ItemSA_CapabilityRegister itemSA_capabilityRegister;

        protected SA_Register sa_register;


        public OfItemSA(SA_Register sa_register) {
            this.sa_register = sa_register;
        }

        @Override
        public Ingredient toIngredient() {
            return new ItemStackIngredient(sa_depositItemRegister.mackItemStack(sa_register));
        }

        @Override
        public boolean test(ItemStack itemStack) {
            Optional<IItemSA> itemSAOptional = itemStack.getCapability(itemSA_capabilityRegister.getCapability()).resolve();
            return itemSAOptional.map(iItemSA -> iItemSA.getSA().equals(sa_register)).orElse(false);
        }
    }

    @StaticVoluntarilyAssignment
    class OfEntity implements IRecipeInItemPack {
        @VoluntarilyAssignment
        protected static EntityDepositItemRegister entity_depositItemRegister;

        @VoluntarilyAssignment
        protected static IItemEntityCapabilityRegister entity_capabilityRegister;

        @JsonAdapter(ForgeRegistryItemTypeAdapterFactory.class)
        protected EntityType<?> entityType;


        public OfEntity(EntityType<?> entityType) {
            this.entityType = entityType;
        }

        @Override
        public Ingredient toIngredient() {
            return new ItemStackIngredient(entity_depositItemRegister.mackItemStack(entityType));
        }

        @Override
        public boolean test(ItemStack itemStack) {
            return itemStack.getCapability(entity_capabilityRegister.getCapability()).resolve().map(entityPack -> entityPack.getEntityType().equals(entityType)).orElse(false);
        }
    }

    @StaticVoluntarilyAssignment
    class OfBiome implements IRecipeInItemPack {


        @VoluntarilyAssignment
        protected static BiomeDepositItemRegister biome_depositItemRegister;

        @VoluntarilyAssignment
        protected static IItemBiomeCapabilityRegister iItemBiome_capabilityRegister;

        protected Biome biome;

        public OfBiome(Biome biome) {
            this.biome = biome;
        }

        @Override
        public Ingredient toIngredient() {
            return new ItemStackIngredient(biome_depositItemRegister.mackItemStack(biome));
        }

        @Override
        public boolean test(ItemStack itemStack) {
            return itemStack.getCapability(iItemBiome_capabilityRegister.getCapability()).resolve()
                    .map(iItemBiome -> biome == null || Objects.equals(iItemBiome.getBiome(), biome)).orElse(false);
        }
    }


    class OfSlashBlade implements IRecipeInItemPack {

        protected ItemStack itemStack;

        @Expose
        protected SlashBladePack slashBladePack;


        public OfSlashBlade(SlashBladePack slashBladePack) {
            this(slashBladePack.getItemStack());
        }

        public OfSlashBlade(ItemStack itemStack) {
            this.itemStack = itemStack;
        }

        @Override
        public boolean test(ItemStack itemStack) {
            SlashBladePack inSlashBladePack = new SlashBladePack(itemStack);
            if (!inSlashBladePack.isEffective(SlashBladePack.EffectiveType.isSlashBlade)) {
                return false;
            }
            return getSlashBladePack().isExtends(inSlashBladePack);
        }

        public SlashBladePack getSlashBladePack() {
            if (slashBladePack == null) {
                slashBladePack = new SlashBladePack(itemStack);
            }
            if (!slashBladePack.isEffective(SlashBladePack.EffectiveType.isSlashBlade)) {
                throw new RuntimeException("错误的拔刀剑物品");
            }
            return slashBladePack;
        }

        @Override
        public Ingredient toIngredient() {
            return new ItemStackIngredient(itemStack);
        }
    }

    class OfSlashBladeRegister implements IRecipeInItemPack {

        protected SlashBladeRegister slashBladeRegister;

        @Expose
        @Nullable
        protected OfSlashBlade ofSlashBlade;

        public OfSlashBladeRegister(SlashBladeRegister slashBladePack) {
            this.slashBladeRegister = slashBladePack;
        }

        @Override
        public Ingredient toIngredient() {
            if (ofSlashBlade == null) {
                ofSlashBlade = new OfSlashBlade(slashBladeRegister.getSlashBladePack().getItemStack());
            }
            return ofSlashBlade.toIngredient();
        }

        @Override
        public boolean test(ItemStack itemStack) {
            if (ofSlashBlade == null) {
                ofSlashBlade = new OfSlashBlade(slashBladeRegister.getSlashBladePack().getItemStack());
            }
            return ofSlashBlade.test(itemStack);
        }
    }
}
