package org.juicemans.enchantedregions;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.RegionResultSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.juicemans.enchantedregions.beans.CreationPlayer;
import org.juicemans.enchantedregions.beans.EditPlayer;
import org.juicemans.enchantedregions.beans.EnchantedRegion;
import org.juicemans.enchantedregions.steps.RegionCreationSteps;
import org.juicemans.enchantedregions.steps.RegionEditSteps;

import java.util.*;

public class EnchantedRegionManager {

    private final EnchantedRegions plugin;
    private final RegionContainer container;
    private final Map<UUID, EnchantedRegion> enchantedRegions;
    private final HashMap<UUID, CreationPlayer> creationPlayers;
    private final HashMap<UUID, EditPlayer> editPlayers;
    private final RegionCreationSteps regionCreationSteps;
    private final RegionEditSteps regionEditSteps;

    public EnchantedRegionManager(EnchantedRegions plugin){
        this.plugin = plugin;
        this.container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        this.enchantedRegions = new HashMap<>();
        this.creationPlayers = new HashMap<>();
        this.editPlayers = new HashMap<>();
        this.regionCreationSteps = new RegionCreationSteps();
        this.regionEditSteps = new RegionEditSteps();
    }

    public void addCreationPlayer(UUID id, CreationPlayer cp){
        this.creationPlayers.putIfAbsent(id, cp);
    }

    public void removeCreationPlayer(Player p){
        this.creationPlayers.remove(p.getUniqueId());
    }

    public CreationPlayer getCreationPlayer(UUID id){
        return this.creationPlayers.get(id);
    }

    public boolean isCreatingRegion(Player p){
        return this.creationPlayers.containsKey(p.getUniqueId());
    }

    public void addEditPlayer(UUID id, EditPlayer ep){
        this.editPlayers.putIfAbsent(id, ep);
    }

    public void removeEditPlayer(Player p){
        this.editPlayers.remove(p.getUniqueId());
    }

    public EditPlayer getEditPlayer(UUID id){
        return this.editPlayers.get(id);
    }

    public boolean isEditingRegion(Player p){
        return this.editPlayers.containsKey(p.getUniqueId());
    }

    public void addEnchantedRegion(UUID id, EnchantedRegion r){
        this.enchantedRegions.put(id, r);
    }

    public void removeEnchantedRegion(UUID id){
        EnchantedRegion enchantedRegion = this.enchantedRegions.get(id);
        ProtectedRegion wgRegion = getWorldGuardRegion(id.toString());
        //Remove from this system
        if(enchantedRegion != null){
            this.enchantedRegions.remove(id);
        }
        //Remove from world guard
        if(wgRegion != null){
            removeWorldGuardRegion(id.toString());
        }
    }

    public ProtectedRegion getWorldGuardRegion(String id){

        for(World w : this.plugin.getServer().getWorlds()){
            RegionManager wgRegionManager = this.container.get(BukkitAdapter.adapt(w));
            if(wgRegionManager == null){
                continue;
            }
            if(wgRegionManager.hasRegion(id)){
                return wgRegionManager.getRegion(id);
            }
        }

        return null;
    }

    public void removeWorldGuardRegion(String id){

        for(World w : this.plugin.getServer().getWorlds()){
            RegionManager wgRegionManager = this.container.get(BukkitAdapter.adapt(w));
            if(wgRegionManager == null){
                continue;
            }
            if(wgRegionManager.hasRegion(id)){
                wgRegionManager.removeRegion(id);
            }
        }
    }

    public EnchantedRegion getEnchantedRegion(UUID id){
        return this.enchantedRegions.get(id);
    }

    public Map<UUID, EnchantedRegion> getEnchantedRegions(){
        return this.enchantedRegions;
    }

    public boolean isRegionEnchantingTable(Location l){
        for(EnchantedRegion r : this.enchantedRegions.values()){
            if(!r.getEnchantingTable().getWorld().equals(l.getWorld())){
                continue;
            }
            if(r.getEnchantingTable().distance(l) == 0){
                return true;
            }
        }

        return false;
    }

