package me.Zaynstir.Anonymous;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Anonymous extends JavaPlugin implements Listener{

	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(this, this);
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(label.equalsIgnoreCase("mask")) {
			if(!(sender instanceof Player)) {
				sender.sendMessage("ERROR: Only Players can use this command.");
				return true;
			}
			Player p = (Player) sender;
			if(p.getInventory().firstEmpty() == -1) {
				p.sendMessage(ChatColor.DARK_RED + "Please empty an inventory slot");
				return true;
			}
			else {
				p.getInventory().addItem();
				p.sendMessage(ChatColor.YELLOW + "You have recieved the Anonymous Mask.");
				return true;
			}
		}
		return false;
	}
	
	public ItemStack getMask() {
		ItemStack mask = new ItemStack(Material.BONE_BLOCK);
		ItemMeta meta = mask.getItemMeta();
		
		meta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Anonymous Mask");
		List<String> lore = new ArrayList<String>();
		lore.add("");
		lore.add(ChatColor.GOLD + "" +ChatColor.ITALIC + "Right-click to place it on your head.");
		lore.add(ChatColor.GOLD + "" +ChatColor.ITALIC + "This will hide your nametag.");
		meta.setLore(lore);
		mask.setItemMeta(meta);
		
		return mask;
	}
	
	@EventHandler()
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		if(e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.BONE_BLOCK)) {
			if(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("Anonymous Mask")) {
				if(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasLore()) {
					Player p = (Player) e.getPlayer();
					if(p.getInventory().getHelmet() == null) {
						p.sendMessage(ChatColor.DARK_RED + "Please remove your helmet.");
					}
					else {
						p.getInventory().removeItem(getMask());
						p.getInventory().setHelmet(getMask());
					}
				}
			}
		}
	}
}
