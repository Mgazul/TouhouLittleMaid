package com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityGarageKit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

/**
 * @author TartaricAcid
 * @date 2019/7/7 13:13
 **/
@SideOnly(Side.CLIENT)
public class TileEntityGarageKitRenderer extends TileEntitySpecialRenderer<TileEntityGarageKit> {
    @Override
    public void render(TileEntityGarageKit te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        final Entity entity = EntityList.createEntityByIDFromName(new ResourceLocation(te.getEntityId()), getWorld());
        final EnumFacing facing = te.getFacing();

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y + 0.0625, z);
        GlStateManager.scale(0.5, 0.5, 0.5);
        GlStateManager.translate(1, 0, 1);

        if (Minecraft.isAmbientOcclusionEnabled()) {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        } else {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }

        switch (facing) {
            case NORTH:
            default:
                GlStateManager.rotate(180, 0, 1, 0);
                break;
            case SOUTH:
                break;
            case EAST:
                GlStateManager.rotate(90, 0, 1, 0);
                break;
            case WEST:
                GlStateManager.rotate(270, 0, 1, 0);
                break;
        }

        if (entity instanceof EntityMaid) {
            ((EntityMaid) entity).setModelLocation(te.getModel());
            ((EntityMaid) entity).setTextureLocation(te.getTexture());
            ((EntityMaid) entity).setModelName(te.getName());
        }

        Minecraft.getMinecraft().getRenderManager().setRenderShadow(false);
        Minecraft.getMinecraft().getRenderManager().renderEntity(entity == null ? new EntityMaid(getWorld()) : entity,
                0, 0, 0, 0, 0, true);
        Minecraft.getMinecraft().getRenderManager().setRenderShadow(true);
        GlStateManager.popMatrix();
    }
}
