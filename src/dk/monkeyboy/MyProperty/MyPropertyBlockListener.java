package dk.monkeyboy.MyProperty;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.material.MaterialData;

public class MyPropertyBlockListener extends BlockListener {
	MyProperty plugin;
	
	public MyPropertyBlockListener(MyProperty ref)
	{
		plugin = ref;
	}
	
	@Override
	public void onSignChange(SignChangeEvent event)
	{
		Block signBlock = event.getBlock();
		Block chest;
		
		int x, y, z;
		
		x = signBlock.getX();
		y = signBlock.getY();
		z = signBlock.getZ();
		
		chest = plugin.getServer().getWorld("world").getBlockAt(x+1, y, z);
		if(chest.getTypeId() != 54){
			chest = plugin.getServer().getWorld("world").getBlockAt(x-1, y, z);
			if(chest.getTypeId() != 54){
				chest = plugin.getServer().getWorld("world").getBlockAt(x, y, z+1);
				if(chest.getTypeId() != 54){
					chest = plugin.getServer().getWorld("world").getBlockAt(x, y, z-1);
					if(chest.getTypeId() != 54){
						// Sign was not placed beside a chest at all so just exit
						// the event.
						return;						
					}
				}
			}
		}
		
		if(event.getLine(0).equals("[Property]")){
			// Sign is a MyProperty sign!
			BlockFace face = chest.getFace(signBlock);
			
			signBlock.setType(Material.WALL_SIGN);
			switch (face)
			{
			case NORTH:
				signBlock.setData((byte)0x04);
				break;
			case SOUTH:
				signBlock.setData((byte)0x05);
				break;
			case EAST:
				signBlock.setData((byte)0x02);
				break;
			case WEST:
				signBlock.setData((byte)0x03);
				break;
			default:
				break;
			}
			signBlock.getState().setData(new MaterialData(Material.WALL_SIGN));
			Sign sign = (Sign)signBlock.getState();
			sign.setLine(0, "[Property]");
			sign.setLine(1, event.getPlayer().getName());
		}
	}
}
