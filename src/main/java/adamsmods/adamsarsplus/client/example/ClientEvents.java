package adamsmods.adamsarsplus.client.example;

import adamsmods.adamsarsplus.AdamsArsPlus;
import adamsmods.adamsarsplus.client.example.firenando.FirenandoFamiliarRenderer;
import adamsmods.adamsarsplus.client.example.firenando.FirenandoRenderer;
import adamsmods.adamsarsplus.client.example.flashjack.FlashJackFamiliarRenderer;
import adamsmods.adamsarsplus.client.example.flashjack.FlashJackRenderer;
import adamsmods.adamsarsplus.client.example.mages.MageRenderer;
import adamsmods.adamsarsplus.client.example.mermaid.MermaidRenderer;
import adamsmods.adamsarsplus.client.example.particle.SparkParticle;
import adamsmods.adamsarsplus.client.example.particle.VenomParticle;
import adamsmods.adamsarsplus.client.example.summons.DireWolfRenderer;
import adamsmods.adamsarsplus.common.CasterHolderContainer;
import adamsmods.adamsarsplus.common.CurioHolderContainer;
import adamsmods.adamsarsplus.common.entity.example.spells.EntityLerpedProjectile;
import adamsmods.adamsarsplus.common.items.example.CurioHolder;
import adamsmods.adamsarsplus.network.OpenCurioBagPacket;
import adamsmods.adamsarsplus.registry.*;
import adamsmods.adamsarsplus.registry.example.ModTiles;
import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.api.item.inv.SlotReference;
import com.hollingsworth.arsnouveau.client.particle.WrappedProvider;
import com.hollingsworth.arsnouveau.client.renderer.entity.RenderSpell;
import com.hollingsworth.arsnouveau.client.renderer.entity.RenderSummonSkeleton;
import com.hollingsworth.arsnouveau.client.renderer.entity.WealdWalkerModel;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import com.hollingsworth.arsnouveau.common.network.Networking;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import static adamsmods.adamsarsplus.AdamsArsPlus.prefix;

