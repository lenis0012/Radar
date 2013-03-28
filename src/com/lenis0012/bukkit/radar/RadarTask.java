package com.lenis0012.bukkit.radar;

import org.bukkit.plugin.java.JavaPlugin;

public class RadarTask extends Thread {
	private RadarPlugin plugin;
	
	public RadarTask(JavaPlugin plugin) {
		this.plugin = (RadarPlugin) plugin;
	}

	@Override
	public void run() {
		while(!this.isInterrupted()) {
			for(DistanceChecker checker : plugin.players.values()) {
				checker.check();
			}
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				//Thread interrupted
			}
		}
	}
}