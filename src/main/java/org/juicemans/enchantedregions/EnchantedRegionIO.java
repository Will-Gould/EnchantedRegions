package org.juicemans.enchantedregions;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.juicemans.enchantedregions.beans.EnchantedRegion;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class EnchantedRegionIO {

    private EnchantedRegions plugin;
    private FileConfiguration flat;

    public EnchantedRegionIO(EnchantedRegions plugin){
        this.plugin = plugin;
        this.flat = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "regions.yml"));
    }

    public void saveRegions(){
        for(World w : this.plugin.getServer().getWorlds()){
            StringBuilder node = new StringBuilder("worlds.");
            List<EnchantedRegion> regions = this.plugin.getRegionManager().getEnchantedRegions().values()
                    .stream()
                    .filter(region -> region.getWorld().equals(w))
                    .collect(Collectors.toList());
            node.append(w.getName()).append(".");
            for(EnchantedRegion r : regions){
                generateRegionNode(r, String.valueOf(node));
            }
        }

        try {
            flat.save(new File(plugin.getDataFolder(), "regions.yml"));
        }catch (Exception e){
            plugin.getLogger().severe("Error saving region");
        }
    }

    public void generateRegionNode(EnchantedRegion r, String node){
        String regionNode =  node + r.getId().toString() + ".";

        flat.set(regionNode + "name", r.getName());

        flat.set(regionNode + "enchantingtable.x", r.getEnchantingTable().getBlockX());
        flat.set(regionNode + "enchantingtable.y", r.getEnchantingTable().getBlockY());
        flat.set(regionNode + "enchantingtable.z", r.getEnchantingTable().getBlockZ());

        if(r.getLodestone() != null){
            flat.set(regionNode + "lodestone.x", r.getLodestone().getBlockX());
            flat.set(regionNode + "lodestone.y", r.getLodestone().getBlockY());
            flat.set(regionNode + "lodestone.z", r.getLodestone().getBlockZ());
        }

        flat.set(regionNode + "diamonds", r.getDiamonds());

    }

    public void loadRegions(){
        ArrayList<EnchantedRegion> enchantedRegions = new ArrayList<>();

        if(flat.isConfigurationSection("worlds")){
            Set<String> worlds = flat.getConfigurationSection("worlds").getKeys(false);
            if(!worlds.isEmpty()){

                for(String world : worlds){
                    World w = this.plugin.getServer().getWorld(world);
                    if(w == null){
                        continue;
                    }
                    RegionManager wgRegionManager = plugin.getRegionManager().getContainer().get(BukkitAdapter.adapt(w));
                    if (wgRegionManager == null){
                        continue;
                    }
                    if(!flat.isConfigurationSection("worlds." + world)){
                        continue;
                    }
                    Set<String> regions = flat.getConfigurationSection("worlds." + world).getKeys(false);
                    if(!regions.isEmpty()){
                        for(String region: regions){
                            loadRegionFromFile("worlds." + world + ".", region, w, wgRegionManager);
                        }
                    }
                }
            }
        }
    }

    private void loadRegionFromFile(String node, String r, World w, RegionManager wgRegionManager) {
        node += r + ".";
        ProtectedCuboidRegion wgRegion = null;
        //Find the corresponding WorldGuard region
        if(wgRegionManager.getRegion(r) instanceof ProtectedCuboidRegion){
            wgRegion = (ProtectedCuboidRegion) wgRegionManager.getRegion(r);
        }

        UUID id = UUID.fromString(r);
        String name = flat.getString(node + "name");
        Location enchantingTable = new Location(w, flat.getInt(node + "enchantingtable.x"), flat.getInt(node + "enchantingtable.y"), flat.getInt(node + "enchantingtable.z"));
        int diamonds = flat.getInt(node + "diamonds");
        EnchantedRegion region = new EnchantedRegion(id, name, w, enchantingTable, diamonds, wgRegion);

        if(flat.isConfigurationSection(node + "lodestone")){
            region.setLodestone(new Location(w, flat.getInt(node + "lodestone.x"), flat.getInt(node + "lodestone.y"), flat.getInt(node + "lodestone.z")));
        }

        this.plugin.getRegionManager().addEnchantedRegion(id, region);
    }

    public void removeJail(String id, String world){
        flat.set("worlds." + world + "." + id, null);
        saveRegions();
    }
}
