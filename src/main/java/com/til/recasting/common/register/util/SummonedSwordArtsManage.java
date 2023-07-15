package com.til.recasting.common.register.util;

import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.common.main.IWorldComponent;
import com.til.recasting.common.capability.SlashBladePack;
import com.til.recasting.common.entity.SummondSwordEntity;
import com.til.recasting.common.register.entity_type.SummondSwordEntityTypeRegister;
import mods.flammpfeil.slashblade.event.InputCommandEvent;
import mods.flammpfeil.slashblade.util.InputCommand;
import mods.flammpfeil.slashblade.util.VectorHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;


public class SummonedSwordArtsManage implements IWorldComponent {

    protected final Map<Entity, Long> timMap = new HashMap<>();

    protected final float[] yPos = new float[]{
            0.5f, 0.5f,
            0.25f, 0.25f,
            0, 0,
            -0.25f, -0.25f
    };

    @SubscribeEvent
    public void onInputChange(InputCommandEvent event) {
        EnumSet<InputCommand> old = event.getOld();
        EnumSet<InputCommand> current = event.getCurrent();
        ServerPlayerEntity sender = event.getPlayer();

        boolean onDown = !old.contains(InputCommand.M_DOWN) && current.contains(InputCommand.M_DOWN);
        boolean onUp = old.contains(InputCommand.M_DOWN) && !current.contains(InputCommand.M_DOWN);


        if (onDown) {
            timMap.put(sender, sender.world.getGameTime());
        }

        if (onUp && timMap.containsKey(sender)) {
            long onDownTime = timMap.get(sender);
            timMap.remove(sender);
            if (onDownTime <= 0) {
                return;
            }
            ItemStack stack = sender.getHeldItemMainhand();
            SlashBladePack slashBladePack = new SlashBladePack(stack);
            if (!slashBladePack.isEffective(SlashBladePack.EffectiveType.canUse)) {
                return;
            }
            Entity targetEntity = slashBladePack.slashBladeState.getTargetEntity(sender.world);
            SummondSwordEntity summondSwordEntity = new SummondSwordEntity(
                    GlowingFireGlow.getInstance().getReflexManage().getVoluntarilyRegisterOfClass(SummondSwordEntityTypeRegister.class).getEntityType(),
                    sender.world,
                    sender);
            slashBladePack.iSlashBladeStateSupplement.decorate(summondSwordEntity);
            summondSwordEntity.setColor(slashBladePack.slashBladeState.getColorCode());
            //todo 伤害评估器
            summondSwordEntity.setDamage(0.25);
            summondSwordEntity.setDelay(100);
            if (targetEntity != null) {
                summondSwordEntity.lookAt(RayTraceUtil.getPosition(targetEntity), false);
            }
            sender.world.addEntity(summondSwordEntity);
            sender.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 0.2F, 1.45F);
        }
    }

    @SubscribeEvent
    protected void onWorldTickEvent(TickEvent.WorldTickEvent event) {
        if (event.world.isRemote) {
            return;
        }
        for (PlayerEntity player : event.world.getPlayers()) {
            if (!timMap.containsKey(player)) {
                continue;
            }
            long onDownTime = timMap.get(player);
            if (onDownTime <= 0) {
                return;
            }
            float time = player.world.getGameTime() - onDownTime;
            if (time < 20) {
                return;
            }
            timMap.remove(player);
            ItemStack stack = player.getHeldItemMainhand();
            SlashBladePack slashBladePack = new SlashBladePack(stack);
            if (!slashBladePack.isEffective(SlashBladePack.EffectiveType.canUse)) {
                return;
            }
            Entity targetEntity = slashBladePack.slashBladeState.getTargetEntity(player.world);
            if (player.isSneaking()) {
                //todo 数量评估器
                int amount = 8;
                for (int i = 0; i < amount; i++) {
                    int dir = i % 8;

                    SummondSwordEntity summondSwordEntity = new SummondSwordEntity(
                            GlowingFireGlow.getInstance().getReflexManage().getVoluntarilyRegisterOfClass(SummondSwordEntityTypeRegister.class).getEntityType(),
                            player.world,
                            player);
                    slashBladePack.iSlashBladeStateSupplement.decorate(summondSwordEntity);
                    summondSwordEntity.setColor(slashBladePack.slashBladeState.getColorCode());
                    //todo 伤害评估器
                    summondSwordEntity.setDamage(0.12);
                    summondSwordEntity.setDelay(100);
                    summondSwordEntity.setStartDelay((dir / 2) * 2);

                    Vector3d pos = player.getEyePosition(1.0f)
                            .add(VectorHelper.getVectorForRotation(0.0f, player.getYaw(0) + 90).scale(dir % 2 == 0 ? 1 : -1));
                    summondSwordEntity.setPosition(pos.getX(), pos.getY() + yPos[dir], pos.getZ());
                    if (targetEntity != null) {
                        summondSwordEntity.lookAt(targetEntity.getPositionVec(), false);
                    }
                    player.world.addEntity(summondSwordEntity);

                }
                player.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 0.2F, 1.45F);
            }
        }
    }

}
