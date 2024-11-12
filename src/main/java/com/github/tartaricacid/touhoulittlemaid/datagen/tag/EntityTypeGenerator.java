package com.github.tartaricacid.touhoulittlemaid.datagen.tag;

import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.EntityTypeTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeEntityTypeTagsProvider;

public class EntityTypeGenerator extends ForgeEntityTypeTagsProvider {
    public EntityTypeGenerator(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, existingFileHelper);
    }

    @Override
    public void addTags() {
        tag(EntityTypeTags.IMPACT_PROJECTILES).add(InitEntities.DANMAKU.get());
        tag(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS).add(InitEntities.FAIRY.get());
        tag(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES).add(InitEntities.FAIRY.get());
    }
}
