package com.til.recasting.common.capability;

import com.til.glowing_fire_glow.common.save.SaveField;
import net.minecraft.entity.EntityType;

public interface IItemEntity {

    EntityType<?> getEntityType();

    void setEntityType(EntityType<?> entityType);

    class ItemEntity implements IItemEntity {
        @SaveField
        protected EntityType<?> entityType;

        public ItemEntity() {

        }


        @Override
        public EntityType<?> getEntityType() {
            return entityType;
        }

        @Override
        public void setEntityType(EntityType<?> entityType) {
            this.entityType = entityType;
        }
    }

}
