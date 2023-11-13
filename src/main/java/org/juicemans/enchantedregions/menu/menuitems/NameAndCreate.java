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
import org.juicemans.enchantedregions.EnchantedRegionManager;
import org.juicemans.enchantedregions.Util;
import org.juicemans.enchantedregions.beans.CreationPlayer;
import org.juicemans.enchantedregions.beans.EnchantedRegion;
import org.juicemans.enchantedregions.menu.MenuHandler;
import org.juicemans.enchantedregions.menu.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class NameAndCreate implements MenuItem {

    private MenuHandler menuHandler;
    @Override
    public GuiItem getMenuItem(MenuHandler menuHandler, EnchantedRegionManager regionManager, Gui gui, Player player, EnchantedRegion region, Location table) {
        this.menuHandler = menuHandler;

        CreationPlayer cp = regionManager.getCreationPlayer(player.getUniqueId());
        if(cp == null){
            return null;
        }

        if(!cp.isConfirmed()){
            return getDisabledItem(regionManager, player, cp, table, "Region selection not confirmed");
        }

        //TODO adjust to price
        if(cp.getPaid() != cp.calculatePrice()){
            return getDisabledItem(regionManager, player, cp, table, "Payment not complete");
        }

        return ItemBuilder.from(Material.ENCHANTED_BOOK)
                .name(Component.text("Name and Create"))
                .lore(getLore(cp, regionManager, null))
                .asGuiItem(event -> {
                    try {
                        //gui.close(p);
                        execute(regionManager, player, table);
                    } catch (Exception e) {
                        player.sendMessage("There was an error processing this request");
                    }
                });
    }

    @Override
    public void execute(EnchantedRegionManager rm, Player p, Location table) throws Exception {
        CreationPlayer cp = rm.getCreationPlayer(p.getUniqueId());

        this.menuHandler.openMenu("nameRegion", p, table);
    }

    @Override
    public GuiItem getDisabledItem(EnchantedRegionManager rm, Player p, CreationPlayer cp, Location table, String reason) {
        return ItemBuilder.from(Material.ENCHANTED_BOOK)
                .name(Component.text("Name and Create", NamedTextColor.RED))
                .lore(Component.text(reason, NamedTextColor.RED))
                //.lore(getLore(regionManager, cp))
                .asGuiItem();
    }

    @Override
    public List<Component> getLore(CreationPlayer cp, EnchantedRegionManager rm, EnchantedRegion r) {
        ArrayList<Component> lore = new ArrayList<>();

        lore.add(Component.text("Name and Create your region", NamedTextColor.GRAY));

        return lore;
    }
}
