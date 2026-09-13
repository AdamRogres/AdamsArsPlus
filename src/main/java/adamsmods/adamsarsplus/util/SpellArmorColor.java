package adamsmods.adamsarsplus.util;

import net.minecraft.world.item.DyeColor;

public final class SpellArmorColor {
    private SpellArmorColor() {}

    /** Nearest available armor dye by squared RGB distance; alpha is ignored. */
    public static DyeColor closestDyeColor(int rgb) {
        DyeColor closest = DyeColor.WHITE;
        int minimum = Integer.MAX_VALUE;
        for (DyeColor dye : DyeColor.values()) {
            int color = dye.getTextureDiffuseColor();
            int red = ((rgb >> 16) & 255) - ((color >> 16) & 255);
            int green = ((rgb >> 8) & 255) - ((color >> 8) & 255);
            int blue = (rgb & 255) - (color & 255);
            int distance = red * red + green * green + blue * blue;
            if (distance < minimum) {
                minimum = distance;
                closest = dye;
            }
        }
        return closest;
    }
}
