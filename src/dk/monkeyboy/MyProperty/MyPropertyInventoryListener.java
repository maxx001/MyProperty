package dk.monkeyboy.MyProperty;

import java.util.HashMap;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;
import org.bukkitcontrib.event.inventory.InventoryCloseEvent;
import org.bukkitcontrib.event.inventory.InventoryListener;
import org.bukkitcontrib.event.inventory.InventoryOpenEvent;

public class MyPropertyInventoryListener extends InventoryListener{
	MyProperty plugin;
	
	public MyPropertyInventoryListener(MyProperty ref)
	{
		plugin = ref;
	}
	
	@Override
	public void onInventoryOpen(InventoryOpenEvent event)
	{
		if(!plugin.isChestMyPropertyChest(event.getLocation().getBlock()).equals(event.getPlayer().getName())){
			// Do not allow this player to open another players MyProperty container!
			event.getPlayer().sendMessage("This is not your property!");
			event.setCancelled(true);
		}
	}
	
	@Override
	public void onInventoryClose(InventoryCloseEvent event)
	{
		Block wallSign;
		Block chest = event.getLocation().getBlock();
		int x, y, z;
		int protectionSize = 0;
		
		x = chest.getX();
		y = chest.getY();
		z = chest.getZ();
		
		System.out.print("Block type for inventory: " + chest.getTypeId());
		
		if(chest.getTypeId() == 54){
			// The inventory belongs to a chest. We need to check if
			// it has a sign that says [Property] in the first line
			wallSign = plugin.getServer().getWorld("world").getBlockAt(x+1, y, z);
			if(wallSign.getTypeId() != 68){
				wallSign = plugin.getServer().getWorld("world").getBlockAt(x-1, y, z);
				if(wallSign.getTypeId() != 68){
					wallSign = plugin.getServer().getWorld("world").getBlockAt(x, y, z+1);
					if(wallSign.getTypeId() != 68){
						wallSign = plugin.getServer().getWorld("world").getBlockAt(x, y, z-1);
						if(wallSign.getTypeId() != 68){
							// Sign was not placed beside a chest at all so just exit
							// the event.
							return;						
						}
					}
				}
			}
			
			// If get to this point, we have found a wall sign!
			System.out.print("Wall Sign found!");
			Sign sign = (Sign)wallSign.getState();
			
			if(sign.getLine(0).equals("[Property]")){
				// Sign is a MyProperty sign
				System.out.print("The sign is recognized as a MyProperty sign!");
				
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
					protectionSize = 12 + (amount * ((amount/4)+1));
					sign.setLine(2, "Level " + amount);
					sign.setLine(3, "Size: " + protectionSize);
				} else {
					sign.setLine(2, "Level 0");
					sign.setLine(3, "Size: 0");
				}
				sign.update();
			}
		}
	}
}
