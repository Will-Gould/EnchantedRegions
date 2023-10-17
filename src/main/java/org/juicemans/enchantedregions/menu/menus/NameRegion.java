package org.juicemans.enchantedregions.menu.menus;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import dev.triumphteam.gui.components.GuiType;
import dev.triumphteam.gui.guis.Gui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.juicemans.enchantedregions.EnchantedRegionManager;
import org.juicemans.enchantedregions.Util;
import org.juicemans.enchantedregions.beans.CreationPlayer;
import org.juicemans.enchantedregions.beans.EnchantedRegion;
import org.juicemans.enchantedregions.menu.Menu;
import org.juicemans.enchantedregions.menu.MenuHandler;
import org.juicemans.enchantedregions.menu.MenuInfo;

import java.util.Arrays;
import java.util.Collections;

@MenuInfo(
        name = "nameRegion",
        title = "Name And Create",
        guiType = GuiType.CHEST,
        rows = 0,
        disabledInteractions = {},
        disableWhenCreating = false,
        disableWhenEditing = false,
        needsTable = false,
        needsRegion = false
)
public class NameRegion extends Menu {

    private final ItemStack confirmItem;

    public NameRegion(){
        super();
        this.confirmItem = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = this.confirmItem.getItemMeta();
        meta.displayName(Component.text("Confirm", NamedTextColor.GREEN));
        this.confirmItem.setItemMeta(meta);
    }
    @Override
    protected boolean isValid(EnchantedRegionManager regionManager, Player player, Location location) {
        CreationPlayer cp = regionManager.getCreationPlayer(player.getUniqueId());
        if(cp == null){
            return false;
        }

        if(!regionManager.isValidRegionSelection(cp)){
            return false;
        }

        if(!cp.isConfirmed()){
            return false;
        }

        //TODO change to calculated price
        if(cp.getPaid() != Util.getVolume(cp.getCornerOne(), cp.getCornerTwo())){
            return false;
        }

        return true;
    }

    @Override
    protected void display(MenuHandler menuHandler, EnchantedRegionManager regionManager, Gui gui, Player player, ProtectedCuboidRegion region, Location table){
        if(!isValid(regionManager, player, table)){
            return;
        }

        CreationPlayer cp = regionManager.getCreationPlayer(player.getUniqueId());
        RegionManager wgRegionManager = regionManager.getContainer().get(BukkitAdapter.adapt(cp.getWorld()));
        if(wgRegionManager == null){
            player.sendMessage(Component.text("There was a WorldGuard related error, please contact an admin", NamedTextColor.RED));
            return;
        }
        new AnvilGUI.Builder()
                .plugin(regionManager.getPlugin())
                .title("Name And Create Region")
                .itemOutput(confirmItem)
                .text("region name")
                .onClick((slot, stateSnapshot) -> {
                    if(slot == AnvilGUI.Slot.OUTPUT){
                        //Check input
                        if(stateSnapshot.getText().isEmpty()){
                            return Collections.emptyList();
                        }

                        if(stateSnapshot.getText().length() > 17){
                            return Collections.emptyList();
                        }

                        //TODO regex


                        return Arrays.asList(
                                AnvilGUI.ResponseAction.close(),
                                AnvilGUI.ResponseAction.run(() -> {
                                    EnchantedRegion r = new EnchantedRegion(
                                            cp.getRegionId(),
                                            stateSnapshot.getText(),
                                            cp.getWorld(),
                                            Util.locationToBv(cp.getCornerOne()),
                                            Util.locationToBv(cp.getCornerTwo()),
                                            cp.getEnchantingTable()
                                    );
                                    wgRegionManager.addRegion(r);
                                    regionManager.removeCreationPlayer(player);
                                })
                        );
                    }

                    return Collections.emptyList();
                })
                .open(player);
    }

    @Override
    protected void loadMenuItems() {

    }
}
