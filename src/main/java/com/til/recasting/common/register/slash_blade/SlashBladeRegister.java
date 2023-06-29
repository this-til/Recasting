package com.til.recasting.common.register.slash_blade;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.RegisterBasics;
import com.til.glowing_fire_glow.util.Delayed;
import com.til.recasting.common.capability.SlashBladePack;
import com.til.recasting.common.item.RecastingSlashBlade;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.client.renderer.SlashBladeTEISR;
import mods.flammpfeil.slashblade.init.SBItems;
import mods.flammpfeil.slashblade.item.ItemTierSlashBlade;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author til
 */
public abstract class SlashBladeRegister extends RegisterBasics {


    protected ResourceLocation model;
    protected ResourceLocation texture;

    @ConfigField
    protected Delayed<ItemStack> itemStack;

    protected SlashBladePack slashBladePack;


    @Override
    protected void init() {
        model = new ResourceLocation(getName().getNamespace(), String.format("%s/%s/%s", SlashBlade.modid, getName().getPath(), "model"));
        texture = new ResourceLocation(getName().getNamespace(), String.format("%s/%s/%s", SlashBlade.modid, getName().getPath(), "texture"));
    }

    public ItemStack getItemStack() {
        return itemStack.get();
    }

    protected void defaultItemStackConfig(ItemStack itemStack) {
        slashBladePack = new SlashBladePack(itemStack);
        //slashBladePack.slashBladeState.setTexture(texture);
        //slashBladePack.slashBladeState.setModel(model);
    }

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        itemStack = new Delayed.ItemStackDelayed(() -> {
            ItemStack itemStack1 = new ItemStack(SBItems.slashblade);
            defaultItemStackConfig(itemStack1);
            return itemStack1;
        });
    }
}
