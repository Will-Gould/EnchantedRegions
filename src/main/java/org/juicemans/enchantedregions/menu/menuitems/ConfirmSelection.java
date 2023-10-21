package org.juicemans.enchantedregions.menu.menuitems;

import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.juicemans.enchantedregions.EnchantedRegionManager;
import org.juicemans.enchantedregions.Util;
import org.juicemans.enchantedregions.beans.CreationPlayer;
import org.juicemans.enchantedregions.menu.MenuHandler;
import org.juicemans.enchantedregions.menu.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class ConfirmSelection implements MenuItem {
    @Override
    public GuiItem getMenuItem(MenuHandler menuHandler, EnchantedRegionManager regionManager, Gui gui, Player player, ProtectedCuboidRegion region, Location table) {

        CreationPlayer cp = regionManager.getCreationPlayer(player.getUniqueId());
        if(cp == null){
            return null;
        }

        if(!regionManager.isValidRegionSelection(cp)){
            return getDisabledItem(regionManager, player, cp, table, "Invalid region selection");
        }

        if(cp.isConfirmed()){
            return ItemBuilder.from(Material.MAP)
                    .name(Component.text("Unconfirm Selection", NamedTextColor.LIGHT_PURPLE))
                    .asGuiItem(event -> {
                        if(cp.getPaid() > 0){
                            player.getWorld().dropItem(player.getLocation(), new ItemStack(Material.DIAMOND, cp.getPaid()));
                        }
                        cp.unconfirmRegion();
                        gui.close(player);
                        gui.open(player);
                    });
        }

        return ItemBuilder.from(Material.FILLED_MAP)
                .name(Component.text("Confirm Region Selection", NamedTextColor.LIGHT_PURPLE))
                .lore(getLore(cp, regionManager))
                .asGuiItem(event -> {
                    try {
                        execute(regionManager, player, table);
                        gui.close(player);
                        gui.open(player);
                    } catch (Exception e) {
                        player.sendMessage("There was an error processing this request");
                    }
                });

    }

    @Override
    public void execute(EnchantedRegionManager rm, Player p, Location table) throws Exception {
        CreationPlayer cp = rm.getCreationPlayer(p.getUniqueId());
        cp.confirmRegion();
    }

    @Override
    public GuiItem getDisabledItem(EnchantedRegionManager rm, Player p, CreationPlayer cp, Location table, String reason) {
        return ItemBuilder.from(Material.FILLED_MAP)
                .name(Component.text("Confirm Region Selection", NamedTextColor.RED))
                .lore(getLore(cp, rm))
                .asGuiItem();
    }

    @Override
    public List<Component> getLore(CreationPlayer cp, EnchantedRegionManager rm) {
        ArrayList<Component> lore = new ArrayList<>();

        //Has primary point been selected?
        if(cp.getCornerOne() != null){
            lore.add(Component.text(" + Primary corner at: ", NamedTextColor.GREEN).append(Util.getPointMessage(cp.getCornerOne())));
        }else{
            lore.add(Component.text(" - Primary point not set", NamedTextColor.RED));
        }

        //Has secondary point been selected?
        if(cp.getCornerTwo() != null){
            lore.add(Component.text(" + Secondary corner at: ", NamedTextColor.GREEN).append(Util.getPointMessage(cp.getCornerTwo())));
        }else{
            lore.add(Component.text(" - Secondary point not set", NamedTextColor.RED));
        }

        if(cp.getCornerOne() == null || cp.getCornerTwo() == null){
            return lore;
        }

        //Is the enchanting table inside the region?
        if(Util.isInsideRegion(cp.getEnchantingTable(), cp.getCornerOne(), cp.getCornerTwo())){
            lore.add(Component.text(" + Enchanting table is inside the selected region", NamedTextColor.GREEN));
        }else{
            lore.add(Component.text(" - Enchanting table is outside the selected region", NamedTextColor.RED));
        }

        //Is the region intersecting an existing region?
        if(rm.getOverlappingRegions(cp).size() > 0){
            lore.add(Component.text(" - Selected region intersects existing region", NamedTextColor.RED));
        }

        return lore;
    }
}
