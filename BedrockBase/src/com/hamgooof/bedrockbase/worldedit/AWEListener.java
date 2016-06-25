package com.hamgooof.bedrockbase.worldedit;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.primesoft.asyncworldedit.blockPlacer.BlockPlacerEntry;
import org.primesoft.asyncworldedit.blockPlacer.entries.JobEntry;
import org.primesoft.asyncworldedit.api.blockPlacer.IBlockPlacerListener;

import com.hamgooof.bedrockbase.core.BBPlugin;
import com.hamgooof.bedrockbase.core.PlayerHandler;
import com.hamgooof.bedrockbase.objects.BedrockSchematic;

public class AWEListener implements IBlockPlacerListener {
	private static List<String> pastingSchematics = new ArrayList<String>();
	private final PlayerHandler playerHandler;

	public AWEListener(PlayerHandler playerHandler) {
		super();
		this.playerHandler = playerHandler;
	}

	public synchronized static boolean isPasting(UUID player) {
		for (String str : pastingSchematics)
			if (str.startsWith(player.toString()))
				return true;
		return false;
	}

	public synchronized static void addPlayer(UUID player, BedrockSchematic schematic) {
		pastingSchematics.add(player.toString() + schematic.getName());
	}

	@Override
	public void jobAdded(JobEntry arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void jobRemoved(JobEntry arg0) {
		try {
			Field f = BlockPlacerEntry.class.getDeclaredField("m_player");
			f.setAccessible(true);
			String[] split = ((String) f.get(arg0)).split("[|]");
			if (split.length == 2) {
				playerHandler.setUsed(split[1], UUID.fromString(split[0]));
				pastingSchematics.remove(split[0] + split[1]);
				Player p = Bukkit.getPlayer(UUID.fromString(split[0]));
				if (p != null && p.isOnline()) {
					if (!isPasting(p.getUniqueId())) {
						p.sendMessage(BBPlugin.title + "You can now return to your base. /bb");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
