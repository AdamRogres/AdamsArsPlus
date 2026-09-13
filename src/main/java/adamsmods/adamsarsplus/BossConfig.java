package adamsmods.adamsarsplus;

import com.hollingsworth.arsnouveau.api.perk.PerkAttributes;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForgeMod;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/** Server-applied base attributes. Registry holders are only resolved after registration. */
public final class BossConfig {
    private BossConfig() {}

    public record AttributeOption(String key, Supplier<Holder<Attribute>> attribute,
                                  double defaultValue, double minimum, double maximum, String description) {}

    public static final List<AttributeOption> ATTRIBUTES = List.of(
        new AttributeOption("max_health", () -> Attributes.MAX_HEALTH, 20, 1, 1000000, "Maximum health in health points (2 = one heart). Minecraft or other mods may cap the effective value; vanilla caps at 1024."),
        new AttributeOption("armor", () -> Attributes.ARMOR, 0, 0, 30, "Base armor, before equipment and effects."),
        new AttributeOption("armor_toughness", () -> Attributes.ARMOR_TOUGHNESS, 0, 0, 20, "Base armor toughness."),
        new AttributeOption("attack_damage", () -> Attributes.ATTACK_DAMAGE, 2, 0, 2048, "Base melee damage. Scripted attacks may apply their own multipliers."),
        new AttributeOption("attack_knockback", () -> Attributes.ATTACK_KNOCKBACK, 0, 0, 5, "Melee attack knockback strength."),
        new AttributeOption("movement_speed", () -> Attributes.MOVEMENT_SPEED, 0.7, 0, 1024, "Base ground movement speed; AI movement multipliers still apply."),
        new AttributeOption("flying_speed", () -> Attributes.FLYING_SPEED, 0.4, 0, 1024, "Base flying speed, where used by the movement controller."),
        new AttributeOption("follow_range", () -> Attributes.FOLLOW_RANGE, 32, 0, 2048, "Target tracking range in blocks. Individual AI goals may impose additional limits."),
        new AttributeOption("knockback_resistance", () -> Attributes.KNOCKBACK_RESISTANCE, 0, 0, 1, "Knockback resistance, from 0 to 1."),
        new AttributeOption("max_absorption", () -> Attributes.MAX_ABSORPTION, 0, 0, 2048, "Maximum absorption; does not itself grant absorption hearts."),
        new AttributeOption("step_height", () -> Attributes.STEP_HEIGHT, 0.6, 0, 10, "Step height in blocks."),
        new AttributeOption("scale", () -> Attributes.SCALE, 1, 0.0625, 16, "Entity size multiplier."),
        new AttributeOption("gravity", () -> Attributes.GRAVITY, 0.08, -1, 1, "Gravity acceleration; ignored while the entity disables gravity."),
        new AttributeOption("safe_fall_distance", () -> Attributes.SAFE_FALL_DISTANCE, 3, -1024, 1024, "Fall distance in blocks before fall damage."),
        new AttributeOption("fall_damage_multiplier", () -> Attributes.FALL_DAMAGE_MULTIPLIER, 1, 0, 100, "Fall damage multiplier, where the boss permits fall damage."),
        new AttributeOption("jump_strength", () -> Attributes.JUMP_STRENGTH, 0.42F, 0, 32, "Jump strength."),
        new AttributeOption("oxygen_bonus", () -> Attributes.OXYGEN_BONUS, 0, 0, 1024, "Underwater breathing bonus."),
        new AttributeOption("burning_time", () -> Attributes.BURNING_TIME, 1, 0, 1024, "Burn duration multiplier; does not remove fire immunity."),
        new AttributeOption("explosion_knockback_resistance", () -> Attributes.EXPLOSION_KNOCKBACK_RESISTANCE, 0, 0, 1, "Explosion knockback resistance, from 0 to 1."),
        new AttributeOption("water_movement_efficiency", () -> Attributes.WATER_MOVEMENT_EFFICIENCY, 0, 0, 1, "Water movement efficiency."),
        new AttributeOption("movement_efficiency", () -> Attributes.MOVEMENT_EFFICIENCY, 0, 0, 1, "Movement efficiency over slowing terrain."),
        new AttributeOption("swim_speed", () -> NeoForgeMod.SWIM_SPEED, 1, 0, 1024, "Swimming speed multiplier."),
        new AttributeOption("nametag_distance", () -> NeoForgeMod.NAMETAG_DISTANCE, 64, 0, 64, "Name tag visibility distance."),
        new AttributeOption("spell_damage_bonus", () -> PerkAttributes.SPELL_DAMAGE_BONUS, 0, 0, 10000, "Flat bonus to each Ars damage-glyph hit. Does not increase radius, duration, or amplification."));

