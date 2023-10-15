package org.juicemans.enchantedregions.beans;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import org.bukkit.Location;
import org.bukkit.World;

public class EnchantedRegion extends ProtectedCuboidRegion {

    private String name;
    private Location enchantingTable;
    private Location lodestone;
    private final World world;

    public EnchantedRegion(String id, String name, World world, BlockVector3 pt1, BlockVector3 pt2, Location enchantingTable) {
        super(id, pt1, pt2);
        this.world = world;
        this.enchantingTable = enchantingTable;
        this.name = name;
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
}
