package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.util.version.TComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

import static com.github.tartaricacid.touhoulittlemaid.item.MaidGroup.MAIN_TAB;

@Deprecated(since = "1.1.13")
public class ItemMonsterList extends Item {
    public ItemMonsterList() {
        super(new Item.Properties().tab(MAIN_TAB).stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(TComponent.translatable("tooltips.touhou_little_maid.monster_list.desc").withStyle(ChatFormatting.DARK_RED));
    }
}