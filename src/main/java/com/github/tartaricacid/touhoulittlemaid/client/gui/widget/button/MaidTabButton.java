package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.client.gui.ITooltipButton;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import com.github.tartaricacid.touhoulittlemaid.util.version.TComponent;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class MaidTabButton extends Button implements ITooltipButton {
    private static final ResourceLocation SIDE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_gui_side.png");
    private final int left;

    public MaidTabButton(int x, int y, int left, String key, Button.OnPress onPressIn, AbstractMaidContainerGui.TooltipRender pOnTooltip) {
        super(x, y, 24, 26, TextComponent.EMPTY, onPressIn, ((pButton, pPoseStack, pMouseX, pMouseY) -> {
            pOnTooltip.renderToolTip(pPoseStack, Lists.newArrayList(
                    TComponent.translatable("gui.touhou_little_maid.button." + key),
                    TComponent.translatable("gui.touhou_little_maid.button." + key + ".desc")
            ), pMouseX, pMouseY);
        }));
        this.left = left;
    }

    public void renderButton(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, SIDE);
        RenderSystem.enableDepthTest();
        if (!this.active) {
            blit(matrixStack, this.x, this.y, left, 21, this.width, this.height, 256, 256);
        }
        blit(matrixStack, this.x + 4, this.y + 6, left, 47, 16, 16, 256, 256);
    }

    @Override
    public boolean isTooltipHovered() {
        return this.active && this.isHovered;
    }

    @Override
    public void renderTooltip(PoseStack poseStack, Minecraft mc, int mouseX, int mouseY) {
        this.renderToolTip(poseStack, mouseX, mouseY);
    }
}