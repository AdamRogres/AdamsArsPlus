package com.adamsmods.adamsarsplus.item;

import com.hollingsworth.arsnouveau.common.items.ModItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class RegularItems extends ModItem {
    boolean showEnch = false;
    String name;

    public RegularItems(Properties p) {
        super(p);
    }

    public RegularItems(Properties p, boolean showEnch){
        super(p);
        this.showEnch = showEnch;
    }
    @Override
    public boolean isFoil(ItemStack s){
        if(showEnch){return true;}else{return super.isFoil(s);}
    }
}