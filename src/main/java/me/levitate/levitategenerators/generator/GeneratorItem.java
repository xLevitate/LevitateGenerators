package me.levitate.levitategenerators.generator;

import me.levitate.levitategenerators.LevitateGenerators;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class GeneratorItem {
    private final GeneratorType generatorType;
    private ItemStack itemStack;

    public GeneratorItem(GeneratorType generatorType) {
        this.generatorType = generatorType;
    }

    public ItemStack getItemStack(int amount) {
        if (itemStack == null) {
            itemStack = new ItemStack(generatorType.getGeneratorBlock(), amount);

            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta == null) return null;

            PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
            dataContainer.set(LevitateGenerators.GENERATOR_TIER_KEY, PersistentDataType.INTEGER, generatorType.getTier());

            List<Component> translatedLore = generatorType.getLore().stream()
                    .map(line -> MiniMessage.miniMessage().deserialize(line).decoration(TextDecoration.ITALIC, false))
                    .toList();

            itemMeta.displayName(MiniMessage.miniMessage().deserialize(generatorType.getName()).decoration(TextDecoration.ITALIC, false));
            itemMeta.lore(translatedLore);

            itemStack.setItemMeta(itemMeta);
        }

        return itemStack;
    }
}
