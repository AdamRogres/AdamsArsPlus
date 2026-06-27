package adamsmods.adamsarsplus.event;


import adamsmods.adamsarsplus.AdamsArsPlus;
import adamsmods.adamsarsplus.client.entities.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = AdamsArsPlus.MODID, value = Dist.CLIENT)
public class ModEventBusClientEvents {

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.RYAN_LAYER, RyanModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.CADE_LAYER, CadeModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.NICK_LAYER, NickModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.CAM_LAYER, CamModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.MATT_LAYER, MattModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.JOSH_LAYER, JoshModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.ADAM_LAYER, AdamModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.MAGE_LAYER, MageModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.MAGEK_LAYER, MageKnightModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.DDOG_LAYER, DDogModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.NUE_LAYER, NueModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.RABE_LAYER, RabbitEModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.RDEER_LAYER, RDeerModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.MAHO_LAYER, MahoragaModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.FIRE_LAYER, FireModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.BLADE_LAYER, BladeModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.SWORD_LAYER, TerraprismaModel::createBodyLayer);
    }

}
