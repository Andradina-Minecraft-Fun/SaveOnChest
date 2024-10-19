package br.com.centralandradina.saveonchest;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


/**
 * class to handle commands
 */
public class CommandsExecutor implements CommandExecutor
{
	protected SaveOnChest plugin;

	/**
	 * constructor
	 */
	public CommandsExecutor(SaveOnChest plugin)
	{
		this.plugin = plugin;
	}

	/**
	 * on command
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		String commandName = cmd.getName().toLowerCase();

		// comando para salvar os itens
		if (commandName.equals("saveonchest"))
		{
			// verifica se é um player
			if(sender instanceof Player) {
				Player player = (Player)sender;

				// recupera o bloco do player
				Block blockPlayer = player.getLocation().getBlock();

				// recupera o bloco à frente do qual o player está olhando
				Block blockFront = blockPlayer.getRelative(player.getFacing());

				// verifica se o bloco à frente está vazio
				if (blockFront.getType() != Material.AIR) {
					player.sendMessage(this.plugin.color(this.plugin.getConfig().getString("messages.no-space")));
					return false;
				}

				// verifica se o local do primeiro bau é protegido
				if(!this.plugin.protectionManager.hasAccess(blockFront.getLocation(), player)) {
					player.sendMessage(this.plugin.color(this.plugin.getConfig().getString("messages.location-no-permitted")));
					return false;
				}

				// recupera o bloco secundario (bau duplo)
				Block blockSecondary = blockFront.getRelative(BlockFace.WEST);

				switch (player.getFacing()) {
					case WEST:
						blockSecondary = blockFront.getRelative(BlockFace.NORTH);
						break;
					case EAST:
						blockSecondary = blockFront.getRelative(BlockFace.SOUTH);
						break;
					case NORTH:
						blockSecondary = blockFront.getRelative(BlockFace.EAST);
						break;
				}

				// verifica se o bloco secundario tambem é ar
				if (blockSecondary.getType() != Material.AIR) {
					player.sendMessage(this.plugin.color(this.plugin.getConfig().getString("messages.no-space")));
					return false;
				}

				// verifica se o local do segundo bau é protegido
				if(!this.plugin.protectionManager.hasAccess(blockSecondary.getLocation(), player)) {
					player.sendMessage(this.plugin.color(this.plugin.getConfig().getString("messages.location-no-permitted")));
					return false;
				}


				// adiciona os baus
				blockFront.setType(Material.CHEST);
				blockSecondary.setType(Material.CHEST);
				
				// conecta os baus
				switch (player.getFacing()) {
					case WEST:
						blockFront.setBlockData(Material.CHEST.createBlockData("[facing=east,type=right]"));
						blockSecondary.setBlockData(Material.CHEST.createBlockData("[facing=east,type=left]"));
						break;

					case EAST:
						blockFront.setBlockData(Material.CHEST.createBlockData("[facing=west,type=right]"));
						blockSecondary.setBlockData(Material.CHEST.createBlockData("[facing=west,type=left]"));
						break;

					case NORTH:
						blockFront.setBlockData(Material.CHEST.createBlockData("[facing=south,type=right]"));
						blockSecondary.setBlockData(Material.CHEST.createBlockData("[facing=south,type=left]"));
						break;

					case SOUTH:
						blockFront.setBlockData(Material.CHEST.createBlockData("[facing=north,type=right]"));
						blockSecondary.setBlockData(Material.CHEST.createBlockData("[facing=north,type=left]"));
						break;
				}

				// faz o player abrir o bau
				player.openInventory(((Chest)blockFront.getState()).getInventory());

				// recupera os 2 inventarios
				Inventory playerInventory = player.getInventory();
				Inventory chestInventory = ((Chest)blockFront.getState()).getInventory();

				// percorre os itens do inventário do player
				for (int slot = 0; slot < playerInventory.getSize(); slot++) {

					// pega o o item no slot atual
					ItemStack item = playerInventory.getItem(slot);

					// verifica se o slot do player não está vazio
					if (item != null) {
						// tira do inventario e poe no bau
						playerInventory.setItem(slot, null);
						chestInventory.addItem(item);
					}
				}
				
			}

			// se nao for um player
			else {
				sender.sendMessage(this.plugin.getConfig().getString("messages.no-player"));
			}
		}

		return false;
	}


	// final float newZ = (float)(player.getLocation().getZ() + ( amt * Math.sin(Math.toRadians(player.getLocation().getYaw() + 90 * direction))));
	// final float newX = (float)(player.getLocation().getX() + ( amt * Math.cos(Math.toRadians(player.getLocation().getYaw() + 90 * direction))));


}
