package com.til.recasting.common.mixin;

import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.recasting.common.capability.SlashBladePack;
import com.til.recasting.common.entity.SummondSwordEntity;
import com.til.recasting.common.register.entity_type.SummondSwordEntityTypeRegister;
import com.til.recasting.common.register.sa.SA_Register;
import com.til.recasting.util.RayTraceUtil;
import mods.flammpfeil.slashblade.ability.SummonedSwordArts;
import mods.flammpfeil.slashblade.event.InputCommandEvent;
import mods.flammpfeil.slashblade.util.AttackManager;
import mods.flammpfeil.slashblade.util.InputCommand;
import mods.flammpfeil.slashblade.util.VectorHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.*;

@Mixin(value = {SummonedSwordArts.class}, remap = false)
public class SummonedSwordArtsMixin {


    /**
     * @author
     * @reason
     */
    @Overwrite
    public void onInputChange(InputCommandEvent event) {
    }
}
