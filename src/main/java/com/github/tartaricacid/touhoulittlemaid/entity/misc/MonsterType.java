package com.github.tartaricacid.touhoulittlemaid.entity.misc;

import com.github.tartaricacid.touhoulittlemaid.util.version.TComponent;
import com.mojang.serialization.Codec;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum MonsterType implements StringRepresentable {
    FRIENDLY,
    NEUTRAL,
    HOSTILE;

    //todo check
    //不知写的对不对
    public static final Codec<MonsterType> CODEC = StringRepresentable.fromEnum(MonsterType::values, (s) -> {
        return MonsterType.valueOf(s.toUpperCase(Locale.ENGLISH));
    });

    private final MutableComponent component;

    MonsterType() {
        this.component = TComponent.translatable("gui.touhou_little_maid.monster_type." + this.name().toLowerCase(Locale.ENGLISH));
    }

    public MonsterType getPrevious() {
        int index = this.ordinal() - 1;
        if (index < 0) {
            index = values().length - 1;
        }
        return values()[index % values().length];
    }

    public MonsterType getNext() {
        int ordinal = this.ordinal();
        int length = MonsterType.values().length;
        return MonsterType.values()[(ordinal + 1) % length];
    }

    public MutableComponent getComponent() {
        return component;
    }

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase(Locale.ENGLISH);
    }
}