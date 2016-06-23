package com.addongaming.minigames.management.scheduling;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_10_R1.DataWatcherObject;
import net.minecraft.server.v1_10_R1.DataWatcherRegistry;

public class ArrowClearer implements Runnable {


	@Override
	public void run() {
		for (Player player : Bukkit.getOnlinePlayers())
			((CraftPlayer)player).getHandle().getDataWatcher().set(new DataWatcherObject<>(10, DataWatcherRegistry.b),0);
	}

}
