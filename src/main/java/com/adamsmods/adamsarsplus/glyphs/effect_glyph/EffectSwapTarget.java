package com.adamsmods.adamsarsplus.glyphs.effect_glyph;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.IWrappedCaster;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.LivingCaster;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.PlayerCaster;
import com.hollingsworth.arsnouveau.api.util.BlockUtil;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAOE;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDampen;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectLinger;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectToss;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectWall;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Set;
import java.util.function.Predicate;

public class EffectSwapTarget extends AbstractEffect {
    public static EffectSwapTarget INSTANCE = new EffectSwapTarget(new ResourceLocation(AdamsArsPlus.MOD_ID, "glyph_effectswaptarget"), "Swap Target");

    public EffectSwapTarget(ResourceLocation tag, String description) {
        super(tag, description);
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        spellContext.setCanceled(true);
        Entity entity = rayTraceResult.getEntity();
        IWrappedCaster wrappedCaster = entity instanceof Player pCaster ? new PlayerCaster(pCaster) : new LivingCaster(shooter);

        if (entity instanceof LivingEntity && world instanceof ServerLevel) {

            if (spellContext.getCurrentIndex() < spellContext.getSpell().recipe.size() && BlockUtil.destroyRespectsClaim(shooter, world, entity.blockPosition().below())) {
                Spell newSpell = spellContext.getRemainingSpell();

                SpellContext newContext = (new SpellContext(world, newSpell, (LivingEntity) entity, wrappedCaster));
                SpellResolver resolver2 = new SpellResolver(newContext);
                resolver2.onResolveEffect(entity.getCommandSenderWorld(),new EntityHitResult(shooter));

            }
        }
    }

    public int getDefaultManaCost() {
        return 800;
    }

    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return this.augmentSetOf(new AbstractAugment[]{});
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }

    @Override
    public Set<SpellSchool> getSchools() {
        return this.setOf(new SpellSchool[]{SpellSchools.MANIPULATION});
    }

    @Override
    protected void addDefaultInvalidCombos(Set<ResourceLocation> defaults) {
        defaults.add(EffectToss.INSTANCE.getRegistryName());
    }
}