package emu.grasscutter.game.ability.actions;

import com.google.protobuf.ByteString;
import emu.grasscutter.data.binout.AbilityModifier.AbilityModifierAction;
import emu.grasscutter.game.ability.Ability;
import emu.grasscutter.data.common.DynamicFloat;
import emu.grasscutter.game.entity.GameEntity;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import emu.grasscutter.Grasscutter;
import emu.grasscutter.game.props.FightProperty;
import emu.grasscutter.server.packet.send.PacketServerGlobalValueChangeNotify;

@AbilityAction(AbilityModifierAction.Type.AddGlobalValue)
public final class ActionAddGlobalValue extends AbilityActionHandler {
    @Override
    public boolean execute(
            Ability ability, AbilityModifierAction action, ByteString abilityData, GameEntity target) {
        var owner = ability.getOwner();
        var properties = new Object2FloatOpenHashMap<String>();

        for (var property : FightProperty.values()) {
  
   var name = property.name();
            var value = owner.getFightProperty(property);
            properties.put(name, value);
        }

        properties.putAll(ability.getAbilitySpecials());
        String valueKey = action.key;
        float valueToAdd = action.ratio.get(properties, 0f);
        float maxValue = action.maxValue.get(properties, 0f);
        float minValue = action.minValue.get(properties, 0f);

        float currentGlobalValue = target.getGlobalAbilityValues().getOrDefault(valueKey, 0f);

        float newValue = currentGlobalValue + valueToAdd;
        if (newValue > maxValue) {
            newValue = maxValue;
        }
        if (newValue < minValue) {
            newValue = minValue;
        }

        target.getGlobalAbilityValues().put(valueKey, newValue);
        Grasscutter.getLogger().info("Global value {} updated to {}", valueKey, newValue);

        target.onAbilityValueUpdate();
        target
                .getScene()
                .getHost()
                .sendPacket(new PacketServerGlobalValueChangeNotify(target, valueKey, newValue));

        return true;
    }
}