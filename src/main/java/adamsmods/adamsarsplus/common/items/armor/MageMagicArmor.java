package adamsmods.adamsarsplus.common.items.armor;

import adamsmods.adamsarsplus.AdamsArsPlus;
import adamsmods.adamsarsplus.client.armor.AdamGenericModel;
import adamsmods.adamsarsplus.client.armor.AdamArmorRenderer;
import adamsmods.adamsarsplus.common.entity.custom.TerraprismaEntity;
import adamsmods.adamsarsplus.util.TooltipUtils;
import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.api.perk.*;
import com.hollingsworth.arsnouveau.api.util.PerkUtil;
import com.hollingsworth.arsnouveau.common.armor.AnimatedMagicArmor;
import com.hollingsworth.arsnouveau.common.items.data.ArmorPerkHolder;
import com.hollingsworth.arsnouveau.common.perk.RepairingPerk;
import com.hollingsworth.arsnouveau.setup.registry.DataComponentRegistry;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static adamsmods.adamsarsplus.ConfigHandler.Common.*;
import static adamsmods.adamsarsplus.common.items.armor.Materials.*;
import static adamsmods.adamsarsplus.common.items.armor.ArmorSet.*;
import static adamsmods.adamsarsplus.registry.ModItems.*;
import static adamsmods.adamsarsplus.registry.ModItems.ADAM_HOOD_A;
import static adamsmods.adamsarsplus.registry.ModPotions.*;
import static net.minecraft.world.effect.MobEffects.MOVEMENT_SPEED;

public class MageMagicArmor extends ArmorItem implements GeoItem {
    public GeoModel<MageMagicArmor> model;

    public MageMagicArmor(Holder<ArmorMaterial> materialIn, Type slot, Properties builder, GeoModel<MageMagicArmor> model) {
        super(materialIn, slot, builder);
        this.model = model;
    }

    public MageMagicArmor(Holder<ArmorMaterial> materialIn, ArmorItem.Type slot, GeoModel<MageMagicArmor> model) {
        this(materialIn, slot, ItemsRegistry.defaultItemProperties().stacksTo(1).component(DataComponentRegistry.ARMOR_PERKS, new ArmorPerkHolder()).component(DataComponents.BASE_COLOR, DyeColor.PURPLE), model);
    }

    public static MageMagicArmor cade(Type slot, Boolean awakened) {
        return new MageMagicArmor(CADE, slot,
                MageArmorProp()
                .stacksTo(1)
                .component(DataComponentRegistry.ARMOR_PERKS, new MageArmorPerkHolder(CADE, awakened))
                .component(DataComponents.BASE_COLOR, DyeColor.PURPLE)
                .durability(slot.getDurability(50))
                , (new AdamGenericModel<MageMagicArmor>("cade_armor", "item/armor")).withEmptyAnim());
    }
    public static MageMagicArmor ryan(Type slot, Boolean awakened) {
        return new MageMagicArmor(RYAN, slot,
                MageArmorProp()
                        .stacksTo(1)
                        .component(DataComponentRegistry.ARMOR_PERKS, new MageArmorPerkHolder(RYAN, awakened))
                        .component(DataComponents.BASE_COLOR, DyeColor.RED)
                        .durability(slot.getDurability(50))
                , (new AdamGenericModel<MageMagicArmor>("ryan_armor", "item/armor")).withEmptyAnim());
    }
    public static MageMagicArmor nick(Type slot, Boolean awakened) {
        return new MageMagicArmor(NICK, slot,
                MageArmorProp()
                        .stacksTo(1)
                        .component(DataComponentRegistry.ARMOR_PERKS, new MageArmorPerkHolder(NICK, awakened))
                        .component(DataComponents.BASE_COLOR, DyeColor.GREEN)
                        .durability(slot.getDurability(50))
                , (new AdamGenericModel<MageMagicArmor>("nick_armor", "item/armor")).withEmptyAnim());
    }
    public static MageMagicArmor camr(Type slot, Boolean awakened) {
        return new MageMagicArmor(CAMR, slot,
                MageArmorProp()
                        .stacksTo(1)
                        .component(DataComponentRegistry.ARMOR_PERKS, new MageArmorPerkHolder(CAMR, awakened))
                        .component(DataComponents.BASE_COLOR, DyeColor.WHITE)
                        .durability(slot.getDurability(100))
                , (new AdamGenericModel<MageMagicArmor>("camr_armor", "item/armor")).withEmptyAnim());
    }
    public static MageMagicArmor matt(Type slot, Boolean awakened) {
        return new MageMagicArmor(MATT, slot,
                MageArmorProp()
                        .stacksTo(1)
                        .component(DataComponentRegistry.ARMOR_PERKS, new MageArmorPerkHolder(MATT, awakened))
                        .component(DataComponents.BASE_COLOR, DyeColor.YELLOW)
                        .durability(slot.getDurability(100))
                , (new AdamGenericModel<MageMagicArmor>("matt_armor", "item/armor")).withEmptyAnim());
    }
    public static MageMagicArmor adam(Type slot, Boolean awakened) {
        return new MageMagicArmor(ADAM, slot,
                MageArmorProp()
                        .stacksTo(1)
                        .component(DataComponentRegistry.ARMOR_PERKS, new MageArmorPerkHolder(ADAM, awakened))
                        .component(DataComponents.BASE_COLOR, DyeColor.GRAY)
                        .durability(slot.getDurability(150))
                , (new AdamGenericModel<MageMagicArmor>("adam_armor", "item/armor")).withEmptyAnim());
    }

