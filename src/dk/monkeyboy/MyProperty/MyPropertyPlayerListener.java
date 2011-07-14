package dk.monkeyboy.MyProperty;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MyPropertyPlayerListener extends PlayerListener {
	private final MyProperty plugin;
	
	public MyPropertyPlayerListener(MyProperty ref)
	{
		plugin = ref;
	}
	
	@Override
	public void onPlayerMove(PlayerMoveEvent event)
	{
		Player player = event.getPlayer();
		boolean playerFound = false;
		
		for(PropertyClass ps : plugin.properties)
		{
			if(ps.isPlayerInsideArea(player))
			{
				for(Player p : ps.Visitors)
				{
					if(p.getName().equals(player.getName())) playerFound = true;
				}
				
				if(!playerFound){
					// Player just entered the area, add player to visitors list
					ps.Visitors.add(player);
					player.sendMessage("Dette område tilhører " + ps.Owner);
					
					return;
				}
			} else {
				for(Player p : ps.Visitors)
				{
					if(p.getName().equals(player.getName())){
						// Player just left the area, remove player from visitors list
						ps.Visitors.remove(player);
						player.sendMessage("Du har forladt " + ps.Owner + "'s område");
						
						return;
					}
				}
			}
		}
	}
}
