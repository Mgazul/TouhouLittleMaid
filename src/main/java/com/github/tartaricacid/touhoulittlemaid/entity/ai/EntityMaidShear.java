package com.github.tartaricacid.touhoulittlemaid.entity.ai;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidMode;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

import java.util.List;

public class EntityMaidShear extends EntityAIBase {
    private EntityMaid entityMaid;
    private float speed;
    private World world;
    private ItemStack mainhandItem;
    private Entity shearableEntity = null;
    private int timeCount;

    public EntityMaidShear(EntityMaid entityMaid, float speed) {
        this.entityMaid = entityMaid;
        this.speed = speed;
        timeCount = 10;
        this.world = entityMaid.world;
        this.mainhandItem = entityMaid.getHeldItemMainhand();
    }

    @Override
    public boolean shouldExecute() {
        if (entityMaid.getMode() != MaidMode.SHEARS || entityMaid.isSitting() ||
                !(entityMaid.getHeldItemMainhand().getItem() instanceof ItemShears)) {
            return false;
        }

        // 计数
        if (timeCount > 0) {
            timeCount--;
            return false;
        }

        timeCount = 10;

        // 开始判定 16 范围内的可剪生物
        List<Entity> entityList = this.world.getEntitiesInAABBexcluding(entityMaid, entityMaid.getEntityBoundingBox()
                .expand(8, 2, 8).expand(-8, -2, -8), EntityMaid.CAN_SHEAR);
        for (Entity entity : entityList) {
            if (entity instanceof IShearable && ((IShearable) entity).isShearable(mainhandItem, world, entity.getPosition())) {
                shearableEntity = entity;
                return true;
            }
        }
        return false;
    }

    @Override
    public void updateTask() {
        if (shearableEntity != null && shearableEntity.isEntityAlive() && shearableEntity instanceof IShearable
                && ((IShearable) shearableEntity).isShearable(mainhandItem, world, shearableEntity.getPosition())) {
            // 先尝试移动到这只羊身边
            entityMaid.getLookHelper().setLookPositionWithEntity(shearableEntity, 30f, entityMaid.getVerticalFaceSpeed());
            entityMaid.getNavigator().tryMoveToEntityLiving(shearableEntity, speed);

            // 如果距离太远，还是先跳过后面的剪羊毛过程吧
            if (entityMaid.getDistance(shearableEntity) > 3) {
                return;
            }

            List<ItemStack> list = ((IShearable) shearableEntity).onSheared(mainhandItem, world, shearableEntity.getPosition(),
                    EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, mainhandItem));

            // 手部动画
            entityMaid.swingArm(EnumHand.MAIN_HAND);

            if (!this.world.isRemote) {
                for (ItemStack stack : list) {
                    // 随机掉落的运动，来自原版剪羊毛
                    EntityItem entityitem = shearableEntity.entityDropItem(stack, 1.0F);
                    if (entityitem == null) {
                        continue;
                    }
                    entityitem.motionY += (double) (world.rand.nextFloat() * 0.05F);
                    entityitem.motionX += (double) ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.1F);
                    entityitem.motionZ += (double) ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.1F);
                }
            }

            // 别忘了扣除耐久，记得要在客户端服务端同步扣除
            entityMaid.getHeldItemMainhand().damageItem(1, entityMaid);
        }
    }

    @Override
    public void resetTask() {
        shearableEntity = null;
        entityMaid.getNavigator().clearPath();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return shearableEntity != null && shearableEntity.isEntityAlive() && shearableEntity instanceof IShearable
                && ((IShearable) shearableEntity).isShearable(mainhandItem, world, shearableEntity.getPosition());
    }
}
