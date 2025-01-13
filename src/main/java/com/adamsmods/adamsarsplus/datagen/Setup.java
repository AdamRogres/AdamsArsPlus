package com.adamsmods.adamsarsplus.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

import static com.adamsmods.adamsarsplus.AdamsArsPlus.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Setup {

    //use runData configuration to generate stuff, event.includeServer() for data, event.includeClient() for assets
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        PackOutput output = event.getGenerator().getPackOutput();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        gen.addProvider(event.includeServer(), new ArsProviders.ImbuementProvider(gen));
        gen.addProvider(event.includeServer(), new ArsProviders.GlyphProvider(gen));
        gen.addProvider(event.includeServer(), new ArsProviders.EnchantingAppProvider(gen));
        gen.addProvider(event.includeServer(), new ArsProviders.CraftingTableProvider(gen));

        BlockTagsProvider BTP = new AdamsItemTagsProvider.AdamBlockTagsProvider(gen, provider, fileHelper);

        gen.addProvider(event.includeServer(), new AdamsEntityTagProvider(output, provider, fileHelper));
        gen.addProvider(event.includeServer(), new AdamsItemTagsProvider(output, provider, fileHelper));
        gen.addProvider(event.includeServer(), new ArsProviders.PatchouliProvider(gen));
    }



}
