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
import org.juicemans.enchantedregions.beans.CreationPlayer;
import org.juicemans.enchantedregions.beans.EditPlayer;
import org.juicemans.enchantedregions.menu.MenuHandler;
import org.juicemans.enchantedregions.menu.MenuItem;

import java.util.List;

public class CancelAction implements MenuItem {
    @Override
    public GuiItem getMenuItem(MenuHandler menuHandler, EnchantedRegionManager regionManager, Gui gui, Player player, ProtectedCuboidRegion region, Location table) {
        //Player must be editing or creating to show in a menu
        if(!regionManager.isCreatingRegion(player) && !regionManager.isEditingRegion(player)){
            return null;
        }

        return ItemBuilder.from(Material.STRUCTURE_VOID)
                .name(Component.text("Cancel", NamedTextColor.RED))
                .glow(true)
                .asGuiItem(event -> {
                    try{
                        execute(regionManager, player, table);
                        gui.close(player);
                    }catch (Exception e){
                        player.sendMessage(Component.text("There was an error processing this request"));
                    }
                });
    }

    @Override
    public void execute(EnchantedRegionManager rm, Player p, Location table) throws Exception {
        CreationPlayer cp = rm.getCreationPlayer(p.getUniqueId());
        EditPlayer ep = rm.getEditPlayer(p.getUniqueId());

        //Cancel creation
        if(cp != null){
            //Refund any payment
            if(cp.getPaid() > 0){
                p.getWorld().dropItem(p.getLocation(), new ItemStack(Material.DIAMOND, cp.getPaid()));
            }

            rm.removeCreationPlayer(p);
        }

        //Cancel edit
        if(ep != null){
            //Check if lodestone has been removed
            if(ep.getRegion().getLodestone() != null && ep.getRegion().getWorld().getBlockAt(ep.getRegion().getLodestone()).getBlockData().getMaterial() != Material.LODESTONE){
                p.sendMessage("Lodestone has been removed");
                ep.getRegion().setLodestone(null);
            }
            rm.removeEditPlayer(p);
        }

    }

    @Override
    public GuiItem getDisabledItem(EnchantedRegionManager rm, Player p, CreationPlayer cp, Location table, String reason) {
        return null;
    }

    @Override
    public List<Component> getLore(CreationPlayer cp, EnchantedRegionManager rm) {
        return null;
    }
}
