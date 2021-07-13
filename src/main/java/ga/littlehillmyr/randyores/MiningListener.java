package ga.littlehillmyr.randyores;

import lombok.val;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class MiningListener implements Listener {

    @NotNull private final FileConfiguration config = Objects.requireNonNull(RandyOres.getPlugin()).getConfig();
    @NotNull private final Economy econ = Objects.requireNonNull(RandyOres.getEcon());
    @NotNull private final ArrayList<@NotNull Location> blockPlaced = new ArrayList<>();

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlace(final BlockPlaceEvent event) { blockPlaced.add(event.getBlock().getLocation()); }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(final BlockBreakEvent event) {

        // Settings
        val silkTouchIsAllowed = config.getBoolean("Settings.allowSilkTouch");
        @NotNull val silkTouchBlockList = Objects.requireNonNullElse(config.getList("Settings.allowedSilkTouchBlocks"), List.of());
        val creativeModeIsAllowed = config.getBoolean("Settings.allowCreativeMode");
        // Player info
        @NotNull val player = event.getPlayer();
        val playerIsUsingSilkTouch = player.getInventory().getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH);
        val playerIsInCreativeMode = player.getGameMode() == GameMode.CREATIVE;
        val playerHasPermission = player.hasPermission("randyores.blockmoney");
        // Block info
        @NotNull val block = event.getBlock();
        @NotNull val blockName = block.getBlockData().getMaterial().toString();
        val blockPrice = config.getDouble("Blocks." + blockName);
        @NotNull val blockLocation = block.getLocation();
        val blockIsInSilkTouchBlockList = silkTouchBlockList.contains(blockName);

        if (blockPlaced.contains(blockLocation)) { blockPlaced.remove(blockLocation); return; }
        if (blockPrice == 0) return;
        if (!playerHasPermission) return;
        if (!creativeModeIsAllowed && playerIsInCreativeMode) return;
        if (!silkTouchIsAllowed && playerIsUsingSilkTouch && !blockIsInSilkTouchBlockList) return;

        econ.depositPlayer(player, blockPrice);

    }
}
