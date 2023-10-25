package org.juicemans.enchantedregions.listeners;

import org.bukkit.*;
import org.bukkit.entity.Allay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.juicemans.enchantedregions.EnchantedRegionManager;
import org.juicemans.enchantedregions.EnchantedRegions;
import org.juicemans.enchantedregions.beans.CreationPlayer;
import org.juicemans.enchantedregions.beans.EditPlayer;
import org.juicemans.enchantedregions.beans.EnchantedRegion;

import java.util.UUID;

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

    @EventHandler
    public void warpKey(PlayerInteractEvent event){
        Player p = event.getPlayer();

        if(p.getInventory().getItemInMainHand().getType() != Material.AMETHYST_SHARD){
            return;
        }

        if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK){
            return;
        }

        if(
                event.getMaterial().equals(Material.BREWING_STAND) ||
                event.getMaterial().equals(Material.STONECUTTER) ||
                Tag.BEDS.isTagged(event.getMaterial()) ||
                event.getMaterial().equals(Material.LOOM) ||
                event.getMaterial().equals(Material.SMITHING_TABLE) ||
                event.getMaterial().equals(Material.ARMOR_STAND) ||
                event.getMaterial().equals(Material.CARTOGRAPHY_TABLE) ||
                event.getMaterial().equals(Material.FLETCHING_TABLE) ||
                event.getMaterial().equals(Material.GRINDSTONE) ||
                Tag.BUTTONS.isTagged(event.getMaterial()) ||
                Tag.WOODEN_DOORS.isTagged(event.getMaterial()) ||
                Tag.WOODEN_TRAPDOORS.isTagged(event.getMaterial()) ||
                Tag.FENCE_GATES.isTagged(event.getMaterial()) ||
                Tag.ITEMS_BOATS.isTagged(event.getMaterial()) ||
                Tag.SHULKER_BOXES.isTagged(event.getMaterial()) ||
                event.getMaterial().equals(Material.CHEST) ||
                event.getMaterial().equals(Material.MINECART) ||
                event.getMaterial().equals(Material.ENDER_CHEST) ||
                event.getMaterial().equals(Material.TRAPPED_CHEST) ||
                event.getMaterial().equals(Material.CHEST_MINECART) ||
                event.getMaterial().equals(Material.HOPPER_MINECART) ||
                event.getMaterial().equals(Material.FURNACE_MINECART) ||
                event.getMaterial().equals(Material.TNT_MINECART) ||
                event.getMaterial().equals(Material.ENCHANTING_TABLE) ||
                event.getMaterial().equals(Material.CRAFTING_TABLE) ||
                event.getMaterial().equals(Material.BARREL) ||
                event.getMaterial().equals(Material.FURNACE) ||
                event.getMaterial().equals(Material.BLAST_FURNACE) ||
                event.getMaterial().equals(Material.LECTERN) ||
                event.getMaterial().equals(Material.SMOKER)
        ){
            return;
        }

        ItemMeta meta = p.getInventory().getItemInMainHand().getItemMeta();
        NamespacedKey key = new NamespacedKey(this.plugin, "region-id");
        if(!meta.getPersistentDataContainer().has(key)){
            return;
        }
        String id = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
        if(id == null){
            return;
        }
        EnchantedRegion r = regionManager.getEnchantedRegion(UUID.fromString(id));
        if(r == null){
            return;
        }

        r.warp(plugin, p);
    }

    @EventHandler
    public void portalKeyProtection(PlayerInteractEntityEvent event){

        if(event.getPlayer().getInventory().getItemInMainHand().isEmpty()){
            return;
        }

        if(event.getPlayer().getInventory().getItemInMainHand().getType() == Material.AMETHYST_SHARD){
            if(event.getRightClicked() instanceof Allay){
                if(event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(this.plugin, "region-id"))){
                    event.setCancelled(true);
                }
            }
        }

    }
}
