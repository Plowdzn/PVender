package me.ploow.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

import me.ploow.Main;
import me.ploow.core.VenderCore;
import me.ploow.utils.Settings;

public class ShiftEvent extends Settings implements Listener {
	private static Main plugin = Main.getInstance();
	private Settings settings = new Settings();
	@SuppressWarnings("deprecation")
	@EventHandler
	public void aoApertarShift(PlayerToggleSneakEvent e) {
		Player p = (Player) e.getPlayer();
		if (hasNameList(p.getName(), settings.getShiftList()) && !p.isSneaking()) {
			for (String x : plugin.getConfig().getStringList("itens")) {
				String[] stringSplited = x.split(";");
				int item_id = Integer.parseInt(stringSplited[0]);
				int item_data = Integer.parseInt(stringSplited[1]);
				double item_value = Double.parseDouble(stringSplited[2]);
				VenderCore.venderItens(p, new ItemStack(item_id, (short) item_data), item_value, Main.getBolsa(), true);
			}
		}
	}
}
