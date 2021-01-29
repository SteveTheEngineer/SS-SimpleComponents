package me.ste.stevesseries.simplecomponents.component;

import com.google.gson.JsonObject;
import me.ste.stevesseries.components.component.ComponentLocation;
import me.ste.stevesseries.components.component.ComponentManager;
import me.ste.stevesseries.components.component.builtin.MapComponent;
import me.ste.stevesseries.components.component.builtin.feature.ActivatableComponent;
import me.ste.stevesseries.components.component.configuration.ConfigurableComponent;
import me.ste.stevesseries.components.component.configuration.element.ConfigurationElement;
import me.ste.stevesseries.components.component.configuration.element.builtin.ComponentConfigurationElement;
import me.ste.stevesseries.components.gui.ComponentModificationGUI;
import me.ste.stevesseries.components.map.CustomMap;
import me.ste.stevesseries.components.map.MapClickType;
import me.ste.stevesseries.components.map.MapData;
import me.ste.stevesseries.components.map.render.CustomMapCanvas;
import me.ste.stevesseries.components.map.render.MapColor;
import me.ste.stevesseries.components.map.text.TextAnchor;
import me.ste.stevesseries.components.map.text.builtin.DefaultFont;
import me.ste.stevesseries.inventoryguilibrary.GUIManager;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BasicTriggerComponent extends MapComponent implements ConfigurableComponent {
    private ComponentLocation targetComponent;

    public BasicTriggerComponent(ComponentLocation location) {
        super(location);
        BasicTriggerComponent self = this;
        this.setMap(new CustomMap() {
            @Override
            public void render(Player player, MapData data, CustomMapCanvas canvas) {
                canvas.clearAll(MapColor.SNOW.withLightness(MapColor.Lightness.LIGHTEST));
                canvas.fillPixels(2, 2, 124, 124, MapColor.COLOR_BLACK.withLightness(MapColor.Lightness.DARKEST));
                canvas.drawText("TOUCH HERE", DefaultFont.INSTANCE, MapColor.SNOW.withLightness(MapColor.Lightness.LIGHTEST), TextAnchor.MIDDLE, TextAnchor.MIDDLE, 63, 63, 1);
            }

            @Override
            public void onClick(Player player, MapData data, MapClickType type, int x, int y, float rawX, float rawY) {
                if(player.getGameMode() == GameMode.CREATIVE && type == MapClickType.RIGHT && player.isSneaking()) {
                    if(player.hasPermission("stevesseries.components.modify")) {
                        GUIManager.open(player, new ComponentModificationGUI(self));
                    } else {
                        player.sendMessage(ChatColor.RED + "Insufficient permissions");
                    }
                    return;
                }

                if(ComponentLocation.isValid(self.targetComponent, ActivatableComponent.class)) {
                    ActivatableComponent component = (ActivatableComponent) ComponentManager.getComponentAt(self.targetComponent);
                    component.setActivated(!component.isActivated());
                }
            }
        });
    }

    @Override
    public void writeToJson(JsonObject object) {
        super.writeToJson(object);

        if(this.targetComponent != null) {
            object.add("targetComponent", this.targetComponent.saveToJson());
        }
    }

    @Override
    public void readFromJson(JsonObject object) {
        super.readFromJson(object);

        if(object.has("targetComponent")) {
            this.targetComponent = ComponentLocation.loadFromJson(object.getAsJsonObject("targetComponent"));
        }
    }

    private final List<ConfigurationElement> configuration = Collections.singletonList(new ComponentConfigurationElement("Activatable component", () -> this.targetComponent, targetComponent -> this.targetComponent = targetComponent));
    @Override
    public Collection<ConfigurationElement> getConfiguration() {
        return this.configuration;
    }
}
