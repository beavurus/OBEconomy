package io.offbit.obeconomy;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class CurrencyItem implements Listener
{
    static JavaPlugin pluginInstance = null;

    public static class Container
    {
        public ItemStack itemStack;
        public CurrencyType currencyType;

        Container(ItemStack itemStack, CurrencyType currencyType)
        {
            this.itemStack = itemStack;
            this.currencyType = currencyType;
        }
    }

    public enum CurrencyType
    {
        BRICK(1),
        NETHER_BRICK(4),
        IRON(16),
        GOLD(64),
        NETHERITE(256);

        public final int value;

        CurrencyType(int value)
        {
            this.value = value;
        }
    }

    public static void init(JavaPlugin plugin)
    {
        pluginInstance = plugin;

        ItemStack netheriteBrik = getCurrency(CurrencyType.NETHERITE).itemStack;
        NamespacedKey namespacedKey = new NamespacedKey(JavaPlugin.getPlugin(PluginEntry.class), "netherite_brik");
        ShapedRecipe recipe = new ShapedRecipe(namespacedKey, netheriteBrik);
        recipe.shape("XX", "XX");
        recipe.setIngredient('X', Material.GOLD_INGOT);
        Bukkit.addRecipe(recipe);
    }

    @EventHandler
    public void craftNetherBrickEvent(PrepareItemCraftEvent event)
    {
        CraftingInventory craftingInventory = event.getInventory();
        int numBrik = 0;
        for (ItemStack storageContent : craftingInventory.getMatrix())
        {
            if (storageContent == null)
                continue;
            if (storageContent.getItemMeta().equals(getCurrency(CurrencyType.BRICK).itemStack.getItemMeta()))
                numBrik++;
        }
        if (event.getRecipe() == null)
            return;
        if (event.getRecipe().getResult().equals(new ItemStack(Material.BRICKS)) && numBrik == 4)
            craftingInventory.setResult(getCurrency(CurrencyType.NETHER_BRICK).itemStack);
    }

    @EventHandler
    public void craftIronBrickEvent(PrepareItemCraftEvent event)
    {
        CraftingInventory craftingInventory = event.getInventory();
        int numBrik = 0;
        for (ItemStack storageContent : craftingInventory.getMatrix())
        {
            if (storageContent == null)
                continue;
            if (storageContent.getItemMeta().equals(getCurrency(CurrencyType.NETHER_BRICK).itemStack.getItemMeta()))
                numBrik++;
        }
        if (event.getRecipe() == null)
            return;
        if (event.getRecipe().getResult().equals(new ItemStack(Material.NETHER_BRICKS)) && numBrik == 4)
            craftingInventory.setResult(getCurrency(CurrencyType.IRON).itemStack);
    }

    @EventHandler
    public void craftGoldBrickEvent(PrepareItemCraftEvent event)
    {
        CraftingInventory craftingInventory = event.getInventory();
        int numBrik = 0;
        for (ItemStack matrix : craftingInventory.getMatrix())
        {
            if (matrix == null)
                continue;
            if (matrix.getItemMeta().equals(getCurrency(CurrencyType.IRON).itemStack.getItemMeta()))
                numBrik++;
        }
        if (event.getRecipe() == null)
            return;
        if (event.getRecipe().getResult().equals(new ItemStack(Material.IRON_TRAPDOOR)) && numBrik == 4)
            craftingInventory.setResult(getCurrency(CurrencyType.GOLD).itemStack);
    }

    @EventHandler
    public void craftNetheriteBrickEvent(PrepareItemCraftEvent event)
    {
        CraftingInventory craftingInventory = event.getInventory();
        int numBrik = 0;
        for (ItemStack matrix : craftingInventory.getMatrix())
        {
            if (matrix == null)
                continue;
            if (matrix.getItemMeta().equals(getCurrency(CurrencyType.GOLD).itemStack.getItemMeta()))
                numBrik++;
        }
        if (event.getRecipe() == null)
            return;
        if (event.getRecipe().getResult().equals(getCurrency(CurrencyType.NETHERITE).itemStack) && numBrik == 4)
            craftingInventory.setResult(getCurrency(CurrencyType.NETHERITE).itemStack);
    }

    @EventHandler
    public void netheriteUpgrade(final PrepareSmithingEvent event)
    {
        SmithingInventory smithingInventory = event.getInventory();

        boolean valid = true;
        for (ItemStack content : smithingInventory.getContents())
        {
            if (content == null)
                continue;
            if (content.getItemMeta().equals(getCurrency(CurrencyType.NETHERITE).itemStack.getItemMeta()))
                valid = false;
        }
        if (!valid)
        {
            // Schedule cancellation to 1 tick later -- avoids inventory de-sync (and item loss)
            Bukkit.getScheduler().scheduleSyncDelayedTask(pluginInstance, new Runnable()
            {
                public void run()
                {
                    event.getView().getPlayer().closeInventory();
                    event.getView().getPlayer().sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "You can't use Pog Briks to upgrade your gear!");
                }
            }, 1L);
        }
    }

    public static CurrencyItem.Container getCurrency(CurrencyType currencyType)
    {
        ItemStack item;
        String itemName = "";
        ArrayList<String> loreList = new ArrayList<String>();

        switch (currencyType)
        {
            case BRICK:
                item = new ItemStack(Material.BRICK);
                itemName = ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Basic Brik";
                loreList.add("Worth 1$ per Brik");
                break;
            case NETHER_BRICK:
                item = new ItemStack(Material.NETHER_BRICK);
                itemName = ChatColor.GRAY + "" + ChatColor.BOLD + "Better Brik";
                loreList.add("Worth 4$ per Brik");
                break;
            case IRON:
                item = new ItemStack(Material.IRON_INGOT);
                itemName = ChatColor.WHITE + "" + ChatColor.BOLD + "Iron Brik";
                loreList.add("Worth 16$ per Brik");
                break;
            case GOLD:
                item = new ItemStack(Material.GOLD_INGOT);
                itemName = ChatColor.GREEN + "" + ChatColor.BOLD + "Golden Brik";
                loreList.add("Worth 64$ per Brik");
                break;
            case NETHERITE:
                item = new ItemStack(Material.NETHERITE_INGOT);
                itemName = ChatColor.GOLD + "" + ChatColor.BOLD + "Pog Brik";
                loreList.add("Worth 256$ per Brik");
                break;
            default:
                item = new ItemStack(Material.BRICK);
                itemName = ChatColor.DARK_RED + "" + ChatColor.BOLD + "PLUGIN IS BROKEN";
                break;
        }

        item.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);

        ItemMeta meta = item.getItemMeta();
        if (meta != null)
        {
            meta.setDisplayName(itemName);
            meta.setLore(loreList);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        return new Container(item, currencyType);
    }
}
