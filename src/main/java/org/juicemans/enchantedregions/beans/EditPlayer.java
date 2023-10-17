package org.juicemans.enchantedregions.beans;

public class EditPlayer {

    private EnchantedRegion region;
    private int edit;

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
