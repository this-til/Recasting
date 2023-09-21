package com.til.recasting.common.entity;

import com.til.glowing_fire_glow.common.register.StaticVoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.recasting.common.capability.IItemEnchantment;
import com.til.recasting.common.capability.IItemSA;
import com.til.recasting.common.capability.IItemSE;
import com.til.recasting.common.capability.ISE;
import com.til.recasting.common.data.SlashBladePack;
import com.til.recasting.common.register.capability.ItemEnchantmentCapabilityRegister;
import com.til.recasting.common.register.capability.ItemSA_CapabilityRegister;
import com.til.recasting.common.register.capability.ItemSE_CapabilityRegister;
import com.til.recasting.common.register.entity_type.StrengthenBladeStandEntityTypeRegister;
import com.til.recasting.common.register.overall_config.Copy_SA_OverallConfigRegister;
import com.til.recasting.common.register.slash_blade.sa.AllSA_Register;
import com.til.recasting.common.register.slash_blade.sa.SA_Register;
import com.til.recasting.common.register.slash_blade.se.SE_Register;
import com.til.recasting.common.register.world.item.SA_DepositItemRegister;
import com.til.recasting.common.register.world.item.SoulItemRegister;
import mods.flammpfeil.slashblade.entity.BladeStandEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * @author til
 */
@StaticVoluntarilyAssignment
public class StrengthenBladeStandEntity extends BladeStandEntity {

    @VoluntarilyAssignment
    protected static AllSA_Register allSARegister;

    @VoluntarilyAssignment
    protected static ItemSA_CapabilityRegister itemSA_capabilityRegister;

    @VoluntarilyAssignment
    protected static ItemSE_CapabilityRegister itemSE_capabilityRegister;

    @VoluntarilyAssignment
    protected static ItemEnchantmentCapabilityRegister itemEnchantmentCapabilityRegister;

    @VoluntarilyAssignment
    protected static StrengthenBladeStandEntityTypeRegister strengthenBladeStandEntityTypeRegister;

    @VoluntarilyAssignment
    protected static SA_DepositItemRegister sa_depositItemRegister;

    @VoluntarilyAssignment
    protected static SoulItemRegister.SoulCubeChangeItemRegister soulCubeChangeItemRegister;

    @VoluntarilyAssignment
    protected static Copy_SA_OverallConfigRegister copy_sa_overallConfigRegister;

    protected final static Random RANDOM = new Random();


    public StrengthenBladeStandEntity(EntityType<? extends BladeStandEntity> entityType, World world) {
        super(entityType, world);
        setInvulnerable(true);
    }

    @Override
    protected void registerData() {
        super.registerData();
    }

    public enum InteractiveType {
        ROTATE, SUPERSEDE, REMOVE, UP, COPY_SA
    }

