package me.levitate.levitategenerators.generator;

import lombok.Getter;
import org.bukkit.Material;

import java.util.List;

@Getter
public class GeneratorType {
    private final Material generatorBlock;
    private final Material dropItem;
    private final int dropInterval;
    private final int dropAmount;
    private final int tier;
    private final int upgradeCost;
    private final String name;
    private final List<String> lore;

    public GeneratorType(Material generatorBlock, Material dropItem, int dropInterval, int dropAmount, int tier, String name, List<String> lore, int upgradeCost) {
        this.generatorBlock = generatorBlock;
        this.dropItem = dropItem;
        this.dropInterval = dropInterval;
        this.dropAmount = dropAmount;
        this.tier = tier;
        this.upgradeCost = upgradeCost;
        this.name = name;
        this.lore = lore;
    }
}
