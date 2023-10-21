package org.juicemans.enchantedregions.menu;

import com.google.common.collect.Lists;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import dev.triumphteam.gui.guis.Gui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.juicemans.enchantedregions.EnchantedRegions;
import org.juicemans.enchantedregions.EnchantedRegionManager;
import org.juicemans.enchantedregions.Util;
import org.juicemans.enchantedregions.beans.EnchantedRegion;
import org.juicemans.enchantedregions.menu.menus.NameRegion;
import org.juicemans.enchantedregions.menu.menus.RegionCreation;
import org.juicemans.enchantedregions.menu.menus.RegionManagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class MenuHandler {

    private final EnchantedRegions plugin;
    private final HashMap<String, Menu> menus;
    private final EnchantedRegionManager regionManager;

    public MenuHandler(EnchantedRegions plugin, EnchantedRegionManager regionManager){
        this.plugin = plugin;
        this.menus = new HashMap<>();
        this.regionManager = regionManager;
        loadMenus();
    }

    public void openMenu(String menuName, Player player, Location location){
        Menu m = getMenu(menuName);
        RegionQuery query = this.regionManager.getContainer().createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(location));
        ArrayList<ProtectedRegion> regions = Lists.newArrayList(set);

        EnchantedRegion region = null;
        ProtectedCuboidRegion wgRegion = null;
        //Enchanted regions should not be able to overlap so there should only be one valid region
        //Loop over the regions in case a regular world guard region is also overlapping
        for(ProtectedRegion pr : regions){
            if(pr instanceof ProtectedCuboidRegion){
                region = this.regionManager.getEnchantedRegion(UUID.fromString(pr.getId()));
                wgRegion = (ProtectedCuboidRegion) pr;
            }
        }

        if(m == null){
            return;
        }

        MenuInfo i = m.getClass().getAnnotation(MenuInfo.class);

        //Check if player is creating something
        if(regionManager.isCreatingRegion(player) && i.disableWhenCreating()){
            this.plugin.getLogger().info("Player is creating something");
            return;
        }

        //Check if player is editing region
        if(regionManager.isEditingRegion(player) && i.disableWhenEditing()){
            this.plugin.getLogger().info("Player is editing region");
            return;
        }

        //Check for enchanting table
        if(location == null && i.needsTable()){
            this.plugin.getLogger().info("Player needs table");
            return;
        }

        //Check for region
        if(region == null && i.needsRegion()){
            this.plugin.getLogger().info("No region found");
            return;
        }

        //Start building GUI
        Gui gui = Gui.gui()
                .title(Component.text(i.title()))
                .type(i.guiType())
                .rows(i.rows())
                .create();

        //Disable interaction
        for(MenuInteraction mi : i.disabledInteractions()){
            switch (mi){
                case DROP:
                    gui.disableItemDrop();
                    break;
                case SWAP:
                    gui.disableItemSwap();
                    break;
                case TAKE:
                    gui.disableItemTake();
                    break;
                case PLACE:
                    gui.disableItemPlace();
                    break;
                default:
                    gui.disableAllInteractions();
                    break;
            }
        }

        //Now pass it over to the menu to handle menu items and display
        try{
            m.display(this, this.regionManager, gui, player, wgRegion, location);
        }catch (Exception e){
            player.sendMessage(Component.text("There was an error opening this menu", NamedTextColor.RED));
        }

    }

    private Menu getMenu(String menuName) {
        return this.menus.get(menuName);
    }

    private void loadMenus() {
        loadMenu(RegionCreation.class);
        loadMenu(RegionManagement.class);
        loadMenu(NameRegion.class);
    }

    private void loadMenu(Class<? extends Menu> m){
        MenuInfo info = m.getAnnotation(MenuInfo.class);
        if(info == null)return;

        try{
            menus.put(info.name(), m.getDeclaredConstructor().newInstance());
        }catch (Exception e){
            plugin.getLogger().severe("Unable to load menu: " + info.name());
        }
    }

}
