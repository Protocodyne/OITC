package me.artish1.OITC.Arena;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import me.artish1.OITC.OITC;
import me.artish1.OITC.Utils.Methods;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class Arena {
	
	  private String name;
	  private GameState state = GameState.LOBBY;
	  private int id = 0;
	  private int counter;
	  private int endtime;
	  private boolean endtimeOn = false;
	  OITC plugin;
	  private List<UUID> players = new ArrayList<UUID>();
	  private int timecheckid = 0;
	  private Scoreboard scoreboard;
	  
	  public Arena(String Name)
	  {
	    this.name = Name;
	    this.plugin = Methods.getPlugin();
	  }
	  
	  private HashMap<UUID,ItemStack[]> armor = new HashMap<UUID,ItemStack[]>();
	  private HashMap<UUID,ItemStack[]> inventory = new HashMap<UUID,ItemStack[]>();
	  
	  
	private void saveInventory(Player player){
		  armor.put(player.getUniqueId(), player.getInventory().getArmorContents());
		  inventory.put(player.getUniqueId(), player.getInventory().getContents());
		  
		  player.getInventory().setArmorContents(null);
		  player.getInventory().clear();
		  player.updateInventory();
	  }
	  
	private void loadInventory(Player player){
		if(armor.containsKey(player.getUniqueId())){
			player.getInventory().setArmorContents(armor.get(player.getUniqueId()));
			armor.remove(player.getUniqueId());
		}
		
		if(inventory.containsKey(player.getUniqueId())){
			player.getInventory().setContents(inventory.get(player.getUniqueId()));
			inventory.remove(player.getUniqueId());
		}
		player.updateInventory();
	  }
	  
	  
	public void sendAll(String Message)
	  {
	    List<UUID> nulls = new ArrayList<UUID>();
	    for (UUID s : this.players) {
	      if (Bukkit.getPlayer(s) != null) {
	        Bukkit.getPlayer(s).sendMessage(Message);
	      } else {
	        nulls.add(s);
	      }
	    }
	    for (UUID s : nulls) {
	      this.players.remove(s);
	    }
	    nulls.clear();
	  }
	  
	 
	  public GameState getState() {
		return state;
	  }
	  
	public void setState(GameState state) {
		this.state = state;
	}
	  
	  public String getName()
	  {
	    return this.name;
	  }
	  
	  public void setName(String name)
	  {
	    this.name = name;
	  }
	  
	  public Location getRandomSpawn()
	  {
	    Random rand = new Random();
	    if (this.plugin.arenas.contains("Arenas." + getName() + ".Spawns.Counter"))
	    {
	      int other = this.plugin.arenas.getInt("Arenas." + getName() + ".Spawns.Counter") - 1;
	      int num = rand.nextInt(other) + 1;
	      

	      Location loc = getSpawn(num);
	      
	      return loc;
	    }
	    return null;
	  }
	  
	  public Location getSpawn(int id)
	  {
	    if (this.plugin.arenas.contains("Arenas." + getName() + ".Spawns." + id + ".World"))
	    {
	      Location loc = new Location(Bukkit.getWorld(this.plugin.arenas.getString("Arenas." + getName() + ".Spawns." + id + ".World")), 
	        this.plugin.arenas.getDouble("Arenas." + getName() + ".Spawns." + id + ".X"), 
	        this.plugin.arenas.getDouble("Arenas." + getName() + ".Spawns." + id + ".Y"), 
	        this.plugin.arenas.getDouble("Arenas." + getName() + ".Spawns." + id + ".Z"));
	      loc.setPitch((float)this.plugin.arenas.getDouble("Arenas." + getName() + ".Spawns." + id + ".Pitch"));
	      loc.setYaw((float)this.plugin.arenas.getDouble("Arenas." + getName() + ".Spawns." + id + ".Yaw"));
	      return loc;
	    }
	    return null;
	  }
	  
	  public void addSpawn(Location loc)
	  {
	    if (!this.plugin.arenas.contains("Arenas." + getName() + ".Spawns.1.X"))
	    {
	      this.plugin.arenas.addDefault("Arenas." + getName() + ".Spawns.Counter", Integer.valueOf(2));
	      this.plugin.arenas.addDefault("Arenas." + getName() + ".Spawns.1" + ".X", Double.valueOf(loc.getX()));
	      this.plugin.arenas.addDefault("Arenas." + getName() + ".Spawns.1" + ".Y", Double.valueOf(loc.getY()));
	      this.plugin.arenas.addDefault("Arenas." + getName() + ".Spawns.1" + ".Z", Double.valueOf(loc.getZ()));
	      this.plugin.arenas.addDefault("Arenas." + getName() + ".Spawns.1" + ".World", loc.getWorld().getName());
	      this.plugin.arenas.addDefault("Arenas." + getName() + ".Spawns.1" + ".Pitch", Float.valueOf(loc.getPitch()));
	      this.plugin.arenas.addDefault("Arenas." + getName() + ".Spawns.1" + ".Yaw", Float.valueOf(loc.getYaw()));
	    }
	    else
	    {
	      int counter = this.plugin.arenas.getInt("Arenas." + getName() + ".Spawns.Counter");
	      this.plugin.arenas.set("Arenas." + getName() + ".Spawns." + counter + ".X", Double.valueOf(loc.getX()));
	      this.plugin.arenas.set("Arenas." + getName() + ".Spawns." + counter + ".Y", Double.valueOf(loc.getY()));
	      this.plugin.arenas.set("Arenas." + getName() + ".Spawns." + counter + ".Z", Double.valueOf(loc.getZ()));
	      this.plugin.arenas.set("Arenas." + getName() + ".Spawns." + counter + ".World", loc.getWorld().getName());
	      this.plugin.arenas.set("Arenas." + getName() + ".Spawns." + counter + ".Pitch", Float.valueOf(loc.getPitch()));
	      this.plugin.arenas.set("Arenas." + getName() + ".Spawns." + counter + ".Yaw", Float.valueOf(loc.getYaw()));
	      
	      counter++;
	      
	      this.plugin.arenas.set("Arenas." + getName() + ".Spawns.Counter", Integer.valueOf(counter));
	    }
	    Methods.saveYamls();
	  }
	  
	  public void setLobbySpawn(Location loc)
	  {
	    if (!this.plugin.arenas.contains("Arenas." + getName() + ".Lobby.Spawn"))
	    {
	      this.plugin.arenas.addDefault("Arenas." + getName() + ".Lobby.Spawn" + ".X", Double.valueOf(loc.getX()));
	      this.plugin.arenas.addDefault("Arenas." + getName() + ".Lobby.Spawn" + ".Y", Double.valueOf(loc.getY()));
	      this.plugin.arenas.addDefault("Arenas." + getName() + ".Lobby.Spawn" + ".Z", Double.valueOf(loc.getZ()));
	      this.plugin.arenas.addDefault("Arenas." + getName() + ".Lobby.Spawn" + ".World", loc.getWorld().getName());
	      this.plugin.arenas.addDefault("Arenas." + getName() + ".Lobby.Spawn" + ".Pitch", Float.valueOf(loc.getPitch()));
	      this.plugin.arenas.addDefault("Arenas." + getName() + ".Lobby.Spawn" + ".Yaw", Float.valueOf(loc.getYaw()));
	    }
	    else
	    {
	      this.plugin.arenas.set("Arenas." + getName() + ".Lobby.Spawn" + ".X", Double.valueOf(loc.getX()));
	      this.plugin.arenas.set("Arenas." + getName() + ".Lobby.Spawn" + ".Y", Double.valueOf(loc.getY()));
	      this.plugin.arenas.set("Arenas." + getName() + ".Lobby.Spawn" + ".Z", Double.valueOf(loc.getZ()));
	      this.plugin.arenas.set("Arenas." + getName() + ".Lobby.Spawn" + ".World", loc.getWorld().getName());
	      this.plugin.arenas.set("Arenas." + getName() + ".Lobby.Spawn" + ".Pitch", Float.valueOf(loc.getPitch()));
	      this.plugin.arenas.set("Arenas." + getName() + ".Lobby.Spawn" + ".Yaw", Float.valueOf(loc.getYaw()));
	    }
	    Methods.saveYamls();
	  }
	  
	  @SuppressWarnings("unused")
	private void timeCheck()
	  {
		  //TODO: Remove Suppress warnings when using.
	    this.timecheckid = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, new Runnable()
	    {
	      public void run()
	      {
	        if (Arena.this.getState() == GameState.INGAME) {
	          Arena.this.check();
	        } else {
	          Bukkit.getScheduler().cancelTask(Arena.this.timecheckid);
	        }
	      }
	    }, 400L, 20L);
	  }
	  
	  public void check() {}
	  
	  public Location getLobbySpawn()
	  {
	    if (this.plugin.arenas.contains("Arenas." + getName() + ".Lobby.Spawn" + ".World"))
	    {
	      Location loc = new Location(Bukkit.getWorld(this.plugin.arenas.getString("Arenas." + getName() + ".Lobby.Spawn" + ".World")), 
	        this.plugin.arenas.getDouble("Arenas." + getName() + ".Lobby.Spawn" + ".X"), 
	        this.plugin.arenas.getDouble("Arenas." + getName() + ".Lobby.Spawn" + ".Y"), 
	        this.plugin.arenas.getDouble("Arenas." + getName() + ".Lobby.Spawn" + ".Z"));
	      loc.setPitch((float)this.plugin.arenas.getDouble("Arenas." + getName() + ".Lobby.Spawn" + ".Pitch"));
	      loc.setYaw((float)this.plugin.arenas.getDouble("Arenas." + getName() + ".Lobby.Spawn" + ".Yaw"));
	      return loc;
	    }
	    return null;
	  }
	  
	  public boolean isOn()
	  {
	    return (getState() == GameState.INGAME) || getState() == GameState.STOPPING;
	  }
	  
	 
	  
	  public List<UUID> getPlayers()
	  {
	    return this.players;
	  }
	  
	public void healAll()
	  {
	    for (UUID s : this.players) {
	      if (Bukkit.getPlayer(s) != null)
	      {
	        Bukkit.getPlayer(s).setHealth(20.0D);
	        Bukkit.getPlayer(s).setFoodLevel(20);
	      }
	    }
	  }
	  
	private void setInventories(){
		  for(UUID s : getPlayers()){
			  if(Bukkit.getPlayer(s) != null){
				  Methods.setDefaultGameInventory(Bukkit.getPlayer(s));
			  }
		  }
	  }
	  
	  
	  
	@SuppressWarnings("deprecation")
	private void setScoreboard()
	  {
	    ScoreboardManager manager = Bukkit.getScoreboardManager();
	    Scoreboard board = manager.getNewScoreboard();
	    
	    Objective main = board.registerNewObjective(ChatColor.RED + "OITC", "kills");
	    main.setDisplaySlot(DisplaySlot.SIDEBAR);
	    for (UUID s : getPlayers()) {
	      if (Bukkit.getPlayer(s) != null)
	      {
	        Player player = Bukkit.getPlayer(s);
	        
	        main.getScore(player).setScore(0);
	        
	        player.setScoreboard(board);
	      }
	    }
	    
	    scoreboard = board;
	    
	  }
	private void spawnPlayers()
	  {
	    for (UUID s : this.players) {
	      if (Bukkit.getPlayer(s) != null)
	      {
	        Player player = Bukkit.getPlayer(s);
	        Location loc = getRandomSpawn();
	        	if(loc != null){
	        		player.teleport(loc);
	        	}
	      }
	    }
	  }
	 
	  public void start()
	  {
		  if(getState() ==GameState.INGAME || getState() == GameState.STARTING || getState() == GameState.STOPPING){
			  return;
		  }
		  
	    this.counter = this.plugin.getConfig().getInt(getName() + ".Countdown");
	    
	      this.id = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, new Runnable()
	      {
	        public void run()
	        {
	          if (Arena.this.counter > 0)
	          {
	        	  setState(GameState.STARTING);
	        	  updateSigns();
	            if (Arena.this.counter == 30) {
	              sendAll(ChatColor.AQUA +""+ counter + ChatColor.GRAY + " 초 남았습니다 잠시만 기다려주세요.");
	            }
	            if (Arena.this.counter == 45) {
	              sendAll(ChatColor.AQUA +""+ counter + ChatColor.GRAY + " 초 남았습니다 잠시만 기다려주세요.");
	            }
	            if (Arena.this.counter == 15) {
	              sendAll(ChatColor.AQUA +""+ counter + ChatColor.GRAY + " 초 남았습니다 잠시만 기다려주세요.");
	            }
	            if (Arena.this.counter <= 10) {
	              sendAll(ChatColor.AQUA +""+ counter + ChatColor.GRAY + " 초 남았습니다 곧 게임이 시작됩니다.");
	            }
	            Arena.this.counter -= 1;
	          }
	          else
	          {
	            Arena.this.sendAll(ChatColor.AQUA + "게임이 시작되었습니다!");
	            setState(GameState.INGAME);
	            Arena.this.startGameTimer();
	            Arena.this.healAll();
	            
	            Arena.this.setScoreboard();
	            
	            Bukkit.getScheduler().cancelTask(Arena.this.id);
	           // Arena.this.check();
	            Arena.this.updateSigns();
	           
	            //Arena.this.timeCheck();
	            Arena.this.spawnPlayers();
	            setInventories();
	            
	            updateSigns();
	          }
	        }
	      }, 0L, 20L);
	    
	  }
	  
	public void stop()
	  {
		
		if(getState() == GameState.STARTING){
			Bukkit.getScheduler().cancelTask(id);
		}
		
		 setState(GameState.STOPPING);
		 updateSigns();
	    healAll();
	   
	    if (this.endtimeOn) {
	      Bukkit.getScheduler().cancelTask(this.endtime);
	    }
	    for (UUID s : players) {
	      if (Bukkit.getPlayer(s) != null)
	      {
	        Player player = Bukkit.getPlayer(s);
	        if (Methods.getLobby() != null) {
	          player.teleport(Methods.getLobby());
	        } else {
	          player.sendMessage(ChatColor.RED + "Error: It seems the Main Lobby has not been setup yet, please tell your server owner ASAP.");
	        }
	       
	        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
	        loadInventory(player);
	 	    player.teleport(Methods.getLobby());
			player.sendMessage(ChatColor.GREEN + "PTS MiNiGame을 즐겨주셔서 감사합니다 :)");
	 	    OITC.sendMessage(player, "당신은 메인 로비로 텔레포트 되었습니다.");
	        Arenas.removeArena(player);	      }
	    }
	    
	    
	    
	   // olist.clear();
	    this.players.clear();
	    this.endtimeOn = false;
	    
	    setState(GameState.LOBBY);
	    updateSigns();
	    
	  }
	  
	  public int getKillsToWin()
	  {
	    return this.plugin.getConfig().getInt(getName() + ".KillsToWin");
	  }
	  
	  public void startGameTimer()
	  {
	    this.endtimeOn = true;
	    this.endtime = Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable()
	    {
	      public void run()
	      {
	        Arena.this.sendAll(ChatColor.GRAY + "시간이 곧 만료됩니다!");
	        Arena.this.stop();
	      }
	    }, this.plugin.getConfig().getInt(getName() + "시간만료") * 20);
	  }
	  
	 
	  
	 
	  
	  public void updateSigns()
	  {
	    for (Location loc : getSigns()) {
	      if ((loc.getBlock().getState() instanceof Sign))
	      {
	        Sign sign = (Sign)loc.getBlock().getState();
	        int total = getPlayers().size();
	        if (this.getState() == GameState.INGAME) {
	          sign.setLine(3, ChatColor.BOLD +""+ total + "/" + getMaxPlayers());
	        } else {
	          sign.setLine(3, ChatColor.BOLD + ""+getPlayers().size() + "/" + getMaxPlayers());
	        }
	        
	       
	        if (getState() == GameState.INGAME) {
	          sign.setLine(2, ChatColor.DARK_RED + "Ingame");
	        } else {
	        	if(getState() == GameState.LOBBY){
	          sign.setLine(2, ChatColor.GREEN + "대기중");
	        	}else{
	        		if(getState() == GameState.STOPPING){
	      	          sign.setLine(2, ChatColor.RED + "정지중");
	        		}else{
	        			if(getState() == GameState.STARTING){
	        		          sign.setLine(2, ChatColor.AQUA + "시작중");
	        			}
	        		}
	        	}
	        }
	        
	        
	        
	        sign.update();
	      }
	    }
	  }
	  
	  public List<Location> getSigns()
	  {
	    String ArenaName = getName();
	    List<Location> locs = new ArrayList<Location>();
	    for (int count = 1; this.plugin.arenas.contains("Arenas." + ArenaName + ".Signs." + count + ".X"); count++)
	    {
	      Location loc = new Location(Bukkit.getWorld(this.plugin.arenas.getString("Arenas." + ArenaName + ".Signs." + count + ".World")), 
	        this.plugin.arenas.getDouble("Arenas." + ArenaName + ".Signs." + count + ".X"), 
	        this.plugin.arenas.getDouble("Arenas." + ArenaName + ".Signs." + count + ".Y"), 
	        this.plugin.arenas.getDouble("Arenas." + ArenaName + ".Signs." + count + ".Z"));
	      locs.add(loc);
	    }
	    return locs;
	  }
	  
	  public void addSign(Location loc)
	  {
	    String Arena = getName();
	    int counter = this.plugin.arenas.getInt("Arenas." + Arena + ".Signs.Counter");
	    counter++;
	    this.plugin.arenas.addDefault("Arenas." + Arena + ".Signs." + counter + ".X", Double.valueOf(loc.getX()));
	    this.plugin.arenas.addDefault("Arenas." + Arena + ".Signs." + counter + ".Y", Double.valueOf(loc.getY()));
	    this.plugin.arenas.addDefault("Arenas." + Arena + ".Signs." + counter + ".Z", Double.valueOf(loc.getZ()));
	    this.plugin.arenas.addDefault("Arenas." + Arena + ".Signs." + counter + ".World", loc.getWorld().getName());
	    
	    this.plugin.arenas.set("Arenas." + Arena + ".Signs.Counter", Integer.valueOf(counter));
	    
	    Methods.saveYamls();
	  }
	  
	  public void removeSign(Location loc)
	  {
		  String ArenaName = getName();
		  for (int count = 1; this.plugin.arenas.contains("Arenas." + ArenaName + ".Signs." + count + ".X"); count++)
		    {
		      Location loc2 = new Location(Bukkit.getWorld(this.plugin.arenas.getString("Arenas." + ArenaName + ".Signs." + count + ".World")), 
		        this.plugin.arenas.getDouble("Arenas." + ArenaName + ".Signs." + count + ".X"), 
		        this.plugin.arenas.getDouble("Arenas." + ArenaName + ".Signs." + count + ".Y"), 
		        this.plugin.arenas.getDouble("Arenas." + ArenaName + ".Signs." + count + ".Z"));
		      
		      if(loc.getX() == loc2.getX() && loc.getY() == loc2.getY() && loc.getZ() == loc2.getZ()){
		    	  plugin.arenas.addDefault("Arenas." + ArenaName + ".Signs." + count + ".X", null);
		    	  plugin.arenas.addDefault("Arenas." + ArenaName + ".Signs." + count + ".Y", null);
		    	  plugin.arenas.addDefault("Arenas." + ArenaName + ".Signs." + count + ".Z", null);
		    	  plugin.arenas.addDefault("Arenas." + ArenaName + ".Signs." + count + ".World", null);
		    	  plugin.arenas.addDefault("Arenas." + ArenaName + ".Signs." + count, null);

				  resetSigns();
				  
				  Methods.saveYamls();
		    	  break;
		     }
		  }
	  }
	  
	  private void resetSigns(){
		  	String ArenaName = getName();
		  	int newCount = 0;
		    int counter = this.plugin.arenas.getInt("Arenas." + ArenaName + ".Signs.Counter");

		  for(int i = 0; i <= counter; i++){
			  if(plugin.arenas.contains("Arenas." + ArenaName + ".Signs." + i + ".X")){
				  newCount++;
				  double x = plugin.arenas.getDouble("Arenas." + ArenaName + ".Signs." + i + ".X");
				  double y = plugin.arenas.getDouble("Arenas." + ArenaName + ".Signs." + i + ".Y");
				  double z = plugin.arenas.getDouble("Arenas." + ArenaName + ".Signs." + i + ".Z");
				  String world = plugin.arenas.getString("Arenas." + ArenaName + ".Signs." + i + ".World");
				  
		    	  plugin.arenas.addDefault("Arenas." + ArenaName + ".Signs." + i + ".X", null);
		    	  plugin.arenas.addDefault("Arenas." + ArenaName + ".Signs." + i + ".Y", null);
		    	  plugin.arenas.addDefault("Arenas." + ArenaName + ".Signs." + i + ".Z", null);
		    	  plugin.arenas.addDefault("Arenas." + ArenaName + ".Signs." + i + ".World", null);	
				  plugin.arenas.addDefault("Arenas." + ArenaName + ".Signs." + i, null);

				  
				  plugin.arenas.addDefault("Arenas." + ArenaName + ".Signs." + newCount + ".X",
						  x);
				  plugin.arenas.addDefault("Arenas." + ArenaName + ".Signs." + newCount + ".Y",
						  y);
				  plugin.arenas.addDefault("Arenas." + ArenaName + ".Signs." + newCount + ".Z",
						  z);
				  plugin.arenas.addDefault("Arenas." + ArenaName + ".Signs." + newCount + ".World",
						 world);
				  
				  plugin.arenas.set("Arenas." + ArenaName + ".Signs.Counter", newCount);
			  }
		  }
	  
	  }
	  
	  
	  public boolean hasPlayer(Player player)
	  {
	    if (players.contains(player.getUniqueId())) {
	      return true;
	    }
	    return false;
	  }
	  
	public void addPlayer(Player player)
	  {
	    if (!players.contains(player.getUniqueId()))
	    {
	      players.add(player.getUniqueId());
	      Arenas.addArena(player, this);
	      sendAll(ChatColor.AQUA + player.getName() + ChatColor.GRAY + " 가 입장하였습니다.");
	      
	      saveInventory(player);
	      
	      if(getState() == GameState.INGAME){
	    	  Location loc = getRandomSpawn();
	    	  if(loc != null){
	    		  player.teleport(loc);
	    		  Methods.setDefaultGameInventory(player);
	    		  player.setScoreboard(scoreboard);
	    		  player.setHealth(20.0);
	    		  player.setFoodLevel(20);
	    		  
	    	  }
	    	  
	    	  
	      }else{
	      
	      Location loc = getLobbySpawn();
	      if(loc != null){
	      player.teleport(loc);
	      }else{
	    	  OITC.sendMessage(player, "Oops, It seems there is no lobby setup for this arena yet! Please contact your server admins.");
	      }
	      
	      if(canStart()){
	    	  start();
	      }
	      
	      }
	      
	      updateSigns();
	    }
	  }
	  
	public void removePlayer(Player player, LeaveReason reason)
	  {
	    if (this.players.contains(player.getUniqueId())) {
	      this.players.remove(player.getUniqueId());
	    }
	   
	    
	    loadInventory(player);
	    if(reason == LeaveReason.QUIT){
	    sendAll(ChatColor.RED + player.getName() + ChatColor.GRAY + " 가 퇴장하였습니다.");
	    }
	    
	    if(reason == LeaveReason.KICK){
		    sendAll(ChatColor.RED + player.getName() + ChatColor.GRAY + " 가 강퇴당하였습니다.");
	    }
	    if(reason == LeaveReason.DEATHS){
		    sendAll(ChatColor.RED + player.getName() + ChatColor.GRAY + " 는 제거되었습니다!");
	    }
	    
	    if(reason == LeaveReason.STOPPED){
	    	player.sendMessage(ChatColor.GREEN + "PTS MiNiGame을 즐겨주셔서 감사합니다 :)");
	    }
	    
	    player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
	    
	    Arenas.removeArena(player);
	    if (player.isInsideVehicle()) {
	      player.getVehicle().eject();
	    }
	  
	    player.teleport(Methods.getLobby());
	    OITC.sendMessage(player, "당신은 메인 로비로 텔레포트 되었습니다.");
	    updateSigns();
	    
	    
	    if (getState() == GameState.INGAME || getState() == GameState.STARTING) {
	      if(players.size() <= 1){
	    	stop();
	      }
	    }
	  }
	  
	  public int getMaxPlayers()
	  {
	    return this.plugin.getConfig().getInt(getName() + ".MaxPlayers");
	  }
	  
	  public int getAutoStartPlayers()
	  {
	    return this.plugin.getConfig().getInt(getName() + ".AutoStartPlayers");
	  }
	  
	  public boolean canStart()
	  {
	    if (getState() != GameState.INGAME && getState() != GameState.STARTING && getState() != GameState.STOPPING)
	    {
	      if (this.players.size() >= getAutoStartPlayers()) {
	        return true;
	      }else{
	      return false;
	      }
	    }
	    return false;
	  }

}
