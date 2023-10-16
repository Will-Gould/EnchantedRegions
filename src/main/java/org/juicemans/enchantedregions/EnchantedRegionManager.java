package org.juicemans.enchantedregions;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.RegionResultSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
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

import java.util.*;

public class EnchantedRegionManager {

    private final EnchantedRegions plugin;
    private final RegionContainer container;
    private final HashMap<UUID, CreationPlayer> creationPlayers;
    private final HashMap<UUID, EditPlayer> editPlayers;

    public EnchantedRegionManager(EnchantedRegions plugin){
        this.plugin = plugin;
        this.container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        this.creationPlayers = new HashMap<>();
        this.editPlayers = new HashMap<>();
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

    public EditPlayer getEditPlayer(UUID id){
        return this.editPlayers.get(id);
    }

    public boolean isEditingRegion(Player p){
        return this.editPlayers.containsKey(p.getUniqueId());
    }

    public boolean isRegionEnchantingTable(Location l){
        RegionQuery query = this.container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(l));
        if(set.size() == 0){
            return false;
        }
        for(ProtectedRegion pr : set){
            if(pr instanceof EnchantedRegion){
                EnchantedRegion er = (EnchantedRegion) pr;
                if(er.getEnchantingTable().distance(l) == 0){
                    return true;
                }
            }
        }
        return false;
    }

    public EnchantedRegion getRegionFromEnchantingTable(Location l){
        RegionQuery query = this.container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(l));
        if(set.size() == 0){
            return null;
        }
        for(ProtectedRegion pr : set){
            if(pr instanceof EnchantedRegion){
                EnchantedRegion er = (EnchantedRegion) pr;
                if(er.getEnchantingTable().distance(l) == 0){
                    return er;
                }
            }
        }
        return null;
    }

    public EnchantedRegion getRegionFromLodestone(Location l){
        RegionQuery query = this.container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(l));
        if(set.size() == 0){
            return null;
        }
        for(ProtectedRegion pr : set){
            if(pr instanceof EnchantedRegion){
                EnchantedRegion er = (EnchantedRegion) pr;
                if(er.getLodestone().distance(l) == 0){
                    return er;
                }
            }
        }
        return null;
    }

    public boolean isTableBeingUsedForCreation(Location l){
        for(CreationPlayer cp : this.creationPlayers.values()){
            if(cp.getEnchantingTable().distance(l) == 0){
                return true;
            }
        }
        return false;
    }

    public CreationPlayer getCreationPlayerFromTable(Location l){
        for(CreationPlayer cp : this.creationPlayers.values()){
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

    public boolean isInsideRegion(Location l, List<EnchantedRegion> regions){
        for(EnchantedRegion r : regions){
            if(Util.isInsideRegion(l, r)){
                return true;
            }
        }
        return false;
    }

    public boolean isValidRegionSelection(CreationPlayer creationPlayer){
        if(creationPlayer.getL1() == null || creationPlayer.getL2() == null){
            return false;
        }
        Location l1 = creationPlayer.getL1();
        Location l2 = creationPlayer.getL2();

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
            if(cp.getL1() == null || cp.getL2() == null) continue;
            //Check for overlap
            if(Util.regionIntersects(l1, l2, cp.getL1(), cp.getL2())){
                return false;
            }
        }

        //Check if enchanting table is inside region
        return Util.isInsideAB(creationPlayer.getEnchantingTable(), l1, l2);
    }

}
