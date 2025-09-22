package com.adamsmods.adamsarsplus.ritual;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.entities.custom.*;
import com.adamsmods.adamsarsplus.glyphs.effect_glyph.EffectTenShadows;
import com.hollingsworth.arsnouveau.api.ritual.AbstractRitual;
import com.hollingsworth.arsnouveau.api.util.BlockUtil;
import com.hollingsworth.arsnouveau.api.util.SpellUtil;
import com.hollingsworth.arsnouveau.common.entity.WildenChimera;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ForgeEventFactory;

import static com.adamsmods.adamsarsplus.datagen.AdamsItemTagsProvider.TS_RITUAL;
import static com.adamsmods.adamsarsplus.registry.ModRegistry.*;

public class RitualTenShadows extends AbstractRitual {
    public static final String ID = "ritual_ten_shadows";

    public RitualTenShadows() {
    }

    protected void tick() {
        WildenChimera.spawnPhaseParticles(this.getPos().above(), this.getWorld(), 1);
        if (this.getWorld().getGameTime() % 20L == 0L) {
            this.incrementProgress();
        }

        int Rank = 0;

        if (this.getWorld().getGameTime() % 60L == 0L && !this.getWorld().isClientSide) {
            for (ItemStack i : getConsumedItems()) {
                if (i.is(MANA_DIAMOND.get())) {
                    Rank += i.getCount();
                }
            }
            if(this.getProgress() >= 8){
                switch(Math.min(Rank, 4)){
                    // Give Glyph (Divine Dogs are free)
                    default -> {
                        this.getWorld().addFreshEntity(new ItemEntity(this.getWorld(), this.getPos().getX(), this.getPos().getY() + 0.5, this.getPos().getZ(), new ItemStack(EffectTenShadows.INSTANCE.getGlyph().asItem()) ));

                        this.setFinished();
                    }
                    // Summon Nue
                    case 1 -> {
                        NueEntity boss = new NueEntity(this.getWorld(), false);
                        this.summon(boss, this.getPos().above(7));

                        for (BlockPos b : BlockPos.betweenClosed(this.getPos().east(5).north(5).above(), this.getPos().west(5).south(5).above(5))) {
                            if (ForgeEventFactory.getMobGriefingEvent(this.getWorld(), boss) && SpellUtil.isCorrectHarvestLevel(4, this.getWorld().getBlockState(b))) {
                                BlockUtil.destroyBlockSafelyWithoutSound(this.getWorld(), b, true);
                            }
                        }
                        this.setFinished();
                    }
                    // Summon Rabbit Escape
                    case 2 -> {
                        RabbitEEntity boss = new RabbitEEntity(this.getWorld(), false, "main");
                        this.summon(boss, this.getPos().above());

                        for (BlockPos b : BlockPos.betweenClosed(this.getPos().east(5).north(5).above(), this.getPos().west(5).south(5).above(5))) {
                            if (ForgeEventFactory.getMobGriefingEvent(this.getWorld(), boss) && SpellUtil.isCorrectHarvestLevel(4, this.getWorld().getBlockState(b))) {
                                BlockUtil.destroyBlockSafelyWithoutSound(this.getWorld(), b, true);
                            }
                        }
                        this.setFinished();
                    }
                    // Summon Round Deer
                    case 3 -> {
                        RDeerEntity boss = new RDeerEntity(this.getWorld(), false);
                        this.summon(boss, this.getPos().above());

                        for (BlockPos b : BlockPos.betweenClosed(this.getPos().east(5).north(5).above(), this.getPos().west(5).south(5).above(5))) {
                            if (ForgeEventFactory.getMobGriefingEvent(this.getWorld(), boss) && SpellUtil.isCorrectHarvestLevel(4, this.getWorld().getBlockState(b))) {
                                BlockUtil.destroyBlockSafelyWithoutSound(this.getWorld(), b, true);
                            }
                        }
                        this.setFinished();
                    }
                    // Summon Mahoraga
                    case 4 -> {

                    }
                }
            }
        }
    }

    public void summon(Mob mob, BlockPos pos) {
        mob.setPos((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
        mob.level().addFreshEntity(mob);
    }

    public String getLangName() {
        return "Challenge Shikigami";
    }

    public String getLangDescription() {
        return "Grants the Ten Shadows Glyph. When augmented with mana diamonds more powerful shikigami can be summoned. If the challenger defeats the shikigami alone they gain the right to summon them through the Ten Shadows Glyph.";
    }

    public boolean canConsumeItem(ItemStack stack) {
        return stack.is(TS_RITUAL);
    }

    public ResourceLocation getRegistryName() {
        return AdamsArsPlus.prefix(ID);
    }
}

