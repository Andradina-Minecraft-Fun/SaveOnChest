package br.com.centralandradina.saveonchest;

import java.util.Arrays;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;




/**
 * main class
 */
public class SaveOnChest extends JavaPlugin 
{

	/**
	 * on enable
	 */
 	@Override
	public void onEnable() 
	{
		PluginManager pluginManager = this.getServer().getPluginManager();

		// set default configs
		FileConfiguration config = getConfig();
		config.options().copyDefaults(true);
		config.addDefault("messages.no-space", "Sem espaço na sua frente para criar o baú duplo");
		config.addDefault("messages.no-player", "Esse comando só pode ser executado por um player");
		// config.addDefault("aliases", Arrays.asList("saveonchest", "guardar"));
		saveConfig();

		// commands
		this.getCommand("saveonchest").setExecutor(new CommandsExecutor(this));

		// all ok
		getLogger().info("SaveOnChest enabled");
	}

	/**
	 * on disable
	 */
	@Override
	public void onDisable() 
	{
		HandlerList.unregisterAll(this);

		getLogger().info("SaveOnChest disabled");
	}

	/**
	 * helper for color
	 */
	public String color(String msg)
	{
		return ChatColor.translateAlternateColorCodes('&', "&f" + msg);
	}

}