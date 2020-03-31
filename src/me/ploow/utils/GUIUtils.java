package me.ploow.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.ploow.Main;
import me.ploow.core.VenderCore;
import me.ploow.events.Events;

public class GUIUtils extends Settings {
	private static Main plugin = Main.getInstance();

	private static Settings settings = new Settings();

	private static Inventory inv;

	public static void criarGui(Player p) {
		inv = Bukkit.createInventory(null, 3 * 9, plugin.getConfig().getString("gui.titulo_gui").replace('&', '§'));

		ItemStack bolsaItem = ItemUtils.createItem(175,
				plugin.getConfig().getString("gui.titulo_bolsa").replace('&', '§'), 0);
		ItemMeta bolsaMeta = bolsaItem.getItemMeta();
		List<String> coloredLoreBolsa = new ArrayList<String>();
		for (String s : plugin.getConfig().getStringList("gui.lore_bolsa")) {
			coloredLoreBolsa.add(ChatColor.translateAlternateColorCodes('&', s).replace("<valor_bolsa>",
					Integer.toString(Main.getBolsa())));
		}
		bolsaMeta.setLore(coloredLoreBolsa);
		bolsaItem.setItemMeta(bolsaMeta);

		ItemStack skullPlayer = ItemUtils.getNamedSkull(p.getName(),
				plugin.getConfig().getString("gui.titulo_info").replace('&', '§'));
		ItemMeta skullMeta = skullPlayer.getItemMeta();
		List<String> skullColoredMeta = new ArrayList<String>();
		String modo = settings.hasNameList(p.getName(), settings.getShiftList()) ? "Shift" : settings.hasNameList(p.getName(), settings.getAutoList()) ? "Automático" : "Nenhum";
		for (String s : plugin.getConfig().getStringList("gui.lore_info")) {
			skullColoredMeta.add(((((ChatColor.translateAlternateColorCodes('&', s)
					.replace("<valor_bolsa>", Integer.toString(Main.getBolsa())))
					.replace("<vendas>", Integer.toString(Events.getInfo().getInt(p.getName() + ".ValorVendas"))))
					.replace("<itens>", VenderCore.format(Events.getInfo().getDouble(p.getName() + ".ValorItensVendidos"))))
					.replace("<money>", VenderCore.format(Events.getInfo().getDouble(p.getName() + ".ValorMoney"))))
					.replace("<modo>", modo));
		}
		skullMeta.setLore(skullColoredMeta);
		skullPlayer.setItemMeta(skullMeta);

		ItemStack vendaShift = ItemUtils.createItem(351,
				plugin.getConfig().getString("gui.titulo_shift").replace('&', '§'),
				settings.hasNameList(p.getName(), settings.getShiftList()) ? 10 : 8);
		ItemMeta vendaShiftMeta = vendaShift.getItemMeta();
		List<String> coloredLoreShift = new ArrayList<String>();
		for (String s : plugin.getConfig().getStringList("gui.lore_shift")) {
			coloredLoreShift.add(ChatColor.translateAlternateColorCodes('&', s).replace("<shift_bool>",
					settings.hasNameList(p.getName(), settings.getShiftList()) ? "Ativada" : "Desativada"));
		}
		vendaShiftMeta.setLore(coloredLoreShift);
		vendaShift.setItemMeta(vendaShiftMeta);

		ItemStack vendaAutomatica = ItemUtils.createItem(351,
				plugin.getConfig().getString("gui.titulo_auto").replace('&', '§'),
				settings.hasNameList(p.getName(), settings.getAutoList()) ? 10 : 8);
		ItemMeta vendaAutoMeta = vendaAutomatica.getItemMeta();
		List<String> coloredLoreAuto = new ArrayList<String>();
		for (String s : plugin.getConfig().getStringList("gui.lore_auto")) {
			coloredLoreAuto.add(ChatColor.translateAlternateColorCodes('&', s).replace("<auto_bool>",
					settings.hasNameList(p.getName(), settings.getAutoList()) ? "Ativada" : "Desativada"));
		}
		vendaAutoMeta.setLore(coloredLoreAuto);
		vendaAutomatica.setItemMeta(vendaAutoMeta);

		ArrayList<String> venderLore = new ArrayList<String>();
		venderLore.add("");
		venderLore.add("§7Clique para vender todos seus itens disponíveis");
		ItemStack venderItens = ItemUtils.createItem(160, "§eVender tudo", venderLore, 5);

		inv.setItem(10, skullPlayer);
		inv.setItem(12, vendaShift);
		inv.setItem(13, bolsaItem);
		inv.setItem(14, vendaAutomatica);
		inv.setItem(16, venderItens);
		p.openInventory(inv);
	}
	
	public Inventory getInventory() {
		return inv;
	}
	
	public static Boolean get_slot(int slot, InventoryClickEvent e) {
		if (e.getSlot() == slot) {
			return true;
		} else {
			return false;
		}
	}
}
