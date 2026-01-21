package adamsmods.adamsarsplus.common.items.armor;

import adamsmods.adamsarsplus.AdamsArsPlus;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

import static adamsmods.adamsarsplus.AdamsArsPlus.MODID;

public class Materials {

    public static final DeferredRegister<ArmorMaterial> A_MATERIALS = DeferredRegister.create(BuiltInRegistries.ARMOR_MATERIAL, MODID);

    public Materials() {
    }

    public static final EnumMap<ArmorItem.Type, Integer> CADE_ARMOR_PROTECTION = Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
        map.put(ArmorItem.Type.BOOTS, 2);
        map.put(ArmorItem.Type.LEGGINGS, 5);
        map.put(ArmorItem.Type.CHESTPLATE, 6);
        map.put(ArmorItem.Type.HELMET, 3);
        map.put(ArmorItem.Type.BODY, 4);
    });
    public static final EnumMap<ArmorItem.Type, Integer> RYAN_ARMOR_PROTECTION = Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
        map.put(ArmorItem.Type.BOOTS, 3);
        map.put(ArmorItem.Type.LEGGINGS, 6);
        map.put(ArmorItem.Type.CHESTPLATE, 8);
        map.put(ArmorItem.Type.HELMET, 3);
        map.put(ArmorItem.Type.BODY, 4);
    });
    public static final EnumMap<ArmorItem.Type, Integer> NICK_ARMOR_PROTECTION = Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
        map.put(ArmorItem.Type.BOOTS, 3);
        map.put(ArmorItem.Type.LEGGINGS, 7);
        map.put(ArmorItem.Type.CHESTPLATE, 10);
        map.put(ArmorItem.Type.HELMET, 4);
        map.put(ArmorItem.Type.BODY, 4);
    });
    public static final EnumMap<ArmorItem.Type, Integer> CAMR_ARMOR_PROTECTION = Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
        map.put(ArmorItem.Type.BOOTS, 3);
        map.put(ArmorItem.Type.LEGGINGS, 7);
        map.put(ArmorItem.Type.CHESTPLATE, 10);
        map.put(ArmorItem.Type.HELMET, 4);
        map.put(ArmorItem.Type.BODY, 4);
    });
    public static final EnumMap<ArmorItem.Type, Integer> MATT_ARMOR_PROTECTION = Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
        map.put(ArmorItem.Type.BOOTS, 5);
        map.put(ArmorItem.Type.LEGGINGS, 8);
        map.put(ArmorItem.Type.CHESTPLATE, 11);
        map.put(ArmorItem.Type.HELMET, 6);
        map.put(ArmorItem.Type.BODY, 4);
    });
    public static final EnumMap<ArmorItem.Type, Integer> ADAM_ARMOR_PROTECTION = Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
        map.put(ArmorItem.Type.BOOTS, 6);
        map.put(ArmorItem.Type.LEGGINGS, 9);
        map.put(ArmorItem.Type.CHESTPLATE, 12);
        map.put(ArmorItem.Type.HELMET, 7);
        map.put(ArmorItem.Type.BODY, 4);
    });

    public final static Holder<ArmorMaterial> CADE = A_MATERIALS.register("an_cade", () -> new ArmorMaterial(CADE_ARMOR_PROTECTION
            , 50, new Holder.Direct<>(SoundEvents.PLAYER_HURT_FREEZE), () -> Ingredient.EMPTY, List.of(new ArmorMaterial.Layer(AdamsArsPlus.prefix("an_cade"))), 2.0f, 0));
    public final static Holder<ArmorMaterial> RYAN = A_MATERIALS.register("an_ryan", () -> new ArmorMaterial(RYAN_ARMOR_PROTECTION
            , 50, new Holder.Direct<>(SoundEvents.GENERIC_BURN), () -> Ingredient.EMPTY, List.of(new ArmorMaterial.Layer(AdamsArsPlus.prefix("an_ryan"))), 3.0f, 0));
    public final static Holder<ArmorMaterial> NICK = A_MATERIALS.register("an_nick", () -> new ArmorMaterial(NICK_ARMOR_PROTECTION
            , 50, new Holder.Direct<>(SoundEvents.BONE_MEAL_USE), () -> Ingredient.EMPTY, List.of(new ArmorMaterial.Layer(AdamsArsPlus.prefix("an_nick"))), 4.0f, 0.05f));
    public final static Holder<ArmorMaterial> CAMR = A_MATERIALS.register("an_camr", () -> new ArmorMaterial(CAMR_ARMOR_PROTECTION
            , 50, new Holder.Direct<>(SoundEvents.LIGHTNING_BOLT_THUNDER), () -> Ingredient.EMPTY, List.of(new ArmorMaterial.Layer(AdamsArsPlus.prefix("an_camr"))), 4.0f, 0));
    public final static Holder<ArmorMaterial> MATT = A_MATERIALS.register("an_matt", () -> new ArmorMaterial(MATT_ARMOR_PROTECTION
            , 50, new Holder.Direct<>(SoundEvents.BEACON_POWER_SELECT), () -> Ingredient.EMPTY, List.of(new ArmorMaterial.Layer(AdamsArsPlus.prefix("an_matt"))), 5.0f, 0.1f));
    public final static Holder<ArmorMaterial> ADAM = A_MATERIALS.register("an_adam", () -> new ArmorMaterial(ADAM_ARMOR_PROTECTION
            , 50, new Holder.Direct<>(SoundEvents.END_PORTAL_SPAWN), () -> Ingredient.EMPTY, List.of(new ArmorMaterial.Layer(AdamsArsPlus.prefix("an_adam"))), 6.0f, 0.1f));

}

