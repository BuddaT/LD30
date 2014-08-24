package net.buddat.ludumdare.ld30.world;

import java.util.ArrayList;

import org.newdawn.slick.tiled.TiledMap;

public class TriggerObject extends WorldObject {

	private final ArrayList<WorldObject> itemTriggers;
	private final ArrayList<TriggerObject> triggerTriggers;

	private boolean isActivated = false;

	public TriggerObject(TiledMap parentMap, int groupId, int objectId) {
		super(parentMap, groupId, objectId);

		itemTriggers = new ArrayList<WorldObject>();
		triggerTriggers = new ArrayList<TriggerObject>();

		WorldMap map = (WorldMap) parentMap;
		for (int i = 0; i < WorldConstants.TRIGGER_ITEMCOUNT; i++) {
			String itemTrigger = getProperty(WorldConstants.TRIGGER_ITEM + i, "null");
			if (!itemTrigger.equals("null")) {
				itemTriggers.add(map.getObjectByName(itemTrigger));
			}
		}

		for (int i = 0; i < WorldConstants.TRIGGER_TRIGGERCOUNT; i++) {
			String triggerTrigger = getProperty(WorldConstants.TRIGGER_TRIGGER + i, "null");
			if (!triggerTrigger.equals("null")) {
				triggerTriggers.add((TriggerObject) map.getObjectByName(triggerTrigger));
			}
		}
	}

	public void update(int delta) {
		isActivated = true;

		for (WorldObject obj : itemTriggers) {
			if (!obj.intersects(this))
				isActivated = false;
		}

		if (isActivated)
			for (TriggerObject obj : triggerTriggers) {
				if (!obj.isActivated())
					isActivated = false;
			}
	}

	public boolean isActivated() {
		return isActivated;
	}

}
