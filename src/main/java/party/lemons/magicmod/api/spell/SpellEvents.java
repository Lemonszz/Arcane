package party.lemons.magicmod.api.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import party.lemons.magicmod.api.capability.PlayerData;
import party.lemons.magicmod.client.ClientUtil;
import net.minecraftforge.event.entity.player.PlayerEvent;
import party.lemons.magicmod.client.gui.GuiManaBar;
import party.lemons.magicmod.network.*;

import java.util.ArrayList;

/**
 * Created by Sam on 14/12/2017.
 */
@Mod.EventBusSubscriber
public class SpellEvents
{
	@SubscribeEvent
	public static void onTick(TickEvent.PlayerTickEvent event)
	{
		PlayerData data = event.player.getCapability(PlayerData.CAPABILITY, null);
		long holdTime = event.player.getEntityWorld().getTotalWorldTime() -  data.getHoldTime();
		if(data.isHoldingCast())
		{
			if(event.player.world.isRemote)
			{
				ClientUtil.getMouseOverExtended(50);
			}
			event.player.resetActiveHand();
			event.player.stopActiveHand();
			SpellUtil.castSpellHold(data.getSelectedSpells()[data.getSelectedIndex()], event.player, (int) holdTime);
		}
		else
		{
			if(data.getMana() < data.getMaxMana() && event.player.world.getTotalWorldTime() % 20 == 0)
			{
				data.setMana(Math.min(data.getMaxMana(), data.getMana() + 1));
				if(event.player.world.isRemote)
				{
					GuiManaBar.drawTime = 2200;
				}
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerInteract(PlayerInteractEvent e)
	{
		PlayerData data = e.getEntityPlayer().getCapability(PlayerData.CAPABILITY, null);
		if(data.isHoldingCast() && e.isCancelable())
		{
			e.getEntityPlayer().resetActiveHand();
			e.getEntityPlayer().stopActiveHand();
			e.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onPlayerRespawn(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent event)
	{
		SpellUtil.syncData(event.player);
	}

	@SubscribeEvent
	public static void onPlayerClone(PlayerEvent.Clone e)
	{
		EntityPlayer newPlayer = e.getEntityPlayer();
		EntityPlayer oldPlayer = e.getOriginal();
		NBTTagCompound tags = new NBTTagCompound();

		PlayerData oldData = oldPlayer.getCapability(PlayerData.CAPABILITY, null);
		tags = (NBTTagCompound) PlayerData.CAPABILITY.getStorage().writeNBT(PlayerData.CAPABILITY, oldData, null);
		PlayerData newData = newPlayer.getCapability(PlayerData.CAPABILITY, null);
		PlayerData.CAPABILITY.getStorage().readNBT(PlayerData.CAPABILITY, newData, null, tags);
	}
}
