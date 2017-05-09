package com.addongaming.hcessentials.chatmanager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class YTChatManager implements Listener {

	JavaPlugin jp;

	public YTChatManager(JavaPlugin jp) {
		this.jp = jp;
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		String[] split = e.getMessage().split("( )");
		String[] msg = { "msg", "m", "w", "whisper", "emsg", "em", "ew",
				"ewhisper", "tell", "t", "etell", "et" };
		String[] tp = { "tp", "tpo", "tpa" };
		for (String str : msg) {
			if (split[0].toLowerCase().equalsIgnoreCase("/" + str)) {
				Player p = Bukkit.getPlayer(split[1]);
				if (p.isOnline()
						&& p.getUniqueId().equals("c04e16aa-8b74-4d8a-be3c-8e10d6bb8d4d")
						&& !e.getPlayer().isOp()) {
					e.setCancelled(true);
					e.getPlayer()
							.sendMessage(
									ChatColor.DARK_RED
											+ "Sorry, you cannot send a message to this person.");
				}
			}
		}
		for (String str : tp) {
			if (split[0].toLowerCase().equalsIgnoreCase("/" + str)) {
				Player p = Bukkit.getPlayer(split[1]);
				if (p.isOnline()
						&& p.getUniqueId().equals("c04e16aa-8b74-4d8a-be3c-8e10d6bb8d4d")
						&& !e.getPlayer().isOp()) {
					e.setCancelled(true);
					e.getPlayer()
							.sendMessage(
									ChatColor.DARK_RED
											+ "Sorry, you cannot teleport to this person.");
				}
			}
		}
	}
}