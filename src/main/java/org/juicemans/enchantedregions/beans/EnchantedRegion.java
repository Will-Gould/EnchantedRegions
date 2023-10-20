package org.juicemans.enchantedregions.beans;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.UUID;

public class EnchantedRegion {

    private ProtectedCuboidRegion region;
    private UUID id;
    private String name;
    private Location enchantingTable;
    private Location lodestone;
    private final World world;
    private int diamonds;

    public EnchantedRegion(UUID id, String name, World world, Location enchantingTable, int diamonds, ProtectedCuboidRegion region) {
        this.id = id;
        this.region = region;
        this.world = world;
        this.enchantingTable = enchantingTable;
        this.name = name;
        this.diamonds = diamonds;
    }

    public UUID getId(){
        return this.id;
    }

    public ProtectedCuboidRegion getRegion(){
        return this.region;
    }

    public Location getEnchantingTable(){
        return this.enchantingTable;
    }

    public void setEnchantingTable(Location l){
        this.enchantingTable = l;
    }

    public Location getLodestone(){
        return this.lodestone;
    }

    public void setLodestone(Location l){
        this.lodestone = l;
    }

    public World getWorld(){
        return this.world;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDiamonds() {
        return diamonds;
    }

    public void setDiamonds(int diamonds) {
        this.diamonds = diamonds;
    }
}
