package net.kingdomscrusade.randyores;

import de.tr7zw.nbtapi.NBTTileEntity;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("SpellCheckingInspection")
public final class BlockBreakListener implements Listener {

    private final Economy econ = RandyOres.getEcon();
    private final Map<Material, Double> materialList = new HashMap<>() {
        {
            FileConfiguration config = RandyOres.getPlugin().getConfig();
            put(Material.NETHERRACK,        config.getDouble("BlockMoney.NETHERRACK"));
            put(Material.STONE,             config.getDouble("BlockMoney.STONE"));
            put(Material.COAL_ORE,          config.getDouble("BlockMoney.COAL_ORE"));
            put(Material.IRON_ORE,          config.getDouble("BlockMoney.IRON_ORE"));
            put(Material.REDSTONE_ORE,      config.getDouble("BlockMoney.REDSTONE_ORE"));
            put(Material.NETHER_QUARTZ_ORE, config.getDouble("BlockMoney.NETHER_QUARTZ_ORE"));
            put(Material.NETHER_GOLD_ORE,   config.getDouble("BlockMoney.NETHER_GOLD_ORE"));
            put(Material.LAPIS_ORE,         config.getDouble("BlockMoney.LAPIS_ORE"));
            put(Material.GOLD_ORE,          config.getDouble("BlockMoney.GOLD_ORE"));
            put(Material.DIAMOND_ORE,       config.getDouble("BlockMoney.DIAMOND_ORE"));
            put(Material.ANCIENT_DEBRIS,    config.getDouble("BlockMoney.ANCIENT_DEBRIS"));
            put(Material.EMERALD_ORE,       config.getDouble("BlockMoney.EMERALD_ORE"));
        }
    };

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {

        // Declaring variables
        String nbtKey = "RandyOres.moneyHasGiven";
        Material eventBlockType = event.getBlock().getType();
        NBTTileEntity eventBlockNBT = new NBTTileEntity(event.getBlock().getState());

        // State checking
        if (!materialList.containsKey(eventBlockType)) return;
        if (eventBlockNBT.hasKey(nbtKey) && eventBlockNBT.getBoolean(nbtKey)) return;

        // Execution
        econ.depositPlayer(event.getPlayer(), materialList.get(eventBlockType));
        eventBlockNBT.setBoolean(nbtKey, true);

    }
}
