package org.juicemans.enchantedregions.menu.menus;

import dev.triumphteam.gui.components.GuiType;
import dev.triumphteam.gui.guis.Gui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.juicemans.enchantedregions.EnchantedRegionManager;
import org.juicemans.enchantedregions.EnchantedRegions;
import org.juicemans.enchantedregions.beans.EnchantedRegion;
import org.juicemans.enchantedregions.menu.*;

import java.util.ArrayList;

@MenuInfo(
        name = "warpKeyCrafting",
        title = "Warp Key Crafting",
        guiType = GuiType.WORKBENCH,
        rows = 0,
        disabledInteractions = {},
        disableWhenCreating = true,
        disableWhenEditing = true,
        needsTable = true,
        needsRegion = true
)
public class WarpKeyCrafting extends Menu {
    @Override
    protected boolean isValid(EnchantedRegionManager regionManager, Player player, Location location) {
        return true;
    }

    @Override
    protected void display(MenuHandler menuHandler, EnchantedRegionManager rm, Gui gui, Player p, EnchantedRegion r, Location table){
        //Test if it is appropriate to open this menu
        if(!isValid(rm, p, table)){
            return;
        }
        //ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(rm.getPlugin(), "warp-key"), getWarpKey(rm.getPlugin(), r));
        //recipe.shape("X");
        //recipe.setIngredient('X', Material.AMETHYST_SHARD);

        gui.setCloseGuiAction(inventoryCloseEvent -> {
            Inventory i = inventoryCloseEvent.getInventory();
            if(i.isEmpty()){
                return;
            }else{
                for (ItemStack item : i.getContents()) {
                    if(item == null){
                        continue;
                    }
                    p.getWorld().dropItem(p.getLocation(), item);
                }
            }
        });
        gui.open(p);
    }

    @Override
    protected void loadMenuItems() {
        //loadMenuItem(WarpCraftingInfo.class, 1);
    }

    private void craftWarpKey(EnchantedRegions pl, EnchantedRegion r, InventoryClickEvent event, Gui gui){

        if(event.getCursor().isEmpty()){
            return;
        }

        if(r.getRegion() == null){
            return;
        }

        if(r.getLodestone() == null){
            return;
        }

        //gui.updateItem(0, getWarpKey(pl, r));

    }

}
