package me.levitate.levitategenerators.config;

import lombok.Getter;
import me.levitate.levitategenerators.generator.GeneratorType;
import me.levitate.levitategenerators.generator.GeneratorTypes;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Configuration {
    private final Plugin plugin;
    private FileConfiguration fileConfiguration;

    @Getter
    private final Map<String, String> messages = new LinkedHashMap<>();

    @Getter
    private final Map<String, GeneratorType> generatorTypes = new LinkedHashMap<>();

    // Settings
    public boolean breakOther = false;

    public Configuration(Plugin plugin, FileConfiguration fileConfiguration) {
        this.plugin = plugin;
        this.fileConfiguration = fileConfiguration;

        reloadConfig();
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        plugin.saveDefaultConfig();

        this.fileConfiguration = plugin.getConfig();
        fileConfiguration.options().copyDefaults(true);
        plugin.saveConfig();

        loadMessages();
        loadSettings();
        loadGeneratorTypesFromConfig();
    }

    private void loadMessages() {
        messages.clear();

        for (String key : fileConfiguration.getConfigurationSection("messages").getKeys(false)) {
            messages.put(key, fileConfiguration.getString("messages." + key));
        }
    }

    private void loadSettings() {
        breakOther = fileConfiguration.getBoolean("settings.break-other");
        System.out.println("break other is set to: " + breakOther);
    }

    private void loadGeneratorTypesFromConfig() {
        ConfigurationSection section = fileConfiguration.getConfigurationSection("generatorTypes");
        if (section == null) return;

        GeneratorTypes.getGeneratorTypes().clear();

        for (String typeName : section.getKeys(false)) {
            int tier = Integer.parseInt(typeName);
            GeneratorType type = parseGeneratorTypeFromConfig(section.getConfigurationSection(typeName));
            GeneratorTypes.addGeneratorType(tier, type);
        }
    }

    private GeneratorType parseGeneratorTypeFromConfig(ConfigurationSection section) {
        String generatorBlock = section.getString("generatorBlock");
        String dropItem = section.getString("dropItem");
        int dropInterval = section.getInt("dropInterval");
        int dropAmount = section.getInt("dropAmount");
        int tier = section.getInt("tier");
        int upgradeCost = section.getInt("upgradeCost");
        String name = section.getString("name");
        List<String> lore = section.getStringList("lore");

        Material generatorMaterial = Material.matchMaterial(generatorBlock);
        Material dropMaterial = Material.matchMaterial(dropItem);

        return (generatorMaterial != null && dropMaterial != null) ?
                new GeneratorType(generatorMaterial, dropMaterial, dropInterval, dropAmount, tier, name, lore, upgradeCost) : null;
    }
}
