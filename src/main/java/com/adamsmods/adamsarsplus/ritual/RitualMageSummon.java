package com.adamsmods.adamsarsplus.ritual;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.entities.custom.*;
import com.hollingsworth.arsnouveau.api.ritual.AbstractRitual;
import com.hollingsworth.arsnouveau.api.util.BlockUtil;
import com.hollingsworth.arsnouveau.api.util.SpellUtil;
import com.hollingsworth.arsnouveau.common.datagen.ItemTagProvider;
import com.hollingsworth.arsnouveau.common.entity.WildenChimera;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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
                RyanEntity boss = new RyanEntity(this.getWorld());
                this.summon(boss, this.getPos().above());

                for(BlockPos b : BlockPos.betweenClosed(this.getPos().east(5).north(5).above(), this.getPos().west(5).south(5).above(5))) {
                    if (ForgeEventFactory.getMobGriefingEvent(this.getWorld(), boss) && SpellUtil.isCorrectHarvestLevel(4, this.getWorld().getBlockState(b))) {
                        BlockUtil.destroyBlockSafelyWithoutSound(this.getWorld(), b, true);
                    }
                }
                this.setFinished();

            // Summon Cade
            } else if (this.getProgress() >= 8 && isCadeSpawn()) {
                CadeEntity boss = new CadeEntity(this.getWorld());
                this.summon(boss, this.getPos().above());

                for(BlockPos b : BlockPos.betweenClosed(this.getPos().east(5).north(5).above(), this.getPos().west(5).south(5).above(5))) {
                    if (ForgeEventFactory.getMobGriefingEvent(this.getWorld(), boss) && SpellUtil.isCorrectHarvestLevel(4, this.getWorld().getBlockState(b))) {
                        BlockUtil.destroyBlockSafelyWithoutSound(this.getWorld(), b, true);
                    }
                }
                this.setFinished();

            // Summon Nick
            } else if (this.getProgress() >= 8 && isNickSpawn()) {
                NickEntity boss = new NickEntity(this.getWorld());
                this.summon(boss, this.getPos().above());

                for(BlockPos b : BlockPos.betweenClosed(this.getPos().east(5).north(5).above(), this.getPos().west(5).south(5).above(5))) {
                    if (ForgeEventFactory.getMobGriefingEvent(this.getWorld(), boss) && SpellUtil.isCorrectHarvestLevel(4, this.getWorld().getBlockState(b))) {
                        BlockUtil.destroyBlockSafelyWithoutSound(this.getWorld(), b, true);
                    }
                }
                this.setFinished();

            // Summon Cam
            } else if (this.getProgress() >= 8 && isCamrSpawn()) {
                CamEntity boss = new CamEntity(this.getWorld());
                this.summon(boss, this.getPos().above());

                for(BlockPos b : BlockPos.betweenClosed(this.getPos().east(5).north(5).above(), this.getPos().west(5).south(5).above(5))) {
                    if (ForgeEventFactory.getMobGriefingEvent(this.getWorld(), boss) && SpellUtil.isCorrectHarvestLevel(4, this.getWorld().getBlockState(b))) {
                        BlockUtil.destroyBlockSafelyWithoutSound(this.getWorld(), b, true);
                    }
                }
                this.setFinished();

            // Summon Matt
            } else if (this.getProgress() >= 8 && isMattSpawn()) {
                MattEntity boss = new MattEntity(this.getWorld());
                this.summon(boss, this.getPos().above());

                for(BlockPos b : BlockPos.betweenClosed(this.getPos().east(5).north(5).above(), this.getPos().west(5).south(5).above(5))) {
                    if (ForgeEventFactory.getMobGriefingEvent(this.getWorld(), boss) && SpellUtil.isCorrectHarvestLevel(4, this.getWorld().getBlockState(b))) {
                        BlockUtil.destroyBlockSafelyWithoutSound(this.getWorld(), b, true);
                    }
                }
                this.setFinished();

            // Summon Adam
            } else if (this.getProgress() >= 8 && isAdamSpawn()) {
                AdamEntity boss = new AdamEntity(this.getWorld());
                this.summon(boss, this.getPos().above());

                for(BlockPos b : BlockPos.betweenClosed(this.getPos().east(5).north(5).above(), this.getPos().west(5).south(5).above(5))) {
                    if (ForgeEventFactory.getMobGriefingEvent(this.getWorld(), boss) && SpellUtil.isCorrectHarvestLevel(4, this.getWorld().getBlockState(b))) {
                        BlockUtil.destroyBlockSafelyWithoutSound(this.getWorld(), b, true);
                    }
                }
                this.setFinished();

            }
        }
    }


    public boolean isRyanSpawn() {
        return this.didConsumeItem(ItemsRegistry.FIRE_ESSENCE.asItem());
    }

    public boolean isCadeSpawn() {
        return this.didConsumeItem(ItemsRegistry.WATER_ESSENCE.asItem());
    }

    public boolean isNickSpawn() {
        return this.didConsumeItem(ItemsRegistry.EARTH_ESSENCE.asItem());
    }

    public boolean isCamrSpawn() {
        return this.didConsumeItem(ItemsRegistry.AIR_ESSENCE.asItem()) && this.didConsumeItem(ELEMENTAL_SOUL.get());
    }

    public boolean isMattSpawn() {
        return this.didConsumeItem(ItemsRegistry.CONJURATION_ESSENCE.asItem()) && this.didConsumeItem(ELEMENTAL_SOUL.get());
    }

    public boolean isAdamSpawn() {
        return this.didConsumeItem(ItemsRegistry.ABJURATION_ESSENCE.asItem()) && this.didConsumeItem(TRUE_ELEMENTAL_SOUL.get());
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
