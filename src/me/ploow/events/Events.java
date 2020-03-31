package me.ploow.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.ploow.Main;
import me.ploow.core.VenderCore;
import me.ploow.utils.Config;
import me.ploow.utils.GUIUtils;
import me.ploow.utils.ItemUtils;
import me.ploow.utils.Settings;

public class Events implements Listener {
	private Main plugin = Main.getInstance();
	private Settings settings = new Settings();
	private GUIUtils guiUtils = new GUIUtils();
	private static Config info = new Config("info.yml");
	
	private void atualizarDados(Player p) {
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

		ItemStack skullPlayer = ItemUtils.getNamedSkull(p.getName(),
				plugin.getConfig().getString("gui.titulo_info").replace('&', '§'));
		ItemMeta skullMeta = skullPlayer.getItemMeta();
		List<String> skullColoredMeta = new ArrayList<String>();
		String modo = settings.hasNameList(p.getName(), settings.getShiftList()) ? "Shift"
				: settings.hasNameList(p.getName(), settings.getAutoList()) ? "Automático" : "Nenhum";
		for (String s : plugin.getConfig().getStringList("gui.lore_info")) {
			skullColoredMeta.add(((((ChatColor.translateAlternateColorCodes('&', s).replace("<valor_bolsa>",
					Integer.toString(Main.getBolsa()))).replace("<vendas>",
							Integer.toString(info.getInt(p.getName() + ".ValorVendas")))).replace("<itens>",
									VenderCore.format(info.getInt(p.getName() + ".ValorItensVendidos")))).replace(
											"<money>", VenderCore.format(info.getDouble(p.getName() + ".ValorMoney"))))
													.replace("<modo>", modo));
		}
		skullMeta.setLore(skullColoredMeta);
		skullPlayer.setItemMeta(skullMeta);

		guiUtils.getInventory().setItem(10, skullPlayer);
		guiUtils.getInventory().setItem(12, vendaShift);
		guiUtils.getInventory().setItem(14, vendaAutomatica);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void clickInventory(InventoryClickEvent e) {
		if (e.getWhoClicked() instanceof Player) {
			Player p = (Player)e.getWhoClicked();
			if (e.getInventory().getTitle().equals(plugin.getConfig().getString("gui.titulo_gui").replace('&', '§'))) {
				e.setCancelled(true);
				if (GUIUtils.get_slot(12, e)) { // Clicou em vender por shift
					if (!settings.hasNameList(p.getName(), settings.getShiftList())) {
						if (settings.hasNameList(p.getName(), settings.getAutoList())) {
							settings.removeFromList(p.getName(), settings.getAutoList());
						}
						settings.addInList(p.getName(), settings.getShiftList());
						p.sendMessage(plugin.getConfig().getString("mensagens.ativar_venda_shift").replace('&', '§'));
						p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
						atualizarDados(p);
						return;
					}
					settings.removeFromList(p.getName(), settings.getShiftList());
					p.sendMessage(plugin.getConfig().getString("mensagens.modo_de_venda_desativado").replace('&', '§'));
					p.playSound(p.getLocation(), Sound.ITEM_PICKUP, 0.5F, 0.5F);
					atualizarDados(p);
					return;
				}
				if (GUIUtils.get_slot(14, e)) {
					if (!settings.hasNameList(p.getName(), settings.getAutoList())) {
						if (settings.hasNameList(p.getName(), settings.getShiftList())) {
							settings.removeFromList(p.getName(), settings.getShiftList());
						}
						settings.addInList(p.getName(), settings.getAutoList());
						p.sendMessage(plugin.getConfig().getString("mensagens.ativar_venda_auto").replace('&', '§'));
						p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1F, 1F);
						atualizarDados(p);
						return;
					}
					settings.removeFromList(p.getName(), settings.getAutoList());
					p.sendMessage(plugin.getConfig().getString("mensagens.modo_de_venda_desativado").replace('&', '§'));
					p.playSound(p.getLocation(), Sound.ITEM_PICKUP, 0.5F, 0.5F);
					atualizarDados(p);
					return;
				}
				if (GUIUtils.get_slot(16, e)) {
					for (String x : plugin.getConfig().getStringList("itens")) {
						String[] stringSplited = x.split(";");
						int item_id = Integer.parseInt(stringSplited[0]);
						int item_data = Integer.parseInt(stringSplited[1]);
						double item_value = Double.parseDouble(stringSplited[2]);
						VenderCore.venderItens(p, new ItemStack(item_id, (short)item_data), item_value, Main.getBolsa(), true);
					}
					p.closeInventory();
				}
			}
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (!info.contains(p.getName())) {
			info.set(p.getName() + ".ValorVendas", 0);
			info.set(p.getName() + ".ValorItensVendidos", 0);
			info.set(p.getName() + ".ValorMoney", "");
			info.saveConfig();
		}
	}

	public static Config getInfo() {
		return info;
	}
}
