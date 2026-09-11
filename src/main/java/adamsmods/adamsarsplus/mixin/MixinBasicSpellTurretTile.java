package adamsmods.adamsarsplus.mixin;

import adamsmods.adamsarsplus.ArsNouveauRegistry;
import adamsmods.adamsarsplus.util.LegacyTurretData;
import com.hollingsworth.arsnouveau.api.registry.GlyphRegistry;
import com.hollingsworth.arsnouveau.common.block.tile.BasicSpellTurretTile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(BasicSpellTurretTile.class)
public abstract class MixinBasicSpellTurretTile {
    // Covers basic, rotating and Auto Turrets before Ars Nouveau decodes their spells.
    @ModifyVariable(method = "loadAdditional", at = @At("HEAD"), argsOnly = true, ordinal = 0, remap = false)
    private CompoundTag adamsarsplus$upgradeLegacyCaster(CompoundTag tag) {
        return LegacyTurretData.upgrade(tag, MixinBasicSpellTurretTile::adamsarsplus$resolveLegacyGlyph);
    }

    @Unique
    private static String adamsarsplus$resolveLegacyGlyph(String name) {
        ResourceLocation id = ResourceLocation.tryParse(name);
        if (id == null || GlyphRegistry.getSpellPart(id) != null) return name;
        // The port currently registers these addon glyphs under Ars Nouveau's namespace.
        if (id.getNamespace().equals("adamsarsplus")) {
            return ArsNouveauRegistry.registeredSpells.stream()
                    .map(part -> part.getRegistryName())
                    .filter(current -> current.getPath().equals(id.getPath()))
                    .map(ResourceLocation::toString)
                    .findFirst().orElse(name);
        }
        return name;
    }
}
