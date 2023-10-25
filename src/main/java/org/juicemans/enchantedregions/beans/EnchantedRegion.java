package org.juicemans.enchantedregions.beans;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.juicemans.enchantedregions.EnchantedRegions;
import org.juicemans.enchantedregions.Util;

import java.util.UUID;

public class EnchantedRegion {

    private final ProtectedCuboidRegion region;
    private final UUID id;
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

    public int getVolume(){
        if(this.region == null){
            return 0;
        }
        return region.volume();
    }

    public void warp(EnchantedRegions plugin, Player p){
        //Check if lodestone not null and still exists in world
        if(this.lodestone == null){
            p.sendMessage(Component.text("Lodestone has been removed", NamedTextColor.RED));
        }
        if(this.world.getBlockAt(this.lodestone).getBlockData().getMaterial() != Material.LODESTONE){
            p.sendMessage(Component.text("Region lodestone has gone missing", NamedTextColor.RED));
        }
        //Check if lodestone is obstructed
        if(!this.world.getBlockAt(this.lodestone.getBlockX(), this.lodestone.getBlockY()+1, this.lodestone.getBlockZ()).getBlockData().getMaterial().equals(Material.AIR) || !this.world.getBlockAt(this.lodestone.getBlockX(), this.lodestone.getBlockY()+2, this.lodestone.getBlockZ()).getBlockData().getMaterial().equals(Material.AIR)){
            p.sendMessage(Component.text("Space above lodestone is obstructed", NamedTextColor.RED));
            return;
        }
        Location tp = new Location(this.world, this.lodestone.x() + 0.5, this.lodestone.getY() + 1, this.lodestone.getZ() + 0.5);
        p.sendMessage(Component.text("Warping to ", NamedTextColor.BLUE).append(Component.text(this.name, NamedTextColor.GRAY)));
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                p.teleport(tp);
            }
        }, 20);
    }
}
