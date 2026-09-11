package adamsmods.adamsarsplus.util;

import net.minecraft.nbt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.function.UnaryOperator;

/** Standalone regression check against the turret records in the shipped structures. */
public class LegacyTurretDataTest {
    public static void main(String[] args) throws Exception {
        int count = 0;
        var records = new ArrayList<CompoundTag>();
        try (var paths = Files.list(Path.of("src/main/resources/data/adamsarsplus/structure"))) {
            for (Path path : paths.filter(p -> p.toString().endsWith(".nbt")).toList()) {
                var template = NbtIo.readCompressed(path, NbtAccounter.unlimitedHeap());
                for (Tag block : template.getList("blocks", Tag.TAG_COMPOUND)) {
                    var nbt = ((CompoundTag) block).getCompound("nbt");
                    if (nbt.contains("ars_nouveau:turret_caster", Tag.TAG_COMPOUND)) records.add(nbt);
                }
            }
        }
        check(!records.isEmpty(), "No legacy fixtures found");
        for (CompoundTag original : records) {
            CompoundTag snapshot = original.copy();
            CompoundTag result = LegacyTurretData.upgrade(original, UnaryOperator.identity());
            check(original.equals(snapshot), "Input was mutated");
            check(result != original, "Legacy data was not upgraded");
            var oldCaster = original.getCompound("ars_nouveau:turret_caster");
            var caster = result.getCompound("spell_caster");
            check(caster.getInt("current_slot") == oldCaster.getInt("current_slot"), "Selected slot");
            check(caster.getInt("max_slots") == oldCaster.getInt("spell_count"), "Slot count");
            check(caster.getString("flavor_text").equals(oldCaster.getString("flavor")), "Flavor");
            for (String key : oldCaster.getCompound("spells").getAllKeys()) {
                var oldSpell = oldCaster.getCompound("spells").getCompound(key);
                var spell = caster.getCompound("spells").getCompound(key.substring(5));
                var recipe = spell.getList("recipe", Tag.TAG_STRING);
                var oldRecipe = oldSpell.getCompound("recipe");
                check(recipe.size() == oldRecipe.getInt("size"), "Recipe length");
                for (int i = 0; i < recipe.size(); i++) check(recipe.getString(i).equals(oldRecipe.getString("part" + i)), "Glyph order");
                for (String channel : new String[]{"r", "g", "b"}) check(spell.getCompound("color").getInt(channel) == oldSpell.getCompound("spellColor").getInt(channel), "Color");
                check(spell.getCompound("sound").getCompound("sound").equals(oldSpell.getCompound("sound").getCompound("soundTag")), "Sound");
                check(spell.getString("name").equals(oldSpell.getString("name")), "Spell name");
            }
            var withoutNewKey = result.copy(); withoutNewKey.remove("spell_caster");
            check(withoutNewKey.equals(original), "Other turret settings changed");
            check(LegacyTurretData.upgrade(result, s -> { throw new AssertionError("Modern data converted"); }) == result, "Modern precedence");
            count++;
        }
        CompoundTag empty = new CompoundTag();
        check(LegacyTurretData.upgrade(empty, UnaryOperator.identity()) == empty, "Unrelated data modified");
        System.out.println("PASS: " + count + " actual legacy turret records; recipes, colors, sounds, settings, input immutability and modern precedence.");
    }
    private static void check(boolean passed, String message) {
        if (!passed) throw new AssertionError(message);
    }
}
