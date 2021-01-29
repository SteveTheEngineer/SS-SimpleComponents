package me.ste.stevesseries.simplecomponents.component;

import com.google.gson.JsonObject;
import me.ste.stevesseries.base.GenericUtil;
import me.ste.stevesseries.components.component.ComponentLocation;
import me.ste.stevesseries.components.component.builtin.BlockComponent;
import me.ste.stevesseries.components.component.builtin.feature.ActivatableComponent;
import me.ste.stevesseries.components.component.configuration.ConfigurableComponent;
import me.ste.stevesseries.components.component.configuration.element.ConfigurationElement;
import me.ste.stevesseries.components.component.configuration.element.builtin.BlockConfigurationElement;
import me.ste.stevesseries.components.component.configuration.element.builtin.ComponentConfigurationElement;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class BasicDoorComponent extends BlockComponent implements ActivatableComponent, ConfigurableComponent {
    private Location position1;
    private Location position2;

    private boolean open;

    public BasicDoorComponent(ComponentLocation location) {
        super(location, Material.YELLOW_STAINED_GLASS);
    }

    @Override
    public void writeToJson(JsonObject object) {
        if(this.position1 != null) {
            object.add("position1", GenericUtil.locationToJson(this.position1));
        }
        if(this.position2 != null) {
            object.add("position2", GenericUtil.locationToJson(this.position2));
        }
        object.addProperty("open", this.open);
    }

    @Override
    public void readFromJson(JsonObject object) {
        if(object.has("position1")) {
            this.position1 = GenericUtil.locationFromJson(object.getAsJsonObject("position1"));
        }
        if(object.has("position2")) {
            this.position2 = GenericUtil.locationFromJson(object.getAsJsonObject("position2"));
        }
        this.open = object.get("open").getAsBoolean();
    }

    @Override
    public void setActivated(boolean activated) {
        if(this.open != activated) {
            if(this.position1 != null && this.position2 != null) {
                for(Location location : GenericUtil.getBlocksInside(this.position1, this.position2)) {
                    location.getBlock().setType(activated ? Material.YELLOW_WOOL : Material.AIR);
                }
            }
            this.open = activated;
        }
    }

    @Override
    public boolean isActivated() {
        return this.open;
    }

    private final List<ConfigurationElement> configuration = Arrays.asList(
            new BlockConfigurationElement("Position 1", () -> this.position1, position1 -> this.position1 = position1),
            new BlockConfigurationElement("Position 2", () -> this.position2, position2 -> this.position2 = position2)
    );
    @Override
    public Collection<ConfigurationElement> getConfiguration() {
        return this.configuration;
    }
}