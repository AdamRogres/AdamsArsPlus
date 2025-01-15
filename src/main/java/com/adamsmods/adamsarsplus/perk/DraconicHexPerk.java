package com.adamsmods.adamsarsplus.perk;

import com.adamsmods.api.APerkSlot;
import com.hollingsworth.arsnouveau.api.perk.IEffectResolvePerk;
import com.hollingsworth.arsnouveau.api.perk.Perk;
import com.hollingsworth.arsnouveau.api.perk.PerkInstance;
import com.hollingsworth.arsnouveau.api.perk.PerkSlot;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.setup.registry.ModPotions;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import static com.adamsmods.adamsarsplus.AdamsArsPlus.MOD_ID;
import static com.adamsmods.adamsarsplus.ArsNouveauRegistry.ERUPTION_EFFECT;

public class DraconicHexPerk extends Perk implements IEffectResolvePerk {
    public static final DraconicHexPerk INSTANCE = new DraconicHexPerk(new ResourceLocation(MOD_ID, "thread_draconic"));
    public DraconicHexPerk(ResourceLocation key) { super(key); }

    public String getLangName() {
        return "Draconic Hex";
    }

    public String getLangDescription() {
        return "Inflicts a draconic curse upon spell targets when using a damaging spell.";
    }

    public PerkSlot minimumSlot() {
        return APerkSlot.FOUR;
    }

    public String getName() {
        return Component.translatable("item.adamsarsplus.thread_draconic").getString();
    }
    public void onPreResolve(HitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver, AbstractEffect effect, PerkInstance perkInstance) {
        if (effect instanceof IDamageEffect damageEffect) {
            if (rayTraceResult instanceof EntityHitResult entityHitResult) {
                Entity var12 = entityHitResult.getEntity();
                if (var12 instanceof LivingEntity livingEntity) {
                    if (damageEffect.canDamage(shooter, spellStats, spellContext, resolver, entityHitResult.getEntity()) && shooter != entityHitResult.getEntity()) {
                        livingEntity.addEffect(new MobEffectInstance((MobEffect) ModPotions.HEX_EFFECT.get(), perkInstance.getSlot().value * 10 * 20, perkInstance.getSlot().value - 4));
                        livingEntity.addEffect(new MobEffectInstance((MobEffect) ERUPTION_EFFECT.get(), perkInstance.getSlot().value * 10 * 20, 0));
                    }
                }
            }
        }
    }

    public void onPostResolve(HitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver, AbstractEffect effect, PerkInstance perkInstance) {
    }
}
