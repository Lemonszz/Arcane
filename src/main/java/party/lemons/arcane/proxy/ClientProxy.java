package party.lemons.arcane.proxy;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import party.lemons.arcane.block.tileentity.render.ConnectionRender;
import party.lemons.arcane.block.tileentity.TileEntityManaNode;
import party.lemons.arcane.client.KeyBindings;
import party.lemons.arcane.entity.EntityArrowTurret;
import party.lemons.arcane.entity.render.RenderArrowTurret;
import party.lemons.arcane.entity.render.RenderFirestorm;
import party.lemons.arcane.entity.EntityFirestormProjectile;
import party.lemons.arcane.entity.EntityPhysicsBlock;
import party.lemons.arcane.entity.render.RenderPhysicsBlock;

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
		RenderingRegistry.registerEntityRenderingHandler(EntityArrowTurret.class, RenderArrowTurret::new);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityManaNode.class, new ConnectionRender());
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
