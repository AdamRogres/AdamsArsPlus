package com.adamsmods.adamsarsplus.ritual;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.datagen.AdamsStructureTagProvider;
import com.adamsmods.adamsarsplus.entities.custom.*;
import com.hollingsworth.arsnouveau.api.ritual.AbstractRitual;
import com.hollingsworth.arsnouveau.api.util.BlockUtil;
import com.hollingsworth.arsnouveau.api.util.SpellUtil;
import com.hollingsworth.arsnouveau.common.datagen.ItemTagProvider;
import com.hollingsworth.arsnouveau.common.entity.WildenChimera;
import com.hollingsworth.arsnouveau.common.util.PortUtil;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.ArrayList;
import java.util.List;

import static com.adamsmods.adamsarsplus.datagen.AdamsItemTagsProvider.MAGE_RITUAL;
import static com.adamsmods.adamsarsplus.registry.ModRegistry.*;
import static com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry.*;

public class RitualMageSummon extends AbstractRitual {
    public static final String ID = "ritual_mage_summon";

    public RitualMageSummon() {
    }

    protected void tick() {
        WildenChimera.spawnPhaseParticles(this.getPos().above(), this.getWorld(), 1);
        if (this.getWorld().getGameTime() % 20L == 0L) {
            this.incrementProgress();
        }

        if (this.getWorld().getGameTime() % 60L == 0L && !this.getWorld().isClientSide) {
            // Summon Mages
            if (!this.isRyanSpawn() && !this.isCadeSpawn() && !this.isNickSpawn() && !this.isCamrSpawn() && !this.isMattSpawn() && !this.isAdamSpawn()) {

                BlockPos summonPos = this.getPos().above().east(this.rand.nextInt(3) - this.rand.nextInt(6)).north(this.rand.nextInt(3) - this.rand.nextInt(6));
                Object var10000;

                var10000 = new MysteriousMageEntity(this.getWorld());

                Mob mobEntity = (Mob)var10000;
                this.summon(mobEntity, summonPos);
                if (this.getProgress() >= 15) {
                    this.setFinished();
                }

            // Summon Ryan
            } else if (this.getProgress() >= 8 && isRyanSpawn()) {

                if (this.getWorld() instanceof ServerLevel) {
                    ServerLevel serverLevel = (ServerLevel) this.getWorld();
                    BlockPos structurePos = serverLevel.findNearestMapStructure(AdamsStructureTagProvider.IP_TAG, this.getPos(), 100, false);
                    if (structurePos != null){
                        if(checkDistance(this.getPos(), serverLevel, AdamsStructureTagProvider.IP_TAG)) {
                            RyanEntity boss = new RyanEntity(this.getWorld());
                            this.summon(boss, this.getPos().above());

                            for (BlockPos b : BlockPos.betweenClosed(this.getPos().east(5).north(5).above(), this.getPos().west(5).south(5).above(5))) {
                                if (ForgeEventFactory.getMobGriefingEvent(this.getWorld(), boss) && SpellUtil.isCorrectHarvestLevel(4, this.getWorld().getBlockState(b))) {
                                    BlockUtil.destroyBlockSafelyWithoutSound(this.getWorld(), b, true);
                                }
                            }
                        } else {
                            for (Entity entity : this.getWorld().getEntities(null, new AABB(new Vec3(this.getPos().getX() + 48, this.getPos().getY() + 48, this.getPos().getZ() + 48), new Vec3(this.getPos().getX() - 48, this.getPos().getY() - 48, this.getPos().getZ() - 48)))) {
                                if(entity instanceof Player player){
                                    PortUtil.sendMessageNoSpam(player, Component.translatable("adamsarsplus.magesummon.nostructure"));
                                }
                            }

                        }
                    } else {
                        for (Entity entity : this.getWorld().getEntities(null, new AABB(new Vec3(this.getPos().getX() + 48, this.getPos().getY() + 48, this.getPos().getZ() + 48), new Vec3(this.getPos().getX() - 48, this.getPos().getY() - 48, this.getPos().getZ() - 48)))) {
                            if(entity instanceof Player player){
                                PortUtil.sendMessageNoSpam(player, Component.translatable("adamsarsplus.magesummon.nostructuredim"));
                            }
                        }

                    }
                }

                this.setFinished();

            // Summon Cade
            } else if (this.getProgress() >= 8 && isCadeSpawn()) {

                if (this.getWorld() instanceof ServerLevel) {
                    ServerLevel serverLevel = (ServerLevel) this.getWorld();
                    BlockPos structurePos = serverLevel.findNearestMapStructure(AdamsStructureTagProvider.FL_TAG, this.getPos(), 100, false);
                    if (structurePos != null){
                        if(checkDistance(this.getPos(), serverLevel, AdamsStructureTagProvider.FL_TAG)) {
                            CadeEntity boss = new CadeEntity(this.getWorld());
                            this.summon(boss, this.getPos().above());

                            for (BlockPos b : BlockPos.betweenClosed(this.getPos().east(5).north(5).above(), this.getPos().west(5).south(5).above(5))) {
                                if (ForgeEventFactory.getMobGriefingEvent(this.getWorld(), boss) && SpellUtil.isCorrectHarvestLevel(4, this.getWorld().getBlockState(b))) {
                                    BlockUtil.destroyBlockSafelyWithoutSound(this.getWorld(), b, true);
                                }
                            }
                        } else {
                            for (Entity entity : this.getWorld().getEntities(null, new AABB(new Vec3(this.getPos().getX() + 48, this.getPos().getY() + 48, this.getPos().getZ() + 48), new Vec3(this.getPos().getX() - 48, this.getPos().getY() - 48, this.getPos().getZ() - 48)))) {
                                if(entity instanceof Player player){
                                    PortUtil.sendMessageNoSpam(player, Component.translatable("adamsarsplus.magesummon.nostructure"));
                                }
                            }

                        }
                    } else {
                        for (Entity entity : this.getWorld().getEntities(null, new AABB(new Vec3(this.getPos().getX() + 48, this.getPos().getY() + 48, this.getPos().getZ() + 48), new Vec3(this.getPos().getX() - 48, this.getPos().getY() - 48, this.getPos().getZ() - 48)))) {
                            if(entity instanceof Player player){
                                PortUtil.sendMessageNoSpam(player, Component.translatable("adamsarsplus.magesummon.nostructuredim"));
                            }
                        }

                    }
                }

                this.setFinished();

            // Summon Nick
            } else if (this.getProgress() >= 8 && isNickSpawn()) {

                if (this.getWorld() instanceof ServerLevel) {
                    ServerLevel serverLevel = (ServerLevel) this.getWorld();
                    BlockPos structurePos = serverLevel.findNearestMapStructure(AdamsStructureTagProvider.OB_TAG, this.getPos(), 100, false);
                    if (structurePos != null){
                        if(checkDistance(this.getPos(), serverLevel, AdamsStructureTagProvider.OB_TAG)) {
                            NickEntity boss = new NickEntity(this.getWorld());
                            this.summon(boss, this.getPos().above());

                            for (BlockPos b : BlockPos.betweenClosed(this.getPos().east(5).north(5).above(), this.getPos().west(5).south(5).above(5))) {
                                if (ForgeEventFactory.getMobGriefingEvent(this.getWorld(), boss) && SpellUtil.isCorrectHarvestLevel(4, this.getWorld().getBlockState(b))) {
                                    BlockUtil.destroyBlockSafelyWithoutSound(this.getWorld(), b, true);
                                }
                            }
                        } else {
                            for (Entity entity : this.getWorld().getEntities(null, new AABB(new Vec3(this.getPos().getX() + 48, this.getPos().getY() + 48, this.getPos().getZ() + 48), new Vec3(this.getPos().getX() - 48, this.getPos().getY() - 48, this.getPos().getZ() - 48)))) {
                                if(entity instanceof Player player){
                                    PortUtil.sendMessageNoSpam(player, Component.translatable("adamsarsplus.magesummon.nostructure"));
                                }
                            }

                        }
                    } else {
                        for (Entity entity : this.getWorld().getEntities(null, new AABB(new Vec3(this.getPos().getX() + 48, this.getPos().getY() + 48, this.getPos().getZ() + 48), new Vec3(this.getPos().getX() - 48, this.getPos().getY() - 48, this.getPos().getZ() - 48)))) {
                            if(entity instanceof Player player){
                                PortUtil.sendMessageNoSpam(player, Component.translatable("adamsarsplus.magesummon.nostructuredim"));
                            }
                        }

                    }
                }

                this.setFinished();

            // Summon Cam
            } else if (this.getProgress() >= 8 && isCamrSpawn()) {

                if (this.getWorld() instanceof ServerLevel) {
                    ServerLevel serverLevel = (ServerLevel) this.getWorld();
                    BlockPos structurePos = serverLevel.findNearestMapStructure(AdamsStructureTagProvider.NR_TAG, this.getPos(), 100, false);
                    if (structurePos != null){
                        if(checkDistance(this.getPos(), serverLevel, AdamsStructureTagProvider.NR_TAG)) {
                            CamEntity boss = new CamEntity(this.getWorld());
                            this.summon(boss, this.getPos().above());

                            for (BlockPos b : BlockPos.betweenClosed(this.getPos().east(5).north(5).above(), this.getPos().west(5).south(5).above(5))) {
                                if (ForgeEventFactory.getMobGriefingEvent(this.getWorld(), boss) && SpellUtil.isCorrectHarvestLevel(4, this.getWorld().getBlockState(b))) {
                                    BlockUtil.destroyBlockSafelyWithoutSound(this.getWorld(), b, true);
                                }
                            }
                        } else {
                            for (Entity entity : this.getWorld().getEntities(null, new AABB(new Vec3(this.getPos().getX() + 48, this.getPos().getY() + 48, this.getPos().getZ() + 48), new Vec3(this.getPos().getX() - 48, this.getPos().getY() - 48, this.getPos().getZ() - 48)))) {
                                if(entity instanceof Player player){
                                    PortUtil.sendMessageNoSpam(player, Component.translatable("adamsarsplus.magesummon.nostructure"));
                                }
                            }

                        }
                    } else {
                        for (Entity entity : this.getWorld().getEntities(null, new AABB(new Vec3(this.getPos().getX() + 48, this.getPos().getY() + 48, this.getPos().getZ() + 48), new Vec3(this.getPos().getX() - 48, this.getPos().getY() - 48, this.getPos().getZ() - 48)))) {
                            if(entity instanceof Player player){
                                PortUtil.sendMessageNoSpam(player, Component.translatable("adamsarsplus.magesummon.nostructuredim"));
                            }
                        }

                    }
                }

                this.setFinished();

            // Summon Matt
            } else if (this.getProgress() >= 8 && isMattSpawn()) {

                if (this.getWorld() instanceof ServerLevel) {
                    ServerLevel serverLevel = (ServerLevel) this.getWorld();
                    BlockPos structurePos = serverLevel.findNearestMapStructure(AdamsStructureTagProvider.HM_TAG, this.getPos(), 100, false);
                    if (structurePos != null){
                        if(checkDistance(this.getPos(), serverLevel, AdamsStructureTagProvider.HM_TAG)) {
                            MattEntity boss = new MattEntity(this.getWorld());
                            this.summon(boss, this.getPos().above());

                            for (BlockPos b : BlockPos.betweenClosed(this.getPos().east(5).north(5).above(), this.getPos().west(5).south(5).above(5))) {
                                if (ForgeEventFactory.getMobGriefingEvent(this.getWorld(), boss) && SpellUtil.isCorrectHarvestLevel(4, this.getWorld().getBlockState(b))) {
                                    BlockUtil.destroyBlockSafelyWithoutSound(this.getWorld(), b, true);
                                }
                            }
                        } else {
                            for (Entity entity : this.getWorld().getEntities(null, new AABB(new Vec3(this.getPos().getX() + 48, this.getPos().getY() + 48, this.getPos().getZ() + 48), new Vec3(this.getPos().getX() - 48, this.getPos().getY() - 48, this.getPos().getZ() - 48)))) {
                                if(entity instanceof Player player){
                                    PortUtil.sendMessageNoSpam(player, Component.translatable("adamsarsplus.magesummon.nostructure"));
                                }
                            }

                        }
                    } else {
                        for (Entity entity : this.getWorld().getEntities(null, new AABB(new Vec3(this.getPos().getX() + 48, this.getPos().getY() + 48, this.getPos().getZ() + 48), new Vec3(this.getPos().getX() - 48, this.getPos().getY() - 48, this.getPos().getZ() - 48)))) {
                            if(entity instanceof Player player){
                                PortUtil.sendMessageNoSpam(player, Component.translatable("adamsarsplus.magesummon.nostructuredim"));
                            }
                        }

                    }
                }

                this.setFinished();

            // Summon Adam
            } else if (this.getProgress() >= 8 && isAdamSpawn()) {

                if (this.getWorld() instanceof ServerLevel) {
                    ServerLevel serverLevel = (ServerLevel) this.getWorld();
                    BlockPos structurePos = serverLevel.findNearestMapStructure(AdamsStructureTagProvider.VF_TAG, this.getPos(), 100, false);
                    if (structurePos != null){
                        if(checkDistance(this.getPos(), serverLevel, AdamsStructureTagProvider.VF_TAG)) {
                            AdamEntity boss = new AdamEntity(this.getWorld());
                            this.summon(boss, this.getPos().above());

                            for (BlockPos b : BlockPos.betweenClosed(this.getPos().east(5).north(5).above(), this.getPos().west(5).south(5).above(5))) {
                                if (ForgeEventFactory.getMobGriefingEvent(this.getWorld(), boss) && SpellUtil.isCorrectHarvestLevel(4, this.getWorld().getBlockState(b))) {
                                    BlockUtil.destroyBlockSafelyWithoutSound(this.getWorld(), b, true);
                                }
                            }
                        } else {
                            for (Entity entity : this.getWorld().getEntities(null, new AABB(new Vec3(this.getPos().getX() + 48, this.getPos().getY() + 48, this.getPos().getZ() + 48), new Vec3(this.getPos().getX() - 48, this.getPos().getY() - 48, this.getPos().getZ() - 48)))) {
                                if(entity instanceof Player player){
                                    PortUtil.sendMessageNoSpam(player, Component.translatable("adamsarsplus.magesummon.nostructure"));
                                }
                            }

                        }
                    } else {
                        for (Entity entity : this.getWorld().getEntities(null, new AABB(new Vec3(this.getPos().getX() + 48, this.getPos().getY() + 48, this.getPos().getZ() + 48), new Vec3(this.getPos().getX() - 48, this.getPos().getY() - 48, this.getPos().getZ() - 48)))) {
                            if(entity instanceof Player player){
                                PortUtil.sendMessageNoSpam(player, Component.translatable("adamsarsplus.magesummon.nostructuredim"));
                            }
                        }

                    }
                }

                this.setFinished();
            }
        }
    }

