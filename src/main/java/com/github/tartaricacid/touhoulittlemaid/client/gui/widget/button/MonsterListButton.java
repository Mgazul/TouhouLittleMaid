package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.task.AttackTaskConfigGui;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class MonsterListButton extends Button {
    private static final ResourceLocation ICON = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/attack_task_config.png");
    private final AttackTaskConfigGui parents;
    private final ResourceLocation entityId;

    public MonsterListButton(Component entityName, int x, int y, ResourceLocation entityId, AttackTaskConfigGui parents) {
        super(x, y, 164, 13, entityName, b -> {});
        this.parents = parents;
        this.entityId = entityId;
    }

    @Override
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float pPartialTick) {
        Minecraft mc = Minecraft.getInstance();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, ICON);
        RenderSystem.enableDepthTest();
        if (deleteClick(mouseX, mouseY)) {
            blit(poseStack, this.x, this.y, 0, 163, this.width, this.height, 256, 256);
        } else if (leftClick(mouseX, mouseY) || rightClick(mouseX, mouseY)) {
            blit(poseStack, this.x, this.y, 0, 150, this.width, this.height, 256, 256);
        } else {
            blit(poseStack, this.x, this.y, 0, 137, this.width, this.height, 256, 256);
        }
        mc.font.draw(poseStack, this.getMessage(), this.x + 5, this.y + 3, 0x444444);
        drawCenteredString(poseStack, mc.font, this.parents.getAttackGroups().get(entityId).getComponent(), this.x + 142, this.y + 3, 0xFFFFFF);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (deleteClick(mouseX, mouseY)) {
            this.parents.removeMonsterType(this.entityId);
        } else if (leftClick(mouseX, mouseY)) {
            this.parents.getAttackGroups().computeIfPresent(this.entityId, (k, monsterType) -> monsterType.getPrevious());
        } else if (rightClick(mouseX, mouseY)) {
            this.parents.getAttackGroups().computeIfPresent(this.entityId, (k, monsterType) -> monsterType.getNext());
        }
    }

    private boolean deleteClick(double mouseX, double mouseY) {
        boolean clickY = this.y <= mouseY && mouseY <= (this.y + this.getHeight());
        boolean deleteClickX = (this.x + 107) <= mouseX && mouseX <= (this.x + 120);
        return clickY && deleteClickX;
    }

    private boolean leftClick(double mouseX, double mouseY) {
        boolean clickY = this.y <= mouseY && mouseY <= (this.y + this.getHeight());
        boolean leftClickX = (this.x + 120) <= mouseX && mouseX <= (this.x + 130);
        return clickY && leftClickX;
    }

    private boolean rightClick(double mouseX, double mouseY) {
        boolean clickY = this.y <= mouseY && mouseY <= (this.y + this.getHeight());
        boolean rightClickX = (this.x + 154) <= mouseX && mouseX <= (this.x + 164);
        return clickY && rightClickX;
    }
}
