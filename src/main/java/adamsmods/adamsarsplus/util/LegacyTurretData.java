package adamsmods.adamsarsplus.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

import java.util.function.UnaryOperator;

/** Converts pre-1.21 turret NBT without modifying the structure template. */
public final class LegacyTurretData {
    private LegacyTurretData() {}

    public static CompoundTag upgrade(CompoundTag tag, UnaryOperator<String> glyphIds) {
        // Current saves take precedence, including when a legacy key remains present.
        if (tag.contains("spell_caster") || !tag.contains("ars_nouveau:turret_caster", Tag.TAG_COMPOUND)) {
            return tag;
        }

        CompoundTag legacy = tag.getCompound("ars_nouveau:turret_caster");
        CompoundTag caster = new CompoundTag();
        caster.putInt("current_slot", legacy.getInt("current_slot"));
        caster.putString("flavor_text", legacy.getString("flavor"));
        caster.putString("hidden_text", legacy.getString("hidden_recipe"));
        caster.putBoolean("is_hidden", legacy.getBoolean("is_hidden"));
        caster.putInt("max_slots", Math.max(1, legacy.getInt("spell_count")));

        CompoundTag spells = new CompoundTag();
        CompoundTag oldSpells = legacy.getCompound("spells");
        for (String key : oldSpells.getAllKeys()) {
            if (!key.matches("spell[0-9]+") || !oldSpells.contains(key, Tag.TAG_COMPOUND)) continue;
            CompoundTag oldSpell = oldSpells.getCompound(key);
            CompoundTag spell = new CompoundTag();
            spell.putString("name", oldSpell.getString("name"));

            CompoundTag oldRecipe = oldSpell.getCompound("recipe");
            ListTag recipe = new ListTag();
            for (int i = 0; i < oldRecipe.getInt("size"); i++) {
                recipe.add(StringTag.valueOf(glyphIds.apply(oldRecipe.getString("part" + i))));
            }
            spell.put("recipe", recipe);

            CompoundTag color = oldSpell.getCompound("spellColor").copy();
            color.putString("id", color.contains("type", Tag.TAG_STRING)
                    ? color.getString("type") : "ars_nouveau:constant");
            color.remove("type");
            if (!color.contains("r")) color.putInt("r", 255);
            if (!color.contains("g")) color.putInt("g", 25);
            if (!color.contains("b")) color.putInt("b", 180);
            spell.put("color", color);

            CompoundTag sound = oldSpell.getCompound("sound").copy();
            if (sound.contains("soundTag", Tag.TAG_COMPOUND)) {
                sound.put("sound", sound.getCompound("soundTag").copy());
                sound.remove("soundTag");
            }
            spell.put("sound", sound);
            spells.put(key.substring("spell".length()), spell);
        }
        caster.put("spells", spells);
        CompoundTag upgraded = tag.copy();
        upgraded.put("spell_caster", caster);
        return upgraded;
    }
}
