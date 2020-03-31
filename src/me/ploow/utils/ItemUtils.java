package me.ploow.utils;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemUtils {
	public static ItemStack createItem(int m, String display_name, ArrayList<String> Lore, int data) {
		@SuppressWarnings("deprecation")
		ItemStack item = new ItemStack(m, 1, (short)data);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(display_name);
		meta.setLore(Lore);
		item.setItemMeta(meta);
		return item;
	}
	public static ItemStack createItem(int m, String display_name, int data) {
		@SuppressWarnings("deprecation")
		ItemStack item = new ItemStack(m, 1, (short)data);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(display_name);
		item.setItemMeta(meta);
		return item;
	}

	  public static ItemStack getNamedSkull(String nick, String nome)
	  {
	    @SuppressWarnings("deprecation")
		ItemStack skull = new ItemStack(397, 1, (short)3);
	    SkullMeta meta = (SkullMeta)skull.getItemMeta();
	    meta.setDisplayName(nome);
	    meta.setOwner(nick);
	    skull.setItemMeta(meta);
	    
	    return skull;
	  }
}
