package dk.monkeyboy.MyProperty;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class MyProperty extends JavaPlugin {
	public ArrayList<PropertyClass> properties = new ArrayList<PropertyClass>();
	public int multiplier = 3;
	public int minSize = 10;
	
	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEnable() {
		// TODO Auto-generated method stub
		MyPropertyBlockListener myPropertyBlockListener = new MyPropertyBlockListener(this);
		
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.SIGN_CHANGE, myPropertyBlockListener, Priority.Lowest, this);
		pm.registerEvent(Event.Type.BLOCK_BREAK, myPropertyBlockListener, Priority.Lowest, this);
		pm.registerEvent(Event.Type.BLOCK_PLACE, myPropertyBlockListener, Priority.Lowest, this);
		pm.registerEvent(Event.Type.PLAYER_MOVE, new MyPropertyPlayerListener(this), Priority.Lowest, this);
		
		
		if(pm.getPlugin("BukkitContrib") != null){
			pm.registerEvent(Event.Type.CUSTOM_EVENT, new MyPropertyInventoryListener(this), Priority.Lowest, this);
		} else {
			System.out.print("[MyProperty] Error - Could not find BukkitContrib!");
		}
		
		PluginDescriptionFile pdfFile = this.getDescription();
		System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
		
		Load();
	}
	
	public String isChestMyPropertyChest(Block chest)
	{
		Block wallSign;
		int x, y, z;
		String worldName = chest.getWorld().getName();
		
		x = chest.getX();
		y = chest.getY();
		z = chest.getZ();
		
		if(chest.getTypeId() == 54){
			// The inventory belongs to a chest. We need to check if
			// it has a sign that says [Property] in the first line
			wallSign = getServer().getWorld(worldName).getBlockAt(x+1, y, z);
			if(wallSign.getTypeId() != 68){
				wallSign = getServer().getWorld(worldName).getBlockAt(x-1, y, z);
				if(wallSign.getTypeId() != 68){
					wallSign = getServer().getWorld(worldName).getBlockAt(x, y, z+1);
					if(wallSign.getTypeId() != 68){
						wallSign = getServer().getWorld(worldName).getBlockAt(x, y, z-1);
						if(wallSign.getTypeId() != 68){
							// Sign was not placed beside a chest at all so just exit
							// the event.
							return null;						
						}
					}
				}
			}
			
			Sign sign = (Sign)wallSign.getState();
			
			if(sign.getLine(0).equals("[Grund]")){
				// Sign is a MyProperty sign				
				return sign.getLine(1);
			}
		}
		return null;		
	}
	
	public void Save()
	{
		try {
			FileWriter f = new FileWriter(new File("plugins/MyProperty/properties.dat"));
			
			for(PropertyClass p : properties)
			{
				f.write(p.Owner + ","
						+ p.World + ","
						+ p.ChestLocation.getBlockX() + "," 
						+ p.ChestLocation.getBlockY() + ","
						+ p.ChestLocation.getBlockZ() + ","
						+ p.Level + "," + p.Size + "\r" + "\n");
			}
			f.close();			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void Load()
	{
		BufferedReader b = null;
		String str = null;
		
		try {
			b = new BufferedReader(new FileReader("plugins/MyProperty/properties.dat"));
			
			while((str = b.readLine()) != null)
			{
				System.out.print(str);
				String[] values = str.split(",");
				PropertyClass p = new PropertyClass();
				
				p.Owner = values[0];
				p.World = values[1];
				p.ChestLocation = new Location(getServer().getWorld("world"), Double.valueOf(values[2]), Double.valueOf(values[3]), Double.valueOf(values[4]));
				p.Level = Integer.parseInt(values[5]);
				p.Size = Integer.parseInt(values[6]);
				
				properties.add(p);
			}
			b.close();
		} catch (FileNotFoundException e) {
			System.out.print("[MyProperty] Creating properties.dat file..");
			
			File dir = new File("plugins/MyProperty");
			
			if(!dir.exists()){
				dir.mkdir();
			}
			
			try {
				new File("plugins/MyProperty/properties.dat").createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
