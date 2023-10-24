package org.juicemans.enchantedregions.menu.menuitems;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.juicemans.enchantedregions.EnchantedRegionManager;
import org.juicemans.enchantedregions.EnchantedRegions;
import org.juicemans.enchantedregions.beans.CreationPlayer;
import org.juicemans.enchantedregions.beans.EnchantedRegion;
import org.juicemans.enchantedregions.menu.MenuHandler;
import org.juicemans.enchantedregions.menu.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class CreateWarpKey implements MenuItem {
    @Override
    public GuiItem getMenuItem(MenuHandler menuHandler, EnchantedRegionManager regionManager, Gui gui, Player player, EnchantedRegion region, Location table) {

        if(region == null){
            return null;
        }

        if(!region.getRegion().getOwners().contains(player.getUniqueId())){
            return null;
        }

        if(region.getLodestone() == null){
            return null;
        }

        if(region.getWorld().getBlockAt(region.getLodestone()).getBlockData().getMaterial() != Material.LODESTONE){
            return getDisabledItem(regionManager, player, null, table, "Lodestone has gone missing");
        }

        if(!player.getInventory().contains(Material.AMETHYST_SHARD)){
            return getDisabledItem(regionManager, player, null, table, "Amethyst shard required");
        }

        //Check if they have a warp key in inventory
        boolean hasWarpKey = false;
        boolean hasPlainShard = false;
        NamespacedKey key = new NamespacedKey(regionManager.getPlugin(), "region-id");
        for(ItemStack itemStack : player.getInventory()){
            if(itemStack == null){
                continue;
            }
            if(itemStack.getType() != Material.AMETHYST_SHARD){
                continue;
            }
            if(itemStack.getItemMeta().getPersistentDataContainer().has(key)){
                hasWarpKey = true;
                continue;
            }
            hasPlainShard = true;
        }

        if(hasWarpKey && !hasPlainShard){
            return getDisabledItem(regionManager, player, null, table, "Cannot use existing key");
        }

        return ItemBuilder.from(Material.AMETHYST_SHARD)
                .name(Component.text("Craft Warp Key", NamedTextColor.LIGHT_PURPLE))
                .lore(getLore(null, regionManager, region))
                .asGuiItem(event -> {
                    try{
                        execute(regionManager, player, table);
                    }catch (Exception e){
                        player.sendMessage("There was an error processing this request");
                    }
                });

    }

    @Override
    public void execute(EnchantedRegionManager rm, Player p, Location table) throws Exception {
        //rm.getPlugin().getMenuHandler().openMenu("warpKeyCrafting", p, table);
        EnchantedRegion r = rm.getRegionFromEnchantingTable(table);
        NamespacedKey key = new NamespacedKey(rm.getPlugin(), "region-id");
        boolean paid = false;
        for(ItemStack stack : p.getInventory()){
            if(stack == null){
                continue;
            }
            if(stack.getType().equals(Material.AMETHYST_SHARD)){
                if(stack.getItemMeta().getPersistentDataContainer().has(key)){
                    continue;
                }
                //Check if amount in stack is more than what is currently owed
                if(stack.getAmount() > 1){
                    stack.subtract(1);
                }else{
                    p.getInventory().removeItem(stack);
                }
                paid = true;
                break;
            }
        }
        if(paid){
            p.getWorld().dropItem(p.getLocation(), getWarpKey(rm.getPlugin(), r));
        }

    }

    @Override
    public GuiItem getDisabledItem(EnchantedRegionManager rm, Player p, CreationPlayer cp, Location table, String reason) {
        return ItemBuilder.from(Material.AMETHYST_SHARD)
                .name(Component.text("Craft Warp Key", NamedTextColor.LIGHT_PURPLE))
                .lore(Component.text(reason, NamedTextColor.RED))
                .asGuiItem();
    }

    @Override
    public List<Component> getLore(CreationPlayer cp, EnchantedRegionManager rm, EnchantedRegion region) {
        return new ArrayList<Component>();
    }

    private ItemStack getWarpKey(EnchantedRegions pl, EnchantedRegion r){
        NamespacedKey key = new NamespacedKey(pl, "region-id");
        ItemStack item = new ItemStack(Material.AMETHYST_SHARD);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text("Portal Key: ", NamedTextColor.LIGHT_PURPLE).append(Component.text(r.getName(), NamedTextColor.GRAY)));
        ArrayList<Component> lore = new ArrayList<>();
        lore.add(Component.text("Right click to teleport", NamedTextColor.GRAY));
        meta.lore(lore);
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, r.getId().toString());
        item.setItemMeta(meta);
        return item;
    }
}
