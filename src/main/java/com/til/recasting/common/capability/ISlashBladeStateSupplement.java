package com.til.recasting.common.capability;


import com.til.glowing_fire_glow.common.register.particle_register.ParticleRegister;
import com.til.glowing_fire_glow.common.save.SaveField;
import com.til.recasting.common.entity.JudgementCutEntity;
import com.til.recasting.common.entity.SlashEffectEntity;
import com.til.recasting.common.entity.SummondSwordEntity;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

/***
 * 一个对应ISlashBladeState的补充状态
 */
public interface ISlashBladeStateSupplement {

    float getAttackDistance();

    void setAttackDistance(float attackDistance);

    @Nullable
    ResourceLocation getSummondSwordModel();

    void setSummondSwordModel(ResourceLocation summondSwordModel);

    @Nullable
    ResourceLocation getSummondSwordTexture();

    void setSummondSwordTexture(ResourceLocation summondSwordTexture);

    @Nullable
    ResourceLocation getJudgementCutModel();

    void setJudgementCutModel(@Nullable ResourceLocation judgementCutModel);

    @Nullable
    ResourceLocation getJudgementCutTexture();

    void setJudgementCutTexture(@Nullable ResourceLocation judgementCutTexture);

    @Nullable
    ResourceLocation getSlashEffectTexture();

    void setSlashEffectTexture(ResourceLocation slashEffectTexture);

    void decorate(SummondSwordEntity summondSwordEntity);

    void decorate(SlashEffectEntity slashEffectEntity);

    void decorate(JudgementCutEntity judgementCutEntity);

    float getDurable();

    void setDurable(float durable);

    class SlashBladeStateSupplement implements ISlashBladeStateSupplement {

        @SaveField
        protected float attackDistance = 1;

        @SaveField
        protected float durable = 1;

        @SaveField
        @Nullable
        protected ResourceLocation summondSwordModel;
        @SaveField
        @Nullable
        protected ResourceLocation summondSwordTexture;
        @SaveField
        @Nullable
        protected ResourceLocation slashEffectTexture;

        @SaveField
        @Nullable
        protected ResourceLocation judgementCutModel;
        @SaveField
        @Nullable
        protected ResourceLocation judgementCutTexture;

        /***
         * 攻击的效果
         */
        @SaveField
        @Nullable
        protected ParticleRegister attackEffect;

        /***
         * 移动的效果
         */
        @SaveField
        @Nullable
        protected ParticleRegister moveEffect;

        @Override
        public float getAttackDistance() {
            return attackDistance;
        }

        @Override
        public void setAttackDistance(float attackDistance) {
            this.attackDistance = attackDistance;
        }

        @Nullable
        @Override
        public ResourceLocation getSummondSwordModel() {
            return summondSwordModel;
        }

        @Override
        public void setSummondSwordModel(ResourceLocation summondSwordModel) {
            this.summondSwordModel = summondSwordModel;
        }

        @Nullable
        @Override
        public ResourceLocation getSummondSwordTexture() {
            return summondSwordTexture;
        }

        @Override
        public void setSummondSwordTexture(ResourceLocation summondSwordTexture) {
            this.summondSwordTexture = summondSwordTexture;
        }

        @Override
        @Nullable
        public ResourceLocation getJudgementCutModel() {
            return judgementCutModel;
        }

        @Override
        public void setJudgementCutModel(@Nullable ResourceLocation judgementCutModel) {
            this.judgementCutModel = judgementCutModel;
        }

        @Override
        @Nullable
        public ResourceLocation getJudgementCutTexture() {
            return judgementCutTexture;
        }

        @Override
        public void setJudgementCutTexture(@Nullable ResourceLocation judgementCutTexture) {
            this.judgementCutTexture = judgementCutTexture;
        }

        @Nullable
        @Override
        public ResourceLocation getSlashEffectTexture() {
            return slashEffectTexture;
        }

        @Override
        public void setSlashEffectTexture(ResourceLocation slashEffectTexture) {
            this.slashEffectTexture = slashEffectTexture;
        }

        @Override
        public void decorate(SummondSwordEntity summondSwordEntity) {
            if (getSummondSwordModel() != null) {
                summondSwordEntity.setModel(getSummondSwordModel());
            }
            if (getSummondSwordTexture() != null) {
                summondSwordEntity.setTexture(getSummondSwordTexture());
            }
        }

        @Override
        public void decorate(SlashEffectEntity slashEffectEntity) {
            if (getSlashEffectTexture() != null) {
                slashEffectEntity.setTexture(getSlashEffectTexture());
            }
        }

        @Override
        public void decorate(JudgementCutEntity judgementCutEntity) {
            if (getJudgementCutModel() != null) {
                judgementCutEntity.setModel(getJudgementCutModel());
            }
            if (getJudgementCutTexture() != null) {
                judgementCutEntity.setTexture(getJudgementCutTexture());
            }
        }

        @Override
        public float getDurable() {
            return Math.max(durable, 0.01f);
        }

        @Override
        public void setDurable(float durable) {
            this.durable = durable;
        }
    }


}
