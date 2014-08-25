package net.buddat.ludumdare.ld30.world.entity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Basic enumeration of mob types
 */
public enum EntityType {
	HORN_DEMON(0),
	SKULL_FACE(1),
	FIRE_FACE(2),
	CLAWED_BITER(3);
	private final int typeId;

	private static final Map<Integer, EntityType> types;

	static {
		HashMap<Integer, EntityType> newTypes = new HashMap<>();
		for (EntityType type : EntityType.values()) {
			newTypes.put(type.typeId, type);
		}
		types = Collections.unmodifiableMap(newTypes);
	}


	private EntityType(int typeId) {
		this.typeId = typeId;
	}

	public static EntityType forTypeId(int typeId) {
		return types.get(typeId);
	}
}
