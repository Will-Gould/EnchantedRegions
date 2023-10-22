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
import org.juicemans.enchantedregions.beans.EnchantedRegion;
import org.juicemans.enchantedregions.menu.MenuHandler;
import org.juicemans.enchantedregions.menu.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class RegionInfo implements MenuItem {
    @Override
    public GuiItem getMenuItem(MenuHandler menuHandler, EnchantedRegionManager regionManager, Gui gui, Player player, EnchantedRegion region, Location table) {

        if(region == null){
            return null;
        }

        if(region.getRegion() == null){
            GuiItem disabledItem = getDisabledItem(regionManager, player, null, table, "WorldGuard region has gone missing");
            List<Component> lore = disabledItem.getItemStack().lore();
            if(lore == null){
                return disabledItem;
            }
            lore.addAll(getLore(null, regionManager, region));
            return ItemBuilder.from(Material.ENCHANTED_BOOK)
                    .name(Component.text("Region Info", NamedTextColor.RED))
                    .lore(lore)
                    .asGuiItem();
        }

        //Check if player is member or owner of region
        if(!region.getRegion().getOwners().contains(player.getUniqueId()) && !region.getRegion().getMembers().contains(player.getUniqueId())){
            return null;
        }

        return ItemBuilder.from(Material.ENCHANTED_BOOK)
                .name(Component.text("Region Info", NamedTextColor.BLUE))
                .lore(getLore(null, regionManager, region))
                .asGuiItem();
    }

    @Override
    public void execute(EnchantedRegionManager rm, Player p, Location table) throws Exception {
    }

    @Override
    public GuiItem getDisabledItem(EnchantedRegionManager rm, Player p, CreationPlayer cp, Location table, String reason) {

        return ItemBuilder.from(Material.ENCHANTED_BOOK)
                .name(Component.text("Region Info", NamedTextColor.RED))
                .lore(Component.text(reason, NamedTextColor.RED))
                .asGuiItem();
    }


    @Override
    public List<Component> getLore(CreationPlayer cp, EnchantedRegionManager rm, EnchantedRegion r) {
        ArrayList<Component> lore = new ArrayList<>();

        if(r == null){
            return lore;
        }

        if(r.getRegion() == null){
            lore.add(Component.text("Your region is NOT protected", NamedTextColor.DARK_RED));
            lore.add(Component.text("Delete your region and re-claim or contact staff for support", NamedTextColor.RED));
            return lore;
        }

        //Region information
        Component name = Component.text("Region Name: ", NamedTextColor.LIGHT_PURPLE).append(Component.text(r.getName(), NamedTextColor.GRAY));
        Component volume = Component.text("Region Volume: ", NamedTextColor.LIGHT_PURPLE).append(Component.text(r.getVolume(), NamedTextColor.GRAY));
        Component diamonds = Component.text("Diamonds Used: ", NamedTextColor.LIGHT_PURPLE).append(Component.text(r.getDiamonds(), NamedTextColor.GRAY));
        lore.add(name);
        lore.add(volume);
        lore.add(diamonds);

        return lore;
    }
}
