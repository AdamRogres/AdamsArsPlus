package com.adamsmods.adamsarsplus.registry;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.block.ModBlocks;
import com.adamsmods.adamsarsplus.block.tile.DomainShellTile;
import com.adamsmods.adamsarsplus.entities.AdamsModEntities;
import com.adamsmods.adamsarsplus.entities.BladeProjectile;
import com.adamsmods.adamsarsplus.entities.client.*;
import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.client.renderer.entity.*;
import com.hollingsworth.arsnouveau.client.renderer.entity.familiar.*;
import com.hollingsworth.arsnouveau.client.renderer.tile.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static com.adamsmods.adamsarsplus.registry.ModRegistry.*;
import static com.hollingsworth.arsnouveau.client.registry.ClientHandler.colorFromArmor;


@SuppressWarnings("unchecked")
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = AdamsArsPlus.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@OnlyIn(Dist.CLIENT)
public class AdamClientHandler {
    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(AdamsModEntities.DOMAIN_SPELL.get(),
                renderManager -> new RenderBlank(renderManager, new ResourceLocation(ArsNouveau.MODID, "textures/entity/spell_proj.png")));
        event.registerEntityRenderer(AdamsModEntities.DETONATE_SPELL.get(),
                renderManager -> new RenderBlank(renderManager, new ResourceLocation(ArsNouveau.MODID, "textures/entity/spell_proj.png")));
        event.registerEntityRenderer(AdamsModEntities.DIVINE_SMITE.get(), DivineSmiteRenderer::new);
        event.registerEntityRenderer(AdamsModEntities.METEOR_SPELL.get(), MeteorRenderer::new);
        event.registerEntityRenderer(AdamsModEntities.BLADE_PROJ.get(), BladeRenderer::new);
        //event.registerEntityRenderer(AdamsModEntities.BLADE_PROJ.get(), TempRenderer::new);
        event.registerBlockEntityRenderer(ModBlocks.AUTO_TURRET_BLOCK_TILE.get(), AutoTurretRenderer::new);
    }

    public static void init(final FMLClientSetupEvent evt) {

        evt.enqueueWork(() -> {

        });
    }

    @SubscribeEvent
    public static void initBlockColors(final RegisterColorHandlersEvent.Block event) {
        event.register((state, reader, pos, tIndex) ->
                reader != null && pos != null && reader.getBlockEntity(pos) instanceof DomainShellTile domainShellTile
                        ? domainShellTile.color.getColor() : -1, ModBlocks.DOMAIN_SHELL_BLOCK.get());
    }

    @SubscribeEvent
    public static void initItemColors(final RegisterColorHandlersEvent.Item event){
        // Cade Armor
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                CADE_BOOTS.get());
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                CADE_LEGGINGS.get());
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                CADE_ROBES.get());
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                CADE_HOOD.get());
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                CADE_BOOTS_A.get());
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                CADE_LEGGINGS_A.get());
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                CADE_ROBES_A.get());
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                CADE_HOOD_A.get());

        // Ryan Armor
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                RYAN_BOOTS.get());
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                RYAN_LEGGINGS.get());
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                RYAN_ROBES.get());
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                RYAN_HOOD.get());
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                RYAN_BOOTS_A.get());
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                RYAN_LEGGINGS_A.get());
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                RYAN_ROBES_A.get());
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                RYAN_HOOD_A.get());

        // Nick Armor
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                NICK_BOOTS.get());
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                NICK_LEGGINGS.get());
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                NICK_ROBES.get());
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                NICK_HOOD.get());
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                NICK_BOOTS_A.get());
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                NICK_LEGGINGS_A.get());
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                NICK_ROBES_A.get());
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                NICK_HOOD_A.get());

        // Cam Armor
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                CAMR_BOOTS.get());
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                CAMR_LEGGINGS.get());
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                CAMR_ROBES.get());
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                CAMR_HOOD.get());
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                CAMR_BOOTS_A.get());
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                CAMR_LEGGINGS_A.get());
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                CAMR_ROBES_A.get());
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                CAMR_HOOD_A.get());

        // Matt Armor
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                MATT_BOOTS.get());
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                MATT_LEGGINGS.get());
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                MATT_ROBES.get());
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                MATT_HOOD.get());
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                MATT_BOOTS_A.get());
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                MATT_LEGGINGS_A.get());
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                MATT_ROBES_A.get());
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                MATT_HOOD_A.get());

        // Adam Armor
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                ADAM_BOOTS.get());
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                ADAM_LEGGINGS.get());
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                ADAM_ROBES.get());
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                ADAM_HOOD.get());
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                ADAM_BOOTS_A.get());
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                ADAM_LEGGINGS_A.get());
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                ADAM_ROBES_A.get());
        event.register((stack, color) -> color > 0 ? -1 :
                        colorFromArmor(stack),
                ADAM_HOOD_A.get());

    }
}
