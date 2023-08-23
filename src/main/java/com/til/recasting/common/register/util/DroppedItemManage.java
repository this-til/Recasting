package com.til.recasting.common.register.util;

import com.til.glowing_fire_glow.common.main.IWorldComponent;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.recasting.common.data.SlashBladePack;
import com.til.recasting.common.data.UseSlashBladeEntityPack;
import com.til.recasting.common.entity.LightningEntity;
import com.til.recasting.common.register.overall_config.DroppedItemOverallConfigRegister;
import com.til.recasting.common.register.world.item.EnchantmentDepositItemRegister;
import com.til.recasting.common.register.world.item.EntityDepositItemRegister;
import mods.flammpfeil.slashblade.init.SBItems;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class DroppedItemManage implements IWorldComponent {

    @VoluntarilyAssignment
    protected DroppedItemOverallConfigRegister droppedItemOverallConfigRegister;

    @VoluntarilyAssignment
    protected EnchantmentDepositItemRegister enchantmentDepositItemRegister;

    @VoluntarilyAssignment
    protected EntityDepositItemRegister entityDepositItemRegister;

    @Nullable
    protected List<Enchantment> enchantmentList = null;


    @SubscribeEvent
    protected void onLivingDeathEvent(LivingDeathEvent event) {
        if (event.getEntity().world.isRemote) {
            return;
        }
        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }
        Entity trueSource = event.getSource().getTrueSource();
        if (trueSource == null) {
            return;
        }
        if (!(trueSource instanceof LivingEntity)) {
            return;
        }
        UseSlashBladeEntityPack useSlashBladeEntityPack = new UseSlashBladeEntityPack(((LivingEntity) trueSource));
        if (!useSlashBladeEntityPack.isEffective(SlashBladePack.EffectiveType.canUse)) {
            return;
        }
        Random random = ((LivingEntity) trueSource).getRNG();
        double x = trueSource.getPosX();
        double y = trueSource.getPosY();
        double z = trueSource.getPosZ();

        if (random.nextDouble() < droppedItemOverallConfigRegister.getSoulDropChance()) {
            InventoryHelper.spawnItemStack(event.getEntity().world, x, y, z, new ItemStack(SBItems.proudsoul));
        }
        if (random.nextDouble() < droppedItemOverallConfigRegister.getTinySoulDropChance()) {
            InventoryHelper.spawnItemStack(event.getEntity().world, x, y, z, new ItemStack(SBItems.proudsoul_tiny));
        }
        if (random.nextDouble() < droppedItemOverallConfigRegister.getEnchantmentSoulDropChance()) {
            if (enchantmentList == null) {
                enchantmentList = new ArrayList<>(ForgeRegistries.ENCHANTMENTS.getValues());
            }
            Enchantment enchantment = enchantmentList.get(random.nextInt(enchantmentList.size()));
            InventoryHelper.spawnItemStack(event.getEntity().world, x, y, z, enchantmentDepositItemRegister.mackItemStack(enchantment, droppedItemOverallConfigRegister.getEnchantmentSoulSuccessRate()));
        }
        if (random.nextDouble() < droppedItemOverallConfigRegister.getEntityDropChance()) {
            InventoryHelper.spawnItemStack(event.getEntity().world, x, y, z, entityDepositItemRegister.mackItemStack(event.getEntity().getType()));
        }
    }
}
