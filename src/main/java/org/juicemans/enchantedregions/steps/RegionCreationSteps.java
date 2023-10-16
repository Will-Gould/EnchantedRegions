package org.juicemans.enchantedregions.steps;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.C;
import org.juicemans.enchantedregions.EnchantedRegionManager;
import org.juicemans.enchantedregions.Util;
import org.juicemans.enchantedregions.beans.CreationPlayer;

public class RegionCreationSteps {

    void step(EnchantedRegionManager regionManager, Player player, CreationPlayer creationPlayer, Location location){
        switch (creationPlayer.getStep()){
            case 0:
                break;
            case 1:
                firstCorner(creationPlayer, player, location);
                break;
            case 2:
                secondCorner(creationPlayer, player, location);
                break;
            case 3:
                confirmRegion(creationPlayer);
                break;
            default:
                player.sendMessage(Component.text("Something went wrong, please start again", NamedTextColor.RED));
                regionManager.removeCreationPlayer(player);
                break;
        }
    }

    private void firstCorner(CreationPlayer cp, Player p, Location l) {
        if(!l.getWorld().equals(cp.getWorld())){
            p.sendMessage(Component.text("Your point must be in the same world as your enchanting table"));
            return;
        }
        p.sendMessage(Component.text("Primary point: ", NamedTextColor.GRAY).append(Util.getPointMessage(l)));

        cp.setCornerOne(l);
        cp.setStep(0);
    }

    private void secondCorner(CreationPlayer cp, Player p, Location l) {
        if(!l.getWorld().equals(cp.getWorld())){
            p.sendMessage(Component.text("Your point must be in the same world as your enchanting table"));
            return;
        }

        p.sendMessage(Component.text("Secondary point: ", NamedTextColor.GRAY).append(Util.getPointMessage(l)));

        cp.setCornerTwo(l);
        cp.setStep(0);
    }

    private void confirmRegion(CreationPlayer cp) {
        cp.confirmRegion();
        cp.setStep(0);
    }


}
