package com.adamsmods.adamsarsplus.events;


import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.entities.AdamsModEntities;
import com.adamsmods.adamsarsplus.entities.client.ModModelLayers;
import com.adamsmods.adamsarsplus.entities.client.ryan_slayer_of_dragons;
import com.adamsmods.adamsarsplus.entities.custom.*;
import com.google.common.eventbus.Subscribe;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AdamsArsPlus.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(AdamsModEntities.RYAN_ENTITY.get(), RyanEntity.createAttributes().build());
        event.put(AdamsModEntities.CADE_ENTITY.get(), CadeEntity.createAttributes().build());
        event.put(AdamsModEntities.NICK_ENTITY.get(), NickEntity.createAttributes().build());
        event.put(AdamsModEntities.CAM_ENTITY.get(), CamEntity.createAttributes().build());
        event.put(AdamsModEntities.MATT_ENTITY.get(), MattEntity.createAttributes().build());
        event.put(AdamsModEntities.ADAM_ENTITY.get(), AdamEntity.createAttributes().build());
        event.put(AdamsModEntities.MAGE_ENTITY.get(), MysteriousMageEntity.createAttributes().build());
    }

}
