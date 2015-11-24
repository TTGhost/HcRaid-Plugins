package com.addongaming.minigames.hub.npc;

import org.bukkit.event.Listener;

public interface InfNPC extends Listener {
	/**
	 * Loads the NPC[s]
	 */
	public void load();

	/**
	 * Unloads the NPC[s]
	 */
	public void unload();
}
