package com.adamsmods.adamsarsplus.block.tile;

import com.adamsmods.adamsarsplus.entities.custom.DivineDogEntity;
import com.hollingsworth.arsnouveau.api.item.IWandable;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.TurretSpellCaster;
import com.hollingsworth.arsnouveau.client.particle.ParticleUtil;
import com.hollingsworth.arsnouveau.common.block.tile.RotatingTurretTile;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectRedstone;
import com.hollingsworth.arsnouveau.common.spell.method.MethodProjectile;
import com.hollingsworth.arsnouveau.common.spell.method.MethodTouch;
import com.hollingsworth.arsnouveau.common.util.PortUtil;
import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

import static com.adamsmods.adamsarsplus.block.ModBlocks.AUTO_TURRET_BLOCK_TILE;

public class AutoTurretTile extends RotatingTurretTile implements IWandable {
    public LivingEntity owner;
    public UUID ownerUUID;
    public LivingEntity target;
    public int mode;
    public boolean creative;

    private int ticksPerSignal = 40;
    public boolean isLocked;
    public boolean isOff;
    public int ticksElapsed;

    public float rotationX;
    public float rotationY;
    public float neededRotationX;
    public float neededRotationY;
    public float clientNeededX;
    public float clientNeededY;

    public AutoTurretTile(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state);
    }

    public AutoTurretTile(BlockPos pos, BlockState state) {
        super((BlockEntityType) AUTO_TURRET_BLOCK_TILE.get(), pos, state);
    }

    public void tick() {
        // Aim Control
        if (this.level.isClientSide) {
            if (this.clientNeededX != this.neededRotationX) {
                float diff = this.neededRotationX - this.clientNeededX;
                if ((double)Math.abs(diff) < 0.1) {
                    this.clientNeededX = this.neededRotationX;
                } else {
                    this.clientNeededX += diff * 0.1F;
                }
            }

            if (this.clientNeededY != this.neededRotationY) {
                float diff = this.neededRotationY - this.clientNeededY;
                if ((double)Math.abs(diff) < 0.1) {
                    this.clientNeededY = this.neededRotationY;
                } else {
                    this.clientNeededY += diff * 0.1F;
                }
            }

            if (this.rotationX != this.clientNeededX) {
                float diff = this.clientNeededX - this.rotationX;
                if ((double)Math.abs(diff) < 0.1) {
                    this.rotationX = this.clientNeededX;
                } else {
                    this.rotationX += diff * 0.1F;
                }
            }

            if (this.rotationY != this.clientNeededY) {
                float diff = this.clientNeededY - this.rotationY;
                if ((double)Math.abs(diff) < 0.1) {
                    this.rotationY = this.clientNeededY;
                } else {
                    this.rotationY += diff * 0.1F;
                }
            }

        } else {
            if (this.rotationX != this.neededRotationX) {
                float diff = this.neededRotationX - this.rotationX;
                if ((double)Math.abs(diff) < 0.1) {
                    this.setRotationX(this.neededRotationX);
                } else {
                    this.setRotationX(this.rotationX + diff * 0.1F);
                }

                this.setChanged();
            }

            if (this.rotationY != this.neededRotationY) {
                float diff = this.neededRotationY - this.rotationY;
                if ((double)Math.abs(diff) < 0.1) {
                    this.setRotationY(this.neededRotationY);
                } else {
                    this.setRotationY(this.rotationY + diff * 0.1F);
                }

                this.setChanged();
            }

        }
        // Targeting Control
        if(!this.level.isClientSide){
            if(this.target == null){
                for (Entity entity : level.getEntities(null, new AABB(this.worldPosition).inflate(30,30,30))) {
                    if(this.mode == 1){
                        // Target non-owner
                        if(entity instanceof LivingEntity && entity != this.owner) {
                            this.target = (LivingEntity) entity;
                            ParticleUtil.beam(this.target.blockPosition(), this.getBlockPos(), this.level);
                            break;
                        }
                    } else if(this.mode == 2) {
                        // Target non-players
                        if(entity instanceof LivingEntity && entity != this.owner && !(entity instanceof Player)) {
                            this.target = (LivingEntity) entity;
                            ParticleUtil.beam(this.target.blockPosition(), this.getBlockPos(), this.level);
                            break;
                        }
                    } else if(this.mode == 3){
                        // Target Monsters
                        if(entity instanceof Monster) {
                            this.target = (LivingEntity) entity;
                            ParticleUtil.beam(this.target.blockPosition(), this.getBlockPos(), this.level);
                            break;
                        }
                    } else {
                        // Target Players
                        if(entity instanceof Player) {
                            this.target = (LivingEntity) entity;
                            ParticleUtil.beam(this.target.blockPosition(), this.getBlockPos(), this.level);
                            break;
                        }
                    }
                }
            } else if(this.isTooFar(this.worldPosition, this.target.blockPosition(), 30)){
                this.target = null;
                this.ticksElapsed = 0;
            } else {
                if(!this.target.isAlive()){
                    this.target = null;
                } else {
                    this.aimAuto(target.blockPosition());

                    ++this.ticksElapsed;
                    if (this.ticksPerSignal > 0 && !this.isOff && this.ticksElapsed >= this.ticksPerSignal) {
                        this.getBlockState().tick((ServerLevel)this.level, this.getBlockPos(), this.getLevel().random);
                        this.ticksElapsed = 0;
                    }
                }
            }
        }

    }

    public void aim(@Nullable BlockPos blockPos, Player playerEntity) {
        if (blockPos != null) {
            Vec3 thisVec = Vec3.atCenterOf(this.getBlockPos());
            Vec3 blockVec = Vec3.atCenterOf(blockPos);
            Vec3 diffVec = blockVec.subtract(thisVec);
            Vec3 diffVec2D = new Vec3(diffVec.x, diffVec.z, (double)0.0F);

            Vec3 rotVec = new Vec3((double)0.0F, (double)1.0F, (double)0.0F);
            float angle = (float)(angleBetween(rotVec, diffVec2D) / Math.PI * (double)180.0F);
            if (blockVec.x < thisVec.x) {
                angle = -angle;
            }
            this.neededRotationX = angle + 90.0F;

            rotVec = new Vec3(diffVec.x, (double)0.0F, diffVec.z);
            angle = (float)(angleBetween(diffVec, rotVec) * (double)180.0F / (double)(float)Math.PI);
            if (blockVec.y < thisVec.y) {
                angle = -angle;
            }
            this.neededRotationY = angle;

            this.updateBlock();
            ParticleUtil.beam(blockPos, this.getBlockPos(), this.level);
            PortUtil.sendMessageNoSpam(playerEntity, Component.literal("Turret now aims to " + blockPos.toShortString()));
        }
    }

    public void aimAuto(@Nullable BlockPos blockPos) {
        if (blockPos != null) {
            Vec3 thisVec = Vec3.atCenterOf(this.getBlockPos());
            Vec3 blockVec = Vec3.atCenterOf(blockPos);
            Vec3 diffVec = blockVec.subtract(thisVec);
            Vec3 diffVec2D = new Vec3(diffVec.x, diffVec.z, (double)0.0F);

            Vec3 rotVec = new Vec3((double)0.0F, (double)1.0F, (double)0.0F);
            float angle = (float)(angleBetween(rotVec, diffVec2D) / Math.PI * (double)180.0F);
            if (blockVec.x < thisVec.x) {
                angle = -angle;
            }
            this.neededRotationX = angle + 90.0F;

            rotVec = new Vec3(diffVec.x, (double)0.0F, diffVec.z);
            angle = (float)(angleBetween(diffVec, rotVec) * (double)180.0F / (double)(float)Math.PI);
            if (blockVec.y < thisVec.y) {
                angle = -angle;
            }
            this.neededRotationY = angle;

            this.updateBlock();
        }
    }

    public boolean isTooFar(BlockPos pos1, BlockPos pos2, float distance){
        int diffX = pos1.getX() - pos2.getX();
        int diffY = pos1.getY() - pos2.getY();
        int diffZ = pos1.getZ() - pos2.getZ();

        return (distance < (Math.sqrt(diffX * diffX + diffY * diffY + diffZ * diffZ)));
    }

    public static double angleBetween(Vec3 a, Vec3 b) {
        double projection = a.normalize().dot(b.normalize());
        return Math.acos(Mth.clamp(projection, (double)-1.0F, (double)1.0F));
    }

    public void onFinishedConnectionFirst(@Nullable BlockPos storedPos, @Nullable LivingEntity storedEntity, Player playerEntity) {
        if (storedPos != null) {
            this.aim(storedPos, playerEntity);
        }
    }

    public int getManaCost() {
        int cost = super.getManaCost();
        if(this.creative){
            cost = 0;
        }
        return Math.max(0, cost);
    }

    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        tag.putFloat("rotationYa", this.rotationY);
        tag.putFloat("rotationXa", this.rotationX);
        tag.putFloat("neededRotationYa", this.neededRotationY);
        tag.putFloat("neededRotationXa", this.neededRotationX);
        if(this.owner != null){
            tag.putUUID("ownerUUID", this.owner.getUUID());
        }
        tag.putInt("mode", this.mode);
        tag.putBoolean("creative", this.creative);

        tag.putBoolean("locked", this.isLocked);
        tag.putInt("time", this.ticksPerSignal);
        tag.putBoolean("off", this.isOff);
        tag.putInt("ticksElapsed", this.ticksElapsed);
    }

    public void load(CompoundTag tag) {
        super.load(tag);

        this.rotationX = tag.getFloat("rotationXa");
        this.rotationY = tag.getFloat("rotationYa");
        this.neededRotationX = tag.getFloat("neededRotationXa");
        this.neededRotationY = tag.getFloat("neededRotationYa");
        this.setOwnerUUID(tag.getUUID("ownerUUID"));
        if(this.ownerUUID != null){
            this.owner = this.getOwnerFromID();
        }
        this.setMode(tag.getInt("mode"));
        this.setCreative(tag.getBoolean("creative"));

        this.isLocked = tag.getBoolean("locked");
        this.ticksPerSignal = tag.getInt("time");
        this.isOff = tag.getBoolean("off");
        this.ticksElapsed = tag.getInt("ticksElapsed");
    }

    public LivingEntity getOwnerFromID() {
        try {
            UUID uuid = this.getOwnerUUID();
            return uuid == null ? null : this.level.getPlayerByUUID(uuid);
        } catch (IllegalArgumentException var21) {
            return null;
        }
    }
    public UUID getOwnerUUID() {
        return this.ownerUUID;
    }

    public float getRotationX() {
        return this.rotationX;
    }
    public float getRotationY() {
        return this.rotationY;
    }
    public LivingEntity getOwner(){ return this.owner; }
    public int getMode(){ return this.mode; }
    public boolean getCreative(){ return this.creative; }

    public void setOwnerUUID(UUID ownerUUID){ this.ownerUUID = ownerUUID; }
    public void setRotationX(float rot) {
        this.rotationX = rot;
    }
    public void setRotationY(float rot) {
        this.rotationY = rot;
    }
    public void setOwner(LivingEntity owner){ this.owner = owner; }
    public void setMode(int mode){ this.mode = mode; }
    public void setCreative(boolean creative){ this.creative = creative; }

    public Vec3 getShootAngle() {
        float f = this.getRotationY() * ((float)Math.PI / 180F);
        float f1 = (90.0F + this.getRotationX()) * ((float)Math.PI / 180F);
        float f2 = Mth.cos(f1);
        float f3 = Mth.sin(f1);
        float f4 = Mth.cos(f);
        float f5 = Mth.sin(f);
        return (new Vec3((double)(f3 * f4), (double)(-f5), (double)(f2 * f4))).reverse();
    }

}
