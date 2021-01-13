package net.hypixel.kaboomplugin;

import java.util.Iterator;
import org.bukkit.util.Vector;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class KaboomPlugin extends JavaPlugin
{
    public void onEnable() {
        this.saveDefaultConfig();
        if (this.getConfig().getBoolean("show-colors-in-console")) {
            this.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "=============================");
            this.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Kaboom Enabled");
            this.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "=============================");
        }
        else {
            this.getServer().getConsoleSender().sendMessage("=============================");
            this.getServer().getConsoleSender().sendMessage("Kaboom Disabled");
            this.getServer().getConsoleSender().sendMessage("=============================");
        }
    }

    public void onDisable() {
        this.saveDefaultConfig();
        if (this.getConfig().getBoolean("show-colors-in-console")) {
            this.getServer().getConsoleSender().sendMessage(ChatColor.RED + "=============================");
            this.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Kaboom Disabled");
            this.getServer().getConsoleSender().sendMessage(ChatColor.RED + "=============================");
        }
        else {
            this.getServer().getConsoleSender().sendMessage("=============================");
            this.getServer().getConsoleSender().sendMessage("Kaboom Disabled");
            this.getServer().getConsoleSender().sendMessage("=============================");
        }
    }

    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (label.equalsIgnoreCase("kaboom")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("prefix") + this.getConfig().getString("no-permission")));
                return true;
            }
            final Player player = (Player)sender;
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    if (player.hasPermission("kaboom.reload")) {
                        this.reloadConfig();
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("prefix") + this.getConfig().getString("reload-message")));
                    }
                    else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("prefix") + this.getConfig().getString("no-permission")));
                    }
                }
                else if (Bukkit.getServer().getPlayer(args[0]) != null) {
                    final Player target = Bukkit.getPlayer(args[0]);
                    if (player.hasPermission("kaboom.use")) {
                        if (Bukkit.getServer().getPlayer(args[0]) == null) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("prefix") + "&cCould not find player &e" + target.getDisplayName()));
                            return true;
                        }
                        if (target.hasPermission("kaboom.exempt")) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("prefix") + this.getConfig().getString("exempt-message")
                                    .replace("%player%", target.getDisplayName())));
                            return true;
                        }
                        target.setVelocity(new Vector(0.0, 64.0, 0.0));
                        target.getWorld().strikeLightningEffect(player.getLocation());
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("prefix") + "&eYou kaboomed &c" + target.getDisplayName()));
                        if (this.getConfig().getBoolean("title-for-kaboomed-players.enabled")) {
                            target.sendTitle(this.getConfig().getString("title-for-kaboomed-players.title")
                                    .replace("%player%", player.getDisplayName())
                                    .replaceAll("&", "§"), this.getConfig().getString("title-for-kaboomed-players.subtitle")
                                    .replace("%player%", player.getDisplayName())
                                    .replaceAll("&", "§"));
                        }
                        if (this.getConfig().getBoolean("message-for-kaboomed-players.enabled")) {
                            target.sendMessage(this.getConfig().getString("message-for-kaboomed-players.message")
                                    .replace("%player%", player.getDisplayName()).replaceAll("&", "§"));
                        }
                    }
                    else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("prefix") + this.getConfig().getString("no-permission")));
                    }
                }
                else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("prefix") + this.getConfig().getString("player-not-found")
                            .replace("%player%", args[0])));
                }
                return true;
            }
            if (sender.hasPermission("kaboom.use")) {
                for (final Player player2 : this.getServer().getOnlinePlayers()) {
                    this.kaboomPlayer(player2, sender.getName(), true);
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aLaunched " + player2.getDisplayName()));
                }
            }
            else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("prefix") + this.getConfig().getString("no-permission")));
            }
        }
        return true;
    }

    public void kaboomPlayer(final Player player, final String name, final boolean b) {
        if (!player.hasPermission("kaboom.exempt")) {
            player.setVelocity(new Vector(0.0, 64.0, 0.0));
            player.getWorld().strikeLightningEffect(player.getLocation());
            if (this.getConfig().getBoolean("title-for-kaboomed-players.enabled")) {
                player.sendTitle(this.getConfig().getString("title-for-kaboomed-players.title").replace("%player%", name)
                        .replaceAll("&", "§"), this.getConfig().getString("title-for-kaboomed-players.subtitle")
                        .replace("%player%", name)
                        .replaceAll("&", "§"));
            }
            if (this.getConfig().getBoolean("message-for-kaboomed-players.enabled")) {
                player.sendMessage(this.getConfig().getString("message-for-kaboomed-players.message").replace("%player%", name)
                        .replaceAll("&", "§"));
            }
        }
    }
}
