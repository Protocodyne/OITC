package me.artish1.OITC;

import java.io.File;
import java.util.logging.Logger;

import me.artish1.OITC.Arena.Arena;
import me.artish1.OITC.Arena.Arenas;
import me.artish1.OITC.Arena.LeaveReason;
import me.artish1.OITC.Listeners.*;
import me.artish1.OITC.Utils.Methods;
import org.black_ixx.playerpoints.PlayerPoints;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


public class OITC extends JavaPlugin {
		
		public final Logger logger = Logger.getLogger("Minecraft");
		public final Methods m = new Methods(this);
		public final GameListener gl = new GameListener(this);
		public final SignListener sl = new SignListener(this);
		
	
		public File kitsFile;
		public static FileConfiguration kits;
		public File playersFile;
		public static FileConfiguration players;
		public File arenasFile;
		public FileConfiguration arenas;
	
	
	  public static FileConfiguration getKitsFile(){
		  return kits;
	  }
	  
	  
	  
	  
	 
	
	public void onEnable() {
		
		//LOADING CONFIG FILES ****************************
	    System.out.println("Loading YML files!");

	    this.playersFile = new File(getDataFolder(), "players.yml");
	    this.arenasFile = new File(getDataFolder(), "arenas.yml");
	    this.kitsFile = new File(getDataFolder(), "kits.yml");
	    
	    kits = new YamlConfiguration();
	    this.arenas = new YamlConfiguration();
	    players = new YamlConfiguration();
	    
	    Methods.loadYamls();
	    
	    this.arenas.options().copyDefaults(true);
	    players.options().copyDefaults(true);
	    kits.options().copyDefaults(true);
	    
	    getConfig().options().copyDefaults(true);
	    
	    System.out.println("Loaded YML files Successfully!");
	    //********************CONFIG FILES***********************
	    
	    
	    
	    
	    //***********LISTENERS*****************
	    
	    getServer().getPluginManager().registerEvents(gl, this);
	    getServer().getPluginManager().registerEvents(sl, this);
	  //  getServer().getPluginManager().registerEvents(gl, this);
	  //  getServer().getPluginManager().registerEvents(gl, this);

	    
	    
	    //*************************************
	    
	    
	    
	    
	    try
	    {
	      for (String s : this.arenas.getStringList("Arenas.List"))
	      {
	        Arena arena = new Arena(s);
	        this.logger.info("[OITC] Now Currently Loading The Arena: " + arena.getName());
	        
	        Arenas.addArena(arena);
	        arena.updateSigns();
	        this.logger.info("[OITC] The Arena: " + arena.getName() + " Has successfully loaded!");
	      }
	    }
	    catch (Exception e)
	    {
	      this.logger.info("[OITC] WARNING, FAILED TO LOAD ARENAS.");
	    }
	    try
	    {
	      this.m.firstRun();
	    }
	    catch (Exception localException1) {}
	    Methods.loadYamls();
		
		super.onEnable();
            hookPlayerPoints();
	}
	
	
	public void onDisable() {
		
		for(Arena arena : Arenas.getArenas()){
			arena.stop();
		}
		
		super.onDisable();
	}
	
	
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		
		if(label.equalsIgnoreCase("oitc") && !(sender instanceof Player)){
			sender.sendMessage("Must be a player to send OITC commands");
		}
		
