package me.ploow.core;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.ploow.Main;
import me.ploow.events.Events;

public class VenderCore {
	
	private static Main plugin = Main.getInstance();
	
	public static int getAmount(final Inventory inventory, final ItemStack item) {
		int count = 0;
		final ItemStack[] items = inventory.getContents();
		for (int i = 0; i < items.length; ++i) {
			if (items[i] != null && items[i].getType() == item.getType()
					&& items[i].getDurability() == item.getDurability()) {
				count += items[i].getAmount();
			}
		}
		return count;
	}

	public static void removeItems(Inventory inventory, Material type, int amount) {
		if (amount <= 0)
			return;
		int size = inventory.getSize();
		for (int slot = 0; slot < size; slot++) {
			ItemStack is = inventory.getItem(slot);
			if (is == null)
				continue;
			if (type == is.getType()) {
				int newAmount = is.getAmount() - amount;
				if (newAmount > 0) {
					is.setAmount(newAmount);
					break;
				} else {
					inventory.clear(slot);
					amount = -newAmount;
					if (amount == 0)
						break;
				}
			}
		}
	}

	private static final char[] c = new char[] { 'K', 'M', 'B', 'T' };

	public static String format(double value) {
		return format(value, 0);
	}

	private static String format(double value, int iteration) {
		if (iteration == 0 && value < 1000)
			return Double.toString((long) (value * 100) / 100D);

		double f = ((long) value / 100) / 10.0D;
		return f < 1000 || iteration >= c.length - 1 ? f + "" + c[iteration] : format(f, ++iteration);
	}

	static List<Player> vez = new ArrayList<>();
	
	@SuppressWarnings("deprecation")
	public static void venderItens(Player p, ItemStack m, double preco, double pc, Boolean showMsgInChat) {
		if (getAmount(p.getInventory(), m) > 0) {
			int total = getAmount(p.getInventory(), m) * 1;
			double dinheiro = (getAmount(p.getInventory(), m) * preco) / 100 * pc;
			p.sendMessage("§cVocê vendeu " + total + " itens por um preço de " + format(dinheiro) + " de coins.");
			Events.getInfo().set(p.getName() + ".ValorVendas", Events.getInfo().getInt(p.getName() + ".ValorVendas") + 1);
			Events.getInfo().set(p.getName() + ".ValorItensVendidos", Events.getInfo().getDouble(p.getName() + ".ValorItensVendidos") + total);
			Events.getInfo().set(p.getName() + ".ValorMoney", Events.getInfo().getDouble(p.getName() + ".ValorMoney") + dinheiro);
			Events.getInfo().saveConfig();
			removeItems(p.getInventory(), m.getType(), getAmount(p.getInventory(), m));
			plugin.getEconomy().depositPlayer(p.getName(), dinheiro);
		} else {
			if (showMsgInChat == true) {
				if (!vez.contains(p)) {
					p.sendMessage(plugin.getConfig().getString("mensagens.sem_itens").replace('&', '§'));
					vez.add(p);
					new BukkitRunnable() {
						@Override
						public void run() {
							if (vez.contains(p)) {
								vez.remove(p);
							}
						}
					}.runTaskLater(Main.getPlugin(Main.class), 60L);
				}
			}
		}
	}
}