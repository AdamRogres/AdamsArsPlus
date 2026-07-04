package adamsmods.adamsarsplus.common.items.armor;

import com.hollingsworth.arsnouveau.api.perk.IPerk;
import com.hollingsworth.arsnouveau.api.perk.PerkSlot;
import com.hollingsworth.arsnouveau.common.items.data.ArmorPerkHolder;
import com.hollingsworth.arsnouveau.common.items.data.PerkMap;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static adamsmods.adamsarsplus.common.items.armor.Materials.*;
import static adamsmods.adamsarsplus.registry.ModItems.*;

public class MageArmorPerkHolder extends ArmorPerkHolder {

    private Holder<ArmorMaterial> material;
    private Boolean aN;

    public MageArmorPerkHolder(Holder<ArmorMaterial> materialIn, Boolean awakened) {
        super("", new ArrayList<>(), determineTier(materialIn, awakened), new HashMap<>());
        this.material = materialIn;
        this.aN = awakened;
    }

    public MageArmorPerkHolder(String color, List<IPerk> perks, int tier, PerkMap perkTags, Holder<ArmorMaterial> materialIn, Boolean awakened) {
        super(color, perks, tier, perkTags);
        this.material = materialIn;
        this.aN = awakened;
    }

    private static int determineTier(Holder<ArmorMaterial> material, boolean awakened) {
        if (material == CADE || material == RYAN || material == NICK) {
            return awakened ? 3 : 2;
        } else if (material == CAMR || material == MATT) {
            return awakened ? 4 : 3;
        } else if (material == ADAM) {
            return awakened ? 5 : 4;
        }
        return 1;
    }

    @Override
    public MageArmorPerkHolder setTier(int tier) {
        return new MageArmorPerkHolder("", getPerks(), tier, getPerkTags(), this.material, this.aN);
    }
}