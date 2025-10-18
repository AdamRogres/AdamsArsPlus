package com.adamsmods.adamsarsplus.entities;

import com.adamsmods.adamsarsplus.entities.custom.*;
import com.adamsmods.adamsarsplus.lib.AdamsLibEntityNames;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

import static com.adamsmods.adamsarsplus.AdamsArsPlus.MOD_ID;
import static com.adamsmods.adamsarsplus.Config.MAGE_DIMENSION_BLACKLIST;
import static net.minecraftforge.registries.ForgeRegistries.ENTITY_TYPES;

public class AdamsModEntities {

        public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ENTITY_TYPES, MOD_ID);

        static <T extends Entity> RegistryObject<EntityType<T>> registerEntity(String name, EntityType.Builder<T> builder) {
            return ENTITIES.register(name, () -> builder.build(MOD_ID + ":" + name));
        }

        public static final RegistryObject<EntityType<EntityDomainSpell>> DOMAIN_SPELL = registerEntity(
                AdamsLibEntityNames.DOMAIN,
                EntityType.Builder.<EntityDomainSpell>of(EntityDomainSpell::new, MobCategory.MISC)
                        .sized(0.5f, 0.5f)
                        .setTrackingRange(20)
                        .setShouldReceiveVelocityUpdates(true)
                        .noSave()
                        .setUpdateInterval(120).setCustomClientFactory(EntityDomainSpell::new));


        public static final RegistryObject<EntityType<DetonateProjectile>> DETONATE_SPELL = registerEntity(
                AdamsLibEntityNames.DETONATE,
                EntityType.Builder.<DetonateProjectile>of(DetonateProjectile::new, MobCategory.MISC)
                        .sized(0.5f, 0.5f)
                        .setTrackingRange(20)
                        .fireImmune()
                        .setShouldReceiveVelocityUpdates(true)
                        .noSave()
                        .setUpdateInterval(120).setCustomClientFactory(DetonateProjectile::new));

    public static final RegistryObject<EntityType<MeteorProjectile>> METEOR_SPELL = registerEntity(
            AdamsLibEntityNames.METEOR,
            EntityType.Builder.<MeteorProjectile>of(MeteorProjectile::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .setTrackingRange(20)
                    .fireImmune()
                    .setShouldReceiveVelocityUpdates(true)
                    .noSave()
                    .setUpdateInterval(120).setCustomClientFactory(MeteorProjectile::new));

    public static final RegistryObject<EntityType<EntityDivineSmite>> DIVINE_SMITE = registerEntity(
            AdamsLibEntityNames.DIVINESMITE,
                    EntityType.Builder.<EntityDivineSmite>of(EntityDivineSmite::new, MobCategory.MISC)
                            .noSave()
                            .sized(0.0F, 0.0F)
                            .clientTrackingRange(16)
                            .updateInterval(Integer.MAX_VALUE)
                            .setShouldReceiveVelocityUpdates(true).setUpdateInterval(60));

    public static final RegistryObject<EntityType<SummonSkeleton_m>> SUMMON_SKELETON_M = registerEntity(
            AdamsLibEntityNames.SUMMONED_SKELETON_M,
                EntityType.Builder.<SummonSkeleton_m>of(SummonSkeleton_m::new, MobCategory.CREATURE)
                        .sized(1.0F, 1.8F)
                        .clientTrackingRange(10));


    public static final RegistryObject<EntityType<RyanEntity>> RYAN_ENTITY = registerEntity(
                AdamsLibEntityNames.RYAN,
                EntityType.Builder.<RyanEntity>of(RyanEntity::new, MobCategory.MONSTER)
                        .sized(0.7f, 1.9f));

        public static final RegistryObject<EntityType<CadeEntity>> CADE_ENTITY = registerEntity(
                AdamsLibEntityNames.CADE,
                EntityType.Builder.<CadeEntity>of(CadeEntity::new, MobCategory.MONSTER)
                        .sized(0.6f, 1.7f));

        public static final RegistryObject<EntityType<NickEntity>> NICK_ENTITY = registerEntity(
                AdamsLibEntityNames.NICK,
                EntityType.Builder.<NickEntity>of(NickEntity::new, MobCategory.MONSTER)
                        .sized(0.9f, 2.3f));

        public static final RegistryObject<EntityType<CamEntity>> CAM_ENTITY = registerEntity(
                AdamsLibEntityNames.CAM,
                EntityType.Builder.<CamEntity>of(CamEntity::new, MobCategory.MONSTER)
                        .sized(0.7f, 2.0f));

        public static final RegistryObject<EntityType<MattEntity>> MATT_ENTITY = registerEntity(
                AdamsLibEntityNames.MATT,
                EntityType.Builder.<MattEntity>of(MattEntity::new, MobCategory.MONSTER)
                        .sized(0.8f, 2.2f));
        public static final RegistryObject<EntityType<AdamEntity>> ADAM_ENTITY = registerEntity(
                AdamsLibEntityNames.ADAM,
                EntityType.Builder.<AdamEntity>of(AdamEntity::new, MobCategory.MONSTER)
                        .sized(0.6f, 1.8f));

    public static final RegistryObject<EntityType<MysteriousMageEntity>> MAGE_ENTITY = registerEntity(
            AdamsLibEntityNames.MAGE,
            EntityType.Builder.<MysteriousMageEntity>of(MysteriousMageEntity::new, MobCategory.MONSTER)
                    .sized(0.7f, 1.8f)
                    .clientTrackingRange(10));

    // Ten Shadows
    public static final RegistryObject<EntityType<DivineDogEntity>> DIVINE_DOG = registerEntity(
            AdamsLibEntityNames.DIVINEDOG,
            EntityType.Builder.<DivineDogEntity>of(DivineDogEntity::new, MobCategory.CREATURE)
                    .sized(0.7f, 0.7f)
                    .clientTrackingRange(10));
    public static final RegistryObject<EntityType<NueEntity>> NUE = registerEntity(
            AdamsLibEntityNames.NUE,
            EntityType.Builder.<NueEntity>of(NueEntity::new, MobCategory.CREATURE)
                    .sized(0.7f, 0.7f)
                    .clientTrackingRange(10));
    public static final RegistryObject<EntityType<RabbitEEntity>> RABBIT_ESCAPE = registerEntity(
            AdamsLibEntityNames.RABBITE,
            EntityType.Builder.<RabbitEEntity>of(RabbitEEntity::new, MobCategory.CREATURE)
                    .sized(1.2f, 1.7f)
                    .clientTrackingRange(10));
    public static final RegistryObject<EntityType<RDeerEntity>> ROUND_DEER = registerEntity(
            AdamsLibEntityNames.RDEER,
            EntityType.Builder.<RDeerEntity>of(RDeerEntity::new, MobCategory.CREATURE)
                    .sized(2.2f, 2.1f)
                    .clientTrackingRange(10));
    public static final RegistryObject<EntityType<MahoragaEntity>> MAHORAGA = registerEntity(
            AdamsLibEntityNames.MAHO,
            EntityType.Builder.<MahoragaEntity>of(MahoragaEntity::new, MobCategory.CREATURE)
                    .sized(1.5f, 3.8f)
                    .clientTrackingRange(10));

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistrationHandler {

        @SubscribeEvent
        public static void registerEntityAttributes(final EntityAttributeCreationEvent event) {
            event.put(AdamsModEntities.RYAN_ENTITY.get(), RyanEntity.createAttributes().build());
            event.put(AdamsModEntities.CADE_ENTITY.get(), CadeEntity.createAttributes().build());
            event.put(AdamsModEntities.NICK_ENTITY.get(), NickEntity.createAttributes().build());
            event.put(AdamsModEntities.CAM_ENTITY.get(), CamEntity.createAttributes().build());
            event.put(AdamsModEntities.MATT_ENTITY.get(), MattEntity.createAttributes().build());
            event.put(AdamsModEntities.ADAM_ENTITY.get(), AdamEntity.createAttributes().build());
            event.put(AdamsModEntities.MAGE_ENTITY.get(), MysteriousMageEntity.createAttributes().build());
            event.put(AdamsModEntities.SUMMON_SKELETON_M.get(), SummonSkeleton_m.createAttributes().build());
            event.put(AdamsModEntities.DIVINE_DOG.get(), DivineDogEntity.createAttributes().build());
            event.put(AdamsModEntities.NUE.get(), NueEntity.createAttributes().build());
            event.put(AdamsModEntities.RABBIT_ESCAPE.get(), RabbitEEntity.createAttributes().build());
            event.put(AdamsModEntities.ROUND_DEER.get(), RDeerEntity.createAttributes().build());
            event.put(AdamsModEntities.MAHORAGA.get(), MahoragaEntity.createAttributes().build());
        }
    }

    public static boolean genericGroundSpawn(EntityType<? extends Entity> animal, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource random) {
            return worldIn.getBlockState(pos.below()).is(Blocks.GRASS_BLOCK) && worldIn.getRawBrightness(pos, 0) > 8;
    }

    public static void registerPlacements() {
        SpawnPlacements.register((EntityType) MAGE_ENTITY.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AdamsModEntities::mageSpawnRules);
    }

    public static boolean mageSpawnRules(EntityType<? extends Monster> type, ServerLevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource randomIn) {
        return worldIn.getDifficulty() != Difficulty.PEACEFUL && Monster.checkMonsterSpawnRules(type, worldIn, reason, pos, randomIn) && !((List) MAGE_DIMENSION_BLACKLIST.get()).contains(worldIn.getLevel().dimension().location().toString());
    }

}