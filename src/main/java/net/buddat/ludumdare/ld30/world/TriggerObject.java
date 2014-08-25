package net.buddat.ludumdare.ld30.world;

import java.util.ArrayList;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class TriggerObject extends WorldObject {

	private final ArrayList<String> itemTriggersString;
	private final ArrayList<TriggerObject> triggerTriggers;

	private Image activeImage;

	private boolean isActivated = false;
	private boolean removeWhenActive = false;

	public TriggerObject(TiledMap parentMap, int groupId, int objectId) {
		super(parentMap, groupId, objectId);

		try {
			String objImageString = getProperty(WorldConstants.OBJPROP_ACTIVEIMAGE, null);
			activeImage = (objImageString != null ? new Image(objImageString) : null);
		} catch (SlickException e) {
			e.printStackTrace();
		}

		itemTriggersString = new ArrayList<String>();
		triggerTriggers = new ArrayList<TriggerObject>();

		WorldMap map = (WorldMap) parentMap;
		for (int i = 0; i < WorldConstants.TRIGGER_ITEMCOUNT; i++) {
			String itemTrigger = getProperty(WorldConstants.TRIGGER_ITEM + i, "null");
			if (!itemTrigger.equals("null")) {
				itemTriggersString.add(itemTrigger);
			}
		}

		for (int i = 0; i < WorldConstants.TRIGGER_TRIGGERCOUNT; i++) {
			String triggerTrigger = getProperty(WorldConstants.TRIGGER_TRIGGER + i, "null");
			if (!triggerTrigger.equals("null")) {
				triggerTriggers.add((TriggerObject) map.getObjectByName(triggerTrigger));
			}
		}

		String itemTrigger = getProperty(WorldConstants.TRIGGER_REMOVE, "false");
		if ("true".equals(itemTrigger))
			removeWhenActive = true;
	}

	public void update(int delta) {
		isActivated = true;

		ArrayList<String> removeItems = new ArrayList<String>();
		for (String s : itemTriggersString) {
			for (WorldObject obj : ((WorldMap) getParentMap())
					.getObjectList(WorldConstants.OBJGROUP_INTERACTIBLE)) {
				if (obj.isRemoved())
					continue;
				if (!obj.getObjName().equals(s))
					continue;

				if (obj.intersects(this)) {
					removeItems.add(s);
					obj.setRemoved(true);
					break;
				}
			}
		}

		itemTriggersString.removeAll(removeItems);
		if (!itemTriggersString.isEmpty())
			isActivated = false;

		if (isActivated)
			for (TriggerObject obj : triggerTriggers) {
				if (!obj.isActivated())
					isActivated = false;
			}

		if (isActivated)
			if (removeWhenActive)
				setRemoved(true);
	}

	public boolean isActivated() {
		return isActivated;
	}

	@Override
	public Image getObjImage() {
		if (isActivated && activeImage != null)
			return activeImage;

		return super.getObjImage();
	}

}
