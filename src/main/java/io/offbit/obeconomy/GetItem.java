package io.offbit.obeconomy;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetItem implements CommandExecutor
{
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if (strings.length > 0)
        {
            if (commandSender instanceof Player)
            {
                if (commandSender.hasPermission("obeconomy.get.others"))
                {
                    Player player = Bukkit.getPlayer(strings[0]);
                    if (player == null)
                        commandSender.sendMessage("Couldn't find player " + strings[0]);
                    else
                        player.getInventory().addItem(CurrencyItem.getCurrency(CurrencyItem.Type.NETHERITE).itemStack);
                }
            } else
            {
                Player player = Bukkit.getPlayer(strings[0]);
                if (player == null)
                    commandSender.sendMessage("Couldn't find player " + strings[0]);
                else
                    player.getInventory().addItem(CurrencyItem.getCurrency(CurrencyItem.Type.NETHERITE).itemStack);
            }
        } else
        {
            if (!(commandSender instanceof Player))
            {
                commandSender.sendMessage("Can't run this command with no arguments in console...");
            } else if (commandSender.hasPermission("obeconomy.get"))
            {
                ((Player) commandSender).getInventory().addItem(CurrencyItem.getCurrency(CurrencyItem.Type.NETHERITE).itemStack);
            }
        }
        return true;
    }
}
