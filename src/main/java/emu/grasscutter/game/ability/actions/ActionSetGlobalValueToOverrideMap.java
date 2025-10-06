package emu.grasscutter.game.ability.actions;

import com.google.protobuf.ByteString;
import emu.grasscutter.Grasscutter;
import emu.grasscutter.data.binout.AbilityModifier.AbilityModifierAction;
import emu.grasscutter.game.ability.Ability;
import emu.grasscutter.game.entity.*;
import emu.grasscutter.server.packet.send.PacketServerGlobalValueChangeNotify;

@AbilityAction(AbilityModifierAction.Type.SetGlobalValueToOverrideMap)
public final class ActionSetGlobalValueToOverrideMap extends AbilityActionHandler {
    @Override
    public boolean execute(
            Ability ability, AbilityModifierAction action, ByteString abilityData, GameEntity target) {
        // TODO:
        GameEntity entity = target;
        if (action.isFromOwner) {
            if (target instanceof EntityClientGadget) {
                EntityClientGadget gadget = (EntityClientGadget)target;
                entity = entity.getScene().getEntityById(gadget.getOwnerEntityId());
            } else if (target instanceof EntityGadget) {
                EntityGadget gadget = (EntityGadget)target;
                entity = gadget.getOwner();
            }
        }
        String globalValueKey = action.globalValueKey;
        String abilityFormula = action.abilityFormula;
        if (!entity.getGlobalAbilityValues().containsKey(globalValueKey)) {
            Grasscutter.getLogger().trace("Action does not contains {} global key", (Object)globalValueKey);
            return true;
        }

        Float globalValue = entity.getGlobalAbilityValues().getOrDefault(globalValueKey, Float.valueOf(0.0f));
        if (abilityFormula.compareTo("DummyThrowSpeed") == 0) {
            globalValue = Float.valueOf(globalValue.floatValue() * 30.0f / ((float)Math.sin(0.9424778) * 100.0f) - 1.0f);
        }
        entity.getGlobalAbilityValues().put(globalValueKey, globalValue);
        ability.getAbilitySpecials().put(action.overrideMapKey, globalValue.floatValue());
        entity.onAbilityValueUpdate();
        entity.getScene().getHost().sendPacket(new PacketServerGlobalValueChangeNotify(entity, globalValueKey, globalValue.floatValue()));
        return true;
    }
}
