package me.artish1.OITC.Listeners;

import me.artish1.OITC.OITC;
import me.artish1.OITC.Arena.Arena;
import me.artish1.OITC.Arena.Arenas;
import me.artish1.OITC.Arena.LeaveReason;
import me.artish1.OITC.Utils.Methods;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand.EnumClientCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class GameListener implements Listener{
	
	OITC plugin;
	
	public GameListener(OITC plugin) {
	
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e){
		final Player player = e.getPlayer();
		
		if(Arenas.isInArena(player)){
			e.setCancelled(true);
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

				public void run() {
					player.updateInventory();
					
				}
				
			}, 1L);
			
		}
	}
	
	
	 @EventHandler
	 public void onHit(EntityDamageByEntityEvent e)
	  {
	    if (((e.getEntity() instanceof Player)) && ((e.getDamager() instanceof Arrow)))
	    {
	      Arrow arrow = (Arrow)e.getDamager();
	      if ((arrow.getShooter() instanceof Player))
	      {
	        Player attacker = (Player)arrow.getShooter();
	        Player player = (Player)e.getEntity();
	        if ((Arenas.isInArena(player)) && (Arenas.isInArena(attacker)))
	        {
	          Arena arena = Arenas.getArena(player);
	          if (arena.isOn()) {
	            if (!player.getName().equalsIgnoreCase(attacker.getName())) {
	              e.setDamage(100.0D);
	            } else {
	              e.setCancelled(true);
	            }
	          }
	        }
	      }
	    }
	  }
	
	 
	@EventHandler
	public void onProjHit(ProjectileHitEvent e){

		if(e.getEntity() instanceof Arrow){

			Arrow arrow = (Arrow) e.getEntity();
			
			if(arrow.getShooter() instanceof Player){
				
				Player shooter = (Player) arrow.getShooter();
				
				if(Arenas.isInArena(shooter)){
					Arena arena = Arenas.getArena(shooter);
					if(arena.isOn()){
						arrow.remove();
					}
				}
			}
		}
	}
	
	@EventHandler			(priority = EventPriority.HIGHEST)
	public void onRespawn(PlayerRespawnEvent e){
		Player player = e.getPlayer();
		
		if(!Arenas.isInArena(player)){
			return;
		}
		
		Arena arena = Arenas.getArena(player);
		if(arena.isOn()){
			e.setRespawnLocation(arena.getRandomSpawn());
			Methods.setDefaultGameInventory(player);
			player.updateInventory();
		}
		
		
		
	}
	
	@EventHandler		(priority = EventPriority.HIGHEST)
	public void onDeath(PlayerDeathEvent e){
		final Player player = e.getEntity();
		if(Arenas.isInArena(player)){
			Arena arena = Arenas.getArena(player);
			if(arena.isOn()){
				e.getDrops().clear();
				e.setDeathMessage("");
				e.setDroppedExp(0);
				
				Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable()
			      {
			        public void run()
			        {
			          PacketPlayInClientCommand packet = new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN);
			          ((CraftPlayer)player).getHandle().playerConnection.a(packet);
			        }
			      }, 1);
				
				if(player.getKiller() != null){
					Player killer = player.getKiller();
					onPlayerKill(killer, player);
					
				}
			}
		}
		
	}
	
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e){
		Player player = e.getPlayer();
		if(!Arenas.isInArena(player)){
			return;
		}
		
		
		Arena arena = Arenas.getArena(player);
		player.teleport(Methods.getLobby());
		player.getInventory().clear();
		arena.removePlayer(player, LeaveReason.QUIT);
		
	}
	
	
	
	public void onPlayerKill(Player killer, Player player){
		Arena arena = Arenas.getArena(killer);
		killer.sendMessage(ChatColor.GRAY + "You have killed " + ChatColor.AQUA + player.getName());
		player.sendMessage(ChatColor.DARK_RED + killer.getName() + ChatColor.GRAY + " 가 당신을 죽였습니다!");
		
		Methods.addArrow(killer);
		
		Scoreboard board = killer.getScoreboard();
		 @SuppressWarnings("deprecation")
		 Score score = board.getObjective(DisplaySlot.SIDEBAR).getScore(killer);
         int kills = score.getScore();
         kills++;
		score.setScore(kills);
		
		if(kills >= arena.getKillsToWin()){
			arena.sendAll("");
			arena.sendAll("");
			arena.sendAll("");
			arena.sendAll("");
			arena.sendAll("");
			arena.sendAll("");
			arena.sendAll("");
			arena.sendAll("");
			arena.sendAll("");

			arena.sendAll(ChatColor.GREEN +"================" + ChatColor.GRAY + "[" + ChatColor.AQUA + "OITC" + ChatColor.GRAY + "]" +ChatColor.GREEN +  "================");


			arena.sendAll(ChatColor.RED + killer.getName() + ChatColor.GRAY +
					" 가 목표킬수에 도달하였습니다. " + ChatColor.GOLD + arena.getKillsToWin() + ChatColor.GRAY + 
					" 그리고 이 게임에서 승리하였습니다.: " + ChatColor.AQUA + arena.getName());
			arena.sendAll(ChatColor.GREEN +"================" + ChatColor.GRAY + "[" + ChatColor.AQUA + "OITC" + ChatColor.GRAY + "]" +ChatColor.GREEN +  "================");
			
			arena.sendAll("");
			arena.sendAll("");

			
			arena.stop();
			
			
		}
		
	}
	
	
	@EventHandler	(priority = EventPriority.HIGH)
	public void onCommand(PlayerCommandPreprocessEvent e){
		Player player = e.getPlayer();
		if(player.hasPermission("oitc.admin") || player.isOp()){
			return;
		}
		
		if(Arenas.isInArena(player)){
			Arena arena = Arenas.getArena(player);
			if(arena.isOn()){
				if(!e.getMessage().equalsIgnoreCase("/oitc") &&
						!e.getMessage().equalsIgnoreCase("/oitc lobby") &&
						!e.getMessage().equalsIgnoreCase("/oitc leave")){
					
					e.setCancelled(true);
                    OITC.sendMessage(player, "/oitc commands 이외의 커맨드는 사용이 불가능합니다.");
					OITC.sendMessage(player, "이 게임을 나가길 원한다면 " + ChatColor.RED + "/oitc leave , OR /oitc lobby 명령어를 사용하세요");
				}
				
				
			}
		}
		
		
		
	}
	
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e){
		Player player = e.getPlayer();
		if(!Arenas.isInArena(player))
			return;
		
		Arena arena = Arenas.getArena(player);
		if(arena.isOn()){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		Player player = e.getPlayer();
		if(!Arenas.isInArena(player))
			return;
		
		Arena arena = Arenas.getArena(player);
		if(arena.isOn()){
			if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
				if(e.getClickedBlock().getType() == Material.CHEST){
					e.setUseInteractedBlock(Result.DENY);
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e){
		Player player = e.getPlayer();
		if(!Arenas.isInArena(player))
			return;
	
		Arena arena = Arenas.getArena(player);
		if(arena.isOn()){
			e.setCancelled(true);
		}
	
	}
	
	
}
