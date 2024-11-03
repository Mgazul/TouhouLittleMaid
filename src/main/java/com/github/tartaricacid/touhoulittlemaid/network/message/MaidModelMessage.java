package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.advancements.maid.TriggerType;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.Util;
import com.github.tartaricacid.touhoulittlemaid.init.InitTrigger;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MaidModelMessage {
    private final int id;
    private final ResourceLocation modelId;

    public MaidModelMessage(int id, ResourceLocation modelId) {
        this.id = id;
        this.modelId = modelId;
    }

    public static void encode(MaidModelMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.id);
        buf.writeResourceLocation(message.modelId);
    }

    public static MaidModelMessage decode(FriendlyByteBuf buf) {
        return new MaidModelMessage(buf.readInt(), buf.readResourceLocation());
    }

    public static void handle(MaidModelMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayer sender = context.getSender();
                if (sender == null) {
                    return;
                }
                Entity entity = sender.level.getEntity(message.id);
                if (entity instanceof EntityMaid && ((EntityMaid) entity).isOwnedBy(sender)) {
                    if (sender.isCreative() || MaidConfig.MAID_CHANGE_MODEL.get()) {
                        ((EntityMaid) entity).setModelId(message.modelId.toString());
                        InitTrigger.MAID_EVENT.trigger(sender, TriggerType.CHANGE_MAID_MODEL);
                    } else {
                        sender.sendMessage(new TranslatableComponent("message.touhou_little_maid.change_model.disabled"),
                                ChatType.GAME_INFO, Util.NIL_UUID);
                    }
                }
            });
        }
        context.setPacketHandled(true);
    }
}
