package com.adamsmods.adamsarsplus.glyphs.effect_glyph;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.api.util.DamageUtil;
import com.hollingsworth.arsnouveau.client.particle.ParticleUtil;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAOE;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.setup.registry.CapabilityRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;


import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Set;




public class ManaBomb extends AbstractEffect implements IDamageEffect {
    public ManaBomb(ResourceLocation tag, String description) {
        super(tag, description);
    }

    private int getAmps(int i) {
        switch (i) {
            case 0 -> {
                return 1;
            }
            case 1 -> {
                return 2;
            }
            case 2 -> {
                return 3;
            }
            default -> {
                return 4;
            }
        }
    }
    /*
    damage = (this.DAMAGE.get() * ((this.AMP_VALUE.get())/1.5 * (spellStats.getAmpMultiplier())))*(1 + bonus)/ 1.5;
    damage = (this.DAMAGE.get() * (this.AMP_VALUE.get() * (spellStats.getAmpMultiplier())))*(1 + bonus);
    * */
    public static final ManaBomb INSTANCE = new ManaBomb(new ResourceLocation(AdamsArsPlus.MOD_ID, "glyph_mana_bomb"), "Mana Explosion");
    private int mana = 0;
    float bonus;
    float damage;

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @Nonnull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        int range = 2 + (int) (2 * spellStats.getAoeMultiplier());
        Entity entity = rayTraceResult.getEntity();
        if ((entity instanceof LivingEntity living && world instanceof ServerLevel level)) {
            shooter.getCapability(CapabilityRegistry.MANA_CAPABILITY).ifPresent(a ->
            {
                mana = (int) a.getCurrentMana()/280;
                a.setMana(0);
            });
            bonus = (float) ((float) mana/2.0+Math.log(mana));
            damage = (float) ((this.DAMAGE.get() + ((this.AMP_VALUE.get())/1.5 * (spellStats.getAmpMultiplier())))+(1 + bonus)/ 1.5);

            for (Entity e : world.getEntities(shooter, new AABB(
                    living.position().add(range, range, range), living.position().subtract(range, range, range)))) {
                if (e.equals(living) || e.equals(shooter) ||!(e instanceof LivingEntity)) {
                    continue;
                }
                Vec3 position = e.position();
                attemptDamage(level, shooter, spellStats, spellContext, resolver, e, DamageUtil.source(level, DamageTypes.MAGIC, shooter), damage);
                level.sendParticles(ParticleTypes.WITCH, position.x, position.y, position.z, 50,
                        ParticleUtil.inRange(-0.1, 0.1), ParticleUtil.inRange(-0.1, 0.1), ParticleUtil.inRange(-0.1, 0.1), 3);
                level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, living.getX(), living.getY(), living.getZ(), 100, 0, 0, 0, 1);

                e.invulnerableTime = 40;
            }
            attemptDamage(level, shooter, spellStats, spellContext, resolver, living, DamageUtil.source(level, DamageTypes.MAGIC, shooter), damage);
            level.sendParticles(ParticleTypes.WITCH, living.getX(), living.getY(), living.getZ(), 100, 0, 0, 0, 1);
            living.invulnerableTime = 40;
        }
    }

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {
        super.buildConfig(builder);
        addDamageConfig(builder, 1);
        addAmpConfig(builder, 1);
    }

    @Nonnull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(AugmentAOE.INSTANCE);
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }

    @Override
    public int getDefaultManaCost() {
        return 10;
    }

    @Override
    protected @NotNull Set<SpellSchool> getSchools() {
        return this.setOf(SpellSchools.CONJURATION);
    }
    @Override
    public void addDefaultAugmentLimits(Map<ResourceLocation, Integer> defaults) {
        defaults.put(AugmentAmplify.INSTANCE.getRegistryName(),4);
        defaults.put(AugmentAOE.INSTANCE.getRegistryName(),4);
    }
}