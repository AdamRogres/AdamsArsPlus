package com.adamsmods.adamsarsplus.entities;

import com.google.common.collect.Sets;
import com.hollingsworth.arsnouveau.setup.registry.ModEntities;
import com.hollingsworth.arsnouveau.setup.registry.ModPotions;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import static com.adamsmods.adamsarsplus.entities.AdamsModEntities.DIVINE_SMITE;
import static com.hollingsworth.arsnouveau.setup.registry.ModPotions.SHOCKED_EFFECT;
import static net.minecraft.world.entity.Entity.getViewScale;

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

                for(Entity entity : list) {
                    if (!ForgeEventFactory.onEntityStruckByLightning(entity, this)) {
                        float origDamage = this.getDamage();
                        this.setDamage(this.getDamage(entity));
                        EntityStruckByLightningEvent event = new EntityStruckByLightningEvent(entity, this);
                        MinecraftForge.EVENT_BUS.post(event);
                        if (!event.isCanceled()) {
                            entity.thunderHit((ServerLevel)this.level(), this);
                            this.setDamage(origDamage);


                            if (!this.level().isClientSide && !this.hitEntities.contains(entity.getId()) && entity instanceof LivingEntity livingEntity && livingEntity.getEffect((MobEffect) SHOCKED_EFFECT.get())  != null) {
                                MobEffectInstance effectInstance = ((LivingEntity)entity).getEffect((MobEffect) SHOCKED_EFFECT.get());
                                int amp = effectInstance != null ? effectInstance.getAmplifier() : -1;

                                if(amp == 0){
                                    ((LivingEntity) entity).removeEffect((MobEffect) SHOCKED_EFFECT.get());
                                } else{
                                    ((LivingEntity) entity).removeEffect((MobEffect) SHOCKED_EFFECT.get());
                                    ((LivingEntity)entity).addEffect(new MobEffectInstance((MobEffect) SHOCKED_EFFECT.get(), 200 + 200 * this.extendTimes, Math.min(2, amp - 1)));
                                }
                            }

                            if (!this.level().isClientSide && !this.hitEntities.contains(entity.getId())) {
                                this.hitEntities.add(entity.getId());
                            }
                        }
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

        if(entity instanceof LivingEntity livingEntity && livingEntity.getEffect((MobEffect) SHOCKED_EFFECT.get())  != null){
            MobEffectInstance effectInstance = (livingEntity).getEffect((MobEffect) SHOCKED_EFFECT.get());
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

    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public EntityDivineSmite(PlayMessages.SpawnEntity packet, Level world) {
        super((EntityType) DIVINE_SMITE.get(), world);
    }
}
