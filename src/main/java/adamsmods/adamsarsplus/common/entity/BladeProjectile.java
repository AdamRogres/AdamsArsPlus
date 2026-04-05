package adamsmods.adamsarsplus.common.entity;

import adamsmods.adamsarsplus.registry.ModEntities;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.world.item.Items.IRON_SWORD;

public class BladeProjectile extends AbstractArrow implements ItemSupplier {

    private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK;
    public int age;
    public double amp;

    public BladeProjectile(Level world, ItemStack item, Entity shooter, double Amp){
        super(ModEntities.BLADE_PROJ.get(), world);

        this.setOwner(shooter);
        this.setItem(item);
        this.setNoGravity(true);

        this.age = 0;
        this.amp = Amp;
    }

    public BladeProjectile(EntityType<? extends AbstractArrow> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    public EntityType<?> getType() {
        return ModEntities.BLADE_PROJ.get();
    }

    @Override
    public void tick() {
        super.tick();

        this.age++;
        if(this.inGround){
            this.age = this.age + 2;
        }
        if(age > 40){
            this.setNoGravity(false);
        }
        if(age > 80){
            this.remove(RemovalReason.DISCARDED);
        }
    }

    protected void onHitEntity(EntityHitResult pResult) {
        Entity entity = pResult.getEntity();
        if(entity instanceof LivingEntity){
            LivingEntity livingTarget = (LivingEntity) entity;



            if(this.doHurtTarget((ServerLevel) pResult.getEntity().level, livingTarget, (LivingEntity) this.getOwner())){
                this.getItem().getItem().hurtEnemy(this.getItem(), livingTarget, (LivingEntity) this.getOwner());
            }
        }
    }

    /*
    public boolean doHurtTarget(Entity target, LivingEntity attacker) {
        float f = 2;

        f += (float) this.amp;

        for (AttributeModifier modifier : this.getItem().getAttributeModifiers(EquipmentSlot.MAINHAND).get(Attributes.ATTACK_DAMAGE))
        {
            f += (float) modifier.amount();
        }
        float f1 = 0;
        for (AttributeModifier modifier : this.getItem().getAttributeModifiers(EquipmentSlot.MAINHAND).get(Attributes.ATTACK_KNOCKBACK))
        {
            f1 += (float) modifier.amount();
        }

        if (target instanceof LivingEntity) {
            f += EnchantmentHelper.getDamageBonus(this.getItem(), ((LivingEntity)target).getMobType());
            f1 += (float)this.getItem().getEnchantmentLevel(Enchantments.KNOCKBACK);
        }

        int i = this.getItem().getEnchantmentLevel(Enchantments.FIRE_ASPECT);
        if (i > 0) {
            target.setSecondsOnFire(i * 4);
        }

        boolean flag;
        if(this.getOwner() == null){
            flag = target.hurt(this.damageSources().arrow(this, this), f);
        } else {
            flag = target.hurt(attacker.damageSources().mobAttack(attacker), f);
            attacker.setLastHurtMob(target);
        }

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
        }

        return flag;
    }
    */

    public boolean doHurtTarget(ServerLevel level, Entity target, LivingEntity attacker) {
        float f = 2;
        f += (float) this.amp;

        ItemAttributeModifiers modifiers = this.getItem().get(DataComponents.ATTRIBUTE_MODIFIERS);
        float f1 = 0;

        if (modifiers != null) {
            for (ItemAttributeModifiers.Entry entry : modifiers.modifiers()) {
                if (entry.slot().test(EquipmentSlot.MAINHAND)) {
                    if (entry.attribute().value().equals(Attributes.ATTACK_DAMAGE)) {
                        f += (float) entry.modifier().amount();
                    }
                    if (entry.attribute().value().equals(Attributes.ATTACK_KNOCKBACK)) {
                        f1 += (float) entry.modifier().amount();
                    }
                }
            }
        }

        ItemEnchantments enchantments = this.getItem().get(DataComponents.ENCHANTMENTS);

        Registry<Enchantment> enchantmentRegistry = level.registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT);

        if (target instanceof LivingEntity) {
            f += enchantments.getLevel(enchantmentRegistry.getHolderOrThrow(Enchantments.SHARPNESS));
            f1 += enchantments.getLevel(enchantmentRegistry.getHolderOrThrow(Enchantments.KNOCKBACK));
            int i = enchantments.getLevel(enchantmentRegistry.getHolderOrThrow(Enchantments.FIRE_ASPECT));

            if (i > 0) {
                target.setRemainingFireTicks(i * 80);
            }
        }

        boolean flag;
        if (this.getOwner() == null) {
            flag = target.hurt(this.damageSources().arrow(this, this), f);
        } else {
            flag = target.hurt(attacker.damageSources().mobAttack(attacker), f);
            attacker.setLastHurtMob(target);
        }

        if (flag) {
            if (f1 > 0.0F && target instanceof LivingEntity livingTarget) {
                livingTarget.knockback(
                        (double)(f1 * 0.5F),
                        (double) Mth.sin(this.getYRot() * ((float) Math.PI / 180F)),
                        (double)(-Mth.cos(this.getYRot() * ((float) Math.PI / 180F)))
                );
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.6, 1.0, 0.6));
            }

