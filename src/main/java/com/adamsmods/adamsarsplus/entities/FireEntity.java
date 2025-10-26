package com.adamsmods.adamsarsplus.entities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;

public class FireEntity extends Monster {

    public int iTime = 0;
    public double accelerates = 0;
    public String animation = "1";
    public float size = 1;
    public int damage = 4;
    public int lifeTime = 0;
    public boolean sensitive = false;

    public static final EntityDataAccessor<Float> SIZE =
            SynchedEntityData.defineId(FireEntity.class, EntityDataSerializers.FLOAT);

    public FireEntity(Level world, float size, int damage, double accelerates, int lifeTime, boolean sensitive) {
        super(AdamsModEntities.FIRE_ENTITY.get(), world);

        this.size = size;
        this.accelerates = accelerates;
        this.damage = damage;
        this.lifeTime = lifeTime;
        this.sensitive = sensitive;

        this.setNoGravity(this.sensitive);
    }

    public FireEntity(EntityType<? extends Monster> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    public EntityType<?> getType() {
        return AdamsModEntities.FIRE_ENTITY.get();
    }

    @Override
    public void tick() {
        super.tick();

        iTime++;

        // Fire Animation Control
        switch(iTime % 7){
            case 1  -> animation = "2";
            case 2  -> animation = "3";
            case 3  -> animation = "4";
            case 4  -> animation = "5";
            case 5  -> animation = "6";
            case 6  -> animation = "7";
            default -> animation = "1";
        }

        if(!level().isClientSide){
            if(iTime > lifeTime * 20 + 60){
                this.remove(RemovalReason.DISCARDED);
            }
            if(iTime % (10 - 2 * accelerates) == 0){
                for (Entity entity : level().getEntities(null, new AABB(new Vec3(blockPosition().getX() + size / 2, blockPosition().getY() + size / 2, blockPosition().getZ() + size / 2), new Vec3(blockPosition().getX() - size / 2, blockPosition().getY() - size / 2, blockPosition().getZ() - size / 2)))) {
                    if(entity == this){
                        continue;
                    }
                    entity.hurt(entity.damageSources().inFire(), 4 + 2 * damage);
                    entity.setSecondsOnFire(20);
                }
            }
            this.setSize(this.size);
        }
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        return false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20D)
                .add(Attributes.FOLLOW_RANGE, 0D);
    }

    public void setSize(float size) { this.entityData.set(SIZE, size); }
    public float getSize(){ return this.entityData.get(SIZE); }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SIZE, 0f);
    }
    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("size", this.getSize());
    }
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.size = compound.getFloat("size");
        this.setSize(this.size);
    }

}
