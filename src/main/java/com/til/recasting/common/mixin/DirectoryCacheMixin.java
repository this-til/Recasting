package com.til.recasting.common.mixin;

import net.minecraft.data.DirectoryCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(DirectoryCache.class)
public class DirectoryCacheMixin {
    /**
     * @author
     * @reason
     */
    @Overwrite
    public void writeCache() {
    }
}
