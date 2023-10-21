package org.juicemans.enchantedregions.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.juicemans.enchantedregions.EnchantedRegionManager;
import org.juicemans.enchantedregions.EnchantedRegions;
import org.juicemans.enchantedregions.beans.CreationPlayer;
import org.juicemans.enchantedregions.beans.EditPlayer;

public class PlayerListener implements Listener {

    private final EnchantedRegions plugin;
    private final EnchantedRegionManager regionManager;

    public PlayerListener(EnchantedRegions plugin){
        this.plugin = plugin;
        this.regionManager = plugin.getRegionManager();
    }

    @EventHandler(ignoreCancelled = true)
    public void regionCreation(PlayerInteractEvent event){
        if(event.getAction() == Action.LEFT_CLICK_BLOCK){
            Player p = event.getPlayer();
            Location loc = event.getClickedBlock() == null ? p.getLocation() : event.getClickedBlock().getLocation();
            EnchantedRegionManager rm = this.plugin.getRegionManager();

            //Handle enchanting table interaction
            if(event.getClickedBlock().getBlockData().getMaterial() == Material.ENCHANTING_TABLE){
                if(rm.isRegionEnchantingTable(loc)){
                    //TODO open region management menu
                    return;
                }
                this.plugin.getMenuHandler().openMenu("regionCreation", p, loc);
            }

            EditPlayer ep = rm.getEditPlayer(p.getUniqueId());
            if(ep != null){
                if(event.getClickedBlock().getBlockData().getMaterial() == Material.LODESTONE && ep.getEdit() == 4){
                    rm.getRegionEditSteps().step(rm, p, ep, loc);
                    return;
                }
            }

            //Handle point selection
            CreationPlayer cp = regionManager.getCreationPlayer(p.getUniqueId());
            if(cp != null){
                if(cp.getStep() == 1 || cp.getStep() == 2){
                    rm.getRegionCreationSteps().step(rm, p, cp, loc);
                    return;
                }
            }
        }
    }

}
