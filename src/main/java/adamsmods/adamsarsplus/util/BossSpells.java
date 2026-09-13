package adamsmods.adamsarsplus.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import com.hollingsworth.arsnouveau.api.particle.timelines.ProjectileTimeline;
import com.hollingsworth.arsnouveau.api.registry.ParticleTimelineRegistry;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import org.slf4j.Logger;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/** Call after mod registries are ready, for example when a boss prepares an attack. */
public final class BossSpells {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String RESOURCE = "data/adamsarsplus/boss_spells/boss_spell_list.json";
    private static final Set<String> WARNED = ConcurrentHashMap.newKeySet();

    private BossSpells() {}

    private record Key(String name, String attack) {
        private static Key of(String name, String attack) {
            return new Key(normalize(name), normalize(attack));
        }
        private static String normalize(String value) {
            return value == null ? "" : value.strip().toLowerCase(Locale.ROOT);
        }
    }

    // Cache the exported strings, not mutable timelines shared by multiple bosses.
    private static class Entries {
        private static final Map<Key, String> VALUES = loadEntries();
    }

    /**
     * Finds a boss attack by name and attack (case-insensitive, ignoring outer spaces).
     * Decodes a fresh Spell, including its name and styles. Blank WIP entries,
     * missing keys and invalid exports return an empty Spell; check isEmpty()
     * before spawning an attack. Missing/invalid entries log once per key.
     * The bundled file is loaded once per game launch.
     */
    public static Spell getSpell(String name, String attack) {
        Key key = Key.of(name, attack);
        String encoded = Entries.VALUES.get(key);
        if (encoded == null) {
            warnOnce(key.toString(), "No boss spell found for " + key);
            return new Spell();
        }
        if (encoded.isBlank()) return new Spell();
        try {
            Spell spell = encoded.stripLeading().startsWith("{")
                    ? Spell.fromJson(encoded) : Spell.fromBinaryBase64(encoded.strip());
            return spell == null ? new Spell() : spell;
        } catch (RuntimeException exception) {
            warnOnce(key.toString(), "Cannot decode boss spell " + key + ": " + exception.getMessage());
            return new Spell();
        }
    }

    private static Map<Key, String> loadEntries() {
        Map<Key, String> entries = new HashMap<>();
        try (InputStream stream = BossSpells.class.getClassLoader().getResourceAsStream(RESOURCE)) {
            if (stream == null) throw new IllegalStateException("Missing " + RESOURCE);
            try (InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
                for (JsonElement element : JsonParser.parseReader(reader).getAsJsonObject().getAsJsonArray("bossSpells")) {
                    var entry = element.getAsJsonObject();
                    Key key = Key.of(entry.get("name").getAsString(), entry.get("attack").getAsString());
                    String encoded = entry.get("spell").getAsString();
                    if (entries.putIfAbsent(key, encoded) != null) {
                        warnOnce("duplicate:" + key, "Duplicate boss spell " + key + "; using the first entry.");
                    }
                }
            }
        } catch (Exception exception) {
            warnOnce("file", "Could not fully load " + RESOURCE + ": " + exception.getMessage());
        }
        return Map.copyOf(entries);
    }

    private static void warnOnce(String key, String message) {
        if (WARNED.add(key)) LOGGER.warn(message);
    }

    /**
     * Applies the exported spell's Projectile method style (also used by
     * DetonateProjectile). Set up the projectile's attack resolver first and
     * call this on the server before level.addFreshEntity(projectile).
     * This does not replace the attack recipe, caster, or current glyph index.
     */
    public static <T extends EntityProjectileSpell> T applyStyle(T projectile, Spell styleSource) {
        Objects.requireNonNull(styleSource, "styleSource");
        return applyStyle(projectile, styleSource.particleTimeline().get(ParticleTimelineRegistry.PROJECTILE_TIMELINE.get()));
    }

    /** Applies an explicitly selected Projectile timeline, including cast/impact sounds. */
    public static <T extends EntityProjectileSpell> T applyStyle(T projectile, ProjectileTimeline style) {
        Objects.requireNonNull(projectile, "projectile");
        Objects.requireNonNull(style, "style");
        if (projectile.level().isClientSide) {
            throw new IllegalStateException("Apply boss projectile styles on the server before spawning the entity");
        }
        SpellResolver original = Objects.requireNonNull(projectile.resolver(), "Set the projectile's attack resolver before applying its style");
        // Timeline properties are mutable. Detach the style from the source spell.
        var codec = ProjectileTimeline.CODEC.codec();
        ProjectileTimeline copy = codec.parse(JsonOps.INSTANCE,
                codec.encodeStart(JsonOps.INSTANCE, style).getOrThrow()).getOrThrow();
        Spell styled = original.spell.withTimeline(original.spell.particleTimeline()
                .put(ParticleTimelineRegistry.PROJECTILE_TIMELINE.get(), copy));
        int index = original.spellContext.getCurrentIndex();
        SpellContext context = original.spellContext.clone().withSpell(styled);
        context.setCurrentIndex(index);
        // Rebuild emitters and synchronize the styled resolver to tracking clients.
        projectile.setResolver(original.getNewResolver(context));
        return projectile;
    }
}