		if(label.equalsIgnoreCase("oitc") && sender instanceof Player){
			Player player = (Player) sender;
			
			if(args.length == 0){
				player.sendMessage("");
		          player.sendMessage("");
		          player.sendMessage("");
		          player.sendMessage("");
		          player.sendMessage("");
		          player.sendMessage("");
		          player.sendMessage("");
		          player.sendMessage("");
		          

		          player.sendMessage(ChatColor.GRAY + "--------" + ChatColor.AQUA + "OITC" + ChatColor.GRAY + "--------");
		          player.sendMessage(ChatColor.GRAY + "Created By: " + ChatColor.RED + "Artish1");
		          
		          player.sendMessage(ChatColor.AQUA + "/oitc lobby" + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + "메인로비로 텔레포트 합니다.");
		          player.sendMessage(ChatColor.AQUA + "/oitc leave" + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + "현재있는 맵에서 나갑니다.");
		          if (player.hasPermission("oitc.admin"))
		          {
		            player.sendMessage(ChatColor.AQUA + "/oitc create [Arena]" + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + "Creates a new Arena");
		            player.sendMessage(ChatColor.AQUA + "/oitc delete [Arena]" + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + "Deletes the Arena");
		            player.sendMessage(ChatColor.AQUA + "/oitc addspawn [Arena]" + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + "Adds a spawn for the Arena");
		            
		            player.sendMessage(ChatColor.AQUA + "/oitc setmainlobby" + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + " Sets the Main Lobby");
		            player.sendMessage(ChatColor.AQUA + "/oitc setlobby [Arena]" + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + " Sets the Lobby of the Arena.");
		            player.sendMessage(ChatColor.AQUA + "/oitc stop [Arena]" + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + "Force stops the Arena");
		            player.sendMessage(ChatColor.AQUA + "/oitc start [Arena]" + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + "Force starts the Arena");
		            player.sendMessage(ChatColor.AQUA + "/oitc reload" + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + "Reloads the configs.");
		            player.sendMessage(ChatColor.AQUA + "/oitc list" + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + "Lists all the Arenas.");
		            player.sendMessage(ChatColor.AQUA + "/oitc version" + ChatColor.DARK_GRAY + " || " + ChatColor.GRAY + "Gives you the version of this plugin.");

		          }
			}
			
			
			if(args.length == 1){
				if(args[0].equalsIgnoreCase("list")){
					
					String arenas = "List of Arenas: " + ChatColor.DARK_AQUA;
					
					for(Arena arena : Arenas.getArenas()){
						arenas = arenas + arena.getName() + ", ";
					}
					
					sendMessage(player, arenas);
				}
				
				if(args[0].equalsIgnoreCase("version")){
					sendMessage(player, "You are using OITC Version " + ChatColor.RED + getDescription().getVersion());
					
				}
				
				
				if(player.hasPermission("oitc.admin")){
					if(args[0].equalsIgnoreCase("setmainlobby")){
						Methods.setLobby(player.getLocation());
						sendMessage(player, "You have set the Main Lobby!");
					}
					
					if(args[0].equalsIgnoreCase("reload")){
						Methods.loadYamls();
						reloadConfig();
						for(Arena arena : Arenas.getArenas()){
							arena.updateSigns();
						}
						
						sendMessage(player, "Reloaded Configs Successfully!");
					}
				
				}
				
				if(args[0].equalsIgnoreCase("lobby")){
					if(!Arenas.isInArena(player)){
						if(Methods.getLobby() != null){
							player.teleport(Methods.getLobby());
							sendMessage(player, "Welcome to the " + ChatColor.DARK_AQUA + "Main Lobby!");
							
						}else{
							sendMessage(player, "Oops, it seems there is no Main Lobby setup yet! Please alert your server admins.");
						}
					}else{
						Arena arena = Arenas.getArena(player);
						sendMessage(player, "현재맵에서 나간후에 로비로 돌아가주세요.");
						arena.removePlayer(player, LeaveReason.QUIT);
					}
				}
				
				if(args[0].equalsIgnoreCase("leave")){
					if(Arenas.isInArena(player)){
						Arena arena = Arenas.getArena(player);
						arena.removePlayer(player, LeaveReason.QUIT);
					}else{
						sendMessage(player,"당신은 현재 맵에서 나가지 않았습니다, 하지만 당신은 여전히 로비로 돌아가려 시도중입니다.!");
					}
				}
				
				
			}
			
			
			if(args.length == 2){
                            	if(args[0].equalsIgnoreCase("start")){
					if(Arenas.arenaExists(args[1])){
						Arena arena = Arenas.getArena(args[1]);
						
						if(arena.getPlayers().size() >= 2){
							arena.start();
							sendMessage(player,ChatColor.DARK_AQUA + arena.getName()+ "께서 게임을 시작합니다" );
							
						}else{
							sendMessage(player, "Cannot start arena.");
							sendMessage(player, "It is either ingame, stopping, or not enough players.");
						}
						
						
					}else{
						sendMessage(player, "Sorry, there is no such arena named " + ChatColor.RED + args[1]);
					}
				}
				if(player.hasPermission("oitc.admin")){
				if(args[0].equalsIgnoreCase("create")){
					if (!Arenas.arenaExists(args[1]))
		            {
		              this.arenas.addDefault("Arenas." + args[1], args[1]);
		              this.arenas.addDefault("Arenas." + args[1] + ".Signs.Counter", Integer.valueOf(0));
		              getConfig().addDefault(args[1] + ".Countdown", Integer.valueOf(15));
		              getConfig().addDefault(args[1] + ".MaxPlayers", Integer.valueOf(20));
		              getConfig().addDefault(args[1] + ".KillsToWin", Integer.valueOf(25));
		              getConfig().addDefault(args[1] + ".AutoStartPlayers", Integer.valueOf(8));
		              getConfig().addDefault(args[1] + ".EndTime", Integer.valueOf(600));
		              Arena arena = new Arena(args[1]);
		              Arenas.addArena(arena);
		              Methods.addToList(arena);
		              sendMessage(player, ChatColor.GRAY + "You have created the Arena: " + ChatColor.GOLD + arena.getName());
		              Methods.saveYamls();
		              saveConfig();
		            }
		            else
		            {
		            	sendMessage(player, ChatColor.RED + "That Arena already Exists!");
		            }
				}
				
				if(args[0].equalsIgnoreCase("delete")){
					if (getConfig().contains(args[1])){
		              getConfig().set(args[1], null);
		              this.arenas.set("Arenas." + args[1], null);
		              
		              Methods.removeFromList(args[1]);
		              
		              Methods.saveYamls();
		              saveConfig();
		              sendMessage(player, "You have deleted " + ChatColor.DARK_RED + args[1]);
		            }
		            else
		            {
		              sendMessage(player, "Sorry, there is no such arena named " + ChatColor.RED + args[1]);
		            }
				}
				
				if(args[0].equalsIgnoreCase("stop")){
					if(Arenas.arenaExists(args[1])){
							
						Arena arena = Arenas.getArena(args[1]);
						arena.sendAll(ChatColor.RED + player.getName() + ChatColor.GRAY + " Has stopped the Arena!");	
						arena.stop();
						
						
					}else{
						sendMessage(player, "Sorry, there is no such arena named " + ChatColor.RED + args[1]);
					}
				}
				
				if(args[0].equalsIgnoreCase("addspawn")){
					if(Arenas.arenaExists(args[1])){
						Arena arena = Arenas.getArena(args[1]);
						arena.addSpawn(player.getLocation()); 
						sendMessage(player, "You have added a spawn for " + ChatColor.DARK_AQUA + arena.getName());
						
					}else{
						sendMessage(player, "Sorry, there is no such arena named " + ChatColor.RED + args[1]);
					}
				}
				
				if(args[0].equalsIgnoreCase("setlobby")){
					if(Arenas.arenaExists(args[1])){
						Arena arena = Arenas.getArena(args[1]);
						arena.setLobbySpawn(player.getLocation()); 
						sendMessage(player, "You have set the lobby spawn for " + ChatColor.DARK_AQUA + arena.getName());
					}else{
						sendMessage(player, "Sorry, there is no such arena named " + ChatColor.RED + args[1]);
					}
				}
				
				}
				
				
				
			}
			
			
			
		}
		
		
		
		
		return super.onCommand(sender, command, label, args);
	}
	
	
	 public static void sendMessage(Player player, String Message)
	  {
	    player.sendMessage(ChatColor.GRAY + "[" + ChatColor.AQUA + "OITC" + ChatColor.GRAY + "] " + ChatColor.GRAY + Message);
	  }
         private boolean hookPlayerPoints() {
            final Plugin plugin = this.getServer().getPluginManager().getPlugin("PlayerPoints");
            playerPoints = PlayerPoints.class.cast(plugin);
            return playerPoints != null; 
}
        private static PlayerPoints playerPoints;
        public static PlayerPoints getPlayerPoints() {
            return playerPoints;
        }
	
}
