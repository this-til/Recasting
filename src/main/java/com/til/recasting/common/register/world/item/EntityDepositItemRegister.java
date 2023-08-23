package com.til.recasting.common.register.world.item;

import com.til.glowing_fire_glow.common.capability.CapabilityProvider;
import com.til.glowing_fire_glow.common.register.StaticVoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.world.item.ItemRegister;
import com.til.glowing_fire_glow.common.util.SetUtil;
import com.til.recasting.common.capability.CapabilityEvent;
import com.til.recasting.common.capability.IItemEntity;
import com.til.recasting.common.register.capability.IItemEntityCapabilityRegister;
import mods.flammpfeil.slashblade.SlashBlade;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@VoluntarilyRegister
@StaticVoluntarilyAssignment
public class EntityDepositItemRegister extends ItemRegister {

    @VoluntarilyAssignment
    protected static IItemEntityCapabilityRegister iItemEntity_capabilityRegister;

    @Override
    protected Item initItem() {
        return new Entity_DepositItem(new Item.Properties().group(SlashBlade.SLASHBLADE));
    }

    public ItemStack mackItemStack(EntityType<?> entityType) {
        ItemStack itemStack = new ItemStack(getItem());
        itemStack.getCapability(iItemEntity_capabilityRegister.getCapability()).ifPresent(pack -> pack.setEntityType(entityType));
        return itemStack;
    }

    @StaticVoluntarilyAssignment
    public static class Entity_DepositItem extends Item implements CapabilityEvent.ICustomCapability {

        @VoluntarilyAssignment
        protected static EntityDepositItemRegister entity_depositItemRegister;

        public Entity_DepositItem(Properties properties) {
            super(properties);
        }

        @Override
        public boolean hasEffect(ItemStack stack) {
            return true;
        }

        @Override
        public void customCapability(ItemStack itemStack, CapabilityProvider capabilityProvider) {
            capabilityProvider.addCapability(iItemEntity_capabilityRegister.getCapability(), new IItemEntity.ItemEntity());
        }

        @Override
        public void addInformation(@Nonnull ItemStack stack, @Nullable World worldIn, @Nonnull List<ITextComponent> tooltip, @Nonnull ITooltipFlag flagIn) {
            super.addInformation(stack, worldIn, tooltip, flagIn);
            stack.getCapability(iItemEntity_capabilityRegister.getCapability()).ifPresent(pack -> {
                EntityType<?> entityType = pack.getEntityType();
                tooltip.add(new TranslationTextComponent("Entity:%s",
                        entityType == null ? "null" : new TranslationTextComponent(entityType.getTranslationKey())));
            });
        }

        @Override
        public void fillItemGroup(@Nonnull ItemGroup group, @Nonnull NonNullList<ItemStack> items) {
            if (!this.isInGroup(group)) {
                return;
            }
            Set<EntityClassification> entityClassifications = SetUtil.of(
                    EntityClassification.MONSTER,
                    EntityClassification.CREATURE,
                    EntityClassification.WATER_CREATURE
            );

            ForgeRegistries.ENTITIES.getValues()
                    .stream()
                    .filter(entityType -> entityClassifications.contains(entityType.getClassification()))
                    .sorted(Comparator.comparing(a -> a.getName().toString()))
                    .forEach(sa_register -> items.add(entity_depositItemRegister.mackItemStack(sa_register)));

        }
    }
}
