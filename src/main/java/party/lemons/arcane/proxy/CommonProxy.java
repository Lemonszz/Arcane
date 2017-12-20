package party.lemons.arcane.proxy;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import party.lemons.arcane.Arcane;
import party.lemons.arcane.api.capability.PlayerData;
import party.lemons.arcane.config.ArcaneConstants;
import party.lemons.arcane.entity.EntityArrowTurret;
import party.lemons.arcane.entity.EntityFirestorm;
import party.lemons.arcane.entity.EntityFirestormProjectile;
import party.lemons.arcane.entity.EntityPhysicsBlock;
import party.lemons.arcane.network.ArcaneNetwork;

/**
 * Created by Sam on 10/12/2017.
 */
public class CommonProxy
{
	public void preInit(FMLPreInitializationEvent event)
	{
		ArcaneNetwork.initNetwork();
		CapabilityManager.INSTANCE.register(PlayerData.class, new PlayerData.Storage(), PlayerData.Impl.class);
		int id = 1;
		EntityRegistry.registerModEntity(new ResourceLocation(ArcaneConstants.MODID + ":physicsblock"), EntityPhysicsBlock.class, "physicsblock", id++, Arcane.instance, 64, 3, true);
		EntityRegistry.registerModEntity(new ResourceLocation(ArcaneConstants.MODID + ":firestormproj"), EntityFirestormProjectile.class, "firestormproj", id++, Arcane.instance, 64, 3, true);
		EntityRegistry.registerModEntity(new ResourceLocation(ArcaneConstants.MODID + ":firestorm"), EntityFirestorm.class, "firestorm", id++, Arcane.instance, 64, 3, true);
		EntityRegistry.registerModEntity(new ResourceLocation(ArcaneConstants.MODID + ":arrowturret"), EntityArrowTurret.class, "arrowturret", id++, Arcane.instance, 64, 3, true);
	}

	public void init(FMLInitializationEvent event)
	{
	}

	public void postInit(FMLPostInitializationEvent event)
	{
	}
}
