package com.github.tartaricacid.touhoulittlemaid.compat.hwyla;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaEntityAccessor;
import mcp.mobius.waila.api.IWailaEntityProvider;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author TartaricAcid
 * @date 2019/7/25 19:46
 **/
public class EntityMaidProvider implements IWailaEntityProvider {
    @Nonnull
    @Override
    public List<String> getWailaBody(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) {
        if (entity instanceof EntityMaid && accessor.getEntity() instanceof EntityMaid) {
            EntityMaid maid = (EntityMaid) accessor.getEntity();
            currenttip.add(I18n.format("hwyla_top.touhou_little_maid.entity_maid.task",
                    maid.getTask().getTaskI18n()));
        }
        return currentTip;
    }
}
