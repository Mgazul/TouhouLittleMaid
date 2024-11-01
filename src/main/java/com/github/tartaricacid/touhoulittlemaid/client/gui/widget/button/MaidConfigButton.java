package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class MaidConfigButton extends Button {
    private static final ResourceLocation ICON = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_gui_button.png");
    private final MaidConfigButton.OnPress leftPress;
    private final MaidConfigButton.OnPress rightPress;
    private boolean leftClicked = false;
    private Component value;

    public MaidConfigButton(int x, int y, Component title, Component value, MaidConfigButton.OnPress onLeftPressIn, MaidConfigButton.OnPress onRightPressIn) {
        super(x, y, 164, 13, title, b -> {
        });
        this.leftPress = onLeftPressIn;
        this.rightPress = onRightPressIn;
        this.value = value;
    }

    public MaidConfigButton(int x, int y, Component title, Component value, MaidConfigButton.OnPress onPress) {
        this(x, y, title, value, onPress, onPress);
    }

    @Override
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, ICON);
        RenderSystem.enableDepthTest();
        if (this.isHovered) {
            blit(poseStack, this.x, this.y, 63, 141, this.width, this.height, 256, 256);
        } else {
            blit(poseStack, this.x, this.y, 63, 128, this.width, this.height, 256, 256);
        }
        mc.font.draw(poseStack, this.getMessage(), this.x + 5, this.y + 3, 0x444444);
        drawCenteredStringWithoutShadow(poseStack, mc.font, this.value, this.x + 142, this.y + 3, ChatFormatting.GREEN.getColor());
    }

    public void setValue(Component value) {
        this.value = value;
    }

    @Override
    protected boolean clicked(double mouseX, double mouseY) {
        if (!this.active || !this.visible) {
            return false;
        }
        boolean leftClickX = (this.x + 120) <= mouseX && mouseX <= (this.x + 130);
        boolean rightClickX = (this.x + 154) <= mouseX && mouseX <= (this.x + 164);
        boolean clickY = this.y <= mouseY && mouseY <= (this.y + this.getHeight());
        if (leftClickX && clickY) {
            leftClicked = true;
            return true;
        }
        if (rightClickX && clickY) {
            leftClicked = false;
            return true;
        }
        return false;
    }

    @Override
    public void onPress() {
        if (leftClicked) {
            leftPress.onPress(this);
        } else {
            rightPress.onPress(this);
        }
    }

    public void drawCenteredStringWithoutShadow(PoseStack poseStack, Font pFont, Component pText, int pX, int pY, int pColor) {
        FormattedCharSequence formattedcharsequence = pText.getVisualOrderText();
        drawString(poseStack, pFont, formattedcharsequence, pX - pFont.width(formattedcharsequence) / 2, pY, pColor);
    }

    @OnlyIn(Dist.CLIENT)
    public interface OnPress {
        void onPress(MaidConfigButton button);
    }
}
