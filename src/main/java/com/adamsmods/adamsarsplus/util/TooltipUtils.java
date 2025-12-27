package com.adamsmods.adamsarsplus.util;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;

public class TooltipUtils {
    public static Component getShiftInfoTooltip(String type) {
        Component shift = Component.literal("SHIFT").withStyle(ChatFormatting.AQUA);
        return Component.translatable("adamsarsplus." + type + ".shift_info", shift).withStyle(ChatFormatting.GRAY);
    }

    public static void addOnShift(List<Component> tooltip, Runnable lambda, String type) {
        if (Screen.hasShiftDown()) {
            lambda.run();
        } else {
            tooltip.add(getShiftInfoTooltip(type));
        }
    }
}
