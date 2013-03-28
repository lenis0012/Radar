package com.lenis0012.bukkit.radar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.scoreboards.CommonObjective;
import com.bergerkiller.bukkit.common.scoreboards.CommonScore;
import com.bergerkiller.bukkit.common.scoreboards.CommonScoreboard;
import com.bergerkiller.bukkit.common.scoreboards.CommonScoreboard.Display;
import com.bergerkiller.bukkit.common.utils.PlayerUtil;

public class DistanceChecker {
	private Map<String, Location> tracker = new HashMap<String, Location>();
	private Map<String, String> names = new HashMap<String, String>();
	private double radius;
	private Player player;
	private RadarPlugin plugin;
	
	public DistanceChecker(Player player, double radius) {
		this.plugin = RadarPlugin.instance;
		this.radius = radius;
		this.player = player;
	}
	
	public void check() {
		if(!this.player.isOnline())
			return;
		
		Set<String> old = this.tracker.keySet();
		List<Player> list = PlayerUtil.getNearbyPlayers(this.player, this.radius);
		CommonObjective sidebar = CommonScoreboard.get(this.player).getObjective(Display.SIDEBAR);
		
		for(Player p : list) {
			if(this.player.equals(p))
				continue;
			
			String name = p.getName();
			String display = plugin.getCorrectName(this.player, p);
			if(old.contains(name)) {
				CommonScore score = sidebar.getScore(display);
				if(score != null) {
					old.remove(name);
					if(this.hasMoved(p)) {
						int distance = (int) player.getLocation().distance(p.getLocation());
						score.setValue(distance);
						score.update();
					}
				}
			} else {
				int distance = (int) player.getLocation().distance(p.getLocation());
				names.put(name, display);
				sidebar.createScore(display, distance);
			}
		}
		
		for(String value : old) {
			String next = names.get(value);
			if(next != null) {
				names.remove(next);
				tracker.remove(next);
				CommonScore score = sidebar.getScore(next);
				if(score != null) {
					sidebar.removeScore(score);
				}
			}
		}
	}
	
	private boolean hasMoved(Player player) {
		String name = player.getName();
		Location newLoc = player.getLocation();
		Location oldLoc = tracker.get(name);
		return newLoc.getX() != oldLoc.getX() || newLoc.getY() != oldLoc.getY() || newLoc.getZ() != oldLoc.getZ();
	}
}