    public boolean checkDistance(BlockPos pos, ServerLevel world, TagKey<Structure> tagKey){
        return world.structureManager().getStructureWithPieceAt(pos, tagKey).isValid();
    }

    public boolean isRyanSpawn() {
        return this.didConsumeItem(EYE_OF_FLAME.get());
    }

    public boolean isCadeSpawn() {
        return this.didConsumeItem(EYE_OF_FROST.get());
    }

    public boolean isNickSpawn() {
        return this.didConsumeItem(EYE_OF_EARTH.get());
    }

    public boolean isCamrSpawn() {
        return this.didConsumeItem(EYE_OF_LIGHTNING.get());
    }

    public boolean isMattSpawn() {
        return this.didConsumeItem(EYE_OF_HOLY.get());
    }

    public boolean isAdamSpawn() {
        return this.didConsumeItem(EYE_OF_VOID.get());
    }

    public void summon(Mob mob, BlockPos pos) {
        mob.setPos((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
        mob.level().addFreshEntity(mob);
    }

    public String getLangName() {
        return "Summon Mage";
    }

    public String getLangDescription() {
        return "Without augments, this ritual will summon a random variety of Mysterious Mages. When augmented with a fire/water/earth essence a powerful mage will be summoned. Using an air or conjuration essence requires an elemental soul and abjuration essence requires a true elemental soul. Note: Mages can be highly destructive.";
    }

    public boolean canConsumeItem(ItemStack stack) {
        return stack.is(MAGE_RITUAL);
    }

    public ResourceLocation getRegistryName() {
        return AdamsArsPlus.prefix(ID);
    }
}
