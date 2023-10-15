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

public class PlayerListener implements Listener {

    private final EnchantedRegions plugin;

    public PlayerListener(EnchantedRegions plugin){
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void regionCreation(PlayerInteractEvent event){
        if(event.getAction() == Action.LEFT_CLICK_BLOCK){
            Player p = event.getPlayer();
            Location loc = event.getClickedBlock() == null ? p.getLocation() : event.getClickedBlock().getLocation();
            EnchantedRegionManager rm = this.plugin.getRegionManager();

            if(event.getClickedBlock().getBlockData().getMaterial() == Material.ENCHANTING_TABLE){
                if(rm.isRegionEnchantingTable(loc)){
                    //TODO open region management menu
                    return;
                }
                this.plugin.getMenuHandler().openMenu("regionCreation", p, loc);
            }
        }
    }

}
