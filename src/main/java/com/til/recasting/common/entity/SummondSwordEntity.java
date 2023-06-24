package com.til.recasting.common.entity;

import com.til.glowing_fire_glow.common.register.StaticVoluntarilyAssignment;
import com.til.glowing_fire_glow.util.Extension;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.entity.IShootable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

@StaticVoluntarilyAssignment
public class SummondSwordEntity extends Entity implements IShootable {

    public static final ResourceLocation defaultModelName = new ResourceLocation(SlashBlade.modid, "model/util/ss.obj");
    public static final ResourceLocation defaultTexture = new ResourceLocation(SlashBlade.modid, "model/util/ss.png");
    /***
     * 幻影剑的颜色
     */
    protected static final DataParameter<Integer> COLOR = EntityDataManager.createKey(SummondSwordEntity.class, DataSerializers.VARINT);

    /***
     * 幻影剑插入敌人的id
     * 如果没有命中为-1
     */
    protected static final DataParameter<Integer> HIT_ENTITY_ID = EntityDataManager.createKey(SummondSwordEntity.class, DataSerializers.VARINT);

    /***
     * 模型
     */
    protected static final DataParameter<String> MODEL = EntityDataManager.createKey(SummondSwordEntity.class, DataSerializers.STRING);

    /***
     * 材质
     */
    protected static final DataParameter<String> TEXTURE = EntityDataManager.createKey(SummondSwordEntity.class, DataSerializers.STRING);


    /***
     * 幻影剑的roll
     */
    protected static final DataParameter<Float> ROLL = EntityDataManager.<Float>createKey(SummondSwordEntity.class, DataSerializers.FLOAT);


    /***
     * 幻影剑的最大生命
     */
    protected int maxLife;

    /***
     * 基础速度
     */
    protected float basicsSeep = 3;

    /***
     *  加速度
     */
    protected float acceleratedSpeed = 1;

    /***
     * 旋转速度
     */
    protected float rotateSpeed = 14.4f;

    /***
     * 延迟开始
     */
    protected int delay = 15;

    /***
     * 幻影剑的伤害
     */
    protected double damage = 1;

    @Nullable
    protected Extension.Func<Vector3d> trackPos;

    /***
     * 幻影剑状态
     */
    protected SummondSwordEntityState summondSwordEntityState = SummondSwordEntityState.aim;

    protected final SoundEvent hitEntitySound = SoundEvents.ITEM_TRIDENT_HIT;

    protected final SoundEvent hitEntityPlayerSound = SoundEvents.ITEM_TRIDENT_HIT;

    protected final SoundEvent hitGroundSound = SoundEvents.ITEM_TRIDENT_HIT_GROUND;


    public SummondSwordEntity(EntityType<? extends SummondSwordEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void registerData() {
        this.dataManager.register(COLOR, 0x3333FF);
        this.dataManager.register(HIT_ENTITY_ID, -1);
        this.dataManager.register(MODEL, "");
        this.dataManager.register(TEXTURE, "");
        this.dataManager.register(ROLL, 0f);
    }


    @Override
    public void tick() {
        super.tick();

    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public double getDamage() {
        return damage;
    }


    public int getColor() {
        return dataManager.get(COLOR);
    }

    public SummondSwordEntity setColor(int color) {
        this.dataManager.set(COLOR, color);
        return this;
    }

    @Nullable
    protected Entity hitEntity;

    @Nullable
    public Entity getHitEntity() {
        if (hitEntity == null) {
            int entityID = dataManager.get(HIT_ENTITY_ID);
            if (entityID >= 0) {
                hitEntity = world.getEntityByID(entityID);
            }
        }
        return hitEntity;
    }

    public SummondSwordEntity setHitEntityId(Entity hitEntityId) {
        hitEntity = hitEntityId;
        dataManager.set(HIT_ENTITY_ID, hitEntityId.getEntityId());
        return this;
    }

    @Nullable
    protected ResourceLocation model;

    public ResourceLocation getModel() {
        if (model == null) {
            String modelString = dataManager.get(MODEL);
            if (modelString.isEmpty()) {
                model = defaultModelName;
            }
        }
        return model;
    }

    public SummondSwordEntity setModel(ResourceLocation model) {
        this.model = model;
        dataManager.set(MODEL, model.toString());
        return this;
    }

    @Nullable
    protected ResourceLocation texture;

    @Nullable
    public ResourceLocation getTexture() {
        if (texture == null) {
            String textureString = dataManager.get(TEXTURE);
            if (textureString.isEmpty()) {
                texture = defaultModelName;
            }
        }
        return texture;
    }

    public SummondSwordEntity setTexture(ResourceLocation texture) {
        dataManager.set(TEXTURE, texture.toString());
        this.texture = texture;
        return this;
    }

    public float getRoll() {
        return dataManager.get(ROLL);
    }

    public SummondSwordEntity setRoll(float roll) {
        dataManager.set(ROLL, roll);
        return this;
    }

    public SummondSwordEntity setDamage(double damage) {
        this.damage = damage;
        return this;
    }

    public SummondSwordEntity setMaxLife(int maxLife) {
        this.maxLife = maxLife;
        return this;
    }

    public SummondSwordEntity setBasicsSeep(float basicsSeep) {
        this.basicsSeep = basicsSeep;
        return this;
    }

    public SummondSwordEntity setAcceleratedSpeed(float acceleratedSpeed) {
        this.acceleratedSpeed = acceleratedSpeed;
        return this;
    }

    public SummondSwordEntity setRotateSpeed(float rotateSpeed) {
        this.rotateSpeed = rotateSpeed;
        return this;
    }

    public SummondSwordEntity setDelay(int delay) {
        this.delay = delay;
        return this;
    }

    public SummondSwordEntity setTrackPos(@Nullable Extension.Func<Vector3d> trackPos) {
        this.trackPos = trackPos;
        return this;
    }

    /***
     *  追踪时改变朝向
     */
    protected void changeForward() {
        if (trackPos == null) {
            return;
        }
        Vector3d finalForward = trackPos.func().subtract(getPositionVec());
        finalForward = finalForward.normalize();

        float angleOffset = Vector3d.angle(transform.forward, finalForward);
        if (angleOffset > rotateSpeed) {
            angleOffset = rotateSpeed;
        }
        transform.forward = Vector3.Lerp(transform.forward, finalForward, getDeltaTime(ScaleManage.TriggerType.fixedUpdate, ScaleManage.TimeType.part) * rotateSpeed / angleOffset);
    }


    public enum SummondSwordEntityState {
        /***
         * 瞄准
         */
        aim,

        /***
         * 移动
         */
        move,

        /***
         * 攻击到
         */
        attack,

        /***
         * 攻击到地面
         */
        attackGround
    }


    /***
     * 幻影剑攻击类型
     */
    public enum AttackTime {
        hit, end
    }
}
