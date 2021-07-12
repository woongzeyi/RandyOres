package ga.littlehillmyr.randyores;

import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Logger;

public final class RandyOres extends JavaPlugin {

    @Getter @Nullable
    private static Plugin plugin;
    @Getter @Nullable
    private static Economy econ;
    @NotNull
    private static final Logger log = Logger.getLogger("Minecraft");

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig(); // Setting up configs
        if (!setupEconomy()) { // Setting up economy instance
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getServer().getPluginManager().registerEvents(new MiningListener(), this); // Setting up listener
    }

    @Override
    public void onDisable() {
        log.info(String.format("[%s] Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return true;
    }
}

