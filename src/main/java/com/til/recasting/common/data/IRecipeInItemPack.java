package com.til.recasting.common.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.til.glowing_fire_glow.common.register.StaticVoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.util.gson.AcceptTypeJson;
import com.til.glowing_fire_glow.common.util.gson.type_adapter.factory.ForgeRegistryItemTypeAdapterFactory;
import com.til.recasting.common.capability.IItemSA;
import com.til.recasting.common.capability.IItemSE;
import com.til.recasting.common.capability.SlashBladePack;
import com.til.recasting.common.register.capability.IItemEntity_CapabilityRegister;
import com.til.recasting.common.register.capability.ItemSA_CapabilityRegister;
import com.til.recasting.common.register.capability.ItemSE_CapabilityRegister;
import com.til.recasting.common.register.sa.SA_Register;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import com.til.recasting.common.register.slash_blade.se.SE_Register;
import com.til.recasting.common.register.world.item.Entity_DepositItemRegister;
import com.til.recasting.common.register.world.item.SA_DepositItemRegister;
import com.til.recasting.common.register.world.item.SE_DepositItemRegister;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nullable;
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

    @StaticVoluntarilyAssignment
    class OfItemSE implements IRecipeInItemPack {

        @VoluntarilyAssignment
        protected static ItemSE_CapabilityRegister itemSE_capabilityRegister;

        @VoluntarilyAssignment
        protected static SE_DepositItemRegister se_depositItemRegister;

        protected SE_Register se_register;
        protected float successRate;

        protected boolean protect;


        public OfItemSE() {
        }

        public OfItemSE(SE_Register se_register, float successRate, boolean protect) {
            this.se_register = se_register;
            this.successRate = successRate;
            this.protect = protect;
        }

        @Override
        public Ingredient toIngredient() {
            return new ItemStackIngredient(se_depositItemRegister.mackItemStack(se_register, successRate, protect));
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
            if (protect && !iItemSE.isProtect()) {
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
        protected static Entity_DepositItemRegister entity_depositItemRegister;

        @VoluntarilyAssignment
        protected static IItemEntity_CapabilityRegister entity_capabilityRegister;

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


    class OfSlashBlade implements IRecipeInItemPack {

        protected ItemStack itemStack;

        @Expose
        protected SlashBladePack slashBladePack;


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
                ofSlashBlade = new OfSlashBlade(slashBladeRegister.getSlashBladePack().itemStack);
            }
            return ofSlashBlade.toIngredient();
        }

        @Override
        public boolean test(ItemStack itemStack) {
            if (ofSlashBlade == null) {
                ofSlashBlade = new OfSlashBlade(slashBladeRegister.getSlashBladePack().itemStack);
            }
            return ofSlashBlade.test(itemStack);
        }
    }
}
