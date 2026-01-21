package adamsmods.adamsarsplus.common.entity.example;


import adamsmods.adamsarsplus.AdamsArsPlus;
import adamsmods.adamsarsplus.common.entity.example.mages.EntityMageBase;
import adamsmods.adamsarsplus.common.entity.example.summon.AllyVhexEntity;
import adamsmods.adamsarsplus.common.entity.example.summon.SummonDirewolf;
import adamsmods.adamsarsplus.registry.ModEntities;
import com.hollingsworth.arsnouveau.common.entity.SummonSkeleton;
import com.hollingsworth.arsnouveau.common.entity.WealdWalker;
import com.hollingsworth.arsnouveau.common.entity.familiar.FamiliarEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Strider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber(modid = AdamsArsPlus.MODID, bus = EventBusSubscriber.Bus.MOD)
public class EntityAttributes {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.SIREN_ENTITY.get(), MermaidEntity.createAttributes());
        event.put(ModEntities.SIREN_FAMILIAR.get(), FamiliarEntity.attributes().build());

        event.put(ModEntities.FIRENANDO_ENTITY.get(), FirenandoEntity.createAttributes().build());
        event.put(ModEntities.FIRENANDO_FAMILIAR.get(), FamiliarEntity.attributes().build());

        event.put(ModEntities.FLASHJACK_ENTITY.get(), FlashjackEntity.createAttributes().build());
        event.put(ModEntities.FLASHJACK_FAMILIAR.get(), FamiliarEntity.attributes().build());


        event.put(ModEntities.FLASHING_WEALD_WALKER.get(), WealdWalker.attributes().build());
        event.put(ModEntities.SKELEHORSE_SUMMON.get(), AbstractHorse.createBaseHorseAttributes().build());
        event.put(ModEntities.CAMEL_SUMMON.get(), AbstractHorse.createBaseHorseAttributes().build());
        event.put(ModEntities.DIREWOLF_SUMMON.get(), SummonDirewolf.createAttributes().build());
        event.put(ModEntities.WSKELETON_SUMMON.get(), SummonSkeleton.createAttributes().build());
        event.put(ModEntities.VHEX_SUMMON.get(), AllyVhexEntity.createAttributes().build());
        event.put(ModEntities.DOLPHIN_SUMMON.get(), Dolphin.createAttributes().add(Attributes.MOVEMENT_SPEED, 1.5).build());
        event.put(ModEntities.STRIDER_SUMMON.get(), Strider.createAttributes().add(Attributes.MOVEMENT_SPEED, 0.2).build());

        event.put(ModEntities.FIRE_MAGE.get(), EntityMageBase.createAttributes().build());
        event.put(ModEntities.WATER_MAGE.get(), EntityMageBase.createAttributes().build());
        event.put(ModEntities.AIR_MAGE.get(), EntityMageBase.createAttributes().build());
        event.put(ModEntities.EARTH_MAGE.get(), EntityMageBase.createAttributes().build());

    }
}
