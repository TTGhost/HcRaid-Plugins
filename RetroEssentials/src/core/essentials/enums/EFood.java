package core.essentials.enums;

import org.bukkit.Material;

import net.minecraft.server.v1_10_R1.DataWatcher.Item;

public enum EFood {
	apple("Apple", Material.APPLE, 4, (Material.APPLE.getId()), 1), bread("Bread", Material.BREAD, 5,
			(Material.BREAD.getId()),
			1), cookPork("Cooked Porkchop", Material.GRILLED_PORK, 8, (Material.GRILLED_PORK.getId()), 1), regPork(
					"Regular pork", Material.PORK, 3, (Material.PORK.getId()),
					1), cookie("Cookie", Material.COOKIE, 2, (Material.COOKIE.getId()), 8), mushsoup("Mushroom soup",
							Material.MUSHROOM_SOUP, 10, (Material.MUSHROOM_SOUP.getId()), 1), gapple("Golden Apple",
									Material.GOLDEN_APPLE, 20, (Material.GOLDEN_APPLE.getId()),
									1), fish("Fish", Material.RAW_FISH, 2, (Material.RAW_FISH.getId()), 1), cfish(
											"Fish", Material.COOKED_FISH, 5, (Material.COOKED_FISH.getId()), 1);
	private final String name;
	private final Material type;
	private final int health;
	private final int stackSize;

	public int getStackSize() {
		return stackSize;
	}

	EFood(String name, Material type, int health, Object itemClass, int stackSize) {
		this.name = name;
		this.type = type;
		this.health = health;
		this.itemClass = itemClass;
		this.stackSize = stackSize;
	}

	private final Object itemClass;

	public Object getItemClass() {
		return itemClass;
	}

	public String getName() {
		return name;
	}

	public Material getType() {
		return type;
	}

	public int getHealing() {
		return health;
	}

	public static boolean isFood(Material m) {
		for (EFood e : EFood.values()) {
			if (e.getType() == m)
				return true;
		}
		return false;
	}

	public static EFood getByName(Material byMat) {
		for (EFood e : EFood.values()) {
			if (e.getType() == byMat) {
				return e;
			}
		}
		return null;
	}
}
