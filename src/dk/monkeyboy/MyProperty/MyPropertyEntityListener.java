package dk.monkeyboy.MyProperty;


import java.util.Random;

import org.bukkit.Location;
import org.bukkit.block.ContainerBlock;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MyPropertyEntityListener extends EntityListener {
	private final MyProperty plugin;
	
	public MyPropertyEntityListener(MyProperty ref)
	{
		plugin = ref;
	}
	
	@Override
	public void onEntityExplode(EntityExplodeEvent event)
	{
		Location l = event.getLocation();

		for(PropertyClass p : plugin.properties)
		{
			if(p.isLocationInsideArea(l)){
				ContainerBlock chest = (ContainerBlock)p.ChestLocation.getBlock().getState();
				Inventory i = chest.getInventory();
				
				if(i.contains(289)){
					// There is gun powder in the inventory so disable creeper explosions!
					event.setCancelled(true);
					ItemStack items = i.getItem(i.first(289));
					
					// There is a small chance that this protection destroys one gun
					// powder item.
					Random r = new Random();
					
					if(r.nextInt(100) < 99){
						items.setAmount(items.getAmount() - 1);
					}
				}
			}
		}
	}
}
