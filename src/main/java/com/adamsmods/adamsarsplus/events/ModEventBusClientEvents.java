package com.adamsmods.adamsarsplus.events;


import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.entities.client.CadeModel;
import com.adamsmods.adamsarsplus.entities.client.ModModelLayers;
import com.adamsmods.adamsarsplus.entities.client.*;
import com.adamsmods.adamsarsplus.entities.client.ryan_slayer_of_dragons;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AdamsArsPlus.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.RYAN_LAYER, ryan_slayer_of_dragons::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.CADE_LAYER, CadeModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.NICK_LAYER, NickModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.CAM_LAYER, CamModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.MATT_LAYER, MattModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.ADAM_LAYER, AdamModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.MAGE_LAYER, MageModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.DDOG_LAYER, DDogModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.NUE_LAYER, NueModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.RABE_LAYER, RabbitEModel::createBodyLayer);
    }

}
