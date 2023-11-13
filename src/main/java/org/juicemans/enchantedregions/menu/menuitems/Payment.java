package org.juicemans.enchantedregions.menu.menuitems;

import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.juicemans.enchantedregions.EnchantedRegionManager;
import org.juicemans.enchantedregions.Util;
import org.juicemans.enchantedregions.beans.CreationPlayer;
import org.juicemans.enchantedregions.beans.EnchantedRegion;
import org.juicemans.enchantedregions.menu.MenuHandler;
import org.juicemans.enchantedregions.menu.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class Payment implements MenuItem {
    @Override
    public GuiItem getMenuItem(MenuHandler menuHandler, EnchantedRegionManager regionManager, Gui gui, Player player, EnchantedRegion region, Location table) {
        CreationPlayer cp = regionManager.getCreationPlayer(player.getUniqueId());
        if(cp == null){
            return null;
        }

        if(!cp.isConfirmed()){
            return getDisabledItem(regionManager, player, cp, table, "Region selection is not confirmed");
        }

        //Check if they have paid
        if(cp.getPaid() == cp.calculatePrice()){
            return getDisabledItem(regionManager, player, cp, table, "Payment complete");
        }

        //Check if player has enough diamonds in their inventory
        if(Util.countPlayerDiamonds(player) < 64){
            return getDisabledItem(regionManager, player, cp, table, "Insufficient diamonds");
        }

        return ItemBuilder.from(Material.DIAMOND)
                .name(Component.text("Pay For Region"))
                .lore(getLore(cp, regionManager, null))
                .asGuiItem(event -> {
                    try {
                        execute(regionManager, player, table);
                        gui.close(player);
                        gui.open(player);
                    } catch (Exception e) {
                        player.sendMessage("There was an error processing this request");
                    }
                });
    }

    @Override
    public void execute(EnchantedRegionManager rm, Player p, Location table) throws Exception {
        CreationPlayer cp = rm.getCreationPlayer(p.getUniqueId());

        //Check if they have already paid
        if(cp.getPaid() == cp.calculatePrice()){
            return;
        }

        int payment = 0;

        for(ItemStack stack : p.getInventory()){
            if(stack == null){
                continue;
            }
            if(stack.getType().equals(Material.DIAMOND)){
                int delta = cp.calculatePrice() - payment;

                //Check if they have fulfilled payment
                if(delta == 0){
                    break;
                }

                //Check if amount in stack is more than what is currently owed
                if(stack.getAmount() > delta){
                    stack.subtract(delta);
                    payment += delta;
                }else{
                    payment += stack.getAmount();
                    p.getInventory().removeItem(stack);
                }
            }
        }

        cp.pay(payment);

    }

    @Override
    public GuiItem getDisabledItem(EnchantedRegionManager rm, Player p, CreationPlayer cp, Location table, String reason) {
        return ItemBuilder.from(Material.DIAMOND)
                .name(Component.text("Pay For Region", NamedTextColor.RED))
                .lore(Component.text(reason, NamedTextColor.RED))
                //.lore(getLore(regionManager, cp))
                .asGuiItem();
    }

    @Override
    public List<Component> getLore(CreationPlayer cp, EnchantedRegionManager rm, EnchantedRegion r) {
        ArrayList<Component> lore = new ArrayList<>();

        lore.add(Component.text("This will be refunded after" + '\n' + "un-confirming or deletion of region", NamedTextColor.GRAY));

        if(cp.getCornerOne() != null && cp.getCornerTwo() != null){
            lore.add(Component.text("Price: ", NamedTextColor.BLUE)
                    .append(Component.text(cp.calculatePrice(), NamedTextColor.LIGHT_PURPLE))
                    .append(Component.text(" Diamonds", NamedTextColor.BLUE))
            );
        }
        return lore;
    }
}
