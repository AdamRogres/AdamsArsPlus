package com.adamsmods.adamsarsplus.item.armor;

import com.adamsmods.adamsarsplus.entities.custom.TerraprismaEntity;
import com.adamsmods.adamsarsplus.item.client.renderer.AdamArmorRenderer;
import com.adamsmods.adamsarsplus.item.client.renderer.AdamGenericModel;
import com.adamsmods.adamsarsplus.util.TooltipUtils;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.api.client.IVariantColorProvider;
import com.hollingsworth.arsnouveau.api.mana.IManaEquipment;
import com.hollingsworth.arsnouveau.api.perk.*;
import com.hollingsworth.arsnouveau.api.registry.PerkRegistry;
import com.hollingsworth.arsnouveau.api.util.PerkUtil;
import com.hollingsworth.arsnouveau.common.crafting.recipes.IDyeable;
import com.hollingsworth.arsnouveau.common.perk.RepairingPerk;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static com.adamsmods.adamsarsplus.ArsNouveauRegistry.*;
import static com.adamsmods.adamsarsplus.Config.Common.*;
import static com.adamsmods.adamsarsplus.item.armor.ArmorSet.*;
import static com.adamsmods.adamsarsplus.item.armor.Materials.*;
import static net.minecraft.world.effect.MobEffects.MOVEMENT_SPEED;

public class MageMagicArmor extends ArmorItem implements IManaEquipment, IDyeable, GeoItem, IVariantColorProvider<ItemStack>  {
    public GeoModel<MageMagicArmor> model;
    AnimatableInstanceCache factory;

