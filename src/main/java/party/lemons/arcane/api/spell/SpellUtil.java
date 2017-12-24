package party.lemons.arcane.api.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import party.lemons.arcane.api.capability.PlayerData;
import party.lemons.arcane.network.*;

/**
 * Created by Sam on 14/12/2017.
 */
public class SpellUtil
{
	public static void castSpellPress(Spell spell, EntityPlayer player)
	{
		spell.castSpellPress(spell, player);

		if(!player.world.isRemote)
			ArcaneNetwork.NETWORK_INSTANCE.sendTo(new PacketSyncCurrentMana(player.getCapability(PlayerData.CAPABILITY, null).getMana()), (EntityPlayerMP) player);
	}

	public static void castSpellHold(Spell spell, EntityPlayer player, long holdTime)
	{
		if(player.getCapability(PlayerData.CAPABILITY, null).hasSpellUnlocked(spell))
			spell.castSpellHold(spell, player, holdTime);

		if(!player.world.isRemote)
			ArcaneNetwork.NETWORK_INSTANCE.sendTo(new PacketSyncCurrentMana(player.getCapability(PlayerData.CAPABILITY, null).getMana()), (EntityPlayerMP) player);
	}

	public static void castSpellHoldRelease(Spell spell, EntityPlayer player, long holdTime)
	{
		spell.castSpellHoldRelease(spell, player, holdTime);

		if(!player.world.isRemote)
			ArcaneNetwork.NETWORK_INSTANCE.sendTo(new PacketSyncCurrentMana(player.getCapability(PlayerData.CAPABILITY, null).getMana()), (EntityPlayerMP) player);
	}

	public static void syncData(EntityPlayer player)
	{
		if(player.world.isRemote)
			return;

		PlayerData newData = player.getCapability(PlayerData.CAPABILITY, null);

		EntityPlayerMP pl = (EntityPlayerMP) player;
		for(Spell sp : newData.getUnlockedSpells())
		{
			ArcaneNetwork.NETWORK_INSTANCE.sendTo(new PacketUnlockSpell(sp.getRegistryName().toString()), pl);
		}
		for(int i = 0; i < newData.getSelectedSpells().length; i++)
		{
			Spell sp = newData.getSelectedSpells()[i];
			if(sp != null)
			{
				newData.setSelectedSpell(sp, i);
				ArcaneNetwork.NETWORK_INSTANCE.sendTo(new PacketSyncSelectedSpell(sp.getRegistryName().toString(), i), pl);
			}
		}
		ArcaneNetwork.NETWORK_INSTANCE.sendTo(new PacketSyncSelectedIndex(newData.getSelectedIndex()), pl);
		ArcaneNetwork.NETWORK_INSTANCE.sendTo(new PacketSyncMaxMana(newData.getMaxMana()), pl);
		ArcaneNetwork.NETWORK_INSTANCE.sendTo(new PacketSyncCurrentMana(newData.getMana()), pl);
		ArcaneNetwork.NETWORK_INSTANCE.sendTo(new PacketSyncStoredLevels(newData.getStoredLevels()), pl);
		ArcaneNetwork.NETWORK_INSTANCE.sendTo(new PacketSyncActionState(newData.getActionState()), pl);

		//ArcaneNetwork.NETWORK_INSTANCE.sendTo(new PacketClientDebugData(), pl);
	}
}
