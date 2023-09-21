package com.til.recasting.common.key;

import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.key.KeyRegister;
import com.til.recasting.common.data.SlashBladePack;
import com.til.recasting.common.data.UseSlashBladeEntityPack;
import com.til.recasting.common.entity.SummondSwordEntity;
import com.til.recasting.common.register.back_type.SummondSwordBackTypeRegister;
import com.til.recasting.common.register.entity_type.SummondSwordEntityTypeRegister;
import com.til.recasting.common.register.overall_config.SlayerStyleArtsOverallConfigRegister;
import com.til.recasting.common.register.util.RayTraceUtil;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.util.InputCommand;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SPlayerPositionLookPacket;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.TicketType;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

@VoluntarilyRegister
public class SpecialActionKeyRegister extends KeyRegister {

    @VoluntarilyAssignment
    protected SlayerStyleArtsOverallConfigRegister styleArtsOverallConfigRegister;

    @VoluntarilyAssignment
    protected SummondSwordEntityTypeRegister summondSwordEntityTypeRegister;

    @VoluntarilyAssignment
    protected SummondSwordBackTypeRegister.SummondSwordAttackBackTypeRegister attackBackTypeRegister;

    protected final static EnumSet<InputCommand> FOWERD_SPRINT_SNEAK = EnumSet.of(InputCommand.FORWARD, InputCommand.SNEAK);
    protected final static EnumSet<InputCommand> MOVE = EnumSet.of(InputCommand.FORWARD, InputCommand.BACK, InputCommand.LEFT, InputCommand.RIGHT);


