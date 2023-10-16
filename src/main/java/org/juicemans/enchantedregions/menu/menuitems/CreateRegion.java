package org.juicemans.enchantedregions.menu.menuitems;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.C;
import org.juicemans.enchantedregions.EnchantedRegionManager;
import org.juicemans.enchantedregions.beans.CreationPlayer;
import org.juicemans.enchantedregions.menu.MenuHandler;
import org.juicemans.enchantedregions.menu.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CreateRegion implements MenuItem {

    @Override
    public GuiItem getMenuItem(MenuHandler menuHandler, EnchantedRegionManager regionManager, Gui gui, Player player, ProtectedCuboidRegion region, Location table) {
        if(regionManager.isCreatingRegion(player)){
            return null;
        }

        return ItemBuilder.from(Material.BOOK)
                .name(Component.text("Create New Region"))
                .lore(getLore(null))
                .asGuiItem(event -> {
                    try{
                        execute(regionManager, player, table);
                    }catch(Exception e){
                        player.sendMessage(Component.text("There was an error processing this request"));
                    }
                });
    }

    @Override
    public void execute(EnchantedRegionManager rm, Player p, Location table) throws Exception {
        RegionContainer container = rm.getContainer();

        //Double check we're not already creating or editing a region
        if(rm.isEditingRegion(p) || rm.isCreatingRegion(p)){
            return;
        }

        //Check if enchanting table is still there
        Material mat = p.getWorld().getBlockAt(table).getBlockData().getMaterial();
        if(mat != Material.ENCHANTING_TABLE){
            p.sendMessage(Component.text("Your enchanting table has gone missing", NamedTextColor.RED));
            return;
        }

        //Make sure there is no UUID collision (As unlikely as it is to happen)
        UUID id = UUID.randomUUID();
        boolean unique = false;
        while(!unique){
             unique = !Objects.requireNonNull(container.get(BukkitAdapter.adapt(table.getWorld()))).hasRegion(id.toString());
        }

        CreationPlayer cp = new CreationPlayer(p, table, id);
        rm.addCreationPlayer(p.getUniqueId(), cp);
    }

    @Override
    public GuiItem getDisabledItem(Player p, CreationPlayer cp, Location table, String reason) {
        return null;
    }


    @Override
    public List<Component> getLore(CreationPlayer cp) {
        ArrayList<Component> lore = new ArrayList<>();
        //lore.add(Component.text("Create new region"));
        return lore;
    }
}
