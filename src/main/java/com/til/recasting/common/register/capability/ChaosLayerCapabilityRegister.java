package com.til.recasting.common.register.capability;

import com.til.glowing_fire_glow.common.capability.synchronous.ISynchronousManage;
import com.til.glowing_fire_glow.common.register.StaticVoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.capability.OriginalCapabilityRegister;
import com.til.glowing_fire_glow.common.register.capability.synchronous.SynchronousCapabilityRegister;
import com.til.glowing_fire_glow.common.save.SaveField;
import com.til.glowing_fire_glow.common.synchronous.SynchronousField;
import com.til.recasting.common.register.slash_blade.instance.BaGuaBigSlashBladeRegister;

import java.util.function.Supplier;

@VoluntarilyRegister
public class ChaosLayerCapabilityRegister extends OriginalCapabilityRegister<ChaosLayerCapabilityRegister.ChaosLayer> {

    @VoluntarilyRegister
    public static class ChaosLayerSynchronousCapabilityRegister extends SynchronousCapabilityRegister<ChaosLayer, ChaosLayerCapabilityRegister> {

    }

    @StaticVoluntarilyAssignment
    public static class ChaosLayer {

        @VoluntarilyAssignment
        protected static BaGuaBigSlashBladeRegister.BaGuaBigSlashBlade_SA baGuaBigSlashBlade_sa;

        @VoluntarilyAssignment
        protected static ChaosLayerSynchronousCapabilityRegister chaosLayerSynchronousCapabilityRegister;
        protected final Supplier<ISynchronousManage> synchronousManageSupplier;

        @SynchronousField
        @SaveField
        protected int layer;

        @SynchronousField
        protected long time;

        @SynchronousField
        protected int color;

        public ChaosLayer(Supplier<ISynchronousManage> synchronousManageSupplier) {
            this.synchronousManageSupplier = synchronousManageSupplier;
        }

        public void upLayer(long time) {
            if (this.time <= 0) {
                this.time = time;
            }
            double difference = (double) (time - this.time);
            int rLayer = (int) Math.floor(difference / baGuaBigSlashBlade_sa.getDuration());
            if (rLayer > 0) {
                layer -= rLayer;
                this.time = time;
            }
        }

        public int getLayer(long time) {
            upLayer(time);
            return layer;
        }

        public void addLayer(long time) {
            upLayer(time);
            layer = Math.min(layer + 1, baGuaBigSlashBlade_sa.getMaxLayer());
            this.time = time;
            synchronousManageSupplier.get().addSynchronousCapability(chaosLayerSynchronousCapabilityRegister);
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }
    }
}
