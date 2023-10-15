package org.juicemans.enchantedregions;

import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.plugin.java.JavaPlugin;
import org.juicemans.enchantedregions.menu.MenuHandler;

public final class EnchantedRegions extends JavaPlugin {

    private RegionContainer container;
    private MenuHandler menuHandler;
    private EnchantedRegionManager regionManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.menuHandler = new MenuHandler(this);
        this.regionManager = new EnchantedRegionManager(this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public MenuHandler getMenuHandler(){
        return this.menuHandler;
    }

    public EnchantedRegionManager getRegionManager() {
        return this.regionManager;
    }

}
