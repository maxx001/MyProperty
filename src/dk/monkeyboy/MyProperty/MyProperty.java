package dk.monkeyboy.MyProperty;

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
		}
		
		PluginDescriptionFile pdfFile = this.getDescription();
		System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
	}
}
