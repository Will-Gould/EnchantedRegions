package org.juicemans.enchantedregions.menu.menus;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import dev.triumphteam.gui.components.GuiType;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.juicemans.enchantedregions.EnchantedRegionManager;
import org.juicemans.enchantedregions.EnchantedRegions;
import org.juicemans.enchantedregions.beans.CreationPlayer;
import org.juicemans.enchantedregions.menu.*;
import org.juicemans.enchantedregions.menu.menuitems.*;

@MenuInfo(
        name = "regionCreation",
        title = "Region Creation",
        guiType = GuiType.CHEST,
        rows = 3,
        disabledInteractions = {MenuInteraction.ALL},
        disableWhenCreating = false,
        disableWhenEditing = true,
        needsTable = true,
        needsRegion = false
)
public class RegionCreation extends Menu {

    @Override
    protected boolean isValid(EnchantedRegionManager regionManager, Player player, Location location) {
        //Check if they have permission to open this menu
        if(!player.hasPermission("enchantedregions.createregion")){
            player.sendMessage(Component.text("You do not have permission to do this", NamedTextColor.RED));
            return false;
        }

        //Check if this enchanting table is within an existing region
        RegionQuery query = regionManager.getContainer().createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(location));
        if(set.size() > 0){
            return false;
        }

        //Check if someone is already using this table to create a region
        CreationPlayer cp = regionManager.getCreationPlayerFromTable(location);
        if(cp != null && !cp.getPlayer().equals(player)){
            player.sendMessage(Component.text("Someone else is using this table for region creation", NamedTextColor.RED));
            return false;
        }

        //Check that if they have already started creating a region that they are using the appropriate enchanting table to open the creation menu
        if(regionManager.isCreatingRegion(player)){
            if(regionManager.getCreationPlayer(player.getUniqueId()).getEnchantingTable().distance(location) != 0){
                player.sendMessage(Component.text("Please use the appropriate enchanting table for creation menu", NamedTextColor.RED));
                return false;
            }
        }

        //Check if player is editing region
        if(regionManager.isEditingRegion(player)){
            player.sendMessage(Component.text("Please finish editing region before you make a new one", NamedTextColor.RED));
            return false;
        }

        return true;
    }

    @Override
    protected void loadMenuItems() {
        loadMenuItem(AddPrimaryPoint.class, 0);
        loadMenuItem(AddSecondaryPoint.class, 9);
        loadMenuItem(ConfirmSelection.class, 3);
        loadMenuItem(Payment.class, 4);
        loadMenuItem(NameAndCreate.class, 5);

        loadMenuItem(CancelAction.class, 8);
        loadMenuItem(CreateRegion.class, 13);
    }

}
