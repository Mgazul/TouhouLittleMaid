package com.github.tartaricacid.touhoulittlemaid.geckolib3.util;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.processor.IBone;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;

import java.util.List;

public final class RenderUtils {
    private static boolean renderingEntitiesInInventory = false;

    public static void setRenderingEntitiesInInventory(boolean value) {
        renderingEntitiesInInventory = value;
    }

    public static boolean isRenderingEntitiesInInventory() {
        return RenderSystem.isOnRenderThread() && renderingEntitiesInInventory;
    }

    public static void translateMatrixToBone(PoseStack poseStack, IBone bone) {
        poseStack.translate(-bone.getPositionX() / 16f, bone.getPositionY() / 16f, bone.getPositionZ() / 16f);
    }

    public static void rotateMatrixAroundBone(PoseStack poseStack, IBone bone) {
        if (bone.getRotationZ() != 0.0F) {
            poseStack.mulPose(Vector3f.ZP.rotation(bone.getRotationZ()));
        }
        if (bone.getRotationY() != 0.0F) {
            poseStack.mulPose(Vector3f.YP.rotation(bone.getRotationY()));
        }
        if (bone.getRotationX() != 0.0F) {
            poseStack.mulPose(Vector3f.XP.rotation(bone.getRotationX()));
        }
    }

    public static void scaleMatrixForBone(PoseStack poseStack, IBone bone) {
        poseStack.scale(bone.getScaleX(), bone.getScaleY(), bone.getScaleZ());
    }

    public static void translateToPivotPoint(PoseStack poseStack, IBone bone) {
        poseStack.translate(bone.getPivotX() / 16f, bone.getPivotY() / 16f, bone.getPivotZ() / 16f);
    }

    public static void translateAwayFromPivotPoint(PoseStack poseStack, IBone bone) {
        poseStack.translate(-bone.getPivotX() / 16f, -bone.getPivotY() / 16f, -bone.getPivotZ() / 16f);
    }

    public static void translateAndRotateMatrixForBone(PoseStack poseStack, IBone bone) {
        translateToPivotPoint(poseStack, bone);
        rotateMatrixAroundBone(poseStack, bone);
    }

    public static void prepMatrixForBone(PoseStack poseStack, IBone bone) {
        translateMatrixToBone(poseStack, bone);
        translateToPivotPoint(poseStack, bone);
        rotateMatrixAroundBone(poseStack, bone);
        scaleMatrixForBone(poseStack, bone);
        translateAwayFromPivotPoint(poseStack, bone);
    }

    public static Matrix4f invertAndMultiplyMatrices(Matrix4f baseMatrix, Matrix4f inputMatrix) {
        inputMatrix = new Matrix4f(inputMatrix);
        inputMatrix.invert();
        inputMatrix.multiply(baseMatrix);
        return inputMatrix;
    }

    public static void prepMatrixForLocator(PoseStack poseStack, List<? extends IBone> locatorHierarchy) {
        for (int i = 0; i < locatorHierarchy.size() - 1; i++) {
            RenderUtils.prepMatrixForBone(poseStack, locatorHierarchy.get(i));
        }
        IBone lastBone = locatorHierarchy.get(locatorHierarchy.size() - 1);
        RenderUtils.translateMatrixToBone(poseStack, lastBone);
        RenderUtils.translateToPivotPoint(poseStack, lastBone);
        RenderUtils.rotateMatrixAroundBone(poseStack, lastBone);
        RenderUtils.scaleMatrixForBone(poseStack, lastBone);
    }
}
