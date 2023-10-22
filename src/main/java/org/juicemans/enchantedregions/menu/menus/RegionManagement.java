package org.juicemans.enchantedregions.menu.menus;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import dev.triumphteam.gui.components.GuiType;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.juicemans.enchantedregions.EnchantedRegionManager;
import org.juicemans.enchantedregions.beans.EnchantedRegion;
import org.juicemans.enchantedregions.menu.Menu;
import org.juicemans.enchantedregions.menu.MenuInfo;
import org.juicemans.enchantedregions.menu.MenuInteraction;
import org.juicemans.enchantedregions.menu.menuitems.CancelAction;
import org.juicemans.enchantedregions.menu.menuitems.DeleteRegion;
import org.juicemans.enchantedregions.menu.menuitems.RegionInfo;
import org.juicemans.enchantedregions.menu.menuitems.SetWarp;

@MenuInfo(
        name = "regionManagement",
        title = "Region Management",
        guiType = GuiType.CHEST,
        rows = 3,
        disabledInteractions = {MenuInteraction.ALL},
        disableWhenCreating = true,
        disableWhenEditing = false,
        needsTable = true,
        needsRegion = true
)
public class RegionManagement extends Menu {
    @Override
    protected boolean isValid(EnchantedRegionManager regionManager, Player player, Location location) {
        //Check if enchanting table location has region
        EnchantedRegion region = regionManager.getRegionFromEnchantingTable(location);
        if(region == null){
            return false;
        }

        //Return if WorldGuard region has gone missing before we test for ownership or membership
        if(region.getRegion() == null){
            return true;
        }

        //Check if the player is an owner
        if(!region.getRegion().getOwners().contains(player.getUniqueId())){
            return false;
        }

        return true;
    }

    @Override
    protected void loadMenuItems() {
        loadMenuItem(RegionInfo.class, 0);
        loadMenuItem(SetWarp.class, 5);

        loadMenuItem(DeleteRegion.class, 22);
        loadMenuItem(CancelAction.class, 8);
    }
}
