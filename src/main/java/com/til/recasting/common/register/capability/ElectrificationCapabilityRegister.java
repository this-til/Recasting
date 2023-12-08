package com.til.recasting.common.register.capability;

import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.capability.OriginalCapabilityRegister;
import com.til.glowing_fire_glow.common.save.SaveField;

@VoluntarilyRegister
@Deprecated
public class ElectrificationCapabilityRegister extends OriginalCapabilityRegister<ElectrificationCapabilityRegister.Electrification> {
    @Deprecated
    public static class Electrification {
        @SaveField
        protected long expireTime;

        @SaveField
        protected int color;

        public boolean has(long time) {
            return time <= expireTime;
        }

        public void up(long time) {
            this.expireTime = time;
        }

        public int getColor() {
            return color;
        }

        public long getExpireTime() {
            return expireTime;
        }

        public Electrification setColor(int color) {
            this.color = color;
            return this;
        }
    }
}
