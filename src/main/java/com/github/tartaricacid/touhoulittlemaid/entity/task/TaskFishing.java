package com.github.tartaricacid.touhoulittlemaid.entity.task;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.ride.MaidRideFindWaterTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidFindChairTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TaskFishing implements IMaidTask {
    public static final ResourceLocation UID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "fishing");

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public ItemStack getIcon() {
        return Items.FISHING_ROD.getDefaultInstance();
    }

    @Nullable
    @Override
    public SoundEvent getAmbientSound(EntityMaid maid) {
        return InitSounds.MAID_IDLE.get();
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        return Lists.newArrayList(Pair.of(5, new MaidFindChairTask(0.6f)));
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createRideBrainTasks(EntityMaid maid) {
        return Lists.newArrayList(Pair.of(5, new MaidRideFindWaterTask(6, 3)));
    }
}
