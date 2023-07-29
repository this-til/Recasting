package com.til.recasting.common.register.util;

import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.common.main.IWorldComponent;
import com.til.glowing_fire_glow.common.register.StaticVoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.recasting.common.data.SlashBladePack;
import com.til.recasting.common.data.UseSlashBladeEntityPack;
import com.til.recasting.common.entity.SummondSwordEntity;
import com.til.recasting.common.register.entity_type.SummondSwordEntityTypeRegister;
import com.til.recasting.common.register.overall_config.SummondSwordOverallConfig;
import mods.flammpfeil.slashblade.event.InputCommandEvent;
import mods.flammpfeil.slashblade.util.InputCommand;
import mods.flammpfeil.slashblade.util.VectorHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@StaticVoluntarilyAssignment
public class SummonedSwordArtsManage implements IWorldComponent {

    @VoluntarilyAssignment
    protected SummondSwordOverallConfig summondSwordOverallConfig;

    @VoluntarilyAssignment
    protected SummondSwordEntityTypeRegister summondSwordEntityTypeRegister;

    protected final Map<Entity, Long> timMap = new HashMap<>();

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
            UseSlashBladeEntityPack uesSlashBladeEntityPack = new UseSlashBladeEntityPack(event.getPlayer());
            if (!uesSlashBladeEntityPack.isEffective(SlashBladePack.EffectiveType.canUse)) {
                return;
            }
            SummondSwordEntity summondSwordEntity = new SummondSwordEntity(
                    GlowingFireGlow.getInstance().getReflexManage().getVoluntarilyRegisterOfClass(SummondSwordEntityTypeRegister.class).getEntityType(),
                    sender.world,
                    sender);
            uesSlashBladeEntityPack.slashBladePack.iSlashBladeStateSupplement.decorate(summondSwordEntity);
            summondSwordEntity.setColor(uesSlashBladeEntityPack.slashBladePack.slashBladeState.getColorCode());
            //todo 伤害评估器
            summondSwordEntity.setDamage(summondSwordOverallConfig.getOrdinaryAttack());
            summondSwordEntity.setMaxDelay(100);
            summondSwordEntity.lookAt(uesSlashBladeEntityPack.getAttackPos(), false);
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
            UseSlashBladeEntityPack useSlashBladeEntityPack = new UseSlashBladeEntityPack(player);
            EnumSet<InputCommand> inputCommands = useSlashBladeEntityPack.iInputState.getCommands();

