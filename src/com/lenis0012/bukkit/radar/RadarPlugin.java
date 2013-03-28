package com.lenis0012.bukkit.radar;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import com.bergerkiller.bukkit.common.PluginBase;
import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.lenis0012.bukkit.radar.hooks.FactionsManager;

public class RadarPlugin extends PluginBase {
	public static RadarPlugin instance;
	public int updateDelay;
	public double defaultRadius;
	public int maxPlayers;
	public Map<String, DistanceChecker> players = new HashMap<String, DistanceChecker>();
	
	/** Hooks */
	public boolean factionsEnabled;
	public FactionsManager factions;
	
	private RadarTask task;
	
	@Override
	public boolean command(CommandSender arg0, String arg1, String[] arg2) {
		return false;
	}

	@Override
	public void disable() {
		task.interrupt();
	}

	@Override
	public void enable() {
		instance = this;
		PluginManager pm = this.getServer().getPluginManager();
		FileConfiguration config = new FileConfiguration(this);
		config.load();
		
		//Create config headers
		config.addHeader("update-delay", "Delay between each update of the gui's (20 = 1 sec)");
		config.addHeader("default-radius", "Default radius of a radar to detect players");
		config.addHeader("max-players", "Max players to be shown on the radar");
		
		//Init config values
		this.updateDelay = config.get("update-delay", 10);
		this.defaultRadius = config.get("default-radius", 50.0D);
		this.maxPlayers = config.get("max-players", 12);
		config.save();
		
		//Register events, commands and hooks
		this.register(new RadarCommand(this), "radar");
		this.factionsEnabled = pm.isPluginEnabled("Factions");
		
		if(this.factionsEnabled)
			this.factions = new FactionsManager(this);
		
		//Start tasks
		this.task = new RadarTask(this);
		task.start();
	}
	
	public String getCorrectName(Player from, Player to) {
		String real = to.getName();
		
		if(this.factionsEnabled) {
			if(real.length() > 14)
				real = real.substring(0, 14);
			
			return factions.getRadarColor(from, to).toString() + real;
		} else
			return real;
	}

	@Override
	public int getMinimumLibVersion() {
		return 151;
	}
}