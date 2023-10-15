package org.juicemans.enchantedregions;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
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

import java.util.HashMap;
import java.util.UUID;

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

        //TODO remove
        World world = Bukkit.getWorld("world");
        BlockVector3 b1 = BlockVector3.at(-569, 68, 523);
        BlockVector3 b2 = BlockVector3.at(-579, 65, 520);
        Location table = new Location(world, -570, 66, 521);
        String id = UUID.randomUUID().toString();
        this.plugin.getLogger().info("Region id: " + id);
        EnchantedRegion er = new EnchantedRegion(id, "Juice Zone", world, b1, b2, table);
        assert world != null;
        RegionManager regionManager = this.container.get(BukkitAdapter.adapt(world));
        assert regionManager != null;
        regionManager.addRegion(er);

        EnchantedRegion r = this.getRegionFromEnchantingTable(table);
        if(r != null){
           this.plugin.getLogger().info("Region name: '" + r.getName() + "' found from table location");
        }else{
            this.plugin.getLogger().info("No region found from table location");
        }

    }

    public void addCreationPlayer(UUID id, CreationPlayer cp){
        this.creationPlayers.putIfAbsent(id, cp);
    }

    public boolean isCreatingRegion(Player p){
        return this.creationPlayers.containsKey(p.getUniqueId());
    }

    public void addEditPlayer(UUID id, EditPlayer ep){
        this.editPlayers.putIfAbsent(id, ep);
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

    public RegionContainer getContainer(){
        return this.container;
    }
}
