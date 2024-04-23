package com.adamsmods.adamsarsplus.entities;


import com.adamsmods.adamsarsplus.lib.AdamsLibEntityNames;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import com.hollingsworth.arsnouveau.common.lib.LibEntityNames;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.adamsmods.adamsarsplus.AdamsArsPlus.MOD_ID;
import static com.hollingsworth.arsnouveau.ArsNouveau.MODID;

public class AdamsModEntities {

        public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MOD_ID);

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


        public static boolean genericGroundSpawn(EntityType<? extends Entity> animal, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource random) {
            return worldIn.getBlockState(pos.below()).is(Blocks.GRASS_BLOCK) && worldIn.getRawBrightness(pos, 0) > 8;
        }

    }