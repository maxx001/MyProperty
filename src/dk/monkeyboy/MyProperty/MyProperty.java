package dk.monkeyboy.MyProperty;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class MyProperty extends JavaPlugin {

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEnable() {
		// TODO Auto-generated method stub
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.SIGN_CHANGE, new MyPropertyBlockListener(this), Priority.Lowest, this);
		
		if(pm.getPlugin("BukkitContrib") != null){
			pm.registerEvent(Event.Type.CUSTOM_EVENT, new MyPropertyInventoryListener(this), Priority.Lowest, this);
		} else {
			System.out.print("[MyProperty] Error - Could not find BukkitContrib!");
		}
		
		PluginDescriptionFile pdfFile = this.getDescription();
		System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
	}
	
	public String isChestMyPropertyChest(Block chest)
	{
		Block wallSign;
		int x, y, z;
		
		x = chest.getX();
		y = chest.getY();
		z = chest.getZ();
		
		if(chest.getTypeId() == 54){
			// The inventory belongs to a chest. We need to check if
			// it has a sign that says [Property] in the first line
			wallSign = getServer().getWorld("world").getBlockAt(x+1, y, z);
			if(wallSign.getTypeId() != 68){
				wallSign = getServer().getWorld("world").getBlockAt(x-1, y, z);
				if(wallSign.getTypeId() != 68){
					wallSign = getServer().getWorld("world").getBlockAt(x, y, z+1);
					if(wallSign.getTypeId() != 68){
						wallSign = getServer().getWorld("world").getBlockAt(x, y, z-1);
						if(wallSign.getTypeId() != 68){
							// Sign was not placed beside a chest at all so just exit
							// the event.
							return null;						
						}
					}
				}
			}
			
			Sign sign = (Sign)wallSign.getState();
			
			if(sign.getLine(0).equals("[Property]")){
				// Sign is a MyProperty sign				
				return sign.getLine(1);
			}
		}
		return null;		
	}
}
