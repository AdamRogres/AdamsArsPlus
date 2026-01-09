package com.adamsmods.adamsarsplus.item.armor;

import com.hollingsworth.arsnouveau.api.perk.ArmorPerkHolder;
import com.hollingsworth.arsnouveau.api.perk.PerkSlot;
import net.minecraft.world.item.ItemStack;

import java.util.List;

import static com.adamsmods.adamsarsplus.registry.ModRegistry.*;

public class MageArmorPerkHolder extends ArmorPerkHolder {

    public MageArmorPerkHolder(ItemStack stack, List<List<PerkSlot>> slotsForTier) {
        super(stack, slotsForTier);

        if(stack.getItem() == CADE_BOOTS.get() || stack.getItem() == CADE_LEGGINGS.get() || stack.getItem() == CADE_ROBES.get() || stack.getItem() == CADE_HOOD.get()){
            this.setTier(2);
        } else
        if(stack.getItem() == RYAN_BOOTS.get() || stack.getItem() == RYAN_LEGGINGS.get() || stack.getItem() == RYAN_ROBES.get() || stack.getItem() == RYAN_HOOD.get()){
            this.setTier(2);
        } else
        if(stack.getItem() == NICK_BOOTS.get() || stack.getItem() == NICK_LEGGINGS.get() || stack.getItem() == NICK_ROBES.get() || stack.getItem() == NICK_HOOD.get()){
            this.setTier(2);
        } else
        if(stack.getItem() == CADE_BOOTS_A.get() || stack.getItem() == CADE_LEGGINGS_A.get() || stack.getItem() == CADE_ROBES_A.get() || stack.getItem() == CADE_HOOD_A.get()){
            this.setTier(3);
        } else
        if(stack.getItem() == RYAN_BOOTS_A.get() || stack.getItem() == RYAN_LEGGINGS_A.get() || stack.getItem() == RYAN_ROBES_A.get() || stack.getItem() == RYAN_HOOD_A.get()){
            this.setTier(3);
        } else
        if(stack.getItem() == NICK_BOOTS_A.get() || stack.getItem() == NICK_LEGGINGS_A.get() || stack.getItem() == NICK_ROBES_A.get() || stack.getItem() == NICK_HOOD_A.get()){
            this.setTier(3);
        } else
        if(stack.getItem() == CAMR_BOOTS.get() || stack.getItem() == CAMR_LEGGINGS.get() || stack.getItem() == CAMR_ROBES.get() || stack.getItem() == CAMR_HOOD.get()){
            this.setTier(3);
        } else
        if(stack.getItem() == MATT_BOOTS.get() || stack.getItem() == MATT_LEGGINGS.get() || stack.getItem() == MATT_ROBES.get() || stack.getItem() == MATT_HOOD.get()){
            this.setTier(3);
        } else
        if(stack.getItem() == CAMR_BOOTS_A.get() || stack.getItem() == CAMR_LEGGINGS_A.get() || stack.getItem() == CAMR_ROBES_A.get() || stack.getItem() == CAMR_HOOD_A.get()){
            this.setTier(4);
        } else
        if(stack.getItem() == MATT_BOOTS_A.get() || stack.getItem() == MATT_LEGGINGS_A.get() || stack.getItem() == MATT_ROBES_A.get() || stack.getItem() == MATT_HOOD_A.get()){
            this.setTier(4);
        } else
        if(stack.getItem() == ADAM_BOOTS.get() || stack.getItem() == ADAM_LEGGINGS.get() || stack.getItem() == ADAM_ROBES.get() || stack.getItem() == ADAM_HOOD.get()){
            this.setTier(4);
        } else 
        if(stack.getItem() == ADAM_BOOTS_A.get() || stack.getItem() == ADAM_LEGGINGS_A.get() || stack.getItem() == ADAM_ROBES_A.get() || stack.getItem() == ADAM_HOOD_A.get()){
            this.setTier(5);
        }

    }
}
