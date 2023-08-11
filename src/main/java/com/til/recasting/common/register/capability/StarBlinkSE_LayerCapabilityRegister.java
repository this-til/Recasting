package com.til.recasting.common.register.capability;

import com.til.glowing_fire_glow.common.capability.synchronous.ISynchronousManage;
import com.til.glowing_fire_glow.common.register.StaticVoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.capability.OriginalCapabilityRegister;
import com.til.glowing_fire_glow.common.register.capability.synchronous.SynchronousCapabilityRegister;
import com.til.glowing_fire_glow.common.save.SaveField;
import com.til.glowing_fire_glow.common.synchronous.SynchronousField;
import com.til.recasting.common.register.slash_blade.instance.special.TilSlashBladeRegister;

import java.util.function.Supplier;

@VoluntarilyRegister
public class StarBlinkSE_LayerCapabilityRegister extends OriginalCapabilityRegister<StarBlinkSE_LayerCapabilityRegister.StarBlinkSE_Layer> {

    @VoluntarilyRegister
    public static class StarBlinkSE_LayerSynchronousCapabilityRegister extends SynchronousCapabilityRegister<StarBlinkSE_Layer, StarBlinkSE_LayerCapabilityRegister> {
    }


    @StaticVoluntarilyAssignment
    public static class StarBlinkSE_Layer {

        @VoluntarilyAssignment
        protected static TilSlashBladeRegister.StarBlinkSE starBlinkSE;

        @VoluntarilyAssignment
        protected static StarBlinkSE_LayerSynchronousCapabilityRegister starBlinkSELayerSynchronousCapabilityRegister;

        protected final Supplier<ISynchronousManage> synchronousManageSupplier;

        @SynchronousField
        @SaveField
        protected int layer;

        @SynchronousField
        @SaveField
        protected int color;

        protected long oldTime;

        public StarBlinkSE_Layer(Supplier<ISynchronousManage> synchronousManageSupplier) {
            this.synchronousManageSupplier = synchronousManageSupplier;
        }

        public boolean tryAdd(long time) {
            if (time - oldTime < starBlinkSE.getCool()) {
                return false;
            }
            if (layer >= starBlinkSE.getMaxLayer()) {
                return true;
            }
            layer ++;
            oldTime = time;
            synchronousManageSupplier.get().addSynchronousCapability(starBlinkSELayerSynchronousCapabilityRegister);
            return false;
        }

        public void reset() {
            if (layer <= 0) {
                return;
            }
            layer = 0;
            synchronousManageSupplier.get().addSynchronousCapability(starBlinkSELayerSynchronousCapabilityRegister);
        }

        public void setColor(int color) {
            if (this.color == color) {
                return;
            }
            this.color = color;
            synchronousManageSupplier.get().addSynchronousCapability(starBlinkSELayerSynchronousCapabilityRegister);
        }

        public int getColor() {
            return color;
        }

        public int getLayer() {
            return layer;
        }
    }
}
