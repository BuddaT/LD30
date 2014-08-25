package net.buddat.ludumdare.ld30.world;


public class WorldConstants {

	public static final int OBJGROUP_TELEPORT = 0;
	public static final int OBJGROUP_INTERACTIBLE = 1;
	public static final int OBJGROUP_TEXT = 2;
	public static final int OBJGROUP_TRIGGER = 3;
	public static final int OBJGROUP_MOB = 4;

	public static final String COLLISION_LAYER_NAME = "Collisions";
	public static final String EXIT_LAYER_NAME_STARTSWITH = "ExitActive";

	public static final String TELEPORT_ENTRY = "entry";
	public static final String TELEPORT_EXIT = "exit";

	public static final String TRIGGER_ITEM = "item";
	public static final String TRIGGER_TRIGGER = "trigger";
	public static final String TRIGGER_REMOVE = "remove";
	public static final int TRIGGER_ITEMCOUNT = 5;
	public static final int TRIGGER_TRIGGERCOUNT = 5;

	public static final int LAYERS_BELOW = 4;

	public static final String OBJPROP_IMAGE = "image";
	public static final String OBJPROP_ACTIVEIMAGE = "activeImage";
	public static final String OBJPROP_ATTRACTOR = "attractor";
	public static final String OBJPRO_SOUND = "sound";

	public static final String MOBPROP_ID = "id";

	public static final String HIGHLIGHT_IMAGE = "sprites/objects/highlight.png";

}
