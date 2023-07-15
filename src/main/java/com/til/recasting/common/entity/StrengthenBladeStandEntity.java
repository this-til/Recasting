package com.til.recasting.common.entity;

import com.til.glowing_fire_glow.common.register.StaticVoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.recasting.common.capability.IItemSA;
import com.til.recasting.common.capability.IItemSE;
import com.til.recasting.common.capability.ISE;
import com.til.recasting.common.capability.SlashBladePack;
import com.til.recasting.common.register.capability.ItemSA_CapabilityRegister;
import com.til.recasting.common.register.capability.ItemSE_CapabilityRegister;
import com.til.recasting.common.register.entity_type.StrengthenBladeStandEntityTypeRegister;
import com.til.recasting.common.register.slash_blade.sa.AllSARegister;
import com.til.recasting.common.register.slash_blade.sa.SA_Register;
import com.til.recasting.common.register.slash_blade.se.SE_Register;
import mods.flammpfeil.slashblade.entity.BladeStandEntity;
import mods.flammpfeil.slashblade.init.SBItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Objects;

/**
 * @author til
 */
@StaticVoluntarilyAssignment
public class StrengthenBladeStandEntity extends BladeStandEntity {

    @VoluntarilyAssignment
    protected static AllSARegister allSARegister;

    @VoluntarilyAssignment
    protected static ItemSA_CapabilityRegister itemSA_capabilityRegister;

    @VoluntarilyAssignment
    protected static ItemSE_CapabilityRegister itemSE_capabilityRegister;

    @VoluntarilyAssignment
    protected static StrengthenBladeStandEntityTypeRegister strengthenBladeStandEntityTypeRegister;

    protected static final DataParameter<Direction> DIRECTION = EntityDataManager.createKey(StrengthenBladeStandEntity.class, DataSerializers.DIRECTION);

    public StrengthenBladeStandEntity(EntityType<? extends BladeStandEntity> entityType, World world) {
        super(entityType, world);
        setInvulnerable(true);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(DIRECTION, Direction.NORTH);
    }

    @Override
    public ActionResultType processInitialInteract(PlayerEntity player, Hand hand) {
        if (world.isRemote || hand != Hand.MAIN_HAND) {
            return ActionResultType.PASS;
        }
        ItemStack itemstack = player.getHeldItem(hand);
        if (itemstack.isEmpty() || itemstack.getItem() == SBItems.slashblade) {
            player.setHeldItem(Hand.MAIN_HAND, getDisplayedItem());
            setDisplayedItem(itemstack);
            itemstack = player.getHeldItem(hand);
        }
        if (player.isSneaking() && getDisplayedItem().isEmpty() && itemstack.isEmpty()) {
            this.remove();
            player.addItemStackToInventory(new ItemStack(currentType));
            return ActionResultType.SUCCESS;
        }

        IItemSA iItemSA = itemstack.getCapability(itemSA_capabilityRegister.getCapability()).orElse(null);
        IItemSE iItemSE = itemstack.getCapability(itemSE_capabilityRegister.getCapability()).orElse(null);

        if (iItemSA == null && iItemSE == null) {
            return ActionResultType.PASS;
        }

        SlashBladePack slashBladePack = new SlashBladePack(getDisplayedItem());
        if (!slashBladePack.isEffective(SlashBladePack.EffectiveType.isSlashBlade)) {
            return ActionResultType.PASS;
        }


        if (iItemSA != null) {
            SA_Register sa_register = allSARegister.getSA_Register(slashBladePack.slashBladeState.getSlashArts());
            if (Objects.equals(sa_register, iItemSA.getSA())) {
                return ActionResultType.SUCCESS;
            }
            iItemSA.tryReplace(slashBladePack);
            setDisplayedItem(slashBladePack.itemStack);
            itemstack.shrink(1);
            player.setHeldItem(Hand.MAIN_HAND, itemstack);
            playSound(SoundEvents.BLOCK_GLASS_BREAK, 1f, 1f);
            ((ServerWorld) world).spawnParticle(ParticleTypes.CRIT, this.getPosX(), this.getPosY(), this.getPosZ(), 16, 0.5, 0.5, 0.5, 0.25f);
            return ActionResultType.SUCCESS;
        }

        if (iItemSE != null) {
            SE_Register se_register = iItemSE.getSE();
            ISE.SE_Pack se_pack = slashBladePack.ise.getPack(se_register);
            if (se_pack.getLevel() >= se_register.getMaxLevel()) {
                return ActionResultType.SUCCESS;
            }
            if (iItemSE.tryUp(slashBladePack)) {
                setDisplayedItem(slashBladePack.itemStack);
                playSound(SoundEvents.BLOCK_GLASS_BREAK, 1f, 1f);
                ((ServerWorld) world).spawnParticle(ParticleTypes.CRIT, this.getPosX(), this.getPosY(), this.getPosZ(), 16, 0.5, 0.5, 0.5, 0.25f);
            } else {
                playSound(SoundEvents.BLOCK_GLASS_BREAK, 1f, 1f);
                ((ServerWorld) world).spawnParticle(ParticleTypes.CLOUD, this.getPosX(), this.getPosY(), this.getPosZ(), 16, 0.5, 0.5, 0.5, 0.25f);
            }
            itemstack.shrink(1);
            player.setHeldItem(Hand.MAIN_HAND, itemstack);
            return ActionResultType.SUCCESS;
        }

        return ActionResultType.PASS;
    }

    public void setLock(Direction direction) {


        switch (direction) {

            case DOWN:
            case UP:
                return;
            case NORTH:
                setItemRotation(0);
                break;
            case SOUTH:
                setItemRotation(180);
                break;
            case WEST:
                setItemRotation(90);
                break;
            case EAST:
                setItemRotation(-90);
                break;
        }

        this.dataManager.set(DIRECTION, direction);
    }

    public static BladeStandEntity createInstanceFromPos(World worldIn, BlockPos placePos, Direction dir, Item type) {
        StrengthenBladeStandEntity e = new StrengthenBladeStandEntity(strengthenBladeStandEntityTypeRegister.getEntityType(), worldIn);
        e.hangingPosition = placePos;
        e.updateFacingWithBoundingBox(dir);
        e.currentType = type;
        return e;
    }

}
