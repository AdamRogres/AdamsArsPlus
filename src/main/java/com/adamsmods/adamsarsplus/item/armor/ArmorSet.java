package com.adamsmods.adamsarsplus.item.armor;

import com.adamsmods.adamsarsplus.registry.ModRegistry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public class ArmorSet {
    public String getName() {
        return name;
    }

    protected String name;
    protected RegistryObject<MageMagicArmor> head;
    protected RegistryObject<MageMagicArmor> chest;
    protected RegistryObject<MageMagicArmor> legs;
    protected RegistryObject<MageMagicArmor> feet;

    protected RegistryObject<MageMagicArmor> head_a;
    protected RegistryObject<MageMagicArmor> chest_a;
    protected RegistryObject<MageMagicArmor> legs_a;
    protected RegistryObject<MageMagicArmor> feet_a;

    static CadeArmor CADE_ARMORSET = new CadeArmor();
    static RyanArmor RYAN_ARMORSET = new RyanArmor();
    static NickArmor NICK_ARMORSET = new NickArmor();
    static CamArmor CAM_ARMORSET = new CamArmor();
    static MattArmor MATT_ARMORSET = new MattArmor();
    static AdamArmor ADAM_ARMORSET = new AdamArmor();

    public static class CadeArmor extends ArmorSet {
        public CadeArmor() {
            this.name = "cade_armor";
            this.head = ModRegistry.CADE_HOOD;
            this.head_a = ModRegistry.CADE_HOOD_A;
            this.chest = ModRegistry.CADE_ROBES;
            this.chest_a = ModRegistry.CADE_ROBES_A;
            this.legs = ModRegistry.CADE_LEGGINGS;
            this.legs_a = ModRegistry.CADE_LEGGINGS_A;
            this.feet = ModRegistry.CADE_BOOTS;
            this.feet_a = ModRegistry.CADE_BOOTS_A;
        }
    }

    public static class RyanArmor extends ArmorSet {
        public RyanArmor() {
            this.name = "ryan_armor";
            this.head = ModRegistry.RYAN_HOOD;
            this.head_a = ModRegistry.RYAN_HOOD_A;
            this.chest = ModRegistry.RYAN_ROBES;
            this.chest_a = ModRegistry.RYAN_ROBES_A;
            this.legs = ModRegistry.RYAN_LEGGINGS;
            this.legs_a = ModRegistry.RYAN_LEGGINGS_A;
            this.feet = ModRegistry.RYAN_BOOTS;
            this.feet_a = ModRegistry.RYAN_BOOTS_A;
        }
    }

    public static class NickArmor extends ArmorSet {
        public NickArmor() {
            this.name = "nick_armor";
            this.head = ModRegistry.NICK_HOOD;
            this.head_a = ModRegistry.NICK_HOOD_A;
            this.chest = ModRegistry.NICK_ROBES;
            this.chest_a = ModRegistry.NICK_ROBES_A;
            this.legs = ModRegistry.NICK_LEGGINGS;
            this.legs_a = ModRegistry.NICK_LEGGINGS_A;
            this.feet = ModRegistry.NICK_BOOTS;
            this.feet_a = ModRegistry.NICK_BOOTS_A;
        }
    }

    public static class CamArmor extends ArmorSet {
        public CamArmor() {
            this.name = "cam_armor";
            this.head = ModRegistry.CAMR_HOOD;
            this.head_a = ModRegistry.CAMR_HOOD_A;
            this.chest = ModRegistry.CAMR_ROBES;
            this.chest_a = ModRegistry.CAMR_ROBES_A;
            this.legs = ModRegistry.CAMR_LEGGINGS;
            this.legs_a = ModRegistry.CAMR_LEGGINGS_A;
            this.feet = ModRegistry.CAMR_BOOTS;
            this.feet_a = ModRegistry.CAMR_BOOTS_A;
        }
    }

    public static class MattArmor extends ArmorSet {
        public MattArmor() {
            this.name = "matt_armor";
            this.head = ModRegistry.MATT_HOOD;
            this.head_a = ModRegistry.MATT_HOOD_A;
            this.chest = ModRegistry.MATT_ROBES;
            this.chest_a = ModRegistry.MATT_ROBES_A;
            this.legs = ModRegistry.MATT_LEGGINGS;
            this.legs_a = ModRegistry.MATT_LEGGINGS_A;
            this.feet = ModRegistry.MATT_BOOTS;
            this.feet_a = ModRegistry.MATT_BOOTS_A;
        }
    }

    public static class AdamArmor extends ArmorSet {
        public AdamArmor() {
            this.name = "adam_armor";
            this.head = ModRegistry.ADAM_HOOD;
            this.head_a = ModRegistry.ADAM_HOOD_A;
            this.chest = ModRegistry.ADAM_ROBES;
            this.chest_a = ModRegistry.ADAM_ROBES_A;
            this.legs = ModRegistry.ADAM_LEGGINGS;
            this.legs_a = ModRegistry.ADAM_LEGGINGS_A;
            this.feet = ModRegistry.ADAM_BOOTS;
            this.feet_a = ModRegistry.ADAM_BOOTS_A;
        }
    }

    public Item[] getHat() {
        Item[] array = { head.get(), head_a.get() };
        return array;
    }

    public Item[] getChest() {
        Item[] array = { chest.get(), chest_a.get() };
        return array;
    }

    public Item[] getLegs() {
        Item[] array = { legs.get(), legs_a.get() };
        return array;
    }

    public Item[] getBoots() {
        Item[] array = { feet.get(), feet_a.get() };
        return array;
    }

    public Item[] getArmorFromSlot(EquipmentSlot slot) {
        return switch (slot) {
            case CHEST -> getChest();
            case LEGS -> getLegs();
            case FEET -> getBoots();
            default -> getHat();
        };
    }

    public String getTranslationKey() {
        return "adamsarsplus.armor_set." + this.name;
    }
}