    public EnchantedRegion getRegionFromEnchantingTable(Location l){
        for(EnchantedRegion r : this.enchantedRegions.values()){
            if(!r.getEnchantingTable().getWorld().equals(l.getWorld())){
                continue;
            }
            if(r.getEnchantingTable().distance(l) == 0){
                return r;
            }
        }
        return null;
    }

    public EnchantedRegion getRegionFromLodestone(Location l){
        for(EnchantedRegion r : this.enchantedRegions.values()){
            if(!r.getLodestone().getWorld().equals(l.getWorld())){
                continue;
            }
            if(r.getLodestone().distance(l) == 0){
                return r;
            }
        }
        return null;
    }

    public boolean isTableBeingUsedForCreation(Location l){
        for(CreationPlayer cp : this.creationPlayers.values()){
            if(!cp.getWorld().equals(l.getWorld())){
                continue;
            }
            if(cp.getEnchantingTable().distance(l) == 0){
                return true;
            }
        }
        return false;
    }

    public CreationPlayer getCreationPlayerFromTable(Location l){
        for(CreationPlayer cp : this.creationPlayers.values()){
            if(!cp.getWorld().equals(l.getWorld())){
                continue;
            }
            if(cp.getEnchantingTable().distance(l) == 0){
                return cp;
            }
        }
        return null;
    }

    public RegionContainer getContainer(){
        return this.container;
    }

    public EnchantedRegions getPlugin(){
        return this.plugin;
    }

    public boolean isValidRegionSelection(CreationPlayer creationPlayer){
        if(creationPlayer.getCornerOne() == null || creationPlayer.getCornerTwo() == null){
            return false;
        }
        Location l1 = creationPlayer.getCornerOne();
        Location l2 = creationPlayer.getCornerTwo();

        RegionManager rm = this.container.get(BukkitAdapter.adapt(creationPlayer.getEnchantingTable().getWorld()));
        if(rm == null){
            this.plugin.getLogger().info("There was an error loading WorldGuard region manager for world: " + creationPlayer.getWorld().getName());
            return false;
        }

        //Check currently loaded regions for an intersection
        Map<String, ProtectedRegion> regions = rm.getRegions();
        for(ProtectedRegion pr : regions.values()){
            if(Util.regionIntersects(l1, l2, Util.bvToLocation(creationPlayer.getWorld(), pr.getMinimumPoint()), Util.bvToLocation(creationPlayer.getWorld(), pr.getMaximumPoint()))){
                return false;
            }
        }

        //Check if region intersects another region currently being created
        for(CreationPlayer cp : this.creationPlayers.values()){
            //Skip the region the player is working on
            if(cp.equals(creationPlayer)) continue;
            //Skip if the creation player is yet to set both points
            if(cp.getCornerOne() == null || cp.getCornerTwo() == null) continue;
            //Check for overlap
            if(Util.regionIntersects(l1, l2, cp.getCornerOne(), cp.getCornerTwo())){
                return false;
            }
        }

        //Check if enchanting table is inside region
        return Util.isInsideAB(creationPlayer.getEnchantingTable(), l1, l2);
    }

    public RegionCreationSteps getRegionCreationSteps() {
        return regionCreationSteps;
    }

    public RegionEditSteps getRegionEditSteps() {
        return regionEditSteps;
    }

    public ApplicableRegionSet getOverlappingRegions(CreationPlayer cp){
        RegionManager wgRegionManager = this.container.get(BukkitAdapter.adapt(cp.getWorld()));
        ProtectedCuboidRegion temp = new ProtectedCuboidRegion("tmp", Util.locationToBv(cp.getCornerOne()), Util.locationToBv(cp.getCornerTwo()));
        if(wgRegionManager == null){
            return null;
        }
        return wgRegionManager.getApplicableRegions(temp);
    }

}
