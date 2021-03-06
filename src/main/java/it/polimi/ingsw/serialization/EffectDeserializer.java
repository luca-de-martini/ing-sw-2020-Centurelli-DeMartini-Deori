package it.polimi.ingsw.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import it.polimi.ingsw.model.action.Effect;
import it.polimi.ingsw.model.action.Effects;

import java.lang.reflect.Type;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map;

/**
 * Json deserialization adapter for {@link Effect}
 */
class EffectDeserializer implements JsonDeserializer<Effect> {

    enum EffectId {
        MOVE,
        BUILD_BLOCK,
        BUILD_DOME,
        SWAP_PAWNS,
        FERRY,
        PUSH_PAWN,
        FORBID_BUILD_CLOSE,
        FORBID_MOVE_DOWN,
        FORBID_MOVE_UP,
        FORBID_MOVE_BACK,
        FORBID_5_TOWERS_LOSS,
        FORBID_BUILD_AT_COORDINATE,
        FORBID_BUILD_AT_OTHER_COORDINATES,
        WIN_ON_JUMP_DOWN,
        WIN_ON_5_TOWERS
    }

    private final Map<EffectId, Effect> map = Map.ofEntries(
        new SimpleImmutableEntry<>(EffectId.MOVE, Effects.move),
        new SimpleImmutableEntry<>(EffectId.FORBID_BUILD_CLOSE, Effects.forbidBuildClose),
        new SimpleImmutableEntry<>(EffectId.BUILD_BLOCK, Effects.buildBlock),
        new SimpleImmutableEntry<>(EffectId.BUILD_DOME, Effects.buildDome),
        new SimpleImmutableEntry<>(EffectId.SWAP_PAWNS, Effects.swapPawns),
        new SimpleImmutableEntry<>(EffectId.FERRY, Effects.ferry),
        new SimpleImmutableEntry<>(EffectId.PUSH_PAWN, Effects.pushPawn),
        new SimpleImmutableEntry<>(EffectId.FORBID_MOVE_DOWN, Effects.forbidMoveDown),
        new SimpleImmutableEntry<>(EffectId.FORBID_MOVE_UP, Effects.forbidMoveUp),
        new SimpleImmutableEntry<>(EffectId.FORBID_MOVE_BACK, Effects.forbidMoveBack),
        new SimpleImmutableEntry<>(EffectId.FORBID_5_TOWERS_LOSS, Effects.forbid5TowersLoss),
        new SimpleImmutableEntry<>(EffectId.FORBID_BUILD_AT_COORDINATE, Effects.forbidBuildAtCoordinate),
        new SimpleImmutableEntry<>(EffectId.FORBID_BUILD_AT_OTHER_COORDINATES, Effects.forbidBuildAtOtherCoordinates),
        new SimpleImmutableEntry<>(EffectId.WIN_ON_JUMP_DOWN, Effects.winOnJumpDown),
        new SimpleImmutableEntry<>(EffectId.WIN_ON_5_TOWERS, Effects.winOn5Towers)
    );

    private Effect getEffectFromId(EffectId id) {
        Effect effect = map.get(id);
        if (effect != null)
            return effect;

        throw new IllegalStateException();
    }

    @Override
    public Effect deserialize(JsonElement jsonElement, Type type,
                              JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        EffectId id = jsonDeserializationContext.deserialize(jsonElement, EffectId.class);
        return getEffectFromId(id);
    }
}
