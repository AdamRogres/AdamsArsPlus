package com.adamsmods.adamsarsplus.datagen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.world.item.DyeColor;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class CommunityMages {
    public static List<ComMages> mages = new ArrayList();

    public CommunityMages() {
    }

    public static void init() {
        try {

            JsonObject object = JsonParser.parseString(readUrl(new URL("https://raw.githubusercontent.com/AdamRogres/AdamsArsPlus/refs/heads/master/src/main/resources/data/adamsarsplus/mysterious_mage_spells/spell_list_new.json"))).getAsJsonObject();


            for(JsonElement element : object.getAsJsonArray("mageOwner")) {
                JsonObject jsonObject = element.getAsJsonObject();
                String name  = jsonObject.get("name").getAsString();
                String color = jsonObject.get("color").getAsString();
                String spell = jsonObject.get("spell").getAsString();
                String coold = jsonObject.get("coold").getAsString();
                String type  = jsonObject.get("type").getAsString();
                String tier  = jsonObject.get("tier").getAsString();
                mages.add(new ComMages(name, color, spell, coold, type, tier));
            }
        } catch (IOException var2) {
            var2.printStackTrace();
            if (!FMLEnvironment.production) {
                throw new RuntimeException("Failed to load supporters.json");
            }
        }

    }

    public static void initAlt(){
        mages.add(new ComMages("Flame Mage","red",
                "ignite-aoe-delay-flare-explosion-burst-aoe-sensitive-ignite"
                ,"medium", "projectile", "one"));
        mages.add(new ComMages("Water Mage","blue",
                "freeze-cold_snap-burst-sensitive-conjure_water-freeze"
                ,"medium", "projectile", "one"));
        mages.add(new ComMages("Earth Mage","green",
                "burst-sensitive-raise_earth"
                ,"medium", "projectile", "one"));
        mages.add(new ComMages("Air Mage","white",
                "launch-slowfall-delay-duration_down-windshear"
                ,"medium", "projectile", "one"));
        mages.add(new ComMages("Abjuration Mage","purple",
                "harm-amp-extend-hex-swap_target-heal"
                ,"short", "projectile", "one"));
        mages.add(new ComMages("Conjuration Mage","gray",
                "summon_undead"
                ,"long", "projectile", "one"));
        mages.add(new ComMages("Manipulation Mage","orange",
                "harm-amp-swap_target-craft"
                ,"short", "projectile", "one"));
    }

    public static String readUrl(URL url) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            StringBuilder buffer = new StringBuilder();
            char[] chars = new char[1024];

            int read;
            while((read = reader.read(chars)) != -1) {
                buffer.append(chars, 0, read);
            }

            return buffer.toString();
        }
    }

    public static class ComMages {
        public String name;
        public String color;
        public String spell;
        public String coold;
        public String type;
        public String tier;

        public ComMages(String name, String color, String spell, String coold, String type, String tier) {
            this.name = name;
            this.color = color;
            this.spell = spell;
            this.coold = coold;
            this.type = type;
            this.tier = tier;

            if (!FMLEnvironment.production) {
                if (name == null) {
                    throw new RuntimeException("Name is null");
                }

                if (color == null) {
                    throw new RuntimeException("Color is null");
                }

                if (spell == null){
                    throw new RuntimeException("Spell is null");
                }

                if(coold == null){
                    throw new RuntimeException("Cooldown is null");
                }

                if(type == null){
                    throw new RuntimeException("Type is null");
                }

                if(tier == null){
                    throw new RuntimeException("Tier is null");
                }
            }
        }
    }
}
