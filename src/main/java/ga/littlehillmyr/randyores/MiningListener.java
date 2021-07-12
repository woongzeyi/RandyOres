package ga.littlehillmyr.randyores;

import lombok.val;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public final class MiningListener implements Listener {

    @NotNull private final Economy econ = RandyOres.getEcon();
    @NotNull private final ArrayList<Location> blockPlaced = new ArrayList<>();

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlace(final BlockPlaceEvent event) { blockPlaced.add(event.getBlock().getLocation()); }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(final BlockBreakEvent event) {

        // Settings
        val silkTouchIsAllowed = RandyOres.getPlugin().getConfig().getBoolean("Settings.allowSilkTouch");
        val silkTouchBlockList = RandyOres.getPlugin().getConfig().getList("Settings.allowedSilkTouchBlocks");
        val creativeModeIsAllowed = RandyOres.getPlugin().getConfig().getBoolean("Settings.allowCreativeMode");
        // Player info
        @NotNull val player = event.getPlayer();
        val playerIsUsingSilkTouch = player.getInventory().getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH);
        val playerIsInCreativeMode = player.getGameMode() == GameMode.CREATIVE;
        val playerHasPermission = player.hasPermission("randyores.blockmoney");
        // Block info
        @NotNull val block = event.getBlock();
        @NotNull val blockName = block.getBlockData().getMaterial().toString();
        val blockPrice = RandyOres.getPlugin().getConfig().getDouble("Blocks." + blockName);
        @NotNull val blockLocation = block.getLocation();
        val blockIsInSilkTouchBlockList = silkTouchBlockList != null && silkTouchBlockList.contains(blockName);

        if (blockPlaced.contains(blockLocation)) { try { blockPlaced.remove(blockLocation); } catch (Exception ignored) {  } return; }
        if (blockPrice == 0) return;
        if (!playerHasPermission) return;
        if (!creativeModeIsAllowed && playerIsInCreativeMode) return;
        if (!silkTouchIsAllowed && playerIsUsingSilkTouch) if (!blockIsInSilkTouchBlockList) return;

        econ.depositPlayer(player, blockPrice);

    }
}
