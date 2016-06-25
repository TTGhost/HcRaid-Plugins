package com.hamgooof.bedrockbase.worldedit;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.primesoft.asyncworldedit.worldedit.AsyncCuboidClipboard;
import org.primesoft.asyncworldedit.worldedit.AsyncEditSessionFactory;
import org.primesoft.asyncworldedit.worldedit.ThreadSafeEditSession;

import com.hamgooof.bedrockbase.core.BBPlugin;
import com.hamgooof.bedrockbase.objects.BedrockSchematic;
import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.data.DataException;

public class PastingRunnable implements Runnable {
	private final BedrockSchematic schematic;
	private final int[] coords;
	private String name;
	private WorldEditPlugin wep;

	public PastingRunnable(String name, BedrockSchematic schematic, int[] coords) {
		this.name = name;
		this.schematic = schematic;
		this.coords = coords;
	}

	@Override
	public void run() {
		try {
			loadArea(Bukkit.getWorld(BBPlugin.world), schematic.getSchematic(),
					new Vector(coords[0], coords[1], coords[2]));
		} catch (MaxChangedBlocksException | DataException | IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	private void loadArea(World world, File file, Vector origin)
			throws DataException, IOException, MaxChangedBlocksException {
		AsyncEditSessionFactory factory = (AsyncEditSessionFactory) wep.getWorldEdit().getInstance()
				.getEditSessionFactory();
		ThreadSafeEditSession es = factory.getThreadSafeEditSession(new BukkitWorld(world), 999999999);
		AsyncCuboidClipboard cc;
		try {
			cc = new AsyncCuboidClipboard(es.getPlayer(),
					CuboidClipboard.loadSchematic(new File(file, name + "|" + schematic.getName())));
			cc.paste(es, origin, false);
		} catch (com.sk89q.worldedit.world.DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
