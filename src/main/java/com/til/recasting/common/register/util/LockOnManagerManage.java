package com.til.recasting.common.register.util;

import com.til.glowing_fire_glow.common.main.IWorldComponent;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.recasting.common.data.UseSlashBladeEntityPack;
import com.til.recasting.common.register.target_selector.DefaultTargetSelectorRegister;
import mods.flammpfeil.slashblade.event.InputCommandEvent;
import mods.flammpfeil.slashblade.util.InputCommand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Comparator;
import java.util.List;

/**
 * @author til
 */
public class LockOnManagerManage implements IWorldComponent {

    @VoluntarilyAssignment
    protected DefaultTargetSelectorRegister defaultTargetSelectorRegister;

    @SubscribeEvent
    protected void onInputChange(InputCommandEvent event) {


        if (event.getOld().contains(InputCommand.SNEAK) == event.getCurrent().contains(InputCommand.SNEAK)) {
            return;
        }

        ServerPlayerEntity player = event.getPlayer();

        UseSlashBladeEntityPack useSlashBladeEntityPack = new UseSlashBladeEntityPack(player);


        if (event.getOld().contains(InputCommand.SNEAK) && !event.getCurrent().contains(InputCommand.SNEAK)) {
            useSlashBladeEntityPack.slashBladePack.slashBladeState.setTargetEntityId(null);
            return;
        }

        RayTraceResult rayTraceResult = defaultTargetSelectorRegister.selector(player);
        if (rayTraceResult.getType() == RayTraceResult.Type.ENTITY) {
            useSlashBladeEntityPack.slashBladePack.slashBladeState.setTargetEntityId(((EntityRayTraceResult) rayTraceResult).getEntity());
            return;
        }

        List<Entity> entityList = HitAssessment.getTargettableEntitiesWithinAABB(player.world, player, player, 32);
        Entity foundEntity = entityList.stream().min(Comparator.comparingDouble(e -> e.getDistanceSq(player))).orElse(null);
        if (foundEntity instanceof PartEntity) {
            foundEntity = ((PartEntity<?>) foundEntity).getParent();
        }
        useSlashBladeEntityPack.slashBladePack.slashBladeState.setTargetEntityId(foundEntity);
    }


}
