package com.lenis0012.bukkit.radar.hooks;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import com.lenis0012.bukkit.radar.RadarPlugin;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;

public class FactionsManager implements Listener {
	private RadarPlugin plugin;
	
	public FactionsManager(RadarPlugin plugin) {
		this.plugin = plugin;
		PluginManager pm = plugin.getServer().getPluginManager();
		pm.registerEvents(this, plugin);
	}
	
	public FPlayer getFPlayer(Player player) {
		return (FPlayer) FPlayers.i.get(player);
	}
	
	public ChatColor getRadarColor(Player from, Player to) {
		FPlayer fFrom = this.getFPlayer(from);
		FPlayer fTo = this.getFPlayer(to);
		return fFrom.getColorTo(fTo);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPluginDisable(PluginDisableEvent event) {
		Plugin disabled = event.getPlugin();
		String dname = disabled.getName();
		if(dname.equals("Factions")) {
			plugin.factionsEnabled = false;
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPluginEnable(PluginEnableEvent event) {
		Plugin enabled = event.getPlugin();
		String ename = enabled.getName();
		if(ename.equals("Factions")) {
			plugin.factionsEnabled = true;
		}
	}
}