    // Preserve the current base stats, including Cade's final flying-speed override.
    private static final Map<String, Map<String, Double>> DEFAULTS = Map.ofEntries(
        Map.entry("ryan", Map.ofEntries(Map.entry("max_health", (double) 500D), Map.entry("armor", (double) 15D), Map.entry("attack_damage", (double) 12.0F), Map.entry("movement_speed", (double) 0.5F), Map.entry("flying_speed", (double) 0.5F), Map.entry("follow_range", (double) 70.0F), Map.entry("attack_knockback", (double) 0.7F), Map.entry("knockback_resistance", (double) 1.0F), Map.entry("armor_toughness", (double) 10D))),
        Map.entry("cade", Map.ofEntries(Map.entry("max_health", (double) 325D), Map.entry("armor", (double) 12D), Map.entry("attack_damage", (double) 6.0F), Map.entry("movement_speed", (double) 0.5F), Map.entry("flying_speed", (double) 0.2F), Map.entry("follow_range", (double) 70.0F), Map.entry("attack_knockback", (double) 1.0F), Map.entry("knockback_resistance", (double) 0.2F), Map.entry("armor_toughness", (double) 5D))),
        Map.entry("nick", Map.ofEntries(Map.entry("max_health", (double) 900D), Map.entry("armor", (double) 17D), Map.entry("attack_damage", (double) 15.0F), Map.entry("movement_speed", (double) 0.4F), Map.entry("flying_speed", (double) 0.4F), Map.entry("follow_range", (double) 70.0F), Map.entry("attack_knockback", (double) 1.0F), Map.entry("knockback_resistance", (double) 0.9F), Map.entry("armor_toughness", (double) 10D))),
        Map.entry("cam", Map.ofEntries(Map.entry("max_health", (double) 925D), Map.entry("armor", (double) 20D), Map.entry("attack_damage", (double) 15.0F), Map.entry("movement_speed", (double) 0.75F), Map.entry("flying_speed", (double) 0.75F), Map.entry("follow_range", (double) 70.0F), Map.entry("attack_knockback", (double) 1.0F), Map.entry("knockback_resistance", (double) 0.5F), Map.entry("armor_toughness", (double) 12D))),
        Map.entry("matt", Map.ofEntries(Map.entry("max_health", (double) 1225D), Map.entry("armor", (double) 23D), Map.entry("attack_damage", (double) 20.0F), Map.entry("movement_speed", (double) 0.4F), Map.entry("flying_speed", (double) 0.4F), Map.entry("follow_range", (double) 150.0F), Map.entry("attack_knockback", (double) 1.5F), Map.entry("knockback_resistance", (double) 0.8F), Map.entry("armor_toughness", (double) 15D))),
        Map.entry("josh", Map.ofEntries(Map.entry("max_health", (double) 1800D), Map.entry("armor", (double) 23D), Map.entry("attack_damage", (double) 20.0F), Map.entry("movement_speed", (double) 0.4F), Map.entry("flying_speed", (double) 0.3F), Map.entry("follow_range", (double) 150.0F), Map.entry("attack_knockback", (double) 1.5F), Map.entry("knockback_resistance", (double) 1F), Map.entry("armor_toughness", (double) 15D))),
        Map.entry("adam", Map.ofEntries(Map.entry("max_health", (double) 2000D), Map.entry("armor", (double) 23D), Map.entry("attack_damage", (double) 20.0F), Map.entry("movement_speed", (double) 0.8F), Map.entry("flying_speed", (double) 0.8F), Map.entry("follow_range", (double) 150.0F), Map.entry("attack_knockback", (double) 1.5F), Map.entry("knockback_resistance", (double) 0.8F), Map.entry("armor_toughness", (double) 15D))),
        Map.entry("mahoraga", Map.ofEntries(Map.entry("max_health", (double) 500D), Map.entry("attack_damage", (double) 10.0F), Map.entry("movement_speed", (double) 0.35F), Map.entry("follow_range", (double) 90.0F), Map.entry("attack_knockback", (double) 1.0F), Map.entry("armor", (double) 20), Map.entry("armor_toughness", (double) 8), Map.entry("knockback_resistance", (double) 0.7))));

    public static final Map<String, Settings> BOSSES = new LinkedHashMap<>();

    public static void define(ModConfigSpec.Builder builder) {
        builder.comment("Per-boss base attributes. Applied on the server when a boss spawns or loads.",
                "Restart the world/server after editing. Existing bosses retain their health percentage.",
                "Equipment, effects, scripted AI and Minecraft attribute limits still apply.")
                .push("Boss Configs");
        for (String name : List.of("ryan", "cade", "nick", "cam", "matt", "josh", "adam", "mahoraga")) {
            builder.push(name);
            Map<String, ModConfigSpec.DoubleValue> values = new LinkedHashMap<>();
            for (AttributeOption option : ATTRIBUTES) {
                // Mahoraga has no flying-speed attribute.
                if (name.equals("mahoraga") && option.key().equals("flying_speed")) continue;
                double value = DEFAULTS.get(name).getOrDefault(option.key(), option.defaultValue());
                values.put(option.key(), builder.comment(option.description()).worldRestart()
                        .defineInRange(option.key(), value, option.minimum(), option.maximum()));
            }
            var multiplier = builder.comment("Scales Ars spell damage after the flat bonus: 0.5 = half, 1 = unchanged, 2 = double.",
                    "Only affects spell hits routed through Ars SpellDamageEvent; not melee, fire ticks, or summoned creatures.")
                    .worldRestart().defineInRange("spell_damage_multiplier", 1.0, 0.0, 1000.0);
            BOSSES.put(name, new Settings(Map.copyOf(values), multiplier));
            builder.pop();
        }
        builder.pop();
    }

    public record Settings(Map<String, ModConfigSpec.DoubleValue> attributes,
                           ModConfigSpec.DoubleValue spellDamageMultiplier) {}
}
