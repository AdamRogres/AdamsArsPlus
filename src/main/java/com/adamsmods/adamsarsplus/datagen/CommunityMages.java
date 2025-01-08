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
import java.util.UUID;
import net.minecraft.world.item.DyeColor;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class CommunityMages {
    public static List<ComMages> mages = new ArrayList();

    public CommunityMages() {
    }

    public static void init() {
        try {

            JsonObject object = JsonParser.parseString(readUrl(new URL("https://raw.githubusercontent.com/AdamRogres/AdamsArsPlus/refs/heads/master/src/main/resources/data/adamsarsplus/mysterious_mage_spells/spell_list.json"))).getAsJsonObject();


            for(JsonElement element : object.getAsJsonArray("mageOwner")) {
                JsonObject jsonObject = element.getAsJsonObject();
                String name = jsonObject.get("name").getAsString();
                String color = jsonObject.get("color").getAsString();
                String spell = jsonObject.get("spell").getAsString();
                Integer coold = jsonObject.get("coold").getAsInt();
                mages.add(new ComMages(name, color, spell, coold));
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
                "ars_nouveau:glyph_ignite-ars_nouveau:glyph_aoe"
                ,100));
        mages.add(new ComMages("Water Mage","blue",
                "ars_nouveau:glyph_ignite-ars_nouveau:glyph_aoe"
                ,100));
        mages.add(new ComMages("Earth Mage","green",
                "ars_nouveau:glyph_ignite-ars_nouveau:glyph_aoe"
                ,100));
        mages.add(new ComMages("Air Mage","white",
                "ars_nouveau:glyph_ignite-ars_nouveau:glyph_aoe"
                ,100));
        mages.add(new ComMages("Abjuration Mage","purple",
                "ars_nouveau:glyph_ignite-ars_nouveau:glyph_aoe"
                ,100));
        mages.add(new ComMages("Conjuration Mage","gray",
                "ars_nouveau:glyph_ignite-ars_nouveau:glyph_aoe"
                ,100));
        mages.add(new ComMages("Manipulation Mage","orange",
                "ars_nouveau:glyph_ignite-ars_nouveau:glyph_aoe"
                ,100));
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
        public Integer coold;

        public ComMages(String name, String color, String spell, Integer coold) {
            this.name = name;
            this.color = color;
            this.spell = spell;
            this.coold = coold;

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

                boolean foundColor = Arrays.stream(DyeColor.values()).anyMatch((dye) -> dye.getName().equals(color));
                if (!foundColor) {
                    throw new RuntimeException("Color is not a valid dye color");
                }
            }

        }
    }
}
