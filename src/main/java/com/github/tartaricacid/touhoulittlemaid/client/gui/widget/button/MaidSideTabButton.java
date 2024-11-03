package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.client.gui.ITooltipButton;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import com.github.tartaricacid.touhoulittlemaid.util.version.TButton;
import com.github.tartaricacid.touhoulittlemaid.util.version.TComponent;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

/**
 * 女仆界面侧边栏按钮
 */
public class MaidSideTabButton extends TButton implements ITooltipButton {
    private static final ResourceLocation SIDE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_gui_side.png");
    private static final int V_OFFSET = 107;
    private final int top;

    public MaidSideTabButton(int x, int y, int top, OnPress onPressIn, List<Component> tooltips, AbstractMaidContainerGui.TooltipRender pOnTooltip) {
        super(TButton.builder(TComponent.empty(), onPressIn).pos(x, y).size(26, 24).onTooltip(tooltips, pOnTooltip));
        this.top = V_OFFSET + top;
    }

    @Override
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, SIDE);
        RenderSystem.enableDepthTest();
        if (!this.active) {
            blit(poseStack, this.x + 2, this.y, 209, top, this.width, this.height, 256, 256);
        }
        // 193, 111
        blit(poseStack, this.x + 6, this.y + 4, 193, top + 4, 16, 16, 256, 256);
    }

    @Override
    public boolean isTooltipHovered() {
        return this.isHovered;
    }

    @Override
    public void renderTooltip(PoseStack poseStack, Minecraft mc, int mouseX, int mouseY) {
        this.renderToolTip(poseStack, mouseX, mouseY);
    }
}