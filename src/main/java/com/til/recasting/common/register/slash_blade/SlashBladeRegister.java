package com.til.recasting.common.register.slash_blade;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.config.ConfigManage;
import com.til.glowing_fire_glow.common.register.RegisterBasics;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.util.Delayed;
import com.til.glowing_fire_glow.common.util.StringUtil;
import com.til.recasting.common.capability.SlashBladePack;
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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

/**
 * @author til
 */
public abstract class SlashBladeRegister extends RegisterBasics {


    protected ResourceLocation model;
    protected ResourceLocation texture;

    @Nullable
    protected ResourceLocation summondSwordModel;
    @Nullable
    protected ResourceLocation summondSwordTexture;

    @Nullable
    protected ResourceLocation slashEffectTexture;

    @ConfigField
    protected Delayed<ItemStack> itemStack;

    protected SlashBladePack slashBladePack;

    @VoluntarilyAssignment
    protected ConfigManage configManage;


    @Override
    protected void init() {
        model = new ResourceLocation(getName().getNamespace(), String.join("/", SlashBlade.modid, getName().getPath(), "model.obj"));
        texture = new ResourceLocation(getName().getNamespace(), String.join("/", SlashBlade.modid, getName().getPath(), "texture.png"));
    }

    protected void defaultItemStackConfig(ItemStack itemStack) {
        slashBladePack = new SlashBladePack(itemStack);
        slashBladePack.slashBladeState.setTexture(texture);
        slashBladePack.slashBladeState.setModel(model);
        slashBladePack.slashBladeState.setTranslationKey(StringUtil.formatLang(getName()));

        slashBladePack.iSlashBladeStateSupplement.setSummondSwordModel(summondSwordModel);
        slashBladePack.iSlashBladeStateSupplement.setSummondSwordTexture(summondSwordTexture);
        slashBladePack.iSlashBladeStateSupplement.setSlashEffectTexture(slashEffectTexture);
    }

    public SlashBladePack getSlashBladePack() {
        return new SlashBladePack(itemStack.get().copy());
    }


    public boolean displayItem() {
        return true;
    }

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        configManage.addDelayedWrite(this);
        itemStack = new Delayed.ItemStackDelayed(() -> {
            ItemStack itemStack1 = new ItemStack(SBItems.slashblade);
            defaultItemStackConfig(itemStack1);
            return itemStack1;
        });
    }
}
