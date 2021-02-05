package io.offbit.obeconomy;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomEconomy implements Economy
{
    private Plugin plugin;

    /**
     * Quick helper function to add to map, with increasing values
     *
     * @param map    HashMap<Type, Amount>
     * @param type   Type
     * @param amount Amount
     */
    private void addToMap(Map<CurrencyItem.Type, Integer> map, CurrencyItem.Type type, int amount)
    {
        if (map.containsKey(type))
            map.put(type, map.get(type) + amount);
        else
            map.put(type, amount);
    }

    /**
     * This method returns an array of all currency on a player
     *
     * @param offlinePlayer player
     * @return item stacks he has of currency type
     */
    private Map<CurrencyItem.Type, Integer> getAllCurrencyAmount(OfflinePlayer offlinePlayer)
    {
        PlayerInventory playerInventory = offlinePlayer.getPlayer().getInventory();
        Map<CurrencyItem.Type, Integer> map = new HashMap<CurrencyItem.Type, Integer>();

        for (ItemStack itemStack : playerInventory.getStorageContents())
        {
            if (itemStack == null)
                continue;

            ItemMeta meta = itemStack.getItemMeta();

            if (meta.equals(CurrencyItem.getCurrency(CurrencyItem.Type.BRICK).itemStack.getItemMeta()))
                addToMap(map, CurrencyItem.Type.BRICK, itemStack.getAmount());
            else if (meta.equals(CurrencyItem.getCurrency(CurrencyItem.Type.NETHER_BRICK).itemStack.getItemMeta()))
                addToMap(map, CurrencyItem.Type.NETHER_BRICK, itemStack.getAmount());
            else if (meta.equals(CurrencyItem.getCurrency(CurrencyItem.Type.IRON).itemStack.getItemMeta()))
                addToMap(map, CurrencyItem.Type.IRON, itemStack.getAmount());
            else if (meta.equals(CurrencyItem.getCurrency(CurrencyItem.Type.GOLD).itemStack.getItemMeta()))
                addToMap(map, CurrencyItem.Type.GOLD, itemStack.getAmount());
            else if (meta.equals(CurrencyItem.getCurrency(CurrencyItem.Type.NETHERITE).itemStack.getItemMeta()))
                addToMap(map, CurrencyItem.Type.NETHERITE, itemStack.getAmount());
        }

        return map;
    }

    /**
     * Removes a certain amount of a certain type of currency from player (amount can be divided into multiple stacks)
     *
     * @param offlinePlayer player
     * @param type          type of currency
     * @param amount        amount to remove
     */
    private boolean removeCurrencyAmount(OfflinePlayer offlinePlayer, CurrencyItem.Type type, int amount)
    {
        ItemStack[] stacks = new ItemStack[0];
        Material material = CurrencyItem.getMaterial(type);

        int materialAmount = (int) Math.floor(amount / type.value);

        if (offlinePlayer.getPlayer() == null)
            return false;

        if (material != null)
        {
            stacks = offlinePlayer.getPlayer().getInventory().all(material).values().toArray(new ItemStack[0]);
        } else
            return false;

        for (ItemStack stack : stacks)
        {
            amount -= stack.getAmount();
            if (stack.getAmount() >= amount)
                stack.setAmount(stack.getAmount() - amount);
            else
                offlinePlayer.getPlayer().getInventory().remove(stack);
        }

        return true;
    }

    public double getBalance(OfflinePlayer offlinePlayer)
    {
        double total = 0;
        for (ItemStack itemStack : offlinePlayer.getPlayer().getInventory().getStorageContents())
        {
            if (itemStack == null)
                continue;
            if (itemStack.getItemMeta().equals(CurrencyItem.getCurrency(CurrencyItem.Type.BRICK).itemStack.getItemMeta()))
                total += itemStack.getAmount() * CurrencyItem.Type.BRICK.value;
            else if (itemStack.getItemMeta().equals(CurrencyItem.getCurrency(CurrencyItem.Type.NETHER_BRICK).itemStack.getItemMeta()))
                total += itemStack.getAmount() * CurrencyItem.Type.NETHER_BRICK.value;
            else if (itemStack.getItemMeta().equals(CurrencyItem.getCurrency(CurrencyItem.Type.IRON).itemStack.getItemMeta()))
                total += itemStack.getAmount() * CurrencyItem.Type.IRON.value;
            else if (itemStack.getItemMeta().equals(CurrencyItem.getCurrency(CurrencyItem.Type.GOLD).itemStack.getItemMeta()))
                total += itemStack.getAmount() * CurrencyItem.Type.GOLD.value;
            else if (itemStack.getItemMeta().equals(CurrencyItem.getCurrency(CurrencyItem.Type.NETHERITE).itemStack.getItemMeta()))
                total += itemStack.getAmount() * CurrencyItem.Type.NETHERITE.value;
        }
        return total;
    }

    public double getBalance(String s)
    {
        Player player = Bukkit.getPlayer(s);
        if (player == null)
            return 0;
        return getBalance(player);
    }

    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double amount)
    {
        offlinePlayer.getPlayer().sendMessage("Removing " + amount + "$ from your account.");
        offlinePlayer.getPlayer().getInventory().remove(CurrencyItem.getCurrency(CurrencyItem.Type.IRON).itemStack);
        return new EconomyResponse(0, 0, null, null);
    }

    public EconomyResponse withdrawPlayer(String s, double amount)
    {
        return withdrawPlayer(Bukkit.getPlayer(s), amount);
    }

    public EconomyResponse depositPlayer(String s, double v)
    {
        return depositPlayer(Bukkit.getPlayer(s), v);
    }

    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double amount)
    {
        offlinePlayer.getPlayer().sendMessage("Depositing " + amount + "$ into your account.");
        ItemStack itemStack = CurrencyItem.getCurrency(CurrencyItem.Type.IRON).itemStack;
//        itemStack.setAmount((int) amount);
        offlinePlayer.getPlayer().getInventory().addItem(itemStack);
        return new EconomyResponse(0, 0, null, null);
    }

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
        return "BrikConomy";
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
        return "Briks";
    }

    public String currencyNameSingular()
    {
        return "Brik";
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


    public EconomyResponse withdrawPlayer(String s, String s1, double v)
    {
        return withdrawPlayer(s, v);
    }

    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String s, double amount)
    {
        return withdrawPlayer(offlinePlayer, amount);
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
