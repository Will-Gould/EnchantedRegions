package org.juicemans.enchantedregions.menu;

import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.juicemans.enchantedregions.EnchantedRegionManager;

import java.util.List;

public interface MenuItem {

    public GuiItem getMenuItem(MenuHandler menuHandler, EnchantedRegionManager regionManager, Gui gui, Player player, ProtectedCuboidRegion region, Location table);
    public void execute(EnchantedRegionManager rm, Player p, Location table) throws Exception;

    public GuiItem getDisabledItem();

    public List<Component> getLore();
}
