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
        	((Player)sender).sendMessage("" + plugin.scores.get((Player)sender));
            return true;
        } else if (command.getName().equals("join")) {
        	//plugin.addPlayer((Player)sender);  //doesn't work!
        	
        	plugin.getLogger().info("Trying to join " + (Player)sender + " to a mined game.");
        	if(!plugin.gameRunning && plugin.gameInit) {
	        	if(plugin.scores.containsKey((Player)sender)) {
	        		plugin.getLogger().info(((Player)sender).getDisplayName() + " has already joined!");
	        		((Player)sender).sendMessage("You are already in the game.");
	        	}
	        	else {
	        		((Player)sender).sendMessage("You have joined a Mined Game!");
	        		plugin.addPlayer((Player)sender);
	        	}
        	}
        	else {
        		if(plugin.gameRunning){
        			((Player)sender).sendMessage("The game has already started.");
        		}
        		else if(!plugin.gameInit){
        			((Player)sender).sendMessage("There is no lobby to join!");
        		}
        		plugin.getLogger().info(((Player)sender).getDisplayName() + " could not join the MinedGame.");
        	}
	        return true;
        } else if (command.getName().equals("start")){
        	plugin.getLogger().info("Trying to start a Mined Game");
        	if(plugin.gameInit) {
        		sender.getServer().broadcastMessage("Starting a Mined Game!");
        		plugin.getLogger().info("Starting a Mined Game");
        		plugin.gameRunning = true;
        	}
        	return true;
    	} else if (command.getName().equals("lobby")) {
    		if(!plugin.gameInit){
    			//this.plugin.loadPointVals();
				plugin.gameInit = true;
				plugin.getLogger().info("The next Mined Game will start soon.");
				plugin.getServer().broadcastMessage(((Player)sender).getDisplayName() + " has created a new Mined Game. \nType /join to go to the lobby!");
				plugin.addPlayer((Player)sender);
    		}
    		else{
    			plugin.getLogger().info(((Player)sender).getDisplayName() + " tried to create another lobby.");
    			((Player)sender).sendMessage("Sorry, there's already a lobby created!");
    		}
    		return true;
    			
    	} else {
    		return false;
        }
    }
}