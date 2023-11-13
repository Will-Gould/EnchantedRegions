package org.juicemans.enchantedregions.beans;

import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.juicemans.enchantedregions.EnchantedRegionManager;

public class EditPlayer {

    private EnchantedRegion region;
    private Player player;
    private int edit;
    private ProtectedCuboidRegion wgRegion;
    private Integer timeoutTask;

    public EditPlayer(EnchantedRegionManager regionManager, Player player, EnchantedRegion region, int edit){
        this.region = region;
        this.player = player;
        this.edit = edit;
        createTimeoutTask(regionManager, player);
    }

    private void createTimeoutTask(EnchantedRegionManager rm, Player p){
        this.timeoutTask = Bukkit.getScheduler().scheduleSyncDelayedTask(rm.getPlugin(), new Runnable() {
            @Override
            public void run() {
                p.sendMessage(Component.text("Region edit has timed out", NamedTextColor.RED));
                rm.removeEditPlayer(p);
            }
        }, 600*20);
    }

    public void refundDiamonds(){
        this.player.getWorld().dropItem(this.player.getLocation(), new ItemStack(Material.DIAMOND, this.region.getDiamonds()));
    }

    public void cancelTimeoutTask(){
        Bukkit.getScheduler().cancelTask(this.timeoutTask);
    }

    public EnchantedRegion getRegion() {
        return region;
    }

    public void setRegion(EnchantedRegion region) {
        this.region = region;
    }

    public int getEdit() {
        return edit;
    }

    public void setEdit(int edit) {
        this.edit = edit;
    }

}
