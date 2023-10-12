package org.juicemans.enchantedregions.menu;

import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.entity.Player;

public interface MenuItem {

    public GuiItem getMenuItem(MenuHandler menuHandler, RegionContainer container, Gui gui, Player player, ProtectedCuboidRegion region, Location table);

    public GuiItem getDisabledItem();

}
