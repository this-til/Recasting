package com.til.recasting.common.register.slash_blade;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.RegisterBasics;
import com.til.recasting.common.item.RecastingSlashBlade;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.client.renderer.SlashBladeTEISR;
import mods.flammpfeil.slashblade.item.ItemTierSlashBlade;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author til
 */
public abstract class SlashBladeRegister extends RegisterBasics {

    /***
     * 基础伤害
     */
    @ConfigField
    protected int basicsAttackDamage;

    /***
     * 攻击速度
     */
    @ConfigField
    protected float attackSpeed;

    /***
     * 耐久
     */
    @ConfigField
    protected int maxUses;

    /***
     * 挖掘等级
     */
    @ConfigField
    protected int harvestLevel;

    /***
     * 附魔等级
     */
    @ConfigField
    protected int enchantability;

    protected ResourceLocation model;
    protected ResourceLocation texture;

    protected IItemTier iItemTier;
    protected RecastingSlashBlade recastingSlashBlade;


    @Override
    protected void init() {
        iItemTier = initItemTier();
        recastingSlashBlade = initRecastingSlashBlade();

        model = new ResourceLocation(getName().getNamespace(), String.format("%s/%s/%s", SlashBlade.modid, getName().getPath(), "model"));
        texture = new ResourceLocation(getName().getNamespace(), String.format("%s/%s/%s", SlashBlade.modid, getName().getPath(), "texture"));

        ForgeRegistries.ITEMS.register(recastingSlashBlade);
    }

    protected IItemTier initItemTier() {
        return new IItemTier() {
            @Override
            public int getMaxUses() {
                return maxUses;
            }

            @Override
            public float getEfficiency() {
                return 0;
            }

            @Override
            public float getAttackDamage() {
                return 0;
            }

            @Override
            public int getHarvestLevel() {
                return harvestLevel;
            }

            @Override
            public int getEnchantability() {
                return enchantability;
            }

            @Override
            public Ingredient getRepairMaterial() {
                ITag<Item> tags = ItemTags.getCollection().get(new ResourceLocation(SlashBlade.modid, "proudsouls"));
                return Ingredient.fromTag(tags);
            }
        };

    }

    protected RecastingSlashBlade initRecastingSlashBlade() {
        return (RecastingSlashBlade) new RecastingSlashBlade(iItemTier, basicsAttackDamage, attackSpeed, (new Item.Properties()).group(ItemGroup.COMBAT)
                .setISTER(() -> SlashBladeTEISR::new))
                .setRegistryName(getName());
    }

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        basicsAttackDamage = 1;
        attackSpeed = -2.4F;
        maxUses = 100;
        harvestLevel = 3;
        enchantability = 10;
    }
}
