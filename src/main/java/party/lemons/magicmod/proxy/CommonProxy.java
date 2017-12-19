package party.lemons.magicmod.proxy;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import party.lemons.magicmod.MagicMod;
import party.lemons.magicmod.api.capability.PlayerData;
import party.lemons.magicmod.config.ModConstants;
import party.lemons.magicmod.entity.EntityFirestorm;
import party.lemons.magicmod.entity.EntityFirestormProjectile;
import party.lemons.magicmod.entity.EntityPhysicsBlock;
import party.lemons.magicmod.network.ModNetwork;

/**
 * Created by Sam on 10/12/2017.
 */
public class CommonProxy
{
	public void preInit(FMLPreInitializationEvent event)
	{
		ModNetwork.initNetwork();
		CapabilityManager.INSTANCE.register(PlayerData.class, new PlayerData.Storage(), PlayerData.Impl.class);
		int id = 1;
		EntityRegistry.registerModEntity(new ResourceLocation(ModConstants.MODID + ":physicsblock"), EntityPhysicsBlock.class, "physicsblock", id++, MagicMod.instance, 64, 3, true);
		EntityRegistry.registerModEntity(new ResourceLocation(ModConstants.MODID + ":firestormproj"), EntityFirestormProjectile.class, "firestormproj", id++, MagicMod.instance, 64, 3, true);
		EntityRegistry.registerModEntity(new ResourceLocation(ModConstants.MODID + ":firestorm"), EntityFirestorm.class, "firestorm", id++, MagicMod.instance, 64, 3, true);
	}

	public void init(FMLInitializationEvent event)
	{
	}

	public void postInit(FMLPostInitializationEvent event)
	{
	}
}
