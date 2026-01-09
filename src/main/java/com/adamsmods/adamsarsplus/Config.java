package com.adamsmods.adamsarsplus;

import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = AdamsArsPlus.MOD_ID)
public class Config {

    public static class Common {
        public static ForgeConfigSpec.IntValue      MAX_DISCOUNTS;
        public static ForgeConfigSpec.IntValue      MAX_DOMAIN_ENTITIES;
        public static ForgeConfigSpec.BooleanValue  DOMAIN_BURNOUT;
        public static ForgeConfigSpec.BooleanValue  DISCOUNT_BACKLASH;
        public static ForgeConfigSpec.BooleanValue  COM_MAGES;
        public static ForgeConfigSpec.BooleanValue  LOCAL_MAGE_POOL;
        public static ForgeConfigSpec.BooleanValue  MAGES_GRIEF;
        public static ForgeConfigSpec.BooleanValue  DO_LEAP_FATIGUE;
        public static ForgeConfigSpec.ConfigValue<List<? extends String>> MAGE_DIMENSION_BLACKLIST;

        public static ForgeConfigSpec.IntValue      CADE_MAX_MANA;
        public static ForgeConfigSpec.DoubleValue   CADE_MANA_REGEN;
        public static ForgeConfigSpec.IntValue      CADE_SPELL_DAMAGE;
        public static ForgeConfigSpec.IntValue      CADE_WARDING;

        public static ForgeConfigSpec.IntValue      RYAN_MAX_MANA;
        public static ForgeConfigSpec.DoubleValue   RYAN_MANA_REGEN;
        public static ForgeConfigSpec.IntValue      RYAN_SPELL_DAMAGE;
        public static ForgeConfigSpec.IntValue      RYAN_WARDING;

        public static ForgeConfigSpec.IntValue      NICK_MAX_MANA;
        public static ForgeConfigSpec.DoubleValue   NICK_MANA_REGEN;
        public static ForgeConfigSpec.IntValue      NICK_SPELL_DAMAGE;
        public static ForgeConfigSpec.IntValue      NICK_WARDING;

        public static ForgeConfigSpec.IntValue      CAM_MAX_MANA;
        public static ForgeConfigSpec.DoubleValue   CAM_MANA_REGEN;
        public static ForgeConfigSpec.IntValue      CAM_SPELL_DAMAGE;
        public static ForgeConfigSpec.IntValue      CAM_WARDING;

        public static ForgeConfigSpec.IntValue      MATT_MAX_MANA;
        public static ForgeConfigSpec.DoubleValue   MATT_MANA_REGEN;
        public static ForgeConfigSpec.IntValue      MATT_SPELL_DAMAGE;
        public static ForgeConfigSpec.IntValue      MATT_WARDING;

