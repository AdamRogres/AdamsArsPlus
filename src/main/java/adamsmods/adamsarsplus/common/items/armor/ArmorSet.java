package adamsmods.adamsarsplus.common.items.armor;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;

import static adamsmods.adamsarsplus.registry.ModItems.*;

public class ArmorSet {
    public String getName() {
        return name;
    }

    protected String name;
    protected DeferredHolder<Item, MageMagicArmor> head;
    protected DeferredHolder<Item, MageMagicArmor> chest;
    protected DeferredHolder<Item, MageMagicArmor> legs;
    protected DeferredHolder<Item, MageMagicArmor> feet;

    protected DeferredHolder<Item, MageMagicArmor> head_a;
    protected DeferredHolder<Item, MageMagicArmor> chest_a;
    protected DeferredHolder<Item, MageMagicArmor> legs_a;
    protected DeferredHolder<Item, MageMagicArmor> feet_a;

    static CadeArmor CADE_ARMORSET = new CadeArmor();
    static RyanArmor RYAN_ARMORSET = new RyanArmor();
    static NickArmor NICK_ARMORSET = new NickArmor();
    static CamArmor CAM_ARMORSET = new CamArmor();
    static MattArmor MATT_ARMORSET = new MattArmor();
    static AdamArmor ADAM_ARMORSET = new AdamArmor();

    public static class CadeArmor extends ArmorSet {
        public CadeArmor() {
            this.name = "cade_armor";
            this.head = CADE_HOOD;
            this.head_a = CADE_HOOD_A;
            this.chest = CADE_ROBES;
            this.chest_a = CADE_ROBES_A;
            this.legs = CADE_LEGGINGS;
            this.legs_a = CADE_LEGGINGS_A;
            this.feet = CADE_BOOTS;
            this.feet_a = CADE_BOOTS_A;
        }
    }

    public static class RyanArmor extends ArmorSet {
        public RyanArmor() {
            this.name = "ryan_armor";
            this.head = RYAN_HOOD;
            this.head_a = RYAN_HOOD_A;
            this.chest = RYAN_ROBES;
            this.chest_a = RYAN_ROBES_A;
            this.legs = RYAN_LEGGINGS;
            this.legs_a = RYAN_LEGGINGS_A;
            this.feet = RYAN_BOOTS;
            this.feet_a = RYAN_BOOTS_A;
        }
    }

    public static class NickArmor extends ArmorSet {
        public NickArmor() {
            this.name = "nick_armor";
            this.head = NICK_HOOD;
            this.head_a = NICK_HOOD_A;
            this.chest = NICK_ROBES;
            this.chest_a = NICK_ROBES_A;
            this.legs = NICK_LEGGINGS;
            this.legs_a = NICK_LEGGINGS_A;
            this.feet = NICK_BOOTS;
            this.feet_a = NICK_BOOTS_A;
        }
    }

    public static class CamArmor extends ArmorSet {
        public CamArmor() {
            this.name = "cam_armor";
            this.head = CAMR_HOOD;
            this.head_a = CAMR_HOOD_A;
            this.chest = CAMR_ROBES;
            this.chest_a = CAMR_ROBES_A;
            this.legs = CAMR_LEGGINGS;
            this.legs_a = CAMR_LEGGINGS_A;
            this.feet = CAMR_BOOTS;
            this.feet_a = CAMR_BOOTS_A;
        }
    }

    public static class MattArmor extends ArmorSet {
        public MattArmor() {
            this.name = "matt_armor";
            this.head = MATT_HOOD;
            this.head_a = MATT_HOOD_A;
            this.chest = MATT_ROBES;
            this.chest_a = MATT_ROBES_A;
            this.legs = MATT_LEGGINGS;
            this.legs_a = MATT_LEGGINGS_A;
            this.feet = MATT_BOOTS;
            this.feet_a = MATT_BOOTS_A;
        }
    }

    public static class AdamArmor extends ArmorSet {
        public AdamArmor() {
            this.name = "adam_armor";
            this.head = ADAM_HOOD;
            this.head_a = ADAM_HOOD_A;
            this.chest = ADAM_ROBES;
            this.chest_a = ADAM_ROBES_A;
            this.legs = ADAM_LEGGINGS;
            this.legs_a = ADAM_LEGGINGS_A;
            this.feet = ADAM_BOOTS;
            this.feet_a = ADAM_BOOTS_A;
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