    @Override
    public void pressedServer(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            PlayerEntity serverPlayerEntity = context.getSender();
            if (serverPlayerEntity == null) {
                return;
            }
            World world = serverPlayerEntity.world;
            UseSlashBladeEntityPack useSlashBladeEntityPack = new UseSlashBladeEntityPack(serverPlayerEntity);

            if (!useSlashBladeEntityPack.isEffective(SlashBladePack.EffectiveType.canUse)) {
                return;
            }

            Entity targetEntity = useSlashBladeEntityPack.getSlashBladePack().getSlashBladeState().getTargetEntity(world);


            EnumSet<InputCommand> inputCommandEnumSet = useSlashBladeEntityPack.getInputState().getCommands();

            if (inputCommandEnumSet.containsAll(FOWERD_SPRINT_SNEAK)) {
                transmit(useSlashBladeEntityPack, targetEntity);
                return;
            }
            if (inputCommandEnumSet.stream().anyMatch(MOVE::contains)) {


                float moveForward = inputCommandEnumSet.contains(InputCommand.FORWARD) == inputCommandEnumSet.contains(InputCommand.BACK) ? 0.0F : (inputCommandEnumSet.contains(InputCommand.FORWARD) ? 1.0F : -1.0F);
                float moveStrafe = inputCommandEnumSet.contains(InputCommand.LEFT) == inputCommandEnumSet.contains(InputCommand.RIGHT) ? 0.0F : (inputCommandEnumSet.contains(InputCommand.LEFT) ? 1.0F : -1.0F);
                Vector3d input = new Vector3d(moveStrafe, 0, moveForward);

                int count = useSlashBladeEntityPack.getMobEffectState().doAvoid(world.getGameTime());
                if (count <= 0) {
                    return;
                }
                //dodge(useSlashBladeEntityPack, input);
                if (serverPlayerEntity.getEntity().isOnGround()) {
                    dodge(useSlashBladeEntityPack, input);
                } else {
                    sprint(useSlashBladeEntityPack, input);
                }
            }
        });
    }

    @Override
    public void releaseServer(NetworkEvent.Context context) {

    }

    protected void sprint(UseSlashBladeEntityPack useSlashBladeEntityPack, Vector3d input) {
        Vector3d motion = getAbsoluteMotion(input, styleArtsOverallConfigRegister.getSprintPower(), useSlashBladeEntityPack.getEntity().rotationYaw);
        Vector3d move = useSlashBladeEntityPack.getEntity().getMotion().add(motion);
        useSlashBladeEntityPack.getEntity().setMotion(move);
    }

    protected void dodge(UseSlashBladeEntityPack useSlashBladeEntityPack, Vector3d input) {
        useSlashBladeEntityPack.getEntity().moveRelative(3.0f, input);
        Vector3d motion = this.maybeBackOffFromEdge(useSlashBladeEntityPack.getEntity().getMotion(), useSlashBladeEntityPack.getEntity());
        useSlashBladeEntityPack.getEntity().playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 0.5f, 1.2f);
        useSlashBladeEntityPack.getEntity().move(MoverType.SELF, motion);
        useSlashBladeEntityPack.getEntity().moveForced(useSlashBladeEntityPack.getEntity().getPositionVec());
        useSlashBladeEntityPack.getSlashBladePack().getSlashBladeState().updateComboSeq(
                useSlashBladeEntityPack.getEntity(),
                useSlashBladeEntityPack.getSlashBladePack().getSlashBladeState().getComboRootAir());
    }


    protected void transmit(UseSlashBladeEntityPack useSlashBladeEntityPack, @Nullable Entity targetEntity) {
        // 兼容网易版
        @Nullable Entity target = targetEntity;
        /*if (targetEntity != null && targetEntity.getParts() != null && 0 < targetEntity.getParts().length) {
            target = targetEntity.getParts()[0];
        } else {
            target = targetEntity;
        }*/
        SummondSwordEntity summondSwordEntity = new SummondSwordEntity(
                summondSwordEntityTypeRegister.getEntityType(),
                useSlashBladeEntityPack.getEntity().world,
                useSlashBladeEntityPack.getEntity()
        );
        useSlashBladeEntityPack.getSlashBladePack().getSlashBladeStateSupplement().decorate(summondSwordEntity);
        summondSwordEntity.setColor(useSlashBladeEntityPack.getSlashBladePack().getSlashBladeState().getColorCode());
        summondSwordEntity.setDamage(styleArtsOverallConfigRegister.getSprintAttack());
        summondSwordEntity.getBackRunPack().addRunBack(attackBackTypeRegister, (summondSwordEntity1, hitEntity) -> {
            if (hitEntity instanceof LivingEntity) {
                doTeleport(useSlashBladeEntityPack.getEntity(), (LivingEntity) hitEntity);
            }
        });
        if (target != null) {
            Vector3d attackPos = RayTraceUtil.getPosition(target);
            summondSwordEntity.setPosition(attackPos.getX(), attackPos.getY(), attackPos.getZ());
            summondSwordEntity.doForceHitEntity(target);
        } else {
            summondSwordEntity.lookAt(useSlashBladeEntityPack.getAttackPos(), false);
        }
        useSlashBladeEntityPack.getEntity().world.addEntity(summondSwordEntity);
    }

    protected void doTeleport(Entity entityIn, LivingEntity target) {
        if (!(entityIn.world instanceof ServerWorld)) return;

        if (entityIn instanceof PlayerEntity) {
            PlayerEntity player = ((PlayerEntity) entityIn);
            player.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 0.75F, 1.25F);

            player.getHeldItemMainhand().getCapability(ItemSlashBlade.BLADESTATE)
                    .ifPresent(state -> state.updateComboSeq(player, state.getComboRootAir()));

            //Untouchable.setUntouchable(player, 10);
        }

        ServerWorld worldIn = (ServerWorld) entityIn.world;

        Vector3d tereportPos = target.getPositionVec().add(0, target.getHeight() / 2.0, 0).add(entityIn.getLookVec().scale(-2.0));

        double x = tereportPos.x;
        double y = tereportPos.y;
        double z = tereportPos.z;
        float yaw = entityIn.rotationYaw;
        float pitch = entityIn.rotationPitch;

        Set<SPlayerPositionLookPacket.Flags> relativeList = Collections.emptySet();
        BlockPos blockpos = new BlockPos(x, y, z);
        if (!World.isInvalidPosition(blockpos)) {
            return;
        }
        if (entityIn instanceof ServerPlayerEntity) {
            ChunkPos chunkpos = new ChunkPos(new BlockPos(x, y, z));
            worldIn.getChunkProvider().registerTicket(TicketType.POST_TELEPORT, chunkpos, 1, entityIn.getEntityId());
            entityIn.stopRiding();
            if (((ServerPlayerEntity) entityIn).isSleeping()) {
                ((ServerPlayerEntity) entityIn).stopSleepInBed(true, true);
            }

            if (worldIn == entityIn.world) {
                ((ServerPlayerEntity) entityIn).connection.setPlayerLocation(x, y, z, yaw, pitch, relativeList);
            } else {
                ((ServerPlayerEntity) entityIn).teleport(worldIn, x, y, z, yaw, pitch);
            }

            entityIn.setRotationYawHead(yaw);
        } else {
            float f1 = MathHelper.wrapDegrees(yaw);
            float f = MathHelper.wrapDegrees(pitch);
            f = MathHelper.clamp(f, -90.0F, 90.0F);
            if (worldIn == entityIn.world) {
                entityIn.setLocationAndAngles(x, y, z, f1, f);
                entityIn.setRotationYawHead(f1);
            } else {
                entityIn.detach();
                Entity entity = entityIn;
                entityIn = entityIn.getType().create(worldIn);
                if (entityIn == null) {
                    return;
                }

                entityIn.copyDataFromOld(entity);
                entityIn.setLocationAndAngles(x, y, z, f1, f);
                entityIn.setRotationYawHead(f1);
                worldIn.addFromAnotherDimension(entityIn);
            }
        }

        if (!(entityIn instanceof LivingEntity) || !((LivingEntity) entityIn).isElytraFlying()) {
            entityIn.setMotion(entityIn.getMotion().mul(1.0D, 0.0D, 1.0D));
            entityIn.setOnGround(false);
        }

        if (entityIn instanceof CreatureEntity) {
            ((CreatureEntity) entityIn).getNavigator().clearPath();
        }


    }

    protected Vector3d maybeBackOffFromEdge(Vector3d vec, LivingEntity mover) {
        double d0 = vec.x;
        double d1 = vec.z;
        double d2 = 0.05D;

        while (d0 != 0.0D && mover.world.hasNoCollisions(mover, mover.getBoundingBox().offset(d0, (double) (-mover.stepHeight), 0.0D))) {
            if (d0 < 0.05D && d0 >= -0.05D) {
                d0 = 0.0D;
            } else if (d0 > 0.0D) {
                d0 -= 0.05D;
            } else {
                d0 += 0.05D;
            }
        }

        while (d1 != 0.0D && mover.world.hasNoCollisions(mover, mover.getBoundingBox().offset(0.0D, (double) (-mover.stepHeight), d1))) {
            if (d1 < 0.05D && d1 >= -0.05D) {
                d1 = 0.0D;
            } else if (d1 > 0.0D) {
                d1 -= 0.05D;
            } else {
                d1 += 0.05D;
            }
        }

        while (d0 != 0.0D && d1 != 0.0D && mover.world.hasNoCollisions(mover, mover.getBoundingBox().offset(d0, (double) (-mover.stepHeight), d1))) {
            if (d0 < 0.05D && d0 >= -0.05D) {
                d0 = 0.0D;
            } else if (d0 > 0.0D) {
                d0 -= 0.05D;
            } else {
                d0 += 0.05D;
            }

            if (d1 < 0.05D && d1 >= -0.05D) {
                d1 = 0.0D;
            } else if (d1 > 0.0D) {
                d1 -= 0.05D;
            } else {
                d1 += 0.05D;
            }
        }

        vec = new Vector3d(d0, vec.y, d1);

        return vec;
    }

    private static Vector3d getAbsoluteMotion(Vector3d relative, float motionScaler, float facing) {
        double d0 = relative.lengthSquared();
        if (d0 < 1.0E-7D) {
            return Vector3d.ZERO;
        } else {
            Vector3d vector3d = (d0 > 1.0D ? relative.normalize() : relative).scale(motionScaler);
            float f = MathHelper.sin(facing * ((float) Math.PI / 180F));
            float f1 = MathHelper.cos(facing * ((float) Math.PI / 180F));
            return new Vector3d(vector3d.x * (double) f1 - vector3d.z * (double) f, vector3d.y, vector3d.z * (double) f1 + vector3d.x * (double) f);
        }
    }
}
