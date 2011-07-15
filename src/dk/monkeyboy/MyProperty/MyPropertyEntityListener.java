package dk.monkeyboy.MyProperty;


import java.util.HashMap;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.block.ContainerBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
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
	public void onEntityDamage(EntityDamageEvent event)
	{
		if(!(event.getEntity() instanceof Player)) return;
		
		Location l = event.getEntity().getLocation();
		
		for(PropertyClass p : plugin.properties)
		{
			if(p.isLocationInsideArea(l)){
				ContainerBlock chest = (ContainerBlock)p.ChestLocation.getBlock().getState();
				Inventory i = chest.getInventory();

				if(i.contains(351)){
					// There is some kind of dye in the inventory, now we need to find
					// out if it's Lapis Lazuli
					HashMap<Integer, ? extends ItemStack> items = i.all(351);
					
					for (ItemStack is : items.values())
					{
						if(is.getData().getData() == 4){
							// This stack contains Lapis Lazuli dye!
							event.setCancelled(true);
							
							// There is a small chance that this protection destroys one gun
							// powder item.
							Random r = new Random();
							
							if(r.nextInt(100) < 3){
								// There is a 3% chance that this protection uses up one Lapis Lazuli
								is.setAmount(is.getAmount() - 1);
							}
							return;
						}
					}
				}
			}
		}
		
		
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
					
					if(r.nextInt(100) < 10){
						// There is a 10% chance that this protection uses up one gun powder
						items.setAmount(items.getAmount() - 1);
					}
				}
			}
		}
	}
}
