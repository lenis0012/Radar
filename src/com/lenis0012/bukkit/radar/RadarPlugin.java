package com.lenis0012.bukkit.radar;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.bergerkiller.bukkit.common.PluginBase;
import com.bergerkiller.bukkit.common.config.FileConfiguration;

public class RadarPlugin extends PluginBase {
	public int updateDelay;
	public double defaultRadius;
	public int maxPlayers;
	public List<String> players = new ArrayList<String>();
	
	private RadarTask task;
	
	@Override
	public boolean command(CommandSender arg0, String arg1, String[] arg2) {
		return false;
	}

	@Override
	public void disable() {
		task.stop();
	}

	@Override
	public void enable() {
		FileConfiguration config = new FileConfiguration(this);
		
		//Create config headers
		config.addHeader("update-delay", "Delay between each update of the gui's (20 = 1 sec)");
		config.addHeader("default-radius", "Default radius of a radar to detect players");
		config.addHeader("max-players", "Max players to be shown on the radar");
		
		//Init config values
		this.updateDelay = config.get("update-delay", 10);
		this.defaultRadius = config.get("default-radius", 50.0D);
		this.maxPlayers = config.get("max-players", 12);
		config.save();
		
		//Register events
		this.register(new RadarCommand(this), "radar");
		
		//Start tasks
		this.task = new RadarTask(this);
		task.start(this.updateDelay, this.updateDelay);
	}

	@Override
	public int getMinimumLibVersion() {
		return 151;
	}
}