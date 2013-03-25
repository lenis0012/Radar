package com.lenis0012.bukkit.radar;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.bergerkiller.bukkit.common.Task;
import com.bergerkiller.bukkit.common.scoreboards.CommonObjective;
import com.bergerkiller.bukkit.common.scoreboards.CommonScoreboard;
import com.bergerkiller.bukkit.common.scoreboards.CommonScoreboard.Display;
import com.bergerkiller.bukkit.common.utils.PlayerUtil;

public class RadarTask extends Task {
	private RadarPlugin plugin;
	
	public RadarTask(JavaPlugin plugin) {
		super(plugin);
		this.plugin = (RadarPlugin) plugin;
	}

	@Override
	public void run() {
		for(String user : plugin.players) {
			Player player = Bukkit.getPlayer(user);
			
			if(player != null && player.isOnline()) {
				CommonObjective sidebar = CommonScoreboard.get(player).getObjective(Display.SIDEBAR);
				sidebar.clearScores();
				
				List<Player> closePlayers = PlayerUtil.getNearbyPlayers(player, plugin.defaultRadius);
				for(Player p : closePlayers) {
					if(player.equals(p))
						continue;
					
					int distance = (int) player.getLocation().distance(p.getLocation());
					sidebar.createScore(p.getName(), distance);
				}
			}
		}
	}
}