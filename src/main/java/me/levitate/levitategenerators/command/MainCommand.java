package me.levitate.levitategenerators.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import lombok.AllArgsConstructor;
import me.levitate.levitategenerators.config.Configuration;
import me.levitate.levitategenerators.generator.GeneratorItem;
import me.levitate.levitategenerators.generator.GeneratorType;
import me.levitate.levitategenerators.generator.GeneratorTypes;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandAlias("generator|gen")
@AllArgsConstructor
public class MainCommand extends BaseCommand {
    private Configuration configuration;

    @Default
    public void onHelp(CommandSender sender, CommandHelp commandHelp) {
        commandHelp.showHelp();
    }

    @Subcommand("give")
    @Syntax("<player> <tier> <amount>")
    @CommandCompletion("@players")
    @CommandPermission("generator.give")
    public void onGive(CommandSender sender, OnlinePlayer onlinePlayer, Integer type, Integer amount) {
        final Player player = onlinePlayer.player;

        GeneratorType generatorType = GeneratorTypes.getGeneratorTypeByTier(type);

        if (generatorType != null) {
            GeneratorItem generatorItem = new GeneratorItem(generatorType);
            ItemStack generatorItemStack = generatorItem.getItemStack(amount);

            if (generatorItemStack != null) {
                player.getInventory().addItem(generatorItem.getItemStack(amount));
            }
        }
    }

    @Subcommand("reload")
    @CommandPermission("generator.reload")
    public void onReload(CommandSender sender) {
        configuration.reloadConfig();
    }
}
