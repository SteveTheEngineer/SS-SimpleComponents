package me.ste.stevesseries.simplecomponents;

import me.ste.stevesseries.components.component.ComponentManager;
import me.ste.stevesseries.simplecomponents.component.BasicDoorComponent;
import me.ste.stevesseries.simplecomponents.component.BasicTriggerComponent;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimpleComponents extends JavaPlugin {
    @Override
    public void onLoad() {
        ComponentManager.register(this, "basic_door", BasicDoorComponent.class, "Basic Door", Material.YELLOW_STAINED_GLASS);
        ComponentManager.register(this, "basic_trigger", BasicTriggerComponent.class, "Basic Trigger", Material.COAL_BLOCK);
    }
}