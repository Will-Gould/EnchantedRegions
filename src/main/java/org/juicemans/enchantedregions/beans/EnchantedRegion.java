package org.juicemans.enchantedregions.beans;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;

public class EnchantedRegion extends ProtectedCuboidRegion {

    private Location enchantingTable;
    private Location lodestone;

    public EnchantedRegion(String id, BlockVector3 pt1, BlockVector3 pt2) {
        super(id, pt1, pt2);
    }

}
