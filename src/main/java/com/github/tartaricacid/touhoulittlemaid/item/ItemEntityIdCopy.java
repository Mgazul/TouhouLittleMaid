package com.github.tartaricacid.touhoulittlemaid.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class ItemEntityIdCopy extends Item {
    public ItemEntityIdCopy() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> components, TooltipFlag pIsAdvanced) {
        components.add(Component.translatable("tooltips.touhou_little_maid.entity_id_copy.desc").withStyle(ChatFormatting.GRAY));
    }
}