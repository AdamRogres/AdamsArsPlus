package com.adamsmods.adamsarsplus.glyphs.effect_glyph;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.entities.BladeProjectile;
import com.hollingsworth.arsnouveau.api.item.inv.*;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.TileCaster;
import com.hollingsworth.arsnouveau.common.block.BasicSpellTurret;
import com.hollingsworth.arsnouveau.common.block.tile.RotatingTurretTile;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.util.FakePlayer;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class EffectConjureBlade extends AbstractEffect {
    public EffectConjureBlade(ResourceLocation tag, String description) {
        super(tag, description);
    }

    public static final EffectConjureBlade INSTANCE = new EffectConjureBlade(new ResourceLocation(AdamsArsPlus.MOD_ID, "glyph_effectconjureblade"), "Conjure Blade");

    public void buildConfig(ForgeConfigSpec.Builder builder) {
        super.buildConfig(builder);
    }

    public void summonBlade(Level world, HitResult rayTraceResult, @Nullable LivingEntity shooter, SpellContext spellContext, SpellStats stats){
        double aoe = stats.getAoeMultiplier();
        int numSplits = 1 + stats.getBuffCount(AugmentSplit.INSTANCE);

        Vec3 pos = rayTraceResult.getLocation();
        List<BladeProjectile> projectiles = new ArrayList<>();

        ItemStack weapon = shooter.getOffhandItem();

        for (int i = 0; i < numSplits + aoe * 2; i++) {
            projectiles.add(new BladeProjectile(world, weapon, shooter, stats.getAmpMultiplier()));
        }
        float velocity = Math.max(0.1f, 0.75f + stats.getAccMultiplier() / 2);
        int opposite = -1;
        int counter = 0;
        int counter2 = 0;

        Vec3 direction = pos.subtract(shooter.position());
        if (spellContext.getCaster() instanceof TileCaster tc) {
            if (tc.getTile() instanceof RotatingTurretTile rotatingTurretTile) {
                direction = rotatingTurretTile.getShootAngle();
            } else {
                direction = new Vec3(tc.getTile().getBlockState().getValue(BasicSpellTurret.FACING).step());
            }
        }
        // Shoot projectiles at target
        if(stats.hasBuff(AugmentSensitive.INSTANCE)){
            for (BladeProjectile proj : projectiles) {

                proj.setPos(pos.add(0, 1, 0));
                if (!(shooter instanceof FakePlayer)) {
                    proj.shootEntity(shooter, shooter.getXRot() - Math.round(counter2 * (1 + aoe) / 2.0) * 5, shooter.getYRot() + Math.round(counter * (1 + aoe) / 2.0) * 5 * opposite - counter2 * 0.5f, 0.0F, velocity, 0.8f);
                } else {
                    proj.shoot(direction.x, direction.y, direction.z, velocity, 0.8F);
                }
                opposite = opposite * -1;
                counter++;
                if(counter > 5){
                    counter = 0;
                    counter2++;
                }

                if(stats.hasBuff(AugmentRandomize.INSTANCE) && shooter instanceof Player){
                    InventoryManager manager = spellContext.getCaster().getInvManager();
                    FilterableItemHandler highestHandler = manager.highestPrefInventory(manager.getInventory(), (i) -> i.getItem() instanceof TieredItem, InteractType.EXTRACT);
                    SlotReference reference = manager.findItemR(highestHandler,(i) -> i.getItem() instanceof TieredItem, InteractType.EXTRACT);
                    if (!reference.isEmpty()) {
                        weapon = reference.getHandler().getStackInSlot(reference.getSlot());
                    }

                } else if(shooter.getMainHandItem().getItem() instanceof TieredItem) {
                    weapon = shooter.getMainHandItem();
                }

                proj.setItem(weapon);
                world.addFreshEntity(proj);
            }
        // Shoot projectiles around target
        } else {
            velocity = velocity * 0.2f;

            for (BladeProjectile proj : projectiles) {
                float xRot = world.random.nextFloat() * 2 * (float) Math.PI;
                float yRot = world.random.nextFloat() * 0.5f * (float) Math.PI;

                double y = Math.sin(yRot) * (aoe + 2);
                double x = Math.cos(xRot) * Math.cos(yRot) * (aoe + 2);
                double z = Math.sin(xRot) * Math.cos(yRot) * (aoe + 2);

                proj.setPos(pos.add(x, y + 1, z));
                proj.setDeltaMovement(-velocity * x, -velocity * y, -velocity * z);
                proj.setXRot(xRot * (float)(180/Math.PI));
                proj.setYRot(yRot * (float)(180/Math.PI));

                if(stats.hasBuff(AugmentRandomize.INSTANCE) && shooter instanceof Player){
                    InventoryManager manager = spellContext.getCaster().getInvManager();
                    FilterableItemHandler highestHandler = manager.highestPrefInventory(manager.getInventory(), (i) -> i.getItem() instanceof TieredItem, InteractType.EXTRACT);
                    SlotReference reference = manager.findItemR(highestHandler,(i) -> i.getItem() instanceof TieredItem, InteractType.EXTRACT);
                    if (!reference.isEmpty()) {
                        weapon = reference.getHandler().getStackInSlot(reference.getSlot());
                    }

                } else if(shooter.getMainHandItem().getItem() instanceof TieredItem) {
                    weapon = shooter.getMainHandItem();
                }

                proj.setItem(weapon);
                world.addFreshEntity(proj);
            }
        }
    }

    @Override
    public void onResolveBlock(BlockHitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        this.summonBlade(world, rayTraceResult, shooter, spellContext,spellStats);
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        this.summonBlade(world, rayTraceResult, shooter, spellContext,spellStats);
    }

    @Nonnull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(
            AugmentAccelerate.INSTANCE,
            AugmentAOE.INSTANCE,
            AugmentSplit.INSTANCE,
            AugmentSensitive.INSTANCE,
            AugmentRandomize.INSTANCE,
            AugmentDecelerate.INSTANCE,
            AugmentAmplify.INSTANCE
        );
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }

    @Override
    public int getDefaultManaCost() {
        return 400;
    }

    @Override
    protected @NotNull Set<SpellSchool> getSchools() {
        return this.setOf(SpellSchools.CONJURATION);
    }
}
