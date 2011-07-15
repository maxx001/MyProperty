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
	
	public boolean isLocationInsideArea(Location location)
	{
		int px, pz, py;
		int x1, x2, z1, z2, y1, y2;
		
		if(Level == 0 || Size == 0) return false;
		
		if(!location.getWorld().getName().equals(World)) return false;
		
		px = location.getBlockX();
		pz = location.getBlockZ();
		py = location.getBlockY();
		
		x1 = ChestLocation.getBlockX() - (Size / 2);
		z1 = ChestLocation.getBlockZ() - (Size / 2);
		y1 = ChestLocation.getBlockY() - (Size / 2);
		
		x2 = ChestLocation.getBlockX() + (Size / 2);
		z2 = ChestLocation.getBlockZ() + (Size / 2);
		y2 = ChestLocation.getBlockY() + (Size / 2);

		if(px >= x1 && px <= x2 && pz >= z1 && pz <= z2 && py >= y1 && py <= y2){
			// Player is inside area!			
			return true;
		}
		
		return false;
	}
}
