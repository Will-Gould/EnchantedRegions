package org.juicemans.enchantedregions.menu.menuitems;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.juicemans.enchantedregions.EnchantedRegionManager;
import org.juicemans.enchantedregions.beans.CreationPlayer;
import org.juicemans.enchantedregions.beans.EditPlayer;
import org.juicemans.enchantedregions.beans.EnchantedRegion;
import org.juicemans.enchantedregions.menu.MenuHandler;
import org.juicemans.enchantedregions.menu.MenuItem;

import java.util.List;

public class DeleteRegion implements MenuItem {
    @Override
    public GuiItem getMenuItem(MenuHandler menuHandler, EnchantedRegionManager regionManager, Gui gui, Player player, EnchantedRegion region, Location table) {
        if(region == null){
            return null;
        }

        if(regionManager.isEditingRegion(player)){
            return null;
        }

        if(!region.getRegion().getOwners().contains(player.getUniqueId()) && !player.hasPermission("enchantedregions.region.admin")){
            return null;
        }

        return ItemBuilder.from(Material.CAMPFIRE)
                .name(Component.text("DELETE REGION", NamedTextColor.DARK_RED))
                .lore(Component.text("Permanently delete this region", NamedTextColor.GRAY))
                .asGuiItem(event -> {
                    try {
                        execute(regionManager, player, table);
                        gui.close(player);
                    }catch (Exception e){
                        player.sendMessage("There was an error processing this request");
                    }
                });
    }

    @Override
    public void execute(EnchantedRegionManager rm, Player p, Location table) throws Exception {
        if(table == null){
            throw new Exception("No enchanting table found");
        }
        EnchantedRegion region = rm.getRegionFromEnchantingTable(table);
        if(region == null){
            throw new Exception("No region found");
        }

        rm.addEditPlayer(p.getUniqueId(), new EditPlayer(rm, p, region, 5));
        p.sendMessage(Component.text("Destroy the region enchanting table to permanently remove the region", NamedTextColor.BLUE));
    }

    @Override
    public GuiItem getDisabledItem(EnchantedRegionManager rm, Player p, CreationPlayer cp, Location table, String reason) {
        return null;
    }

    @Override
    public List<Component> getLore(CreationPlayer cp, EnchantedRegionManager rm, EnchantedRegion region) {
        return null;
    }
}
