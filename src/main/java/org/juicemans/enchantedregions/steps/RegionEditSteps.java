package org.juicemans.enchantedregions.steps;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.juicemans.enchantedregions.EnchantedRegionManager;
import org.juicemans.enchantedregions.Util;
import org.juicemans.enchantedregions.beans.EditPlayer;

public class RegionEditSteps {

    public void step(EnchantedRegionManager regionManager, Player player, EditPlayer editPlayer, Location location){
        switch (editPlayer.getEdit()){
            case 1:
                //Region Selection
                break;
            case 3:
                //Edit enchanting table location
                break;
            case 4:
                //Edit warp
                setRegionWarp(regionManager, player, editPlayer, location);
                break;
            case 5:
                //Delete region
                deleteRegion(regionManager, player, editPlayer, location);
                break;
        }
    }

    private void setRegionWarp(EnchantedRegionManager rm, Player p, EditPlayer ep, Location l) {
        //Check if the block is still a lodestone
        if(!l.getBlock().getBlockData().getMaterial().equals(Material.LODESTONE)){
            return;
        }

        //Check if the lodestone is inside the region
        if(!Util.isInsideRegion(l, ep.getRegion())){
            p.sendMessage(Component.text("Lodestone is not inside region", NamedTextColor.RED));
            return;
        }

        //Check if the area above the lodestone is clear
        if(!l.getWorld().getBlockAt(l.getBlockX(), l.getBlockY()+1, l.getBlockZ()).getBlockData().getMaterial().equals(Material.AIR) || !l.getWorld().getBlockAt(l.getBlockX(), l.getBlockY()+2, l.getBlockZ()).getBlockData().getMaterial().equals(Material.AIR)){
            p.sendMessage(Component.text("Space above lodestone is not clear, please make sure there are no obstructions", NamedTextColor.RED));
            return;
        }

        p.sendMessage("Region Lodestone warp saved");
        ep.getRegion().setLodestone(l);
        ep.removeTimeoutTask();
        rm.removeEditPlayer(p);
        rm.getPlugin().getIO().saveRegions();
    }

    private void deleteRegion(EnchantedRegionManager rm, Player p, EditPlayer ep, Location l) {
        //Refund payment
        if(ep.getRegion().getDiamonds() > 0){
            p.getWorld().dropItem(p.getLocation(), new ItemStack(Material.DIAMOND, ep.getRegion().getDiamonds()));
        }
        //Remove region and edit player
        rm.removeEnchantedRegion(ep.getRegion().getId());
        ep.removeTimeoutTask();
        rm.removeEditPlayer(p);
        p.sendMessage(Component.text("Region has been removed", NamedTextColor.BLUE));
    }


}
