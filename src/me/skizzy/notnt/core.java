package me.skizzy.notnt;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class core extends JavaPlugin implements Listener {

	public static Material[] banList = { Material.TNT };

	public void onEnable() {
		Bukkit.getServer().getLogger().info("NoTNT v" + getDescription().getVersion() + " has been enabled.");
		Bukkit.getServer().getLogger().info("NoTNT is running v" + getConfig().get("config-version") + " config.");
		getConfig().options().copyDefaults(true);
		saveConfig();
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(this, this);
	}

	public void onDisable() {
		Bukkit.getServer().getLogger().info("NoTNT v" + getDescription().getVersion() + " has been disbaled.");
		Bukkit.getServer().getLogger().info("NoTNT is running v" + getConfig().get("config-version") + " config.");
	}

	@EventHandler
	public void onTNTInteract(PlayerInteractEvent e) {
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		Block b = e.getClickedBlock();
		Player p = e.getPlayer();
		Location loc = e.getClickedBlock().getLocation();
		if (b.getType() == Material.TNT)
			if (!p.isOp() || !p.hasPermission(getConfig().getString("ignite-permission"))) {
				p.sendMessage(
						getConfig().getString("message").replaceAll("&", "§").replaceAll("%player%", p.getName()));
				e.setCancelled(true);
				Bukkit.getServer().getLogger()
						.info("[NoTNT] " + p.getName() + " tried to ignite TNT in " + loc.getWorld().getName() + ": X("
								+ loc.getBlockX() + ") Y(" + loc.getBlockY() + ") Z(" + loc.getBlockZ() + ")");
			} else {
				e.setCancelled(false);
				Bukkit.getServer().getLogger()
				.info("[NoTNT] " + p.getName() + " ignited TNT in " + loc.getWorld().getName() + ": X("
						+ loc.getBlockX() + ") Y(" + loc.getBlockY() + ") Z(" + loc.getBlockZ() + ")");
			}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		Material b = e.getBlock().getType();
		Location loc = e.getBlock().getLocation();
		Player p = e.getPlayer();
		Material[] arrayOfMaterial;
		int j = (arrayOfMaterial = banList).length;
		for (int i = 0; i < j; i++) {
			Material blocked = arrayOfMaterial[i];
			if (blocked == b) {
				p.isOp();
				if (!p.hasPermission(getConfig().getString("place-permission"))) {
					e.setCancelled(true);
					Bukkit.getServer().getLogger()
							.info("[NoTNT] " + p.getName() + " tried to place TNT in " + loc.getWorld().getName()
									+ ": X(" + loc.getBlockX() + ") Y(" + loc.getBlockY() + ") Z(" + loc.getBlockZ()
									+ ")");
					p.sendMessage(
							getConfig().getString("message").replaceAll("&", "§").replaceAll("%player%", p.getName()));
				} else {
					e.setCancelled(false);
					Bukkit.getServer().getLogger()
							.info("[NoTNT] " + p.getName() + " placed TNT in " + loc.getWorld().getName() + ": X("
									+ loc.getBlockX() + ") Y(" + loc.getBlockY() + ") Z(" + loc.getBlockZ() + ")");
				}
			}
		}
	}
}