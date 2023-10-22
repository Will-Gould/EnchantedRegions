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

import java.util.ArrayList;
import java.util.List;

public class SetWarp implements MenuItem {

    @Override
    public GuiItem getMenuItem(MenuHandler menuHandler, EnchantedRegionManager regionManager, Gui gui, Player player, EnchantedRegion region, Location table) {

        if(region == null){
            return null;
        }

        if(region.getLodestone() == null){
            return ItemBuilder.from(Material.LODESTONE)
                    .name(Component.text("Edit Lodestone Warp", NamedTextColor.GREEN))
                    .lore(getLore(null, regionManager, region))
                    .asGuiItem(event -> {
                        try{
                            execute(regionManager, player, table);
                            gui.close(player);
                        }catch (Exception e){
                            player.sendMessage("There was an error processing this request");
                        }
                    });
        }

        return ItemBuilder.from(Material.LODESTONE)
                .name(Component.text("Set Lodestone Warp", NamedTextColor.GREEN))
                .lore(getLore(null, regionManager, region))
                .asGuiItem(event -> {
                    try {
                        execute(regionManager, player, table);
                        gui.close(player);
                    } catch (Exception e) {
                        player.sendMessage("There was an error processing this request");
                    }
                });
    }

    @Override
    public void execute(EnchantedRegionManager rm, Player p, Location table) throws Exception {
        EnchantedRegion r = rm.getRegionFromEnchantingTable(table);
        EditPlayer ep = new EditPlayer(rm, p, r, 4);
        rm.addEditPlayer(p.getUniqueId(), ep);
    }

    @Override
    public GuiItem getDisabledItem(EnchantedRegionManager rm, Player p, CreationPlayer cp, Location table, String reason) {
        return null;
    }

    @Override
    public List<Component> getLore(CreationPlayer cp, EnchantedRegionManager rm, EnchantedRegion region) {
        ArrayList<Component> lore = new ArrayList<>();
        lore.add(Component.text("Select a lodestone in your", NamedTextColor.GRAY));
        lore.add(Component.text("region to set a warp point", NamedTextColor.GRAY));
        return lore;
    }
}
