package party.lemons.magicmod.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import party.lemons.magicmod.api.capability.PlayerData;

/**
 * Created by Sam on 11/12/2017.
 */
public class PacketSyncStoredLevels implements IMessage
{
	public PacketSyncStoredLevels(){}

	private int level;

	public PacketSyncStoredLevels(int level)
	{
		this.level = level;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		level = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(level);
	}

	public static class Handler implements IMessageHandler<PacketSyncStoredLevels, IMessage>
	{

		@Override
		public IMessage onMessage(PacketSyncStoredLevels message, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(() ->
			{
				EntityPlayerSP cp = Minecraft.getMinecraft().player;
				if(cp.hasCapability(PlayerData.CAPABILITY, null))
				{
					PlayerData caster = cp.getCapability(PlayerData.CAPABILITY, null);
					caster.setStoredLevels(message.level);
				}
			});
			return null;
		}
	}
}
