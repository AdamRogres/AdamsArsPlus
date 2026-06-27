package adamsmods.adamsarsplus.common.perk;

import adamsmods.adamsarsplus.AdamsArsPlus;
import adamsmods.adamsarsplus.util.APerkSlot;
import com.google.common.collect.Multimap;
import com.hollingsworth.arsnouveau.api.perk.Perk;
import com.hollingsworth.arsnouveau.api.perk.PerkSlot;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.UUID;

public class InvinciblePerk extends Perk {
    public static final InvinciblePerk INSTANCE = new InvinciblePerk(AdamsArsPlus.prefix("thread_invincible"));

    public InvinciblePerk(ResourceLocation key) {
        super(key);
    }

    public static final UUID PERK_UUID = UUID.fromString("e2a7e5bc-ab34-4ea5-b3b6-ef23d352fa49");

    public ItemAttributeModifiers applyAttributeModifiers(ItemAttributeModifiers modifiers, ItemStack stack, int slotValue, EquipmentSlotGroup equipmentSlotGroup) {
        return modifiers.withModifierAdded(Attributes.ARMOR, new AttributeModifier(INSTANCE.getRegistryName(), (double)(2 * (slotValue - 3) + 2), AttributeModifier.Operation.ADD_VALUE), equipmentSlotGroup);
    }

    public String getLangName() {
        return "Invincible";
    }

    public String getLangDescription() {
        return "Grants an increasing amount of Armor each level.";
    }

    public PerkSlot minimumSlot() {
        return APerkSlot.FOUR;
    }

    public String getName() {
        return Component.translatable("item.adamsarsplus.thread_invincible").getString();
    }

}
