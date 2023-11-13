package org.juicemans.enchantedregions.util;

import static be.seeseemelk.mockbukkit.MockBukkit.mock;
import static be.seeseemelk.mockbukkit.MockBukkit.load;

import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.Material;
import org.bukkit.World;
import org.juicemans.enchantedregions.EnchantedRegions;

public class TestInstanceCreator {

    private ServerMock server;
    private EnchantedRegions plugin;
    private WorldGuardPlugin wgPlugin;
    private World mockWorld;

    public boolean setup(){
        this.server = mock();
        this.mockWorld = new WorldMock(Material.GRASS, 3);
        this.plugin = load(EnchantedRegions.class);
        this.wgPlugin = load(WorldGuardPlugin.class);
        this.server.addSimpleWorld("world");

        return true;
    }

}
