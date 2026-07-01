package adamsmods.adamsarsplus.registry;

import adamsmods.adamsarsplus.common.entity.*;
import adamsmods.adamsarsplus.common.entity.custom.*;
import adamsmods.adamsarsplus.common.entity.variants.*;
import adamsmods.adamsarsplus.common.lib.AdamsLibEntityNames;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

import static adamsmods.adamsarsplus.AdamsArsPlus.MODID;
import static adamsmods.adamsarsplus.ConfigHandler.Common.MAGE_DIMENSION_BLACKLIST;
import static net.minecraft.world.entity.Mob.checkMobSpawnRules;

@EventBusSubscriber(modid = MODID)
public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<EyeOfSoulSeeking>> EYE_OF_SOUL;
    public static final DeferredHolder<EntityType<?>, EntityType<EntityDomainSpell>> DOMAIN_SPELL;
    public static final DeferredHolder<EntityType<?>, EntityType<DetonateProjectile>> DETONATE_SPELL;
    public static final DeferredHolder<EntityType<?>, EntityType<MeteorProjectile>> METEOR_SPELL;
    public static final DeferredHolder<EntityType<?>, EntityType<BladeProjectile>> BLADE_PROJ;
    public static final DeferredHolder<EntityType<?>, EntityType<EntityDivineSmite>> DIVINE_SMITE;
    public static final DeferredHolder<EntityType<?>, EntityType<SummonSkeleton_m>> SUMMON_SKELETON_M;
    public static final DeferredHolder<EntityType<?>, EntityType<FireEntity>> FIRE_ENTITY;
    public static final DeferredHolder<EntityType<?>, EntityType<TerraprismaEntity>> TERRA_ENTITY;

    public static final DeferredHolder<EntityType<?>, EntityType<RyanEntity>> RYAN_ENTITY;
    public static final DeferredHolder<EntityType<?>, EntityType<CadeEntity>> CADE_ENTITY;
    public static final DeferredHolder<EntityType<?>, EntityType<NickEntity>> NICK_ENTITY;
    public static final DeferredHolder<EntityType<?>, EntityType<CamEntity>> CAM_ENTITY;
    public static final DeferredHolder<EntityType<?>, EntityType<MattEntity>> MATT_ENTITY;
    public static final DeferredHolder<EntityType<?>, EntityType<JoshEntity>> JOSH_ENTITY;
    public static final DeferredHolder<EntityType<?>, EntityType<AdamEntity>> ADAM_ENTITY;

    public static final DeferredHolder<EntityType<?>, EntityType<MysteriousMageEntity>> MAGE_ENTITY;
    public static final DeferredHolder<EntityType<?>, EntityType<FlameMageEntity>> FLAME_MAGE_ENTITY;
    public static final DeferredHolder<EntityType<?>, EntityType<FrostMageEntity>> FROST_MAGE_ENTITY;
    public static final DeferredHolder<EntityType<?>, EntityType<EarthMageEntity>> EARTH_MAGE_ENTITY;
    public static final DeferredHolder<EntityType<?>, EntityType<LightningMageEntity>> LIGHTNING_MAGE_ENTITY;
    public static final DeferredHolder<EntityType<?>, EntityType<HolyMageEntity>> HOLY_MAGE_ENTITY;
    public static final DeferredHolder<EntityType<?>, EntityType<VoidMageEntity>> VOID_MAGE_ENTITY;

    public static final DeferredHolder<EntityType<?>, EntityType<MageKnightEntity>> MAGE_KNIGHT;
    public static final DeferredHolder<EntityType<?>, EntityType<FlameMageKnight>> FLAME_KNIGHT;
    public static final DeferredHolder<EntityType<?>, EntityType<FrostMageKnight>> FROST_KNIGHT;
    public static final DeferredHolder<EntityType<?>, EntityType<EarthMageKnight>> EARTH_KNIGHT;
    public static final DeferredHolder<EntityType<?>, EntityType<LightningMageKnight>> LIGHTNING_KNIGHT;
    public static final DeferredHolder<EntityType<?>, EntityType<HolyMageKnight>> HOLY_KNIGHT;
    public static final DeferredHolder<EntityType<?>, EntityType<VoidMageKnight>> VOID_KNIGHT;

    public static final DeferredHolder<EntityType<?>, EntityType<DivineDogEntity>> DIVINE_DOG;
    public static final DeferredHolder<EntityType<?>, EntityType<NueEntity>> NUE;
    public static final DeferredHolder<EntityType<?>, EntityType<RabbitEEntity>> RABBIT_ESCAPE;
    public static final DeferredHolder<EntityType<?>, EntityType<RDeerEntity>> ROUND_DEER;
    public static final DeferredHolder<EntityType<?>, EntityType<MahoragaEntity>> MAHORAGA;

    static {
        // Misc
        EYE_OF_SOUL = registerEntity(AdamsLibEntityNames.SOUL_EYE,
                EntityType.Builder.<EyeOfSoulSeeking>of(EyeOfSoulSeeking::new, MobCategory.MISC)
                        .sized(0.25f, 0.25f)
                        .clientTrackingRange(4)
                        .updateInterval(4));

        DOMAIN_SPELL = registerEntity(
                AdamsLibEntityNames.DOMAIN,
                EntityType.Builder.<EntityDomainSpell>of(EntityDomainSpell::new, MobCategory.MISC)
                        .sized(0.5f, 0.5f)
                        .setTrackingRange(20)
                        .setShouldReceiveVelocityUpdates(true)
                        .noSave()
                        .setUpdateInterval(120));

        DETONATE_SPELL = registerEntity(
                AdamsLibEntityNames.DETONATE,
                EntityType.Builder.<DetonateProjectile>of(DetonateProjectile::new, MobCategory.MISC)
                        .sized(0.5f, 0.5f)
                        .setTrackingRange(20)
                        .fireImmune()
                        .setShouldReceiveVelocityUpdates(true)
                        .noSave()
                        .setUpdateInterval(120));

        METEOR_SPELL = registerEntity(
                AdamsLibEntityNames.METEOR,
                EntityType.Builder.<MeteorProjectile>of(MeteorProjectile::new, MobCategory.MISC)
                        .sized(0.5f, 0.5f)
                        .setTrackingRange(20)
                        .fireImmune()
                        .setShouldReceiveVelocityUpdates(true)
                        .noSave()
                        .setUpdateInterval(120));

        BLADE_PROJ = registerEntity(
                AdamsLibEntityNames.BLADE,
                EntityType.Builder.<BladeProjectile>of(BladeProjectile::new, MobCategory.MISC)
                        .sized(0.3f, 0.3f)
                        .clientTrackingRange(20));

        DIVINE_SMITE = registerEntity(
                AdamsLibEntityNames.DIVINESMITE,
                EntityType.Builder.<EntityDivineSmite>of(EntityDivineSmite::new, MobCategory.MISC)
                        .noSave()
                        .sized(0.0F, 0.0F)
                        .clientTrackingRange(16)
                        .updateInterval(Integer.MAX_VALUE)
                        .setShouldReceiveVelocityUpdates(true)
                        .setUpdateInterval(60));

        SUMMON_SKELETON_M = registerEntity(
                AdamsLibEntityNames.SUMMONED_SKELETON_M,
                EntityType.Builder.<SummonSkeleton_m>of(SummonSkeleton_m::new, MobCategory.CREATURE)
                        .sized(1.0F, 1.8F)
                        .clientTrackingRange(10));

        FIRE_ENTITY = registerEntity(
                AdamsLibEntityNames.FIRE,
                EntityType.Builder.<FireEntity>of(FireEntity::new, MobCategory.CREATURE)
                        .sized(0.8f, 0.8f)
                        .setShouldReceiveVelocityUpdates(true));

        TERRA_ENTITY = registerEntity(
                AdamsLibEntityNames.SWORD,
                EntityType.Builder.<TerraprismaEntity>of(TerraprismaEntity::new, MobCategory.CREATURE)
                        .sized(0.8f, 0.8f)
                        .setShouldReceiveVelocityUpdates(true));

        // Bosses
        RYAN_ENTITY = registerEntity(
                AdamsLibEntityNames.RYAN,
                EntityType.Builder.<RyanEntity>of(RyanEntity::new, MobCategory.MONSTER)
                        .sized(0.7f, 1.9f));

        CADE_ENTITY = registerEntity(
                AdamsLibEntityNames.CADE,
                EntityType.Builder.<CadeEntity>of(CadeEntity::new, MobCategory.MONSTER)
                        .sized(0.6f, 1.7f));

        NICK_ENTITY = registerEntity(
                AdamsLibEntityNames.NICK,
                EntityType.Builder.<NickEntity>of(NickEntity::new, MobCategory.MONSTER)
                        .sized(0.9f, 2.3f));

        CAM_ENTITY = registerEntity(
                AdamsLibEntityNames.CAM,
                EntityType.Builder.<CamEntity>of(CamEntity::new, MobCategory.MONSTER)
                        .sized(0.7f, 2.0f));

        MATT_ENTITY = registerEntity(
                AdamsLibEntityNames.MATT,
                EntityType.Builder.<MattEntity>of(MattEntity::new, MobCategory.MONSTER)
                        .sized(0.8f, 2.2f));

        JOSH_ENTITY = registerEntity(
                AdamsLibEntityNames.JOSH,
                EntityType.Builder.<JoshEntity>of(JoshEntity::new, MobCategory.MONSTER)
                        .sized(1.7f, 3.7f)
                        .fireImmune());

        ADAM_ENTITY = registerEntity(
                AdamsLibEntityNames.ADAM,
                EntityType.Builder.<AdamEntity>of(AdamEntity::new, MobCategory.MONSTER)
                        .sized(0.6f, 1.8f));

        // Mages
        MAGE_ENTITY = registerEntity(
                AdamsLibEntityNames.MAGE,
                EntityType.Builder.<MysteriousMageEntity>of(MysteriousMageEntity::new, MobCategory.MONSTER)
                        .sized(0.7f, 1.8f)
                        .setTrackingRange(10));

        FLAME_MAGE_ENTITY = registerEntity(
                AdamsLibEntityNames.FLAME_MAGE,
                EntityType.Builder.<FlameMageEntity>of(FlameMageEntity::new, MobCategory.MONSTER)
                        .sized(0.7f, 1.8f));

        FROST_MAGE_ENTITY = registerEntity(
                AdamsLibEntityNames.FROST_MAGE,
                EntityType.Builder.<FrostMageEntity>of(FrostMageEntity::new, MobCategory.MONSTER)
                        .sized(0.7f, 1.8f));

        EARTH_MAGE_ENTITY = registerEntity(
                AdamsLibEntityNames.EARTH_MAGE,
                EntityType.Builder.<EarthMageEntity>of(EarthMageEntity::new, MobCategory.MONSTER)
                        .sized(0.7f, 1.8f));

        LIGHTNING_MAGE_ENTITY = registerEntity(
                AdamsLibEntityNames.LIGHTNING_MAGE,
                EntityType.Builder.<LightningMageEntity>of(LightningMageEntity::new, MobCategory.MONSTER)
                        .sized(0.7f, 1.8f));

        HOLY_MAGE_ENTITY = registerEntity(
                AdamsLibEntityNames.HOLY_MAGE,
                EntityType.Builder.<HolyMageEntity>of(HolyMageEntity::new, MobCategory.MONSTER)
                        .sized(0.7f, 1.8f));

        VOID_MAGE_ENTITY = registerEntity(
                AdamsLibEntityNames.VOID_MAGE,
                EntityType.Builder.<VoidMageEntity>of(VoidMageEntity::new, MobCategory.MONSTER)
                        .sized(0.7f, 1.8f));

        MAGE_KNIGHT = registerEntity(
                AdamsLibEntityNames.MAGE_KNIGHT,
                EntityType.Builder.<MageKnightEntity>of(MageKnightEntity::new, MobCategory.MONSTER)
                        .sized(0.9f, 1.9f)
                        .setTrackingRange(10));

        FLAME_KNIGHT = registerEntity(
                AdamsLibEntityNames.FLAME_KNIGHT,
                EntityType.Builder.<FlameMageKnight>of(FlameMageKnight::new, MobCategory.MONSTER)
                        .sized(0.9f, 1.9f)
                        .setTrackingRange(10));

        FROST_KNIGHT = registerEntity(
                AdamsLibEntityNames.FROST_KNIGHT,
                EntityType.Builder.<FrostMageKnight>of(FrostMageKnight::new, MobCategory.MONSTER)
                        .sized(0.9f, 1.9f)
                        .setTrackingRange(10));

        EARTH_KNIGHT = registerEntity(
                AdamsLibEntityNames.EARTH_KNIGHT,
                EntityType.Builder.<EarthMageKnight>of(EarthMageKnight::new, MobCategory.MONSTER)
                        .sized(0.9f, 1.9f)
                        .setTrackingRange(10));

        LIGHTNING_KNIGHT = registerEntity(
                AdamsLibEntityNames.LIGHTNING_KNIGHT,
                EntityType.Builder.<LightningMageKnight>of(LightningMageKnight::new, MobCategory.MONSTER)
                        .sized(0.9f, 1.9f)
                        .setTrackingRange(10));

        HOLY_KNIGHT = registerEntity(
                AdamsLibEntityNames.HOLY_KNIGHT,
                EntityType.Builder.<HolyMageKnight>of(HolyMageKnight::new, MobCategory.MONSTER)
                        .sized(0.9f, 1.9f)
                        .setTrackingRange(10));

        VOID_KNIGHT = registerEntity(
                AdamsLibEntityNames.VOID_KNIGHT,
                EntityType.Builder.<VoidMageKnight>of(VoidMageKnight::new, MobCategory.MONSTER)
                        .sized(0.9f, 1.9f)
                        .setTrackingRange(10));

        // Ten Shadows
        DIVINE_DOG = registerEntity(
                AdamsLibEntityNames.DIVINEDOG,
                EntityType.Builder.<DivineDogEntity>of(DivineDogEntity::new, MobCategory.CREATURE)
                        .sized(0.7f, 0.7f)
                        .clientTrackingRange(10));

        NUE = registerEntity(
                AdamsLibEntityNames.NUE,
                EntityType.Builder.<NueEntity>of(NueEntity::new, MobCategory.CREATURE)
                        .sized(0.7f, 0.7f)
                        .clientTrackingRange(10));

        RABBIT_ESCAPE = registerEntity(
                AdamsLibEntityNames.RABBITE,
                EntityType.Builder.<RabbitEEntity>of(RabbitEEntity::new, MobCategory.CREATURE)
                        .sized(1.2f, 1.7f)
                        .clientTrackingRange(10));

        ROUND_DEER = registerEntity(
                AdamsLibEntityNames.RDEER,
                EntityType.Builder.<RDeerEntity>of(RDeerEntity::new, MobCategory.CREATURE)
                        .sized(2.2f, 2.1f)
                        .clientTrackingRange(10));

        MAHORAGA = registerEntity(
                AdamsLibEntityNames.MAHO,
                EntityType.Builder.<MahoragaEntity>of(MahoragaEntity::new, MobCategory.CREATURE)
                        .sized(1.5f, 3.8f)
                        .clientTrackingRange(10));
    }

    static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> registerEntity(String name, EntityType.Builder<T> builder) {
        return ENTITIES.register(name, () -> builder.build(MODID + ":" + name));
    }

    @EventBusSubscriber(modid = MODID)
    public static class RegistrationHandler {

        @SubscribeEvent
        public static void registerEntityAttributes(final EntityAttributeCreationEvent event) {
            event.put(ModEntities.RYAN_ENTITY.get(), RyanEntity.createAttributes().build());
            event.put(ModEntities.CADE_ENTITY.get(), CadeEntity.createAttributes().build());
            event.put(ModEntities.NICK_ENTITY.get(), NickEntity.createAttributes().build());
            event.put(ModEntities.CAM_ENTITY.get(), CamEntity.createAttributes().build());
            event.put(ModEntities.MATT_ENTITY.get(), MattEntity.createAttributes().build());
            event.put(ModEntities.JOSH_ENTITY.get(), JoshEntity.createAttributes().build());
            event.put(ModEntities.ADAM_ENTITY.get(), AdamEntity.createAttributes().build());

            event.put(ModEntities.MAGE_ENTITY.get(), MysteriousMageEntity.createAttributes().build());
            event.put(ModEntities.FLAME_MAGE_ENTITY.get(), FlameMageEntity.createAttributes().build());
            event.put(ModEntities.FROST_MAGE_ENTITY.get(), FrostMageEntity.createAttributes().build());
            event.put(ModEntities.EARTH_MAGE_ENTITY.get(), EarthMageEntity.createAttributes().build());
            event.put(ModEntities.LIGHTNING_MAGE_ENTITY.get(), LightningMageEntity.createAttributes().build());
            event.put(ModEntities.HOLY_MAGE_ENTITY.get(), HolyMageEntity.createAttributes().build());
            event.put(ModEntities.VOID_MAGE_ENTITY.get(), VoidMageEntity.createAttributes().build());

            event.put(ModEntities.MAGE_KNIGHT.get(), MageKnightEntity.createAttributes().build());
            event.put(ModEntities.FLAME_KNIGHT.get(), FlameMageKnight.createAttributes().build());
            event.put(ModEntities.FROST_KNIGHT.get(), FrostMageKnight.createAttributes().build());
            event.put(ModEntities.EARTH_KNIGHT.get(), EarthMageKnight.createAttributes().build());
            event.put(ModEntities.LIGHTNING_KNIGHT.get(), LightningMageKnight.createAttributes().build());
            event.put(ModEntities.HOLY_KNIGHT.get(), HolyMageKnight.createAttributes().build());
            event.put(ModEntities.VOID_KNIGHT.get(), VoidMageKnight.createAttributes().build());

            event.put(ModEntities.SUMMON_SKELETON_M.get(), SummonSkeleton_m.createAttributes().build());
            event.put(ModEntities.DIVINE_DOG.get(), DivineDogEntity.createAttributes().build());
            event.put(ModEntities.NUE.get(), NueEntity.createAttributes().build());
            event.put(ModEntities.RABBIT_ESCAPE.get(), RabbitEEntity.createAttributes().build());
            event.put(ModEntities.ROUND_DEER.get(), RDeerEntity.createAttributes().build());
            event.put(ModEntities.MAHORAGA.get(), MahoragaEntity.createAttributes().build());
            event.put(ModEntities.FIRE_ENTITY.get(), FireEntity.createAttributes().build());
            event.put(ModEntities.TERRA_ENTITY.get(), TerraprismaEntity.createAttributes().build());
        }
    }


    @SubscribeEvent
    public static void registerSP(RegisterSpawnPlacementsEvent event) {

        event.register(MAGE_ENTITY.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ModEntities::mageSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(MAGE_KNIGHT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ModEntities::mageVariantSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(FLAME_MAGE_ENTITY.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ModEntities::mageVariantSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(FLAME_KNIGHT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ModEntities::mageVariantSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(FROST_MAGE_ENTITY.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ModEntities::mageVariantSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(FROST_KNIGHT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ModEntities::mageVariantSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(EARTH_MAGE_ENTITY.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ModEntities::mageVariantSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(EARTH_KNIGHT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ModEntities::mageVariantSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(LIGHTNING_MAGE_ENTITY.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ModEntities::mageVariantSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(LIGHTNING_KNIGHT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ModEntities::mageVariantSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(HOLY_MAGE_ENTITY.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ModEntities::mageVariantSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(HOLY_KNIGHT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ModEntities::mageVariantSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(VOID_MAGE_ENTITY.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ModEntities::mageVariantSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(VOID_KNIGHT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ModEntities::mageVariantSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);

    }

    public static boolean mageSpawnRules(EntityType<? extends Monster> type, ServerLevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource randomIn) {
        return worldIn.getDifficulty() != Difficulty.PEACEFUL && Monster.checkMonsterSpawnRules(type, worldIn, reason, pos, randomIn) && !((List) MAGE_DIMENSION_BLACKLIST.get()).contains(worldIn.getLevel().dimension().location().toString());
    }

    public static boolean mageVariantSpawnRules(EntityType<? extends Monster> type, ServerLevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource randomIn) {
        return worldIn.getDifficulty() != Difficulty.PEACEFUL && checkMonsterSpawnRules(type, worldIn, reason, pos, randomIn);
    }

    public static boolean checkMonsterSpawnRules(EntityType<? extends Monster> pType, ServerLevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
        return pLevel.getDifficulty() != Difficulty.PEACEFUL && isNaturalDarkEnoughToSpawn(pLevel, pPos, pRandom) && checkMobSpawnRules(pType, pLevel, pSpawnType, pPos, pRandom);
    }

    public static boolean isNaturalDarkEnoughToSpawn(ServerLevelAccessor pLevel, BlockPos pPos, RandomSource pRandom) {
        DimensionType dimensiontype = pLevel.dimensionType();
        int i = dimensiontype.monsterSpawnBlockLightLimit();
        if (i < 15 && pLevel.getBrightness(LightLayer.BLOCK, pPos) > i) {
            return false;
        } else {
            int j = pLevel.getLevel().isThundering() ? pLevel.getMaxLocalRawBrightness(pPos, 10) : pLevel.getMaxLocalRawBrightness(pPos);
            return j <= dimensiontype.monsterSpawnLightTest().sample(pRandom);
        }

    }

}
