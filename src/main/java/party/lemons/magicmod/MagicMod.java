package party.lemons.magicmod;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import party.lemons.magicmod.config.ModConstants;
import party.lemons.magicmod.proxy.CommonProxy;

/**
 * Created by Sam on 10/12/2017.
 */
@Mod(modid = ModConstants.MODID, name = ModConstants.MODNAME, version = ModConstants.VERSION)
public class MagicMod
{
	@Mod.Instance(ModConstants.MODID)
	public static MagicMod instance;

	@SidedProxy(clientSide = "party.lemons.magicmod.proxy.ClientProxy", serverSide = "party.lemons.magicmod.proxy.ServerProxy")
	public static CommonProxy proxy;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		proxy.preInit(event);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.init(event);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		proxy.postInit(event);
	}
}
