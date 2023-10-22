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
import org.juicemans.enchantedregions.EnchantedRegionIO;
import org.juicemans.enchantedregions.EnchantedRegionManager;
import org.juicemans.enchantedregions.beans.CreationPlayer;
import org.juicemans.enchantedregions.beans.EnchantedRegion;
import org.juicemans.enchantedregions.menu.Menu;
import org.juicemans.enchantedregions.menu.MenuHandler;
import org.juicemans.enchantedregions.menu.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class AddSecondaryPoint implements MenuItem {

    @Override
    public GuiItem getMenuItem(MenuHandler menuHandler, EnchantedRegionManager regionManager, Gui gui, Player player, EnchantedRegion region, Location table) {
        //Check if they are creating a region
        if(!regionManager.isCreatingRegion(player)){
            return null;
        }
        CreationPlayer cp = regionManager.getCreationPlayer(player.getUniqueId());

        if(cp.isConfirmed()){
            return getDisabledItem(regionManager, player, cp, table, "Region is confirmed");
        }

        //Adapt name part of allowing player to cancel point selection
        Component name;
        if(cp.getStep() == 2){
            name = Component.text("Cancel Point Selection", NamedTextColor.RED);
        }else{
            name = Component.text("Select Second Corner", NamedTextColor.BLUE);
        }

        return ItemBuilder.from(Material.SOUL_TORCH)
                .name(name)
                .lore(getLore(cp, regionManager, null))
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
        CreationPlayer cp = rm.getCreationPlayer(p.getUniqueId());
        //Allow player to cancel point selection
        if(cp.getStep() == 2){
            cp.setStep(0);
        }else{
            cp.setStep(2);
        }
    }

    @Override
    public GuiItem getDisabledItem(EnchantedRegionManager rm, Player p, CreationPlayer cp, Location table, String reason) {
        return ItemBuilder.from(Material.TORCH)
                .name(Component.text("Select Second Corner", NamedTextColor.RED))
                .lore(Component.text(reason, NamedTextColor.RED))
                .lore(getLore(cp, rm, null))
                .asGuiItem();
    }


    @Override
    public List<Component> getLore(CreationPlayer cp, EnchantedRegionManager rm, EnchantedRegion r) {
        ArrayList<Component> lore = new ArrayList<>();

        if(cp.getStep() != 2){
            lore.add(Component.text("Punch the block in the opposite corner", NamedTextColor.GRAY));
        }
        if(cp.getCornerTwo() != null){
            lore.add(Component.text(" + Currently set", NamedTextColor.BLUE));
        }

        return lore;
    }
}
