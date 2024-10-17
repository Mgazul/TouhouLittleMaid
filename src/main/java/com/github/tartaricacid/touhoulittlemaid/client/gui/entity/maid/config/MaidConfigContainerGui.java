package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.config;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.MaidConfigButton;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidConfigManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.PickType;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.config.MaidConfigContainer;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.MaidSubConfigMessage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.anti_ad.mc.ipn.api.IPNButton;
import org.anti_ad.mc.ipn.api.IPNGuiHint;
import org.anti_ad.mc.ipn.api.IPNPlayerSideOnly;

@IPNPlayerSideOnly
@IPNGuiHint(button = IPNButton.SORT, horizontalOffset = -36, bottom = -12)
@IPNGuiHint(button = IPNButton.SORT_COLUMNS, horizontalOffset = -24, bottom = -24)
@IPNGuiHint(button = IPNButton.SORT_ROWS, horizontalOffset = -12, bottom = -36)
@IPNGuiHint(button = IPNButton.SHOW_EDITOR, horizontalOffset = -5)
@IPNGuiHint(button = IPNButton.SETTINGS, horizontalOffset = -5)
public class MaidConfigContainerGui extends AbstractMaidContainerGui<MaidConfigContainer> {
    private static final ResourceLocation ICON = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_gui_config.png");
    private final MaidConfigManager.SyncNetwork syncNetwork;

    public MaidConfigContainerGui(MaidConfigContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.syncNetwork = getMaid().getConfigManager().getSyncNetwork();
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTicks, int x, int y) {
        super.renderBg(poseStack, partialTicks, x, y);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, ICON);
        RenderSystem.enableDepthTest();
        blit(poseStack, leftPos + 80, topPos + 28, 0, 0, imageWidth, imageHeight);
    }

    @Override
    protected void initAdditionWidgets() {
        int buttonLeft = leftPos + 86;
        int buttonTop = topPos + 52;

        this.addRenderableWidget(new MaidConfigButton(buttonLeft, buttonTop,
                TComponent.translatable("gui.touhou_little_maid.maid_config.show_backpack"),
                TComponent.translatable("gui.touhou_little_maid.maid_config.value." + this.syncNetwork.showBackpack()),
                button -> {
                    this.syncNetwork.setShowBackpack(!this.syncNetwork.showBackpack());
                    button.setValue(TComponent.translatable("gui.touhou_little_maid.maid_config.value." + this.syncNetwork.showBackpack()));
                }
        ));
        buttonTop += 13;

        this.addRenderableWidget(new MaidConfigButton(buttonLeft, buttonTop,
                TComponent.translatable("gui.touhou_little_maid.maid_config.show_back_item"),
                TComponent.translatable("gui.touhou_little_maid.maid_config.value." + this.syncNetwork.showBackItem()),
                button -> {
                    this.syncNetwork.setShowBackItem(!this.syncNetwork.showBackItem());
                    button.setValue(TComponent.translatable("gui.touhou_little_maid.maid_config.value." + this.syncNetwork.showBackItem()));
                }
        ));
        buttonTop += 13;

        this.addRenderableWidget(new MaidConfigButton(buttonLeft, buttonTop,
                TComponent.translatable("gui.touhou_little_maid.maid_config.show_chat_bubble"),
                TComponent.translatable("gui.touhou_little_maid.maid_config.value." + this.syncNetwork.showChatBubble()),
                button -> {
                    this.syncNetwork.setShowChatBubble(!this.syncNetwork.showChatBubble());
                    button.setValue(TComponent.translatable("gui.touhou_little_maid.maid_config.value." + this.syncNetwork.showChatBubble()));
                }
        ));
        buttonTop += 13;


        this.addRenderableWidget(new MaidConfigButton(buttonLeft, buttonTop,
                TComponent.translatable("gui.touhou_little_maid.maid_config.sound_frequency"),
                TComponent.literal(Math.round(this.syncNetwork.soundFreq() * 100) + "%").withStyle(ChatFormatting.YELLOW),
                button -> {
                    this.syncNetwork.setSoundFreq(this.syncNetwork.soundFreq() - 0.1f);
                    button.setValue(TComponent.literal(Math.round(this.syncNetwork.soundFreq() * 100) + "%").withStyle(ChatFormatting.YELLOW));
                },
                button -> {
                    this.syncNetwork.setSoundFreq(this.syncNetwork.soundFreq() + 0.1f);
                    button.setValue(TComponent.literal(Math.round(this.syncNetwork.soundFreq() * 100) + "%").withStyle(ChatFormatting.YELLOW));
                }
        ));
        buttonTop += 13;

        this.addRenderableWidget(new MaidConfigButton(buttonLeft, buttonTop,
                TComponent.translatable("gui.touhou_little_maid.maid_config.pick_type"),
                TComponent.translatable(PickType.getTransKey(this.syncNetwork.pickType())).withStyle(ChatFormatting.DARK_RED),
                button -> {
                    this.syncNetwork.setPickType(PickType.getPreviousPickType(this.syncNetwork.pickType()));
                    button.setValue(TComponent.translatable(PickType.getTransKey(this.syncNetwork.pickType())).withStyle(ChatFormatting.DARK_RED));
                },
                button -> {
                    this.syncNetwork.setPickType(PickType.getNextPickType(this.syncNetwork.pickType()));
                    button.setValue(TComponent.translatable(PickType.getTransKey(this.syncNetwork.pickType())).withStyle(ChatFormatting.DARK_RED));
                }
        ));
        buttonTop += 13;

        this.addRenderableWidget(new MaidConfigButton(buttonLeft, buttonTop,
                TComponent.translatable("gui.touhou_little_maid.maid_config.open_door"),
                TComponent.translatable("gui.touhou_little_maid.maid_config.value." + this.syncNetwork.openDoor()),
                button -> {
                    this.syncNetwork.setOpenDoor(!this.syncNetwork.openDoor());
                    button.setValue(TComponent.translatable("gui.touhou_little_maid.maid_config.value." + this.syncNetwork.openDoor()));
                }
        ));
        buttonTop += 13;

        this.addRenderableWidget(new MaidConfigButton(buttonLeft, buttonTop,
                TComponent.translatable("gui.touhou_little_maid.maid_config.open_fence_gate"),
                TComponent.translatable("gui.touhou_little_maid.maid_config.value." + this.syncNetwork.openFenceGate()),
                button -> {
                    this.syncNetwork.setOpenFenceGate(!this.syncNetwork.openFenceGate());
                    button.setValue(TComponent.translatable("gui.touhou_little_maid.maid_config.value." + this.syncNetwork.openFenceGate()));
                }
        ));
    }

    @Override
    protected void renderAddition(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        drawString(poseStack, font, TComponent.translatable("gui.touhou_little_maid.button.maid_config"), leftPos + 140, topPos + 41, 0xFFFFFF);
    }

    @Override
    public void onClose() {
        if (this.maid != null) {
            NetworkHandler.CHANNEL.sendToServer(new MaidSubConfigMessage(this.maid.getId(), this.syncNetwork));
        }
        super.onClose();
    }
}