package org.juicemans.enchantedregions.menu.menuitems;

import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.juicemans.enchantedregions.menu.MenuHandler;
import org.juicemans.enchantedregions.menu.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class CreateRegion implements MenuItem {
    @Override
    public GuiItem getMenuItem(MenuHandler menuHandler, RegionContainer container, Gui gui, Player player, ProtectedCuboidRegion region, Location table) {
        return null;
    }

    @Override
    public GuiItem getDisabledItem() {
        return null;
    }

    @Override
    public List<Component> getLore() {
        ArrayList<Component> lore = new ArrayList<>();

        return lore;
    }
}
