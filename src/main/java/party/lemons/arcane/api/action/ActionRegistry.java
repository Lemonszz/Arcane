package party.lemons.arcane.api.action;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

/**
 * Created by Sam on 11/12/2017.
 */
@Mod.EventBusSubscriber
public class ActionRegistry
{
	public static IForgeRegistry<PlayerAction> ACTION_REGISTRY;

	@SubscribeEvent
	public static void createRegistry(RegistryEvent.NewRegistry event)
	{
		ACTION_REGISTRY =
				new RegistryBuilder<PlayerAction>()
				.setType(PlayerAction.class)
				.setName(new ResourceLocation("arcane", "actions"))
				.create();
	}
}
