package io.offbit.obeconomy.commands;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalanceCommand implements CommandExecutor
{
    private Economy economy;

    public BalanceCommand(Economy economyInstance)
    {
        this.economy = economyInstance;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if (commandSender instanceof Player)
        {
            double balance = economy.getBalance((Player) commandSender);
            String res = String.format(ChatColor.GREEN + "" + ChatColor.BOLD + "You have %.2f %s in your account.",
                    balance,
                    balance == 1 ? economy.currencyNameSingular() : economy.currencyNamePlural());
            commandSender.sendMessage(res);
        } else
        {
            return false;
        }
        return true;
    }
}
