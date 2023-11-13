package org.juicemans.enchantedregions.beans;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.juicemans.enchantedregions.EnchantedRegionManager;
import org.juicemans.enchantedregions.Util;

import java.util.UUID;

public class CreationPlayer {

    private final Player player;
    private final UUID regionId;
    private final World world;
    private int step;
    private final Location enchantingTable;
    private Location l1;
    private Location l2;
    private int payment;
    private boolean confirmed;
    private Integer timeoutTask;

    public CreationPlayer(Player player, Location enchantingTable, UUID regionId, EnchantedRegionManager regionManager){
        this.player = player;
        this.regionId = regionId;
        this.world = enchantingTable.getWorld();
        this.enchantingTable = enchantingTable;
        this.payment = 0;
        this.confirmed = false;
        createTimoutTask(this, regionManager, player);
    }

    private void createTimoutTask(CreationPlayer cp, EnchantedRegionManager rm, Player p) {

        this.timeoutTask = Bukkit.getScheduler().scheduleSyncDelayedTask(rm.getPlugin(), new Runnable() {

            @Override
            public void run() {
                p.sendMessage(Component.text("Region creation has timed out", NamedTextColor.RED));
                //Give back any diamonds paid
                if(cp.payment > 0){
                    ItemStack diamonds = new ItemStack(Material.DIAMOND, cp.payment);
                    p.getWorld().dropItem(p.getLocation(), diamonds);
                }
                rm.removeCreationPlayer(p);
            }
        }, 600*20);

    }

    public void cancelTimeoutTask(){
        Bukkit.getScheduler().cancelTask(this.timeoutTask);
    }

    public UUID getRegionId() {
        return regionId;
    }

    public World getWorld() {
        return world;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public Location getEnchantingTable() {
        return enchantingTable;
    }

    public Location getCornerOne() {
        return l1;
    }

    public void setCornerOne(Location l1) {
        if(l1.getWorld().equals(this.world)){
            this.l1 = l1;
        }
    }

    public Location getCornerTwo() {
        return l2;
    }

    public void setCornerTwo(Location l2) {
        if(l2.getWorld().equals(this.world)){
            this.l2 = l2;
        }
    }

    public Player getPlayer(){
        return this.player;
    }

    public boolean isConfirmed() {
        return confirmed;
    }
    public void confirmRegion(){
        this.confirmed = true;
    }
    public void unconfirmRegion(){
        this.payment = 0;
        this.confirmed = false;
    }

    public void pay(int payment){
        this.payment += payment;
    }

    public void setPayment(int payment){
        this.payment = payment;
    }

    public int getPaid(){
        return this.payment;
    }

    public void refundDiamonds(){
        if(this.payment > 0){
            player.getWorld().dropItem(player.getLocation(), new ItemStack(Material.DIAMOND, this.payment));
        }
        this.payment = 0;
    }

    public int calculatePrice(){
        if(l1 == null || l2 == null){
            return 0;
        }

        int v = Util.getVolume(l1, l2);
        return Util.calculatePrice(v);
    }
}
