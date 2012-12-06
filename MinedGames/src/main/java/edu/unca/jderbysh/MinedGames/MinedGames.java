package edu.unca.jderbysh.MinedGames;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

/*
* This is the main class of the sample plug-in
*/
public final class MinedGames extends JavaPlugin {
    /*
	* This is called when your plug-in is enabled
	*/
	
	public HashMap<Player, Integer> scores = new HashMap<Player, Integer>();
	public HashMap<Player, ItemStack[]> inventories = new HashMap<Player, ItemStack[]>();
	public Map<Material, Integer> pointValues = new HashMap<Material, Integer>();
	public boolean gameRunning = false;
	public boolean gameInit = false;

    @Override
    public void onEnable() {
        // save the configuration file
        saveDefaultConfig();
        
        // Create the SampleListener
        new MinedGamesListener(this);
        
        // set the command executor for sample
        this.getCommand("message").setExecutor(new MinedGamesCommandExecutor(this));
        this.getCommand("score").setExecutor(new MinedGamesCommandExecutor(this));
        this.getCommand("join").setExecutor(new MinedGamesCommandExecutor(this));
        this.getCommand("lobby").setExecutor(new MinedGamesCommandExecutor(this));
        this.getCommand("start").setExecutor(new MinedGamesCommandExecutor(this));
        this.getCommand("trade").setExecutor(new MinedGamesCommandExecutor(this));
        
    }
    
    /*
	* This is called when your plug-in shuts down
	*/
    @Override
    public void onDisable() {
        
    }
    
    public File getRecords() {
	    File records = new File("plugins/MinedGames/scores.hi");
	    if(!records.exists()) {
			try {
				records.createNewFile();
				BufferedWriter out = new BufferedWriter(new FileWriter(records));
				out.write("Testing");
				out.newLine();
				out.write("Twice!");
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	    return records;
    }
    
    /*
	* Inserts a player and default score into the scores map,
	* inserts the player and their inventory into the inventories map,
	* and initializes (clears) their inventories for the game.
	*/
	public void addPlayer(Player sender) {
		//this.writeToRecords(sender.getDisplayName() + ": " + 0);
		scores.put(sender, 0);
		inventories.put(sender, sender.getInventory().getContents().clone());
		sender.getInventory().clear();
	}
	
	/*
	* Runs when the game win condition is met,
	* sets the "init" and "running" bools to false,
	* restores inventories,
	* and clears the maps to prepare for a new round.
	*/
	public void gameOver() {
		gameInit = false;
		gameRunning = false;
		this.getLogger().info("Giving back players' items.");
		for(Player player : inventories.keySet().toArray(new Player[inventories.keySet().size()])){
			player.getInventory().clear();
			for(int i = 0; i < inventories.get(player).length; i++){
				if(inventories.get(player)[i] != null){
					player.getInventory().setItem(i, inventories.get(player)[i]);
				}
			}
		}
		inventories = new HashMap<Player, ItemStack[]>();
		scores = new HashMap<Player, Integer>();
	}
	
	public void dirtyLoadPointVals() {
		pointValues.put(Material.DIRT, 1);
		pointValues.put(Material.COBBLESTONE, 3);
		pointValues.put(Material.SAND, 1);
		pointValues.put(Material.SANDSTONE, 2);
		pointValues.put(Material.GOLD_ORE, 50);
		pointValues.put(Material.IRON_ORE, 20);
		pointValues.put(Material.COAL, 5);
		pointValues.put(Material.REDSTONE, 7);
		pointValues.put(Material.LOG, 5);
		pointValues.put(Material.CLAY_BALL, 5);
		pointValues.put(Material.DIAMOND, 200);
	}
	
	
	public void loadPointVals() {
		this.getLogger().info("Pre-enabling some config values.");
	    //gets the point values of materials from the config.yml (these can be customized by the player)
	    this.getLogger().info("geting the config values as a map of string-object pairs");
	    Map<String,Object> holder = this.getConfig().getConfigurationSection("score").getValues(false);
	    this.getLogger().info("This config should be called score.  It is called " + this.getConfig().getConfigurationSection("score").getName());
	    this.getLogger().info("Turning the string-object pairs into material-integer pairs.");
	    this.getLogger().info(holder.keySet().toString());
	    for(String key : holder.keySet().toArray(new String[holder.keySet().size()])) {
	    	try{
	    	if(key!=null) {
	    		this.getLogger().info("adding " + Material.getMaterial(key).name() + " with a score of " + Integer.parseInt( (String)holder.get(key) ));
	    		pointValues.put(Material.getMaterial(key), Integer.parseInt((String)(holder.get(key))));
	    	}
	    	}
	    	catch (NullPointerException e) {
	    		this.getLogger().info("can't find a value with that key.");
	    	}
	    }
	}
	/*
	* Gives a player a reward (called when game is over)
	*/
	public void rewardPlayer(Player player){
		player.getInventory().addItem(new ItemStack(Material.DIAMOND, 1));
	}
	public void writeToRecords(String str) {
		File records = getRecords();
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(records));
			out.write(str);
			out.newLine();
			out.close();
		} catch (IOException e) {
			this.getLogger().warning("File io messed up in writeToRecords function");
		}
	}

}	

