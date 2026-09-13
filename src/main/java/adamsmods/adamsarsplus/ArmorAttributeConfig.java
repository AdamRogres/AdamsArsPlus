package adamsmods.adamsarsplus;

import net.minecraft.world.item.ArmorItem;
import net.neoforged.neoforge.common.ModConfigSpec;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;

/** Base equipment bonuses, independent of Ars perks and armor tiers. */
public final class ArmorAttributeConfig {
    private ArmorAttributeConfig() {}
    public static final Map<String, Settings> SETS = new LinkedHashMap<>();

    public record Settings(Map<ArmorItem.Type, ModConfigSpec.IntValue> armor,
                           ModConfigSpec.DoubleValue toughness,
                           ModConfigSpec.DoubleValue knockbackResistance) {}

    public static void define(ModConfigSpec.Builder builder) {
        builder.comment("Minecraft base bonuses for normal and awakened armor. Restart the world/server after editing.",
                "Armor values are per piece. Toughness and knockback resistance are granted by EACH equipped piece.",
                "These do not scale with Ars perk tiers. Minecraft's total attribute caps still apply.")
                .push("Minecraft Attributes");
        defineSet(builder, "cade", "an_cade", 3, 6, 5, 2, 2.0, 0.0);
        defineSet(builder, "ryan", "an_ryan", 3, 8, 6, 3, 3.0, 0.0);
        defineSet(builder, "nick", "an_nick", 4, 10, 7, 3, 4.0, (double) 0.05F);
        defineSet(builder, "cam", "an_camr", 4, 10, 7, 3, 4.0, 0.0);
        defineSet(builder, "matt", "an_matt", 6, 11, 8, 5, 5.0, (double) 0.1F);
        defineSet(builder, "adam", "an_adam", 7, 12, 9, 6, 6.0, (double) 0.1F);
        builder.pop();
    }

    private static void defineSet(ModConfigSpec.Builder builder, String name, String material,
                                  int helmet, int chestplate, int leggings, int boots,
                                  double toughness, double knockback) {
        builder.push(name);
        var armor = new EnumMap<ArmorItem.Type, ModConfigSpec.IntValue>(ArmorItem.Type.class);
        armor.put(ArmorItem.Type.HELMET, armorValue(builder, "helmet_armor", helmet));
        armor.put(ArmorItem.Type.CHESTPLATE, armorValue(builder, "chestplate_armor", chestplate));
        armor.put(ArmorItem.Type.LEGGINGS, armorValue(builder, "leggings_armor", leggings));
        armor.put(ArmorItem.Type.BOOTS, armorValue(builder, "boots_armor", boots));
        var tough = builder.comment("Armor toughness per equipped piece.").worldRestart()
                .defineInRange("armor_toughness", toughness, 0.0, 20.0);
        var resist = builder.comment("Knockback resistance per equipped piece. 0.1 = 10%; four pieces grant 40%.").worldRestart()
                .defineInRange("knockback_resistance", knockback, 0.0, 1.0);
        SETS.put(material, new Settings(Map.copyOf(armor), tough, resist));
        builder.pop();
    }

    private static ModConfigSpec.IntValue armorValue(ModConfigSpec.Builder builder, String key, int value) {
        return builder.comment("Armor points from this piece (2 points = one armor icon).").worldRestart()
                .defineInRange(key, value, 0, 30);
    }
}
