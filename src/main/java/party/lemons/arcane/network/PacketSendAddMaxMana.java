package party.lemons.arcane.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import party.lemons.arcane.api.capability.PlayerData;
import party.lemons.arcane.api.spell.SpellUtil;

/**
 * Created by Sam on 11/12/2017.
 */
public class PacketSendAddMaxMana implements IMessage
{
	public PacketSendAddMaxMana(){}

	@Override
	public void fromBytes(ByteBuf buf)
	{
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
	}

	public static class Handler implements IMessageHandler<PacketSendAddMaxMana, IMessage>
	{

		@Override
		public IMessage onMessage(PacketSendAddMaxMana message, MessageContext ctx)
		{
			ctx.getServerHandler().player.getServerWorld().addScheduledTask(() ->
			{
				PlayerData data = ctx.getServerHandler().player.getCapability(PlayerData.CAPABILITY, null);
				if(ctx.getServerHandler().player.experienceLevel >= 10)
				{
					ctx.getServerHandler().player.addExperienceLevel(-10);
					data.setMaxMana(data.getMaxMana() + 5);
				}
				SpellUtil.syncData(ctx.getServerHandler().player);
			});

			return null;
		}
	}
}