            if (!useSlashBladeEntityPack.isEffective(SlashBladePack.EffectiveType.canUse)) {
                return;
            }
            Vector3d attackPos = useSlashBladeEntityPack.getAttackPos();
            AttackType attackType;
            if (inputCommands.contains(InputCommand.SNEAK)) {
                if (inputCommands.contains(InputCommand.FORWARD) && inputCommands.contains(InputCommand.BACK)) {
                    attackType = AttackType.HEAVY_RAIN_SWORD;
                } else if (inputCommands.contains(InputCommand.FORWARD)) {
                    attackType = AttackType.BLISTERING_SWORD;
                } else if (inputCommands.contains(InputCommand.BACK)) {
                    attackType = AttackType.STORM_SWORD;
                } else {
                    attackType = AttackType.SPIRAL_SWORD;
                }
            } else {
                attackType = AttackType.SPIRAL_SWORD;
            }
            int amount;
            double stepping;
            Vector3d pos;
            switch (attackType) {
                case SPIRAL_SWORD:
                    amount = summondSwordOverallConfig.getSpiralSwordAttackNumber();
                    stepping = Math.PI * 2 / amount;
                     pos = RayTraceUtil.getPosition(useSlashBladeEntityPack.entity);
                    for (int i = 0; i < amount; i++) {
                        SummondSwordEntity summondSwordEntity = new SummondSwordEntity(
                                GlowingFireGlow.getInstance().getReflexManage().getVoluntarilyRegisterOfClass(SummondSwordEntityTypeRegister.class).getEntityType(),
                                player.world,
                                player);
                        useSlashBladeEntityPack.slashBladePack.iSlashBladeStateSupplement.decorate(summondSwordEntity);
                        summondSwordEntity.setColor(useSlashBladeEntityPack.slashBladePack.slashBladeState.getColorCode());
                        summondSwordEntity.setDamage(summondSwordOverallConfig.getSpiralSwordAttack());
                        summondSwordEntity.setMaxPierce(summondSwordOverallConfig.getSpiralSwordPierceNumber());
                        summondSwordEntity.setPosition(pos.getX(), pos.getY(), pos.getZ());
                        float yaw = useSlashBladeEntityPack.entity.rotationYaw;
                        yaw = yaw / 180;
                        yaw += stepping * i;
                        Vector3d lookAtPosRotated = new Vector3d(Math.sin(yaw), 0, Math.cos(yaw));
                        summondSwordEntity.lookAt(lookAtPosRotated, true);
                        player.world.addEntity(summondSwordEntity);
                    }
                    player.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 0.2F, 1.45F);
                    break;
                case BLISTERING_SWORD:
                    amount = summondSwordOverallConfig.getBlisteringAttackNumber();
                    for (int i = 0; i < amount; i++) {
                        int dir = i % 8;
                        SummondSwordEntity summondSwordEntity = new SummondSwordEntity(
                                GlowingFireGlow.getInstance().getReflexManage().getVoluntarilyRegisterOfClass(SummondSwordEntityTypeRegister.class).getEntityType(),
                                player.world,
                                player);
                        useSlashBladeEntityPack.slashBladePack.iSlashBladeStateSupplement.decorate(summondSwordEntity);
                        summondSwordEntity.setColor(useSlashBladeEntityPack.slashBladePack.slashBladeState.getColorCode());
                        summondSwordEntity.setDamage(summondSwordOverallConfig.getBlisteringAttack());
                        summondSwordEntity.setMaxDelay(100);
                        summondSwordEntity.setStartDelay((dir / 2) * 2);
                        pos = player.getEyePosition(1.0f)
                                .add(VectorHelper.getVectorForRotation(0.0f, player.getYaw(0) + 90).scale(dir % 2 == 0 ? 1 : -1));
                        summondSwordEntity.setPosition(pos.getX(), pos.getY() + summondSwordOverallConfig.getBlisteringYOffset()[dir], pos.getZ());
                        summondSwordEntity.lookAt(attackPos, false);
                        player.world.addEntity(summondSwordEntity);
                    }
                    player.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 0.2F, 1.45F);
                    break;
                case STORM_SWORD:
                    int attackNumber = summondSwordOverallConfig.getStormSwordAttackNumber();
                    stepping = Math.PI * 2 / attackNumber;
                    for (int i = 0; i < attackNumber; i++) {
                        double offsetX = Math.sin(stepping * i);
                        double offsetZ = Math.cos(stepping * i);
                        SummondSwordEntity summondSwordEntity = new SummondSwordEntity(
                                GlowingFireGlow.getInstance().getReflexManage().getVoluntarilyRegisterOfClass(SummondSwordEntityTypeRegister.class).getEntityType(),
                                player.world,
                                player);
                        useSlashBladeEntityPack.slashBladePack.iSlashBladeStateSupplement.decorate(summondSwordEntity);
                        summondSwordEntity.setColor(useSlashBladeEntityPack.slashBladePack.slashBladeState.getColorCode());
                        summondSwordEntity.setStartDelay(5);
                        summondSwordEntity.setDamage(summondSwordOverallConfig.getStormSwordAttack());
                        summondSwordEntity.setMaxDelay(20);
                        summondSwordEntity.setPosition(
                                attackPos.getX() + offsetX * summondSwordOverallConfig.getStormSwordDistance(),
                                attackPos.getY(),
                                attackPos.getZ() + offsetZ * summondSwordOverallConfig.getStormSwordDistance());
                        summondSwordEntity.lookAt(attackPos, false);

                        player.world.addEntity(summondSwordEntity);
                    }
                    player.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 0.2F, 1.45F);
                    break;
                case HEAVY_RAIN_SWORD:
                    Vector3d centerAttackPos = attackPos.add(0, summondSwordOverallConfig.getHeavyRainYOffset(), 0);
                    Vector3d offset = summondSwordOverallConfig.getHeavyRainOffset();
                    amount = summondSwordOverallConfig.getHeavyRainAttackNumber();
                    Random random = useSlashBladeEntityPack.entity.getRNG();
                    for (int i = 0; i < amount; i++) {
                        pos = centerAttackPos.add(
                                -offset.getX() / 2 + random.nextDouble() * offset.getX(),
                                -offset.getY() / 2 + random.nextDouble() * offset.getY(),
                                -offset.getZ() / 2 + random.nextDouble() * offset.getZ());

                        SummondSwordEntity summondSwordEntity = new SummondSwordEntity(
                                summondSwordEntityTypeRegister.getEntityType(),
                                useSlashBladeEntityPack.entity.world,
                                useSlashBladeEntityPack.entity
                        );
                        useSlashBladeEntityPack.slashBladePack.iSlashBladeStateSupplement.decorate(summondSwordEntity);
                        summondSwordEntity.setColor(useSlashBladeEntityPack.slashBladePack.slashBladeState.getColorCode());
                        summondSwordEntity.setDamage(summondSwordOverallConfig.getHeavyRainAttack());
                        summondSwordEntity.setMaxDelay(10 + random.nextInt(10));
                        summondSwordEntity.setStartDelay(random.nextInt(10));
                        summondSwordEntity.setPosition(pos.getX(), pos.getY(), pos.getZ());
                        summondSwordEntity.lookAt(new Vector3d(
                                -offset.getX() / 2 + random.nextDouble() * offset.getX(),
                                -summondSwordOverallConfig.getHeavyRainYOffset() - offset.getY() / 2 + random.nextDouble() * offset.getY(),
                                -offset.getZ() / 2 + random.nextDouble() * offset.getZ()
                        ), true);
                        summondSwordEntity.setRoll(useSlashBladeEntityPack.entity.getRNG().nextInt(360));
                        player.world.addEntity(summondSwordEntity);
                    }
                    player.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 0.2F, 1.45F);
                    break;
            }
        }
    }

    protected enum AttackType {
        SPIRAL_SWORD,
        BLISTERING_SWORD,
        STORM_SWORD,
        HEAVY_RAIN_SWORD,
    }

}