    @Override
    public ActionResultType processInitialInteract(PlayerEntity player, Hand hand) {
        if (world.isRemote || hand != Hand.MAIN_HAND) {
            return ActionResultType.PASS;
        }

        ItemStack playerItemStack = player.getHeldItem(hand);
        ItemStack displayedItem = getDisplayedItem();
        SlashBladePack slashBladePack = new SlashBladePack(displayedItem);
        SlashBladePack playerSlashBladePack = new SlashBladePack(playerItemStack);

        IItemSA iItemSA = null;
        IItemSE iItemSE = null;
        IItemEnchantment iItemEnchantment = null;
        InteractiveType interactiveType;
        if (playerSlashBladePack.isEffective(SlashBladePack.EffectiveType.isSlashBlade) && slashBladePack.isEffective(SlashBladePack.EffectiveType.isSlashBlade)) {
            interactiveType = InteractiveType.SUPERSEDE;
        } else if (playerSlashBladePack.isEffective(SlashBladePack.EffectiveType.isSlashBlade) && displayedItem.isEmpty()) {
            interactiveType = InteractiveType.SUPERSEDE;
        } else if (slashBladePack.isEffective(SlashBladePack.EffectiveType.isSlashBlade) && playerItemStack.isEmpty()) {
            interactiveType = InteractiveType.SUPERSEDE;
        } else if (player.isSneaking() && playerItemStack.isEmpty() && displayedItem.isEmpty()) {
            interactiveType = InteractiveType.REMOVE;
        } else if (slashBladePack.isEffective(SlashBladePack.EffectiveType.isSlashBlade) && playerItemStack.getItem().equals(soulCubeChangeItemRegister.getItem())) {
            interactiveType = InteractiveType.COPY_SA;
        } else {
            iItemSA = playerItemStack.getCapability(itemSA_capabilityRegister.getCapability()).orElse(null);
            iItemSE = playerItemStack.getCapability(itemSE_capabilityRegister.getCapability()).orElse(null);
            iItemEnchantment = playerItemStack.getCapability(itemEnchantmentCapabilityRegister.getCapability()).orElse(null);
            interactiveType = (iItemSA != null || iItemSE != null || iItemEnchantment != null) && slashBladePack.isEffective(SlashBladePack.EffectiveType.isSlashBlade)
                    ? InteractiveType.UP : InteractiveType.ROTATE;
        }

        switch (interactiveType) {
            case ROTATE:
                this.playSound(SoundEvents.ENTITY_ITEM_FRAME_ROTATE_ITEM, 1.0F, 1.0F);
                this.setItemRotation(this.getRotation() + 1);
                return ActionResultType.PASS;
            case SUPERSEDE:
                player.setHeldItem(Hand.MAIN_HAND, getDisplayedItem());
                setDisplayedItem(playerItemStack);
                return ActionResultType.SUCCESS;
            case REMOVE:
                this.remove();
                player.addItemStackToInventory(new ItemStack(currentType));
                return ActionResultType.SUCCESS;
            case COPY_SA:
                if (slashBladePack.getSlashBladeState().getRefine() < copy_sa_overallConfigRegister.getMinRefine()) {
                    return ActionResultType.SUCCESS;
                }
                if (slashBladePack.getSlashBladeState().getKillCount() < copy_sa_overallConfigRegister.getMinKill()) {
                    return ActionResultType.SUCCESS;
                }
                if (player.getRNG().nextDouble() < copy_sa_overallConfigRegister.getGetSuccessRate()) {
                    InventoryHelper.spawnItemStack(world, getPosX(), getPosY(), getPosZ(), sa_depositItemRegister.mackItemStack(slashBladePack.getSA()));
                    playSound(SoundEvents.BLOCK_GLASS_BREAK, 1f, 1f);
                    ((ServerWorld) world).spawnParticle(ParticleTypes.CRIT, this.getPosX(), this.getPosY(), this.getPosZ(), 16, 0.5, 0.5, 0.5, 0.25f);

                } else {
                    playSound(SoundEvents.BLOCK_GLASS_BREAK, 1f, 1f);
                    ((ServerWorld) world).spawnParticle(ParticleTypes.CLOUD, this.getPosX(), this.getPosY(), this.getPosZ(), 16, 0.5, 0.5, 0.5, 0.25f);
                }
                slashBladePack.getSlashBladeState().setRefine((int) (slashBladePack.getSlashBladeState().getRefine() * (1 - copy_sa_overallConfigRegister.getLossRefine())));
                playerItemStack.shrink(1);
                player.setHeldItem(Hand.MAIN_HAND, playerItemStack);
                return ActionResultType.SUCCESS;
            case UP:
                if (iItemSA != null) {
                    SA_Register sa_register = allSARegister.getSA_Register(slashBladePack.getSlashBladeState().getSlashArts());
                    if (Objects.equals(sa_register, iItemSA.getSA())) {
                        return ActionResultType.SUCCESS;
                    }
                    iItemSA.tryReplace(slashBladePack);
                    setDisplayedItem(slashBladePack.getItemStack());
                    playSound(SoundEvents.BLOCK_GLASS_BREAK, 1f, 1f);
                    ((ServerWorld) world).spawnParticle(ParticleTypes.CRIT, this.getPosX(), this.getPosY(), this.getPosZ(), 16, 0.5, 0.5, 0.5, 0.25f);
                }

                if (iItemSE != null) {
                    SE_Register se_register = iItemSE.getSE();
                    ISE.SE_Pack se_pack = slashBladePack.getIse().getPack(se_register);
                    if (se_pack.getLevel() >= se_register.getMaxLevel()) {
                        return ActionResultType.SUCCESS;
                    }
                    float successRate = iItemSE.getBasicsSuccessRate();
                    int allLevel = 1;
                    for (ISE.SE_Pack value : slashBladePack.getIse().getAllSE().values()) {
                        allLevel += value.getLevel();
                    }
                    successRate = successRate / (allLevel * 0.1f + 1);
                    if (RANDOM.nextDouble() < successRate) {
                        se_pack.setLevel(se_pack.getLevel() + 1);
                        setDisplayedItem(slashBladePack.getItemStack());
                        playSound(SoundEvents.BLOCK_GLASS_BREAK, 1f, 1f);
                        ((ServerWorld) world).spawnParticle(ParticleTypes.CRIT, this.getPosX(), this.getPosY(), this.getPosZ(), 16, 0.5, 0.5, 0.5, 0.25f);
                    } else {
                        playSound(SoundEvents.BLOCK_GLASS_BREAK, 1f, 1f);
                        ((ServerWorld) world).spawnParticle(ParticleTypes.CLOUD, this.getPosX(), this.getPosY(), this.getPosZ(), 16, 0.5, 0.5, 0.5, 0.25f);
                    }
                }

                if (iItemEnchantment != null) {
                    Enchantment enchantment = iItemEnchantment.getEnchantment();
                    Map<Enchantment, Integer> enchantmentIntegerMap = EnchantmentHelper.getEnchantments(slashBladePack.getItemStack());
                    if (enchantmentIntegerMap.containsKey(enchantment) && enchantmentIntegerMap.get(enchantment) >= enchantment.getMaxLevel()) {
                        return ActionResultType.SUCCESS;
                    }
                    boolean isSuccess = RANDOM.nextDouble() < iItemEnchantment.getBasicsSuccessRate();
                    if (isSuccess) {
                        enchantmentIntegerMap.put(enchantment, enchantmentIntegerMap.getOrDefault(enchantment, 0) + 1);
                        EnchantmentHelper.setEnchantments(enchantmentIntegerMap, slashBladePack.getItemStack());
                        setDisplayedItem(slashBladePack.getItemStack());
                        playSound(SoundEvents.BLOCK_GLASS_BREAK, 1f, 1f);
                        ((ServerWorld) world).spawnParticle(ParticleTypes.CRIT, this.getPosX(), this.getPosY(), this.getPosZ(), 16, 0.5, 0.5, 0.5, 0.25f);
                    } else {
                        playSound(SoundEvents.BLOCK_GLASS_BREAK, 1f, 1f);
                        ((ServerWorld) world).spawnParticle(ParticleTypes.CLOUD, this.getPosX(), this.getPosY(), this.getPosZ(), 16, 0.5, 0.5, 0.5, 0.25f);
                    }
                }
                playerItemStack.shrink(1);
                player.setHeldItem(Hand.MAIN_HAND, playerItemStack);
                return ActionResultType.SUCCESS;
            default:
                return ActionResultType.PASS;
        }
    }

    public static BladeStandEntity createInstanceFromPos(World worldIn, BlockPos placePos, Direction dir, Item type) {
        StrengthenBladeStandEntity e = new StrengthenBladeStandEntity(strengthenBladeStandEntityTypeRegister.getEntityType(), worldIn);
        e.hangingPosition = placePos;
        e.updateFacingWithBoundingBox(dir);
        e.currentType = type;
        return e;
    }

}
