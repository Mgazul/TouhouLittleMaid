package com.github.tartaricacid.touhoulittlemaid.api.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.config.IPluginConfig;
import net.minecraftforge.eventbus.api.Event;

public class AddJadeInfoEvent extends Event {
    private final EntityMaid maid;
    private final ITooltip tooltip;
    private final IPluginConfig pluginConfig;

    public AddJadeInfoEvent(EntityMaid maid, ITooltip tooltip, IPluginConfig pluginConfig) {
        this.maid = maid;
        this.tooltip = tooltip;
        this.pluginConfig = pluginConfig;
    }

    public EntityMaid getMaid() {
        return maid;
    }

    public ITooltip getTooltip() {
        return tooltip;
    }

    public IPluginConfig getPluginConfig() {
        return pluginConfig;
    }
}
