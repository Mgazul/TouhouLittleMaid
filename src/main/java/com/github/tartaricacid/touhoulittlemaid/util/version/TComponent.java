package com.github.tartaricacid.touhoulittlemaid.util.version;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public final class TComponent {

    public static final Component EMPTY = TextComponent.EMPTY;
    private TComponent() {
    }

    public static Component empty() {
        return EMPTY;
    }

    public static Component translatable(String key) {
        return new TranslatableComponent(key);
    }


    public static Component literal(String string) {
        return new TextComponent(string);
    }
}