@EventBusSubscriber(modid = AdamsArsPlus.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientEvents {

    static final ResourceLocation SkeletalHorseTexture = ResourceLocation.withDefaultNamespace("textures/entity/horse/horse_skeleton.png");
    static final ResourceLocation VhexTexture = prefix("textures/entity/vhex.png");

    public static final KeyMapping CURIO_BAG_KEYBINDING = new KeyMapping("key.ars_elemental.open_pouch", GLFW.GLFW_KEY_J, "key.category.ars_nouveau.general");

    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {

    }


    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.SPARK.get(), SparkParticle::factory);
        event.registerSpriteSet(ModParticles.VENOM.get(), VenomParticle::factory);
        event.registerSpriteSet(ModParticles.SPARK_2.get(), (sprites -> new WrappedProvider(ModParticles.SPARK.get(), SparkParticle::factory)));
        event.registerSpriteSet(ModParticles.VENOM_2.get(), (sprites -> new WrappedProvider(ModParticles.VENOM.get(), VenomParticle::factory)));
    }

    @SubscribeEvent
    public static void bindRenderers(final EntityRenderersEvent.RegisterRenderers event) {

        event.registerBlockEntityRenderer(ModTiles.ADVANCED_PRISM.get(), PrismRenderer::new);
        event.registerEntityRenderer(ModEntities.SIREN_ENTITY.get(), MermaidRenderer::new);
        event.registerEntityRenderer(ModEntities.SIREN_FAMILIAR.get(), MermaidRenderer::new);

        event.registerEntityRenderer(ModEntities.FIRENANDO_ENTITY.get(), FirenandoRenderer::new);
        event.registerEntityRenderer(ModEntities.FIRENANDO_FAMILIAR.get(), FirenandoFamiliarRenderer::new);

        event.registerEntityRenderer(ModEntities.FLASHJACK_ENTITY.get(), FlashJackRenderer::new);
        event.registerEntityRenderer(ModEntities.FLASHJACK_FAMILIAR.get(), FlashJackFamiliarRenderer::new);


        event.registerEntityRenderer(ModEntities.SKELEHORSE_SUMMON.get(), manager -> new UndeadHorseRenderer(manager, ModelLayers.SKELETON_HORSE) {
            @Override
            public @NotNull ResourceLocation getTextureLocation(@NotNull AbstractHorse pEntity) {
                return SkeletalHorseTexture;
            }
        });

        event.registerEntityRenderer(ModEntities.CAMEL_SUMMON.get(), manager -> new CamelRenderer(manager, ModelLayers.CAMEL));

        event.registerEntityRenderer(ModEntities.DIREWOLF_SUMMON.get(), DireWolfRenderer::new);
        event.registerEntityRenderer(ModEntities.WSKELETON_SUMMON.get(), renderManagerIn -> new RenderSummonSkeleton(renderManagerIn) {
            @Override
            public @NotNull ResourceLocation getTextureLocation(@NotNull AbstractSkeleton entity) {
                return ResourceLocation.withDefaultNamespace("textures/entity/skeleton/wither_skeleton.png");
            }
        });
        event.registerEntityRenderer(ModEntities.DOLPHIN_SUMMON.get(), DolphinRenderer::new);
        event.registerEntityRenderer(ModEntities.STRIDER_SUMMON.get(), StriderRenderer::new);
        event.registerEntityRenderer(ModEntities.VHEX_SUMMON.get(), manager -> new VexRenderer(manager) {
            @Override
            public @NotNull ResourceLocation getTextureLocation(@NotNull Vex p_110775_1_) {
                return VhexTexture;
            }
        });

        event.registerEntityRenderer(ModEntities.FLASHING_WEALD_WALKER.get(), v -> new GeoEntityRenderer<>(v, new WealdWalkerModel<>("flashing_weald")));

        event.registerEntityRenderer(ModEntities.FIRE_MAGE.get(), MageRenderer::new);
        event.registerEntityRenderer(ModEntities.WATER_MAGE.get(), MageRenderer::new);
        event.registerEntityRenderer(ModEntities.AIR_MAGE.get(), MageRenderer::new);
        event.registerEntityRenderer(ModEntities.EARTH_MAGE.get(), MageRenderer::new);

        event.registerEntityRenderer(ModEntities.LINGER_MAGNET.get(), ClientEvents::projectileRender);
        event.registerEntityRenderer(ModEntities.FLASH_LIGHTNING.get(), LightningBoltRenderer::new);
        event.registerEntityRenderer(ModEntities.DRIPSTONE_SPIKE.get(), GeoSpikeRenderer::new);
        event.registerEntityRenderer(ModEntities.ICE_SPIKE.get(), renderManager -> new GeoSpikeRenderer(renderManager, prefix("textures/entity/ice_spike.png")));
        event.registerEntityRenderer(ModEntities.THROWN_SPIKE.get(), FallingSpikeRenderer::new);
        event.registerEntityRenderer(ModEntities.THROWN_ICE_SPIKE.get(), renderManager -> new FallingSpikeRenderer(renderManager, prefix("textures/entity/ice_spike.png")));

        event.registerEntityRenderer(ModEntities.LERP_PROJECTILE.get(), (m) -> new EntityRenderer<>(m) {
            @Override
            public @NotNull ResourceLocation getTextureLocation(@NotNull EntityLerpedProjectile pEntity) {
                return ResourceLocation.fromNamespaceAndPath(ArsNouveau.MODID, "textures/entity/spell_proj.png");
            }
        });

    }

    @SubscribeEvent
    public static void initItemColors(final RegisterColorHandlersEvent.Item event) {
        event.register((stack, color) -> color > 0 ? -1 :
                        stack.getOrDefault(DataComponents.BASE_COLOR, DyeColor.RED).getTextureDiffuseColor() + 0xFF000000,
                ModItems.CASTER_BAG.get());
    }

    //keybinding
    @SubscribeEvent
    public static void registerKeyBindings(RegisterKeyMappingsEvent event) {
        event.register(CURIO_BAG_KEYBINDING);
    }

    //Curio bag stuff
    @SubscribeEvent
    public static void bindContainerRenderers(RegisterMenuScreensEvent event) {
        event.register(ModRegistry.CURIO_HOLDER.get(), (CurioHolderContainer screenContainer, Inventory inv, Component titleIn) -> new CurioHolderScreen<>(screenContainer, inv, titleIn, prefix("textures/gui/curio_bag.png"), 175, 163));
        event.register(ModRegistry.CASTER_HOLDER.get(), (CasterHolderContainer screenContainer, Inventory inv, Component titleIn) -> new CurioHolderScreen<>(screenContainer, inv, titleIn, prefix("textures/gui/curio_bag_2.png"), 175, 217));
    }

    private static @NotNull EntityRenderer<EntityProjectileSpell> projectileRender(EntityRendererProvider.Context renderManager) {
        return new RenderSpell(renderManager, ResourceLocation.fromNamespaceAndPath(ArsNouveau.MODID, "textures/entity/spell_proj.png"));
    }

    public void openBackpackGui(ClientTickEvent.Post event) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            Minecraft minecraft = Minecraft.getInstance();
            Player playerEntity = minecraft.player;
            if (!(minecraft.screen instanceof CurioHolderScreen) && (playerEntity != null)) {
                if (CURIO_BAG_KEYBINDING.isDown()) {
                    SlotReference backpack = CurioHolder.isEquipped(playerEntity);

                    if (!backpack.isEmpty()) {
                        Networking.sendToServer(new OpenCurioBagPacket());
                    }
                }
            }
        }
    }

}
