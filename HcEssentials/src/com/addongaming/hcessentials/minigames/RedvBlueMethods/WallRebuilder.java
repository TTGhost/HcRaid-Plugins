package com.addongaming.hcessentials.minigames.RedvBlueMethods;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.material.Wool;
import org.bukkit.plugin.java.JavaPlugin;

public class WallRebuilder {

	JavaPlugin jp;
	private ScheduledWallRemover instance;

	public WallRebuilder(JavaPlugin jp) {
		instance = new ScheduledWallRemover(jp);
		this.jp = jp;
	}

	public void WallRebuild() {
		for (Block b : instance.getRedWall().getAllBlocks()) {
			b.setType(Material.WOOL);
			Wool wool = (Wool) b;
			wool.setColor(DyeColor.RED);
		}
		for (Block b : instance.getBlueWall().getAllBlocks()) {
			b.setType(Material.WOOL);
			Wool wool = (Wool) b;
			wool.setColor(DyeColor.BLUE);
		}

	}

}
