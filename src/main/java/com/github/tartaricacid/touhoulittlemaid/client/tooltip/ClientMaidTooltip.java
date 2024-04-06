package com.github.tartaricacid.touhoulittlemaid.client.tooltip;

import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.MaidModelInfo;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.tooltip.ItemMaidTooltip;
import com.github.tartaricacid.touhoulittlemaid.util.EntityCacheUtil;
import com.github.tartaricacid.touhoulittlemaid.util.ParseI18n;
import com.github.tartaricacid.touhoulittlemaid.util.RenderHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static com.github.tartaricacid.touhoulittlemaid.client.event.SpecialMaidRenderEvent.EASTER_EGG_MODEL;
import static com.github.tartaricacid.touhoulittlemaid.util.EntityCacheUtil.clearMaidDataResidue;

public class ClientMaidTooltip implements ClientTooltipComponent {
    private final @Nullable MaidModelInfo info;
    private final MutableComponent name;
    private final String customName;

    public ClientMaidTooltip(ItemMaidTooltip tooltip) {
        this.info = CustomPackLoader.MAID_MODELS.getInfo(tooltip.getModelId()).orElse(null);
        this.name = (this.info == null ? Component.empty() : Component.translatable(ParseI18n.getI18nKey(info.getName())));
        this.customName = tooltip.getCustomName();
    }

    @Override
    public int getHeight() {
        return 70;
    }

    @Override
    public int getWidth(Font font) {
        return Math.max(font.width(this.name), 50);
    }

    @Override
    public void renderImage(Font font, int pX, int pY, PoseStack poseStack, ItemRenderer itemRenderer, int blitOffset) {
        if (info == null) {
            return;
        }
        Level world = Minecraft.getInstance().level;
        if (world == null) {
            return;
        }

        MutableComponent customNameComponent = null;
        if (StringUtils.isNotBlank(customName)) {
            customNameComponent = Component.Serializer.fromJson(customName);
            if (customNameComponent != null) {
                font.draw(poseStack, customNameComponent.withStyle(ChatFormatting.GRAY), pX, pY + 2, 0xFFFFFF);
            }
        } else {
            font.draw(poseStack, name.withStyle(ChatFormatting.GRAY), pX, pY + 2, 0xFFFFFF);
        }

        int width = this.getWidth(font);
        int posX = pX + width / 2;
        int posY = pY + 64;
        double rot = ((System.currentTimeMillis() / 25.0) % 360);
        Quaternion pose = Vector3f.ZP.rotation((float) Math.PI);
        Quaternion rotation = Vector3f.YP.rotation((float) Math.toRadians(rot));
        pose.mul(rotation);
        EntityMaid maid;
        try {
            maid = (EntityMaid) EntityCacheUtil.ENTITY_CACHE.get(EntityMaid.TYPE, () -> {
                Entity e = EntityMaid.TYPE.create(world);
                return Objects.requireNonNullElseGet(e, () -> new EntityMaid(world));
            });
        } catch (ExecutionException | ClassCastException e) {
            e.printStackTrace();
            return;
        }
        clearMaidDataResidue(maid, false);
        if (StringUtils.isNotBlank(customName)) {
            maid.setCustomName(customNameComponent);
        }
        if (info.getEasterEgg() != null) {
            maid.setModelId(EASTER_EGG_MODEL);
        } else {
            maid.setModelId(info.getModelId().toString());
        }

        RenderSystem.enableScissor(pX, posY - 50, pX + width, posY);
        RenderHelper.renderEntityInInventory(poseStack, posX, posY, (int) (25 * info.getRenderItemScale()), pose, null, maid);
        RenderSystem.disableScissor();
    }
}
