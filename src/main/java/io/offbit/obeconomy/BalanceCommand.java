package io.offbit.obeconomy;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class BalanceCommand implements CommandExecutor
{
    private double getBalance(String playerName)
    {
        return getBalance(Bukkit.getPlayer(playerName));
    }

    private double getBalance(Player player)
    {
        return JavaPlugin.getPlugin(PluginEntry.class).getEconomy().getBalance(player);
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if (commandSender instanceof Player)
        {
            commandSender.sendMessage(getBalance((Player) commandSender) + " GOLD COINS");
        } else
        {
            return false;
        }
        return true;
    }
}
