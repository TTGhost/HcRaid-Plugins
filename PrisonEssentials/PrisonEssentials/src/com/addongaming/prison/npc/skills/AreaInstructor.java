package com.addongaming.prison.npc.skills;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCDespawnEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.addongaming.hcessentials.utils.Utils;
import com.addongaming.prison.classes.PlayerClasses;
import com.addongaming.prison.data.utilities.AreaData;
import com.addongaming.prison.npc.InfNPC;
import com.addongaming.prison.npc.NPCData;
import com.addongaming.prison.player.Prisoner;
import com.addongaming.prison.player.PrisonerManager;
import com.addongaming.prison.prison.Prison;
import com.addongaming.prison.prison.PrisonManager;

public class AreaInstructor implements InfNPC {

	private JavaPlugin jp;
	List<NPCData> npcList = new ArrayList<NPCData>();

	public AreaInstructor(JavaPlugin jp) {
		this.jp = jp;
		setupConfig(jp.getConfig());
	}

	private void createNpc(String str) {
		int value = jp.getConfig().getInt(str + ".maxLevel");
		Location loc = Utils.loadLoc(jp.getConfig()
				.getString(str + ".location"));
		NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.VILLAGER,
				"Area Teacher");
		npc.setProtected(true);
		npc.spawn(loc);
		Entity entity = npc.getEntity();
		npcList.add(new NPCData(npc.getId(), value, loc.getWorld()));
		Villager vill = (Villager) entity;
		vill.setCustomName("Area Teacher");
		vill.setCustomNameVisible(true);
		vill.setAdult();
		vill.setProfession(Profession.LIBRARIAN);
		vill.setMaxHealth(Double.MAX_VALUE);
	}

	@EventHandler
	public void itemClick(InventoryClickEvent event) {
		if (event.getView().getTopInventory().getTitle()
				.startsWith(ChatColor.GOLD + "Char")) {
			Prisoner prisoner = PrisonerManager.getInstance().getPrisonerInfo(
					event.getWhoClicked().getName());
			event.setCancelled(true);
			if (event.getCurrentItem() != null
					&& event.getCurrentItem().getType() != Material.AIR) {
				ItemStack is = event.getCurrentItem();
				String name = is.getItemMeta().getDisplayName();
				AreaData md = AreaData.getAreaByMaterial(event.getCurrentItem()
						.getType());
				if (name.startsWith(ChatColor.RED + "")) {
					messagePlayer((Player) event.getWhoClicked(),
							"Sorry, I cannot teach you this.");
					event.getWhoClicked().closeInventory();
					return;
				}
				if (name.startsWith(ChatColor.STRIKETHROUGH + "")) {
					messagePlayer((Player) event.getWhoClicked(),
							"Sorry, you need to visit a higher levelled teacher.");
					event.getWhoClicked().closeInventory();
					return;
				}
				if (name.startsWith(ChatColor.GREEN + "")) {
					String strt = (is.getItemMeta().getLore().get(is
							.getItemMeta().getLore().size() - 1));
					int cost = Integer.parseInt(strt.substring(1));
					if (prisoner.hasBalance(cost)) {
						messagePlayer((Player) event.getWhoClicked(),
								"Congratulations " + prisoner.getName()
										+ " you can now enter " + md.getName()
										+ " on island " + md.getPrison() + "!");
						prisoner.addPermission(md.getPermission());
						event.getWhoClicked().closeInventory();
						prisoner.removeBalance(cost);
						return;
					} else {
						messagePlayer((Player) event.getWhoClicked(),
								"Sorry, you do not have enough money.");
						event.getWhoClicked().closeInventory();
						return;
					}
				}
				return;

			}
		}
	}

	@Override
	public void load() {
		FileConfiguration fc = jp.getConfig();
		for (String s : fc.getConfigurationSection("npcs.instructors.area")
				.getKeys(false))
			createNpc("npcs.instructors.area." + s);

	}

	private void messagePlayer(Player p, String message) {
		p.sendMessage(ChatColor.AQUA + "Area Instructor" + ChatColor.RESET
				+ "> " + message);
	}

	@EventHandler
	public void npcClick(NPCRightClickEvent event) {
		for (NPCData nd : npcList)
			if (nd.getId() == event.getNPC().getId())
				openInventory(nd, event.getClicker());
	}

	@EventHandler
	public void npcDespawn(NPCDespawnEvent event) {
		for (NPCData nd : this.npcList)
			if (event.getNPC().getId() == nd.getId()) {
				event.setCancelled(true);
				return;
			}
	}

	private void openInventory(NPCData nd, Player play) {
		Prisoner prisoner = PrisonerManager.getInstance().getPrisonerInfo(
				play.getName());
		Inventory i = Bukkit.createInventory(
				null,
				9 * 2,
				ChatColor.GOLD
						+ "Char Level: "
						+ PlayerClasses.getLevel(prisoner.getCharacterExp())
						+ ChatColor.RESET
						+ " - "
						+ new DecimalFormat("########.#").format(prisoner
								.getBalance()) + " R");
		List<ItemStack> cmds = new ArrayList<ItemStack>();
		Prison prison = PrisonManager.getInstance().getPrison(play);
		for (AreaData md : AreaData.values()) {
			if (md.getShopid() == null)
				continue;
			if (!md.getPrison().equalsIgnoreCase(prison.getName()))
				continue;
			ItemStack cmd = new ItemStack(md.getShopid());
			if (md.getCharLevel() <= nd.getValue()) {
				if (prisoner.hasPermission(md.getPermission()))
					cmd = Utils.setLore(
							Utils.setName(ChatColor.RED + md.getName(), cmd),
							ChatColor.GOLD + "Island: " + md.getPrison(),
							"You can already enter", "this area");
				else if (PlayerClasses.getLevel(prisoner.getCharacterExp()) < md
						.getCharLevel())
					cmd = Utils.setLore(
							Utils.setName(ChatColor.RED + md.getName(), cmd),
							ChatColor.GOLD + "Island: " + md.getPrison(),
							"You need a character",
							"level of " + md.getCharLevel() + ".");
				else
					cmd = Utils.setLore(
							Utils.setName(ChatColor.GREEN + md.getName(), cmd),
							ChatColor.GOLD + "Island: " + md.getPrison(),
							"You can buy the permission",
							"to enter " + md.getName() + " for ",
							"$" + md.getCost());
			} else {
				cmd = Utils.setLore(Utils.setName(
						ChatColor.STRIKETHROUGH + md.getName(), cmd),
						ChatColor.GOLD + "Island: " + md.getPrison(),
						"Sorry, you need to", "go to a higher",
						"level teacher.");
			}
			cmds.add(cmd);
		}
		int counter = 0;
		for (ItemStack is : cmds) {
			i.setItem(counter++, is);
		}
		play.openInventory(i);
	}

	private void setupConfig(final FileConfiguration fc) {
		Location temp = new Location(Bukkit.getWorld("world"), 0, 0, 0);
		fc.addDefault("npcs.instructors.area.npc1.location",
				Utils.locationToSaveString(temp));
		fc.addDefault("npcs.instructors.area.npc1.maxLevel", 20);
		fc.options().copyDefaults(true);
		jp.saveConfig();
	}

	@Override
	public void unload() {
		for (NPCData nd : npcList)
			CitizensAPI.getNPCRegistry().getById(nd.getId()).destroy();
	}
}
