package party.lemons.arcane.spell.action;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import party.lemons.arcane.api.action.PlayerAction;
import party.lemons.arcane.config.ArcaneConstants;

/**
 * Created by Sam on 24/12/2017.
 */
@Mod.EventBusSubscriber
public class ArcaneActions
{
	public static PlayerAction RECALL_UP, RECALL_DOWN;

	@SubscribeEvent
	public static void onActionRegister(RegistryEvent.Register<PlayerAction> event)
	{
		RECALL_UP = new ActionRecall.RecallUp().setRegistryName(ArcaneConstants.MODID, "recallup");
		RECALL_DOWN = new ActionRecall.RecallDown().setRegistryName(ArcaneConstants.MODID, "recalldown");

		event.getRegistry().registerAll(
				RECALL_UP,
				RECALL_DOWN
		);
	}
}
