package com.til.recasting.common.capability;


import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.recasting.common.entity.SlashEffectEntity;
import com.til.recasting.common.entity.SummondSwordEntity;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

/***
 * 一个对应ISlashBladeState的补充状态
 */
public interface ISlashBladeStateSupplement {
    @Nullable
    ResourceLocation getSummondSwordModel();

    void setSummondSwordModel(ResourceLocation summondSwordModel);

    @Nullable
    ResourceLocation getSummondSwordTexture();

    void setSummondSwordTexture(ResourceLocation summondSwordTexture);

    @Nullable
    ResourceLocation getSlashEffectTexture();

    void setSlashEffectTexture(ResourceLocation slashEffectTexture);

    void decorate(SummondSwordEntity summondSwordEntity);

    void decorate(SlashEffectEntity slashEffectEntity);

    class SlashBladeStateSupplement implements ISlashBladeStateSupplement {

        @ConfigField
        @Nullable
        protected ResourceLocation summondSwordModel;
        @ConfigField
        @Nullable
        protected ResourceLocation summondSwordTexture;
        @ConfigField
        @Nullable
        protected ResourceLocation slashEffectTexture;

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
    }


}
