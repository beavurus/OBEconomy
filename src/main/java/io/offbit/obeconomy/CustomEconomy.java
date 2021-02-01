package io.offbit.obeconomy;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class CustomEconomy implements Economy
{
    private Plugin plugin;

    public CustomEconomy(Plugin plugin)
    {
        this.plugin = plugin;
    }

    public boolean isEnabled()
    {
        return true;
    }

    public String getName()
    {
        return "OBEconomy";
    }

    public boolean hasBankSupport()
    {
        return false;
    }

    public int fractionalDigits()
    {
        return 0;
    }

    public String format(double v)
    {
        return v + "";
    }

    public String currencyNamePlural()
    {
        return "Gold Coins";
    }

    public String currencyNameSingular()
    {
        return "Gold Coin";
    }

    public boolean hasAccount(String s)
    {
        return true;
    }

    public boolean hasAccount(OfflinePlayer offlinePlayer)
    {
        return true;
    }

    public boolean hasAccount(String s, String s1)
    {
        return true;
    }

    public boolean hasAccount(OfflinePlayer offlinePlayer, String s)
    {
        return true;
    }

    public double getBalance(String s)
    {
        Player player = Bukkit.getPlayer(s);
        if (player == null)
            return 0;
        return getBalance(player);
    }

    public double getBalance(OfflinePlayer offlinePlayer)
    {
        double total = 0;
        for (ItemStack itemStack : offlinePlayer.getPlayer().getInventory().getStorageContents())
        {
            if (itemStack == null)
                continue;
            if (itemStack.getItemMeta().equals(CurrencyItem.getCurrency(CurrencyItem.CurrencyType.BRICK).itemStack.getItemMeta()))
                total += itemStack.getAmount();
        }
        return total;
    }

    public double getBalance(String s, String s1)
    {
        return getBalance(s);
    }

    public double getBalance(OfflinePlayer offlinePlayer, String s)
    {
        return getBalance(offlinePlayer);
    }

    public boolean has(String s, double v)
    {
        return (getBalance(s) >= v);
    }

    public boolean has(OfflinePlayer offlinePlayer, double v)
    {
        return (getBalance(offlinePlayer) >= v);
    }

    public boolean has(String s, String s1, double v)
    {
        return (getBalance(s, s1) >= v);
    }

    public boolean has(OfflinePlayer offlinePlayer, String s, double v)
    {
        return (getBalance(offlinePlayer, s) >= v);
    }

    public EconomyResponse withdrawPlayer(String s, double amount)
    {
        Player player = Bukkit.getPlayer(s);
        if (player == null)
        {
            return new EconomyResponse(amount, 0, EconomyResponse.ResponseType.FAILURE, "Player does not exist.");
        } else
        {
            return withdrawPlayer(player, amount);
        }
    }

    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double amount)
    {
        double currentBalance = getBalance(offlinePlayer);
        if (currentBalance < amount)
            return new EconomyResponse(amount, currentBalance, EconomyResponse.ResponseType.FAILURE, String.format("%s doesn't have enough %s", offlinePlayer.getName(), currencyNamePlural()));
        else
        {
            for (ItemStack itemStack : offlinePlayer.getPlayer().getInventory().getStorageContents())
            {
                if (itemStack == null)
                    continue;
                else if (itemStack.getItemMeta().equals(CurrencyItem.getCurrency(CurrencyItem.CurrencyType.BRICK).itemStack.getItemMeta()))
                {
                    if (itemStack.getAmount() == amount)
                    {
                        offlinePlayer.getPlayer().getInventory().remove(itemStack);
                        break;
                    } else if (itemStack.getAmount() > amount)
                    {
                        itemStack.setAmount(itemStack.getAmount() - (int) amount);
                        break;
                    } else
                    {
                        amount -= itemStack.getAmount();
                        offlinePlayer.getPlayer().getInventory().remove(itemStack);
                    }
                }
            }
        }
        return new EconomyResponse(amount, getBalance(offlinePlayer), EconomyResponse.ResponseType.SUCCESS, null);
    }

    public EconomyResponse withdrawPlayer(String s, String s1, double v)
    {
        return withdrawPlayer(s, v);
    }

    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String s, double amount)
    {
        return withdrawPlayer(offlinePlayer, amount);
    }

    public EconomyResponse depositPlayer(String s, double v)
    {
        Player player = Bukkit.getPlayer(s);
        if (player == null)
            return new EconomyResponse(v, 0, EconomyResponse.ResponseType.FAILURE, "Player does not exist.");
        else
            return depositPlayer(player, v);
    }

    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double amount)
    {
        int stacksRequired = (int) Math.ceil(amount / 64);

        int freeSpace = 0;
        for (ItemStack storageContent : offlinePlayer.getPlayer().getInventory().getStorageContents())
        {
            if (storageContent == null)
                freeSpace += 64;
        }
        if (amount > freeSpace)
            return new EconomyResponse(amount, getBalance(offlinePlayer), EconomyResponse.ResponseType.FAILURE, "Player doesnt have enough free space to accept payment.");

        int modAmount = (int) amount;

        ItemStack[] itemStacks = new ItemStack[stacksRequired];
        for (int i = 0; i < stacksRequired; i++)
        {
            itemStacks[i] = CurrencyItem.getCurrency(CurrencyItem.CurrencyType.BRICK).itemStack;
            if (modAmount < 64)
            {
                itemStacks[i].setAmount(modAmount);
                modAmount = 0;
            } else
            {
                itemStacks[i].setAmount(64);
                modAmount -= 64;
            }

        }
        for (ItemStack stack : itemStacks)
        {
            offlinePlayer.getPlayer().getInventory().addItem(stack);
        }
        return new EconomyResponse(amount, getBalance(offlinePlayer), EconomyResponse.ResponseType.SUCCESS, null);
    }

    public EconomyResponse depositPlayer(String s, String s1, double v)
    {
        return depositPlayer(s, v);
    }

    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String s, double v)
    {
        return depositPlayer(offlinePlayer, v);
    }

    public EconomyResponse createBank(String s, String s1)
    {
        return null;
    }

    public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer)
    {
        return null;
    }

    public EconomyResponse deleteBank(String s)
    {
        return null;
    }

    public EconomyResponse bankBalance(String s)
    {
        return null;
    }

    public EconomyResponse bankHas(String s, double v)
    {
        return null;
    }

    public EconomyResponse bankWithdraw(String s, double v)
    {
        return null;
    }

    public EconomyResponse bankDeposit(String s, double v)
    {
        return null;
    }

    public EconomyResponse isBankOwner(String s, String s1)
    {
        return null;
    }

    public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer)
    {
        return null;
    }

    public EconomyResponse isBankMember(String s, String s1)
    {
        return null;
    }

    public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer)
    {
        return null;
    }

    public List<String> getBanks()
    {
        return null;
    }

    public boolean createPlayerAccount(String s)
    {
        return false;
    }

    public boolean createPlayerAccount(OfflinePlayer offlinePlayer)
    {
        return false;
    }

    public boolean createPlayerAccount(String s, String s1)
    {
        return false;
    }

    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String s)
    {
        return false;
    }
}
