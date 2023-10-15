package org.juicemans.enchantedregions;

import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.entity.Player;
import org.juicemans.enchantedregions.beans.CreationPlayer;
import org.juicemans.enchantedregions.beans.EditPlayer;

import java.util.HashMap;
import java.util.UUID;

public class RegionManager {

    private final EnchantedRegions plugin;
    private final RegionContainer container;
    private final HashMap<UUID, CreationPlayer> creationPlayers;
    private final HashMap<UUID, EditPlayer> editPlayers;

    public RegionManager(EnchantedRegions plugin){
        this.plugin = plugin;
        this.container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        this.creationPlayers = new HashMap<>();
        this.editPlayers = new HashMap<>();
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

    public RegionContainer getRegionContainer(){
        return this.container;
    }

    public boolean isRegionEnchantingTable(Location l){
        this.container.get(l.get)
    }
}
