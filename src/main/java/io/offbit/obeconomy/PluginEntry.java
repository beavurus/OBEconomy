package io.offbit.obeconomy;

import net.milkbowl.vault.economy.Economy;
import net.minecraft.server.v1_16_R3.Block;
import org.bukkit.Material;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Iterator;

public class PluginEntry extends JavaPlugin
{

    private Economy economy;
    private ServicesManager serviceManager;

    public Economy getEconomy()
    {
        return economy;
    }

    @Override
    public void onEnable()
    {
        this.serviceManager = getServer().getServicesManager();
        Plugin vault = getServer().getPluginManager().getPlugin("Vault");
        if (vault != null)
        {
//            this.economy = new CustomEconomy();
            try {
                Class<CustomEconomy> economyClass = CustomEconomy.class;
                this.economy = economyClass.getConstructor(Plugin.class).newInstance(this);
                serviceManager.register(Economy.class, economy, this, ServicePriority.Normal);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            getServer().getServicesManager().register(Economy.class, this.economy, this, ServicePriority.Normal);
//            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "VaultAPI hooked.");
        } else
        {
            getLogger().severe("Vault was not found. Disabled this plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }


        CurrencyItem.init(this);

        this.getCommand("getcurrency").setExecutor(new GetItem());
        this.getCommand("balance").setExecutor(new BalanceCommand());

        getServer().getLogger().info("Loaded OBEconomy");

        getServer().getPluginManager().registerEvents(new DisabledCraftingEvent(), this);
        getServer().getPluginManager().registerEvents(new CurrencyItem(), this);
    }

    @Override
    public void onDisable()
    {
        getLogger().info("Plugin Disabled");
    }
}
