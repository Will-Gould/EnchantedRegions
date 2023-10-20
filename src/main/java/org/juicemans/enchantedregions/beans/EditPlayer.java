package org.juicemans.enchantedregions.beans;

import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;

public class EditPlayer {

    private EnchantedRegion region;
    private int edit;
    private ProtectedCuboidRegion wgRegion;

    public EditPlayer(EnchantedRegion region, int edit){
        this.region = region;
        this.edit = edit;
    }

    public EnchantedRegion getRegion() {
        return region;
    }

    public void setRegion(EnchantedRegion region) {
        this.region = region;
    }

    public int getEdit() {
        return edit;
    }

    public void setEdit(int edit) {
        this.edit = edit;
    }

}
