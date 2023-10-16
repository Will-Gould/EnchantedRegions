package org.juicemans.enchantedregions.beans;


import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CreationPlayer {

    private Player player;
    private String regionId;
    private final World world;
    private int step;
    private final Location enchantingTable;
    private Location l1;
    private Location l2;

    public CreationPlayer(Player player, Location enchantingTable, UUID regionId){
        this.player = player;
        this.regionId = regionId.toString();
        this.world = enchantingTable.getWorld();
        this.enchantingTable = enchantingTable;
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

    public Location getL1() {
        return l1;
    }

    public void setL1(Location l1) {
        if(l1.getWorld().equals(this.world)){
            this.l1 = l1;
        }
    }

    public Location getL2() {
        return l2;
    }

    public void setL2(Location l2) {
        if(l2.getWorld().equals(this.world)){
            this.l2 = l2;
        }
    }

    public Player getPlayer(){
        return this.player;
    }
}
