package org.juicemans.enchantedregions.menu;

import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.juicemans.enchantedregions.EnchantedRegions;

import java.util.HashMap;
import java.util.Map;

public abstract class Menu {

    protected HashMap<Integer, MenuItem> menuItems;
    protected EnchantedRegions plugin;
    protected MenuHandler menuHandler;

    public Menu(EnchantedRegions plugin, MenuHandler menuHandler){
        this.plugin = plugin;
        this.menuHandler = menuHandler;
        this.menuItems = new HashMap<>();
        loadMenuItems();
    }

    protected void display(RegionContainer container, Gui gui, Player player, ProtectedCuboidRegion region, Location table){
        for(Map.Entry<Integer, MenuItem> menuItem : this.menuItems.entrySet()){
            //i will be null menu item cannot be shown at the time
            GuiItem i = menuItem.getValue().getMenuItem(this.menuHandler, container, gui, player, region, table);
            if(i != null){
                gui.setItem(menuItem.getKey(), i);
            }
            gui.getFiller().fill(ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).asGuiItem());
        }
        gui.open(player);
    }

    protected abstract void loadMenuItems();

    protected void loadMenuItem(Class<? extends MenuItem> menuItem, int position){
        try{
            menuItems.put(position, menuItem.getDeclaredConstructor().newInstance());
        }catch(Exception e){
            this.plugin.getLogger().severe("Unable to load menu item");
        }
    }

    protected void reload(){
        this.menuItems = new HashMap<>();
        loadMenuItems();
    }

}
