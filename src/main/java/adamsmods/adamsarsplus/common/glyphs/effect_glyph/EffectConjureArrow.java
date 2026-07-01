package adamsmods.adamsarsplus.common.glyphs.effect_glyph;

import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.TileCaster;
import com.hollingsworth.arsnouveau.common.block.BasicSpellTurret;
import com.hollingsworth.arsnouveau.common.block.tile.RotatingTurretTile;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.FakePlayer;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static net.minecraft.world.entity.projectile.AbstractArrow.Pickup.CREATIVE_ONLY;
import static net.minecraft.world.entity.projectile.AbstractArrow.Pickup.DISALLOWED;

public class EffectConjureArrow extends AbstractEffect {

    public static final EffectConjureArrow INSTANCE = new EffectConjureArrow();

    public EffectConjureArrow() {
        super("glyph_effectconjurearrow", "Conjure Arrow");
    }

    public void summonArrow(Level world, HitResult rayTraceResult, @Nullable LivingEntity shooter, SpellContext spellContext, SpellStats stats){
        int numSplits = 1 + stats.getBuffCount(AugmentSplit.INSTANCE);
        int amp = (int)(1 + stats.getAmpMultiplier());

        Vec3 pos = rayTraceResult.getLocation();
        List<Arrow> projectiles = new ArrayList<>();

        for (int i = 0; i < numSplits; i++) {
            projectiles.add(new Arrow(world, shooter, new ItemStack(Items.ARROW), null));
        }
        float velocity = Math.max(0.1f, 0.75f + stats.getAccMultiplier());

        Vec3 direction = pos.subtract(shooter.position());
        if (spellContext.getCaster() instanceof TileCaster tc) {
            if (tc.getTile() instanceof RotatingTurretTile rotatingTurretTile) {
                direction = rotatingTurretTile.getShootAngle();
            } else {
                direction = new Vec3(tc.getTile().getBlockState().getValue(BasicSpellTurret.FACING).step());
            }
        }

        int opposite = -1;
        int counter = 0;
        int counter2 = 0;

        // Shoot projectiles at target
        for (Arrow proj : projectiles) {

            proj.setBaseDamage(amp);
            proj.setPos(pos.add(0, 1, 0));
            proj.setOwner(shooter);
            proj.pickup = CREATIVE_ONLY;

            if (!(shooter instanceof FakePlayer)) {
                this.shootEntity(proj, shooter.getXRot() - Math.round(counter2 / 2.0) * 5, shooter.getYRot() + Math.round(counter / 2.0) * 5 * opposite - counter2 * 0.5f, 0.0F, velocity, 0.8f);
            } else {
                proj.shoot(direction.x, direction.y, direction.z, velocity, 0.8F);
            }

            world.addFreshEntity(proj);

            opposite = opposite * -1;
            counter++;
            if(counter > 5){
                counter = 0;
                counter2++;
            }
        }
    }

    public void shootEntity(Arrow entity, float rotationPitchIn, float rotationYawIn, float pitchOffset, float velocity, float inaccuracy) {
        float f = -Mth.sin(rotationYawIn * ((float)Math.PI / 180F)) * Mth.cos(rotationPitchIn * ((float)Math.PI / 180F));
        float f1 = -Mth.sin((rotationPitchIn + pitchOffset) * ((float)Math.PI / 180F));
        float f2 = Mth.cos(rotationYawIn * ((float)Math.PI / 180F)) * Mth.cos(rotationPitchIn * ((float)Math.PI / 180F));
        entity.shoot((double)f, (double)f1, (double)f2, velocity, inaccuracy);
    }

    @Override
    public void onResolveBlock(BlockHitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        this.summonArrow(world, rayTraceResult, shooter, spellContext,spellStats);
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        this.summonArrow(world, rayTraceResult, shooter, spellContext,spellStats);
    }

    @Nonnull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(
            AugmentAccelerate.INSTANCE,
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
        return 30;
    }

    @Override
    protected @NotNull Set<SpellSchool> getSchools() {
        return this.setOf(SpellSchools.MANIPULATION);
    }
}
