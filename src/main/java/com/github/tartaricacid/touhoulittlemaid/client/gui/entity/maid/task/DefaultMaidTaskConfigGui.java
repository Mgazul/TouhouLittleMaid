package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.task;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.task.TaskConfigContainer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import org.anti_ad.mc.ipn.api.IPNButton;
import org.anti_ad.mc.ipn.api.IPNGuiHint;
import org.anti_ad.mc.ipn.api.IPNPlayerSideOnly;

import java.util.List;

@IPNPlayerSideOnly
@IPNGuiHint(button = IPNButton.SORT, horizontalOffset = -36, bottom = -12)
@IPNGuiHint(button = IPNButton.SORT_COLUMNS, horizontalOffset = -24, bottom = -24)
@IPNGuiHint(button = IPNButton.SORT_ROWS, horizontalOffset = -12, bottom = -36)
@IPNGuiHint(button = IPNButton.SHOW_EDITOR, horizontalOffset = -5)
@IPNGuiHint(button = IPNButton.SETTINGS, horizontalOffset = -5)
public class DefaultMaidTaskConfigGui extends MaidTaskConfigGui<TaskConfigContainer> {
    private static final ResourceLocation BG = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/default_task_config.png");

    public DefaultMaidTaskConfigGui(TaskConfigContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTicks, int x, int y) {
        super.renderBg(poseStack, partialTicks, x, y);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BG);
        blit(poseStack, leftPos + 80, topPos + 28, 0, 0, imageWidth, 137);
    }

    @Override
    protected void renderAddition(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        List<FormattedCharSequence> split = font.split(Component.translatable("gui.touhou_little_maid.default_task_config.title"), 160);
        int yOffset = topPos + 38;
        for (FormattedCharSequence fs : split) {
            font.draw(poseStack, fs, leftPos + 88, yOffset, ChatFormatting.DARK_GRAY.getColor());
            yOffset += font.lineHeight;
        }
    }
}