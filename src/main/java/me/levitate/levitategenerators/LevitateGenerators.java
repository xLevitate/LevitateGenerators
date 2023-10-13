package me.levitate.levitategenerators;

import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import me.levitate.levitategenerators.command.MainCommand;
import me.levitate.levitategenerators.config.Configuration;
import me.levitate.levitategenerators.generator.GeneratorManager;
import me.levitate.levitategenerators.listener.PlayerListener;
import me.levitate.levitategenerators.utils.VaultImpl;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

// TODO: Add generator limit per player and add permission-based system for more generators.
// TODO: Add placeholders to the plugin
// TODO: Add an API
// TODO: Add a sell system

@Getter
public final class LevitateGenerators extends JavaPlugin {
    private Plugin plugin;

    public static GeneratorManager generatorManager;
    private Configuration configuration;
    private VaultImpl vault;

    public static NamespacedKey GENERATOR_TIER_KEY;

    @Override
    public void onEnable() {
        plugin = this;
        GENERATOR_TIER_KEY = new NamespacedKey(this, "generator_tier");

        getConfig().options().copyDefaults(true);
        saveConfig();

        final PaperCommandManager commandManager = new PaperCommandManager(this);

        configuration = new Configuration(this, getConfig());
        vault = new VaultImpl(this);
        generatorManager = new GeneratorManager(this);

        getServer().getPluginManager().registerEvents(new PlayerListener(generatorManager, configuration, vault), this);

        commandManager.enableUnstableAPI("help");
        commandManager.registerCommand(new MainCommand(configuration));
    }

    @Override
    public void onDisable() {
        // TODO: Save the data from the generators
    }
}