    private static final EnumMap<Type, UUID> ARMOR_MODIFIER_UUID_PER_TYPE = (EnumMap) Util.make(new EnumMap(Type.class), (p_266744_) -> {
        p_266744_.put(ArmorItem.Type.BOOTS, UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"));
        p_266744_.put(ArmorItem.Type.LEGGINGS, UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"));
        p_266744_.put(ArmorItem.Type.CHESTPLATE, UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"));
        p_266744_.put(ArmorItem.Type.HELMET, UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150"));
    });

    public MageMagicArmor(ArmorMaterial materialIn, ArmorItem.Type slot, Item.Properties builder, GeoModel<MageMagicArmor> model) {
        super(materialIn, slot, builder);
        this.factory = GeckoLibUtil.createInstanceCache(this);
        this.model = model;
    }

    public MageMagicArmor(ArmorMaterial materialIn, ArmorItem.Type slot, GeoModel<MageMagicArmor> model) {
        this(materialIn, slot, ItemsRegistry.defaultItemProperties().stacksTo(1), model);
    }

    public static MageMagicArmor cade(ArmorItem.Type slot) {
        return new MageMagicArmor(CADE, slot, (new AdamGenericModel("cade_armor", "item/armor")).withEmptyAnim());
    }
    public static MageMagicArmor ryan(ArmorItem.Type slot) {
        return new MageMagicArmor(RYAN, slot, (new AdamGenericModel("ryan_armor", "item/armor")).withEmptyAnim());
    }
    public static MageMagicArmor nick(ArmorItem.Type slot) {
        return new MageMagicArmor(NICK, slot, (new AdamGenericModel("nick_armor", "item/armor")).withEmptyAnim());
    }
    public static MageMagicArmor camr(ArmorItem.Type slot) {
        return new MageMagicArmor(CAMR, slot, (new AdamGenericModel("camr_armor", "item/armor")).withEmptyAnim());
    }
    public static MageMagicArmor matt(ArmorItem.Type slot) {
        return new MageMagicArmor(MATT, slot, (new AdamGenericModel("matt_armor", "item/armor")).withEmptyAnim());
    }
    public static MageMagicArmor adam(ArmorItem.Type slot) {
        return new MageMagicArmor(ADAM, slot, (new AdamGenericModel("adam_armor", "item/armor")).withEmptyAnim());
    }

    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
    }

    public void onArmorTick(ItemStack stack, Level world, Player player) {
        if (!world.isClientSide()) {
            RepairingPerk.attemptRepair(stack, player);
            IPerkHolder<ItemStack> perkHolder = PerkUtil.getPerkHolder(stack);
            if (perkHolder != null) {
                for(PerkInstance instance : perkHolder.getPerkInstances()) {
                    IPerk var8 = instance.getPerk();
                    if (var8 instanceof ITickablePerk) {
                        ITickablePerk tickablePerk = (ITickablePerk)var8;
                        tickablePerk.tick(stack, world, player, instance);
                    }
                }
            }
            if(hasFullSuitOfArmorOn(player)){
                switch(getActiveSetBonus(player)){
                    case "cade_armor" -> {
                        if(!player.hasEffect(WALKING_BLIZZARD_EFFECT.get())){
                            player.addEffect(new MobEffectInstance(WALKING_BLIZZARD_EFFECT.get(), 100, 1, false, false));
                        }
                    }
                    case "ryan_armor" -> {
                        if(!player.hasEffect(FLAME_DEITY_EFFECT.get())){
                            player.addEffect(new MobEffectInstance(FLAME_DEITY_EFFECT.get(), 100, 0, false, false));
                        }
                    }
                    case "nick_armor" -> {
                        if(player.hasEffect(EARTHEN_HEART_EFFECT.get())){
                            if(player.getEffect(EARTHEN_HEART_EFFECT.get()).getDuration() < 50){
                                player.addEffect(new MobEffectInstance(EARTHEN_HEART_EFFECT.get(), 200, 0, false, false));
                            }
                        } else {
                            player.addEffect(new MobEffectInstance(EARTHEN_HEART_EFFECT.get(), 200, 0, false, false));
                        }
                    }
                    case "cam_armor" -> {
                        if(DO_LEAP_FATIGUE.get()){
                            if((player.level().getGameTime() % 160L == 0L)){
                                if(!player.hasEffect(LIGHTNING_STEPS_EFFECT.get())){
                                    player.addEffect(new MobEffectInstance(LIGHTNING_STEPS_EFFECT.get(), 180, 0, false, false));
                                } else if(player.getEffect(LIGHTNING_STEPS_EFFECT.get()).getDuration() < 179) {
                                    int amp = Math.min(2, player.getEffect(LIGHTNING_STEPS_EFFECT.get()).getAmplifier() + 1) ;
                                    player.removeEffect(LIGHTNING_STEPS_EFFECT.get());
                                    player.addEffect(new MobEffectInstance(LIGHTNING_STEPS_EFFECT.get(), 180, amp, false, false));
                                }
                            }
                        } else {
                            if(!player.hasEffect(MOVEMENT_SPEED)){
                                player.addEffect(new MobEffectInstance(MOVEMENT_SPEED, 40, 2, false, false));
                            }
                        }
                    }
                    case "matt_armor" -> {
                        if(!player.hasEffect(HOLY_LEGION_EFFECT.get())){
                            player.addEffect(new MobEffectInstance(HOLY_LEGION_EFFECT.get(), 100, 0, false, false));
                            TerraprismaEntity summon = new TerraprismaEntity(world, player, true);
                            summon.setPos(player.getEyePosition());
                            summon.setOwnerID(player.getUUID());
                            world.addFreshEntity(summon);
                        } else {
                            if(player.getEffect(HOLY_LEGION_EFFECT.get()).getDuration() < 20){
                                player.addEffect(new MobEffectInstance(HOLY_LEGION_EFFECT.get(), 100, 0, false, false));
                            }
                        }
                    }
                    case "adam_armor" -> {
                        if(!player.hasEffect(ABYSSAL_DOMINATION_EFFECT.get())){
                            player.addEffect(new MobEffectInstance(ABYSSAL_DOMINATION_EFFECT.get(), 200, 0, false, false));
                        }
                    }
                }
            }
        }
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot pEquipmentSlot, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> attributes = new ImmutableMultimap.Builder();
        attributes.putAll(super.getDefaultAttributeModifiers(pEquipmentSlot));
        if (this.type.getSlot() == pEquipmentSlot) {
            UUID uuid = (UUID)ARMOR_MODIFIER_UUID_PER_TYPE.get(this.type);
            IPerkHolder<ItemStack> perkHolder = PerkUtil.getPerkHolder(stack);
            if (perkHolder != null) {
                if(this.material == CADE){
                    attributes.put((Attribute) PerkAttributes.MAX_MANA.get(), new AttributeModifier(uuid, "max_mana_armor",             (double)(CADE_MAX_MANA.get() * (perkHolder.getTier() + 1)), AttributeModifier.Operation.ADDITION));
                    attributes.put((Attribute) PerkAttributes.MANA_REGEN_BONUS.get(), new AttributeModifier(uuid, "mana_regen_armor",   (double)(CADE_MANA_REGEN.get() * (perkHolder.getTier() + 1)), AttributeModifier.Operation.ADDITION));
                    attributes.put((Attribute) PerkAttributes.SPELL_DAMAGE_BONUS.get(), new AttributeModifier(uuid,"spell_damage_armor",(double)(CADE_SPELL_DAMAGE.get() * (perkHolder.getTier() + 1)), AttributeModifier.Operation.ADDITION));
                    attributes.put((Attribute) PerkAttributes.WARDING.get(), new AttributeModifier(uuid,"warding_armor",                (double)(CADE_WARDING.get()), AttributeModifier.Operation.ADDITION));
                } else if(this.material == RYAN){
                    attributes.put((Attribute) PerkAttributes.MAX_MANA.get(), new AttributeModifier(uuid, "max_mana_armor",             (double)(RYAN_MAX_MANA.get() * (perkHolder.getTier() + 1)), AttributeModifier.Operation.ADDITION));
                    attributes.put((Attribute) PerkAttributes.MANA_REGEN_BONUS.get(), new AttributeModifier(uuid, "mana_regen_armor",   (double)(RYAN_MANA_REGEN.get() * (perkHolder.getTier() + 1)), AttributeModifier.Operation.ADDITION));
                    attributes.put((Attribute) PerkAttributes.SPELL_DAMAGE_BONUS.get(), new AttributeModifier(uuid,"spell_damage_armor",(double)(RYAN_SPELL_DAMAGE.get() * (perkHolder.getTier())), AttributeModifier.Operation.ADDITION));
                    attributes.put((Attribute) PerkAttributes.WARDING.get(), new AttributeModifier(uuid,"warding_armor",                (double)(RYAN_WARDING.get()), AttributeModifier.Operation.ADDITION));
                } else if(this.material == NICK){
                    attributes.put((Attribute) PerkAttributes.MAX_MANA.get(), new AttributeModifier(uuid, "max_mana_armor",             (double)(NICK_MAX_MANA.get() * (perkHolder.getTier() + 1)), AttributeModifier.Operation.ADDITION));
                    attributes.put((Attribute) PerkAttributes.MANA_REGEN_BONUS.get(), new AttributeModifier(uuid, "mana_regen_armor",   (double)(NICK_MANA_REGEN.get() * (perkHolder.getTier() + 1)), AttributeModifier.Operation.ADDITION));
                    attributes.put((Attribute) PerkAttributes.SPELL_DAMAGE_BONUS.get(), new AttributeModifier(uuid,"spell_damage_armor",(double)(NICK_SPELL_DAMAGE.get() * (perkHolder.getTier() - 1)), AttributeModifier.Operation.ADDITION));
                    attributes.put((Attribute) PerkAttributes.WARDING.get(), new AttributeModifier(uuid,"warding_armor",                (double)(NICK_WARDING.get()), AttributeModifier.Operation.ADDITION));
                }else if(this.material == CAMR){
                    attributes.put((Attribute) PerkAttributes.MAX_MANA.get(), new AttributeModifier(uuid, "max_mana_armor",             (double)(CAM_MAX_MANA.get() * (perkHolder.getTier() + 1)), AttributeModifier.Operation.ADDITION));
                    attributes.put((Attribute) PerkAttributes.MANA_REGEN_BONUS.get(), new AttributeModifier(uuid, "mana_regen_armor",   (double)(CAM_MANA_REGEN.get() * (perkHolder.getTier() + 1)), AttributeModifier.Operation.ADDITION));
                    attributes.put((Attribute) PerkAttributes.SPELL_DAMAGE_BONUS.get(), new AttributeModifier(uuid,"spell_damage_armor",(double)(CAM_SPELL_DAMAGE.get() * (perkHolder.getTier() + 2)), AttributeModifier.Operation.ADDITION));
                    attributes.put((Attribute) PerkAttributes.WARDING.get(), new AttributeModifier(uuid,"warding_armor",                (double)(CAM_WARDING.get()), AttributeModifier.Operation.ADDITION));
                }else if(this.material == MATT){
                    attributes.put((Attribute) PerkAttributes.MAX_MANA.get(), new AttributeModifier(uuid, "max_mana_armor",             (double)(MATT_MAX_MANA.get() * (perkHolder.getTier() + 1)), AttributeModifier.Operation.ADDITION));
                    attributes.put((Attribute) PerkAttributes.MANA_REGEN_BONUS.get(), new AttributeModifier(uuid, "mana_regen_armor",   (double)(MATT_MANA_REGEN.get() * (perkHolder.getTier() + 1)), AttributeModifier.Operation.ADDITION));
                    attributes.put((Attribute) PerkAttributes.SPELL_DAMAGE_BONUS.get(), new AttributeModifier(uuid,"spell_damage_armor",(double)(MATT_SPELL_DAMAGE.get() * (perkHolder.getTier())), AttributeModifier.Operation.ADDITION));
                    attributes.put((Attribute) PerkAttributes.WARDING.get(), new AttributeModifier(uuid,"warding_armor",                (double)(MATT_WARDING.get()), AttributeModifier.Operation.ADDITION));
                }else if(this.material == ADAM){
                    attributes.put((Attribute) PerkAttributes.MAX_MANA.get(), new AttributeModifier(uuid, "max_mana_armor",             (double)(ADAM_MAX_MANA.get() * (perkHolder.getTier() + 1)), AttributeModifier.Operation.ADDITION));
                    attributes.put((Attribute) PerkAttributes.MANA_REGEN_BONUS.get(), new AttributeModifier(uuid, "mana_regen_armor",   (double)(ADAM_MANA_REGEN.get() * (perkHolder.getTier() + 1)), AttributeModifier.Operation.ADDITION));
                    attributes.put((Attribute) PerkAttributes.SPELL_DAMAGE_BONUS.get(), new AttributeModifier(uuid,"spell_damage_armor",(double)(ADAM_SPELL_DAMAGE.get() * (perkHolder.getTier() + 2)), AttributeModifier.Operation.ADDITION));
                    attributes.put((Attribute) PerkAttributes.WARDING.get(), new AttributeModifier(uuid,"warding_armor",                (double)(ADAM_WARDING.get()), AttributeModifier.Operation.ADDITION));
                }

                for(PerkInstance perkInstance : perkHolder.getPerkInstances()) {
                    IPerk perk = perkInstance.getPerk();
                    attributes.putAll(perk.getModifiers(this.type.getSlot(), stack, perkInstance.getSlot().value));
                }
            }
        }

        return attributes.build();
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        IPerkProvider<ItemStack> perkProvider = PerkRegistry.getPerkProvider(stack.getItem());
        if (perkProvider != null) {
            IPerkHolder var7 = perkProvider.getPerkHolder(stack);
            if (var7 instanceof ArmorPerkHolder) {
                ArmorPerkHolder armorPerkHolder = (ArmorPerkHolder)var7;
                tooltip.add(Component.translatable("ars_nouveau.tier", new Object[]{armorPerkHolder.getTier() + 1}).withStyle(ChatFormatting.GOLD));
            }

            perkProvider.getPerkHolder(stack).appendPerkTooltip(tooltip, stack);
        }
        TooltipUtils.addOnShift(tooltip, () -> addInformationAfterShift(stack, world, tooltip, flag), "armor_set");
    }

    EquipmentSlot[] OrderedSlots = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

    @OnlyIn(Dist.CLIENT)
    public void addInformationAfterShift(ItemStack stack, Level world, List<Component> list, TooltipFlag flags) {
        if(stack.getItem() instanceof MageMagicArmor itemArmor){
            Player player = ArsNouveau.proxy.getPlayer();
            if (player != null) {
                ArmorSet set = getArmorSetFromMaterial(itemArmor.material);
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
        return switch (material.getName()) {
            case "an_cade" -> CADE_ARMORSET;
            case "an_ryan" -> RYAN_ARMORSET;
            case "an_nick" -> NICK_ARMORSET;
            case "an_camr" -> CAM_ARMORSET;
            case "an_matt" -> MATT_ARMORSET;
            case "an_adam" -> ADAM_ARMORSET;
            default -> null;
        };
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

    public void onDye(ItemStack stack, DyeColor dyeColor) {
        IPerkHolder<ItemStack> perkHolder = PerkUtil.getPerkHolder(stack);
        if (perkHolder instanceof ArmorPerkHolder armorPerkHolder) {
            armorPerkHolder.setColor(dyeColor.getName());
        }
    }

    public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
        return false;
    }

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            private GeoArmorRenderer<?> renderer;

            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (this.renderer == null) {
                    this.renderer = new AdamArmorRenderer(MageMagicArmor.this.getArmorModel());
                }

                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return this.renderer;
            }
        });
    }

    public GeoModel<MageMagicArmor> getArmorModel() {
        return this.model;
    }

    public @Nullable String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        AdamGenericModel<MageMagicArmor> genericModel = (AdamGenericModel)this.model;
        String var10003 = genericModel.textPathRoot;
        return (new ResourceLocation("adamsarsplus", "textures/" + var10003 + "/" + genericModel.name + "/" + this.getColor(stack) + ".png")).toString();
    }

    public void setColor(String color, ItemStack armor) {
    }

    public String getColor(ItemStack object) {
        IPerkHolder<ItemStack> perkHolder = PerkUtil.getPerkHolder(object);
        if (!(perkHolder instanceof ArmorPerkHolder data)) {
            return "gray";
        } else {
            return data.getColor() != null && !data.getColor().isEmpty() ? data.getColor() : "gray";
        }
    }

    public int getMinTier() {
        return 2;
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
