package org.juicemans.enchantedregions.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
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
    public void regionMenu(PlayerInteractEvent event){
        if(event.getAction() == Action.LEFT_CLICK_BLOCK){
            Player p = event.getPlayer();
            Location loc = event.getClickedBlock() == null ? p.getLocation() : event.getClickedBlock().getLocation();
            EnchantedRegionManager rm = this.plugin.getRegionManager();

            //Handle enchanting table interaction
            if(p.getInventory().getItemInMainHand().isEmpty()){
                if(event.getClickedBlock().getBlockData().getMaterial() == Material.ENCHANTING_TABLE){
                    if(rm.isRegionEnchantingTable(loc)){
                        this.plugin.getMenuHandler().openMenu("regionManagement", p, loc);
                        return;
                    }
                    this.plugin.getMenuHandler().openMenu("regionCreation", p, loc);
                }
            }

        }
    }

    @EventHandler
    public void selectPoint(PlayerInteractEvent event){
        Player p = event.getPlayer();
        Location loc = event.getClickedBlock() == null ? p.getLocation() : event.getClickedBlock().getLocation();

        if(event.getAction() != Action.LEFT_CLICK_BLOCK){
            return;
        }

        //Handle Lodestone selection
        EditPlayer ep = this.regionManager.getEditPlayer(p.getUniqueId());
        if(ep != null){
            if(event.getClickedBlock().getBlockData().getMaterial() == Material.LODESTONE && ep.getEdit() == 4){
                this.regionManager.getRegionEditSteps().step(this.regionManager, p, ep, loc);
                return;
            }
        }

        //Handle point selection
        CreationPlayer cp = regionManager.getCreationPlayer(p.getUniqueId());
        if(cp != null){
            if(cp.getStep() == 1 || cp.getStep() == 2){
                this.regionManager.getRegionCreationSteps().step(this.regionManager, p, cp, loc);
                return;
            }
        }
    }

    @EventHandler
    public void deleteRegion(BlockBreakEvent event){
        Player p = event.getPlayer();
        EditPlayer ep = this.regionManager.getEditPlayer(p.getUniqueId());

        if(ep == null){
            return;
        }

        if(!ep.getRegion().getEnchantingTable().equals(event.getBlock().getLocation())){
            return;
        }

        if(ep.getEdit() != 5){
            return;
        }

        regionManager.getRegionEditSteps().step(this.regionManager, p, ep, event.getBlock().getLocation());
    }

    @EventHandler
    public void cancelActionOnLeave(PlayerQuitEvent event){
        Player p = event.getPlayer();

        if(this.regionManager.isCreatingRegion(p)){
            this.regionManager.removeCreationPlayer(p);
        }

        if(this.regionManager.isEditingRegion(p)){
            this.regionManager.removeEditPlayer(p);
        }
    }

}
