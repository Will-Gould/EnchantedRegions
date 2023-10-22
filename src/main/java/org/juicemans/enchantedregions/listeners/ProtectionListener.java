package org.juicemans.enchantedregions.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.juicemans.enchantedregions.EnchantedRegionManager;
import org.juicemans.enchantedregions.EnchantedRegions;
import org.juicemans.enchantedregions.beans.EditPlayer;
import org.juicemans.enchantedregions.beans.EnchantedRegion;

public class ProtectionListener implements Listener {

    private final EnchantedRegions plugin;
    private final EnchantedRegionManager regionManager;

    public ProtectionListener(EnchantedRegions plugin){
        this.plugin = plugin;
        this.regionManager = plugin.getRegionManager();
    }

    @EventHandler
    public void protectRegionEnchantingTable(BlockBreakEvent event){
        Player p = event.getPlayer();
        EnchantedRegion region = regionManager.getRegionFromEnchantingTable(event.getBlock().getLocation());

        if(event.getBlock().getBlockData().getMaterial() != Material.ENCHANTING_TABLE){
            return;
        }

        if(region == null){
            return;
        }

        //Check if they are deleting region
        EditPlayer ep = regionManager.getEditPlayer(p.getUniqueId());
        if(ep == null){
            event.setCancelled(true);
            return;
        }

        if(ep.getEdit() == 5){
            regionManager.getRegionEditSteps().step(regionManager, p, ep, event.getBlock().getLocation());
        }

    }

}
