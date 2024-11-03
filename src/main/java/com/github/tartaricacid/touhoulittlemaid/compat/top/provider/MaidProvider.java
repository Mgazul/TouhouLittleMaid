package com.github.tartaricacid.touhoulittlemaid.compat.top.provider;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.event.AddTopInfoEvent;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import mcjty.theoneprobe.api.*;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;

public class MaidProvider implements IProbeInfoEntityProvider {
    private static final String ID = (new ResourceLocation(TouhouLittleMaid.MOD_ID, "maid")).toString();

    @Override
    public void addProbeEntityInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player playerEntity, Level world, Entity entity, IProbeHitEntityData iProbeHitEntityData) {
        if (entity instanceof EntityMaid maid) {
            if (maid.isTame()) {
                IMaidTask task = maid.getTask();
                MutableComponent taskTitle = new TranslatableComponent("top.touhou_little_maid.entity_maid.task").append(task.getName());
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER)).text(taskTitle);

                // 添加于 Mode 之下，用于给 Mode 添加额外的信息
                MinecraftForge.EVENT_BUS.post(new AddTopInfoEvent(maid, probeMode, probeInfo, iProbeHitEntityData));

                MutableComponent scheduleTitle = new TranslatableComponent("top.touhou_little_maid.entity_maid.schedule").append(getActivityTransText(maid));
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER)).text(scheduleTitle);

                MutableComponent favorabilityTitle = new TranslatableComponent("top.touhou_little_maid.entity_maid.favorability", maid.getFavorabilityManager().getLevel());
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER)).text(favorabilityTitle);

                MutableComponent nextFavorabilityPointTitle = new TranslatableComponent("top.touhou_little_maid.entity_maid.nex_favorability_point", maid.getFavorabilityManager().nextLevelPoint());
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER)).text(nextFavorabilityPointTitle);
            }
            if (maid.getIsInvulnerable()) {
                MutableComponent text = new TranslatableComponent("top.touhou_little_maid.entity_maid.invulnerable").withStyle(ChatFormatting.DARK_PURPLE);
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER)).text(text);
            }
        }
    }

    @Override
    public String getID() {
        return ID;
    }

    private MutableComponent getActivityTransText(EntityMaid maid) {
        return getActivityTransText(maid.getScheduleDetail());
    }

    private MutableComponent getActivityTransText(Activity activity) {
        return new TranslatableComponent("gui.touhou_little_maid.activity." + activity.getName());
    }
}