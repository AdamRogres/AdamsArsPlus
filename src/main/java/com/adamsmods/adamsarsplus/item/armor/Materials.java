package com.adamsmods.adamsarsplus.item.armor;

import com.adamsmods.adamsarsplus.ArsNouveauRegistry;
import com.adamsmods.adamsarsplus.registry.ModRegistry;
import com.hollingsworth.arsnouveau.common.items.ModItem;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.function.Supplier;

import static com.adamsmods.adamsarsplus.registry.ModRegistry.*;

public class Materials {
    public static final ANArmorMaterial CADE;
    public static final ANArmorMaterial RYAN;
    public static final ANArmorMaterial NICK;
    public static final ANArmorMaterial CAMR;
    public static final ANArmorMaterial MATT;
    public static final ANArmorMaterial ADAM;

    public Materials() {
    }

    static {
        CADE = new ANArmorMaterial("an_cade", 80,  new int[]{2, 5, 6, 3}, 30, SoundEvents.ARMOR_EQUIP_LEATHER,   2.0F, FROST_SOUL);
        RYAN = new ANArmorMaterial("an_ryan", 80,  new int[]{3, 6, 8, 3}, 30, SoundEvents.ARMOR_EQUIP_CHAIN,     3.0F, FLAME_SOUL);
        NICK = new ANArmorMaterial("an_nick", 80,  new int[]{3, 7, 10,4}, 30, SoundEvents.ARMOR_EQUIP_IRON,      4.0F, EARTH_SOUL);
        CAMR = new ANArmorMaterial("an_camr", 120, new int[]{3, 7, 10,4}, 30, SoundEvents.ARMOR_EQUIP_LEATHER,   4.0F, LIGHTNING_SOUL);
        MATT = new ANArmorMaterial("an_matt", 120, new int[]{5, 8, 11,6}, 30, SoundEvents.ARMOR_EQUIP_GOLD,      5.0F, HERO_SOUL);
        ADAM = new ANArmorMaterial("an_adam", 160, new int[]{6, 9, 12,7}, 30, SoundEvents.ARMOR_EQUIP_NETHERITE, 6.0F, VOID_SOUL);
    }

    public static class ANArmorMaterial implements ArmorMaterial {
        private static final int[] Max_Damage_Array = new int[]{13, 15, 16, 11};
        private final String name;
        private final int maxDamageFactor;
        private final int[] damageReductionAmountArray;
        private final int enchantability;
        private final SoundEvent soundEvent;
        private final float toughness;
        private final LazyLoadedValue<Ingredient> repairMaterial;

        public ANArmorMaterial(String name, int maxDamageFactor, int[] damageReductionAmountArray, int enchantability, SoundEvent soundEvent, float toughness, Supplier<ModItem> supplier) {
            this.name = name;
            this.maxDamageFactor = maxDamageFactor;
            this.damageReductionAmountArray = damageReductionAmountArray;
            this.enchantability = enchantability;
            this.soundEvent = soundEvent;
            this.toughness = toughness;
            this.repairMaterial = new LazyLoadedValue(supplier);
        }

        public int getDurabilityForType(ArmorItem.Type p_266807_) {
            return Max_Damage_Array[p_266807_.getSlot().getIndex()] * this.maxDamageFactor;
        }

        public int getDefenseForType(ArmorItem.Type p_267168_) {
            return this.damageReductionAmountArray[p_267168_.getSlot().getIndex()];
        }

        public int getEnchantmentValue() {
            return this.enchantability;
        }

        public SoundEvent getEquipSound() {
            return this.soundEvent;
        }

        public Ingredient getRepairIngredient() {
            return (Ingredient)this.repairMaterial.get();
        }

        public String getName() {
            return this.name;
        }

        public float getToughness() {
            return this.toughness;
        }

        public float getKnockbackResistance() {
            return 0.0F;
        }
    }
}