    @Override
    public boolean isEnchantable(ItemStack pStack) {
        return true;
    }

    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
    }

    static Item.Properties itemProps() {
        return new Item.Properties();
    }

    public static Item.Properties MageArmorProp() {
        //return itemProps().stacksTo(1).rarity(Rarity.EPIC).component(DataComponentRegistry.ARMOR_PERKS, new ArmorPerkHolder());
        return itemProps().stacksTo(1).rarity(Rarity.EPIC);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level world, @NotNull Entity player, int slotId, boolean pIsSelected) {
        super.inventoryTick(stack, world, player, slotId, pIsSelected);
        if (slotId >= Inventory.INVENTORY_SIZE && slotId < Inventory.INVENTORY_SIZE + 4) {
            if (world.isClientSide())
                return;
            if (player instanceof LivingEntity livingEntity) {
                RepairingPerk.attemptRepair(stack, livingEntity);
                var perkHolder = PerkUtil.getPerkHolder(stack);
                if (perkHolder == null)
                    return;
                for (PerkInstance instance : perkHolder.getPerkInstances(stack)) {
                    if (instance.getPerk() instanceof ITickablePerk tickablePerk) {
                        tickablePerk.tick(stack, world, livingEntity, instance);
                    }
                }
            }
            if(player instanceof Player player1){
                if(hasFullSuitOfArmorOn(player1)){
                    switch(getActiveSetBonus(player1)){
                        case "cade_armor" -> {
                            if(!player1.hasEffect(WALKING_BLIZZARD_EFFECT)){
                                player1.addEffect(new MobEffectInstance(WALKING_BLIZZARD_EFFECT, 100, 1, false, false));
                            }
                        }
                        case "ryan_armor" -> {
                            if(!player1.hasEffect(FLAME_DEITY_EFFECT)){
                                player1.addEffect(new MobEffectInstance(FLAME_DEITY_EFFECT, 100, 0, false, false));
                            }
                        }
                        case "nick_armor" -> {
                            if(player1.hasEffect(EARTHEN_HEART_EFFECT)){
                                if(player1.getEffect(EARTHEN_HEART_EFFECT).getDuration() < 50){
                                    player1.addEffect(new MobEffectInstance(EARTHEN_HEART_EFFECT, 200, 0, false, false));
                                }
                            } else {
                                player1.addEffect(new MobEffectInstance(EARTHEN_HEART_EFFECT, 200, 0, false, false));
                            }
                        }
                        case "cam_armor" -> {
                            if(DO_LEAP_FATIGUE.get()){
                                if((player1.level().getGameTime() % 160L == 0L)){
                                    if(!player1.hasEffect(LIGHTNING_STEPS_EFFECT)){
                                        player1.addEffect(new MobEffectInstance(LIGHTNING_STEPS_EFFECT, 180, 0, false, false));
                                    } else if(player1.getEffect(LIGHTNING_STEPS_EFFECT).getDuration() < 179) {
                                        int amp = Math.min(2, player1.getEffect(LIGHTNING_STEPS_EFFECT).getAmplifier() + 1) ;
                                        player1.removeEffect(LIGHTNING_STEPS_EFFECT);
                                        player1.addEffect(new MobEffectInstance(LIGHTNING_STEPS_EFFECT, 180, amp, false, false));
                                    }
                                }
                            } else {
                                if(!player1.hasEffect(MOVEMENT_SPEED)){
                                    player1.addEffect(new MobEffectInstance(MOVEMENT_SPEED, 40, 2, false, false));
                                }
                            }
                        }
                        case "matt_armor" -> {
                            if(!player1.hasEffect(HOLY_LEGION_EFFECT)){
                                player1.addEffect(new MobEffectInstance(HOLY_LEGION_EFFECT, 100, 0, false, false));
                                TerraprismaEntity summon = new TerraprismaEntity(world, player1, true);
                                summon.setPos(player1.getEyePosition());
                                summon.setOwnerID(player1.getUUID());
                                world.addFreshEntity(summon);
                            } else {
                                if(player1.getEffect(HOLY_LEGION_EFFECT).getDuration() < 20){
                                    player1.addEffect(new MobEffectInstance(HOLY_LEGION_EFFECT, 100, 0, false, false));
                                }
                            }
                        }
                        case "adam_armor" -> {
                            if(!player1.hasEffect(ABYSSAL_DOMINATION_EFFECT)){
                                player1.addEffect(new MobEffectInstance(ABYSSAL_DOMINATION_EFFECT, 200, 0, false, false));
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public @NotNull ItemAttributeModifiers getDefaultAttributeModifiers(@NotNull ItemStack stack) {
        var modifiers = super.getDefaultAttributeModifiers(stack);
        var perkHolder = PerkUtil.getPerkHolder(stack);
        if (perkHolder != null) {

            for (PerkInstance instance : perkHolder.getPerkInstances(stack)) {
                modifiers = instance.getPerk().applyAttributeModifiers(modifiers, stack, instance.getSlot().value(), EquipmentSlotGroup.bySlot(this.type.getSlot()));
            }

            if (this.material == CADE) {
                modifiers = modifiers.withModifierAdded(PerkAttributes.MAX_MANA, new AttributeModifier(ArsNouveau.prefix("max_mana_armor_" + this.type.getName()), (CADE_MAX_MANA.get() * (perkHolder.getTier() + 1)), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(this.type.getSlot()));
                modifiers = modifiers.withModifierAdded(PerkAttributes.MANA_REGEN_BONUS, new AttributeModifier(ArsNouveau.prefix("mana_regen_armor_" + this.type.getName()), (CADE_MANA_REGEN.get() * (perkHolder.getTier() + 1)), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(this.type.getSlot()));
                modifiers = modifiers.withModifierAdded(PerkAttributes.SPELL_DAMAGE_BONUS, new AttributeModifier(ArsNouveau.prefix("spell_damage_armor_" + this.type.getName()), (CADE_SPELL_DAMAGE.get() * (perkHolder.getTier() + 1)), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(this.type.getSlot()));
                modifiers = modifiers.withModifierAdded(PerkAttributes.WARDING, new AttributeModifier(ArsNouveau.prefix("warding_armor_" + this.type.getName()), (CADE_WARDING.get()), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(this.type.getSlot()));
            } else if (this.material == RYAN) {
                modifiers = modifiers.withModifierAdded(PerkAttributes.MAX_MANA, new AttributeModifier(ArsNouveau.prefix("max_mana_armor_" + this.type.getName()), (RYAN_MAX_MANA.get() * (perkHolder.getTier() + 1)), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(this.type.getSlot()));
                modifiers = modifiers.withModifierAdded(PerkAttributes.MANA_REGEN_BONUS, new AttributeModifier(ArsNouveau.prefix("mana_regen_armor_" + this.type.getName()), (RYAN_MANA_REGEN.get() * (perkHolder.getTier() + 1)), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(this.type.getSlot()));
                modifiers = modifiers.withModifierAdded(PerkAttributes.SPELL_DAMAGE_BONUS, new AttributeModifier(ArsNouveau.prefix("spell_damage_armor_" + this.type.getName()), (RYAN_SPELL_DAMAGE.get() * (perkHolder.getTier())), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(this.type.getSlot()));
                modifiers = modifiers.withModifierAdded(PerkAttributes.WARDING, new AttributeModifier(ArsNouveau.prefix("warding_armor_" + this.type.getName()), (RYAN_WARDING.get()), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(this.type.getSlot()));
            } else if (this.material == NICK) {
                modifiers = modifiers.withModifierAdded(PerkAttributes.MAX_MANA, new AttributeModifier(ArsNouveau.prefix("max_mana_armor_" + this.type.getName()), (NICK_MAX_MANA.get() * (perkHolder.getTier() + 1)), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(this.type.getSlot()));
                modifiers = modifiers.withModifierAdded(PerkAttributes.MANA_REGEN_BONUS, new AttributeModifier(ArsNouveau.prefix("mana_regen_armor_" + this.type.getName()), (NICK_MANA_REGEN.get() * (perkHolder.getTier() + 1)), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(this.type.getSlot()));
                modifiers = modifiers.withModifierAdded(PerkAttributes.SPELL_DAMAGE_BONUS, new AttributeModifier(ArsNouveau.prefix("spell_damage_armor_" + this.type.getName()), (NICK_SPELL_DAMAGE.get() * (perkHolder.getTier() - 1)), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(this.type.getSlot()));
                modifiers = modifiers.withModifierAdded(PerkAttributes.WARDING, new AttributeModifier(ArsNouveau.prefix("warding_armor_" + this.type.getName()), (NICK_WARDING.get()), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(this.type.getSlot()));
            } else if (this.material == CAMR) {
                modifiers = modifiers.withModifierAdded(PerkAttributes.MAX_MANA, new AttributeModifier(ArsNouveau.prefix("max_mana_armor_" + this.type.getName()), (CAM_MAX_MANA.get() * (perkHolder.getTier() + 1)), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(this.type.getSlot()));
                modifiers = modifiers.withModifierAdded(PerkAttributes.MANA_REGEN_BONUS, new AttributeModifier(ArsNouveau.prefix("mana_regen_armor_" + this.type.getName()), (CAM_MANA_REGEN.get() * (perkHolder.getTier() + 1)), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(this.type.getSlot()));
                modifiers = modifiers.withModifierAdded(PerkAttributes.SPELL_DAMAGE_BONUS, new AttributeModifier(ArsNouveau.prefix("spell_damage_armor_" + this.type.getName()), (CAM_SPELL_DAMAGE.get() * (perkHolder.getTier() + 2)), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(this.type.getSlot()));
                modifiers = modifiers.withModifierAdded(PerkAttributes.WARDING, new AttributeModifier(ArsNouveau.prefix("warding_armor_" + this.type.getName()), (CAM_WARDING.get()), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(this.type.getSlot()));
            } else if (this.material == MATT) {
                modifiers = modifiers.withModifierAdded(PerkAttributes.MAX_MANA, new AttributeModifier(ArsNouveau.prefix("max_mana_armor_" + this.type.getName()), (MATT_MAX_MANA.get() * (perkHolder.getTier() + 1)), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(this.type.getSlot()));
                modifiers = modifiers.withModifierAdded(PerkAttributes.MANA_REGEN_BONUS, new AttributeModifier(ArsNouveau.prefix("mana_regen_armor_" + this.type.getName()), (MATT_MANA_REGEN.get() * (perkHolder.getTier() + 1)), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(this.type.getSlot()));
                modifiers = modifiers.withModifierAdded(PerkAttributes.SPELL_DAMAGE_BONUS, new AttributeModifier(ArsNouveau.prefix("spell_damage_armor_" + this.type.getName()), (MATT_SPELL_DAMAGE.get() * (perkHolder.getTier())), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(this.type.getSlot()));
                modifiers = modifiers.withModifierAdded(PerkAttributes.WARDING, new AttributeModifier(ArsNouveau.prefix("warding_armor_" + this.type.getName()), (MATT_WARDING.get()), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(this.type.getSlot()));
            } else if (this.material == ADAM) {
                modifiers = modifiers.withModifierAdded(PerkAttributes.MAX_MANA, new AttributeModifier(ArsNouveau.prefix("max_mana_armor_" + this.type.getName()), (ADAM_MAX_MANA.get() * (perkHolder.getTier() + 1)), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(this.type.getSlot()));
                modifiers = modifiers.withModifierAdded(PerkAttributes.MANA_REGEN_BONUS, new AttributeModifier(ArsNouveau.prefix("mana_regen_armor_" + this.type.getName()), (ADAM_MANA_REGEN.get() * (perkHolder.getTier() + 1)), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(this.type.getSlot()));
                modifiers = modifiers.withModifierAdded(PerkAttributes.SPELL_DAMAGE_BONUS, new AttributeModifier(ArsNouveau.prefix("spell_damage_armor_" + this.type.getName()), (ADAM_SPELL_DAMAGE.get() * (perkHolder.getTier() + 2)), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(this.type.getSlot()));
                modifiers = modifiers.withModifierAdded(PerkAttributes.WARDING, new AttributeModifier(ArsNouveau.prefix("warding_armor_" + this.type.getName()), (ADAM_WARDING.get()), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(this.type.getSlot()));
            }
        }

        return modifiers;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext world, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        var data = stack.get(DataComponentRegistry.ARMOR_PERKS);
        if (data != null) {
            tooltip.add(Component.translatable("ars_nouveau.tier", data.getTier() + 1).withStyle(ChatFormatting.GOLD));
            data.appendPerkTooltip(tooltip, stack);
        }
        TooltipUtils.addOnShift(tooltip, () -> addInformationAfterShift(stack, world.level(), tooltip, flag), "armor_set");
    }

    EquipmentSlot[] OrderedSlots = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

    @OnlyIn(Dist.CLIENT)
    public void addInformationAfterShift(ItemStack stack, Level world, List<Component> list, TooltipFlag flags) {
        if(stack.getItem() instanceof MageMagicArmor itemArmor){
            Player player = ArsNouveau.proxy.getPlayer();
            if (player != null) {
                ArmorSet set = getArmorSetFromMaterial(itemArmor.material.value());
                List<Component> equippedList = new ArrayList<>();
                //check if the player have all the armor pieces of the set. Color the text green if they do, gray if they don't
                int equippedCounter = 0;
                for (EquipmentSlot slot : OrderedSlots) {
                    Item[] armor = set.getArmorFromSlot(slot);
                    MutableComponent cmp = Component.literal(" - ").append(armor[0].getDefaultInstance().getHoverName());
                    if (hasArmorSetItem(player.getItemBySlot(slot), armor)) {
                        cmp.withStyle(ChatFormatting.GREEN);
                        equippedCounter++;
                    } else cmp.withStyle(ChatFormatting.GRAY);

                    equippedList.add(cmp);
                }
                //add the tooltip for the armor set and the list of equipped armor pieces, then add the description
                list.add(getArmorSetTitle(set, equippedCounter));
                list.addAll(equippedList);
                addArmorSetDescription(set, list);
            }
        }
    }

    ArmorSet getArmorSetFromMaterial(ArmorMaterial material) {
        if(material == CADE.value()){
            return CADE_ARMORSET;
        } else if (material == RYAN.value()) {
            return RYAN_ARMORSET;
        } else if (material == NICK.value()) {
            return NICK_ARMORSET;
        } else if (material == CAMR.value()) {
            return CAM_ARMORSET;
        } else if (material == MATT.value()) {
            return MATT_ARMORSET;
        } else if (material == ADAM.value()) {
            return ADAM_ARMORSET;
        } else {
            return null;
        }
    }

    private boolean hasArmorSetItem(ItemStack armor, Item[] armorFromSlot) {
        return (armor.getItem() == armorFromSlot[0] || armor.getItem() == armorFromSlot[1]);
    }

    private Component getArmorSetTitle(ArmorSet set, int equipped) {
        return Component.translatable(set.getTranslationKey())
                .append(" (" + equipped + " / 4)")
                .withStyle(ChatFormatting.DARK_AQUA);
    }

    public void addArmorSetDescription(ArmorSet set, List<Component> list) {
        list.add(Component.translatable("adamsarsplus.armor_set." + set.getName() + ".desc").withStyle(ChatFormatting.GRAY));
    }

    /*
    public void onDye(ItemStack stack, DyeColor dyeColor) {
        IPerkHolder<ItemStack> perkHolder = PerkUtil.getPerkHolder(stack);
        if (perkHolder instanceof ArmorPerkHolder armorPerkHolder) {
            armorPerkHolder.setColor(dyeColor.getName());
        }
    }
     */

    @Override
    public boolean makesPiglinsNeutral(@NotNull ItemStack stack, @NotNull LivingEntity wearer) {
        return true;
    }

    AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return factory;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        GeoItem.super.createGeoRenderer(consumer);
        consumer.accept(new GeoRenderProvider() {
            private GeoArmorRenderer<?> renderer;

            @Override
            public @Nullable <T extends LivingEntity> HumanoidModel<?> getGeoArmorRenderer(@Nullable T livingEntity, ItemStack itemStack, @Nullable EquipmentSlot equipmentSlot, @Nullable HumanoidModel<T> original) {
                if (renderer == null) {
                    renderer = new AdamArmorRenderer(getArmorModel());
                }
                return this.renderer;
            }
        });
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return !stack.has(DataComponentRegistry.UNBREAKING);
    }

    public GeoModel<MageMagicArmor> getArmorModel() {
        return this.model;
    }

    @Override
    public @Nullable ResourceLocation getArmorTexture(@NotNull ItemStack stack, @NotNull Entity entity, @NotNull EquipmentSlot slot, ArmorMaterial.@NotNull Layer layer, boolean innerModel) {
        AdamGenericModel<MageMagicArmor> genericModel = (AdamGenericModel<MageMagicArmor>) model;
        return AdamsArsPlus.prefix("textures/" + genericModel.textPathRoot + "/" + genericModel.name + "_" + this.getColor(stack) + ".png");
    }

    public String getColor(ItemStack object) {
        return object.getOrDefault(DataComponents.BASE_COLOR, DyeColor.PURPLE).getName();
    }

    private boolean hasFullSuitOfArmorOn(Player player){
        ItemStack boots = player.getInventory().getArmor(0);
        ItemStack leggings = player.getInventory().getArmor(1);
        ItemStack chestplate = player.getInventory().getArmor(2);
        ItemStack helmet = player.getInventory().getArmor(3);

        return !helmet.isEmpty() && !chestplate.isEmpty() && !leggings.isEmpty() && !boots.isEmpty();
    }

    private String getActiveSetBonus(Player player){
        if(player.getInventory().getArmor(0).getItem() instanceof MageMagicArmor mageBoots && player.getInventory().getArmor(1).getItem() instanceof MageMagicArmor mageLeggings && player.getInventory().getArmor(2).getItem() instanceof MageMagicArmor mageChest && player.getInventory().getArmor(3).getItem() instanceof MageMagicArmor mageHelmet){
            if(mageBoots.material == CADE && mageLeggings.material == CADE && mageChest.material == CADE && mageHelmet.material == CADE){
                return "cade_armor";
            } else if(mageBoots.material == RYAN && mageLeggings.material == RYAN && mageChest.material == RYAN && mageHelmet.material == RYAN){
                return "ryan_armor";
            } else if(mageBoots.material == NICK && mageLeggings.material == NICK && mageChest.material == NICK && mageHelmet.material == NICK){
                return "nick_armor";
            } else if(mageBoots.material == CAMR && mageLeggings.material == CAMR && mageChest.material == CAMR && mageHelmet.material == CAMR){
                return "cam_armor";
            } else if(mageBoots.material == MATT && mageLeggings.material == MATT && mageChest.material == MATT && mageHelmet.material == MATT){
                return "matt_armor";
            } else if(mageBoots.material == ADAM && mageLeggings.material == ADAM && mageChest.material == ADAM && mageHelmet.material == ADAM){
                return "adam_armor";
            }
        }
        return "null";
    }

}
