package com.til.recasting.common.mixin;

import com.til.recasting.common.entity.StrengthenBladeStandEntity;
import mods.flammpfeil.slashblade.entity.BladeStandEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

/**
 * @author til
 */
@Mixin(value = {BladeStandEntity.class}, remap = false)
public class BladeStandEntityMixin {

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static BladeStandEntity createInstanceFromPos(World worldIn, BlockPos placePos, Direction dir, Item type){
       return StrengthenBladeStandEntity.createInstanceFromPos(worldIn, placePos, dir, type);
    }

}
