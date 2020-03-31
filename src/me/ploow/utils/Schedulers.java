package me.ploow.utils;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.ploow.Main;
import me.ploow.core.VenderCore;

public class Schedulers {
	private static Settings settings = new Settings();
	private static Main plugin = Main.getInstance();

	public static void iniciarBolsa() {
		new BukkitRunnable() {
			@SuppressWarnings("static-access")
			@Override
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {
					p.playSound(p.getLocation(), Sound.LEVEL_UP, 0.6F, 0.6F);
				}
				plugin.setBolsa(new Random().nextInt(99) + 1);;
				for (String s : plugin.getConfig().getStringList("mensagens.mensagem_bolsa_alterada")) {
					Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', s).replace("<valor_bolsa>", Integer.toString(plugin.getBolsa())));
				}
			}
		}.runTaskTimerAsynchronously(plugin, 20L, 20 * plugin.getConfig().getInt("configs.tempo_troca_bolsa")); // 30 é os segundos
	}
	
	public static void iniciarVendaAutomática() {
		new BukkitRunnable() {
			@SuppressWarnings({ "deprecation", "static-access" })
			@Override
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (settings.hasNameList(p.getName(), settings.getAutoList())) {
						for (String x : plugin.getConfig().getStringList("itens")) {
							String[] stringSplited = x.split(";");
							int item_id = Integer.parseInt(stringSplited[0]);
							int item_data = Integer.parseInt(stringSplited[1]);
							double item_value = Double.parseDouble(stringSplited[2]);
							VenderCore.venderItens(p, new ItemStack(item_id, (short)item_data), item_value, plugin.getBolsa(), false);
						}
					}
				}
			}
		}.runTaskTimerAsynchronously(plugin, 0L, 20 * plugin.getConfig().getInt("configs.tempo_venda_auto"));
	}
}
