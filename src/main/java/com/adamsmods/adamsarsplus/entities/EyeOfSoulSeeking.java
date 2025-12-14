package com.adamsmods.adamsarsplus.entities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.EyeOfEnder;
import net.minecraft.world.level.Level;

import static com.adamsmods.adamsarsplus.entities.AdamsModEntities.EYE_OF_SOUL;

public class EyeOfSoulSeeking extends EyeOfEnder {

    public EyeOfSoulSeeking(EntityType<? extends EyeOfEnder> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public EyeOfSoulSeeking(Level pLevel, double pX, double pY, double pZ) {
        this(EYE_OF_SOUL.get(), pLevel);
        this.setPos(pX, pY, pZ);
    }

}
