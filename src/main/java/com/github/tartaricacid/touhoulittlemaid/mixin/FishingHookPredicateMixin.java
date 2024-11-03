package com.github.tartaricacid.touhoulittlemaid.mixin;

import com.github.tartaricacid.touhoulittlemaid.entity.projectile.MaidFishingHook;
import net.minecraft.advancements.critereon.FishingHookPredicate;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FishingHookPredicate.class)
public class FishingHookPredicateMixin {
    @Shadow
    @Final
    private boolean inOpenWater;

    @Inject(method = "matches", at = @At("RETURN"), cancellable = true)
    private void matches$tlm(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        FishingHookPredicate predicate = (FishingHookPredicate) (Object) this;
        if (predicate != FishingHookPredicate.ANY && entity instanceof MaidFishingHook fishingHook) {
            cir.setReturnValue(this.inOpenWater == fishingHook.isOpenWaterFishing());
        }
    }
}
