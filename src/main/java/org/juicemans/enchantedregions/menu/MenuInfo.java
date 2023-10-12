package org.juicemans.enchantedregions.menu;

import dev.triumphteam.gui.components.GuiType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface MenuInfo {

    public String name();
    public String title();
    public GuiType guiType();
    public int rows();
    public MenuInteraction[] disabledInteractions();
    public boolean disableWhenCreating();
    public boolean disableWhenEditing();
    public boolean needsTable();
    public boolean needsRegion();

}
