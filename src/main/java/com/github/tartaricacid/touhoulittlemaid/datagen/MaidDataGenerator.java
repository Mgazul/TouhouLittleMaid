package com.github.tartaricacid.touhoulittlemaid.datagen;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.datagen.tag.DamageTypeGenerator;
import com.github.tartaricacid.touhoulittlemaid.datagen.tag.EntityTypeGenerator;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MaidDataGenerator {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();

        // Advancement
        generator.addProvider(true, new ForgeAdvancementProvider(
                packOutput, event.getLookupProvider(), event.getExistingFileHelper(),
                Collections.singletonList(new AdvancementGenerator())
        ));

        // Loot Tables
        generator.addProvider(event.includeServer(), new LootTableProvider(packOutput,
                Set.of(LootTableGenerator.CAKE),
                List.of(new LootTableProvider.SubProviderEntry(LootTableGenerator.AdvancementLootTables::new, LootContextParamSets.ADVANCEMENT_REWARD))
        ));

        // Tags
        generator.addProvider(event.includeServer(), new DamageTypeGenerator(packOutput, event.getLookupProvider(), event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new EntityTypeGenerator(packOutput, event.getLookupProvider(), event.getExistingFileHelper()));

        //generator.addProvider(true, new LanguageGenerator(packOutput));
    }
}
