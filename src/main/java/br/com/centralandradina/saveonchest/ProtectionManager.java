package br.com.centralandradina.saveonchest;

import br.net.fabiozumbi12.RedProtect.Bukkit.RedProtect;
import br.net.fabiozumbi12.RedProtect.Bukkit.Region;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 
 */
public class ProtectionManager 
{
	protected JavaPlugin plugin;
	public boolean pluginRedProtect = false;
	public boolean pluginWorldGuard = false;

	/**
	 * constructor
	 */
	public ProtectionManager(JavaPlugin plugin)
	{
		this.plugin = plugin;
	}

	/**
	 * verify if can catch
	 */
	public boolean canCapture(Location entityLocation, Player player)
	{
		// verify redprotect
		if(this.pluginRedProtect) {
			if(!this.hasAccessRedProtect(entityLocation, player)) {
				return false;
			}
		}

		// verify worldguard
		if(this.pluginWorldGuard) {
			if(!this.hasAccessWorldGuard(entityLocation, player)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * verify if can access the area of player
	 */
	public boolean hasAccess(Location entityLocation, Player player)
	{
		// verify redprotect
		if(this.pluginRedProtect) {
			if(!this.hasAccessRedProtect(entityLocation, player)) {
				return false;
			}
		}

		// verify worldguard
		if(this.pluginWorldGuard) {
			if(!this.hasAccessWorldGuard(entityLocation, player)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * do the verification of RedProtect
	 * nice reference https://github.com/FabioZumbi12/RedProtect/blob/master/RedProtect-Core/src/main/java/br/net/fabiozumbi12/RedProtect/Core/region/CoreRegion.java#L228
	 */
	public boolean hasAccessRedProtect(Location entityLocation, Player player)
	{
		// get region
		Region reg = RedProtect.get().getAPI().getRegion(entityLocation);
		if(reg != null) {

			// is owner
			if(!reg.isLeader(player)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * do the verification of WorldGuard
	 * nice reference https://worldguard.enginehub.org/en/latest/developer/regions/protection-query/
	 */
	public boolean hasAccessWorldGuard(Location entityLocation, Player player)
	{

		RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
		com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(entityLocation);
		com.sk89q.worldedit.world.World world = BukkitAdapter.adapt(entityLocation.getWorld());

		// verify if player has bypass permission
		if (!WorldGuard.getInstance().getPlatform().getSessionManager().hasBypass(WorldGuardPlugin.inst().wrapPlayer(player), world)) {

			// interact flag
			if(query.testState(loc, WorldGuardPlugin.inst().wrapPlayer(player), com.sk89q.worldguard.protection.flags.Flags.INTERACT)) {
				return false;
			}

			// ride flag
			if(query.testState(loc, WorldGuardPlugin.inst().wrapPlayer(player), com.sk89q.worldguard.protection.flags.Flags.RIDE)) {
				return false;
			}

		}


		return true;

	}
}
