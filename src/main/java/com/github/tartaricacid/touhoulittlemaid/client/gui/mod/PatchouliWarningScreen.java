package com.github.tartaricacid.touhoulittlemaid.client.gui.mod;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.StringUtils;

@OnlyIn(value = Dist.CLIENT)
public class PatchouliWarningScreen extends Screen {
    private final String patchouliUrl = "https://www.curseforge.com/minecraft/mc-mods/patchouli";
    private final Screen lastScreen;
    private MultiLineLabel message = MultiLineLabel.EMPTY;

    protected PatchouliWarningScreen(Screen lastScreen) {
        super(Component.literal("Patchouli"));
        this.lastScreen = lastScreen;
    }

    public static void open() {
        Minecraft.getInstance().setScreen(new PatchouliWarningScreen(Minecraft.getInstance().screen));
    }

    @Override
    protected void init() {
        int posX = (this.width - 200) / 2;
        int posY = this.height / 2;
        this.message = MultiLineLabel.create(this.font, Component.translatable("gui.touhou_little_maid.patchouli_warning.tips"), 300);
        this.addRenderableWidget(new Button(posX, posY - 15, 200, 20, Component.translatable("gui.touhou_little_maid.patchouli_warning.download"), b -> openUrl(patchouliUrl)));
        this.addRenderableWidget(new Button(posX, posY + 50, 200, 20, CommonComponents.GUI_BACK, (pressed) -> Minecraft.getInstance().setScreen(this.lastScreen)));
    }

    @Override
    public void render(PoseStack poseStack, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(poseStack);
        this.message.renderCentered(poseStack, this.width / 2, 80);
        super.render(poseStack, pMouseX, pMouseY, pPartialTick);
    }

    private void openUrl(String url) {
        if (StringUtils.isNotBlank(url) && minecraft != null) {
            minecraft.setScreen(new ConfirmLinkScreen(yes -> {
                if (yes) {
                    Util.getPlatform().openUri(url);
                }
                minecraft.setScreen(this);
            }, url, true));
        }
    }
}
