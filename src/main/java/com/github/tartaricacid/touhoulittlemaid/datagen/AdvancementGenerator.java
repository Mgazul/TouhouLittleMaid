package com.github.tartaricacid.touhoulittlemaid.datagen;

import com.github.tartaricacid.touhoulittlemaid.datagen.advancement.BaseAdvancement;
import com.github.tartaricacid.touhoulittlemaid.datagen.advancement.ChallengeAdvancement;
import com.github.tartaricacid.touhoulittlemaid.datagen.advancement.FavorabilityAdvancement;
import com.github.tartaricacid.touhoulittlemaid.datagen.advancement.MaidBaseAdvancement;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Consumer;

public class AdvancementGenerator extends AdvancementProvider {

    public AdvancementGenerator(DataGenerator generatorIn, ExistingFileHelper fileHelperIn) {
        super(generatorIn, fileHelperIn);
    }

    @Override
    protected void registerAdvancements(Consumer<Advancement> saver, ExistingFileHelper existingFileHelper) {
        BaseAdvancement.generate(saver, existingFileHelper);
        MaidBaseAdvancement.generate(saver, existingFileHelper);
        FavorabilityAdvancement.generate(saver, existingFileHelper);
        ChallengeAdvancement.generate(saver, existingFileHelper);
    }
}
