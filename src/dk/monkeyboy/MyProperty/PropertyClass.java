package dk.monkeyboy.MyProperty;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PropertyClass {
	public String Owner;
	public ArrayList<Player> Visitors = new ArrayList<Player>();
	public Location ChestLocation;
	public int Level;
	public int Size;
	public String World;
	
	public boolean isPlayerInsideArea(Player player)
	{
		int px, pz;
		int x1, x2, z1, z2;
		
		if(Level == 0 || Size == 0) return false;
		
		if(!player.getWorld().getName().equals(World)) return false;
		
		px = player.getLocation().getBlockX();
		pz = player.getLocation().getBlockZ();
		
		x1 = ChestLocation.getBlockX() - (Size / 2);
		z1 = ChestLocation.getBlockZ() - (Size / 2);
		
		x2 = ChestLocation.getBlockX() + (Size / 2);
		z2 = ChestLocation.getBlockZ() + (Size / 2);
		
		if(px >= x1 && px <= x2 && pz >= z1 && pz <= z2){
			// Player is inside area!			
			return true;
		}
		
		return false;
	}
}
