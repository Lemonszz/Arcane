package party.lemons.arcane.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import party.lemons.arcane.api.capability.PlayerData;
import party.lemons.arcane.api.spell.Spell;
import party.lemons.arcane.api.spell.SpellUtil;
import party.lemons.arcane.spell.Spells;

/**
 * Created by Sam on 11/12/2017.
 */
@Mod.EventBusSubscriber
public class NetworkEvents
{
	@SubscribeEvent
	public static void syncSpellCap(PlayerEvent.PlayerLoggedInEvent event)
	{
		SpellUtil.syncData(event.player);
	}
}
