package org.juicemans.enchantedregions;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.plugin.java.JavaPlugin;
import org.juicemans.enchantedregions.menu.MenuHandler;

public final class EnchantedRegions extends JavaPlugin {

    private RegionContainer container;
    private MenuHandler menuHandler;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        this.menuHandler = new MenuHandler(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public RegionContainer getRegionContainer() {
        return this.container;
    }

    public MenuHandler getMenuHandler(){
        return this.menuHandler;
    }
}
