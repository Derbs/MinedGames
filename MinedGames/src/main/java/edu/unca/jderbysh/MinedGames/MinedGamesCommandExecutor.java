package edu.unca.jderbysh.MinedGames;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.google.common.base.Joiner;

/*
 * This is a sample CommandExectuor
 */
public class MinedGamesCommandExecutor implements CommandExecutor {
    private final MinedGames plugin;

    /*
     * This command executor needs to know about its plugin from which it came from
     */
    public MinedGamesCommandExecutor(MinedGames plugin) {
        this.plugin = plugin;
    }

    /*
     * On command set the sample message
     */
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("message") && sender.hasPermission("message")) {
            this.plugin.getConfig().set("message", Joiner.on(' ').join(args));
            return true;
        } else if (command.getName().equals("score")){
        	Player p = (Player)sender;
        	((Player)sender).sendMessage("" + plugin.scores.get((Player)sender));
            return true;
        } else if (command.getName().equals("join")) {
        	plugin.addPlayer((Player)sender);
        	return true;
        } else {
        	return false;
        }
    }

}
