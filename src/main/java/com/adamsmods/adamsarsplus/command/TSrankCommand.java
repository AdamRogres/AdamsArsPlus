package com.adamsmods.adamsarsplus.command;

import com.adamsmods.adamsarsplus.registry.AdamCapabilityRegistry;
import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.sun.jdi.connect.Connector;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collection;

public class TSrankCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("adamsarsplus-tsrank").
                requires(sender -> sender.hasPermission(2))
                .executes(context -> setPlayers(context.getSource(), ImmutableList.of(context.getSource().getEntityOrException()), 0))
                .then(  Commands.argument("targets", EntityArgument.entities())
                        .executes(context -> setPlayers(context.getSource(), EntityArgument.getEntities(context, "targets"), 0))
                        .then(Commands.argument("Rank", IntegerArgumentType.integer())
                                .executes(context -> setPlayers(context.getSource(), EntityArgument.getEntities(context, "targets"), IntegerArgumentType.getInteger(context, "Rank"))))
                        ));
    }

    private static int setPlayers(CommandSourceStack source, Collection<? extends Entity> entities, int newRank) {
        for (Entity e : entities) {
            if (!(e instanceof LivingEntity))
                continue;

            AdamCapabilityRegistry.getTsTier((LivingEntity) e).ifPresent((pRank) -> {
                pRank.setTsTier(newRank);
            });

        }
        source.sendSuccess(() -> Component.translatable("Ten Shadows rank changed to: " + newRank), true);
        return 1;
    }



}
