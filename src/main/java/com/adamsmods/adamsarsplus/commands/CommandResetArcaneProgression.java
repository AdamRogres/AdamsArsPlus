package com.adamsmods.adamsarsplus.commands;

import com.adamsmods.adamsarsplus.capabilities.ArcaneLevels;
import com.adamsmods.adamsarsplus.capabilities.ArcaneLevelsAttacher;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import java.util.Objects;

public class CommandResetArcaneProgression {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("reset-arcane-levels").
                requires(sender -> sender.hasPermission(2))
                .executes(context -> resetsProgression(context.getSource())));
    }

    private static int resetsProgression(CommandSourceStack source) {
        if (Objects.requireNonNull(source.getPlayer()).getCapability(ArcaneLevelsAttacher.ArcaneLevelsProvider.PLAYER_LEVEL).isPresent()) {
            Objects.requireNonNull(source.getPlayer()).getCapability(ArcaneLevelsAttacher.ArcaneLevelsProvider.PLAYER_LEVEL).ifPresent(ArcaneLevels::RESET_PLAYER);
            return 1;
        }
        return 0;
    }
}