package party.lemons.magicmod.proxy;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import party.lemons.magicmod.client.KeyBindings;
import party.lemons.magicmod.client.render.RenderFirestorm;
import party.lemons.magicmod.entity.EntityFirestormProjectile;
import party.lemons.magicmod.entity.EntityPhysicsBlock;
import party.lemons.magicmod.entity.render.RenderPhysicsBlock;

/**
 * Created by Sam on 10/12/2017.
 */
public class ClientProxy extends CommonProxy
{
	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		super.preInit(event);
		KeyBindings.init();

		RenderingRegistry.registerEntityRenderingHandler(EntityPhysicsBlock.class, RenderPhysicsBlock::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityFirestormProjectile.class, RenderFirestorm::new);
	}

	@Override
	public void init(FMLInitializationEvent event)
	{
		super.init(event);
	}

	@Override
	public void postInit(FMLPostInitializationEvent event)
	{
		super.postInit(event);
	}
}