        public static ForgeConfigSpec.IntValue      ADAM_MAX_MANA;
        public static ForgeConfigSpec.DoubleValue   ADAM_MANA_REGEN;
        public static ForgeConfigSpec.IntValue      ADAM_SPELL_DAMAGE;
        public static ForgeConfigSpec.IntValue      ADAM_WARDING;

    
        public Common(ForgeConfigSpec.Builder builder){
            builder.push("Configs for Adams Ars Plus");

            DOMAIN_BURNOUT      = builder.comment("does the domain glyph have a cooldown burnout?").define("doDomainBurnout", true);
            DISCOUNT_BACKLASH   = builder.comment("does the spell efficiency glyph inflict mana exhaustion?").define("doDiscountBacklash", true);
            MAX_DISCOUNTS       = builder.comment("maximum number of times spell efficiency can appear").defineInRange("maxDiscount",5,0,10);
            MAX_DOMAIN_ENTITIES = builder.comment("maximum number of entities a domain can target every spell cast").defineInRange("maxDomainEntities",10,1,100);
            COM_MAGES           = builder.comment("include community spells for the mysterious mage entity?").define("doComMages", true);
            LOCAL_MAGE_POOL     = builder.comment("access pool of possible mage spells from local device (true) or online file (false)?").define("doLocalMagePool", true);
            MAGES_GRIEF         = builder.comment("Allow overworld mages that can mobgrief?").define("doMageGrief", true);
            DO_LEAP_FATIGUE     = builder.comment("Have a cooldown on the leap glyph?").define("doLeapFatigue", true);
            MAGE_DIMENSION_BLACKLIST = builder.comment("Dimensions where mages will not spawn. Ex: [\"minecraft:overworld\", \"undergarden:undergarden\"]. . Run /forge dimensions for a list.").defineList("dimensionBlacklist", new ArrayList<>(), (o) -> true);

            builder.pop();

            builder.push("Armor Configs");

            CADE_MAX_MANA           = builder.comment("Frost Max Mana per tier").defineInRange("cadeMaxMana",40,0,999);
            CADE_MANA_REGEN         = builder.comment("Frost Mana Regen per tier").defineInRange("cadeManaRegen",2.0,0,99);
            CADE_SPELL_DAMAGE       = builder.comment("Frost Spell Damage per tier").defineInRange("cadeSpellDamage",1,0,99);
            CADE_WARDING            = builder.comment("Frost Warding").defineInRange("cadeWarding",0,0,99);

            RYAN_MAX_MANA           = builder.comment("FLAME Max Mana per tier").defineInRange("RYANMaxMana",35,0,999);
            RYAN_MANA_REGEN         = builder.comment("FLAME Mana Regen per tier").defineInRange("RYANManaRegen",1.5,0,99);
            RYAN_SPELL_DAMAGE       = builder.comment("FLAME Spell Damage per tier").defineInRange("RYANSpellDamage",1,0,99);
            RYAN_WARDING            = builder.comment("FLAME Warding").defineInRange("RYANWarding",0,0,99);

            NICK_MAX_MANA           = builder.comment("EARTH Max Mana per tier").defineInRange("NICKMaxMana",30,0,999);
            NICK_MANA_REGEN         = builder.comment("EARTH Mana Regen per tier").defineInRange("NICKManaRegen",1.0,0,99);
            NICK_SPELL_DAMAGE       = builder.comment("EARTH Spell Damage per tier").defineInRange("NICKSpellDamage",1,0,99);
            NICK_WARDING            = builder.comment("EARTH Warding").defineInRange("NICKWarding",0,0,99);

            CAM_MAX_MANA            = builder.comment("LIGHTNING Max Mana per tier").defineInRange("CAMMaxMana",38,0,999);
            CAM_MANA_REGEN          = builder.comment("LIGHTNING Mana Regen per tier").defineInRange("CAMManaRegen",2.5,0,99);
            CAM_SPELL_DAMAGE        = builder.comment("LIGHTNING Spell Damage per tier").defineInRange("CAMSpellDamage",1,0,99);
            CAM_WARDING             = builder.comment("LIGHTNING Warding").defineInRange("CAMWarding",0,0,99);

            MATT_MAX_MANA           = builder.comment("HOLY Max Mana per tier").defineInRange("MATTMaxMana",30,0,999);
            MATT_MANA_REGEN         = builder.comment("HOLY Mana Regen per tier").defineInRange("MATTManaRegen",1.0,0,99);
            MATT_SPELL_DAMAGE       = builder.comment("HOLY Spell Damage per tier").defineInRange("MATTSpellDamage",1,0,99);
            MATT_WARDING            = builder.comment("HOLY Warding").defineInRange("MATTWarding",1,0,99);

            ADAM_MAX_MANA           = builder.comment("VOID Max Mana per tier").defineInRange("ADAMMaxMana",50,0,999);
            ADAM_MANA_REGEN         = builder.comment("VOID Mana Regen per tier").defineInRange("ADAMManaRegen",4.0,0,99);
            ADAM_SPELL_DAMAGE       = builder.comment("VOID Spell Damage per tier").defineInRange("ADAMSpellDamage",1,0,99);
            ADAM_WARDING            = builder.comment("VOID Warding").defineInRange("ADAMWarding",1,0,99);

            builder.pop();

            builder.push("Boss Configs");


            builder.pop();
        }
    }

    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

}
