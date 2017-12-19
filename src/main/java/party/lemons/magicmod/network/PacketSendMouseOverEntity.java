package party.lemons.magicmod.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import party.lemons.magicmod.api.capability.PlayerData;

/**
 * Created by Sam on 14/12/2017.
 */
public class PacketSendMouseOverEntity implements IMessage
{
	public PacketSendMouseOverEntity(){}

	private int id;

	public PacketSendMouseOverEntity(int index)
	{
		this.id = index;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		id = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(id);
	}

	public static class Handler implements IMessageHandler<PacketSendMouseOverEntity, IMessage>
	{

		@Override
		public IMessage onMessage(PacketSendMouseOverEntity message, MessageContext ctx)
		{
			EntityPlayerMP serverPlayer = ctx.getServerHandler().player;

			serverPlayer.getServerWorld().addScheduledTask(() ->
			{
				if(message.id == -1)
				{
					PlayerData caster = serverPlayer.getCapability(PlayerData.CAPABILITY, null);
					caster.setLookEntity(null);
				}
				else
				{
					Entity theEntity = serverPlayer.world.getEntityByID(message.id);
					PlayerData caster = serverPlayer.getCapability(PlayerData.CAPABILITY, null);
					caster.setLookEntity(theEntity);
				}

			});
			return null;
		}
	}
}
