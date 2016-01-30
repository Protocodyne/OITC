package me.artish1.OITC.Listeners;

import me.artish1.OITC.OITC;
import me.artish1.OITC.Arena.Arena;
import me.artish1.OITC.Arena.Arenas;
import me.artish1.OITC.Arena.GameState;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignListener implements Listener{
	
	OITC plugin;
	
	public SignListener(OITC plugin) {
		this.plugin = plugin;	
	}
	
	@EventHandler
	public void onSignBreak(BlockBreakEvent e){
		if(e.getBlock().getState() instanceof Sign){
			Sign sign = (Sign) e.getBlock().getState();
			if(e.getPlayer().hasPermission("oitc.admin")){
				if(sign.getLine(0).equalsIgnoreCase(ChatColor.DARK_GRAY + "[" + ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "OITC" + ChatColor.DARK_GRAY + "]")){
				if(e.getPlayer().isSneaking()){
					
					for(Arena arena : Arenas.getArenas()){
						if(sign.getLine(1).equalsIgnoreCase( ChatColor.BOLD + arena.getName() )){
							arena.removeSign(e.getBlock().getLocation());
							OITC.sendMessage(e.getPlayer(), "You have removed a sign from " + ChatColor.DARK_AQUA + arena.getName());
							
							break;
						}
					}
					
				}else{
					e.setCancelled(true);
					sign.update(true);
					OITC.sendMessage(e.getPlayer(), "If you want to break this sign, please sneak + break!");
				}
				
			}
			}else{
				if(sign.getLine(0).equalsIgnoreCase(ChatColor.DARK_GRAY + "[" + ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "OITC" + ChatColor.DARK_GRAY + "]")){
					e.setCancelled(true);
					sign.update(true);
				}
				
			}
			
			
			
			
			
		}
	}
	
	@EventHandler
	public void onSignCreate(SignChangeEvent e){
		Player player = e.getPlayer();
	    if ((e.getLine(0).equalsIgnoreCase("oitc")) && 
	      (player.hasPermission("oitc.admin"))) {
	      for (Arena arena : Arenas.getArenas()) {
	    	  
	        if (e.getLine(1).equalsIgnoreCase(arena.getName()))
	        {

	        	e.setLine(0, ChatColor.DARK_GRAY + "[" + ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "OITC" + ChatColor.DARK_GRAY + "]");
	          e.setLine(1, ChatColor.BOLD + arena.getName());
	          e.setLine(3, ChatColor.BOLD + ""+arena.getPlayers().size() + "/" + arena.getMaxPlayers());
	          
	          if (arena.getState() == GameState.INGAME) {
		          e.setLine(2, ChatColor.DARK_RED + "Ingame");
		        } else {
		        	if(arena.getState() == GameState.LOBBY){
		          e.setLine(2, ChatColor.GREEN + "대기중");
		        	}else{
		        		if(arena.getState() == GameState.STOPPING){
		      	          e.setLine(2, ChatColor.RED + "정지중");
		        		}else{
		        			if(arena.getState() == GameState.STARTING){
		        		          e.setLine(2, ChatColor.AQUA + "시작중");
		        			}
		        		}
		        	}
		        }
	          
	          
	
	          arena.addSign(e.getBlock().getLocation());
	          arena.updateSigns();
	          player.sendMessage(ChatColor.GRAY + "You made a join sign for " + ChatColor.GOLD + arena.getName());
	        }
	      }
	    }
	}
	
	
	
	@EventHandler
	public void onSignInteract(PlayerInteractEvent e){
		Player player = e.getPlayer();
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.hasBlock() && e.getClickedBlock().getState()  instanceof Sign){
			Sign sign = (Sign) e.getClickedBlock().getState();
				if(sign.getLine(0).equalsIgnoreCase(ChatColor.DARK_GRAY + "[" + ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + 
			        "OITC" + ChatColor.DARK_GRAY + "]"))
				{
			        sign.update();
			        
			        if(Arenas.isInArena(player)){
			        	player.sendMessage(ChatColor.RED + "당신은 이미 해당맵 안에 있습니다!");
			        	player.sendMessage(ChatColor.GRAY + "현재맵에서 나가고 싶다면 /oitc leave 명령어를 사용하세요")
			        	return;
			        }
			        
			        	for(Arena arena : Arenas.getArenas()){
			        		if(sign.getLine(1).equalsIgnoreCase( ChatColor.BOLD + arena.getName() )){
			        		if(!arena.hasPlayer(player)){
			        			//if(!arena.isOn()){
			        				if(arena.getMaxPlayers() > arena.getPlayers().size()){
			        					
			        					arena.addPlayer(player);
			        					
			        					
			        					
			        				}else{
			        					player.sendMessage(ChatColor.RED + "죄송합니다! 현재맵은 꽉찼습니다!");
			        				}
			        				
			        				
			        			//}else{
			        				//player.sendMessage(ChatColor.RED + "Sorry! That Arena is " + arena.getState().toString());
			        			//}
			        		}
			        		
			        		break;
			        	}
			        }
				}
		}
		
	}
	
	
}
