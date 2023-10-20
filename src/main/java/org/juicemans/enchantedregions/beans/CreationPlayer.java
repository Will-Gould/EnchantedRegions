package org.juicemans.enchantedregions.beans;


import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.juicemans.enchantedregions.Util;

import java.util.UUID;

public class CreationPlayer {

    private Player player;
    private UUID regionId;
    private final World world;
    private int step;
    private final Location enchantingTable;
    private Location l1;
    private Location l2;
    private int payment;
    private boolean confirmed;

    public CreationPlayer(Player player, Location enchantingTable, UUID regionId){
        this.player = player;
        this.regionId = regionId;
        this.world = enchantingTable.getWorld();
        this.enchantingTable = enchantingTable;
        this.payment = 0;
        this.confirmed = false;
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

    public int getPaid(){
        return this.payment;
    }

    public int calculatePrice(){
        if(l1 == null || l2 == null){
            return 0;
        }

        //TODO create formula for price calculation
        //Calculate volume
        int v = Util.getVolume(l1, l2);
        return v;
    }
}
