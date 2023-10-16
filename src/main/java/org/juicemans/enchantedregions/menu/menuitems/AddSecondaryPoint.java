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
import org.juicemans.enchantedregions.beans.CreationPlayer;
import org.juicemans.enchantedregions.menu.MenuHandler;
import org.juicemans.enchantedregions.menu.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class AddSecondaryPoint implements MenuItem {

    @Override
    public GuiItem getMenuItem(MenuHandler menuHandler, EnchantedRegionManager regionManager, Gui gui, Player player, ProtectedCuboidRegion region, Location table) {
        //Check if they are creating a region
        if(!regionManager.isCreatingRegion(player)){
            return null;
        }
        CreationPlayer cp = regionManager.getCreationPlayer(player.getUniqueId());

        if(cp.isConfirmed()){
            return getDisabledItem(player, cp, table, "Region is confirmed");
        }

        return ItemBuilder.from(Material.SOUL_TORCH)
                .name(Component.text("Select Second Corner", NamedTextColor.BLUE))
                .lore(getLore(cp))
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
        cp.setStep(2);
        return;
    }

    @Override
    public GuiItem getDisabledItem(Player p, CreationPlayer cp, Location table, String reason) {
        return ItemBuilder.from(Material.TORCH)
                .name(Component.text("Select Second Corner", NamedTextColor.RED))
                .lore(Component.text(reason, NamedTextColor.RED))
                .lore(getLore(cp))
                .asGuiItem();
    }


    @Override
    public List<Component> getLore(CreationPlayer cp) {
        ArrayList<Component> lore = new ArrayList<>();

        lore.add(Component.text("Punch the block in the opposite corner", NamedTextColor.GRAY));

        if(cp.getCornerOne() != null){
            lore.add(Component.text(" + Currently set", NamedTextColor.BLUE));
        }

        return lore;
    }
}
