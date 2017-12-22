package party.lemons.arcane;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import party.lemons.arcane.config.ArcaneConstants;
import party.lemons.arcane.proxy.CommonProxy;

/**
 * Created by Sam on 10/12/2017.
 */
@Mod(modid = ArcaneConstants.MODID, name = ArcaneConstants.MODNAME, version = ArcaneConstants.VERSION)
public class Arcane
{
	@Mod.Instance(ArcaneConstants.MODID)
	public static Arcane instance;

	@SidedProxy(clientSide = "party.lemons.arcane.proxy.ClientProxy", serverSide = "party.lemons.arcane.proxy.ServerProxy")
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

	public static final Logger logger = LogManager.getLogger(ArcaneConstants.MODID);
}
