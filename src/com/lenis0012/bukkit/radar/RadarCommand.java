package com.lenis0012.bukkit.radar;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.scoreboards.CommonObjective;
import com.bergerkiller.bukkit.common.scoreboards.CommonScoreboard;
import com.bergerkiller.bukkit.common.scoreboards.CommonScoreboard.Display;

public class RadarCommand implements CommandExecutor {
	private RadarPlugin plugin;
	
	public RadarCommand(RadarPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			err(sender, "You aren't a player!");
			return true;
		}
		
		Player player = (Player) sender;
		String name = player.getName();
		
		if(!player.hasPermission("radar.use")) {
			err(player, "No permission!");
			return true;
		}
		
		if(args.length == 0) {
			if(plugin.players.contains(name)) {
				plugin.players.remove(name);
				CommonObjective sidebar = CommonScoreboard.get(player).getObjective(Display.SIDEBAR);
				sidebar.clearScores();
				sidebar.hide();
				inf(player, "Radar has been turned "+ChatColor.DARK_RED+"OFF");
			} else {
				CommonObjective sidebar = CommonScoreboard.get(player).getObjective(Display.SIDEBAR);
				sidebar.setDisplayName("Radar");
				sidebar.show();
				plugin.players.add(name);
				inf(player, "Radar has been turned "+ChatColor.DARK_GREEN+"ON");
			}
		}
		
		return true;
	}
	
	private void err(CommandSender sender, String msg) {
		sender.sendMessage(ChatColor.RED + msg);
	}
	
	private void inf(CommandSender sender, String msg) {
		sender.sendMessage(ChatColor.GREEN + msg);
	}
}