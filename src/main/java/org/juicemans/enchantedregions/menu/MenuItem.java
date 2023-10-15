package org.juicemans.enchantedregions.menu;

import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public interface MenuItem {

    public GuiItem getMenuItem(MenuHandler menuHandler, RegionContainer container, Gui gui, Player player, ProtectedCuboidRegion region, Location table);

    public GuiItem getDisabledItem();

    public List<Component> getLore();
}
