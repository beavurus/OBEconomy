package io.offbit.obeconomy;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DisabledCraftingEvent implements Listener
{
    @EventHandler
    public void onPlayerCraft(PrepareItemCraftEvent event)
    {
//        CraftingInventory inventory = event.getInventory();
//        for (ItemStack stack : inventory.getStorageContents())
//        {
//            if (stack == null)
//                continue;
//
//            ItemMeta meta = stack.getItemMeta();
//            if (meta == null)
//                continue;
//            else if (meta.equals(CurrencyItem.getCurrency().getItemMeta()))
//                inventory.setResult(null);
//        }
    }
}
