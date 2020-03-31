package me.ploow;

import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import me.ploow.commands.MainCommand;
import me.ploow.events.Events;
import me.ploow.events.ShiftEvent;
import me.ploow.utils.Schedulers;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {

	private static int porcentagem = new Random().nextInt(99) + 1;
		
	@Override
	public void onEnable() {
		loadHooks();
		Schedulers.iniciarVendaAutomática();
		Schedulers.iniciarBolsa();
		loadCommands("vender", new MainCommand());
		loadCommands("bolsa", new MainCommand());
		loadEvents(new Events());
		loadEvents(new ShiftEvent());	
		saveDefaultConfig();
		Bukkit.getConsoleSender().sendMessage("§aPVender Ligado §f- §bv1.0");
	}
	
	private void loadCommands(String cmd, CommandExecutor exec) {
		getCommand(cmd).setExecutor(exec);
	}
	
	private void loadEvents(Listener listener) {
		Bukkit.getPluginManager().registerEvents(listener, this);
	}
	
	private void loadHooks() {
		if (!setupEconomy()) {
			Bukkit.getConsoleSender().sendMessage(
					String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
	}
	
	public static Main getInstance() {
		return getPlugin(Main.class);
	}
	
	public static int getBolsa() {
		return porcentagem;
	}
	
	public static void setBolsa(int value) {
		porcentagem = value;
	}
	
	private Economy econ = null;

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}
	
	public Economy getEconomy() {
		return econ;
	}
}
