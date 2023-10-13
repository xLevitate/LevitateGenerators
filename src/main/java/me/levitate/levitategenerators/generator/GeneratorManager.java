package me.levitate.levitategenerators.generator;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

import java.util.*;

@Getter
public class GeneratorManager {
    private final Plugin plugin;

    // Amount of generators a player has
    private final Map<UUID, Integer> playerGeneratorAmount = new HashMap<>();

    // Most likely have to change it to UUID, Generator
    private final Map<Location, Generator> generators = new HashMap<>();

    public GeneratorManager(Plugin plugin) {
        this.plugin = plugin;

        // TODO: Load all the existing generators from a database.

        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Generator generator : generators.values()) {
                if (System.currentTimeMillis() - generator.getLastDropTime() >= generator.getDropInterval()) {
                    generator.dropItem();
                    generator.setLastDropTime();
                }
            }
        }, 0L, 20L);
    }

    public void addGenerator(Generator generator) {

        generators.put(generator.getLocation(), generator);
    }

    public void removeGenerator(Location generatorLocation) {
        generators.remove(generatorLocation);
    }

    public void upgradeGenerator(Location generatorLocation) {
        Generator generator = generators.get(generatorLocation);

        // Upgrade the generator
        generator.upgrade();

        // Update the block
        Block block = generatorLocation.getBlock();
        block.setType(generator.getGeneratorBlock());
    }

    public boolean isGenerator(Location location) {
        return generators.containsKey(location);
    }
}
