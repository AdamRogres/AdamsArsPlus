package adamsmods.adamsarsplus.util;

import net.minecraft.world.item.DyeColor;

public class SpellArmorColorTest {
    public static void main(String[] args) {
        for (DyeColor dye : DyeColor.values()) {
            int color = dye.getTextureDiffuseColor();
            if (SpellArmorColor.closestDyeColor(color) != dye
                    || SpellArmorColor.closestDyeColor(color | 0xff000000) != dye) {
                throw new AssertionError("Exact dye match failed: " + dye);
            }
        }
        if (SpellArmorColor.closestDyeColor(0xffffff) != DyeColor.WHITE
                || SpellArmorColor.closestDyeColor(0) != DyeColor.BLACK) {
            throw new AssertionError("Neutral color matching failed");
        }
        System.out.println("PASS: all 16 armor dyes, alpha handling, white and black hue matching.");
    }
}
