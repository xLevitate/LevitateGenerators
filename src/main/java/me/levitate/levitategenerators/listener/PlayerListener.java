package me.levitate.levitategenerators.listener;

import lombok.AllArgsConstructor;
import me.levitate.levitategenerators.LevitateGenerators;
import me.levitate.levitategenerators.config.Configuration;
import me.levitate.levitategenerators.generator.*;
import me.levitate.levitategenerators.utils.VaultImpl;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

@AllArgsConstructor
public class PlayerListener implements Listener {
    private GeneratorManager generatorManager;
    private Configuration configuration;
    private VaultImpl vault;

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlockPlaced();
        ItemStack itemPlaced = event.getItemInHand();

        ItemMeta itemMeta = itemPlaced.getItemMeta();
        if (itemMeta == null) return;

        PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
        Integer generatorTier = dataContainer.get(LevitateGenerators.GENERATOR_TIER_KEY, PersistentDataType.INTEGER);

        if (generatorTier != null) {
            UUID playerUUID = event.getPlayer().getUniqueId();
            Location blockLocation = block.getLocation();

            // TODO: Add an effect when the player places down a generator.

            Generator generator = new Generator(
                    playerUUID,
                    blockLocation,
                    GeneratorTypes.getGeneratorTypeByTier(generatorTier)
            );

            generatorManager.addGenerator(generator);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        Block block = event.getBlock();
        Location location = block.getLocation();

        if (generatorManager.isGenerator(location)) {
            Generator generator = generatorManager.getGenerators().get(location);

            if (generator.getOwnerUUID() != player.getUniqueId() && !configuration.breakOther) {
                player.sendMessage(MiniMessage.miniMessage().deserialize(configuration.getMessages().get("break-other")));
                return;
            }

            GeneratorType generatorType = generator.getGeneratorType();
            GeneratorItem generatorItem = new GeneratorItem(generatorType);

            player.getInventory().addItem(generatorItem.getItemStack(1));

            generatorManager.getGenerators().remove(location);
            event.setDropItems(false);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getHand() == EquipmentSlot.HAND) return;

        Player player = event.getPlayer();
        if (!player.isSneaking()) return;

        double playerBalance = vault.getEcon().getBalance(player);

        Block block = event.getClickedBlock();
        if (block == null) return;

        Location location = block.getLocation();

        if (generatorManager.isGenerator(location)) {
            Generator generator = generatorManager.getGenerators().get(location);

            if (generator.getOwnerUUID() != player.getUniqueId()) {
                player.sendMessage(MiniMessage.miniMessage().deserialize(configuration.getMessages().get("upgrade-other")));
                return;
            }

            if (generator.getTier() < GeneratorTypes.getMaxTier()) {
                int nextTierPrice = GeneratorTypes.getGeneratorTypeByTier(generator.getTier() + 1).getUpgradeCost();

                if (playerBalance >= nextTierPrice) {
                    vault.getEcon().withdrawPlayer(player, nextTierPrice);

                    generatorManager.upgradeGenerator(block.getLocation());
                    player.sendMessage(MiniMessage.miniMessage().deserialize(configuration.getMessages().get("upgrade")));
                }
                else {
                    player.sendMessage(MiniMessage.miniMessage().deserialize(configuration.getMessages().get("no-balance")));
                }
            }
            else {
                player.sendMessage(MiniMessage.miniMessage().deserialize(configuration.getMessages().get("max-tier")));
            }
        }
    }
}