            if (target instanceof Player player) {
                maybeDisableShield(player, this.getItem(), player.isUsingItem() ? player.getUseItem() : ItemStack.EMPTY, attacker);
            }

            DamageSource damageSource = this.getOwner() == null
                    ? this.damageSources().arrow(this, this)
                    : attacker.damageSources().mobAttack(attacker);
            EnchantmentHelper.doPostAttackEffectsWithItemSource(level, target, damageSource, this.getItem());
        }

        return flag;
    }

    private void maybeDisableShield(Player pPlayer, ItemStack pMobItemStack, ItemStack pPlayerItemStack, LivingEntity attacker) {
        if (!pMobItemStack.isEmpty() && !pPlayerItemStack.isEmpty() && pMobItemStack.getItem() instanceof AxeItem && pPlayerItemStack.is(Items.SHIELD)) {
            pPlayer.getCooldowns().addCooldown(Items.SHIELD, 100);
            this.level().broadcastEntityEvent(pPlayer, (byte)30);
        }
    }

    public void shootEntity(Entity entityThrower, float rotationPitchIn, float rotationYawIn, float pitchOffset, float velocity, float inaccuracy) {
        float f = -Mth.sin(rotationYawIn * ((float)Math.PI / 180F)) * Mth.cos(rotationPitchIn * ((float)Math.PI / 180F));
        float f1 = -Mth.sin((rotationPitchIn + pitchOffset) * ((float)Math.PI / 180F));
        float f2 = Mth.cos(rotationYawIn * ((float)Math.PI / 180F)) * Mth.cos(rotationPitchIn * ((float)Math.PI / 180F));
        this.shoot((double)f, (double)f1, (double)f2, velocity, inaccuracy);
    }

    public void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);

        ItemStack stack = this.getItemRaw();
        if (!stack.isEmpty()) {
            pCompound.put("Item", stack.save(this.level().registryAccess()));
        }
    }

    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);

        ItemStack stack = ItemStack.parseOptional(this.level().registryAccess(), pCompound.getCompound("Item"));
        this.setItem(stack);
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return null;
    }

    public void setItem(ItemStack pStack) {
        if (!pStack.is(this.getDefaultItem())) {
            this.getEntityData().set(DATA_ITEM_STACK, pStack.copyWithCount(1));
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

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ITEM_STACK, ItemStack.EMPTY);
    }

    static {
        DATA_ITEM_STACK = SynchedEntityData.defineId(BladeProjectile.class, EntityDataSerializers.ITEM_STACK);
    }
}