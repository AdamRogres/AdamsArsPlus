package adamsmods.adamsarsplus.common.glyphs.effect_glyph;

import adamsmods.adamsarsplus.AdamsArsPlus;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.LivingCaster;
import com.hollingsworth.arsnouveau.api.util.BlockUtil;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAOE;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDurationDown;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentExtendTime;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentPierce;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static adamsmods.adamsarsplus.registry.ModPotions.SIMPLE_DOMAIN_EFFECT;

public class EffectSimpleDomain extends AbstractEffect implements IPotionEffect {

    public EffectSimpleDomain() {
        super("glyph_effectsimpledomain", "Simple Domain");
    }
    public static EffectSimpleDomain INSTANCE = new EffectSimpleDomain();

    @Override
    public void applyConfigPotion(LivingEntity entity, Holder<MobEffect> potionEffect, SpellStats spellStats) {
        this.applyConfigPotion(entity, potionEffect, spellStats, false);
    }

    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        Entity var8 = rayTraceResult.getEntity();
        if (var8 instanceof LivingEntity living) {
            this.applyConfigPotion(living, SIMPLE_DOMAIN_EFFECT, spellStats);

            if(spellStats.hasBuff(AugmentPierce.INSTANCE)){
                spellContext.setCanceled(true);
                if (spellContext.getCurrentIndex() >= spellContext.getSpell().size())
                    return;
                Spell newSpell = spellContext.getRemainingSpell();
                SpellContext newContext = spellContext.clone().withSpell(newSpell);

                startEffect(living, world, spellStats, newContext, resolver, null );
            }
        }

    }

    public void startEffect(LivingEntity playerEntity, Level world, SpellStats spellStats, SpellContext context, SpellResolver resolver, Entity mob) {
        int baseDuration = 20;
        int targetDelay = 2;

        int finalDuration = 60 + baseDuration * (int) (spellStats.getDurationMultiplier());

        //target entities with lowered tick rate
        AdamsArsPlus.setInterval(() -> {

                int radius = 1 + (int) spellStats.getAoeMultiplier();
                BlockPos entityBlockPos = playerEntity.blockPosition();
                for (LivingEntity entity : world.getEntitiesOfClass(LivingEntity.class, new AABB(entityBlockPos).inflate(radius, radius, radius))) {
                    if (entity.equals(playerEntity)) {
                        continue;
                    }

                    BlockPos pos = entity.blockPosition();

                    if (BlockUtil.distanceFrom(entityBlockPos, pos) <= radius + 0.5) {
                        EntityHitResult entityHitResult = new EntityHitResult(entity);
                        EntitySpellResolver newResolver = new EntitySpellResolver(new SpellContext(entity.level(), context.getSpell(), entity, new LivingCaster(entity)));
                        newResolver.onResolveEffect(world, entityHitResult);

                        playerEntity.removeEffect(SIMPLE_DOMAIN_EFFECT);
                        break;
                    }
                }
        }, targetDelay, finalDuration, () -> !(playerEntity.hasEffect(SIMPLE_DOMAIN_EFFECT)));
    }

    @Override
    public void buildConfig(ModConfigSpec.Builder builder) {
        super.buildConfig(builder);
        this.addPotionConfig(builder, 3);
        this.addExtendTimeConfig(builder, 1);
    }

    public int getDefaultManaCost() {
        return 600;
    }

    public String getBookDescription() {
        return "Grants a simple domain that protects against the surehit of domains. ";
    }

    public SpellTier defaultTier() {
        return SpellTier.TWO;
    }

    public @NotNull Set<AbstractAugment> getCompatibleAugments() {
        return this.augmentSetOf(new AbstractAugment[]{AugmentExtendTime.INSTANCE, AugmentDurationDown.INSTANCE, AugmentPierce.INSTANCE, AugmentAOE.INSTANCE});
    }

    public @NotNull Set<SpellSchool> getSchools() {
        return this.setOf(new SpellSchool[]{SpellSchools.ABJURATION});
    }

    public int getBaseDuration() {
        return this.POTION_TIME == null ? 3 : 3;
    }

    public int getExtendTimeDuration() {
        return this.EXTEND_TIME == null ? 1 : 1;
    }
}

