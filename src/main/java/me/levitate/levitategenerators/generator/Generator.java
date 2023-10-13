package me.levitate.levitategenerators.generator;

import lombok.Getter;
import me.levitate.levitategenerators.LevitateGenerators;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@Getter
public class Generator {
    private final UUID ownerUUID;
    private final Location location;
    private final Location dropLocation;

    private Material generatorBlock;
    private Material dropItem;
    private int dropAmount;
    private int dropInterval;
    private long lastDropTime;
    private int tier;

    private GeneratorType generatorType;

    public Generator(UUID ownerUUID, Location location, GeneratorType generatorType) {
        this.ownerUUID = ownerUUID;
        this.location = location;
        this.dropLocation = location.clone().add(0, 1, 0);

        this.generatorType = generatorType;
        this.generatorBlock = generatorType.getGeneratorBlock();
        this.dropItem = generatorType.getDropItem();
        this.dropAmount = generatorType.getDropAmount();
        this.dropInterval = generatorType.getDropInterval();
        this.tier = generatorType.getTier();

        this.lastDropTime = System.currentTimeMillis();
    }

    public void dropItem() {
        World world = this.location.getWorld();
        ItemStack dropItemStack = new ItemStack(this.dropItem, 1);

        world.dropItemNaturally(dropLocation, dropItemStack);
    }

    public void setLastDropTime() {
        this.lastDropTime = System.currentTimeMillis();
    }

    public int getDropInterval() {
        return dropInterval * 1000;
    }

    public void upgrade() {
        this.generatorType = GeneratorTypes.getGeneratorTypeByTier(this.tier + 1);

        this.generatorBlock = generatorType.getGeneratorBlock();
        this.dropItem = generatorType.getDropItem();
        this.dropAmount = generatorType.getDropAmount();
        this.dropInterval = generatorType.getDropInterval();
        this.tier = generatorType.getTier();
    }
}
