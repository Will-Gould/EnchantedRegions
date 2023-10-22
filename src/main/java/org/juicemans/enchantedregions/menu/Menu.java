package org.juicemans.enchantedregions.menu;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.juicemans.enchantedregions.EnchantedRegionManager;
import org.juicemans.enchantedregions.beans.EnchantedRegion;

import java.util.HashMap;
import java.util.Map;

public abstract class Menu {

    protected HashMap<Integer, MenuItem> menuItems;

    public Menu(){
        this.menuItems = new HashMap<>();
        loadMenuItems();
    }

    protected abstract boolean isValid(EnchantedRegionManager regionManager, Player player, Location location);

    protected void display(MenuHandler menuHandler, EnchantedRegionManager regionManager, Gui gui, Player player, EnchantedRegion region, Location table){
        //Test if it is appropriate to open this menu
        if(!isValid(regionManager, player, table)){
            return;
        }
        for(Map.Entry<Integer, MenuItem> menuItem : this.menuItems.entrySet()){
            //i will be null menu item cannot be shown at the time
            GuiItem i = menuItem.getValue().getMenuItem(menuHandler, regionManager, gui, player, region, table);
            if(i != null){
                gui.setItem(menuItem.getKey(), i);
            }
        }
        gui.getFiller().fill(ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).asGuiItem());
        gui.open(player);
    }

    protected abstract void loadMenuItems();

    protected void loadMenuItem(Class<? extends MenuItem> menuItem, int position){
        try{
            menuItems.put(position, menuItem.getDeclaredConstructor().newInstance());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    protected void reload(){
        this.menuItems = new HashMap<>();
        loadMenuItems();
    }

}
