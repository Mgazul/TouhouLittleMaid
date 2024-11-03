package com.github.tartaricacid.touhoulittlemaid.datagen;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MaidDataGenerator {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();

        // Advancement
        generator.addProvider(new AdvancementGenerator(generator, helper));

        // Loot Tables
        generator.addProvider(new LootTableGenerator.AdvancementLootTables(generator));
    }
}
