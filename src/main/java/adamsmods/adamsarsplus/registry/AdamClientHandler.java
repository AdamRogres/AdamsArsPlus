package adamsmods.adamsarsplus.registry;

import adamsmods.adamsarsplus.AdamsArsPlus;
import adamsmods.adamsarsplus.client.entities.*;
import adamsmods.adamsarsplus.common.blocks.AutoTurretTile;
import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.client.renderer.entity.RenderBlank;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

import static adamsmods.adamsarsplus.registry.ModEntities.*;
import static adamsmods.adamsarsplus.registry.ModItems.*;

@SuppressWarnings("unchecked")
@EventBusSubscriber(value = Dist.CLIENT, modid = AdamsArsPlus.MODID)
@OnlyIn(Dist.CLIENT)
public class AdamClientHandler {
    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        // Mages
        event.registerEntityRenderer(RYAN_ENTITY.get(), RyanRenderer::new);
        event.registerEntityRenderer(CADE_ENTITY.get(), CadeRenderer::new);
        event.registerEntityRenderer(NICK_ENTITY.get(), NickRenderer::new);
        event.registerEntityRenderer(CAM_ENTITY.get(), CamRenderer::new);
        event.registerEntityRenderer(MATT_ENTITY.get(), MattRenderer::new);
        event.registerEntityRenderer(JOSH_ENTITY.get(), JoshRenderer::new);
        event.registerEntityRenderer(ADAM_ENTITY.get(), AdamRenderer::new);

        event.registerEntityRenderer(MAGE_ENTITY.get(), MageRenderer::new);
        event.registerEntityRenderer(FLAME_MAGE_ENTITY.get(), MageRenderer::new);
        event.registerEntityRenderer(FROST_MAGE_ENTITY.get(), MageRenderer::new);
        event.registerEntityRenderer(EARTH_MAGE_ENTITY.get(), MageRenderer::new);
        event.registerEntityRenderer(LIGHTNING_MAGE_ENTITY.get(), MageRenderer::new);
        event.registerEntityRenderer(HOLY_MAGE_ENTITY.get(), MageRenderer::new);
        event.registerEntityRenderer(VOID_MAGE_ENTITY.get(), MageRenderer::new);

        event.registerEntityRenderer(MAGE_KNIGHT.get(), MageKnightRenderer::new);
        event.registerEntityRenderer(FLAME_KNIGHT.get(), MageKnightRenderer::new);
        event.registerEntityRenderer(FROST_KNIGHT.get(), MageKnightRenderer::new);
        event.registerEntityRenderer(EARTH_KNIGHT.get(), MageKnightRenderer::new);
        event.registerEntityRenderer(LIGHTNING_KNIGHT.get(), MageKnightRenderer::new);
        event.registerEntityRenderer(HOLY_KNIGHT.get(), MageKnightRenderer::new);
        event.registerEntityRenderer(VOID_KNIGHT.get(), MageKnightRenderer::new);

        // Ten Shadows
        event.registerEntityRenderer(DIVINE_DOG.get(), DDogRenderer::new);
        event.registerEntityRenderer(NUE.get(), NueRenderer::new);
        event.registerEntityRenderer(RABBIT_ESCAPE.get(), RabbitERenderer::new);
        event.registerEntityRenderer(ROUND_DEER.get(), RDeerRenderer::new);
        event.registerEntityRenderer(MAHORAGA.get(), MahoragaRenderer::new);

        // Misc
        event.registerEntityRenderer(FIRE_ENTITY.get(), FireRenderer::new);
        event.registerEntityRenderer(SUMMON_SKELETON_M.get(), RenderSummonedSkeletonM::new);
        event.registerEntityRenderer(TERRA_ENTITY.get(), TerraprismaRenderer::new);
        event.registerEntityRenderer(DOMAIN_SPELL.get(),
                renderManager -> new RenderBlank(renderManager, ArsNouveau.prefix("textures/entity/spell_proj.png")));
        event.registerEntityRenderer(DETONATE_SPELL.get(),
                renderManager -> new RenderBlank(renderManager, ArsNouveau.prefix( "textures/entity/spell_proj.png")));
        event.registerEntityRenderer(DIVINE_SMITE.get(), DivineSmiteRenderer::new);
        event.registerEntityRenderer(METEOR_SPELL.get(), MeteorRenderer::new);
        event.registerEntityRenderer(BLADE_PROJ.get(), BladeRenderer::new);
        event.registerEntityRenderer(EYE_OF_SOUL.get(), SoulEyeRenderer::new);

        // Tiles
        event.registerBlockEntityRenderer(ModBlocks.AUTO_TURRET_BLOCK_TILE.get(), AutoTurretRenderer::new);

    }

    @SubscribeEvent
    public static void init(final FMLClientSetupEvent evt) {

        evt.enqueueWork(() -> {

        });
    }

    @SubscribeEvent
    public static void initItemColors(final RegisterColorHandlersEvent.Item event) {

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

        // RYAN Armor
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

        // NICK Armor
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

        // CAMR Armor
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

        // MATT Armor
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

        // ADAM Armor
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

    public static int colorFromArmor(ItemStack stack) {
        DyeColor color = stack.getOrDefault(DataComponents.BASE_COLOR, DyeColor.PURPLE);
        return FastColor.ABGR32.opaque(color.getTextColor());
    }
}
