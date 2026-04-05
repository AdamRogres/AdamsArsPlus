package adamsmods.adamsarsplus.common.entity;

import com.hollingsworth.arsnouveau.setup.registry.ModPotions;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityStruckByLightningEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static adamsmods.adamsarsplus.registry.ModEntities.DIVINE_SMITE;

public class EntityDivineSmite extends LightningBolt {
    float aoe;
    boolean sensitive;
    private int lightningState;
    public long boltVertex;
    private int boltLivingTime;
    private boolean effectOnly;
    List<Integer> hitEntities = new ArrayList();
    @Nullable
    private ServerPlayer caster;
    public float amps;
    public int extendTimes;
    public float ampScalar;

    public EntityDivineSmite(EntityType<? extends LightningBolt> type, Level world) {
        super(type,world);
        this.noCulling = true;
        this.lightningState = 2;
        this.boltVertex = 0; //this.random.nextLong();
        this.boltLivingTime = this.random.nextInt(3) + 1;
    }

    public void setAoe(float amount){
        aoe = amount;
    }
    public void setSensitive(boolean bool){
        sensitive = bool;
    }

    public void setVisualOnly(boolean effectOnly) {
        this.effectOnly = effectOnly;
    }

    public SoundSource getSoundSource() {
        return SoundSource.WEATHER;
    }

    public void setCause(@Nullable ServerPlayer casterIn) {
        this.caster = casterIn;
    }

    public void tick() {
        this.baseTick();
        if (this.lightningState == 2) {
            Difficulty difficulty = this.level().getDifficulty();
            this.level().playSound((Player)null, this.getX(), this.getY(), this.getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.WEATHER, 1.0F, 0.8F + this.random.nextFloat() * 0.2F);
            this.level().playSound((Player)null, this.getX(), this.getY(), this.getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.WEATHER, 1.0F, 0.5F + this.random.nextFloat() * 0.2F);
        }

        --this.lightningState;
        if (this.lightningState < 0) {
            if (this.boltLivingTime == 0) {
                this.remove(RemovalReason.DISCARDED);
            } else if (this.lightningState < -this.random.nextInt(10)) {
                --this.boltLivingTime;
                this.lightningState = 1;
                this.boltVertex = 0; //this.random.nextLong();
            }
        }

        if (this.lightningState >= 0) {
            if (!(this.level() instanceof ServerLevel)) {
                this.level().setSkyFlashTime(2);
            } else if (!this.effectOnly) {
                List<Entity> list = this.level().getEntities(this, new AABB(this.getX() - (double)3.0F, this.getY() - (double)3.0F, this.getZ() - (double)3.0F, this.getX() + (double)3.0F, this.getY() + (double)6.0F + (double)3.0F, this.getZ() + (double)3.0F), Entity::isAlive);

                for (Entity entity : list) {
                    if (!net.neoforged.neoforge.event.EventHooks.onEntityStruckByLightning(entity, this)) {
                        float origDamage = this.getDamage();
                        this.setDamage(this.getDamage(entity));
                        EntityStruckByLightningEvent event = new EntityStruckByLightningEvent(entity, this);
                        NeoForge.EVENT_BUS.post(event);
                        if (event.isCanceled())
                            continue;
                        entity.thunderHit((ServerLevel) this.level, this);
                        this.setDamage(origDamage);

                        if (!this.level.isClientSide && !this.hitEntities.contains(entity.getId()) && entity instanceof LivingEntity livingEntity && livingEntity.getEffect(ModPotions.SHOCKED_EFFECT)  != null) {
                            MobEffectInstance effectInstance = ((LivingEntity)entity).getEffect(ModPotions.SHOCKED_EFFECT);
                            int amp = effectInstance != null ? effectInstance.getAmplifier() : -1;

                            if(amp == 0){
                                ((LivingEntity) entity).removeEffect(ModPotions.SHOCKED_EFFECT);
                            } else{
                                ((LivingEntity) entity).removeEffect(ModPotions.SHOCKED_EFFECT);
                                ((LivingEntity)entity).addEffect(new MobEffectInstance(ModPotions.SHOCKED_EFFECT, 200 + 200 * this.extendTimes, Math.min(2, amp - 1)));
                            }
                        }

                        if (!level.isClientSide && !hitEntities.contains(entity.getId()))
                            hitEntities.add(entity.getId());

                    }
                }

                if (this.caster != null) {
                    CriteriaTriggers.CHANNELED_LIGHTNING.trigger(this.caster, list);
                }
            }
        }

    }

    private void igniteBlocks(int extraIgnitions) {

    }

    public float getDamage(Entity entity) {
        float baseDamage = this.getDamage() + this.ampScalar * this.amps;
        int multiplier = 1;

        if(entity instanceof LivingEntity livingEntity && livingEntity.getEffect(ModPotions.SHOCKED_EFFECT)  != null){
            MobEffectInstance effectInstance = (livingEntity).getEffect(ModPotions.SHOCKED_EFFECT);
            multiplier = (effectInstance != null ? effectInstance.getAmplifier() : -1) + 1;
        }

        return baseDamage * (float)multiplier;
    }


    @OnlyIn(Dist.CLIENT)
    public boolean shouldRenderAtSqrDistance(double distance) {
        double d0 = (double)64.0F * getViewScale();
        return distance < d0 * d0;
    }

    protected void defineSynchedData() {
    }

    protected void readAdditionalSaveData(CompoundTag compound) {
    }

    protected void addAdditionalSaveData(CompoundTag compound) {
    }

    public EntityType<?> getType() { return (EntityType) DIVINE_SMITE.get(); }

}
