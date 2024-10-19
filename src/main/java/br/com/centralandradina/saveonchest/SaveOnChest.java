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
	public ProtectionManager protectionManager;

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
		config.addDefault("messages.location-no-permitted", "Você não tem acesso à esse local");
		saveConfig();

		// create ProtectionManager
		protectionManager = new ProtectionManager(this);

		// verify if are using RedProtect
		if (pluginManager.isPluginEnabled("RedProtect")) {
			this.getLogger().info("RedProtect hooked");
			protectionManager.pluginRedProtect = true;
		}

		// verify if are using WorldGuard
		if (pluginManager.isPluginEnabled("WorldGuard")) {
			this.getLogger().info("WorldGuard hooked");
			protectionManager.pluginWorldGuard = true;
		}

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