package party.lemons.magicmod.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import party.lemons.magicmod.api.capability.PlayerData;
import party.lemons.magicmod.api.spell.Spell;

/**
 * Created by Sam on 11/12/2017.
 */
public class PacketClientDebugData implements IMessage
{
	public PacketClientDebugData(){}

	@Override
	public void fromBytes(ByteBuf buf)
	{
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
	}

	public static class Handler implements IMessageHandler<PacketClientDebugData, IMessage>
	{

		@Override
		public IMessage onMessage(PacketClientDebugData message, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(() ->
			{
				EntityPlayerSP cp = Minecraft.getMinecraft().player;
				PlayerData caster = cp.getCapability(PlayerData.CAPABILITY, null);

				System.out.println("---Unlocked Spells---");
				for(Spell sp : caster.getUnlockedSpells())
				{
					System.out.println("- " + sp.getRegistryName());
				}
				System.out.println("---Selected Spells---");
				for(int i = 0; i < 6; i++)
				{
					Spell sp = caster.getSelectedSpells()[i];
					String out = i + "- ";
					if(sp != null)
					{
						out += sp.getRegistryName();
					}
					System.out.print(out);
				}
				System.out.println("---Mana---");
				System.out.println("current- " + caster.getMana());
				System.out.println("max- " + caster.getMaxMana());
				System.out.println("---Other---");
				System.out.println("selected index- " + caster.getSelectedIndex());
			});
			return null;
		}
	}
}
