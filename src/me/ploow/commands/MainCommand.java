package me.ploow.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ploow.Main;
import me.ploow.utils.GUIUtils;
import me.ploow.utils.Settings;

public class MainCommand extends Settings implements CommandExecutor {

	private static Main plugin = Main.getInstance();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			Bukkit.getConsoleSender().sendMessage("§aVocê não é um player");
			return true;
		}
		Player p = (Player) sender;

		if (cmd.getName().equalsIgnoreCase("vender")) {
			if (!(args.length == 0)) {
				return false;
			}
			GUIUtils.criarGui(p);
		}
		if (cmd.getName().equalsIgnoreCase("bolsa")) {
			if (args.length == 0) {
				for (String x : plugin.getConfig().getStringList("mensagens.mensagem_bolsa_comando")) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', x).replace("<valor_bolsa>",
							Integer.toString(Main.getBolsa())));
				}
			} else if (args.length > 0) {
				if (args[0].equalsIgnoreCase("definir")) {
					if (args.length == 1) {
						p.sendMessage("§cUso incorreto, utilize /bolsa definir <valor>");
						return false;
					}
					if (!p.hasPermission("pvender.definirbolsa")) {
						p.sendMessage("§cSem permissão!");
						return false;
					}
					if (!isNumeric(args[1]) && !(Integer.parseInt(args[1]) > 0)
							&& !(Integer.parseInt(args[1]) <= 100)) {
						p.sendMessage("§cNúmero inválido!");
						return false;
					}
					Main.setBolsa(Integer.parseInt(args[1]));
					for (String x : plugin.getConfig().getStringList("mensagens.mensagem_bolsa_alterada_por_player")) {
						Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', x)
								.replace("<valor_bolsa>", Integer.toString(Main.getBolsa()))
								.replace("<player>", p.getName()));
					}
					sender.sendMessage("§aAlterado com sucesso.");
				}
			}
		}
		return false;
	}
}
