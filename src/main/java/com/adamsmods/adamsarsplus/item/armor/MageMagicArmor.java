package com.adamsmods.adamsarsplus.item.armor;

import com.adamsmods.adamsarsplus.item.client.renderer.AdamArmorRenderer;
import com.adamsmods.adamsarsplus.item.client.renderer.AdamGenericModel;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.hollingsworth.arsnouveau.api.client.IVariantColorProvider;
import com.hollingsworth.arsnouveau.api.mana.IManaEquipment;
import com.hollingsworth.arsnouveau.api.perk.*;
import com.hollingsworth.arsnouveau.api.registry.PerkRegistry;
import com.hollingsworth.arsnouveau.api.util.PerkUtil;
import com.hollingsworth.arsnouveau.client.renderer.item.ArmorRenderer;
import com.hollingsworth.arsnouveau.client.renderer.tile.GenericModel;
import com.hollingsworth.arsnouveau.common.armor.Materials;
import com.hollingsworth.arsnouveau.common.crafting.recipes.IDyeable;
import com.hollingsworth.arsnouveau.common.perk.RepairingPerk;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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

import java.util.EnumMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static com.adamsmods.adamsarsplus.item.armor.Materials.*;

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
                    attributes.put((Attribute) PerkAttributes.MAX_MANA.get(), new AttributeModifier(uuid, "max_mana_armor",             (double)(40 * (perkHolder.getTier() + 1)), AttributeModifier.Operation.ADDITION));
                    attributes.put((Attribute) PerkAttributes.MANA_REGEN_BONUS.get(), new AttributeModifier(uuid, "mana_regen_armor",   (double)(2 * (perkHolder.getTier() + 1)), AttributeModifier.Operation.ADDITION));
                    attributes.put((Attribute) PerkAttributes.SPELL_DAMAGE_BONUS.get(), new AttributeModifier(uuid,"spell_damage_armor",(double)(perkHolder.getTier() + 1), AttributeModifier.Operation.ADDITION));
                } else if(this.material == RYAN){
                    attributes.put((Attribute) PerkAttributes.MAX_MANA.get(), new AttributeModifier(uuid, "max_mana_armor",             (double)(35 * (perkHolder.getTier() + 1)), AttributeModifier.Operation.ADDITION));
                    attributes.put((Attribute) PerkAttributes.MANA_REGEN_BONUS.get(), new AttributeModifier(uuid, "mana_regen_armor",   (double)(1.5 * (perkHolder.getTier() + 1)), AttributeModifier.Operation.ADDITION));
                    attributes.put((Attribute) PerkAttributes.SPELL_DAMAGE_BONUS.get(), new AttributeModifier(uuid,"spell_damage_armor",(double)(perkHolder.getTier()), AttributeModifier.Operation.ADDITION));
                } else if(this.material == NICK){
                    attributes.put((Attribute) PerkAttributes.MAX_MANA.get(), new AttributeModifier(uuid, "max_mana_armor",             (double)(30 * (perkHolder.getTier() + 1)), AttributeModifier.Operation.ADDITION));
                    attributes.put((Attribute) PerkAttributes.MANA_REGEN_BONUS.get(), new AttributeModifier(uuid, "mana_regen_armor",   (double)(1 * (perkHolder.getTier() + 1)), AttributeModifier.Operation.ADDITION));
                    attributes.put((Attribute) PerkAttributes.SPELL_DAMAGE_BONUS.get(), new AttributeModifier(uuid,"spell_damage_armor",(double)(perkHolder.getTier() - 1), AttributeModifier.Operation.ADDITION));
                }else if(this.material == CAMR){
                    attributes.put((Attribute) PerkAttributes.MAX_MANA.get(), new AttributeModifier(uuid, "max_mana_armor",             (double)(38 * (perkHolder.getTier() + 1)), AttributeModifier.Operation.ADDITION));
                    attributes.put((Attribute) PerkAttributes.MANA_REGEN_BONUS.get(), new AttributeModifier(uuid, "mana_regen_armor",   (double)(2.5 * (perkHolder.getTier() + 1)), AttributeModifier.Operation.ADDITION));
                    attributes.put((Attribute) PerkAttributes.SPELL_DAMAGE_BONUS.get(), new AttributeModifier(uuid,"spell_damage_armor",(double)(perkHolder.getTier() + 2), AttributeModifier.Operation.ADDITION));
                }else if(this.material == MATT){
                    attributes.put((Attribute) PerkAttributes.MAX_MANA.get(), new AttributeModifier(uuid, "max_mana_armor",             (double)(30 * (perkHolder.getTier() + 1)), AttributeModifier.Operation.ADDITION));
                    attributes.put((Attribute) PerkAttributes.MANA_REGEN_BONUS.get(), new AttributeModifier(uuid, "mana_regen_armor",   (double)(1 * (perkHolder.getTier() + 1)), AttributeModifier.Operation.ADDITION));
                    attributes.put((Attribute) PerkAttributes.SPELL_DAMAGE_BONUS.get(), new AttributeModifier(uuid,"spell_damage_armor",(double)(perkHolder.getTier()), AttributeModifier.Operation.ADDITION));
                    attributes.put((Attribute) PerkAttributes.WARDING.get(), new AttributeModifier(uuid,"warding_armor",                (double)(1), AttributeModifier.Operation.ADDITION));
                }else if(this.material == ADAM){
                    attributes.put((Attribute) PerkAttributes.MAX_MANA.get(), new AttributeModifier(uuid, "max_mana_armor",             (double)(50 * (perkHolder.getTier() + 1)), AttributeModifier.Operation.ADDITION));
                    attributes.put((Attribute) PerkAttributes.MANA_REGEN_BONUS.get(), new AttributeModifier(uuid, "mana_regen_armor",   (double)(4 * (perkHolder.getTier() + 1)), AttributeModifier.Operation.ADDITION));
                    attributes.put((Attribute) PerkAttributes.SPELL_DAMAGE_BONUS.get(), new AttributeModifier(uuid,"spell_damage_armor",(double)(perkHolder.getTier() + 2), AttributeModifier.Operation.ADDITION));
                    attributes.put((Attribute) PerkAttributes.WARDING.get(), new AttributeModifier(uuid,"warding_armor",                (double)(1), AttributeModifier.Operation.ADDITION));
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

    }

    public void onDye(ItemStack stack, DyeColor dyeColor) {
        IPerkHolder<ItemStack> perkHolder = PerkUtil.getPerkHolder(stack);
        if (perkHolder instanceof ArmorPerkHolder armorPerkHolder) {
            armorPerkHolder.setColor(dyeColor.getName());
        }

    }

    public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
        return true;
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
        return (new ResourceLocation("adamsarsplus", "textures/" + var10003 + "/" + genericModel.name + "_" + this.getColor(stack) + ".png")).toString();
    }

    public void setColor(String color, ItemStack armor) {
    }

    public String getColor(ItemStack object) {
        IPerkHolder<ItemStack> perkHolder = PerkUtil.getPerkHolder(object);
        if (!(perkHolder instanceof ArmorPerkHolder data)) {
            return "default";
        } else {
            return data.getColor() != null && !data.getColor().isEmpty() ? data.getColor() : "default";
        }
    }

    public int getMinTier() {
        return 2;
    }
}
