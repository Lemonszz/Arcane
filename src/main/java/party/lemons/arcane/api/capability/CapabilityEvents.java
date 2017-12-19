package party.lemons.arcane.api.capability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import party.lemons.arcane.config.ArcaneConstants;

/**
 * Created by Sam on 11/12/2017.
 */
@Mod.EventBusSubscriber
public class CapabilityEvents
{
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void attachCaps(AttachCapabilitiesEvent<Entity> event)
	{
		if(event.getObject() instanceof EntityPlayer)
		{
			event.addCapability(new ResourceLocation(ArcaneConstants.MODID, "spell_cap"), new PlayerData.Provider());
		}
	}
}
