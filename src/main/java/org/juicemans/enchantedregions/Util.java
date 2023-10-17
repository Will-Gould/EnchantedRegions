package org.juicemans.enchantedregions;

import com.sk89q.worldedit.math.BlockVector3;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.juicemans.enchantedregions.beans.EnchantedRegion;

public class Util {

    private static boolean isInside(double loc, double first, double second) {
        double point1 = 0;
        double point2 = 0;
        if (first < second) {
            point1 = first;
            point2 = second;
        } else {
            point2 = first;
            point1 = second;
        }

        return point1 <= loc && loc <= point2;
    }
    public static boolean isInsideAB(Location point, Location first, Location second){
        boolean x = isInside(point.getX(), first.getX(), second.getX());
        boolean y = isInside(point.getY(), first.getY(), second.getY());
        boolean z = isInside(point.getZ(), first.getZ(), second.getZ());

        return x && y && z;
    }

    public static boolean isInsideRegion(Location p, EnchantedRegion r){
        Location l1 = bvToLocation(r.getWorld(), r.getMinimumPoint());
        Location l2 = bvToLocation(r.getWorld(), r.getMaximumPoint());
        if(p.getWorld().equals(r.getWorld())){
            return isInsideAB(p, l1, l2);
        }
        return false;
    }

    public static boolean isInsideRegion(Location p, Location l1, Location l2){
        if(p.getWorld().equals(l1.getWorld()) && p.getWorld().equals(l2.getWorld())){
            return isInsideAB(p, l1, l2);
        }
        return false;
    }

    public static boolean regionIntersects(Location a1, Location a2, Location b1, Location b2){
        if (!a1.getWorld().equals(b1.getWorld())){
            return false;
        }

        boolean xAxisSep = true;
        boolean yAxisSep = true;
        boolean zAxisSep = true;

        Location l1 = getMinLocation(a1, a2);
        Location h1 = getMaxLocation(a1, a2);
        Location l2 = getMinLocation(b1, b2);
        Location h2 = getMaxLocation(b1, b2);

        //Check overlap in x axis
        if(l1.getX() <= h2.getX() && l2.getX() <= h1.getX()){
            xAxisSep = false;
        }

        //check overlap in y axis
        if(l1.getY() <= h2.getY() && l2.getY() <= h1.getY()){
            yAxisSep = false;
        }

        //check overlap in z axis
        if(l1.getZ() <= h2.getZ() && l2.getZ() <= h1.getZ()){
            zAxisSep = false;
        }

        return !(xAxisSep || yAxisSep || zAxisSep);

    }

    public static Location getMinLocation(Location l1, Location l2){
        final double lx = Math.min(l1.getX(), l2.getX());
        final double ly = Math.min(l1.getY(), l2.getY());
        final double lz = Math.min(l1.getZ(), l2.getZ());
        return new Location(l1.getWorld(), lx, ly, lz);
    }

    public static Location getMaxLocation(Location l1, Location l2){
        final double lx = Math.max(l1.getX(), l2.getX());
        final double ly = Math.max(l1.getY(), l2.getY());
        final double lz = Math.max(l1.getZ(), l2.getZ());
        return new Location(l1.getWorld(), lx, ly, lz);
    }

    public static int getVolume(Location l1, Location l2){
        Location min = getMinLocation(l1, l2);
        Location max = getMaxLocation(l1, l2);

        int h = max.getBlockY() - min.getBlockY() + 1;
        int l = max.getBlockX() - min.getBlockX() + 1;
        int w = max.getBlockZ() - min.getBlockZ() + 1;

        return h * l * w;

    }

    public static Location bvToLocation(World w, BlockVector3 bv){
        return new Location(w, bv.getX(), bv.getY(), bv.getZ());
    }

    public static BlockVector3 locationToBv(Location l){
        return BlockVector3.at(l.getBlockX(), l.getBlockY(), l.getBlockZ());
    }

    public static Component getPointMessage(Location l){

        return Component.text("x=", NamedTextColor.GRAY)
                .append(Component.text(l.getX(), NamedTextColor.BLUE))
                .append(Component.text(" y=", NamedTextColor.GRAY))
                .append(Component.text(l.getY(), NamedTextColor.BLUE))
                .append(Component.text(" z=", NamedTextColor.GRAY))
                .append(Component.text(l.getZ(), NamedTextColor.BLUE));
    }

    public static int countPlayerDiamonds(Player p){
        int count = 0;
        for(ItemStack stack : p.getInventory()){
            if(stack == null){
                continue;
            }
            if(stack.getType().equals(Material.DIAMOND)){
                count += stack.getAmount();
            }
        }
        return count;
    }

}
