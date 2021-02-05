package io.offbit.obeconomy;

import io.offbit.obeconomy.commands.BalanceCommand;
import io.offbit.obeconomy.commands.EconomyCommand;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

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
            try
            {
                Class<CustomEconomy> economyClass = CustomEconomy.class;
                this.economy = economyClass.getConstructor(Plugin.class).newInstance(this);
                serviceManager.register(Economy.class, economy, this, ServicePriority.Normal);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        } else
        {
            getLogger().severe("Vault was not found. Disabled this plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        CurrencyItem.init(this);

        this.getCommand("balance").setExecutor(new BalanceCommand(this.economy));
        this.getCommand("brik").setExecutor(new EconomyCommand(this.economy));

        this.getCommand("brik").setTabCompleter(new EconomyCommand(this.economy));

        getServer().getLogger().info("Loaded Briks");

        getServer().getPluginManager().registerEvents(new CurrencyItem(), this);
    }

    @Override
    public void onDisable()
    {
        getLogger().info("Plugin Disabled");
    }
}
