package io.offbit.obeconomy.commands;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EconomyCommand implements CommandExecutor, TabCompleter
{
    private Economy economy;
    private String[] firstArgument;

    public EconomyCommand(Economy economyInstance)
    {
        this.economy = economyInstance;

        firstArgument = new String[]{"add", "remove"};
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args)
    {
        if (args == null || args.length == 0)
        {
            commandSender.sendMessage(
                    "Brik v1.0.0\n"
                            + "/brik add {player} {amount}: adds a certain amount of briks to a player\n"
                            + "/brik remove {player} {amount}: removes a certain amount of briks from a player"
            );
            return true;
        }

        switch (args[0].toUpperCase())
        {
            case "ADD":
                if (args.length == 3)
                {
                    Player player = Bukkit.getPlayer(args[1]);
                    if (player == null)
                        commandSender.sendMessage("Couldn't find player " + args[1]);
                    else
                        this.economy.depositPlayer(player, Double.parseDouble(args[2]));
                } else
                {
                    commandSender.sendMessage("Wrong format: /economy add {player} {amount}");
                }
                break;
            case "REMOVE":
                if (args.length == 3)
                {
                    Player player = Bukkit.getPlayer(args[1]);
                    if (player == null)
                        commandSender.sendMessage("Couldn't find player " + args[1]);
                    else
                        this.economy.withdrawPlayer(player, Double.parseDouble(args[2]));
                } else
                {
                    commandSender.sendMessage("Wrong format: /economy remove {player} {amount}");
                }
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args)
    {
        List<String> commands = Arrays.asList(this.firstArgument);
        List<String> completions = new ArrayList<>();

        if (args.length == 1)
        {
            StringUtil.copyPartialMatches(args[0], commands, completions);
            Collections.sort(completions);
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove")))
        {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers())
            {
                completions.add(onlinePlayer.getDisplayName());
            }
            Collections.sort(completions);
        } else if (args.length == 3 && (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove")))
        {
            completions.add("1");
            completions.add("10");
            completions.add("50");
            completions.add("100");
        }

        return completions;
    }
}
