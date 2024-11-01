package com.github.tartaricacid.touhoulittlemaid.datagen;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import net.minecraft.data.DataGenerator;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.common.data.LanguageProvider;
import org.apache.commons.compress.utils.Lists;

import java.util.List;


public class LanguageGenerator extends LanguageProvider {
    private static final List<MutableComponent> DATA = Lists.newArrayList();

    public LanguageGenerator(DataGenerator gen) {
        super(gen, TouhouLittleMaid.MOD_ID, "en_us");
    }

    public static void addLanguage(MutableComponent component) {
        DATA.add(component);
    }

    @Override
    protected void addTranslations() {
        DATA.forEach(component -> {
            if (component instanceof TranslatableComponent translatableComponent) {
                String key = translatableComponent.getKey();
                add(key, "");
            }
        });
    }
}
