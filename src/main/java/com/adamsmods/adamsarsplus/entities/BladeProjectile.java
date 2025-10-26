package com.adamsmods.adamsarsplus.entities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

import static net.minecraft.world.item.Items.IRON_SWORD;

public class BladeProjectile extends AbstractArrow implements ItemSupplier {

    private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK;
    public int age;

    public BladeProjectile(Level world, ItemStack item, Entity shooter){
        super(AdamsModEntities.BLADE_PROJ.get(), world);

        this.setOwner(shooter);
        this.setItem(item);
        this.setNoGravity(true);

        this.age = 0;
    }

    public BladeProjectile(EntityType<? extends AbstractArrow> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    public EntityType<?> getType() {
        return AdamsModEntities.BLADE_PROJ.get();
    }

    @Override
    public void tick() {
        super.tick();

        this.age++;
        if(age > 100){
            this.setNoGravity(false);
        }
    }

    protected void onHitEntity(EntityHitResult pResult) {
        Entity entity = pResult.getEntity();
        if(entity instanceof LivingEntity){
            LivingEntity livingTarget = (LivingEntity) entity;

            if(this.doHurtTarget(livingTarget, (LivingEntity) this.getOwner())){
                this.getItem().getItem().hurtEnemy(this.getItem(), livingTarget, (LivingEntity) this.getOwner());
            }
        }
    }

    public boolean doHurtTarget(Entity target, LivingEntity attacker) {
        float f = 2;
        for (AttributeModifier modifier : this.getItem().getAttributeModifiers(EquipmentSlot.MAINHAND).get(Attributes.ATTACK_DAMAGE))
        {
            f += (float) modifier.getAmount();
        }
        float f1 = 0;
        for (AttributeModifier modifier : this.getItem().getAttributeModifiers(EquipmentSlot.MAINHAND).get(Attributes.ATTACK_KNOCKBACK))
        {
            f1 += (float) modifier.getAmount();
        }

        if (target instanceof LivingEntity) {
            f += EnchantmentHelper.getDamageBonus(this.getItem(), ((LivingEntity)target).getMobType());
            f1 += (float)this.getItem().getEnchantmentLevel(Enchantments.KNOCKBACK);
        }

        int i = this.getItem().getEnchantmentLevel(Enchantments.FIRE_ASPECT);
        if (i > 0) {
            target.setSecondsOnFire(i * 4);
        }

        boolean flag = target.hurt(attacker.damageSources().mobAttack(attacker), f);
        if (flag) {
            if (f1 > 0.0F && target instanceof LivingEntity) {
                ((LivingEntity)target).knockback((double)(f1 * 0.5F), (double)Mth.sin(this.getYRot() * ((float)Math.PI / 180F)), (double)(-Mth.cos(this.getYRot() * ((float)Math.PI / 180F))));
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.6, (double)1.0F, 0.6));
            }

            if (target instanceof Player) {
                Player player = (Player)target;
                maybeDisableShield(player, this.getItem(), player.isUsingItem() ? player.getUseItem() : ItemStack.EMPTY, attacker);
            }

            this.doEnchantDamageEffects(attacker, target);
            attacker.setLastHurtMob(target);
        }

        return flag;
    }

    private void maybeDisableShield(Player pPlayer, ItemStack pMobItemStack, ItemStack pPlayerItemStack, LivingEntity attacker) {
        if (!pMobItemStack.isEmpty() && !pPlayerItemStack.isEmpty() && pMobItemStack.getItem() instanceof AxeItem && pPlayerItemStack.is(Items.SHIELD)) {
            float f = 0.25F + (float)EnchantmentHelper.getBlockEfficiency(attacker) * 0.05F;
            if (this.random.nextFloat() < f) {
                pPlayer.getCooldowns().addCooldown(Items.SHIELD, 100);
                this.level().broadcastEntityEvent(pPlayer, (byte)30);
            }
        }

    }

    public void shootEntity(Entity entityThrower, float rotationPitchIn, float rotationYawIn, float pitchOffset, float velocity, float inaccuracy) {
        float f = -Mth.sin(rotationYawIn * ((float)Math.PI / 180F)) * Mth.cos(rotationPitchIn * ((float)Math.PI / 180F));
        float f1 = -Mth.sin((rotationPitchIn + pitchOffset) * ((float)Math.PI / 180F));
        float f2 = Mth.cos(rotationYawIn * ((float)Math.PI / 180F)) * Mth.cos(rotationPitchIn * ((float)Math.PI / 180F));
        this.shoot((double)f, (double)f1, (double)f2, velocity, inaccuracy);
    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);

        ItemStack $$1 = this.getItemRaw();
        if (!$$1.isEmpty()) {
            pCompound.put("Item", $$1.save(new CompoundTag()));
        }
    }

    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);

        ItemStack $$1 = ItemStack.of(pCompound.getCompound("Item"));
        this.setItem($$1);
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    public void setItem(ItemStack pStack) {
        if (!pStack.is(this.getDefaultItem()) || pStack.hasTag()) {
            this.getEntityData().set(DATA_ITEM_STACK, pStack.copyWithCount(1));
            this.setItemSlot(EquipmentSlot.MAINHAND, pStack.copyWithCount(1));
        }

    }

    protected Item getDefaultItem() {
        return IRON_SWORD;
    }

    private ItemStack getItemRaw() {
        return (ItemStack)this.getEntityData().get(DATA_ITEM_STACK);
    }

    public ItemStack getItem() {
        ItemStack $$0 = this.getItemRaw();
        return $$0.isEmpty() ? new ItemStack(IRON_SWORD) : $$0;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(DATA_ITEM_STACK, ItemStack.EMPTY);
    }

    static {
        DATA_ITEM_STACK = SynchedEntityData.defineId(BladeProjectile.class, EntityDataSerializers.ITEM_STACK);
    }
}