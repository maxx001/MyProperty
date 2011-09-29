package dk.monkeyboy.MyProperty;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.event.inventory.InventoryCloseEvent;
import org.getspout.spoutapi.event.inventory.InventoryListener;
import org.getspout.spoutapi.event.inventory.InventoryOpenEvent;

public class MyPropertyInventoryListener extends InventoryListener{
	MyProperty plugin;
	
	public MyPropertyInventoryListener(MyProperty ref)
	{
		plugin = ref;
	}
	
	@Override
	public void onInventoryOpen(InventoryOpenEvent event)
	{
		// Exit event if player inventory was opened
		if(event.getLocation() == null) return;
		
		String chestOwner = plugin.isChestMyPropertyChest(event.getLocation().getBlock());
		
		if(chestOwner != null){
			if(!chestOwner.equals(event.getPlayer().getName())){
				// Do not allow this player to open another players MyProperty container!
				event.getPlayer().sendMessage("Denne grundkiste tilhører ikke dig");
				event.setCancelled(true);
			}
		}
	}
	
	@Override
	public void onInventoryClose(InventoryCloseEvent event)
	{
		// Exit event if player inventory was opened
		if(event.getLocation() == null) return;
		
		Block wallSign;
		Block chest = event.getLocation().getBlock();
		int x, y, z;
		int protectionSize = 0;
		String worldName = event.getPlayer().getWorld().getName();
		
		x = chest.getX();
		y = chest.getY();
		z = chest.getZ();
		
		if(chest.getTypeId() == 54){
			// The inventory belongs to a chest. We need to check if
			// it has a sign that says [Property] in the first line
			wallSign = plugin.getServer().getWorld(worldName).getBlockAt(x+1, y, z);
			if(wallSign.getTypeId() != 68){
				wallSign = plugin.getServer().getWorld(worldName).getBlockAt(x-1, y, z);
				if(wallSign.getTypeId() != 68){
					wallSign = plugin.getServer().getWorld(worldName).getBlockAt(x, y, z+1);
					if(wallSign.getTypeId() != 68){
						wallSign = plugin.getServer().getWorld(worldName).getBlockAt(x, y, z-1);
						if(wallSign.getTypeId() != 68){
							// Sign was not placed beside a chest at all so just exit
							// the event.
							return;						
						}
					}
				}
			}
			
			// If get to this point, we have found a wall sign!
			Sign sign = (Sign)wallSign.getState();
			
			if(sign.getLine(0).equalsIgnoreCase("[Grund]")){
				// Sign is a MyProperty sign
				
				// Ok now that we know that this chest is a MyProperty chest we
				// can check how many MyProperty enabled items it contains...
				// For now this only means golden apples.
				HashMap<Integer, ? extends ItemStack> items = event.getInventory().all(Material.GOLDEN_APPLE);
				int amount = 0;
				
				for(ItemStack itemStack : items.values()){
					if(itemStack.getType() == Material.GOLDEN_APPLE){
						amount = amount + itemStack.getAmount();
					}
				}
				
				if(amount > 0){
					protectionSize = plugin.minSize + (amount * plugin.multiplier);
					if(protectionSize % 2 == 0) protectionSize = protectionSize + 1;
					sign.setLine(2, "Level " + amount);
					sign.setLine(3, "Areal: " + protectionSize);
				} else {
					protectionSize = 0;
					sign.setLine(2, "Level 0");
					sign.setLine(3, "Areal: 0");
				}
				sign.update();
				
				for(PropertyClass ps : plugin.properties)
				{
					if(ps.Owner.equals(event.getPlayer().getName()) && ps.World.equals(worldName)){
						if(x == ps.ChestLocation.getBlockX() 
								&& y == ps.ChestLocation.getBlockY() 
								&& z == ps.ChestLocation.getBlockZ()){
							// Found the correct chest
							ps.Size = protectionSize;
							ps.Level = amount;
							
							break;
						}
					}
				}
				// Save change to disk
				plugin.Save();
			}
		}
	}
}
