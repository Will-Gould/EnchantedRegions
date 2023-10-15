package org.juicemans.enchantedregions;

import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.juicemans.enchantedregions.listeners.PlayerListener;
import org.juicemans.enchantedregions.menu.MenuHandler;

public final class EnchantedRegions extends JavaPlugin {

    private RegionContainer container;
    private MenuHandler menuHandler;
    private EnchantedRegionManager regionManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        PluginManager plm = this.getServer().getPluginManager();
        this.regionManager = new EnchantedRegionManager(this);
        this.menuHandler = new MenuHandler(this, this.regionManager);

        //Register listeners
        plm.registerEvents(new PlayerListener(this), this);

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
